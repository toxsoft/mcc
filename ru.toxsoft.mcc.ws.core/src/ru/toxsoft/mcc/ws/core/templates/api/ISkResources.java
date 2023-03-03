package ru.toxsoft.mcc.ws.core.templates.api;

/**
 * Localizable resources.
 *
 * @author dima
 */
@SuppressWarnings( "nls" )
interface ISkResources {

  /**
   * {@link EAggregationFunc}
   */
  String STR_N_AVERAGE = "среднее";
  String STR_D_AVERAGE = "вычисление среднего значения";
  String STR_N_MIN     = "min";
  String STR_D_MIN     = "вычисление минимального значения";
  String STR_N_MAX     = "max";
  String STR_D_MAX     = "вычисление максимального значения";
  String STR_N_SUM     = "сумма";
  String STR_D_SUM     = "вычисление суммы значений";
  String STR_N_COUNT   = "кол-во";
  String STR_D_COUNT   = "вычисление количества значений";

  /**
   * {@link ISkTemplateEditorServiceHardConstants}
   */
  String STR_N_AGGR_FUNC           = "функция агрегации";
  String STR_D_AGGR_FUNC           = "функция агрегации";
  String STR_N_DISPLAY_FORMAT      = "формат отображения";
  String STR_D_DISPLAY_FORMAT      = "формат отображения значения параметра";
  String STR_N_TITLE               = "заголовок";
  String STR_D_TITLE               = "заголовок отчета/графика";
  String STR_N_TEMPLATE_PARAMS     = "параметры шаблона";
  String STR_D_TEMPLATE_PARAMS     = "параметры шаблона отчета/графика";
  String STR_N_HAS_SUMMARY         = "область 'Итого'";
  String STR_D_HAS_SUMMARY         = "в отчет добавить область 'Итого'";
  String STR_N_AGGR_STEP           = "шаг агрегации";
  String STR_D_AGGR_STEP           = "шаг агрегации";
  String STR_N_MAX_EXECUTION_TIME  = "время выполнения";
  String STR_D_MAX_EXECUTION_TIME  = "максимальное время выполнения запроса (мсек)";
  String STR_N_CLB_TEMPLATE_PARAMS = "список параметров шаблона";
  String STR_D_CLB_TEMPLATE_PARAMS = "список параметров шаблона";
  String STR_N_TEMPLATE_AUTHOR     = "автор шаблона";
  String STR_D_TEMPLATE_AUTHOR     = "Пользователь системы - автор шаблона";

}
