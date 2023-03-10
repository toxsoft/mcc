package ru.toxsoft.mcc.ws.exe;

import static ru.toxsoft.mcc.ws.exe.IMccResources.*;

import java.time.*;

import org.toxsoft.core.tsgui.mws.appinf.*;
import org.toxsoft.core.tslib.utils.*;

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

  TsVersion APP_VERSION = new TsVersion( 1, 0, 2022, Month.DECEMBER, 22 );

  ITsApplicationInfo APP_INFO = new TsApplicationInfo( APP_ID, STR_N_APP_INFO, STR_D_APP_INFO, APP_ALIAS, APP_VERSION );

}
