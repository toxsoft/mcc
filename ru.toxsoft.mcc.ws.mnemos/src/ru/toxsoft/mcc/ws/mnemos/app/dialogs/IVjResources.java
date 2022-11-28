package ru.toxsoft.mcc.ws.mnemos.app.dialogs;

/**
 * Локализуемые ресурсы.
 *
 * @author goga
 */
@SuppressWarnings( "nls" )
interface IVjResources {

  String DLG_SETTINGS_MSG = "Установите требуемые параметры";

  /**
   * {@link PanelAnalogInputSettings}
   */
  String STR_DLG_T_AI_SETTINGS = "Изменить значение";
  String STR_DLG_M_AI_SETTINGS = "Введите значение:";

  /**
   * {@link PanelAnalogInput}
   */
  String STR_OUTPUT_VALUE   = "Выходное значение";
  String STR_ALARM          = "Авария";
  String STR_WARNING        = "Предупреждение";
  String STR_BLOCKING_IS_ON = "Блокировка включена";
  String STR_LIMIT_VALUES   = "Предельные значения";
  String STR_INDICATION     = "Индикация";
  String STR_GENERATION     = "Генерация";

  String STR_HI_ALARM_LIMIT  = "Верхний аварийный предел: ";
  String STR_HI_WARN_LIMIT   = "Верхний предупредительный предел: ";
  String STR_LOW_WARN_LIMIT  = "Нижний предупредительный предел: ";
  String STR_LOW_ALARM_LIMIT = "Нижний аварийный предел: ";

  String STR_CONFIRMATION        = "Квитировать";
  String STR_SETTINGS            = "Настройки";
  String STR_IMITATION           = "Имитация";
  String STR_SCALING             = "Масштабирование";
  String STR_UNIT                = "ед.изм.";
  String STR_INVOKE_SETTINGS_DLG = "Вызвать диалог настроек аналогового синала";

}
