package ru.toxsoft.mcc.ws.core.templates;

import org.eclipse.e4.core.contexts.*;
import org.toxsoft.core.tsgui.bricks.quant.*;
import org.toxsoft.core.tsgui.chart.api.*;
import org.toxsoft.core.tslib.utils.valobj.*;

import ru.toxsoft.mcc.ws.core.templates.api.*;

/**
 * The libtary quant.
 *
 * @author hazard157
 */
public class QuantSkReportTemplate
    extends AbstractQuant {

  public QuantSkReportTemplate() {
    super( QuantSkReportTemplate.class.getSimpleName() );
    TsValobjUtils.registerKeeper( EAggregationFunc.KEEPER_ID, EAggregationFunc.KEEPER );
    TsValobjUtils.registerKeeper( EDisplayFormat.KEEPER_ID, EDisplayFormat.KEEPER );
    TsValobjUtils.registerKeeper( ETimeUnit.KEEPER_ID, ETimeUnit.KEEPER );
  }

  @Override
  protected void doInitApp( IEclipseContext aAppContext ) {
    // nop
  }

  @Override
  protected void doInitWin( IEclipseContext aWinContext ) {
    // nop
  }

}
