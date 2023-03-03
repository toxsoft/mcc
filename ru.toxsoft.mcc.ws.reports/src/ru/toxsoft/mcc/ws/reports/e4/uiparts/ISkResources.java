package ru.toxsoft.mcc.ws.reports.e4.uiparts;

import ru.toxsoft.mcc.ws.core.templates.gui.PanelGwidSelector;

/**
 * Localizable resources.
 *
 * @author dima
 */
@SuppressWarnings( "nls" )
interface ISkResources {

  /**
   * {@link PanelGwidSelector}
   */
  String DLG_T_GWID_SEL         = "Выбор данного (Gwid)";
  String STR_MSG_GWID_SELECTION = "Выберите класс, объект и его данное";
  String STR_MSG_SELECT_OBJ     = "не указан объект";
  String STR_MSG_SELECT_DATA    = "не указано данное";

  /**
   * {@link Ts4GraphTemplateEditorPanel}
   */
  String STR_N_BY_USERS       = "по пользователям";
  String STR_D_BY_USERS       = "Дерево пользователи-шаблоны отчетов";
  String STR_N_GENERATE_CHART = "Сфонирмировать график";
  String STR_D_GENERATE_CHART = "Загрузка данных из БД и формирование графика";

  /**
   * {@link Ts4ReportTemplateEditorPanel}
   */
  String STR_N_GENERATE_REPORT    = "Сформировать отчёт";
  String STR_D_GENERATE_REPORT    = "Загрузка данных из БД и формирование отчёта";
  String STR_N_COPY_TEMPLATE      = "Копия шаблона";
  String STR_D_COPY_TEMPLATE      = "Сделать копию шаблона";
  String ERR_QUERY_FAILED         = "Ошибка выполнения запроса данных: %s";
  String STR_EXEC_QUERY_REPORT    = "Запрос данных для отчета";
  String STR_EXEC_QUERY_FOR_GRAPH = "Запрос данных для графика";
}
