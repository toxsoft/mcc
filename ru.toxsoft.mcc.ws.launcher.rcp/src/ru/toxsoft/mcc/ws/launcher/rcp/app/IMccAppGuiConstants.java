package ru.toxsoft.mcc.ws.launcher.rcp.app;

import org.eclipse.e4.core.contexts.IEclipseContext;

import ru.toxsoft.mcc.ws.launcher.rcp.Activator;
import ru.toxsoft.tsgui.appcontext.services.resources.ITsIconManager;

/**
 * Конснанты GUI приложения WS MCC.
 *
 * @author goga
 */
@SuppressWarnings( { "nls", "javadoc" } )
public interface IMccAppGuiConstants {

  String PREFIX_OF_ICON_FIELD_NAME = "ICON_MCC_";
  String ICON_MCC_APP_ICON         = "app-icon";

  static void init( IEclipseContext aWinContext ) {
    ITsIconManager iconManager = aWinContext.get( ITsIconManager.class );
    iconManager.registerStdIconByIds( Activator.PLUGIN_ID, IMccAppGuiConstants.class, PREFIX_OF_ICON_FIELD_NAME );
  }

}
