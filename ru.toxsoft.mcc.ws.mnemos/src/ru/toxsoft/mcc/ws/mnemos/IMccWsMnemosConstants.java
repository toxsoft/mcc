package ru.toxsoft.mcc.ws.mnemos;

import org.eclipse.e4.core.contexts.*;
import org.toxsoft.core.tsgui.graphics.icons.*;

/**
 * Plugin constants.
 *
 * @author vs
 */
@SuppressWarnings( "javadoc" )
public interface IMccWsMnemosConstants {

  // ------------------------------------------------------------------------------------
  // Icons

  String PREFIX_OF_ICON_FIELD_NAME = "ICONID_"; //$NON-NLS-1$
  // String ICONID_APP_ICON = "app-icon"; //$NON-NLS-1$

  String ICONID_CHECK_FALSE = "check-false"; //$NON-NLS-1$
  String ICONID_CHECK_TRUE  = "check-true";  //$NON-NLS-1$

  String ICONID_GRAY_LAMP   = "gray-lamp";   //$NON-NLS-1$
  String ICONID_GREEN_LAMP  = "green-lamp";  //$NON-NLS-1$
  String ICONID_ORANGE_LAMP = "orange-lamp"; //$NON-NLS-1$
  String ICONID_RED_LAMP    = "red-lamp";    //$NON-NLS-1$
  String ICONID_YELLOW_LAMP = "yellow-lamp"; //$NON-NLS-1$

  /**
   * Constants registration.
   *
   * @param aWinContext {@link IEclipseContext} - windows level context
   */
  static void init( IEclipseContext aWinContext ) {
    ITsIconManager iconManager = aWinContext.get( ITsIconManager.class );
    iconManager.registerStdIconByIds( Activator.PLUGIN_ID, IMccWsMnemosConstants.class, PREFIX_OF_ICON_FIELD_NAME );
  }

}
