package ru.toxsoft.mcc.ws.journals.e4.uiparts.engine;

import static ru.toxsoft.mcc.ws.journals.e4.uiparts.engine.IMmResources.*;

import java.lang.reflect.*;

import org.eclipse.core.runtime.*;
import org.toxsoft.core.tslib.bricks.time.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.gw.gwid.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.uskat.core.*;
import org.toxsoft.uskat.core.api.cmdserv.*;
import org.toxsoft.uskat.s5.legacy.*;

/**
 * Реализация движка {@link IQueryEngine}.
 *
 * @author dima
 */
public class CommandQueryEngine
    implements IQueryEngine<IDtoCompletedCommand> {

  private final ISkCoreApi                     coreApi;
  private IListBasicEdit<IDtoCompletedCommand> result;

  private IQueryInterval      interval;
  private IJournalQueryFilter params;

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
      result = new SortedElemLinkedBundleList<>();
      ISkCommandService cmdService = coreApi.cmdService();
      // для каждого элемента из aParams.items() запросим службу команд
      for( int i = 0, count = aParams.items().size(); i < count; i++ ) {
        ConcerningEventsItem item = (ConcerningEventsItem)aParams.items().get( i );
        // проверим, прекращает ли пользователь обработку запроса
        double donePercents = (100.0 * i) / (count);
        if( aCallback.onNextStep( donePercents ) ) {
          break;
        }
        IQueryInterval qi = new QueryInterval( EQueryIntervalType.CSCE, aInterval.startTime(), aInterval.endTime() );
        // собственно запрос
        for( Gwid gwid : item.gwids( false, coreApi ) ) {
          ITimedList<IDtoCompletedCommand> cmds = cmdService.queryObjCommands( qi, gwid );
          result.addAll( cmds );
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
    // Делаем запрос на данные с обновлением индикатора прогресса
    ISkCommandService cmdService = coreApi.cmdService();

    result = new SortedElemLinkedBundleList<>();
    // для каждого элемента из aParams.items() запросим службу IEventService
    for( int i = 0, count = params.items().size(); i < count; i++ ) {
      ConcerningEventsItem item = (ConcerningEventsItem)params.items().get( i );
      // собственно запрос
      // собственно запрос
      for( Gwid gwid : item.gwids( false, coreApi ) ) {
        ITimedList<IDtoCompletedCommand> cmds = cmdService.queryObjCommands( interval, gwid );
        result.addAll( cmds );
      }

      aMonitor.worked( 1 );
    }
    aMonitor.done();
  }
}