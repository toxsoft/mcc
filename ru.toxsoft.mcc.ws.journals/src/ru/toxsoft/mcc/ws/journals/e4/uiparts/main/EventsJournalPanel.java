package ru.toxsoft.mcc.ws.journals.e4.uiparts.main;

import static ru.toxsoft.mcc.ws.journals.e4.uiparts.IMmFgdpLibCfgJournalsConstants.*;
import static ru.toxsoft.mcc.ws.journals.e4.uiparts.main.IMmResources.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.eclipse.swt.widgets.Composite;
import org.toxsoft.core.jasperreports.gui.main.*;
import org.toxsoft.core.tsgui.bricks.ctx.ITsGuiContext;
import org.toxsoft.core.tsgui.bricks.ctx.impl.TsGuiContext;
import org.toxsoft.core.tsgui.dialogs.TsDialogUtils;
import org.toxsoft.core.tsgui.m5.IM5Domain;
import org.toxsoft.core.tsgui.m5.IM5Model;
import org.toxsoft.core.tsgui.m5.gui.panels.IM5CollectionPanel;
import org.toxsoft.core.tsgui.m5.model.impl.M5DefaultItemsProvider;
import org.toxsoft.core.tsgui.panels.TsPanel;
import org.toxsoft.core.tsgui.utils.layout.BorderLayout;
import org.toxsoft.core.tslib.av.impl.AvUtils;
import org.toxsoft.core.tslib.av.metainfo.IDataDef;
import org.toxsoft.core.tslib.bricks.events.change.IGenericChangeListener;
import org.toxsoft.core.tslib.bricks.strid.coll.IStridablesList;
import org.toxsoft.core.tslib.coll.IList;
import org.toxsoft.core.tslib.coll.primtypes.IStringList;
import org.toxsoft.core.tslib.coll.primtypes.IStringListEdit;
import org.toxsoft.core.tslib.coll.primtypes.impl.StringArrayList;
import org.toxsoft.core.tslib.utils.errors.TsException;
import org.toxsoft.core.tslib.utils.errors.TsIllegalStateRtException;
import org.toxsoft.core.tslib.utils.logs.impl.LoggerUtils;
import org.toxsoft.uskat.base.gui.conn.ISkConnectionSupplier;
import org.toxsoft.uskat.core.api.evserv.SkEvent;
import org.toxsoft.uskat.core.api.sysdescr.ISkClassInfo;
import org.toxsoft.uskat.core.api.sysdescr.dto.IDtoEventInfo;
import org.toxsoft.uskat.core.api.users.ISkUser;
import org.toxsoft.uskat.core.connection.ISkConnection;
import org.toxsoft.uskat.s5.utils.S5ConnectionUtils;

import ru.toxsoft.mcc.ws.journals.e4.uiparts.JournalsLibUtils;
import ru.toxsoft.mcc.ws.journals.e4.uiparts.devel.DefaultMwsModJournalEventFormattersRegistry;
import ru.toxsoft.mcc.ws.journals.e4.uiparts.devel.IMwsModJournalEventFormattersRegistry;
import ru.toxsoft.mcc.ws.journals.e4.uiparts.engine.*;
import ru.toxsoft.mcc.ws.journals.e4.uiparts.engine.IJournalParamsPanel.ECurrentAction;

import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.type.HorizontalTextAlignEnum;

/**
 * Панель журнала событий.
 *
 * @author max
 * @author dima
 */
