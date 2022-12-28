package ru.toxsoft.mcc.ws.exe.e4.addons;

import static org.toxsoft.core.tsgui.graphics.icons.EIconSize.*;
import static ru.toxsoft.mcc.ws.core.IMccWsCoreConstants.*;

import org.eclipse.e4.core.contexts.*;
import org.eclipse.e4.ui.model.application.*;
import org.eclipse.e4.ui.model.application.ui.basic.*;
import org.eclipse.e4.ui.workbench.modeling.*;
import org.toxsoft.core.tsgui.bricks.quant.*;
import org.toxsoft.core.tsgui.graphics.icons.impl.*;
import org.toxsoft.core.tsgui.mws.*;
import org.toxsoft.core.tsgui.mws.Activator;
import org.toxsoft.core.tsgui.mws.bases.*;
import org.toxsoft.uskat.onews.gui.*;
import org.toxsoft.uskat.users.gui.*;

/**
 * Application addon.
 *
 * @author hazard157
 */
public class AddonMccWsExe
    extends MwsAbstractAddon {

  /**
   * Constructor.
   */
  public AddonMccWsExe() {
    super( Activator.PLUGIN_ID );
  }

  @Override
  protected void doRegisterQuants( IQuantRegistrator aQuantRegistrator ) {
    // регистрация М5 моделей работы с пользователями и правами доступа
    aQuantRegistrator.registerQuant( new QuantSkUsersGui() );
    aQuantRegistrator.registerQuant( new QuantSkOneWsGui() );
  }

  @Override
  protected void initApp( IEclipseContext aAppContext ) {
    // application and main window icon
    MApplication app = aAppContext.get( MApplication.class );
    EModelService modelService = aAppContext.get( EModelService.class );
    MTrimmedWindow mainWindow = (MTrimmedWindow)modelService.find( IMwsCoreConstants.MWSID_WINDOW_MAIN, app );
    mainWindow.setIconURI( TsIconManagerUtils.makeStdIconUriString( ru.toxsoft.mcc.ws.core.Activator.PLUGIN_ID,
        ICONID_APP_ICON, IS_48X48 ) );
  }

  @Override
  protected void initWin( IEclipseContext aWinContext ) {
    // nop
  }

}
