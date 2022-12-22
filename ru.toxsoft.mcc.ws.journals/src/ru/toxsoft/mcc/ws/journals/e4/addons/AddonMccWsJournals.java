package ru.toxsoft.mcc.ws.journals.e4.addons;

import org.eclipse.e4.core.contexts.*;
import org.toxsoft.core.tsgui.mws.bases.*;

import ru.toxsoft.mcc.ws.journals.*;

/**
 * Plugin adoon.
 *
 * @author hazard157
 */
public class AddonMccWsJournals
    extends MwsAbstractAddon {

  /**
   * Constructor.
   */
  public AddonMccWsJournals() {
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
    IMccWsJournalsConstants.init( aWinContext );
  }

}
