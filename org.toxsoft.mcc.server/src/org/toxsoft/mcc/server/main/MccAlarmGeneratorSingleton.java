package org.toxsoft.mcc.server.main;

import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.mcc.server.main.IMccResources.*;
import static org.toxsoft.uskat.alarms.lib.EAlarmPriority.*;
import static org.toxsoft.uskat.s5.server.IS5ImplementConstants.*;

import javax.ejb.*;

import org.toxsoft.core.tslib.gw.skid.ISkidList;
import org.toxsoft.core.tslib.gw.skid.Skid;
import org.toxsoft.uskat.alarms.lib.ISkAlarmService;
import org.toxsoft.uskat.alarms.s5.generator.S5AbstractAlarmGeneratorSingleton;
import org.toxsoft.uskat.core.ISkCoreApi;
import org.toxsoft.uskat.core.api.objserv.ISkObjectService;

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

      String dataId = "rtdParamState";
      addAlarm( "openOnFailure",      NORMAL, "Внимание не включился на открытие",        objId,   dataId,        value -> hasBit( value, 0 )  );
      addAlarm( "openOffFailure",     NORMAL, "Внимание не отключился на открытие",       objId,   dataId,        value -> hasBit( value, 1 )  );
      addAlarm( "closeOnFailure",     NORMAL, "Внимание не включился на закрытие",        objId,   dataId,        value -> hasBit( value, 2 )  );
      addAlarm( "closeOffFailure",    NORMAL, "Внимание не отключился на закрытие",       objId,   dataId,        value -> hasBit( value, 3 )  );
      addAlarm( "useless",            NORMAL, "Внимание не готов",                        objId,   dataId,        value -> hasBit( value, 5 )  );
      addAlarm( "driveFailure",       NORMAL, "Внимание авария привода",                  objId,   dataId,        value -> hasBit( value, 6 )  );
      addAlarm( "noPowerSupply",      NORMAL, "Внимание отсутствует напряжение питания",  objId,   dataId,        value -> hasBit( value, 7 )  );
    }

    // @formatter:on
  }
}
