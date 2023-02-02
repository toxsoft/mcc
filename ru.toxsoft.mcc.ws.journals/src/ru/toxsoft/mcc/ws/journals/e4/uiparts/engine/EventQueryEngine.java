package ru.toxsoft.mcc.ws.journals.e4.uiparts.engine;

import static org.toxsoft.core.tslib.utils.TsTestUtils.*;
import static ru.toxsoft.mcc.ws.journals.e4.uiparts.engine.IMmResources.*;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.toxsoft.core.tslib.av.opset.IOptionSet;
import org.toxsoft.core.tslib.bricks.time.*;
import org.toxsoft.core.tslib.bricks.time.impl.QueryInterval;
import org.toxsoft.core.tslib.bricks.time.impl.TimedList;
import org.toxsoft.core.tslib.coll.IList;
import org.toxsoft.core.tslib.gw.gwid.Gwid;
import org.toxsoft.core.tslib.gw.gwid.GwidList;
import org.toxsoft.core.tslib.utils.errors.TsIllegalStateRtException;
import org.toxsoft.core.tslib.utils.errors.TsNullArgumentRtException;
import org.toxsoft.core.tslib.utils.logs.impl.LoggerUtils;
import org.toxsoft.uskat.core.ISkCoreApi;
import org.toxsoft.uskat.core.api.evserv.SkEvent;
import org.toxsoft.uskat.core.api.hqserv.*;

/**
 * Реализация движка {@link IQueryEngine} для событий.
 *
 * @author goga, dima
 */
