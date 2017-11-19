package ru.toxsoft.mcc.ws.launcher.rcp.app.addons;

import static ru.toxsoft.mcc.ws.launcher.rcp.app.addons.IMccResources.*;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.basic.MTrimmedWindow;
import org.eclipse.e4.ui.model.application.ui.basic.MWindow;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;

import ru.toxsoft.mcc.ws.launcher.rcp.Activator;
import ru.toxsoft.mcc.ws.launcher.rcp.app.IMccAppGuiConstants;
import ru.toxsoft.mws.base.IMwsGuiConstants;
import ru.toxsoft.tsgui.appcontext.services.resources.impl.TsIconManagerUtils;
import ru.toxsoft.tsgui.e4.app.addons.AbstractTsE4Addon;
import ru.toxsoft.tsgui.quants.QuantManager;
import ru.toxsoft.tsgui.utils.icons.EIconSize;

/**
 * Аддон приложения Psx4.
 *
 * @author goga
 */
public class AddonMccRcp
    extends AbstractTsE4Addon {

  /**
   * Конструктор.
   */
  public AddonMccRcp() {
    super( Activator.PLUGIN_ID );
  }

  @Override
  protected void doRegisterQuants( QuantManager aQuantManager ) {
    // nop
  }

  @Override
  protected void initApp( IEclipseContext aAppContext ) {

    // задаем значок окна и приложения
    MApplication app = aAppContext.get( MApplication.class );
    EModelService modelService = aAppContext.get( EModelService.class );
    MTrimmedWindow mainWindow = (MTrimmedWindow)modelService.find( IMwsGuiConstants.MWSID_WINDOW_MAIN, app );
    mainWindow.setIconURI( TsIconManagerUtils.makeStdIconUriString( getPluginId(),
        IMccAppGuiConstants.ICON_MCC_APP_ICON, EIconSize.IS_48X48 ) );

    // начальные размеры окна
    Display display = aAppContext.get( Display.class );
    Rectangle dBounds = display.getBounds();
    mainWindow.setX( dBounds.x + 8 );
    mainWindow.setY( 0 );
    mainWindow.setWidth( dBounds.width - 4 * 8 );
    mainWindow.setHeight( dBounds.height );

  }

  @Override
  protected void initWin( IEclipseContext aWinContext, MWindow aWindow ) {
    // инициализация констант GUI
    IMccAppGuiConstants.init( aWinContext );

    updateWindowsTitle( aWinContext );
  }

  // ------------------------------------------------------------------------------------
  // Внутренние методы
  //

  void updateWindowsTitle( IEclipseContext aWinContext ) {
    MWindow window = aWinContext.get( MWindow.class );
    window.setLabel( WINDOW_TITLE );
  }

}
