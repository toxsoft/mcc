package ru.toxsoft.mcc.ws.core.templates.api;

import org.toxsoft.core.tslib.coll.helpers.*;
import org.toxsoft.uskat.core.*;

/**
 * Listener to the {@link ISkReportTemplateService}.
 *
 * @author dima
 */
public interface ISkReportTemplateServiceListener {

  /**
   * Called when any change in templates occur.
   *
   * @param aCoreApi {@link ISkCoreApi} - the event source
   * @param aOp {@link ECrudOp} - the kind of change
   * @param aReportTemplateId String - affected report template or <code>null</code> for batch changes
   *          {@link ECrudOp#LIST}
   */
  void onReportTemplateChanged( ISkCoreApi aCoreApi, ECrudOp aOp, String aReportTemplateId );

}
