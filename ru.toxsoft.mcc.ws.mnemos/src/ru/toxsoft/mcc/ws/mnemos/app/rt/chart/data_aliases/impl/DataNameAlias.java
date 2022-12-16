package ru.toxsoft.mcc.ws.mnemos.app.rt.chart.data_aliases.impl;

import static org.toxsoft.core.tslib.bricks.strio.IStrioHardConstants.*;

import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.keeper.AbstractEntityKeeper.*;
import org.toxsoft.core.tslib.bricks.strio.*;
import org.toxsoft.core.tslib.gw.gwid.*;

import ru.toxsoft.mcc.ws.mnemos.app.rt.chart.data_aliases.*;

/**
 * {@link IDataNameAlias} immutable implementation.
 *
 * @author dima
 */
public final class DataNameAlias
    implements IDataNameAlias {

  /**
   * Value-object keeper identifier.
   */
  public static final String KEEPER_ID = "DataNameAlias"; //$NON-NLS-1$

  /**
   * Keeper singleton.
   */
  public final static IEntityKeeper<IDataNameAlias> KEEPER =
      new AbstractEntityKeeper<>( IDataNameAlias.class, EEncloseMode.NOT_IN_PARENTHESES, null ) {

        @Override
        protected void doWrite( IStrioWriter aSw, IDataNameAlias aEntity ) {
          // пишем Gwid
          Gwid.KEEPER.write( aSw, aEntity.gwid() );
          aSw.writeChar( CHAR_ITEM_SEPARATOR );
          // name
          aSw.writeQuotedString( aEntity.title() );
          aSw.writeChar( CHAR_ITEM_SEPARATOR );
          // description
          aSw.writeQuotedString( aEntity.description() );
        }

        @Override
        protected IDataNameAlias doRead( IStrioReader aSr ) {
          Gwid gwid = Gwid.KEEPER.read( aSr );
          aSr.ensureChar( CHAR_ITEM_SEPARATOR );
          String title = aSr.readQuotedString();
          aSr.ensureChar( CHAR_ITEM_SEPARATOR );
          String descr = aSr.readQuotedString();
          return new DataNameAlias( gwid, title, descr );
        }
      };

  protected final Gwid   gwid;
  protected final String title;
  protected final String description;

  /**
   * Constructor.
   *
   * @param aGwid {@link Gwid} green world id of that parameter
   * @param aTitle name of parameter
   * @param aDescr description of parameter
   */
  public DataNameAlias( Gwid aGwid, String aTitle, String aDescr ) {
    gwid = aGwid;
    title = aTitle;
    description = aDescr;
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  @Override
  public Gwid gwid() {
    return gwid;
  }

  @Override
  public String title() {
    return title;
  }

  @Override
  public String description() {
    return description;
  }

}
