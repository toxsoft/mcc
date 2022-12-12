package ru.toxsoft.mcc.ws.core.templates.api;

import org.toxsoft.core.tslib.coll.*;

/**
 * Interface to specify list parameters for template of graph report. <br>
 *
 * @author dima
 */

public interface ISkGraphParamsList {

  /**
   * @return list of template parameters
   */
  IList<ISkGraphParam> items();

}
