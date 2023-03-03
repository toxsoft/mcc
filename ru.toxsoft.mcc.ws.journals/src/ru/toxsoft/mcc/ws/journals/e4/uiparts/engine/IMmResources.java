package ru.toxsoft.mcc.ws.journals.e4.uiparts.engine;

/**
 * Локализуемые ресурсы.
 *
 * @author dima
 */
@SuppressWarnings( "nls" )
interface IMmResources {

  /**
   * {@link EventM5Model},
   */
  String EVENTS_LIST_TABLE_DESCR = Messages.EVENTS_LIST_TABLE_DESCR;
  String EVENTS_LIST_TABLE_NAME  = Messages.EVENTS_LIST_TABLE_NAME;
  String EVENT_SRC_COL_DESCR     = Messages.EVENT_SRC_COL_DESCR;
  String EVENT_SRC_COL_NAME      = Messages.EVENT_SRC_COL_NAME;
  String EVENT_TIME_COL_DESCR    = Messages.EVENT_TIME_COL_DESCR;
  String EVENT_TIME_COL_NAME     = Messages.EVENT_TIME_COL_NAME;
  String EVENT_NAME_COL_DESCR    = Messages.EVENT_NAME_COL_DESCR;
  String EVENT_NAME_COL_NAME     = Messages.EVENT_NAME_COL_NAME;
  String DESCRIPTION_STR         = Messages.DESCRIPTION_STR;
  String EV_DESCRIPTION          = Messages.EV_DESCRIPTION;
  String MSG_INFO_QUERIENG_CMDS  = Messages.MSG_INFO_QUERIENG_CMDS;
  String ERR_QUERY_CMDS_FAILED   = "Ошибка выполнения запроса событий: %s";

  /**
   * {@link DialogConcerningEventsParams},
   */
  String STR_L_CLASSES               = Messages.STR_L_CLASSES;
  String STR_BTN_CLEAR_FILTER        = Messages.STR_BTN_CLEAR_FILTER;
  String STR_L_EVENTS                = Messages.STR_L_EVENTS;
  String STR_L_COMMANDS              = Messages.STR_L_COMMANDS;
  String STR_L_OBJECTS               = Messages.STR_L_OBJECTS;
  String STR_COL_OBJ_NAME            = Messages.STR_COL_OBJ_NAME;
  String STR_COL_EVENTS              = Messages.STR_COL_EVENTS;
  String STR_COL_COMMANDS            = Messages.STR_COL_COMMANDS;
  String DLG_T_CEP_EDIT              = Messages.DLG_T_CEP_EDIT;
  String DLG_C_CEP_EDIT              = Messages.DLG_C_CEP_EDIT;
  String STR_P_CHECK_ALL             = Messages.STR_P_CHECK_ALL;
  String STR_P_UNCHECK_ALL           = Messages.STR_P_UNCHECK_ALL;
  String CLASSES_N_PARAMS_TREE_TITLE = Messages.CLASSES_N_PARAMS_TREE_TITLE;
  String ENTITY_TREE_TITLE           = Messages.ENTITY_TREE_TITLE;

  /**
   * {@link EventQueryEngine}
   */
  String MSG_INFO_QUERIENG_EVENTS = Messages.MSG_INFO_QUERIENG_EVENTS;
  String ERR_QUERY_EVENTS_FAILED  = "Ошибка выполнения запроса событий: %s";

  /**
   * {@link JournalParamsPanel}, { JournalCmdParamsPanel}
   */
  String STR_P_TEXT_START_TIME = Messages.STR_P_TEXT_START_TIME;
  String STR_P_TEXT_DURATION   = Messages.STR_P_TEXT_DURATION;
  String STR_L_JPP_NAME        = Messages.STR_L_JPP_NAME;
  String STR_SEARCH_BTN_NAME   = Messages.STR_SEARCH_BTN_NAME;
  String DLG_T_EXPORT_FILE     = Messages.DLG_T_EXPORT_FILE;

  /**
   * {@link CommandM5Model},
   */
  String CMDS_LIST_TABLE_DESCR = Messages.CMDS_LIST_TABLE_DESCR;
  String CMDS_LIST_TABLE_NAME  = Messages.CMDS_LIST_TABLE_NAME;
  String CMD_EXEC_COL_DESCR    = Messages.CMD_EXEC_COL_DESCR;
  String CMD_EXEC_COL_NAME     = Messages.CMD_EXEC_COL_NAME;
  String AUTHOR_COL_DESCR      = Messages.AUTHOR_COL_DESCR;
  String AUTHOR_COL_NAME       = Messages.AUTHOR_COL_NAME;
  String TIME_COL_DESCR        = Messages.TIME_COL_DESCR;
  String TIME_COL_NAME         = Messages.TIME_COL_NAME;
  String VIS_NAME_COL_DESCR    = Messages.VIS_NAME_COL_DESCR;
  String VIS_NAME_COL_NAME     = Messages.VIS_NAME_COL_NAME;

  /**
   * {@link ObjectsTreeComposite},
   */
  String STR_SEARCH = Messages.STR_SEARCH;

  /**
   * Относительный путь к иконке "Фильтра запроса событий".
   */
  String ICON_FILTER    = "icons/is24x24/filter.png"; //$NON-NLS-1$
  /**
   * Относительный путь к иконке "Запуск запроса".
   */
  String ICON_RUN_QUERY = "icons/is24x24/run.png";    //$NON-NLS-1$

  /**
   * Относительный путь к иконке "Экспорт в Excel".
   */
  String ICON_EXCEL = "icons/is16x16/page_excel.png"; //$NON-NLS-1$

  /**
   * Относительный путь к иконке "Печать".
   */
  String ICON_PRINT = "icons/is24x24/print.png"; //$NON-NLS-1$

}
