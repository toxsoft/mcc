package ru.toxsoft.mcc.ws.core.templates;

import org.eclipse.e4.core.contexts.*;
import org.toxsoft.core.tsgui.bricks.quant.*;
import org.toxsoft.core.tsgui.chart.api.*;
import org.toxsoft.core.tslib.utils.valobj.*;
import org.toxsoft.uskat.base.gui.km5.*;

import ru.toxsoft.mcc.ws.core.*;
import ru.toxsoft.mcc.ws.core.templates.api.*;
import ru.toxsoft.mcc.ws.core.templates.api.impl.*;
import ru.toxsoft.mcc.ws.core.templates.gui.m5.*;

/**
 * The libtary quant.
 *
 * @author hazard157
 */
public class QuantSkReportTemplate
    extends AbstractQuant {

  /**
   * Constructor.
   */
  public QuantSkReportTemplate() {
    super( QuantSkReportTemplate.class.getSimpleName() );
    TsValobjUtils.registerKeeper( EAggregationFunc.KEEPER_ID, EAggregationFunc.KEEPER );
    TsValobjUtils.registerKeeper( EDisplayFormat.KEEPER_ID, EDisplayFormat.KEEPER );
    TsValobjUtils.registerKeeper( ETimeUnit.KEEPER_ID, ETimeUnit.KEEPER );
    TsValobjUtils.registerKeeper( SkGraphParamsList.KEEPER_ID, SkGraphParamsList.KEEPER );
    KM5Utils.registerContributorCreator( KM5TemplateContributor.CREATOR );
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
