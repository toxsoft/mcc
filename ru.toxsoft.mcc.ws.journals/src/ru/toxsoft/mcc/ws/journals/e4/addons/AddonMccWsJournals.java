package ru.toxsoft.mcc.ws.journals.e4.addons;

import org.eclipse.e4.core.contexts.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.mws.bases.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.gw.gwid.*;
import org.toxsoft.uskat.core.api.evserv.*;

import ru.toxsoft.mcc.ws.journals.*;
import ru.toxsoft.mcc.ws.journals.e4.uiparts.devel.*;

/**
 * Plugin adoon.
 *
 * @author hazard157
 */
public class AddonMccWsJournals
    extends MwsAbstractAddon {

  // ------------------------------------------------------------------------------------------------
  /**
   * Класс: система
   */
  private static final String CLS_SYSTEM = "sk.System";

  /**
   * Событие: потеря связи
   */
  private static final String EVENT_LOGIN_FAILED = "LoginFailed";

  /**
   * Событие: создана сессия
   */
  private static final String EVENT_SESSION_CREATED = "SessionCreated";

  /**
   * Событие: сессия закрыта
   */
  private static final String EVENT_SESSION_CLOSED = "SessionClosed";

  /**
   * Событие: потеря связи
   */
  private static final String EVENT_SESSION_BREAKED = "SessionBreaked";

  /**
   * Событие: восстановление связи
   */
  private static final String EVENT_SESSION_RESTORED = "SessionRestored";

  /**
   * Событие: изменение системного описания
   */
  private static final String EVENT_SESSION_CHANGED = "SysdescrChanged";

  /**
   * Constructor.
   */
  public AddonMccWsJournals() {
    super( Activator.PLUGIN_ID );
  }

  // ------------------------------------------------------------------------------------
  // MwsAbstractAddon
  //

  @Override
  protected void initApp( IEclipseContext aAppContext ) {
    IMwsModJournalEventFormattersRegistry eventFormattersRegistry =
        aAppContext.containsKey( IMwsModJournalEventFormattersRegistry.class )
            ? aAppContext.get( IMwsModJournalEventFormattersRegistry.class )
            : new DefaultMwsModJournalEventFormattersRegistry();

    aAppContext.set( IMwsModJournalEventFormattersRegistry.class, eventFormattersRegistry );

    Gwid evGwid = Gwid.createEvent( CLS_SYSTEM, EVENT_SESSION_CREATED );
    eventFormattersRegistry.registerFomatter( evGwid, new SessionChangeJournalEventFormatter() );
    evGwid = Gwid.createEvent( CLS_SYSTEM, EVENT_SESSION_CREATED );
    eventFormattersRegistry.registerFomatter( evGwid, new SessionChangeJournalEventFormatter() );
    evGwid = Gwid.createEvent( CLS_SYSTEM, EVENT_SESSION_CLOSED );
    eventFormattersRegistry.registerFomatter( evGwid, new SessionChangeJournalEventFormatter() );
    evGwid = Gwid.createEvent( CLS_SYSTEM, EVENT_SESSION_BREAKED );
    eventFormattersRegistry.registerFomatter( evGwid, new SessionChangeJournalEventFormatter() );
    evGwid = Gwid.createEvent( CLS_SYSTEM, EVENT_SESSION_RESTORED );
    eventFormattersRegistry.registerFomatter( evGwid, new SessionChangeJournalEventFormatter() );
    evGwid = Gwid.createEvent( CLS_SYSTEM, EVENT_LOGIN_FAILED );
  }

  @Override
  protected void initWin( IEclipseContext aWinContext ) {
    IMccWsJournalsConstants.init( aWinContext );
  }

  /**
   * Класс осуществляющий форматирование события изменение сессии соединения с сервером.
   *
   * @author Dima
   */
  static class SessionChangeJournalEventFormatter
      implements IMwsModJournalEventFormatter {

    private static final String STR_IP_ADDRESS  = "IP адрес: ";
    private static final String EVPARAM_IP_ADDR = "IP";

    @Override
    public String formatShortText( SkEvent aEvent, ITsGuiContext aContext ) {
      IOptionSet parvals = aEvent.paramValues();
      String ip = parvals.getStr( EVPARAM_IP_ADDR );

      return STR_IP_ADDRESS + ip;
    }

    @Override
    public String formatLongText( SkEvent aEvent, ITsGuiContext aContext ) {
      IOptionSet parvals = aEvent.paramValues();
      String ip = parvals.getStr( EVPARAM_IP_ADDR );

      return STR_IP_ADDRESS + ip;
    }

  }

}
