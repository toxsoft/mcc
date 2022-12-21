package ru.toxsoft.mcc.ws.mnemos.app.rt.chart.data_aliases.impl;

import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.keeper.AbstractEntityKeeper.*;
import org.toxsoft.core.tslib.bricks.strio.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;

import ru.toxsoft.mcc.ws.core.templates.api.*;
import ru.toxsoft.mcc.ws.mnemos.app.rt.chart.data_aliases.*;

/**
 * {@link ISkGraphParamsList} mutable implementation.
 *
 * @author dima
 */
public final class DataNameAliasesList
    implements IDataNameAliasesList {

  /**
   * Value-object keeper identifier.
   */
  public static final String KEEPER_ID = "DataNameAliasesList"; //$NON-NLS-1$

  /**
   * Keeper of list.
   */
  public static final IEntityKeeper<IDataNameAliasesList> KEEPER =
      new AbstractEntityKeeper<>( IDataNameAliasesList.class, EEncloseMode.NOT_IN_PARENTHESES, null ) {

        @Override
        protected void doWrite( IStrioWriter aSw, IDataNameAliasesList aEntity ) {
          DataNameAlias.KEEPER.writeColl( aSw, aEntity.items(), false );
        }

        @Override
        protected IDataNameAliasesList doRead( IStrioReader aSr ) {
          return new DataNameAliasesList( DataNameAlias.KEEPER.readColl( aSr ) );
        }
      };

  private final IListEdit<IDataNameAlias> items = new ElemArrayList<>();

  /**
   * Constructor.
   */
  public DataNameAliasesList() {
    // nop
  }

  /**
   * Constructor.
   *
   * @param aList {@link IDataNameAlias} список aliases
   */
  public DataNameAliasesList( IList<IDataNameAlias> aList ) {
    items.addAll( aList );
  }

  @Override
  public IListEdit<IDataNameAlias> items() {
    return items;
  }
}
