package ru.toxsoft.mcc.ws.core.e4.addons;

import org.eclipse.e4.core.contexts.*;
import org.toxsoft.core.tsgui.bricks.quant.*;
import org.toxsoft.core.tsgui.mws.bases.*;
import org.toxsoft.uskat.base.gui.*;

import ru.toxsoft.mcc.ws.core.*;
import ru.toxsoft.mcc.ws.core.Activator;
import ru.toxsoft.mcc.ws.core.templates.*;

/**
 * Plugin adoon.
 *
 * @author hazard157
 */
public class AddonMccWsCore
    extends MwsAbstractAddon {

  /**
   * Constructor.
   */
  public AddonMccWsCore() {
    super( Activator.PLUGIN_ID );
  }

  // ------------------------------------------------------------------------------------
  // MwsAbstractAddon
  //

  @Override
  protected void doRegisterQuants( IQuantRegistrator aQuantRegistrator ) {
    aQuantRegistrator.registerQuant( new QuantSkBaseGui() );
    aQuantRegistrator.registerQuant( new QuantSkReportTemplate() );

  }

  @Override
  protected void initApp( IEclipseContext aAppContext ) {
    // nop
  }

  @Override
  protected void initWin( IEclipseContext aWinContext ) {
    IMccWsCoreConstants.init( aWinContext );
  }

}