public class EventQueryEngine
    implements IQueryEngine<SkEvent> {

  private final ISkCoreApi coreApi;
  private IList<SkEvent>   result;

  private IQueryInterval      interval;
  private IJournalQueryFilter params;
  private ISkQueryRawHistory  query;

  /**
   * Признак процесса обработки запроса.
   */
  private boolean processing = false;

  /**
   * Просто конструктор.
   *
   * @param aServerApi {@link ISkCoreApi} - API сервера
   */
  public EventQueryEngine( ISkCoreApi aServerApi ) {
    coreApi = TsNullArgumentRtException.checkNull( aServerApi );

  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса IEventQueryEngine
  //

  @Override
  public void setQueryParams( ITimeInterval aInterval, IJournalQueryFilter aParams ) {
    interval = new QueryInterval( EQueryIntervalType.CSCE, aInterval.startTime(), aInterval.endTime() );
    params = aParams;
  }

  @Override
  public IList<SkEvent> getResult() {
    return result;
  }

  @Override
  public IList<SkEvent> query( ITimeInterval aInterval, IJournalQueryFilter aParams, ICallback aCallback ) {
    TsNullArgumentRtException.checkNulls( aInterval, aParams, aCallback );
    TsIllegalStateRtException.checkTrue( processing );
    if( aParams.items().isEmpty() ) {
      return IList.EMPTY;
    }
    processing = true;
    try {
      // 2023-01-19 mvk
      result = execQuery( coreApi, aInterval, aParams );
      return result;

      // result = new SortedElemLinkedBundleList<>();
      // ISkEventService eventService = coreApi.eventService();
      // // для каждого элемента из aParams.items() запросим службу событий
      // for( int i = 0, count = aParams.items().size(); i < count; i++ ) {
      // ConcerningEventsItem item = (ConcerningEventsItem)aParams.items().get( i );
      // // проверим, прекращает ли пользователь обработку запроса
      // double donePercents = (100.0 * i) / (count);
      // if( aCallback.onNextStep( donePercents ) ) {
      // break;
      // }
      // IQueryInterval qi = new QueryInterval( EQueryIntervalType.CSCE, aInterval.startTime(), aInterval.endTime() );
      // // собственно запрос
      // // Dima, 21.02.20 переходим на SkApi
      // // ITimedList<SkEvent> events = es.queryEvents( qi, item.objIds(), item.classId(), item.eventIds() );
      //
      // for( Gwid gwid : item.gwids( true, coreApi ) ) {
      // ITimedList<SkEvent> events = eventService.queryObjEvents( qi, gwid );
      // result.addAll( events );
      // }
      // }
      // return result;
    }
    catch( Exception e ) {
      e.printStackTrace();
      throw e;
    }
    finally {
      processing = false;
    }
  }

  @Override
  public void run( IProgressMonitor aMonitor )
      throws InvocationTargetException,
      InterruptedException {
    aMonitor.beginTask( MSG_INFO_QUERIENG_EVENTS, params.items().size() );

    // 2023-01-19 mvk
    result = execQuery( coreApi, interval, params );

    // ISkEventService eventService = coreApi.eventService();
    // // Делаем запрос на данные с обновлением индикатора прогресса
    // result = new SortedElemLinkedBundleList<>();
    //
    // // для каждого элемента из aParams.items() запросим службу IEventService
    // for( int i = 0, count = params.items().size(); i < count; i++ ) {
    // ConcerningEventsItem item = (ConcerningEventsItem)params.items().get( i );
    // // собственно запрос
    // for( Gwid gwid : item.gwids( true, coreApi ) ) {
    // long intervalStart = interval.startTime();
    // long intervalEnd = interval.endTime();
    //
    // ITimedList<SkEvent> events = eventService.queryObjEvents( interval, gwid );
    // if( events.size() > 0 ) {
    // System.out.println( "Strart interval = " + new Date( intervalStart ) );
    // System.out.println( "End interval = " + new Date( intervalEnd ) );
    // System.out.println( "Events = " + events.size() );
    // }
    //
    // result.addAll( events );
    // }
    // aMonitor.worked( 1 );
    // }
    // aMonitor.done();
  }

  // 2023-01-19 mvk
  @SuppressWarnings( "nls" )
  private IList<SkEvent> execQuery( ISkCoreApi aCoreApi, ITimeInterval aInterval, IJournalQueryFilter aParams ) {
    TsNullArgumentRtException.checkNulls( aInterval, aParams );
    // Служба запросов
    ISkHistoryQueryService queryService = aCoreApi.hqService();
    // Запрос
    query = queryService.createHistoricQuery( IOptionSet.NULL );
    // Результат
    TimedList<SkEvent> retValue = new TimedList<>();
    try {
      query.genericChangeEventer().addListener( aSource -> {
        ISkQueryRawHistory q = (ISkQueryRawHistory)aSource;
        pl( "EventQueryEngine.query(...): queryId = %s, state = %s", q.queryId(), q.state() );
        if( q.state() == ESkQueryState.READY || q.state() == ESkQueryState.FAILED ) {
          synchronized (query) {
            query.notifyAll();
          }
        }
        if( q.state() == ESkQueryState.READY ) {
          for( Gwid gwid : q.listGwids() ) {
            pl( "EventQueryEngine.query(...):  gwid = %s, value count = %d", gwid,
                Integer.valueOf( q.get( gwid ).size() ) );
          }
        }
      } );
      GwidList gwids = new GwidList();
      for( int i = 0, count = aParams.items().size(); i < count; i++ ) {
        ConcerningEventsItem item = (ConcerningEventsItem)aParams.items().get( i );
        gwids.addAll( item.gwids( true, aCoreApi ) );
      }
      query.prepare( gwids );
      synchronized (query) {
        query.exec( new QueryInterval( EQueryIntervalType.CSCE, aInterval.startTime(), aInterval.endTime() ) );
        try {
          query.wait();
        }
        catch( InterruptedException ex ) {
          LoggerUtils.errorLogger().error( ex );
        }
      }
      for( Gwid gwid : query.listGwids() ) {
        retValue.addAll( query.get( gwid ) );
      }
    }
    finally {
      query.close();
    }
    return retValue;

  }

  public void cancelQuery() {
    if( query != null ) {
      query.cancel();
    }
  }
}
