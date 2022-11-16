package ru.toxsoft.mcc.ws.mnemos.app.controls;

import org.eclipse.swt.graphics.*;

/**
 * Интерфейс элемента, который может сам себя отрисовывать.
 * <p>
 *
 * @author vs
 */
public interface ISelfPaintable {

  /**
   * Отрисовывает элемент с использованием переданного контекста.
   *
   * @param aGc GC - графический контекст
   */
  void paint( GC aGc );
}
