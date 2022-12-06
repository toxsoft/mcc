package ru.toxsoft.mcc.ws.mnemos.app.controls;

import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.keeper.std.*;
import org.toxsoft.core.tslib.bricks.strid.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Опорная точка (точка привяки) прямоугольника.
 * <p>
 * От слова Fulcrum - точка опоры рычага.
 *
 * @author hazard157
 */
@SuppressWarnings( { "nls", "javadoc" } )
public enum EReversibleEngineState
    implements IStridable {

  UNKNOWN( "unknown", "Входные данные отсутствуют", "Неопределено" ),

  INVALID( "invalid", "Недопустимая комбинация значений входных данных", "Недопустимое состояние" ),

  FAULT( "fault", "Авария", "Авария" ),

  OPENED( "opened", "Полностью открыто", "Открыто" ),

  CLOSED( "closed", "Полностью закрыто", "Закрыто" ),

  OPENING( "opening", "В процессе открывания", "Открывается" ),

  CLOSING( "closing", "В процессе закрывания", "Закрывается" ),

  PARTIALLY_OPENED( "partiallyOpened", "Частично открыто", "Промежуточное" );

  /**
   * Keeper ID.
   */
  public static final String KEEPER_ID = "EReversibleEngineState"; //$NON-NLS-1$

  /**
   * Keeper singleton.
   */
  public static IEntityKeeper<EReversibleEngineState> KEEPER =
      new StridableEnumKeeper<>( EReversibleEngineState.class );

  private final String id;
  private final String description;
  private final String name;

  EReversibleEngineState( String aId, String aDescr, String aName ) {
    id = aId;
    description = aDescr;
    name = aName;
  }

  // --------------------------------------------------------------------------
  // IStridable
  //

  @Override
  public String id() {
    return id;
  }

  @Override
  public String description() {
    return description;
  }

  @Override
  public String nmName() {
    return name;
  }

  // ----------------------------------------------------------------------------------
  // Методы проверки
  //

  /**
   * Определяет, существует ли константа перечисления с заданным идентификатором.
   *
   * @param aId String - идентификатор искомой константы
   * @return boolean - признак существования константы <br>
   *         <b>true</b> - константа с заданным идентификатором существует;<br>
   *         <b>false</b> - неет константы с таким идентификатором.
   * @throws TsNullArgumentRtException аргумент = null
   */
  public static boolean isItemById( String aId ) {
    return findByIdOrNull( aId ) != null;
  }

  /**
   * Определяет, существует ли константа перечисления с заданным описанием.
   *
   * @param aDescription String - описание искомой константы
   * @return boolean - признак существования константы <br>
   *         <b>true</b> - константа с заданным описанием существует;<br>
   *         <b>false</b> - неет константы с таким описанием.
   * @throws TsNullArgumentRtException аргумент = null
   */
  public static boolean isItemByDescription( String aDescription ) {
    return findByDescriptionOrNull( aDescription ) != null;
  }

  /**
   * Определяет, существует ли константа перечисления с заданным именем.
   *
   * @param aName String - имя (название) искомой константы
   * @return boolean - признак существования константы <br>
   *         <b>true</b> - константа с заданным именем существует;<br>
   *         <b>false</b> - неет константы с таким именем.
   * @throws TsNullArgumentRtException аргумент = null
   */
  public static boolean isItemByName( String aName ) {
    return findByNameOrNull( aName ) != null;
  }

  // ----------------------------------------------------------------------------------
  // Методы поиска
  //

  /**
   * Возвращает константу по идентификатору или null.
   *
   * @param aId String - идентификатор искомой константы
   * @return ETsRectFulcrum - найденная константа, или null если нет константы с таимк идентификатором
   * @throws TsNullArgumentRtException аргумент = null
   */
  public static EReversibleEngineState findByIdOrNull( String aId ) {
    TsNullArgumentRtException.checkNull( aId );
    for( EReversibleEngineState item : values() ) {
      if( item.id.equals( aId ) ) {
        return item;
      }
    }
    return null;
  }

  /**
   * Возвращает константу по идентификатору или выбрасывает исключение.
   *
   * @param aId String - идентификатор искомой константы
   * @return ETsRectFulcrum - найденная константа
   * @throws TsNullArgumentRtException аргумент = null
   * @throws TsItemNotFoundRtException нет константы с таким идентификатором
   */
  public static EReversibleEngineState findById( String aId ) {
    return TsItemNotFoundRtException.checkNull( findByIdOrNull( aId ) );
  }

  /**
   * Возвращает константу по описанию или null.
   *
   * @param aDescription String - описание искомой константы
   * @return ETsRectFulcrum - найденная константа, или null если нет константы с таким описанием
   * @throws TsNullArgumentRtException аргумент = null
   */
  public static EReversibleEngineState findByDescriptionOrNull( String aDescription ) {
    TsNullArgumentRtException.checkNull( aDescription );
    for( EReversibleEngineState item : values() ) {
      if( item.description.equals( aDescription ) ) {
        return item;
      }
    }
    return null;
  }

  /**
   * Возвращает константу по описанию или выбрасывает исключение.
   *
   * @param aDescription String - описание искомой константы
   * @return ETsRectFulcrum - найденная константа
   * @throws TsNullArgumentRtException аргумент = null
   * @throws TsItemNotFoundRtException нет константы с таким описанием
   */
  public static EReversibleEngineState findByDescription( String aDescription ) {
    return TsItemNotFoundRtException.checkNull( findByDescriptionOrNull( aDescription ) );
  }

  /**
   * Возвращает константу по имени или null.
   *
   * @param aName String - имя искомой константы
   * @return ETsRectFulcrum - найденная константа, или null если нет константы с таким именем
   * @throws TsNullArgumentRtException аргумент = null
   */
  public static EReversibleEngineState findByNameOrNull( String aName ) {
    TsNullArgumentRtException.checkNull( aName );
    for( EReversibleEngineState item : values() ) {
      if( item.name.equals( aName ) ) {
        return item;
      }
    }
    return null;
  }

  /**
   * Возвращает константу по имени или выбрасывает исключение.
   *
   * @param aName String - имя искомой константы
   * @return ETsRectFulcrum - найденная константа
   * @throws TsNullArgumentRtException аргумент = null
   * @throws TsItemNotFoundRtException нет константы с таким именем
   */
  public static EReversibleEngineState findByName( String aName ) {
    return TsItemNotFoundRtException.checkNull( findByNameOrNull( aName ) );
  }

}
