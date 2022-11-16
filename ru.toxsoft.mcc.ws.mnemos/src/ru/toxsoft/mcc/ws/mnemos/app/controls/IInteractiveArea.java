package ru.toxsoft.mcc.ws.mnemos.app.controls;

import org.toxsoft.core.tsgui.graphics.cursors.*;

/**
 * Активная, чувствительная для мыши область.
 * <p>
 *
 * @author vs
 */
public interface IInteractiveArea {

  /**
   * Определяет, принадлежит ли точка элементу
   *
   * @param aX int - x координата точки
   * @param aY int - y координата точки
   * @return <b>true</b> - точка принадлежит элементу<br>
   *         <b>false</b> - точка находится вне элемента
   */
  boolean contains( int aX, int aY );

  /**
   * Возвращает тип курсора мыши для случая, когда он находится над данной областью.
   *
   * @return ECursorType - тип курсора мыши для случая когда он находится над данной областью
   */
  ECursorType cursorType();
}
