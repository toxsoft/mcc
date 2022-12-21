package ru.toxsoft.mcc.ws.mnemos.app.rt.chart.data_aliases;

import org.eclipse.e4.core.contexts.*;
import org.toxsoft.core.tsgui.bricks.quant.*;
import org.toxsoft.core.tslib.utils.valobj.*;
import org.toxsoft.uskat.base.gui.km5.*;

import ru.toxsoft.mcc.ws.core.*;
import ru.toxsoft.mcc.ws.mnemos.app.rt.chart.data_aliases.impl.*;

/**
 * The libtary quant.
 *
 * @author dima
 */
public class QuantDataAliases
    extends AbstractQuant {

  /**
   * Constructor.
   */
  public QuantDataAliases() {
    super( QuantDataAliases.class.getSimpleName() );
    TsValobjUtils.registerKeeper( DataNameAliasesList.KEEPER_ID, DataNameAliasesList.KEEPER );
    KM5Utils.registerContributorCreator( KM5DataAliasesContributor.CREATOR );
  }

  @Override
  protected void doInitApp( IEclipseContext aAppContext ) {
    // nop
  }

  @Override
  protected void doInitWin( IEclipseContext aWinContext ) {
    IMccWsCoreConstants.init( aWinContext );
  }

}
