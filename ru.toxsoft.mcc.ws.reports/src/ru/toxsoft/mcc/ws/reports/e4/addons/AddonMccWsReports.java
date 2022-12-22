package ru.toxsoft.mcc.ws.reports.e4.addons;

import org.eclipse.e4.core.contexts.*;
import org.toxsoft.core.tsgui.mws.bases.*;

import ru.toxsoft.mcc.ws.reports.*;

/**
 * Plugin adoon.
 *
 * @author hazard157
 */
public class AddonMccWsReports
    extends MwsAbstractAddon {

  /**
   * Constructor.
   */
  public AddonMccWsReports() {
    super( Activator.PLUGIN_ID );
  }

  // ------------------------------------------------------------------------------------
  // MwsAbstractAddon
  //

  @Override
  protected void initApp( IEclipseContext aAppContext ) {
    // nop
  }

  @Override
  protected void initWin( IEclipseContext aWinContext ) {
    IMccWsReportsConstants.init( aWinContext );
  }

}
