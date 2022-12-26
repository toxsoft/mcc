package ru.toxsoft.mcc.ws.journals.e4.uiparts.engine;

import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.keeper.std.*;
import org.toxsoft.core.tslib.bricks.strio.*;
import org.toxsoft.core.tslib.coll.primtypes.*;

/**
 * Хранитель объектов типа {@link ConcerningEventsItem}.
 *
 * @author goga
 */
public class ConcerningEventsItemKeeper
    extends AbstractEntityKeeper<ConcerningEventsItem> {

  /**
   * Экземпляр-синглтон хранителя.
   */
  public static final IEntityKeeper<ConcerningEventsItem> KEEPER = new ConcerningEventsItemKeeper();

  protected ConcerningEventsItemKeeper() {
    super( ConcerningEventsItem.class, EEncloseMode.ENCLOSES_BASE_CLASS, null );
  }

  @Override
  protected void doWrite( IStrioWriter aSw, ConcerningEventsItem aEntity ) {
    aSw.writeAsIs( aEntity.classId() );
    aSw.writeSeparatorChar();
    StringListKeeper.KEEPER.write( aSw, aEntity.eventIds() );
    aSw.writeSeparatorChar();
    StringListKeeper.KEEPER.write( aSw, aEntity.strids() );
  }

  @Override
  protected ConcerningEventsItem doRead( IStrioReader aSr ) {

    String classId = aSr.readIdPath();
    aSr.ensureSeparatorChar();
    IStringList eventIds = StringListKeeper.KEEPER.read( aSr );
    aSr.ensureSeparatorChar();
    IStringList strids = StringListKeeper.KEEPER.read( aSr );
    return new ConcerningEventsItem( classId, eventIds, strids );
  }
}
