package ru.toxsoft.mcc.ws.journals.e4.uiparts.engine;

import static ru.toxsoft.mcc.ws.journals.e4.uiparts.engine.IMmResources.*;

import org.eclipse.jface.viewers.*;
import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.tsnodes.*;
import org.toxsoft.core.tsgui.utils.layout.*;
import org.toxsoft.core.tsgui.widgets.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.uskat.base.gui.conn.*;
import org.toxsoft.uskat.core.*;
import org.toxsoft.uskat.core.api.objserv.*;
import org.toxsoft.uskat.core.connection.*;

/**
 * Дерево объектов системы, построенное по модели {@link ILibSkObjectsTreeModel}
 *
 * @author max, dima
 */
public class ObjectsTreeComposite
    extends TsComposite {

  static final String OBJECT_NODE_KIND_ID = "object"; //$NON-NLS-1$

  static final ITsNodeKind<ISkObject> skObjectKind =
      new TsNodeKind<>( OBJECT_NODE_KIND_ID, "Object", "System object", ISkObject.class, true, null ); //$NON-NLS-1$ //$NON-NLS-2$

  /**
   * Дерево имеющихся в наличии данных (команды или события) для выбора.
   */
  TsTreeViewerCheck treeViewerCheck;

  /**
   * Контекст.
   */
  ITsGuiContext context;

  /**
   * Список классов, объекты которых во первых отображаются в дереве (не только как промежуточные) и могут мыть выбраны
   * как отдельные сущности.
   */
  IStringList filterList = new StringArrayList();

  /**
   * Модель объектов.
   */
  private ILibSkObjectsTreeModel treeModel;

  /**
   * Конструктор.
   *
   * @param aParent Composite - родительский компонент.
   * @param aContext ITsGuiContext - контекст.
   * @param aTitle String - название в тултипе.
   */
  public ObjectsTreeComposite( Composite aParent, ITsGuiContext aContext, String aTitle ) {
    super( aParent, SWT.NONE );
    setLayout( new BorderLayout() );
    context = aContext;

    // в зависимости от фильтра - подсвечивание родительских узлов варьируется
    treeViewerCheck = new TsTreeViewerCheck( aContext ) {

      @Override
      public void ensureParentChecked( ITsNode aChecked ) {
        ITsNode parent = aChecked.parent();
        while( parent != null ) {
          boolean hasChecked = false;

          for( ITsNode child : parent.childs() ) {
            if( treeViewerCheck.getChecked( child ) ) {
              hasChecked = true;
            }
          }

          if( !hasChecked && parent.entity() instanceof ISkObject
              && filterList.hasElem( ((ISkObject)parent.entity()).classId() ) ) {
            break;
          }

          treeViewerCheck.setChecked( parent, hasChecked );

          parent = parent.parent();
        }
      }
    };

    treeViewerCheck.createControl( this ).setLayoutData( BorderLayout.CENTER );

    // Создаем панель для toolBar
    TsTreeViewerCheckToolbar evTopPanel = new TsTreeViewerCheckToolbar( this, context, treeViewerCheck, aTitle );
    evTopPanel.setLayoutData( BorderLayout.NORTH );

    // Create Filter Text Box
    Text searchText = new Text( this, SWT.SEARCH | SWT.CANCEL );
    searchText.setMessage( STR_SEARCH );
    searchText.setLayoutData( BorderLayout.SOUTH );
    // Add Listener to the Filter Text Box
    searchText.addModifyListener( e -> filterTreeModel( searchText.getText() ) );
  }

  //
  // --------------------------------------------------------------------------------
  // API добавления модели, фильтров классов и выделенных объектов

  /**
   * Устанавливает объекты, узлы которых должны быть выделены (галочками)
   *
   * @param aCheckedObject ILongList - идентификаторы объектов, узлы которых должны быть выделены (галочками)
   */
  public void setCheckedObjects( IStringList aCheckedObject ) {
    setChecked( treeViewerCheck.root(), aCheckedObject );
    treeViewerCheck.ensureGrayedState();
    treeViewerCheck.fireCommonCheckStateChanged();
  }

  /**
   * Возвращает список выделенных галочками объектов.
   *
   * @return IList<ISkObject> - список выделенных галочками объектов.
   */
  public IList<ISkObject> getCheckedObjects() {
    Object[] checked = treeViewerCheck.getCheckedElements();

    IListEdit<ISkObject> result = new ElemArrayList<>();
    for( Object checkObj : checked ) {
      if( checkObj instanceof SkObjectTsNode && treeViewerCheck.getChecked( (ITsNode)checkObj ) ) {

        ISkObject checkObject = (ISkObject)((ITsNode)checkObj).entity();

        result.add( checkObject );
      }
    }

    return result;
  }

  /**
   * Устанавливает модель дерева объектов и список фильтрованных классов.
   *
   * @param aTreeModel IObjectsTreeModel - модель дерева объектов.
   * @param aFilterClassesList IStringList - список фильтрованных классов.
   */
  public void setTreeModel( ILibSkObjectsTreeModel aTreeModel, IStringList aFilterClassesList ) {
    filterList = aFilterClassesList;
    treeModel = aTreeModel;

    IListEdit<ITsNode> roots = new ElemArrayList<>();

    ISkCoreApi coreApi = context.get( ISkConnectionSupplier.class ).defConn().coreApi();
    ISkObjectService objService = coreApi.objService();
    for( String clsId : aFilterClassesList ) {
      for( ISkObject obj : objService.listObjs( clsId, true ) ) {
        ITsNode rootNode = createNodeForSkObject( treeViewerCheck, obj, true );
        if( rootNode == null ) {
          System.out.println( "NULL ROOT FOR OBJ CLASS: " + obj.classId() ); //$NON-NLS-1$
        }
        else {
          roots.add( rootNode );
        }
      }
    }
    treeViewerCheck.setRootNodes( roots );
  }

  /**
   * Фильтрует модель дерева объектов.
   *
   * @param aText2Filter тект для фильтра
   */
  public void filterTreeModel( String aText2Filter ) {
    String search4 = aText2Filter.strip();

    IListEdit<ITsNode> roots = new ElemArrayList<>();

    ISkCoreApi coreApi = context.get( ISkConnection.class ).coreApi();
    ISkObjectService objService = coreApi.objService();
    for( String clsId : filterList ) {
      for( ISkObject obj : objService.listObjs( clsId, true ) ) {
        String name = obj.readableName();
        if( search4.length() > 0 && !name.contains( search4 ) ) {
          continue;
        }
        ITsNode rootNode = createNodeForSkObject( treeViewerCheck, obj, true );
        if( rootNode == null ) {
          System.out.println( "NULL ROOT FOR OBJ CLASS: " + obj.classId() ); //$NON-NLS-1$
        }
        else {
          roots.add( rootNode );
        }
      }
    }
    treeViewerCheck.setRootNodes( roots );
  }

  /**
   * Возвращает список классов, объекты которых считаются выбранными, если на их узлах стоят галочки.
   *
   * @return IStringList - список идентификаторов классов, объекты которых считаются выбранными, если на их узлах стоят
   *         галочки.
   */
  public IStringList getFilterClasses() {
    return filterList;
  }

  //
  // --------------------------------------------------------------------------------
  // вспомогательные методы добавления модели, фильтров классов и выделенных объектов

  private void setChecked( ITsNode aParent, IStringList aCheckedObject ) {
    if( aParent.entity() instanceof ISkObject && aCheckedObject.hasElem( ((ISkObject)aParent.entity()).strid() ) ) {
      treeViewerCheck.setChecked( aParent, true );
      treeViewerCheck.ensureParentChecked( aParent );
    }

    for( ITsNode child : aParent.childs() ) {
      setChecked( child, aCheckedObject );
    }
  }

  private ITsNode createNodeForSkObject( ITsNode aParenNode, ISkObject aObject, boolean aIsRoot ) {

    final boolean isFiltered = filterList.hasElem( aObject.classId() );

    DefaultTsNode<ISkObject> objectNode = new SkObjectTsNode( aParenNode, aObject, isFiltered );

    IList<ISkObject> objs = treeModel.getChildren( aObject );

    for( ISkObject child : objs ) {
      ITsNode childNode = createNodeForSkObject( objectNode, child, false );
      if( childNode != null && !objectNode.childs().hasElem( childNode ) ) {
        objectNode.addNode( childNode );
      }
    }

    if( !isFiltered && !aIsRoot && objectNode.childs().size() == 0 ) {
      return null;
    }

    return objectNode;
  }

  //
  // -----------------------------------------------------------------------
  // API добавления и удаления слушателей событий установки чек-боксов

  /**
   * Удаляет слушателя событий изменения состояния выбора узлов дерева.
   *
   * @param listener ICheckStateListener - слушатель.
   */
  public void removeCheckStateListener( ICheckStateListener listener ) {
    treeViewerCheck.removeCheckStateListener( listener );
  }

  /**
   * Добавляет слушателя событий изменения состояния выбора узлов дерева.
   *
   * @param listener ICheckStateListener - слушатель.
   */
  public void addCheckStateListener( ICheckStateListener listener ) {
    treeViewerCheck.addCheckStateListener( listener );
  }

  /**
   * Класс узла дерева, содержащий в качестве пользовательского объекта - объект системы.
   *
   * @author max
   */
  static class SkObjectTsNode
      extends DefaultTsNode<ISkObject> {

    private int hashCode = 0;

    private boolean isFiltered;

    public SkObjectTsNode( ITsNode aParent, ISkObject aEntity, boolean aIsFiltered ) {
      super( skObjectKind, aParent, aEntity );
      isFiltered = aIsFiltered;
    }

    @Override
    protected String doGetName() {
      // отображение, зависящее от присутствия класса в фильтре
      return String.format( isFiltered ? "<%s [%s-%s]>" : "%s [%s-%s]", entity().readableName(), entity().classId(), //$NON-NLS-1$ //$NON-NLS-2$
          entity().strid() );
    }

    @Override
    public int hashCode() {
      if( hashCode == 0 ) {
        hashCode = 31 * entity().hashCode();
        if( parent() != null ) {
          hashCode += parent().hashCode();
        }
      }
      return hashCode;
    }

    @SuppressWarnings( "rawtypes" )
    @Override
    public boolean equals( Object obj ) {
      if( !(obj instanceof DefaultTsNode node) ) {
        return false;
      }

      if( !entity().equals( node.entity() ) ) {
        return false;
      }

      if( parent() == null ) {
        return node.parent() == null;
      }

      return parent().equals( node.parent() );
    }
  }
}
