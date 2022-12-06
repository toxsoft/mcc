package ru.toxsoft.mcc.ws.core.templates.api;

import org.toxsoft.core.tsgui.graphics.colors.*;

/**
 * Interface to specify one parameter for template of graph report. <br>
 * TODO это шаблон для описания одного параметра отображаемого на графике в виде линии, а еще может быть график
 * состояния (например, АВТ/РУЧ) и график событий (подача анода вверх)
 *
 * @author dima
 */

public interface ISkGraphParam
    extends ISkTemplateParam {

  /**
   * @return { link @ETsColor} color of line of parameter
   */
  ETsColor color();

  /**
   * @return { link @ETsColor} color of line of parameter
   */
  int lineWidth();

  /**
   * @return id of unit for Y scale
   */
  String unitId();

  /**
   * @return name of unit for Y scale
   */
  String unitName();

  /**
   * @return draw as ladder
   */
  boolean isLadder();

}
