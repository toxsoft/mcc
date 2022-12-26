package ru.toxsoft.mcc.ws.reports.e4.uiparts.chart.dataset;

import static org.toxsoft.uskat.core.api.hqserv.ISkHistoryQueryServiceConstants.*;

import org.toxsoft.core.tsgui.chart.api.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.av.temporal.*;
import org.toxsoft.core.tslib.bricks.events.change.*;
import org.toxsoft.core.tslib.bricks.filter.*;
import org.toxsoft.core.tslib.bricks.strid.impl.*;
import org.toxsoft.core.tslib.bricks.time.*;
import org.toxsoft.core.tslib.bricks.time.impl.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.helpers.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.core.tslib.gw.gwid.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.uskat.core.api.hqserv.*;
import org.toxsoft.uskat.core.connection.*;
import org.toxsoft.uskat.core.impl.dto.*;

import ru.toxsoft.mcc.ws.core.templates.utils.*;

/**
 * Набор данных для графика. <br>
 * Самостоятельно подкачивает данные по мере необходимости.
 *
 * @author dima
 */
public class G2SelfUploadHistoryDataSet
    extends Stridable
    implements IG2DataSet, IGenericChangeEventer {

  String QUERY_PARAM_ID = "query_param_id"; //$NON-NLS-1$

  IListEdit<ITemporalAtomicValue>    values          = new ElemArrayList<>();
  private final ISkConnection        conn;
  private final IDataSetParam        param;
  private final GenericChangeEventer eventer;
  ITimeInterval                      queriedInterval = null;

  /**
   * Конструктор набора данных для графика
   *
   * @param aConnection соединение с сервером
   * @param aId id набора данных
   * @param aParam описание параметра данных
   */
  public G2SelfUploadHistoryDataSet( ISkConnection aConnection, String aId, IDataSetParam aParam ) {
    super( aId, TsLibUtils.EMPTY_STRING, TsLibUtils.EMPTY_STRING, true );
    conn = aConnection;
    param = aParam;
    eventer = new GenericChangeEventer( this );
  }

  /**
   * Установить данные для графика
   *
   * @param aValues список значений с меткой времени
   */
  public void setValues( IList<ITemporalAtomicValue> aValues ) {
    values.addAll( aValues );
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса ICloseable
  //

  @Override
  public IList<ITemporalAtomicValue> getValues( ITimeInterval aInterval ) {
    // System.out.println( "getValues(" + aInterval.toString() );
    if( aInterval.equals( ITimeInterval.NULL ) || aInterval.equals( ITimeInterval.WHOLE ) ) {
      return new ElemArrayList<>( values );
    }
    // // вырезаем данные под запрошенный интервал
    // ElemArrayList<ITemporalAtomicValue> retVal = new ElemArrayList<>();
    // for( ITemporalAtomicValue val : values ) {
    // if( TimeUtils.contains( aInterval, val.timestamp() ) ) {
    // retVal.add( val );
    // }
    // }
    // return retVal;
    // готовим данные
    prepare( aInterval );

    return new ElemArrayList<>( values );
  }

  @Override
  public void prepare( ITimeInterval aInterval ) {
    // System.out.println( "prepare(" + aInterval.toString() );

    if( queriedInterval == null ) {
      queriedInterval = aInterval;
      queryData( queriedInterval );
    }
    else {
      // проверяем наличие данных и в случае необходимости подкачиваем
      Pair<ITimeInterval, ITimeInterval> empty = TimeUtils.subtract( aInterval, queriedInterval );
      if( empty.left() != null ) {
        // подкачаем данные слева
        queryData( empty.left() );
      }
      if( empty.right() != null ) {
        // подкачаем данные справа
        queryData( empty.right() );
      }
    }
  }

  @Override
  public Pair<ITemporalAtomicValue, ITemporalAtomicValue> locate( long aTimeStamp ) {
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

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса ICloseable
  //

  @Override
  public void close() {
    // nop
  }

  /**
   * Запрос на историю параметра
   *
   * @param aTimeInterval интервал запроса
   */
  private void queryData( ITimeInterval aTimeInterval ) {
    // System.out.println( "queryData(" + aTimeInterval.toString() );
    // запросим историю параметра
    ISkQueryProcessedData processData = conn.coreApi().hqService().createProcessedQuery( IOptionSet.NULL );
    IStringMap<IDtoQueryParam> queryParams = queryParams( param.gwid() );
    processData.prepare( queryParams );

    processData.genericChangeEventer().addListener( aSource -> {
      ISkQueryProcessedData q = (ISkQueryProcessedData)aSource;
      if( q.state() == ESkQueryState.READY ) {
        IList<ITimedList<?>> requestAnswer = ReportTemplateUtilities.createResult( processData, queryParams );
        ITimedList<?> historyData = requestAnswer.getOnly();
        for( Object value : historyData ) {
          if( value instanceof TemporalAtomicValue tav ) {
            values.add( tav );
          }
        }
        // сортируем по времени
        IListReorderer<ITemporalAtomicValue> reorderer = new ListReorderer<>( values );
        reorderer.sort( ( aO1, aO2 ) -> (int)(aO1.timestamp() - aO2.timestamp()) );
        values = new ElemArrayList<>( reorderer.list() );
        // обновляем данные об уже запрошеном интревале
        updateQueriedIntrvl( aTimeInterval );
        // нотификация слушателя о появлении новых данных
        eventer.fireChangeEvent();
      }
    } );
    processData
        .exec( new QueryInterval( EQueryIntervalType.OSOE, aTimeInterval.startTime(), aTimeInterval.endTime() ) );

  }

  private void updateQueriedIntrvl( ITimeInterval aTimeInterval ) {
    queriedInterval = new TimeInterval( Long.min( queriedInterval.startTime(), aTimeInterval.startTime() ),
        Long.max( queriedInterval.endTime(), aTimeInterval.endTime() ) );
  }

  private IStringMap<IDtoQueryParam> queryParams( Gwid aGwid ) {
    IStringMapEdit<IDtoQueryParam> result = new StringMap<>();
    ITsCombiFilterParams filter = ITsCombiFilterParams.ALL;
    IOptionSetEdit funcArgs = new OptionSet();
    // задаем интервал агрегации
    funcArgs.setInt( HQFUNC_ARG_AGGREGAION_INTERVAL, param.aggrStep() );
    // задаем функцию агрегации
    String aggrFuncId = param.aggrFuncId(); // HQFUNC_ID_AVERAGE
    IDtoQueryParam qParam = DtoQueryParam.create( aGwid, filter, aggrFuncId, funcArgs );
    result.put( QUERY_PARAM_ID, qParam );
    return result;
  }

  @Override
  public void addListener( IGenericChangeListener aListener ) {
    eventer.addListener( aListener );
  }

  @Override
  public void removeListener( IGenericChangeListener aListener ) {
    eventer.removeListener( aListener );
  }

  @Override
  public void muteListener( IGenericChangeListener aListener ) {
    eventer.muteListener( aListener );
  }

  @Override
  public void unmuteListener( IGenericChangeListener aListener ) {
    eventer.unmuteListener( aListener );
  }

  @Override
  public boolean isListenerMuted( IGenericChangeListener aListener ) {
    return eventer.isListenerMuted( aListener );
  }

  @Override
  public void pauseFiring() {
    eventer.pauseFiring();
  }

  @Override
  public void resumeFiring( boolean aFireDelayed ) {
    eventer.resumeFiring( aFireDelayed );
  }

  @Override
  public boolean isFiringPaused() {
    return eventer.isFiringPaused();
  }

  @Override
  public boolean isPendingEvents() {
    return eventer.isPendingEvents();
  }

  @Override
  public void resetPendingEvents() {
    eventer.resetPendingEvents();
  }

}
