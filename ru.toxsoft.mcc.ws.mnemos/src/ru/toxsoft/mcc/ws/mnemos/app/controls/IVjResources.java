package ru.toxsoft.mcc.ws.mnemos.app.controls;

/**
 * Локализуемые ресурсы.
 *
 * @author vs
 */
@SuppressWarnings( "nls" )
interface IVjResources {

  String STR_ERR_VALUE_NOT_SET = "Значение не установлено"; // Значение не установлено

  /**
   * {@link MccRtTextEditor}
   */
  String STR_DLG_T_EDIT_VALUE = "Изменить значение";
  String STR_DLG_M_VALUE      = "Введите значение:";

  /**
   * {@link AbstractMccDialogPanel}
   */
  String STR_OPERATING_TIME       = "Наработка";
  String STR_CLEAR                = "Сбросить...";
  String STR_CLEAR_OPERATING_TIME = "Сбросить время наработки?";

  /**
   * MccAnalogInputControl
   */
  String STR_INVALID_STATE = "недопустимое состояние";
  String STR_NO_DATA       = "данное не установлено";

  String STR_STATE_NORMAL           = "норма";
  String STR_STATE_IMITATION        = "имитация";
  String STR_STATE_NO_CONTROL_POWER = "нет U управления";
  String STR_STATE_BLOCKED          = "заблокирован";
  String STR_STATE_LESS_FIVE        = "отклонение < в пределах 5%";
  String STR_STATE_GREATER_FIVE     = "отклонение > в пределах 5%";
  String STR_STATE_WARNING          = "предупреждение";
  String STR_STATE_ALARM            = "авария";

}
