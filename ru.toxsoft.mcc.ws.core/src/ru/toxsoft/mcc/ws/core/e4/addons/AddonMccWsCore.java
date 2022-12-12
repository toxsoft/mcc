package ru.toxsoft.mcc.ws.core.e4.addons;

import org.eclipse.e4.core.contexts.*;
import org.toxsoft.core.tsgui.bricks.quant.*;
import org.toxsoft.core.tsgui.mws.bases.*;
import org.toxsoft.core.tsgui.valed.impl.*;
import org.toxsoft.uskat.base.gui.*;
import org.toxsoft.uskat.core.impl.*;

import ru.toxsoft.mcc.ws.core.*;
import ru.toxsoft.mcc.ws.core.Activator;
import ru.toxsoft.mcc.ws.core.templates.*;
import ru.toxsoft.mcc.ws.core.templates.api.impl.*;
import ru.toxsoft.mcc.ws.core.templates.gui.valed.*;

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
    // KM5Utils.registerContributorCreator( KM5TemplateContributor.CREATOR );

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

    SkCoreUtils.registerSkServiceCreator( SkReportTemplateService.CREATOR );
    SkCoreUtils.registerSkServiceCreator( SkGraphTemplateService.CREATOR );

    // ISkConnectionSupplier connSup = aWinContext.get( ISkConnectionSupplier.class );
    // ISkConnection conn = connSup.defConn();
    //
    // // регистрируем свои m5 модели
    // IM5Domain m5 = aWinContext.get( IM5Domain.class );
    // m5.addModel( new SkReportParamM5Model() );
    // m5.addModel( new SkReportTemplateM5Model( conn ) );
    // m5.addModel( new SkGraphParamM5Model() );
    // m5.addModel( new SkGraphTemplateM5Model( conn ) );

    ValedControlFactoriesRegistry vcReg = aWinContext.get( ValedControlFactoriesRegistry.class );
    vcReg.registerFactory( ValedGwidEditor.FACTORY );
    vcReg.registerFactory( ValedAvValobjGwidEditor.FACTORY );
    vcReg.registerFactory( ValedSkidEditor.FACTORY );
    vcReg.registerFactory( ValedAvValobjSkidEditor.FACTORY );

  }

}
