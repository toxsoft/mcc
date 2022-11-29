package ru.toxsoft.mcc.ws.mnemos.app.controls;

import org.toxsoft.core.tslib.av.*;

/**
 * Преобразователь атомарного значения в строку для проекта МосКокс.
 * <p>
 *
 * @author vs
 */
public interface IMccAvValueFormatter {

  /**
   * Возвращает строковое представление атомарного значения.
   *
   * @param aValue IAtomicValue - значение для преобразование (м.б. null)
   * @return String - строковое представление значения
   */
  String formatValue( IAtomicValue aValue );
}
