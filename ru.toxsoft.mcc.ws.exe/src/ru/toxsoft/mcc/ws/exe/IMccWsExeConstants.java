package ru.toxsoft.mcc.ws.exe;

import static ru.toxsoft.mcc.ws.exe.IMccResources.*;

import java.time.Month;

import org.toxsoft.core.tsgui.mws.appinf.ITsApplicationInfo;
import org.toxsoft.core.tsgui.mws.appinf.TsApplicationInfo;
import org.toxsoft.core.tslib.utils.TsVersion;

/**
 * Application common constants.
 *
 * @author hazard157
 */
@SuppressWarnings( "javadoc" )
public interface IMccWsExeConstants {

  // ------------------------------------------------------------------------------------
  // App info

  String APP_ID    = "ru.toxsoft.mcc"; //$NON-NLS-1$
  String APP_ALIAS = "mcc";            //$NON-NLS-1$

  // ------------------------------------------------------------------------------------
  // Icons

  String PREFIX_OF_ICON_FIELD_NAME = "ICONID_";  //$NON-NLS-1$
  String ICONID_APP_ICON           = "app-icon"; //$NON-NLS-1$

  // ------------------------------------------------------------------------------------
  // App constants

  TsVersion          APP_VERSION = new TsVersion( 1, 0, 2022, Month.DECEMBER, 22 );
  ITsApplicationInfo APP_INFO    =
      new TsApplicationInfo( APP_ID, STR_N_APP_INFO, STR_D_APP_INFO, APP_ALIAS, APP_VERSION );

}
