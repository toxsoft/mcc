package ru.toxsoft.mcc.ws.mnemos.app.rt;

import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.bricks.strid.*;
import org.toxsoft.core.tslib.gw.gwid.*;
import org.toxsoft.core.tslib.utils.*;

/**
 * "Потребитель" данных реального времени.
 * <p>
 *
 * @author vs
 */
public interface IRtDataConsumer
    extends IStridable {

  /**
   * Возвращает список конкретных ИДов данных реального времени, которые он "потребляет".
   *
   * @return IGwidList - список конкретных ИДов данных реального времени, которые он "потребляет"
   */
  IGwidList listNeededGwids();

  // /**
  // * Передает новое значение РВ-данного.<br>
  // *
  // * @param aGwid Gwid - конкретный ИД РВ-данного
  // * @param aValue IAtomicValue - новое значение РВ данного
  // */
  // void setValue( Gwid aGwid, IAtomicValue aValue );
  //
  // /**
  // * Передает новые значения РВ данных потребителю в виде карты, где ключ - ИД РВ данного.
  // * <p>
  // *
  // * @param aValues IMap&lt;Gwid, IAtomicValue> - карта новых значений РВ данных, где ключ - ИД РВ данного
  // */
  // void setValues( IMap<Gwid, IAtomicValue> aValues );

  /**
   * Передает новые значения РВ данных потребителю в виде 2-х массивов одинаковой длины.
   * <p>
   *
   * @param aGwids Gwid[] - массив ИДов РВ данных
   * @param aValues IAtomicValue[] - массив новых значений РВ данных
   * @param aCount int - количество данных
   */
  void setValues( Gwid[] aGwids, IAtomicValue[] aValues, int aCount );

  @Override
  default String nmName() {
    return TsLibUtils.EMPTY_STRING;
  }

  @Override
  default String description() {
    return TsLibUtils.EMPTY_STRING;
  }

}
