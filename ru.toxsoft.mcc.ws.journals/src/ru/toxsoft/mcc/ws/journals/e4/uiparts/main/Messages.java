package ru.toxsoft.mcc.ws.journals.e4.uiparts.main;

import org.eclipse.osgi.util.*;

@SuppressWarnings( "javadoc" )
public class Messages
    extends NLS {

  private static final String BUNDLE_NAME = "ru.toxsoft.mcc.ws.journals.e4.uiparts.main.messages"; //$NON-NLS-1$
  public static String        AUTHOR_STR;
  public static String        CMDS_STR;
  public static String        DATE_STR;
  public static String        EVENTS_STR;
  public static String        PRINT_EVENT_LIST_TITLE_FORMAT;
  static {
    // initialize resource bundle
    NLS.initializeMessages( BUNDLE_NAME, Messages.class );
  }

  private Messages() {
  }
}
