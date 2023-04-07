package ru.toxsoft.mcc.ws.mnemos.app.rt.chart;

import static org.toxsoft.uskat.core.api.hqserv.ISkHistoryQueryServiceConstants.*;

import java.util.Timer;
import java.util.TimerTask;

import org.toxsoft.core.tsgui.chart.api.IG2DataSet;
import org.toxsoft.core.tslib.av.IAtomicValue;
import org.toxsoft.core.tslib.av.opset.IOptionSetEdit;
import org.toxsoft.core.tslib.av.opset.impl.OptionSet;
import org.toxsoft.core.tslib.av.temporal.ITemporalAtomicValue;
import org.toxsoft.core.tslib.av.temporal.TemporalAtomicValue;
import org.toxsoft.core.tslib.bricks.filter.ITsCombiFilterParams;
import org.toxsoft.core.tslib.bricks.strid.impl.Stridable;
import org.toxsoft.core.tslib.bricks.time.ITimeInterval;
import org.toxsoft.core.tslib.bricks.time.ITimedList;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.ElemArrayList;
import org.toxsoft.core.tslib.coll.primtypes.IStringMap;
import org.toxsoft.core.tslib.coll.primtypes.IStringMapEdit;
import org.toxsoft.core.tslib.coll.primtypes.impl.StringMap;
import org.toxsoft.core.tslib.gw.gwid.Gwid;
import org.toxsoft.core.tslib.gw.gwid.GwidList;
import org.toxsoft.core.tslib.utils.Pair;
import org.toxsoft.uskat.core.ISkCoreApi;
import org.toxsoft.uskat.core.api.hqserv.IDtoQueryParam;
import org.toxsoft.uskat.core.api.objserv.ISkObject;
import org.toxsoft.uskat.core.api.rtdserv.ISkReadCurrDataChannel;
import org.toxsoft.uskat.core.impl.dto.DtoQueryParam;

import ru.toxsoft.skt.vetrol.ws.core.templates.api.IVtGraphParam;
import ru.toxsoft.skt.vetrol.ws.core.templates.utils.ReportTemplateUtilities;

/**
 * Набор данных для графика реального времени.
 * <p>
 * Представляет собой список с фиксированным максимальнм значением элементов. Если при добавлении нового данного размер
 * списка превышает максимальный, то самый старый элемент удаляется.
 *
 * @author dima
 */
