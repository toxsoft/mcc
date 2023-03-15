package ru.toxsoft.mcc.ws.mnemos.e4.addons;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.toxsoft.core.tsgui.bricks.quant.IQuantRegistrator;
import org.toxsoft.core.tsgui.mws.bases.MwsAbstractAddon;
import org.toxsoft.core.tsgui.valed.impl.ValedControlFactoriesRegistry;

import ru.toxsoft.mcc.ws.mnemos.Activator;
import ru.toxsoft.mcc.ws.mnemos.IMccWsMnemosConstants;
import ru.toxsoft.mcc.ws.mnemos.app.rt.chart.data_aliases.QuantDataAliases;
import ru.toxsoft.mcc.ws.mnemos.app.valed.ValedAvBooleanCheckAdv;
import ru.toxsoft.mcc.ws.mnemos.app.valed.ValedBooleanCheckAdv;
import ru.toxsoft.mcc.ws.mnemos.app.valed_unneeded.ValedIntegerTextCommand;

/**
 * Plugin addon.
 *
 * @author vs
 */
public class AddonMccMnemos
    extends MwsAbstractAddon {

  /**
   * Constructor.
   */
  public AddonMccMnemos() {
    super( Activator.PLUGIN_ID );
  }

  @Override
  protected void initApp( IEclipseContext aAppContext ) {
    // nop
  }

  @Override
  protected void doRegisterQuants( IQuantRegistrator aQuantRegistrator ) {
    aQuantRegistrator.registerQuant( new QuantDataAliases() );
  }

  @Override
  protected void initWin( IEclipseContext aWinContext ) {
    IMccWsMnemosConstants.init( aWinContext );

    ValedControlFactoriesRegistry fr = aWinContext.get( ValedControlFactoriesRegistry.class );
    fr.registerFactory( ValedBooleanCheckAdv.FACTORY );
    fr.registerFactory( ValedAvBooleanCheckAdv.FACTORY );
    fr.registerFactory( ValedIntegerTextCommand.FACTORY );
  }
}
