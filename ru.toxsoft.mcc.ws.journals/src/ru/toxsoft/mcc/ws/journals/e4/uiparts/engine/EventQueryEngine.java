package ru.toxsoft.mcc.ws.journals.e4.uiparts.engine;

import static ru.toxsoft.mcc.ws.journals.e4.uiparts.engine.IMmResources.*;

import java.lang.reflect.*;
import java.util.*;

import org.eclipse.core.runtime.*;
import org.toxsoft.core.tslib.bricks.time.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.gw.gwid.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.uskat.core.*;
import org.toxsoft.uskat.core.api.evserv.*;
import org.toxsoft.uskat.s5.legacy.*;

/**
 * Реализация движка {@link IQueryEngine} для событий.
 *
 * @author goga, dima
 */
public class EventQueryEngine
    implements IQueryEngine<SkEvent> {

  private final ISkCoreApi        serverApi;
  private IListBasicEdit<SkEvent> result;

  private IQueryInterval      interval;
  private IJournalQueryFilter params;

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
    serverApi = TsNullArgumentRtException.checkNull( aServerApi );

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
      result = new SortedElemLinkedBundleList<>();
      ISkEventService eventService = serverApi.eventService();
      // для каждого элемента из aParams.items() запросим службу событий
      for( int i = 0, count = aParams.items().size(); i < count; i++ ) {
        ConcerningEventsItem item = (ConcerningEventsItem)aParams.items().get( i );
        // проверим, прекращает ли пользователь обработку запроса
        double donePercents = (100.0 * i) / (count);
        if( aCallback.onNextStep( donePercents ) ) {
          break;
        }
        IQueryInterval qi = new QueryInterval( EQueryIntervalType.CSCE, aInterval.startTime(), aInterval.endTime() );
        // собственно запрос
        // Dima, 21.02.20 переходим на SkApi
        // ITimedList<SkEvent> events = es.queryEvents( qi, item.objIds(), item.classId(), item.eventIds() );

        for( Gwid gwid : item.gwids( true, serverApi ) ) {
          ITimedList<SkEvent> events = eventService.queryObjEvents( qi, gwid );
          result.addAll( events );
        }
      }
      return result;
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
    ISkEventService eventService = serverApi.eventService();
    // Делаем запрос на данные с обновлением индикатора прогресса
    result = new SortedElemLinkedBundleList<>();

    // для каждого элемента из aParams.items() запросим службу IEventService
    for( int i = 0, count = params.items().size(); i < count; i++ ) {
      ConcerningEventsItem item = (ConcerningEventsItem)params.items().get( i );
      // собственно запрос
      for( Gwid gwid : item.gwids( true, serverApi ) ) {
        long intervalStart = interval.startTime();
        long intervalEnd = interval.endTime();

        ITimedList<SkEvent> events = eventService.queryObjEvents( interval, gwid );
        if( events.size() > 0 ) {
          System.out.println( "Strart interval = " + new Date( intervalStart ) );
          System.out.println( "End interval = " + new Date( intervalEnd ) );
          System.out.println( "Events = " + events.size() );
        }

        result.addAll( events );
      }
      aMonitor.worked( 1 );
    }
    aMonitor.done();
  }
}
