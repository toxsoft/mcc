package ru.toxsoft.mcc.server.main;

import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.uskat.alarms.lib.EAlarmPriority.*;
import static org.toxsoft.uskat.s5.server.IS5ImplementConstants.*;
import static ru.toxsoft.mcc.server.main.IMccResources.*;

import javax.ejb.*;

import org.toxsoft.core.tslib.gw.skid.*;
import org.toxsoft.uskat.alarms.lib.*;
import org.toxsoft.uskat.alarms.s5.generator.*;
import org.toxsoft.uskat.core.*;
import org.toxsoft.uskat.core.api.objserv.*;

/**
 * Абстрактная реализация синглтона генератора алармов службы {@link ISkAlarmService}.
 *
 * @author mvk
 */
@Startup
@Singleton
@DependsOn( PROJECT_INITIAL_SYSDESCR_SINGLETON )
@TransactionManagement( TransactionManagementType.CONTAINER )
@TransactionAttribute( TransactionAttributeType.SUPPORTS )
@ConcurrencyManagement( ConcurrencyManagementType.CONTAINER )
@Lock( LockType.READ )
public class MccAlarmGeneratorSingleton
    extends S5AbstractAlarmGeneratorSingleton {

  private static final long serialVersionUID = 157157L;

  /**
   * Имя синглетона в контейнере сервера для организации зависимостей (@DependsOn)
   */
  public static final String ALARM_GENERATOR_ID = "MccAlarmGeneratorSingleton"; //$NON-NLS-1$

  /**
   * Конструктор.
   */
  public MccAlarmGeneratorSingleton() {
    super( ALARM_GENERATOR_ID, STR_N_MCC_ALARM_GENERATOR );
  }

  // ------------------------------------------------------------------------------------
  // S5AbstractAlarmGeneratorSingleton
  //
  @SuppressWarnings( "nls" )
  @Override
  protected void doAddAlarmDefs( ISkCoreApi aCoreApi ) {
    // Служба объектов
    ISkObjectService objectService = aCoreApi.objService();
    // Добавление алармов для генерации
    ISkidList objIds = null;

    // =============================================================================================================
    // Класс Аналоговый вход
    //
    // @formatter:off

    // aIncludeSubclasses = true
    objIds = objectService.listSkids( "mcc.AnalogInput", true );
    for( int index = 0, n = objIds.size(); index < n; index++ ) {
      Skid objId = objIds.get( index );
      addAlarm( "AnalogInputAlarmMinIndication",    HIGH, STR_N_ANALOG_INPUT_ALARM_MIN_INDICATION,          objId,  "rtdAlarmMinIndication",      value -> equals( value, AV_TRUE  ) );
      addAlarm( "AnalogInputWarningMinIndication",  NORMAL, STR_N_ANALOG_INPUT_WARNING_MIN_INDICATION,      objId,  "rtdWarningMinIndication",      value -> equals( value, AV_TRUE  ) );
      addAlarm( "AnalogInputWarningMaxIndication",  NORMAL, STR_N_ANALOG_INPUT_WARNING_MAX_INDICATION,      objId,  "rtdWarningMaxIndication",      value -> equals( value, AV_TRUE  ) );
      addAlarm( "AnalogInputAlarmMaxIndication",    HIGH, STR_N_ANALOG_INPUT_ALARM_MAX_INDICATION,          objId,  "rtdAlarmMaxIndication",      value -> equals( value, AV_TRUE  ) );
      addAlarm( "AnalogInputCalibrationError",      NORMAL, STR_N_ANALOG_INPUT_CALIBRATION_ERROR,           objId,  "rtdCalibrationError",      value -> equals( value, AV_TRUE  ) );


      // dima 13.01.23 этого нет в данном классе, оставлено как пример работы с битовыми полями
//      String dataId = "rtdParamState";
//      addAlarm( "openOnFailure",      NORMAL, "Внимание не включился на открытие",        objId,   dataId,        value -> hasBit( value, 0 )  );
    }

    // @formatter:on

    // =============================================================================================================
    // Класс Нереверсивный привод
    //
    // @formatter:off

    objIds = objectService.listSkids( "mcc.IrreversibleEngine", true );
    for( int index = 0, n = objIds.size(); index < n; index++ ) {
      Skid objId = objIds.get( index );
      addAlarm( "IrreversibleEngineSwitchOnFailure",   HIGH, STR_N_IRREVERSIBLE_ENGINE_SWITCH_ON_FAILURE,      objId,  "rtdSwitchOnFailure",     value -> equals( value, AV_TRUE  ) );
      addAlarm( "IrreversibleEngineSwitchOffFailure",  HIGH, STR_N_IRREVERSIBLE_ENGINE_SWITCH_OFF_FAILURE,       objId,  "rtdSwitchOffFailure",      value -> equals( value, AV_TRUE  ) );
      addAlarm( "IrreversibleEnginePwr",  HIGH, "Нет питания",       objId, STR_N_IRREVERSIBLE_ENGINE_PWR,      value -> equals( value, AV_FALSE  ) );

    }
    // @formatter:on

    // =============================================================================================================
    // Класс Высоковольтный выключатель
    //
    // @formatter:off

    objIds = objectService.listSkids( "mcc.MainSwitch", true );
    for( int index = 0, n = objIds.size(); index < n; index++ ) {
      Skid objId = objIds.get( index );
      addAlarm( "MainSwitchEmergencyStop",   NORMAL, STR_N_MAIN_SWITCH_EMERGENCY_STOP,      objId,  "rtdEmergencyStop",     value -> equals( value, AV_TRUE  ) );
      addAlarm( "MainSwitchAlarm",           HIGH, STR_N_MAIN_SWITCH_ALARM,      objId,  "rtdMainSwitchAlarm",     value -> equals( value, AV_TRUE  ) );
      addAlarm( "MainSwitchSwitchOnFailure",   HIGH, STR_N_MAIN_SWITCH_SWITCH_ON_FAILURE,      objId,  "rtdSwitchOnFailure",     value -> equals( value, AV_TRUE  ) );
      addAlarm( "MainSwitchSwitchOffFailure",  HIGH, STR_N_MAIN_SWITCH_SWITCH_OFF_FAILURE,      objId,  "rtdSwitchOffFailure",     value -> equals( value, AV_TRUE  ) );
      addAlarm( "MainSwitchPowerControl",  HIGH, STR_N_MAIN_SWITCH_POWER_CONTROL,      objId,  "rtdPowerControl",     value -> equals( value, AV_FALSE  ) );

    }

    // @formatter:on

    // =============================================================================================================
    // Класс Реверсивный привод
    //
    // @formatter:off

    objIds = objectService.listSkids( "mcc.ReversibleEngine", true );
    for( int index = 0, n = objIds.size(); index < n; index++ ) {
      Skid objId = objIds.get( index );
      addAlarm( "ReversibleEngineOpenFailure",   HIGH, STR_N_REVERSIBLE_ENGINE_OPEN_FAILURE,      objId,  "rtdOpenFailure",     value -> equals( value, AV_TRUE  ) );
      addAlarm( "ReversibleEngineCloseFailure",  HIGH, STR_N_REVERSIBLE_ENGINE_CLOSE_FAILURE,       objId,  "rtdCloseFailure",      value -> equals( value, AV_TRUE  ) );
      addAlarm( "ReversibleEngineOpenOnFailure",   HIGH, STR_N_REVERSIBLE_ENGINE_OPEN_ON_FAILURE,      objId,  "rtdOpenOnFailure",     value -> equals( value, AV_TRUE  ) );
      addAlarm( "ReversibleEngineOpenOffFailure",  HIGH, STR_N_REVERSIBLE_ENGINE_OPEN_OFF_FAILURE,       objId,  "rtdOpenOffFailure",      value -> equals( value, AV_TRUE  ) );
      addAlarm( "ReversibleEngineCloseOnFailure",   HIGH, STR_N_REVERSIBLE_ENGINE_CLOSE_ON_FAILURE,      objId,  "rtdCloseOnFailure",     value -> equals( value, AV_TRUE  ) );
      addAlarm( "ReversibleEngineCloseOffFailure",  HIGH, STR_N_REVERSIBLE_ENGINE_CLOSE_OFF_FAILURE,       objId,  "rtdCloseOffFailure",      value -> equals( value, AV_TRUE  ) );
      addAlarm( "ReversibleEnginePowerControl",  HIGH, STR_N_REVERSIBLE_ENGINE_POWER_CONTROL,      objId,  "rtdPowerControl",     value -> equals( value, AV_FALSE  ) );
      addAlarm( "ReversibleEnginePwr",  HIGH, STR_N_REVERSIBLE_ENGINE_PWR,       objId,  "rtdPwr",      value -> equals( value, AV_FALSE  ) );
    }

    // @formatter:on

    // =============================================================================================================
    // Класс Система управления
    //
    // @formatter:off

    objIds = objectService.listSkids( "mcc.CtrlSystem", true );
    for( int index = 0, n = objIds.size(); index < n; index++ ) {
      Skid objId = objIds.get( index );
      addAlarm( "CtrlSystemOilFilterAlarm",  NORMAL, STR_N_CTRL_SYSTEM_OIL_FILTER_ALARM,      objId,  "rtdOilFilterAlarm",     value -> equals( value, AV_TRUE  ) );
      addAlarm( "CtrlSystemEmergencyStop",   NORMAL, STR_N_CTRL_SYSTEM_EMERGENCY_STOP,      objId,  "rtdEmergencyStop",     value -> equals( value, AV_TRUE  ) );
      addAlarm( "CtrlSystemLoOil",   NORMAL, STR_N_CTRL_SYSTEM_LO_OIL,      objId,  "rtdLoOil",     value -> equals( value, AV_TRUE  ) );
    }
  }
}
