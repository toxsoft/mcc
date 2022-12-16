package ru.toxsoft.mcc.ws.mnemos.app.rt.alarm;

import static org.toxsoft.core.tslib.av.EAtomicType.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.model.impl.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.av.math.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.bricks.filter.*;
import org.toxsoft.core.tslib.bricks.time.*;
import org.toxsoft.core.tslib.bricks.time.impl.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.uskat.alarms.lib.*;
import org.toxsoft.uskat.alarms.lib.filters.*;
import org.toxsoft.uskat.alarms.lib.impl.*;

/**
 * Lifecycle manager for {@link SkAlarmM5Model}.
 *
 * @author max
 */
public class SkAlarmM5LifecycleManager
    extends M5LifecycleManager<ISkAlarm, ISkAlarmService> {

  private static final String ALARM_GUI_THREAD_ID = "main.gui.thread";

  private static final IDataDef ALARM_QUIT_ID =
      org.toxsoft.core.tslib.av.impl.DataDef.create( "m5.alarm.is.quit", BOOLEAN, TSID_NAME, "Alarm is quit", //
          TSID_DEFAULT_VALUE, AV_FALSE );

  private static final ITsCombiFilterParams FILTER1 = SkAlarmFilterByHistory.makeFilterParams( //
      // test ISkAlarm.history() size ('!=0'):
      EAvCompareOp.NE, AvUtils.AV_0,

      true,
      // test ISkAlarmThreadHistoryItem.timestamp() ('any'):
      EAvCompareOp.GE, AvUtils.avTimestamp( TimeUtils.MIN_TIMESTAMP ),

      // test ISkAlarmThreadHistoryItem.announceThreadId() ('not empty'):
      EAvCompareOp.NE, AvUtils.avStr( ALARM_GUI_THREAD_ID )// ,

  // test ISkAlarmThreadHistoryItem.params():
  // new ParamValue( ALARM_QUIT_ID.id(), EAvCompareOp.NE, AvUtils.AV_TRUE )
  );//

  private static final ITsCombiFilterParams FILTER2 = SkAlarmFilterByHistory.makeFilterParams( //
      // test ISkAlarm.history() size ('!=0'):
      EAvCompareOp.NE, AvUtils.AV_0,

      true,
      // test ISkAlarmThreadHistoryItem.timestamp() ('any'):
      EAvCompareOp.GE, AvUtils.avTimestamp( TimeUtils.MIN_TIMESTAMP ),

      // test ISkAlarmThreadHistoryItem.announceThreadId() ('not empty'):
      EAvCompareOp.EQ, AvUtils.avStr( ISkAlarmThreadHistoryItem.ALARM_THREAD_NULL ) );//

  /**
   * Constructor.
   *
   * @param aModel {@link IM5Model}&lt;T&gt; - the model
   * @param aMaster {@link ISkAlarmService}&lt;M&gt; - master object
   * @throws TsNullArgumentRtException model is <code>null</code>
   */
  public SkAlarmM5LifecycleManager( IM5Model<ISkAlarm> aModel, ISkAlarmService aMaster ) {
    super( aModel, false, false, true, true, aMaster );
  }

  public void setQuitValue( ISkAlarm aAlarm, boolean aIsQuit ) {
    OptionSet alarmOptions = new OptionSet();
    ALARM_QUIT_ID.setValue( alarmOptions, AvUtils.avBool( aIsQuit ) );

    ISkAlarmThreadHistoryItem quitItem =
        new SkAlarmThreadHistoryItem( System.currentTimeMillis(), ALARM_GUI_THREAD_ID, alarmOptions );
    master().addThreadHistoryItem( aAlarm.alarmId(), quitItem );
  }

  @Override
  public void doRemove( ISkAlarm aAlarm ) {
    OptionSet alarmOptions = new OptionSet();
    ALARM_QUIT_ID.setValue( alarmOptions, AV_TRUE );

    ISkAlarmThreadHistoryItem quitItem =
        new SkAlarmThreadHistoryItem( System.currentTimeMillis(), ALARM_GUI_THREAD_ID, alarmOptions );
    master().addThreadHistoryItem( aAlarm.alarmId(), quitItem );
  }

  @Override
  protected IList<ISkAlarm> doListEntities() {
    IListEdit<ISkAlarm> result = new ElemArrayList<>();

    ITimedList<ISkAlarm> alarms1 = master().queryAlarms( ITimeInterval.WHOLE, FILTER1 );
    for( ISkAlarm alarm : alarms1 ) {
      result.add( alarm );
    }

    // ITimedList<ISkAlarm> alarms2 = master().queryAlarms( ITimeInterval.WHOLE, FILTER2 );
    // for( ISkAlarm alarm : alarms2 ) {
    // result.add( alarm );
    // }

    // TODO - это вместо фильтра, нужно реализовать фильтр
    // for( ISkAlarm alarm : alarms ) {
    // if( alarm.history().size() == 0 || !REPORT_TITLE_M5_ID.getValue( alarm.history().last().params() ).asBool() ) {
    // result.add( alarm );
    // }
    // }

    System.out.println( "Alarm count = " + result.size() );

    return result;
  }

}
