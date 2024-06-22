package ru.toxsoft.mcc.ws.mnemos.app.rt.chart.data_aliases;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.toxsoft.core.tsgui.bricks.quant.AbstractQuant;
import org.toxsoft.core.tslib.utils.valobj.TsValobjUtils;
import org.toxsoft.uskat.core.gui.km5.KM5Utils;

import ru.toxsoft.mcc.ws.mnemos.app.rt.chart.data_aliases.impl.DataNameAliasesList;
import ru.toxsoft.mcc.ws.mnemos.app.rt.chart.data_aliases.impl.KM5DataAliasesContributor;

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
    // 2023-06-06 mvk TODO: ???
    // IVtWsCoreConstants.init( aWinContext );
  }

}
