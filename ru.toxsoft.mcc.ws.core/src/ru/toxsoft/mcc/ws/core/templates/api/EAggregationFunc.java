package ru.toxsoft.mcc.ws.core.templates.api;

import static ru.toxsoft.mcc.ws.core.templates.api.ISkResources.*;

import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.keeper.std.*;
import org.toxsoft.core.tslib.bricks.strid.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * The enumeration of aggregation functions.
 *
 * @author dima
 */
public enum EAggregationFunc
    implements IStridable {

  /**
   * Calculate average value of series values
   */
  AVERAGE( "AVERAGE", STR_N_AVERAGE, STR_D_AVERAGE ), //$NON-NLS-1$

  /**
   * Calculate min value of series values.
   */
  MIN( "MIN", STR_N_MIN, STR_D_MIN ), //$NON-NLS-1$

  /**
   * Calculate max value of series values.
   */
  MAX( "MAX", STR_N_MAX, STR_D_MAX ), //$NON-NLS-1$

  /**
   * Calculate sum value of series values.
   */
  SUM( "SUM", STR_N_SUM, STR_D_SUM ), //$NON-NLS-1$

  /**
   * Calculate count value of series values.
   */
  COUNT( "COUNT", STR_N_COUNT, STR_D_COUNT ); //$NON-NLS-1$

  /**
   * The keeper ID.
   */
  public static final String KEEPER_ID = "EAggregationFunc"; //$NON-NLS-1$

  /**
   * Keeper singleton.
   */
  public static final IEntityKeeper<EAggregationFunc> KEEPER = new StridableEnumKeeper<>( EAggregationFunc.class );

  private static IStridablesListEdit<EAggregationFunc> list = null;

  private final String id;
  private final String name;
  private final String description;

  EAggregationFunc( String aId, String aName, String aDescription ) {
    id = aId;
    name = aName;
    description = aDescription;
  }

  // --------------------------------------------------------------------------
  // IStridable
  //

  @Override
  public String id() {
    return id;
  }

  @Override
  public String nmName() {
    return name;
  }

  @Override
  public String description() {
    return description;
  }

  // ----------------------------------------------------------------------------------
  // API
  //

  /**
   * Returns all constants in single list.
   *
   * @return {@link IStridablesList}&lt; {@link EAggregationFunc} &gt; - list of constants in order of declaraion
   */
  public static IStridablesList<EAggregationFunc> asList() {
    if( list == null ) {
      list = new StridablesList<>( values() );
    }
    return list;
  }

  /**
   * Returns the constant by the ID.
   *
   * @param aId String - the ID
   * @return {@link EAggregationFunc} - found constant
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemNotFoundRtException no constant found by specified ID
   */
  public static EAggregationFunc getById( String aId ) {
    return asList().getByKey( aId );
  }

  /**
   * Finds the constant by the name.
   *
   * @param aName String - the name
   * @return {@link EAggregationFunc} - found constant or <code>null</code>
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static EAggregationFunc findByName( String aName ) {
    TsNullArgumentRtException.checkNull( aName );
    for( EAggregationFunc item : values() ) {
      if( item.name.equals( aName ) ) {
        return item;
      }
    }
    return null;
  }

  /**
   * Returns the constant by the name.
   *
   * @param aName String - the name
   * @return {@link EAggregationFunc} - found constant
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemNotFoundRtException no constant found by specified name
   */
  public static EAggregationFunc getByName( String aName ) {
    return TsItemNotFoundRtException.checkNull( findByName( aName ) );
  }

}
