package ru.toxsoft.mcc.ws.reports.e4.uiparts;

import org.eclipse.e4.core.contexts.*;
import org.toxsoft.core.tsgui.graphics.icons.*;

import ru.toxsoft.mcc.ws.reports.*;

/**
 * Plugin constants.
 *
 * @author dima
 */
@SuppressWarnings( "javadoc" )
public interface IReportTemplateConstants {

  // ------------------------------------------------------------------------------------
  // Icons

  String PREFIX_OF_ICON_FIELD_NAME = "ICON_";              //$NON-NLS-1$
  String ICON_USER                 = "user-red";           //$NON-NLS-1$
  String ICON_RUN                  = "run";                //$NON-NLS-1$
  String ICON_TEMPLATE             = "gdp-shablons";       //$NON-NLS-1$
  String ICON_REPORT_TEMPLATE      = "uipart-events";      //$NON-NLS-1$
  String ICON_GRAPH_TEMPLATE       = "uipart-ws-profiles"; //$NON-NLS-1$

  /**
   * Constants registration.
   *
   * @param aWinContext {@link IEclipseContext} - windows level context
   */
  static void init( IEclipseContext aWinContext ) {
    ITsIconManager iconManager = aWinContext.get( ITsIconManager.class );
    iconManager.registerStdIconByIds( Activator.PLUGIN_ID, IReportTemplateConstants.class, PREFIX_OF_ICON_FIELD_NAME );
  }

}
