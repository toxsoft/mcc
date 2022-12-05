package ru.toxsoft.mcc.ws.reports.lib;

import org.toxsoft.core.tslib.coll.helpers.*;
import org.toxsoft.uskat.core.*;

/**
 * Listener to the {@link ISkGraphTemplateService}.
 *
 * @author dima
 */
public interface ISkGraphTemplateServiceListener {

  /**
   * Called when any change in templates occur.
   *
   * @param aCoreApi {@link ISkCoreApi} - the event source
   * @param aOp {@link ECrudOp} - the kind of change
   * @param aGraphTemplateId String - affected graph template or <code>null</code> for batch changes
   *          {@link ECrudOp#LIST}
   */
  void onGraphTemplateChanged( ISkCoreApi aCoreApi, ECrudOp aOp, String aGraphTemplateId );

}
