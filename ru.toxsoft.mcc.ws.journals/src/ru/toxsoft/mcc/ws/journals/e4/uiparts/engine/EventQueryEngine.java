package ru.toxsoft.mcc.ws.journals.e4.uiparts.engine;

import static org.toxsoft.uskat.base.gui.utils.SkQueryProgressDialogUtils.*;
import static ru.toxsoft.mcc.ws.journals.e4.uiparts.engine.IMmResources.*;

import org.eclipse.swt.widgets.Shell;
import org.toxsoft.core.tsgui.dialogs.TsDialogUtils;
import org.toxsoft.core.tslib.av.impl.AvUtils;
import org.toxsoft.core.tslib.av.opset.IOptionSetEdit;
import org.toxsoft.core.tslib.av.opset.impl.OptionSet;
import org.toxsoft.core.tslib.av.opset.impl.OptionSetUtils;
import org.toxsoft.core.tslib.bricks.ctx.ITsContext;
import org.toxsoft.core.tslib.bricks.time.*;
import org.toxsoft.core.tslib.bricks.time.impl.QueryInterval;
import org.toxsoft.core.tslib.bricks.time.impl.TimedList;
import org.toxsoft.core.tslib.coll.IList;
import org.toxsoft.core.tslib.gw.gwid.Gwid;
import org.toxsoft.core.tslib.gw.gwid.GwidList;
import org.toxsoft.core.tslib.utils.errors.TsNullArgumentRtException;
import org.toxsoft.uskat.base.gui.conn.ISkConnectionSupplier;
import org.toxsoft.uskat.core.ISkCoreApi;
import org.toxsoft.uskat.core.api.evserv.SkEvent;
import org.toxsoft.uskat.core.api.hqserv.*;

/**
 * Реализация движка {@link IQueryEngine} для событий.
 *
 * @author mvk
 */
public class EventQueryEngine
    implements IQueryEngine<SkEvent> {

  /**
   * Таймаут (мсек) запроса событий. < 0: бесконечно
   */
  private static final long EVENT_QUERY_TIMEOUT = -1;

  private final Shell      shell;
  private final ISkCoreApi coreApi;

  /**
   * Конструктор.
   *
   * @param aContext {@link ITsContext} контекст
   * @throws TsNullArgumentRtException аргумент = null
   */
  public EventQueryEngine( ITsContext aContext ) {
    TsNullArgumentRtException.checkNull( aContext );
    shell = aContext.get( Shell.class );
    coreApi = aContext.get( ISkConnectionSupplier.class ).defConn().coreApi();
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса IEventQueryEngine
  //
  @Override
  public IList<SkEvent> query( ITimeInterval aInterval, IJournalQueryFilter aParams ) {
    TsNullArgumentRtException.checkNulls( aInterval, aParams );
    if( aParams.items().isEmpty() ) {
      return IList.EMPTY;
    }
    GwidList gwids = new GwidList();
    for( int i = 0, count = aParams.items().size(); i < count; i++ ) {
      ConcerningEventsItem item = (ConcerningEventsItem)aParams.items().get( i );
      gwids.addAll( item.gwids( true, coreApi ) );
    }
    // Параметры запроса
    IOptionSetEdit options = new OptionSet( OptionSetUtils.createOpSet( //
        ISkHistoryQueryServiceConstants.OP_SK_MAX_EXECUTION_TIME, AvUtils.avInt( EVENT_QUERY_TIMEOUT ) //
    ) );
    // Формирование запроса
    ISkQueryRawHistory query = coreApi.hqService().createHistoricQuery( options );
    try {
      // Подготовка запроса
      query.prepare( gwids );
      // Настройка обработки результатов запроса
      TimedList<SkEvent> retValue = new TimedList<>();
      query.genericChangeEventer().addListener( aSource -> {
        ISkQueryRawHistory q = (ISkQueryRawHistory)aSource;
        if( q.state() == ESkQueryState.READY ) {
          for( Gwid gwid : query.listGwids() ) {
            retValue.addAll( query.get( gwid ) );
          }
        }
        if( q.state() == ESkQueryState.FAILED ) {
          String stateMessage = q.stateMessage();
          TsDialogUtils.error( shell, ERR_QUERY_EVENTS_FAILED, stateMessage );
        }
      } );
      // Интервал запроса
      IQueryInterval interval =
          new QueryInterval( EQueryIntervalType.CSCE, aInterval.startTime(), aInterval.endTime() );
      // Выполение запроса в прогресс-диалоге
      execQueryByProgressDialog( shell, MSG_INFO_QUERIENG_EVENTS, query, interval, EVENT_QUERY_TIMEOUT );

      return retValue;
    }
    finally {
      query.close();
    }
  }
}
