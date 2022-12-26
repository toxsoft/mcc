package ru.toxsoft.mcc.ws.journals.e4.uiparts.engine;

import org.toxsoft.core.tslib.bricks.filter.*;
import org.toxsoft.core.tslib.bricks.strio.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Редактируемая реализация {@link IConcerningEventsParams}.
 *
 * @author goga
 */
public class ConcerningEventsParams
    implements IConcerningEventsParams {

  private final IListEdit<ConcerningEventsItem> items = new ElemLinkedBundleList<>();

  /**
   * Пустой конструктор.
   */
  public ConcerningEventsParams() {
    // nop
  }

  // ------------------------------------------------------------------------------------
  // API редактирования
  //

  /**
   * Добавляет элемент в список {@link #items()}.
   *
   * @param aItem {@link ConcerningEventsItem} - добаляемый элемент
   * @throws TsNullArgumentRtException аргумент = null
   */
  public void addItem( ConcerningEventsItem aItem ) {
    if( !items.hasElem( aItem ) ) {
      items.add( aItem );
    }
  }

  /**
   * Удаляет элемент из списка {@link #items()}.
   *
   * @param aIndex int - индекс удаляемого элемента
   * @throws TsIllegalArgumentRtException byltrc ds[jlbn pf ljgecnbvst ghtltks
   */
  public void removeItem( int aIndex ) {
    items.removeByIndex( aIndex );
  }

  /**
   * Удаляет элемент из списка {@link #items()}.
   *
   * @param aItem ConcerningEventsItem - удаляемый элемент
   * @throws TsIllegalArgumentRtException нет такого элеимента в списке
   */
  public void removeItem( ConcerningEventsItem aItem ) {
    items.remove( aItem );
  }

  /**
   * Получить элемент по id класса.
   *
   * @param aClassId String id класса
   * @return описание параметров событий этого класса
   */
  public ConcerningEventsItem getItem( String aClassId ) {
    ConcerningEventsItem retVal = null;
    for( ConcerningEventsItem item : items ) {
      if( item.classId().compareTo( aClassId ) == 0 ) {
        retVal = item;
      }
    }
    return retVal;
  }

  /**
   * Возвращает все эелементы перечня параметров запроса исторических сущностей (события, команды и т.д.)
   *
   * @return IList<ConcerningEventsItem> - все эелементы перечня параметров запроса исторических сущностей (события,
   *         команды и т.д.)
   */
  public IList<ConcerningEventsItem> eventItems() {
    return items;
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса IConcerningEventsParams
  //

  @Override
  public IList<ITsCombiFilterParams> items() {
    IListEdit<ITsCombiFilterParams> result = new ElemLinkedBundleList<>();
    for( ConcerningEventsItem item : items ) {
      result.add( item );
    }
    return result;
  }

  @Override
  public void write( IStrioWriter aSw ) {
    ConcerningEventsItemKeeper.KEEPER.writeColl( aSw, items, true );
  }

  @Override
  public void read( IStrioReader aSr ) {
    IList<ConcerningEventsItem> ll = ConcerningEventsItemKeeper.KEEPER.readColl( aSr );
    items.setAll( ll );
  }

  public void clear() {
    items.clear();
  }

}
