package ru.toxsoft.mcc.ws.reports.lib;

import org.toxsoft.core.tsgui.chart.api.*;
import org.toxsoft.core.tslib.gw.gwid.*;

/**
 * Interface to specify one parameter for template.
 *
 * @author dima
 */

public interface ISkTemplateParam {

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

  /**
   * @return EAggregationFunc { @link EAggregationFunc} function of aggregation
   */
  EAggregationFunc aggrFunc();

  /**
   * @return EDisplayFormat { @link EDisplayFormat} format to display value
   */
  EDisplayFormat displayFormat();

}
