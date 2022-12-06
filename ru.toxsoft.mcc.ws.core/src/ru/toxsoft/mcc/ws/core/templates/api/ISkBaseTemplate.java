package ru.toxsoft.mcc.ws.core.templates.api;

import org.toxsoft.core.tsgui.chart.api.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.uskat.core.api.objserv.*;
import org.toxsoft.uskat.core.api.users.*;

/**
 * Interface to specify template of doc report.
 *
 * @author dima
 * @param <T> тип параметра отчета
 */

public interface ISkBaseTemplate<T extends ISkTemplateParam>
    extends ISkObject {

  /**
   * Returns the parameters of template.
   *
   * @return {@link IList}&lt;{@link ISkTemplateParam}&gt; - the parameters of template
   */
  IList<T> listParams();

  /**
   * Returns the author (user) of template.
   *
   * @return {@link ISkUser} - author of template
   */
  ISkUser author();

  /**
   * Determines title of report.
   *
   * @return String - title of report
   */
  default String title() {
    return attrs().getStr( ISkTemplateEditorServiceHardConstants.ATRID_TITLE );
  }

  /**
   * @return { @link ETimeUnit} - time step of aggregation
   */
  default ETimeUnit aggrStep() {
    return attrs().getValobj( ISkTemplateEditorServiceHardConstants.ATRID_AGGR_STEP );
  }

}
