package ru.toxsoft.mcc.ws.mnemos.app.rt.alarm;

import static org.toxsoft.core.tsgui.bricks.actions.ITsStdActionDefs.*;
import static org.toxsoft.core.tsgui.graphics.icons.ITsStdIconIds.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.uskat.core.ISkHardConstants.*;

import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.actions.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.ctx.impl.*;
import org.toxsoft.core.tsgui.bricks.tstree.impl.*;
import org.toxsoft.core.tsgui.dialogs.*;
import org.toxsoft.core.tsgui.graphics.icons.*;
import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.gui.mpc.*;
import org.toxsoft.core.tsgui.m5.gui.mpc.impl.*;
import org.toxsoft.core.tsgui.m5.gui.panels.*;
import org.toxsoft.core.tsgui.m5.gui.panels.impl.*;
import org.toxsoft.core.tsgui.m5.gui.viewers.*;
import org.toxsoft.core.tsgui.panels.*;
import org.toxsoft.core.tsgui.panels.toolbar.*;
import org.toxsoft.core.tsgui.utils.layout.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.bricks.time.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.uskat.alarms.lib.*;
import org.toxsoft.uskat.alarms.lib.impl.*;
import org.toxsoft.uskat.alarms.s5.supports.*;
import org.toxsoft.uskat.core.api.users.*;
import org.toxsoft.uskat.core.connection.*;
import org.toxsoft.uskat.s5.utils.*;

/**
 * Панель отображения и квитирования списка Алармов
 *
 * @author max
 */
