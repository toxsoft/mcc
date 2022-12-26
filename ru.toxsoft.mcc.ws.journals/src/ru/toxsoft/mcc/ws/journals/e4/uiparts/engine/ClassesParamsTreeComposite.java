package ru.toxsoft.mcc.ws.journals.e4.uiparts.engine;

import org.eclipse.jface.viewers.*;
import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.tsnodes.*;
import org.toxsoft.core.tsgui.utils.layout.*;
import org.toxsoft.core.tsgui.widgets.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.uskat.core.api.sysdescr.*;
import org.toxsoft.uskat.core.api.sysdescr.dto.*;

/**
 * Дерево классов системы и связанных с ними параметров, построенное по модели {@link ILibClassInfoesTreeModel}
 *
 * @author max, dima
 * @param <T> - класс параметра класса
 */
public class ClassesParamsTreeComposite<T extends IDtoClassPropInfoBase>
    extends TsComposite {

  private static final String PARAM_NODE_KIND_ID = "param"; //$NON-NLS-1$

  private static final String CLASS_NODE_KIND_ID = "class"; //$NON-NLS-1$

  static ITsNodeKind<ISkClassInfo> classKind =
      new TsNodeKind<>( CLASS_NODE_KIND_ID, "Class", "System class", ISkClassInfo.class, true, null ); //$NON-NLS-1$ //$NON-NLS-2$

  static ITsNodeKind<IDtoClassPropInfoBase> paramKind =
      new TsNodeKind<>( PARAM_NODE_KIND_ID, "Param", "System class param", IDtoClassPropInfoBase.class, false, null ); //$NON-NLS-1$ //$NON-NLS-2$

  /**
   * Модель дерева классов и их параметров.
   */
  private ILibClassInfoesTreeModel<T> classesModel;

  /**
   * Дерево имеющихся в наличии данных (команды или события) для выбора.
   */
  TsTreeViewerCheck treeViewerCheck;

  /**
   * Контекст.
   */
  ITsGuiContext context;

  /**
   * Конструктор.
   *
   * @param aParent Composite - родительский компонент.
   * @param aContext ITsGuiContext - контекст.
   * @param aTitle String - название в тултипе.
   */
  public ClassesParamsTreeComposite( Composite aParent, ITsGuiContext aContext, String aTitle ) {
    super( aParent, SWT.NONE );
    setLayout( new BorderLayout() );
    context = aContext;

    treeViewerCheck = new TsTreeViewerCheck( aContext );

    treeViewerCheck.createControl( this ).setLayoutData( BorderLayout.CENTER );

    // Создаем панель для toolBar
    TsTreeViewerCheckToolbar evTopPanel = new TsTreeViewerCheckToolbar( this, context, treeViewerCheck, aTitle );

    evTopPanel.setLayoutData( BorderLayout.NORTH );
  }

  //
  // --------------------------------------------------------------------------------
  // API добавления модели, выделенных классов и параметров

  /**
   * Устанавливает выделенные классы и параметры этих классов.
   *
   * @param aCheckedClasses IMap<ISkClassInfo, IList<ISkClassPropInfoBase>> - выделенные классы и параметры этих
   *          классов.
   */
  public void setCheckedClassInfoesAndParams( IMap<ISkClassInfo, IList<IDtoClassPropInfoBase>> aCheckedClasses ) {

    for( ISkClassInfo checkClass : aCheckedClasses.keys() ) {

      // найти узел
      IList<ITsNode> roots = treeViewerCheck.root().childs();
      for( ITsNode rootNode : roots ) {
        if( ((ISkClassInfo)rootNode.entity()).id().equals( checkClass.id() ) ) {
          treeViewerCheck.setChecked( rootNode, true );

          IList<IDtoClassPropInfoBase> checkParams = aCheckedClasses.getByKey( checkClass );

          for( IDtoClassPropInfoBase param : checkParams ) {
            for( ITsNode child : rootNode.childs() ) {
              if( ((IDtoClassPropInfoBase)child.entity()).id().equals( param.id() ) ) {
                treeViewerCheck.setChecked( child, true );
                break;
              }
            }
          }
          break;
        }
      }
    }

    treeViewerCheck.ensureGrayedState();
  }

  /**
   * Возвращает выделенные классы и параметры этих классов.
   *
   * @return IMap<ISkClassInfo, IList<ISkClassPropInfoBase>> - выделенные классы и параметры этих классов.
   */
  public IMap<ISkClassInfo, IList<IDtoClassPropInfoBase>> getCheckedClassInfoesAndParams() {
    Object[] checked = treeViewerCheck.getCheckedElements();

    IMapEdit<ISkClassInfo, IList<IDtoClassPropInfoBase>> result = new ElemMap<>();
    for( Object checkObj : checked ) {
      if( ((ITsNode)checkObj).kind() == classKind ) {

        ISkClassInfo checkClass = (ISkClassInfo)((ITsNode)checkObj).entity();

        IListEdit<IDtoClassPropInfoBase> checkParams = new ElemArrayList<>();

        result.put( checkClass, checkParams );

        for( ITsNode child : ((ITsNode)checkObj).childs() ) {
          if( child.kind() == paramKind && treeViewerCheck.getChecked( child ) ) {
            checkParams.add( (IDtoClassPropInfoBase)child.entity() );
          }
        }
      }
    }

    return result;
  }

  /**
   * Устанавливает модель кассов и их параметров
   *
   * @param aClassesModel IClassesInfoTreeModel - модель кассов и их параметров
   */
  public void setClassesModel( ILibClassInfoesTreeModel<T> aClassesModel ) {
    classesModel = aClassesModel;

    IListEdit<ITsNode> roots = new ElemArrayList<>();

    for( ISkClassInfo classInfo : classesModel.getRootClasses() ) {
      // TODO: 2020-05-19 mvkd ??? в базе может быть очень много объектов ISkSession. Диалог виснет
      // Dima, 2020-11-04 иначе пустой список классов событий для администратора
      // if( classInfo.id().equals( ISkSession.CLASS_ID ) ) {
      // continue;
      // }
      ITsNode root = createNodeForClass( treeViewerCheck, classInfo );
      if( root != null ) {
        roots.add( root );
      }
    }
    treeViewerCheck.setRootNodes( roots );
  }

  //
  // --------------------------------------------------------------------------------
  // вспомогательные методы добавления модели, выделенных классов и параметров

  private ITsNode createNodeForClass( ITsNode aParenNode, ISkClassInfo aObject ) {
    DefaultTsNode<ISkClassInfo> classNode = new DefaultTsNode<>( classKind, aParenNode, aObject ) {

      @Override
      protected String doGetName() {
        return String.format( "%s [%s]", entity().nmName(), entity().id() ); //$NON-NLS-1$
      }

    };

    addClassDataNodes( aObject, classNode );

    if( classNode.childs().size() == 0 ) {
      return null;
    }
    return classNode;
  }

  private boolean addClassDataNodes( ISkClassInfo objClass, DefaultTsNode<?> objectNode ) {
    IList<? extends IDtoClassPropInfoBase> paramInfoes = classesModel.getParamsInfo( objClass );

    if( paramInfoes.size() == 0 ) {
      return false;
    }

    for( IDtoClassPropInfoBase paramInfo : paramInfoes ) {

      ITsNode dataNode = new DefaultTsNode<>( paramKind, objectNode, paramInfo ) {

        @Override
        protected String doGetName() {
          return String.format( "%s", entity().nmName() ); //$NON-NLS-1$
        }
      };

      objectNode.addNode( dataNode );

    }
    return true;
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
}
