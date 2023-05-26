package ru.toxsoft.mcc.server.main;

/**
 * Локализуемые ресурсы.
 *
 * @author mvk
 */
@SuppressWarnings( "nls" )
interface IMccResources {

  String STR_N_MCC_ALARM_GENERATOR                 = "mcc alarm generator";
  String STR_N_ANALOG_INPUT_ALARM_MIN_INDICATION   = "Нижний аварийный уровень";
  String STR_N_ANALOG_INPUT_WARNING_MIN_INDICATION = "Нижний предупредительный уровень";
  String STR_N_ANALOG_INPUT_WARNING_MAX_INDICATION = "Верхний предупредительный уровень";
  String STR_N_ANALOG_INPUT_ALARM_MAX_INDICATION   = "Верхний аварийный уровень";
  String STR_N_ANALOG_INPUT_CALIBRATION_ERROR      = "Неисправность датчика";

  String STR_N_IRREVERSIBLE_ENGINE_SWITCH_ON_FAILURE  = "Не включился";
  String STR_N_IRREVERSIBLE_ENGINE_SWITCH_OFF_FAILURE = "Не отключился";
  String STR_N_IRREVERSIBLE_ENGINE_PWR_FAILURE        = "Нет питания";

  String STR_N_MAIN_SWITCH_ALARM                 = "Авария из ячейки";
  String STR_N_MAIN_SWITCH_SWITCH_ON_FAILURE     = "Не включился";
  String STR_N_MAIN_SWITCH_SWITCH_OFF_FAILURE    = "Не отключился";
  String STR_N_MAIN_SWITCH_POWER_CONTROL_FAILURE = "Hет напряжения управления";

  String STR_N_REVERSIBLE_ENGINE_OPEN_FAILURE          = "Не открылось";
  String STR_N_REVERSIBLE_ENGINE_CLOSE_FAILURE         = "Не закрылось";
  String STR_N_REVERSIBLE_ENGINE_OPEN_ON_FAILURE       = "Не вкл. на открытие";
  String STR_N_REVERSIBLE_ENGINE_OPEN_OFF_FAILURE      = "Не откл. на открытие";
  String STR_N_REVERSIBLE_ENGINE_CLOSE_ON_FAILURE      = "Не вкл. на закрытие";
  String STR_N_REVERSIBLE_ENGINE_CLOSE_OFF_FAILURE     = "Не откл. на закрытие";
  String STR_N_REVERSIBLE_ENGINE_POWER_CONTROL_FAILURE = "Нет напряжения управления";
  String STR_N_REVERSIBLE_ENGINE_PWR_FAILURE           = "Нет питания";

  String STR_N_CTRL_SYSTEM_OIL_FILTER_ALARM = "Признак грязного маслофильтра";
  String STR_N_CTRL_SYSTEM_EMERGENCY_STOP   = "Режим Аварийного останова";
  String STR_N_CTRL_SYSTEM_LO_OIL           = "Низкий уровень масла";

  String STR_N_ACKNOWLEDGMENT_CMD = "Квитирование";
  String STR_D_ACKNOWLEDGMENT_CMD = "Команда используемая для квитирования аларма";
}
