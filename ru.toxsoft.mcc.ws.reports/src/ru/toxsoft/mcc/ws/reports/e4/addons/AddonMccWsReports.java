package ru.toxsoft.mcc.ws.reports.e4.addons;

import org.eclipse.e4.core.contexts.*;
import org.toxsoft.core.tsgui.mws.bases.*;
import org.toxsoft.core.tslib.bricks.strid.*;
import org.toxsoft.uskat.base.gui.conn.*;
import org.toxsoft.uskat.core.connection.*;
import org.toxsoft.uskat.onews.lib.*;

import ru.toxsoft.mcc.ws.reports.*;

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
  protected void initApp( IEclipseContext aAppContext ) {
    // nop
  }

  @Override
  protected void initWin( IEclipseContext aWinContext ) {
    IMccWsReportsConstants.init( aWinContext );
    // описываем здесь свои возможности
    ISkConnectionSupplier connSup = aWinContext.get( ISkConnectionSupplier.class );
    ISkConnection conn = connSup.defConn();
    ISkOneWsService ows = (ISkOneWsService)conn.coreApi().getService( ISkOneWsService.SERVICE_ID );
    for( IStridableParameterized abilityKind : ReportsAbilities.listAbilityKinds() ) {
      ows.defineAbilityKind( abilityKind );
    }
    for( IOneWsAbility ability : ReportsAbilities.listAbilities() ) {
      ows.defineAbility( ability );
    }

  }

}
