package ru.toxsoft.mcc.ws.journals.e4.uiparts.engine;

import static ru.toxsoft.mcc.ws.journals.e4.uiparts.engine.IMmResources.*;

import org.eclipse.e4.core.contexts.*;
import org.eclipse.jface.viewers.*;
import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.tsnodes.*;
import org.toxsoft.core.tsgui.dialogs.*;
import org.toxsoft.core.tsgui.dialogs.datarec.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.uskat.core.api.objserv.*;
import org.toxsoft.uskat.core.api.sysdescr.*;
import org.toxsoft.uskat.core.api.sysdescr.dto.*;

import ru.toxsoft.mcc.ws.journals.e4.uiparts.*;

/**
 * Диалог редатирования {@link IConcerningEventsParams}.
 *
 * @author goga, dima
 */
public class DialogConcerningEventsParams
    extends AbstractTsDialogPanel<ConcerningEventsParams, ITsGuiContext>
    implements ICheckStateListener {

  /**
   * Дерево классов и параметров классов.
   */
  ClassesParamsTreeComposite classesTree;

  /**
   * Дерево объектов
   */
  ObjectsTreeComposite objectsTree;

  /**
   * Параметры запроса на сервер
   */
  ConcerningEventsParams eventsParams;

  /**
   * Модель объектов.
   */
  ILibSkObjectsTreeModel model;

  /**
   * Модель дерева классов и параметров классов.
   */
  ILibClassInfoesTreeModel<?> classesModel;

  /**
   * Конcтруктор для встраивания в диалог.
   *
   * @param aParent {@link Composite} - родительская панель
   * @param aOwnerDialog {@link CommonDialogBase} - родительский диалог
   */
  protected DialogConcerningEventsParams( Composite aParent,
      TsDialog<ConcerningEventsParams, ITsGuiContext> aOwnerDialog ) {
    super( aParent, aOwnerDialog );

    setLayout( new FillLayout() );
    SashForm sf = new SashForm( this, SWT.VERTICAL );

    // дерево классов
    createClassesTreeViewer( sf );

    // дерево объектов
    createObjectsTreeViewer( sf );

    sf.setWeights( 630, 630 );
  }

  private void createClassesTreeViewer( SashForm aSashForm ) {

    classesTree = new ClassesParamsTreeComposite( aSashForm, tsContext(), CLASSES_N_PARAMS_TREE_TITLE );
    classesTree.addCheckStateListener( event -> {

      IMap<ISkClassInfo, IList<IDtoClassPropInfoBase>> classes = classesTree.getCheckedClassInfoesAndParams();
      IStringList currFilterList = objectsTree.getFilterClasses();
      IStringListEdit filterList = new StringArrayList( false );
      boolean equales = true;
      for( ISkClassInfo chClass : classes.keys() ) {
        filterList.add( chClass.id() );

        equales = equales && currFilterList.hasElem( chClass.id() );
      }

      equales = equales && currFilterList.size() == filterList.size();
      if( equales ) {
        System.out.println( "EQUALS" ); //$NON-NLS-1$
        if( event.getElement() instanceof ITsNode
            && ((ITsNode)event.getElement()).kind() == ClassesParamsTreeComposite.paramKind ) {
          ISkClassInfo classInfo = (ISkClassInfo)((ITsNode)event.getElement()).parent().entity();
          ConcerningEventsItem item = eventsParams.getItem( classInfo.id() );
          eventsParams.removeItem( item );
          IStringListEdit events = new StringArrayList();
          for( IDtoClassPropInfoBase param : classes.getByKey( classInfo ) ) {
            events.add( param.id() );
          }
          eventsParams.addItem( new ConcerningEventsItem( classInfo.id(), events, item.strids() ) );
        }
        return;
      }

      objectsTree.setTreeModel( model, filterList );
    } );
  }

  private void createObjectsTreeViewer( SashForm aSashForm ) {
    objectsTree = new ObjectsTreeComposite( aSashForm, tsContext(), ENTITY_TREE_TITLE );
    objectsTree.addCheckStateListener( this );
  }

  @Override
  protected void doSetDataRecord( ConcerningEventsParams aData ) {
    // Запоминаем переданные данные
    eventsParams = aData;
    // Отображаем список классов
    classesModel =
        (ILibClassInfoesTreeModel)tsContext().get( IMmFgdpLibCfgJournalsConstants.FILTER_CLASSES_TREE_MODEL_LIB );

    classesTree.setClassesModel( classesModel );

    IMapEdit<ISkClassInfo, IList<IDtoClassPropInfoBase>> checkedClasses = new ElemMap<>();

    IStringListEdit checkedObjects = new StringArrayList();

    for( ConcerningEventsItem item : aData.eventItems() ) {
      checkedObjects.addAll( item.strids() );
      ISkClassInfo checkClass = classesModel.getRootClasses().findByKey( item.classId() );

      if( checkClass != null ) {
        IListEdit<IDtoClassPropInfoBase> params = new ElemArrayList<>();

        checkedClasses.put( checkClass, params );

        for( String eventId : item.eventIds() ) {
          IDtoClassPropInfoBase sdInfo = classesModel.getParamsInfo( checkClass ).findByKey( eventId );

          if( sdInfo != null ) {
            params.add( sdInfo );
          }
        }
      }

    }
    classesTree.setCheckedClassInfoesAndParams( checkedClasses );

    IStringListEdit filterList = new StringArrayList( false );
    for( ISkClassInfo chClass : checkedClasses.keys() ) {
      filterList.add( chClass.id() );
    }

    model = (ILibSkObjectsTreeModel)tsContext().get( IMmFgdpLibCfgJournalsConstants.FILTER_OBJECTS_TREE_MODEL_LIB );
    model.init( tsContext() );

    objectsTree.setTreeModel( model, filterList );

    objectsTree.setCheckedObjects( checkedObjects );
  }

  @Override
  protected ConcerningEventsParams doGetDataRecord() {
    return eventsParams;
  }

  /**
   * Вызывается в момент изменения выбора в дереве объектов
   */
  @Override
  public void checkStateChanged( CheckStateChangedEvent event ) {
    // изменение выделенных объектов в дереве объектов
    IList<ISkObject> checkedObjects = objectsTree.getCheckedObjects();
    IMap<ISkClassInfo, IList<IDtoClassPropInfoBase>> classesParams = classesTree.getCheckedClassInfoesAndParams();

    eventsParams.clear();

    for( ISkClassInfo checkClass : classesParams.keys() ) {
      IList<IDtoClassPropInfoBase> checkParams = classesParams.getByKey( checkClass );

      IStringListEdit paramsIds = new StringArrayList();
      for( IDtoClassPropInfoBase sysdescrInfo : checkParams ) {
        paramsIds.add( sysdescrInfo.id() );
      }

      IStringListEdit strids = new StringArrayList();
      for( ISkObject obj : checkedObjects ) {
        if( obj.classId().equals( checkClass.id() ) ) {
          strids.add( obj.strid() );
        }
      }

      ConcerningEventsItem newItem = new ConcerningEventsItem( checkClass.id(), paramsIds, strids );
      eventsParams.addItem( newItem );
    }
    // System.out.println();
  }

  // ------------------------------------------------------------------------------------
  // Статический метод вызова диалога
  //

  /**
   * Выводит диалог редактирования параметров {@link ConcerningEventsParams}.
   *
   * @param aParams {@link ConcerningEventsParams} - исходные параметры или null для набора параметров "с нуля"
   * @param aContext {@link IEclipseContext} - контекст приложения (содержт все нужное для доступа к серверу)
   * @return {@link ConcerningEventsParams} - отредактированные параметры или null если пользователь отказался
   */
  public static ConcerningEventsParams edit( ConcerningEventsParams aParams, ITsGuiContext aContext ) {
    // в диалоге работа ведётся с копией
    // в случае отмены - вернётся null и будут действовать старые (пришедшие как атрибуты этого метода) параметры
    // фильтра
    // в случае принятия - вернётся редактированная копия, которая и заместить старую
    final ConcerningEventsParams params = copyParams( aParams );
    TsNullArgumentRtException.checkNull( aContext );
    Shell shell = aContext.get( Shell.class );
    try {
      TsDialogInfo cdi = new TsDialogInfo( aContext, shell, DLG_C_CEP_EDIT, DLG_T_CEP_EDIT, 0 );
      cdi.setMinSizeShellRelative( 30, 60 );
      cdi.setMaxSizeShellRelative( 50, 90 );
      IDialogPanelCreator<ConcerningEventsParams, ITsGuiContext> creator = DialogConcerningEventsParams::new;
      TsDialog<ConcerningEventsParams, ITsGuiContext> d = new TsDialog<>( cdi, params, aContext, creator );
      return d.execData();
    }
    catch( Exception e ) {
      e.printStackTrace();
      TsDialogUtils.error( shell, e );
      return null;
    }
  }

  private static ConcerningEventsParams copyParams( ConcerningEventsParams aEventsParams ) {
    ConcerningEventsParams copyParams = new ConcerningEventsParams();

    for( ConcerningEventsItem item : aEventsParams.eventItems() ) {
      ConcerningEventsItem copyItem = new ConcerningEventsItem( item.classId(), new StringArrayList( item.eventIds() ),
          new StringArrayList( item.strids() ) );
      copyParams.addItem( copyItem );
    }
    return copyParams;
  }
}
