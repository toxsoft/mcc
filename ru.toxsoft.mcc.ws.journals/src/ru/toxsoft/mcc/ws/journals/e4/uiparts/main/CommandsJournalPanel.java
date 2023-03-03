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
import org.toxsoft.uskat.core.api.cmdserv.IDtoCompletedCommand;
import org.toxsoft.uskat.core.api.sysdescr.ISkClassInfo;
import org.toxsoft.uskat.core.api.sysdescr.dto.IDtoCmdInfo;
import org.toxsoft.uskat.core.api.users.ISkUser;
import org.toxsoft.uskat.core.connection.ISkConnection;
import org.toxsoft.uskat.s5.utils.S5ConnectionUtils;

import ru.toxsoft.mcc.ws.journals.e4.uiparts.JournalsLibUtils;
import ru.toxsoft.mcc.ws.journals.e4.uiparts.engine.*;
import ru.toxsoft.mcc.ws.journals.e4.uiparts.engine.IJournalParamsPanel.ECurrentAction;

import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.type.HorizontalTextAlignEnum;

/**
 * Панель просмотра журнала команд
 *
 * @author dima
 */
public class CommandsJournalPanel
    extends TsPanel
    implements IGenericChangeListener {

  /**
   * формат для отображения метки времени
   */
  private static final String timestampFormatString = "dd.MM.yy HH:mm:ss"; //$NON-NLS-1$

  private static final DateFormat timestampFormat = new SimpleDateFormat( timestampFormatString );

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

    IM5Model<IDtoCompletedCommand> userModel = m5().getModel( CommandM5Model.MODEL_ID, IDtoCompletedCommand.class );
    // EventM5LifecycleManager lm =
    // (EventM5LifecycleManager)userModel.getLifecycleManager( windowContext().get( IEventService.class ) );
    commandProvider = new M5DefaultItemsProvider<>();
    panel = userModel.panelCreator().createCollViewerPanel( aContext, commandProvider );
    panel.createControl( this ).setLayoutData( BorderLayout.CENTER );

    queryEngine = new CommandQueryEngine( aContext );

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
    if( journalPanel.currentAction() == ECurrentAction.PRINT ) {
      printCommands();
    }
  }

  private void queryAllEvents() {
    IList<IDtoCompletedCommand> commands = queryEngine.query( paramsPanel.interval(), allCommandsParams() );
    commandProvider.items().clear();
    commandProvider.items().addAll( commands );
    panel.refresh();
  }

  private void querySelectedEvents() {
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
    IList<IDtoCompletedCommand> commands = queryEngine.query( paramsPanel.interval(), selEvents );
    commandProvider.items().clear();
    commandProvider.items().addAll( commands );
    panel.refresh();
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

  private void printCommands() {
    try {
      ISkConnectionSupplier connectionSup = eclipseContext().get( ISkConnectionSupplier.class );
      ISkConnection connection = connectionSup.defConn();
      CommandM5Model printCommandsModel = new CommandM5Model( connection, true );

      m5().initTemporaryModel( printCommandsModel );

      ITsGuiContext printContext = new TsGuiContext( tsContext() );

      long startTime = paramsPanel.interval().startTime();
      long endTime = paramsPanel.interval().endTime();

      String title = String.format( PRINT_COMMAND_LIST_TITLE_FORMAT, timestampFormat.format( new Date( startTime ) ),
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
          ReportGenerator.generateJasperPrint( printContext, printCommandsModel, commandProvider );
      JasperReportDialog.showPrint( printContext, jasperPrint );
    }
    catch( Exception ex ) {
      LoggerUtils.errorLogger().error( ex );
      TsDialogUtils.error( getShell(), ex );
    }

  }
}
