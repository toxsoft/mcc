package ru.toxsoft.mcc.ws.journals.e4.uiparts.main;

import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static ru.toxsoft.mcc.ws.journals.e4.uiparts.main.IMmResources.*;

import java.text.*;
import java.util.*;

import org.eclipse.swt.widgets.*;
import org.toxsoft.core.jasperreports.gui.main.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.ctx.impl.*;
import org.toxsoft.core.tsgui.dialogs.*;
import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.gui.mpc.*;
import org.toxsoft.core.tsgui.m5.gui.mpc.impl.*;
import org.toxsoft.core.tsgui.m5.gui.panels.*;
import org.toxsoft.core.tsgui.m5.gui.panels.impl.*;
import org.toxsoft.core.tsgui.panels.*;
import org.toxsoft.core.tsgui.utils.layout.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.bricks.events.change.*;
import org.toxsoft.core.tslib.bricks.filter.*;
import org.toxsoft.core.tslib.bricks.time.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.tslib.utils.logs.impl.*;
import org.toxsoft.uskat.alarms.lib.*;
import org.toxsoft.uskat.base.gui.conn.*;
import org.toxsoft.uskat.core.api.users.*;
import org.toxsoft.uskat.core.connection.*;
import org.toxsoft.uskat.s5.utils.*;

import ru.toxsoft.mcc.ws.journals.e4.uiparts.engine.*;
import ru.toxsoft.mcc.ws.journals.e4.uiparts.engine.IJournalParamsPanel.*;
import ru.toxsoft.mcc.ws.mnemos.app.rt.alarm.*;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.type.*;

/**
 * Панель просмотра журнала тревог
 *
 * @author max
 */
public class AlarmsJournalPanel
    extends TsPanel
    implements IGenericChangeListener {

  /**
   * формат для отображения метки времени
   */
  private static final String timestampFormatString = "dd.MM.yy HH:mm:ss"; //$NON-NLS-1$

  private static final DateFormat timestampFormat = new SimpleDateFormat( timestampFormatString );

  IM5CollectionPanel<ISkAlarm> alarmsPanel = null;

  SkAlarmM5LifecycleManager alarmsLifecycleManager = null;

  JournalParamsPanel paramsPanel = null;

  /**
   * @param aParent родительская панель
   * @param aContext контекст
   * @throws TsException - ошибка создания
   */
  public AlarmsJournalPanel( Composite aParent, ITsGuiContext aContext )
      throws TsException {
    super( aParent, aContext );

    setLayout( new BorderLayout() );

    ISkConnection connection = aContext.get( ISkConnectionSupplier.class ).defConn();

    IM5Model<ISkAlarm> model = m5().getModel( SkAlarmM5Model.MODEL_ID, ISkAlarm.class );

    ISkAlarmService alarmService =
        (ISkAlarmService)connection.coreApi().services().getByKey( ISkAlarmService.SERVICE_ID );

    alarmsLifecycleManager = new SkAlarmM5LifecycleManager( model, alarmService );
    ITsGuiContext ctx = new TsGuiContext( aContext );
    ctx.params().addAll( aContext.params() );
    IMultiPaneComponentConstants.OPDEF_IS_DETAILS_PANE.setValue( ctx.params(), AvUtils.AV_TRUE );
    IMultiPaneComponentConstants.OPDEF_DETAILS_PANE_PLACE.setValue( ctx.params(),
        avValobj( EBorderLayoutPlacement.SOUTH ) );
    // IMultiPaneComponentConstants.OPDEF_IS_SUPPORTS_CHECKS.setValue( ctx.params(), AvUtils.AV_TRUE );
    // IMultiPaneComponentConstants.OPDEF_IS_ACTIONS_CRUD.setValue( ctx.params(), AvUtils.AV_TRUE );
    // добавляем в панель фильтр
    IMultiPaneComponentConstants.OPDEF_IS_FILTER_PANE.setValue( ctx.params(), AvUtils.AV_TRUE );

    // SashForm sf = new SashForm( aParent, SWT.HORIZONTAL );
    MultiPaneComponentModown<ISkAlarm> componentModown =
        new MultiPaneComponentModown<>( ctx, model, alarmsLifecycleManager.itemsProvider(), alarmsLifecycleManager );

    alarmsPanel = new M5CollectionPanelMpcModownWrapper<>( componentModown, false );

    alarmsPanel.createControl( this );

    paramsPanel = new JournalParamsPanel( ctx ) {

      @Override
      protected IConcerningEventsParams chooseFilterParams() {
        TsDialogUtils.underDevelopment( getShell() );
        return null;
      }

    };
    paramsPanel.addListener( this );

    paramsPanel.setAppContext( aContext.eclipseContext() );

    // панель инструментов сверху
    paramsPanel.createControl( this );
    paramsPanel.getControl().setLayoutData( BorderLayout.NORTH );
    // настройка взаимодействия между компонентами
    // paramsPanel.addGenericChangeListener( queryStartListener );
    // eventProvider.items().addAll( lm.doListEntities() );

    alarmsPanel.refresh();
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
      queryAlarms( true );
    }
    if( journalPanel.currentAction() == ECurrentAction.QUERY_SELECTED ) {
      queryAlarms( false );
    }
    if( journalPanel.currentAction() == ECurrentAction.PRINT ) {
      printAlarms();
    }
  }

  private void queryAlarms( boolean aAllAlarmsInInterval ) {
    try {
      ITimeInterval interval = paramsPanel.interval();
      alarmsLifecycleManager.setInterval( interval );

      ITsCombiFilterParams filter = formFilter( aAllAlarmsInInterval );
      alarmsLifecycleManager.setFilter( filter );

    }
    catch( Exception ex ) {
      ex.printStackTrace();
      LoggerUtils.errorLogger().error( ex );
      TsDialogUtils.error( getShell(), ex );
    }
    // new ProgressMonitorDialog( getShell() ).run( true, true, queryEngine );
    alarmsPanel.refresh();
  }

  private ITsCombiFilterParams formFilter( boolean aAllAlarmsInInterval ) {

    ITsCombiFilterParams allInInterval = SkAlarmM5LifecycleManager.EMPTY_FILTER;

    if( !aAllAlarmsInInterval ) {
      // сложный фильтр
      //return paramsPanel.selectedParams().items().first();
    }

    return allInInterval;
  }

  private void printAlarms() {
    try {
      ISkConnectionSupplier connectionSup = eclipseContext().get( ISkConnectionSupplier.class );
      ISkConnection connection = connectionSup.defConn();
      SkAlarmM5Model printAlarmsModel = new SkAlarmM5Model( true );

      m5().initTemporaryModel( printAlarmsModel );

      ITsGuiContext printContext = new TsGuiContext( tsContext() );

      long startTime = paramsPanel.interval().startTime();
      long endTime = paramsPanel.interval().endTime();

      String title = String.format( PRINT_ALARM_LIST_TITLE_FORMAT, timestampFormat.format( new Date( startTime ) ),
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
          ReportGenerator.generateJasperPrint( printContext, printAlarmsModel, alarmsLifecycleManager.itemsProvider() );
      JasperReportDialog.showPrint( printContext, jasperPrint );
    }
    catch( Exception ex ) {
      LoggerUtils.errorLogger().error( ex );
      TsDialogUtils.error( getShell(), ex );
    }

  }

}
