package ru.toxsoft.mcc.ws.module.schemes.app.addons;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.ui.model.application.ui.basic.MWindow;

import ru.toxsoft.mcc.ws.module.schemes.Activator;
import ru.toxsoft.tsgui.e4.app.addons.AbstractTsE4Addon;

/**
 * Адон модуля.
 *
 * @author goga
 */
public class AddonMccWsModuleSchemes
    extends AbstractTsE4Addon {

  /**
   * Конструктор.
   */
  public AddonMccWsModuleSchemes() {
    super( Activator.PLUGIN_ID );
  }

  @Override
  protected void initApp( IEclipseContext aAppContext ) {
    // nop
  }

  @Override
  protected void initWin( IEclipseContext aWinContext, MWindow aWindow ) {
    // nop
  }

}
