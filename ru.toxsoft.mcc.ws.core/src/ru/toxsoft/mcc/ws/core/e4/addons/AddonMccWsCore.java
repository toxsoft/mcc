package ru.toxsoft.mcc.ws.core.e4.addons;

import org.eclipse.e4.core.contexts.*;
import org.toxsoft.core.tsgui.bricks.quant.*;
import org.toxsoft.core.tsgui.mws.bases.*;
import org.toxsoft.core.tsgui.valed.impl.*;
import org.toxsoft.uskat.base.gui.*;
import org.toxsoft.uskat.base.gui.km5.*;

import ru.toxsoft.mcc.ws.core.*;
import ru.toxsoft.mcc.ws.core.Activator;
import ru.toxsoft.mcc.ws.core.templates.*;
import ru.toxsoft.mcc.ws.core.templates.gui.m5.*;
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
    KM5Utils.registerContributorCreator( KM5TemplateContributor.CREATOR );

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
    // TODO вынести в отдельный реестр, Гога должен подготовить
    // ISkConnectionSupplier cs = aWinContext.get( ISkConnectionSupplier.class );
    // TsInternalErrorRtException.checkNull( cs );
    // ISkConnection conn = cs.defConn();
    // TsInternalErrorRtException.checkNull( conn );
    //
    // conn.coreApi().addService( SkReportTemplateService.CREATOR );
    // conn.coreApi().addService( SkGraphTemplateService.CREATOR );
    // // обязательно нужно для того чтобы знать автора
    // ISkGraphTemplateService graphTemplateService = conn.coreApi().getService( ISkGraphTemplateService.SERVICE_ID );
    // graphTemplateService.setConnection( conn );
    // ISkReportTemplateService reportTemplService = conn.coreApi().getService( ISkReportTemplateService.SERVICE_ID );
    // reportTemplService.setConnection( conn );

    ValedControlFactoriesRegistry vcReg = aWinContext.get( ValedControlFactoriesRegistry.class );
    vcReg.registerFactory( ValedGwidEditor.FACTORY );
    vcReg.registerFactory( ValedAvValobjGwidEditor.FACTORY );
    vcReg.registerFactory( ValedSkidEditor.FACTORY );
    vcReg.registerFactory( ValedAvValobjSkidEditor.FACTORY );

  }

}
