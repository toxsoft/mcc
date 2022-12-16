package ru.toxsoft.mcc.ws.mnemos.app.rt.chart.data_aliases;

import org.toxsoft.core.tslib.gw.gwid.*;

/**
 * Interface to specify one data name alias.
 *
 * @author dima
 */

public interface IDataNameAlias {

  /**
   * @return Gwid { @link Gwid} green world id of parameter
   */
  Gwid gwid();

  /**
   * @return String title of parameter
   */
  String title();

  /**
   * @return String description of parameter
   */
  String description();

}
