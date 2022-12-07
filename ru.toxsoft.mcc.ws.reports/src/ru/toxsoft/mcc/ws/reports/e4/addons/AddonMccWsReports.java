package ru.toxsoft.mcc.ws.reports.e4.addons;

import org.eclipse.e4.core.contexts.*;
import org.toxsoft.core.tsgui.bricks.quant.*;
import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.mws.bases.*;
import org.toxsoft.uskat.base.gui.*;
import org.toxsoft.uskat.base.gui.conn.*;
import org.toxsoft.uskat.core.connection.*;

import ru.toxsoft.mcc.ws.core.templates.api.impl.*;
import ru.toxsoft.mcc.ws.core.templates.gui.m5.*;
import ru.toxsoft.mcc.ws.reports.*;
import ru.toxsoft.mcc.ws.reports.Activator;
import ru.toxsoft.mcc.ws.reports.e4.uiparts.*;

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
  protected void doRegisterQuants( IQuantRegistrator aQuantRegistrator ) {
    aQuantRegistrator.registerQuant( new QuantSkBaseGui() );
  }

  @Override
  protected void initApp( IEclipseContext aAppContext ) {
    // nop
  }

  @Override
  protected void initWin( IEclipseContext aWinContext ) {
    IMccWsReportsConstants.init( aWinContext );

    // регистрируем картинки
    IReportTemplateConstants.init( aWinContext );
    ISkConnectionSupplier connSup = aWinContext.get( ISkConnectionSupplier.class );
    ISkConnection conn = connSup.defConn();

    conn.coreApi().addService( SkReportTemplateService.CREATOR );
    conn.coreApi().addService( SkGraphTemplateService.CREATOR );

    // регистрируем свои m5 модели
    IM5Domain m5 = aWinContext.get( IM5Domain.class );
    m5.addModel( new SkReportParamM5Model() );
    m5.addModel( new SkReportTemplateM5Model( conn ) );
    m5.addModel( new SkGraphParamM5Model() );
    m5.addModel( new SkGraphTemplateM5Model( conn ) );

  }

}
