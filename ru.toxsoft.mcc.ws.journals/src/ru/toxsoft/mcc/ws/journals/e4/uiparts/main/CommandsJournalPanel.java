package ru.toxsoft.mcc.ws.journals.e4.uiparts.main;

import static ru.toxsoft.mcc.ws.journals.e4.uiparts.IMmFgdpLibCfgJournalsConstants.*;

import org.eclipse.jface.dialogs.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.dialogs.*;
import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.gui.panels.*;
import org.toxsoft.core.tsgui.m5.model.impl.*;
import org.toxsoft.core.tsgui.panels.*;
import org.toxsoft.core.tsgui.utils.layout.*;
import org.toxsoft.core.tslib.bricks.events.change.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.tslib.utils.logs.impl.*;
import org.toxsoft.uskat.base.gui.conn.*;
import org.toxsoft.uskat.core.api.cmdserv.*;
import org.toxsoft.uskat.core.api.sysdescr.*;
import org.toxsoft.uskat.core.api.sysdescr.dto.*;
import org.toxsoft.uskat.core.connection.*;

import ru.toxsoft.mcc.ws.journals.e4.uiparts.*;
import ru.toxsoft.mcc.ws.journals.e4.uiparts.engine.*;
import ru.toxsoft.mcc.ws.journals.e4.uiparts.engine.IJournalParamsPanel.*;

/**
 * Панель просмотра журнала команд
 *
 * @author dima
 */
public class CommandsJournalPanel
    extends TsPanel
    implements IGenericChangeListener {

  IM5CollectionPanel<IDtoCompletedCommand> panel = null;

  JournalParamsPanel paramsPanel = null;

  private CommandQueryEngine                           queryEngine;
  private M5DefaultItemsProvider<IDtoCompletedCommand> commandProvider;

  /**
   * @param aParent родительская панель
   * @param aContext контекст
   * @throws TsException - ошибка создания
   */
  public CommandsJournalPanel( Composite aParent, ITsGuiContext aContext )
      throws TsException {
    super( aParent, aContext );

    setLayout( new BorderLayout() );

    ISkConnection connection = aContext.get( ISkConnectionSupplier.class ).defConn();

    IM5Model<IDtoCompletedCommand> userModel = m5().getModel( CommandM5Model.MODEL_ID, IDtoCompletedCommand.class );
    // EventM5LifecycleManager lm =
    // (EventM5LifecycleManager)userModel.getLifecycleManager( windowContext().get( IEventService.class ) );
    commandProvider = new M5DefaultItemsProvider<>();
    panel = userModel.panelCreator().createCollViewerPanel( aContext, commandProvider );
    panel.createControl( this ).setLayoutData( BorderLayout.CENTER );

    queryEngine = new CommandQueryEngine( connection.coreApi() );

    JournalsLibUtils.loadClassesTreeModel( aContext, COMMANDS_FILTER_CLASSES_TREE_MODEL_LIB );

    paramsPanel = new JournalParamsPanel( aContext );
    paramsPanel.addListener( this );

    JournalsLibUtils.loadObjectsTreeModel( aContext, COMMANDS_FILTER_OBJECTS_TREE_MODEL_LIB );

    paramsPanel.setAppContext( aContext.eclipseContext() );

    // панель инструментов сверху
    paramsPanel.createControl( this );
    paramsPanel.getControl().setLayoutData( BorderLayout.NORTH );
    // настройка взаимодействия между компонентами
    // paramsPanel.addGenericChangeListener( queryStartListener );
    // eventProvider.items().addAll( lm.doListEntities() );

    panel.refresh();
  }

  /**
   * Возвращает домен моделирования сущностей приложения.
   *
   * @return {@link IM5Domain} - домен моделирования сущностей приложения
   */
  @Override
  public IM5Domain m5() {
    IM5Domain d = eclipseContext().get( IM5Domain.class );
    TsIllegalStateRtException.checkNull( d, "No domain in context" ); //$NON-NLS-1$
    return d;
  }

  @Override
  public void onGenericChangeEvent( Object aSource ) {
    IJournalParamsPanel journalPanel = (IJournalParamsPanel)aSource;
    if( journalPanel.currentAction() == ECurrentAction.QUERY_ALL ) {
      queryAllEvents();
    }
    if( journalPanel.currentAction() == ECurrentAction.QUERY_SELECTED ) {
      querySelectedEvents();
    }
  }

  private void queryAllEvents() {
    try {
      queryEngine.setQueryParams( paramsPanel.interval(), allCommandsParams() );
      new ProgressMonitorDialog( getShell() ).run( true, true, queryEngine );
      commandProvider.items().clear();
      commandProvider.items().addAll( queryEngine.getResult() );
      panel.refresh();
    }
    catch( Exception ex ) {
      ex.printStackTrace();
      LoggerUtils.errorLogger().error( ex );
      TsDialogUtils.error( getShell(), ex );
      commandProvider.items().clear();
      panel.refresh();
    }
  }

  private void querySelectedEvents() {
    try {
      // Поскольку метод queryEvents воспринимает пустые списки параметров и/или объектов как
      // "запросить все и по всем"
      // то тут проредим параметры фильтрованного запроса
      ConcerningEventsParams selEvents = new ConcerningEventsParams();
      for( ConcerningEventsItem item : ((ConcerningEventsParams)paramsPanel.selectedParams()).eventItems() ) {
        if( item.eventIds().isEmpty() || item.strids().isEmpty() ) {
          continue;
        }
        selEvents.addItem( item );
      }
      queryEngine.setQueryParams( paramsPanel.interval(), selEvents );
      new ProgressMonitorDialog( getShell() ).run( true, true, queryEngine );
      commandProvider.items().clear();
      commandProvider.items().addAll( queryEngine.getResult() );
      panel.refresh();
    }
    catch( Exception ex ) {
      ex.printStackTrace();
      LoggerUtils.errorLogger().error( ex );
      TsDialogUtils.error( getShell(), ex );
      commandProvider.items().clear();
      panel.refresh();
    }
  }

  /**
   * @return параметры запроса на все события
   */
  IConcerningEventsParams allCommandsParams() {
    ConcerningEventsParams retVal = new ConcerningEventsParams();
    for( ISkClassInfo classInfo : listNeededClasses() ) {
      IStringListEdit cmdsIds = new StringArrayList();

      for( IDtoCmdInfo cmd : classInfo.cmds().list() ) {
        cmdsIds.add( cmd.id() );
      }

      ConcerningEventsItem evItem = new ConcerningEventsItem( classInfo.id(), cmdsIds, IStringList.EMPTY );
      retVal.addItem( evItem );
    }
    return retVal;
  }

  @SuppressWarnings( "unchecked" )
  protected IStridablesList<ISkClassInfo> listNeededClasses() {
    ILibClassInfoesTreeModel<IDtoCmdInfo> classModel =
        (ILibClassInfoesTreeModel<IDtoCmdInfo>)tsContext().get( FILTER_CLASSES_TREE_MODEL_LIB );
    return classModel.getRootClasses();
  }
}
