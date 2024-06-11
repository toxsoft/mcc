package ru.toxsoft.mcc.ws.mnemos.app.rt.alarm;

import static org.toxsoft.core.tsgui.bricks.actions.ITsStdActionDefs.*;
import static org.toxsoft.core.tsgui.graphics.icons.ITsStdIconIds.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.uskat.core.ISkHardConstants.*;

import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.actions.ITsActionDef;
import org.toxsoft.core.tsgui.bricks.actions.TsActionDef;
import org.toxsoft.core.tsgui.bricks.ctx.ITsGuiContext;
import org.toxsoft.core.tsgui.bricks.ctx.impl.TsGuiContext;
import org.toxsoft.core.tsgui.bricks.tstree.impl.TsTreeViewer;
import org.toxsoft.core.tsgui.graphics.icons.EIconSize;
import org.toxsoft.core.tsgui.m5.IM5Model;
import org.toxsoft.core.tsgui.m5.gui.mpc.IMultiPaneComponentConstants;
import org.toxsoft.core.tsgui.m5.gui.mpc.impl.MultiPaneComponentModown;
import org.toxsoft.core.tsgui.m5.gui.panels.IM5CollectionPanel;
import org.toxsoft.core.tsgui.m5.gui.panels.impl.M5CollectionPanelMpcModownWrapper;
import org.toxsoft.core.tsgui.m5.gui.viewers.IM5Column;
import org.toxsoft.core.tsgui.panels.TsPanel;
import org.toxsoft.core.tsgui.panels.toolbar.ITsToolbar;
import org.toxsoft.core.tsgui.utils.layout.BorderLayout;
import org.toxsoft.core.tsgui.utils.layout.EBorderLayoutPlacement;
import org.toxsoft.core.tslib.av.impl.AvUtils;
import org.toxsoft.core.tslib.av.opset.IOptionSet;
import org.toxsoft.core.tslib.av.opset.impl.OptionSet;
import org.toxsoft.core.tslib.bricks.time.ITimeInterval;
import org.toxsoft.core.tslib.coll.IList;
import org.toxsoft.core.tslib.coll.IListEdit;
import org.toxsoft.core.tslib.gw.skid.Skid;
import org.toxsoft.core.tslib.utils.errors.TsNotAllEnumsUsedRtException;
import org.toxsoft.skf.legacy.alarms.lib.*;
import org.toxsoft.skf.legacy.alarms.lib.impl.SkAlarmFlacon;
import org.toxsoft.skf.legacy.alarms.s5.supports.S5AlarmDefEntity;
import org.toxsoft.uskat.core.connection.ISkConnection;

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
  private SoundPlayer         player;

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

    player = new SoundPlayer( "sound/train.wav" ); //$NON-NLS-1$

    ISkAlarmService alarmService = (ISkAlarmService)skConn.coreApi().services().getByKey( ISkAlarmService.SERVICE_ID );

    if( alarmService.findAlarmDef( TEST_ALARM_ID ) == null ) {
      alarmService.registerAlarmDef( new S5AlarmDefEntity( TEST_ALARM_ID, TEST_ALARM_NAME, IOptionSet.NULL ) );
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
                // dima 19.05.23 отключено по требованию Синько
                // if( TsDialogUtils.askYesNoCancel( getShell(),
                // "Квитировать выбранные тревоги?" ) != ETsDialogCode.YES ) {
                // return;
                // }

                for( ISkAlarm alarm : selAlarms ) {
                  lifecycleManager().remove( alarm );
                }
                refresh();
                if( tree().items().size() == 0 ) {
                  player.stop();
                }
                break;
              }
              case ACTID_GENERATE_TEST_ALARM: {
                // TODO
                Skid currUser = skConn.coreApi().getCurrentUserInfo().userSkid();
                alarmService.generateAlarm( TEST_ALARM_ID, currUser, currUser, (byte)0,
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
    // dima 30.01.23 начинаем звонить, если есть хоть один аларм
    if( !lm.doListEntities().isEmpty() ) {
      player.start();
    }

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
        display.asyncExec( () -> {
          componentModown.refresh();
          player.start();
        } );
        // }
      }
    } );

    // alarmService.queryAlarms( null, null )
  }

}
