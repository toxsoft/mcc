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
import org.toxsoft.uskat.core.api.cmdserv.IDtoCompletedCommand;
import org.toxsoft.uskat.core.api.hqserv.*;

/**
 * Реализация движка {@link IQueryEngine}.
 *
 * @author dima
 */
public class CommandQueryEngine
    implements IQueryEngine<IDtoCompletedCommand> {

  private final ISkCoreApi            coreApi;
  private IList<IDtoCompletedCommand> result;

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
   * @param aCoreApi {@link ISkCoreApi} - API сервераs
   */
  public CommandQueryEngine( ISkCoreApi aCoreApi ) {
    coreApi = TsNullArgumentRtException.checkNull( aCoreApi );

  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса IEventQueryEngine
  //

  @Override
  public IList<IDtoCompletedCommand> query( ITimeInterval aInterval, IJournalQueryFilter aParams,
      ICallback aCallback ) {
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
      // ISkCommandService cmdService = coreApi.cmdService();
      // // для каждого элемента из aParams.items() запросим службу команд
      // for( int i = 0, count = aParams.items().size(); i < count; i++ ) {
      // ConcerningEventsItem item = (ConcerningEventsItem)aParams.items().get( i );
      // // проверим, прекращает ли пользователь обработку запроса
      // double donePercents = (100.0 * i) / (count);
      // if( aCallback.onNextStep( donePercents ) ) {
      // break;
      // }
      // IQueryInterval qi = new QueryInterval( EQueryIntervalType.CSCE, aInterval.startTime(), aInterval.endTime() );
      // // собственно запрос
      // for( Gwid gwid : item.gwids( false, coreApi ) ) {
      // ITimedList<IDtoCompletedCommand> cmds = cmdService.queryObjCommands( qi, gwid );
      // result.addAll( cmds );
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
  public IList<IDtoCompletedCommand> getResult() {
    // на самом деле, в списке только IDisplayableCommand
    return result;
  }

  @Override
  public void setQueryParams( ITimeInterval aInterval, IJournalQueryFilter aParams ) {
    interval = new QueryInterval( EQueryIntervalType.CSCE, aInterval.startTime(), aInterval.endTime() );
    params = aParams;
  }

  @Override
  public void run( IProgressMonitor aMonitor )
      throws InvocationTargetException,
      InterruptedException {
    aMonitor.beginTask( MSG_INFO_QUERIENG_CMDS, params.items().size() );

    // 2023-01-19 mvk
    result = execQuery( coreApi, interval, params );

    // // Делаем запрос на данные с обновлением индикатора прогресса
    // ISkCommandService cmdService = coreApi.cmdService();
    //
    // result = new SortedElemLinkedBundleList<>();
    //
    // long intervalStart1 = interval.startTime();
    // long intervalEnd1 = interval.endTime();
    //
    // System.out.println( "!!! Strart interval = " + new Date( intervalStart1 ) );
    // System.out.println( "!!! End interval = " + new Date( intervalEnd1 ) );
    //
    // // для каждого элемента из aParams.items() запросим службу IEventService
    // for( int i = 0, count = params.items().size(); i < count; i++ ) {
    // ConcerningEventsItem item = (ConcerningEventsItem)params.items().get( i );
    // // собственно запрос
    // // собственно запрос
    // for( Gwid gwid : item.gwids( false, coreApi ) ) {
    // long intervalStart = interval.startTime();
    // long intervalEnd = interval.endTime();
    // ITimedList<IDtoCompletedCommand> cmds = cmdService.queryObjCommands( interval, gwid );
    //
    // if( cmds.size() > 0 ) {
    // System.out.println( "Strart interval = " + new Date( intervalStart ) );
    // System.out.println( "End interval = " + new Date( intervalEnd ) );
    // System.out.println( "Events = " + cmds.size() );
    // }
    // result.addAll( cmds );
    // }
    //
    // aMonitor.worked( 1 );
    // }
    aMonitor.done();
  }

  // 2023-01-19 mvk
  @SuppressWarnings( "nls" )
  private IList<IDtoCompletedCommand> execQuery( ISkCoreApi aCoreApi, ITimeInterval aInterval,
      IJournalQueryFilter aParams ) {
    TsNullArgumentRtException.checkNulls( aInterval, aParams );
    // Служба запросов
    ISkHistoryQueryService queryService = aCoreApi.hqService();
    // Запрос
    query = queryService.createHistoricQuery( IOptionSet.NULL );
    // Результат
    TimedList<IDtoCompletedCommand> retValue = new TimedList<>();
    try {
      query.genericChangeEventer().addListener( aSource -> {
        ISkQueryRawHistory q = (ISkQueryRawHistory)aSource;
        pl( "CommandQueryEngine.query(...): queryId = %s, state = %s", q.queryId(), q.state() );
        if( q.state() == ESkQueryState.READY || q.state() == ESkQueryState.FAILED ) {
          synchronized (query) {
            query.notifyAll();
          }
        }
        if( q.state() == ESkQueryState.READY ) {
          for( Gwid gwid : q.listGwids() ) {
            pl( "CommandQueryEngine.query(...):  gwid = %s, value count = %d", gwid,
                Integer.valueOf( q.get( gwid ).size() ) );
          }
        }
      } );
      GwidList gwids = new GwidList();
      for( int i = 0, count = aParams.items().size(); i < count; i++ ) {
        ConcerningEventsItem item = (ConcerningEventsItem)aParams.items().get( i );
        gwids.addAll( item.gwids( false, aCoreApi ) );
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
