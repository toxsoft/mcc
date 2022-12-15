package ru.toxsoft.mcc.ws.core.templates.api.impl;

import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.keeper.AbstractEntityKeeper.*;
import org.toxsoft.core.tslib.bricks.strio.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;

import ru.toxsoft.mcc.ws.core.templates.api.*;

/**
 * {@link ISkGraphParamsList} mutable implementation.
 *
 * @author dima
 */
public final class SkGraphParamsList
    implements ISkGraphParamsList {

  /**
   * Value-object keeper identifier.
   */
  public static final String KEEPER_ID = "SkGraphParamsList"; //$NON-NLS-1$

  /**
   * Keeper of list.
   */
  public static final IEntityKeeper<ISkGraphParamsList> KEEPER =
      new AbstractEntityKeeper<>( ISkGraphParamsList.class, EEncloseMode.NOT_IN_PARENTHESES, null ) {

        @Override
        protected void doWrite( IStrioWriter aSw, ISkGraphParamsList aEntity ) {
          SkGraphParam.KEEPER.writeColl( aSw, aEntity.items(), false );
        }

        @Override
        protected ISkGraphParamsList doRead( IStrioReader aSr ) {
          return (ISkGraphParamsList)SkGraphParam.KEEPER.readColl( aSr );
        }
      };

  private final IListEdit<ISkGraphParam> items = new ElemArrayList<>();

  /**
   * Constructor.
   */
  public SkGraphParamsList() {
    // nop
  }

  /**
   * Constructor.
   *
   * @param aList {@link ISkGraphParam} список параметров
   */
  public SkGraphParamsList( IList<ISkGraphParam> aList ) {
    items.addAll( aList );
  }

  @Override
  public IListEdit<ISkGraphParam> items() {
    return items;
  }
}