public class RtGraphDataSet
    extends Stridable
    implements IG2DataSet {

  String QUERY_PARAM_ID = "rt_graph_query_param_id"; //$NON-NLS-1$

  private final IListEdit<ITemporalAtomicValue> values   = new ElemArrayList<>();
  private final ISkReadCurrDataChannel          rtDataChannel;
  int                                           maxCount = 600 + 2;
  private final Timer                           timer    = new Timer();
  private final IVtGraphParam                   graphParam;

  /**
   * @return graphParam
   */
  public IVtGraphParam getGraphParam() {
    return graphParam;
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса IG2DataSet
  //
  /**
   * Создание набора данных реального времени
   *
   * @param aGraphParam описание rt data
   * @param aServerApi доступ к серверу
   * @param aHistoryData исторические данные этого параметра
   */

  public RtGraphDataSet( IVtGraphParam aGraphParam, ISkCoreApi aServerApi, ITimedList<?> aHistoryData ) {
    super( ReportTemplateUtilities.graphDataSetId( aGraphParam ) );
    // получим название и описание параметра
    ISkObject myselfObj = aServerApi.objService().find( aGraphParam.gwid().skid() );
    setName( myselfObj.nmName() );
    setDescription( myselfObj.description() );
    graphParam = aGraphParam;

    // запросим историю параметра за последние 10 минут
    // ISkQueryProcessedData processData = aServerApi.hqService().createProcessedQuery( IOptionSet.NULL );
    // IStringMap<IDtoQueryParam> queryParams = queryParams( aGraphParam.gwid() );
    // processData.prepare( queryParams );
    // TimeInterval queryInterval =
    // new TimeInterval( System.currentTimeMillis() - 10 * 60 * 1000, System.currentTimeMillis() );
    //
    // values = new ElemArrayList<>();
    //
    // processData.genericChangeEventer().addListener( aSource -> {
    // ISkQueryProcessedData q = (ISkQueryProcessedData)aSource;
    // if( q.state() == ESkQueryState.READY ) {
    // IList<ITimedList<?>> requestAnswer = ReportTemplateUtilities.createResult( processData, queryParams );
    // ITimedList<?> historyData = requestAnswer.getOnly();
    // for( Object value : historyData ) {
    // if( value instanceof TemporalAtomicValue tav ) {
    // values.add( tav );
    // }
    // }
    // // только после поучения данных запускаем таймер
    // aRtChartPanel.init();
    // aRtChartPanel.start();
    // TimerTask repeatedTask = new TimerTask() {
    //
    // @Override
    // public void run() {
    // // обновим текущее значение
    // addCurrValue( rtDataChannel.getValue() );
    // }
    // };
    // timer.scheduleAtFixedRate( repeatedTask, 1000L, 1000L );
    // }
    // } );
    //
    // processData
    // .exec( new QueryInterval( EQueryIntervalType.OSOE, queryInterval.startTime(), queryInterval.endTime() ) );

    for( Object value : aHistoryData ) {
      if( value instanceof TemporalAtomicValue tav ) {
        values.add( tav );
      }
    }

    // создаем канал текущих данных
    IMap<Gwid, ISkReadCurrDataChannel> channelMap =
        aServerApi.rtdService().createReadCurrDataChannels( new GwidList( aGraphParam.gwid() ) );
    rtDataChannel = channelMap.getByKey( aGraphParam.gwid() );
    // запускаем таймер
    TimerTask repeatedTask = new TimerTask() {

      @Override
      public void run() {
        // обновим текущее значение
        addCurrValue( rtDataChannel.getValue() );
      }
    };
    timer.scheduleAtFixedRate( repeatedTask, 1000L, 1000L );

  }

  private synchronized void addCurrValue( IAtomicValue aValue ) {
    long timestamp = System.currentTimeMillis();
    if( values.size() >= maxCount ) {
      values.removeByIndex( 0 );
    }
    values.add( new TemporalAtomicValue( timestamp, aValue ) );
  }

  private IStringMap<IDtoQueryParam> queryParams( Gwid aGwid ) {
    IStringMapEdit<IDtoQueryParam> result = new StringMap<>();
    ITsCombiFilterParams filter = ITsCombiFilterParams.ALL;
    IOptionSetEdit funcArgs = new OptionSet();
    // задаем интервал агрегации
    funcArgs.setInt( HQFUNC_ARG_AGGREGAION_INTERVAL, 1000 );
    IDtoQueryParam qParam = DtoQueryParam.create( aGwid, filter, HQFUNC_ID_AVERAGE, funcArgs );
    result.put( QUERY_PARAM_ID, qParam );
    return result;
  }

  @Override
  public void close() {
    // закрываем канал текущих данных
    rtDataChannel.close();
    timer.cancel();
  }

  @Override
  public synchronized IList<ITemporalAtomicValue> getValues( ITimeInterval aInterval ) {
    if( aInterval.equals( ITimeInterval.NULL ) || aInterval.equals( ITimeInterval.WHOLE ) ) {
      IListEdit<ITemporalAtomicValue> retVal = new ElemArrayList<>( values );
      return retVal;
    }
    IListEdit<ITemporalAtomicValue> retVal = new ElemArrayList<>();
    for( ITemporalAtomicValue val : values ) {
      if( aInterval.startTime() <= val.timestamp() && val.timestamp() <= aInterval.endTime() ) {
        retVal.add( val );
      }
    }
    return retVal;
  }

  @Override
  public void prepare( ITimeInterval aInterval ) {
    maxCount = (int)(aInterval.duration() / 100) + 2;
  }

  @Override
  public synchronized Pair<ITemporalAtomicValue, ITemporalAtomicValue> locate( long aTimeStamp ) {
    ITemporalAtomicValue leftVal = ITemporalAtomicValue.NULL;
    ITemporalAtomicValue rightVal = ITemporalAtomicValue.NULL;
    for( ITemporalAtomicValue value : values ) {
      if( value.timestamp() <= aTimeStamp ) {
        leftVal = value;
      }
      else {
        rightVal = value;
        break;
      }
    }
    return new Pair<>( leftVal, rightVal );
  }

}