public class EventsJournalPanel
    extends TsPanel
    implements IGenericChangeListener {

  /**
   * формат для отображения метки времени
   */
  private static final String timestampFormatString = "dd.MM.yy HH:mm:ss"; //$NON-NLS-1$

  private static final DateFormat timestampFormat = new SimpleDateFormat( timestampFormatString );

  private static final String ERR_MSG_NO_DOMAIN_IN_CONTEXT = "No domain in context"; //$NON-NLS-1$

  private EventQueryEngine queryEngine;

  private M5DefaultItemsProvider<SkEvent> eventProvider;

  private IM5CollectionPanel<SkEvent> panel;

  private JournalParamsPanel paramsPanel;

  private IM5Model<SkEvent> eventsModel;

  /**
   * Конструктор.
   *
   * @param aParent - родительский компонент.
   * @param aContext - контекст.
   * @throws TsException - ошибка при формировании панели журнала событий.
   */
  public EventsJournalPanel( Composite aParent, ITsGuiContext aContext )
      throws TsException {
    super( aParent, aContext );
    init( aContext, EVENTS_FILTER_CLASSES_TREE_MODEL_LIB );
  }

  /**
   * Конструктор.
   *
   * @param aParent - родительский компонент.
   * @param aContext - контекст.
   * @param aFilteredClassesModelId отфильтрованный список классов
   * @throws TsException - ошибка при формировании панели журнала событий.
   */
  public EventsJournalPanel( Composite aParent, ITsGuiContext aContext, IDataDef aFilteredClassesModelId )
      throws TsException {
    super( aParent, aContext );
    init( aContext, aFilteredClassesModelId );
  }

  private void init( ITsGuiContext aContext, IDataDef aEventsFilterClassesTreeModelLib )
      throws TsException {
    if( !aContext.hasKey( IMwsModJournalEventFormattersRegistry.class ) ) {
      aContext.put( IMwsModJournalEventFormattersRegistry.class, new DefaultMwsModJournalEventFormattersRegistry() );
    }

    ISkConnection connection = aContext.get( ISkConnectionSupplier.class ).defConn();

    if( !m5().models().hasKey( EventM5Model.MODEL_ID ) ) {
      m5().addModel( new EventM5Model( connection, aContext ) );
    }

    setLayout( new BorderLayout() );

    eventsModel = m5().getModel( EventM5Model.MODEL_ID, SkEvent.class );

    eventProvider = new M5DefaultItemsProvider<>();
    panel = eventsModel.panelCreator().createCollViewerPanel( aContext, eventProvider );
    panel.createControl( this ).setLayoutData( BorderLayout.CENTER );

    queryEngine = new EventQueryEngine( aContext );

    JournalsLibUtils.loadClassesTreeModel( aContext, aEventsFilterClassesTreeModelLib );

    paramsPanel = new JournalParamsPanel( aContext );

    paramsPanel.addListener( this );

    JournalsLibUtils.loadObjectsTreeModel( aContext, EVENTS_FILTER_OBJECTS_TREE_MODEL_LIB );

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
    TsIllegalStateRtException.checkNull( d, ERR_MSG_NO_DOMAIN_IN_CONTEXT );
    return d;
  }

  /**
   * @return параметры запроса на все события
   */
  IConcerningEventsParams allEventsParams() {
    ConcerningEventsParams retVal = new ConcerningEventsParams();
    for( ISkClassInfo classInfo : listNeededClasses() ) {

      IStringListEdit eventsIds = new StringArrayList();

      for( IDtoEventInfo event : classInfo.events().list() ) {
        eventsIds.add( event.id() );
      }

      ConcerningEventsItem evItem = new ConcerningEventsItem( classInfo.id(), eventsIds, IStringList.EMPTY );
      retVal.addItem( evItem );
    }
    return retVal;
  }

  protected IStridablesList<ISkClassInfo> listNeededClasses() {
    @SuppressWarnings( "unchecked" )
    ILibClassInfoesTreeModel<IDtoEventInfo> classModel =
        (ILibClassInfoesTreeModel<IDtoEventInfo>)tsContext().get( FILTER_CLASSES_TREE_MODEL_LIB );
    return classModel.getRootClasses();
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
    if( journalPanel.currentAction() == ECurrentAction.PRINT ) {
      printEvents();
    }
    // if( journalPanel.currentAction() == ECurrentAction.EXPORT_EXCEL ) {
    // try {
    // new ProgressMonitorDialog( getShell() ).run( true, true, new XlsExportRunner() );
    // }
    // catch( Exception e ) {
    // LoggerUtils.errorLogger().error( e );
    // TsDialogUtils.error( getShell(), e );
    // }
    // }

    // if(journalPanel.currentAction() == ECurrentAction.SEARCH_IN_LIST){
    // String searchStr = paramsPanel.searchString();
    // IList<IDisplayableEvent> events = eventsProvider.list();
    // IListEdit<IDisplayableEvent> searchedEvents = new ElemArrayList<>();
    // for(IDisplayableEvent event: events){
    // String longDescr = event.longDescription();
    // if(longDescr.contains(searchStr)){
    // searchedEvents.add(event);
    // }
    // }
    // eventsProvider.setList(searchedEvents);
    // }

  }

  private void printEvents() {
    try {
      ISkConnectionSupplier connectionSup = eclipseContext().get( ISkConnectionSupplier.class );
      ISkConnection connection = connectionSup.defConn();
      EventM5Model printEventsModel = new EventM5Model( connection, tsContext(), true );

      m5().initTemporaryModel( printEventsModel );

      ITsGuiContext printContext = new TsGuiContext( tsContext() );

      long startTime = paramsPanel.interval().startTime();
      long endTime = paramsPanel.interval().endTime();

      String title = String.format( PRINT_EVENT_LIST_TITLE_FORMAT, timestampFormat.format( new Date( startTime ) ),
          timestampFormat.format( new Date( endTime ) ) );

      IJasperReportConstants.REPORT_TITLE_M5_ID.setValue( printContext.params(), AvUtils.avStr( title ) );

      // выясняем текущего пользователя

      ISkUser user = S5ConnectionUtils.getConnectedUser( connection.coreApi() );
      String userName = user.nmName().trim().length() > 0 ? user.nmName() : user.login();

      IJasperReportConstants.LEFT_BOTTOM_STR_M5_ID.setValue( printContext.params(),
          AvUtils.avStr( AUTHOR_STR + userName ) );
      IJasperReportConstants.RIGHT_BOTTOM_STR_M5_ID.setValue( printContext.params(),
          AvUtils.avStr( DATE_STR + timestampFormat.format( new Date() ) ) );

      printContext.params().setStr( IJasperReportConstants.REPORT_DATA_HORIZONTAL_TEXT_ALIGN_ID,
          HorizontalTextAlignEnum.LEFT.getName() );

      final JasperPrint jasperPrint =
          ReportGenerator.generateJasperPrint( printContext, printEventsModel, eventProvider );
      JasperReportDialog.showPrint( printContext, jasperPrint );
    }
    catch( Exception ex ) {
      LoggerUtils.errorLogger().error( ex );
      TsDialogUtils.error( getShell(), ex );
    }

  }

  private void queryAllEvents() {
    LoggerUtils.defaultLogger().info( "queryAllEvents(): start. queryEngine.query(...)" );

    IList<SkEvent> events = queryEngine.query( paramsPanel.interval(), allEventsParams() );

    LoggerUtils.defaultLogger().info( "queryAllEvents(): event size = %d", events.size() );

    LoggerUtils.defaultLogger().info( "queryAllEvents(): eventProvider.items().clear()" );
    eventProvider.items().clear();

    LoggerUtils.defaultLogger().info( "queryAllEvents(): eventProvider.items().addAll( events )" );
    eventProvider.items().addAll( events );

    LoggerUtils.defaultLogger().info( "queryAllEvents(): panel.refresh()" );
    panel.refresh();

    LoggerUtils.defaultLogger().info( "queryAllEvents(): finish" );
  }

  private void querySelectedEvents() {
    // Поскольку метод queryEvents воспринимает пустые списки параметров и/или объектов как
    // "запросить все и по всем" то тут проредим параметры фильтрованного запроса
    ConcerningEventsParams selEvents = new ConcerningEventsParams();
    for( ConcerningEventsItem item : ((ConcerningEventsParams)paramsPanel.selectedParams()).eventItems() ) {
      if( item.eventIds().isEmpty() || item.strids().isEmpty() ) {
        continue;
      }
      selEvents.addItem( item );
    }
    IList<SkEvent> events = queryEngine.query( paramsPanel.interval(), selEvents );
    eventProvider.items().clear();
    eventProvider.items().addAll( events );
    panel.refresh();
  }
}
