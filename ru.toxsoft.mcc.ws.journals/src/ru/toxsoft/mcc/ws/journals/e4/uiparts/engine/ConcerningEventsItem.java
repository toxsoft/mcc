package ru.toxsoft.mcc.ws.journals.e4.uiparts.engine;

import org.toxsoft.core.tslib.bricks.filter.*;
import org.toxsoft.core.tslib.bricks.strid.impl.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.core.tslib.gw.gwid.*;
import org.toxsoft.core.tslib.math.logicop.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.uskat.core.*;
import org.toxsoft.uskat.core.api.sysdescr.*;

/**
 * Элемент перечня .
 * <p>
 * Содержит перечень параметров (данные, события, команды - идентификаторы) одного класса и перечень объектов этого же
 * класса - источников событий.
 * <p>
 * Неизменяемый класс. TODO - будет заменён на фильтр {@link ITsCombiFilterParams}, когда соответствующйи сервис будет
 * принимать фильтры.
 *
 * @author goga, dima
 */
public final class ConcerningEventsItem
    implements ITsCombiFilterParams {

  private final String               classId;
  private final IStringListBasicEdit eventIds = new SortedStringLinkedBundleList();
  private final IStringListBasicEdit strids   = new SortedStringLinkedBundleList();

  /**
   * Конструктор со всеми инвариантами.
   *
   * @param aClassId String - идентификатор класса интересующих объектов и событий
   * @param aEventIds {@link IStringList} - идентификаторы событий класса aClassId или пустой список для всех событий
   * @param aObjIds {@link IStringList} - список интересующих объектов или пустой список для всех объектов класса
   *          aClassId
   * @throws TsNullArgumentRtException любой аргумент = null
   * @throws TsIllegalArgumentRtException aClassId не ИД-путь
   * @throws TsIllegalArgumentRtException любой из aEventIds не ИД-имя
   */
  public ConcerningEventsItem( String aClassId, IStringList aEventIds, IStringList aObjIds ) {
    classId = StridUtils.checkValidIdPath( aClassId );
    TsNullArgumentRtException.checkNulls( aEventIds, aObjIds );
    for( String s : aEventIds ) {
      StridUtils.checkValidIdName( s );
      if( !eventIds.hasElem( s ) ) {
        eventIds.add( s );
      }
    }
    for( String s : aObjIds ) {
      StridUtils.checkValidIdName( s );
      if( !strids.hasElem( s ) ) {
        strids.add( s );
      }
    }
  }

  /**
   * Возвращает идентификатор класса интересующих объектов и событий.
   *
   * @return String - идентификатор класса интересующих объектов и событий
   */
  public String classId() {
    return classId;
  }

  /**
   * Возвращает идентификаторы событий класса aClassId или пустой список для всех событий.
   *
   * @return {@link IStringList} - идентификаторы событий класса aClassId или пустой список для всех событий
   */
  public IStringList eventIds() {
    return eventIds;
  }

  /**
   * Возвращает список интересующих объектов или пустой список для всех объектов класса aClassId.
   *
   * @return {@link IStringList} - список интересующих объектов или пустой список для всех объектов класса aClassId
   */
  public IStringList strids() {
    return strids;
  }

  /**
   * Возвращает список интересующих Gwid.
   *
   * @param isEvents boolean - события или команда
   * @param aServerApi ISkCoreApi - апи обращения к серверу
   * @return {@link GwidList} - список интересующих Gwid
   */
  public GwidList gwids( boolean isEvents, ISkCoreApi aServerApi ) {

    IStringListEdit filterObjs = new StringArrayList();
    IStringListEdit filterProps = new StringArrayList();

    if( eventIds().size() == 0 ) {
      filterProps.add( Gwid.STR_MULTI_ID );
    }
    else {
      // описание класса
      ISkClassInfo classInfo = aServerApi.sysdescr().getClassInfo( classId() );

      // количество события/команд класса
      int classPropsSize = isEvents ? classInfo.events().list().size() : classInfo.cmds().list().size();

      // если выбраны все команды/события
      if( classPropsSize == eventIds().size() ) {
        filterProps.add( Gwid.STR_MULTI_ID );
      }
      else {
        // иначе фильтровать по выбранным
        for( String eventId : eventIds() ) {
          filterProps.add( eventId );
        }
      }
    }

    if( strids().size() == 0 ) {
      filterObjs.add( Gwid.STR_MULTI_ID );
    }
    else {
      // TODO можно вставить проверку на количество всех объектов
      for( String strid : strids() ) {
        filterObjs.add( strid );
      }
    }

    GwidList evGwidList = new GwidList();
    for( String filterProp : filterProps ) {
      for( String filterObj : filterObjs ) {
        evGwidList.add( isEvents ? Gwid.createEvent( classId(), filterObj, filterProp )
            : Gwid.createCmd( classId(), filterObj, filterProp ) );
      }
    }

    return evGwidList;
  }

  // ------------------------------------------------------------------------------------
  // Реализация методов класса Object
  //

  @Override
  public String toString() {
    return classId + ":events[" + eventIds.size() + "]:objects[" + strids.size() + "]"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
  }

  @Override
  public boolean equals( Object aObj ) {
    if( aObj == this ) {
      return true;
    }
    if( aObj instanceof ConcerningEventsItem that ) {
      if( classId.equals( that.classId ) ) {
        return eventIds.equals( that.eventIds ) && strids.equals( that.strids );
      }
    }
    return false;
  }

  @Override
  public int hashCode() {
    int result = INITIAL_HASH_CODE;
    result = PRIME * result + classId.hashCode();
    result = PRIME * result + eventIds.hashCode();
    result = PRIME * result + strids.hashCode();
    return result;
  }

  //
  // -----------------------------------------------------------------
  // Для совместимости

  @Override
  public boolean isSingle() {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public ITsSingleFilterParams single() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public ITsCombiFilterParams left() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public ELogicalOp op() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public ITsCombiFilterParams right() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public boolean isInverted() {
    // TODO Auto-generated method stub
    return false;
  }

  /**
   * Начальное значение при вычилении хеш-кода.
   * <p>
   * Для использования см. комментарии к {@link #PRIME}.
   *
   * @see #PRIME
   */
  public static final int INITIAL_HASH_CODE = 1;

  /**
   * Простое число, используемое как множитель при добавлении хеш-кода очередного поля объекта (элемента коллекции).
   * <p>
   * Пример подсчета хеш-кода:
   *
   * <pre>
   * ...
   * &#064;Override
   * public int hashCode() {
   *   int result = CollectionsUtils.INITIAL_HASH_CODE;
   *   result = CollectionsUtils.PRIME * result + <b>objectField</b>.hashCode();
   *   result = CollectionsUtils.PRIME * result + (<b>booleanField</b> ? 1 : 0);
   *   result = CollectionsUtils.PRIME * result + <b>intField</b>;
   *   result = CollectionsUtils.PRIME * result + (int)(<b>longField</b> ^ (<b>longField</b> >>> 32));
   *   int fltval = Float.floatToRawIntBits( <b>floatField</b> );
   *   result = CollectionsUtils.PRIME * result + fltval;
   *   long dblval = Double.doubleToRawLongBits( <b>doubleField</b> );
   *   result = CollectionsUtils.PRIME * result + (int)(dblval ^ (dblval >>> 32));
   *   result = CollectionsUtils.PRIME * result + <b>stringField</b>.hashCode();
   *   return result;
   * }
   * </pre>
   */
  public static final int PRIME = 31;
}