public class Ts4AlarmPanel
    extends TsPanel {

  final static String ACTID_QUIT_ALARM = SK_ID + ".users.gui.QuitAlarm";

  final static TsActionDef ACDEF_QUIT_ALARM = TsActionDef.ofPush2( ACTID_QUIT_ALARM, "Квитировать тревоги",
      "Квитирование выбранных тревог", ICONID_LIST_REMOVE );

  final static String ACTID_GENERATE_TEST_ALARM = SK_ID + ".users.gui.GenerateTestAlarm";

  final static TsActionDef ACDEF_GENERATE_TEST_ALARM = TsActionDef.ofPush2( ACTID_GENERATE_TEST_ALARM,
      "Сгенерировать тестовую тревогу", "Генерация тестовой тревоги", ICONID_LIST_ADD );

  final static String TEST_ALARM_ID = "test.Alarm";

  final static String TEST_ALARM_NAME = "TestAlarm";

  IM5CollectionPanel<ISkAlarm> alarmPanel;

  private final ISkConnection skConn;

  /**
   * Конструктор.
   *
   * @param aParent Composite - родительская компонента
   * @param aSkConn ISkConnection - соединение с сервером
   * @param aCtx ITsGuiContext - соответствующий контекст
   */
  public Ts4AlarmPanel( Composite aParent, ISkConnection aSkConn, ITsGuiContext aCtx ) {
    super( aParent, aCtx );

    skConn = aSkConn;
    BorderLayout bl = new BorderLayout();
    bl.setHgap( 0 );
    bl.setVgap( 0 );
    aParent.setLayout( bl );
    this.setLayout( bl );

    if( !skConn.coreApi().services().hasKey( ISkAlarmService.SERVICE_ID ) ) {
      return;
    }

    ISkAlarmService alarmService = (ISkAlarmService)skConn.coreApi().services().getByKey( ISkAlarmService.SERVICE_ID );

    if( alarmService.findAlarmDef( TEST_ALARM_ID ) == null ) {
      alarmService.registerAlarmDef( new S5AlarmDefEntity( TEST_ALARM_ID, TEST_ALARM_NAME ) );
    }

    IM5Model<ISkAlarm> model = m5().getModel( SkAlarmM5Model.MODEL_ID, ISkAlarm.class );

    SkAlarmM5LifecycleManager lm = new SkAlarmM5LifecycleManager( model, alarmService );
    lm.setInterval( ITimeInterval.WHOLE );
    lm.setFilter( SkAlarmM5LifecycleManager.FILTER1 );
    ITsGuiContext ctx = new TsGuiContext( aCtx );
    ctx.params().addAll( aCtx.params() );
    IMultiPaneComponentConstants.OPDEF_IS_DETAILS_PANE.setValue( ctx.params(), AvUtils.AV_TRUE );
    IMultiPaneComponentConstants.OPDEF_DETAILS_PANE_PLACE.setValue( ctx.params(),
        avValobj( EBorderLayoutPlacement.SOUTH ) );
    IMultiPaneComponentConstants.OPDEF_IS_SUPPORTS_CHECKS.setValue( ctx.params(), AvUtils.AV_TRUE );
    IMultiPaneComponentConstants.OPDEF_IS_ACTIONS_CRUD.setValue( ctx.params(), AvUtils.AV_FALSE );
    // прячем фильтр
    IMultiPaneComponentConstants.OPDEF_IS_FILTER_PANE.setValue( ctx.params(), AvUtils.AV_FALSE );

    // SashForm sf = new SashForm( aParent, SWT.HORIZONTAL );
    TsTreeViewer.OPDEF_IS_HEADER_SHOWN.setValue( ctx.params(), AvUtils.AV_FALSE );
    MultiPaneComponentModown<ISkAlarm> componentModown =
        new MultiPaneComponentModown<>( ctx, model, lm.itemsProvider(), lm ) {

          @Override
          protected ITsToolbar doCreateToolbar( ITsGuiContext aContext, String aName, EIconSize aIconSize,
              IListEdit<ITsActionDef> aActs ) {
            aActs.add( ACDEF_SEPARATOR );
            aActs.add( ACDEF_QUIT_ALARM );
            // aActs.add( ACDEF_SEPARATOR );
            // aActs.add( ACDEF_GENERATE_TEST_ALARM );

            ITsToolbar toolbar =

                super.doCreateToolbar( aContext, aName, aIconSize, aActs );

            // toolbar.addListener( aActionId -> { } );

            return toolbar;
          }

          @Override
          protected void doProcessAction( String aActionId ) {

            switch( aActionId ) {
              case ACTID_QUIT_ALARM: {
                // TODO
                IList<ISkAlarm> selAlarms = tree().checks().listCheckedItems( true );
                if( TsDialogUtils.askYesNoCancel( getShell(),
                    "Квитировать выбранные тревоги?" ) != ETsDialogCode.YES ) {
                  return;
                }

                for( ISkAlarm alarm : selAlarms ) {
                  lifecycleManager().remove( alarm );
                }

                refresh();
                break;
              }
              case ACTID_GENERATE_TEST_ALARM: {
                // TODO
                ISkUser currUser = S5ConnectionUtils.getConnectedUser( skConn.coreApi() );

                alarmService.generateAlarm( TEST_ALARM_ID, currUser.skid(), currUser.skid(), (byte)0,
                    new SkAlarmFlacon( new OptionSet() ) );
                break;
              }
              default:
                throw new TsNotAllEnumsUsedRtException( aActionId );
            }
          }
        };
    alarmPanel = new M5CollectionPanelMpcModownWrapper<>( componentModown, false );

    Control c = alarmPanel.createControl( this );

    // dima 17.01.23 под Win ширина колонки маленькая, уширим
    IM5Column<ISkAlarm> messageColumn =
        componentModown.tree().columnManager().columns().findByKey( SkAlarmM5Model.FID_MESSAGE );
    messageColumn.setWidth( 600 );

    alarmService.eventer().addListener( new ISkAlarmServiceListener() {

      @Override
      public void onAlarm( ISkAlarm aSkAlarm ) {
        // boolean quit = aSkAlarm.history().size() > 0
        // && SkAlarmM5LifecycleManager.ALARM_QUIT_ID.getValue( aSkAlarm.history().last().params() ).asBool();
        //
        // if( !quit ) {
        // ((SkAlarmM5LifecycleManager)componentModown.lifecycleManager()).setQuitValue( aSkAlarm, false );

        Display display = aCtx.get( Display.class );
        display.asyncExec( () -> componentModown.refresh() );
        // }
      }
    } );

    // alarmService.queryAlarms( null, null )
  }

}
