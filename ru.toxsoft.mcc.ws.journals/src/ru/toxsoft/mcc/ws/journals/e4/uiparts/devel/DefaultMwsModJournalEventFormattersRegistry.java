package ru.toxsoft.mcc.ws.journals.e4.uiparts.devel;

import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.gw.gwid.*;

/**
 * Реестер форматтеров для отображения событий системы в журнале.
 *
 * @author max
 */
public class DefaultMwsModJournalEventFormattersRegistry
    implements IMwsModJournalEventFormattersRegistry {

  private IMapEdit<Gwid, IMwsModJournalEventFormatter> formattersMap = new ElemMap<>();

  @Override
  public IMwsModJournalEventFormatter find( String aClassId, String aEventId ) {
    Gwid gwid = Gwid.createEvent( aClassId, aEventId );
    return formattersMap.findByKey( gwid );
  }

  @Override
  public IMap<Gwid, IMwsModJournalEventFormatter> formattersMap() {
    return formattersMap;
  }

  @Override
  public void registerFomatter( Gwid aEventGwid, IMwsModJournalEventFormatter aFormatter ) {
    formattersMap.put( aEventGwid, aFormatter );
  }

  @Override
  public void unregisterFormatter( Gwid aEventGwid ) {
    formattersMap.removeByKey( aEventGwid );
  }
}
