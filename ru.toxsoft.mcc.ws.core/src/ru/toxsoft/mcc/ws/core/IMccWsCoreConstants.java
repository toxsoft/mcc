package ru.toxsoft.mcc.ws.core;

import org.eclipse.e4.core.contexts.*;
import org.toxsoft.core.tsgui.graphics.icons.*;

/**
 * Plugin constants.
 *
 * @author hazard157
 */
@SuppressWarnings( "javadoc" )
public interface IMccWsCoreConstants {

  String MCC_ID = "mcc"; //$NON-NLS-1$

  // ------------------------------------------------------------------------------------
  // E4

  String PERSPID_MCC_MAIN = "ru.toxsoft.mcc.persp.mcc_main"; //$NON-NLS-1$

  // ------------------------------------------------------------------------------------
  // Icons

  String PREFIX_OF_ICON_FIELD_NAME = "ICONID_";  //$NON-NLS-1$
  String ICONID_APP_ICON           = "app-icon"; //$NON-NLS-1$

  /**
   * Constants registration.
   *
   * @param aWinContext {@link IEclipseContext} - windows level context
   */
  static void init( IEclipseContext aWinContext ) {
    ITsIconManager iconManager = aWinContext.get( ITsIconManager.class );
    iconManager.registerStdIconByIds( Activator.PLUGIN_ID, IMccWsCoreConstants.class, PREFIX_OF_ICON_FIELD_NAME );
    //
  }

}
