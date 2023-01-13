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
      addAlarm( "AnalogInputAlarm",   HIGH,   "Авария",                                   objId,  "rtdAlarm",     value -> equals( value, AV_TRUE  ) );
      addAlarm( "AnalogInputWarn",    NORMAL, "Предупреждение",                           objId,  "rtdWarn",      value -> equals( value, AV_TRUE  ) );
      // dima 13.01.23 новые алармы
      addAlarm( "AnalogInputAlarmMinGeneration",    HIGH, "Авария",                       objId,  "rtdAlarmMinGeneration",      value -> equals( value, AV_TRUE  ) );
      addAlarm( "AnalogInputAlarmMinIndication",    HIGH, "Авария",                       objId,  "rtdAlarmMinIndication",      value -> equals( value, AV_TRUE  ) );
      addAlarm( "AnalogInputWarningMinGeneration",    NORMAL, "Предупреждение",           objId,  "rtdWarningMinGeneration",      value -> equals( value, AV_TRUE  ) );
      addAlarm( "AnalogInputWarningMinIndication",    NORMAL, "Предупреждение",           objId,  "rtdWarningMinIndication",      value -> equals( value, AV_TRUE  ) );
      addAlarm( "AnalogInputWarningMaxGeneration",    NORMAL, "Предупреждение",           objId,  "rtdWarningMaxGeneration",      value -> equals( value, AV_TRUE  ) );
      addAlarm( "AnalogInputWarningMaxIndication",    NORMAL, "Предупреждение",           objId,  "rtdWarningMaxIndication",      value -> equals( value, AV_TRUE  ) );
      addAlarm( "AnalogInputAlarmMaxGeneration",    HIGH, "Авария",                       objId,  "rtdAlarmMaxGeneration",      value -> equals( value, AV_TRUE  ) );
      addAlarm( "AnalogInputAlarmMaxIndication",    HIGH, "Авария",                       objId,  "rtdAlarmMaxIndication",      value -> equals( value, AV_TRUE  ) );

      addAlarm( "AnalogInputCalibrationWarning",    NORMAL, "Предупреждение",           objId,  "rtCalibrationWarning",      value -> equals( value, AV_TRUE  ) );
      addAlarm( "AnalogInputCalibrationError",    HIGH, "Авария",                       objId,  "rtdCalibrationError",      value -> equals( value, AV_TRUE  ) );


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
      addAlarm( "IrreversibleEngineSwitchOnFailure",   HIGH, "Не включился",      objId,  "rtdSwitchOnFailure",     value -> equals( value, AV_TRUE  ) );
      addAlarm( "IrreversibleEngineSwitchOffFailure",  HIGH, "Не отключился",       objId,  "rtdSwitchOffFailure",      value -> equals( value, AV_TRUE  ) );

    }

    // @formatter:on

    // =============================================================================================================
    // Класс Высоковольтный выключатель
    //
    // @formatter:off

    objIds = objectService.listSkids( "mcc.MainSwitch", true );
    for( int index = 0, n = objIds.size(); index < n; index++ ) {
      Skid objId = objIds.get( index );
      addAlarm( "MainSwitchEmergencyStop",   NORMAL, "Кнопка STOP",      objId,  "rtdEmergencyStop",     value -> equals( value, AV_TRUE  ) );
      addAlarm( "MainSwitchAlarm",           HIGH, "Авария из ячейки",      objId,  "rtdMainSwitchAlarm",     value -> equals( value, AV_TRUE  ) );
      addAlarm( "MainSwitchSwitchOnFailure",   HIGH, "Не включился",      objId,  "rtdSwitchOnFailure",     value -> equals( value, AV_TRUE  ) );
      addAlarm( "MainSwitchSwitchOffFailure",  HIGH, "Не отключился",      objId,  "rtdSwitchOffFailure",     value -> equals( value, AV_TRUE  ) );

    }
    // @formatter:on

    // =============================================================================================================
    // Класс Реверсивный привод
    //
    // @formatter:off

    objIds = objectService.listSkids( "mcc.ReversibleEngine", true );
    for( int index = 0, n = objIds.size(); index < n; index++ ) {
      Skid objId = objIds.get( index );
      addAlarm( "ReversibleEngineOpenFailure",   HIGH, "Не открылось",      objId,  "rtdOpenFailure",     value -> equals( value, AV_TRUE  ) );
      addAlarm( "ReversibleEngineCloseFailure",  HIGH, "Не закрылось",       objId,  "rtdCloseFailure",      value -> equals( value, AV_TRUE  ) );

      addAlarm( "ReversibleEngineOpenOnFailure",   HIGH, "Не вкл. на открытие",      objId,  "rtdOpenOnFailure",     value -> equals( value, AV_TRUE  ) );
      addAlarm( "ReversibleEngineOpenOffFailure",  HIGH, "Не откл. на открытие",       objId,  "rtdOpenOffFailure",      value -> equals( value, AV_TRUE  ) );
      addAlarm( "ReversibleEngineCloseOnFailure",   HIGH, "Не вкл. на закрытие",      objId,  "rtdCloseOnFailure",     value -> equals( value, AV_TRUE  ) );
      addAlarm( "ReversibleEngineCloseOffFailure",  HIGH, "Не откл. на закрытие",       objId,  "rtdCloseOffFailure",      value -> equals( value, AV_TRUE  ) );
    }

    // @formatter:on

    // =============================================================================================================
    // Класс Система управления
    //
    // @formatter:off

    objIds = objectService.listSkids( "mcc.CtrlSystem", true );
    for( int index = 0, n = objIds.size(); index < n; index++ ) {
      Skid objId = objIds.get( index );
      addAlarm( "CtrlSystemOilFilterAlarm",  NORMAL, "Признак грязного маслофильтра",      objId,  "rtdOilFilterAlarm",     value -> equals( value, AV_TRUE  ) );
      addAlarm( "CtrlSystemEmergencyStop",   HIGH, "Останов Аварийный",      objId,  "rtdEmergencyStop",     value -> equals( value, AV_TRUE  ) );
      addAlarm( "CtrlSystemIrrEngineAlarm",   HIGH, "Есть авария двигателя",      objId,  "rtdIrrEngineAlarm",     value -> equals( value, AV_TRUE  ) );
      addAlarm( "CtrlSystemRevEngineAlarm",   HIGH, "Есть авария задвижки",      objId,  "rtdRevEngineAlarm",     value -> equals( value, AV_TRUE  ) );
      addAlarm( "CtrlSystemIrrEngineBlock",   HIGH, "Есть блокировка двигателя",      objId,  "rtdIrrEngineBlock",     value -> equals( value, AV_TRUE  ) );
      addAlarm( "CtrlSystemRevEngineBlock",   HIGH, "Есть блокировка задвижки",      objId,  "rtdRevEngineBlock",     value -> equals( value, AV_TRUE  ) );
      addAlarm( "CtrlSystemWaterAlarm",   HIGH, "Авария по давлению воды",      objId,  "rtdWaterAlarm",     value -> equals( value, AV_TRUE  ) );
      addAlarm( "CtrlSystemLoOil",   HIGH, "Низкий уровень масла",      objId,  "rtdLoOil",     value -> equals( value, AV_TRUE  ) );
    }

    // @formatter:on

    // =============================================================================================================
    // Класс Аналоговый привод
    //
    // @formatter:off

    objIds = objectService.listSkids( "mcc.AnalogEngine", true );
    for( int index = 0, n = objIds.size(); index < n; index++ ) {
      Skid objId = objIds.get( index );
      addAlarm( "AnalogEngineAlarm",   HIGH, "Авария",      objId,  "rtdAlarm",     value -> equals( value, AV_TRUE  ) );
    }

  }
}
