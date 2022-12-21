package ru.toxsoft.mcc.ws.mnemos.app.controls;

import org.toxsoft.core.tslib.av.*;

/**
 * Интерфейс объекта способного завершить редактирование - обработать новое (отредактированное) значение.
 * <p>
 * Например, в одном случае необходимо установить значение атрибута, в другом послать соответствующую команду и т.п.
 *
 * @author vs
 */
public interface IEditingFinisher {

  /**
   * Совершает все необходимые действия при завершении редактирования.
   *
   * @param aNewValue IAtomicValue - новое (отредактированное) значение
   * @return <b>true</b> - редактирование было завершено успешно<br>
   *         <b>false</b> - не удалось завершить редактирование
   */
  boolean finishEditing( IAtomicValue aNewValue );
}
