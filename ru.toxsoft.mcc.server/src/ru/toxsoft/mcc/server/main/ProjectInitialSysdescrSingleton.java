package ru.toxsoft.mcc.server.main;

import static ru.uskat.s5.server.IS5ImplementConstants.*;

import java.util.concurrent.TimeUnit;

import javax.ejb.*;

import ru.toxsoft.tslib.datavalue.IAtomicValue;
import ru.toxsoft.tslib.datavalue.impl.DvUtils;
import ru.uskat.core.api.users.ISkUser;
import ru.uskat.s5.server.startup.IS5InitialSysdescrSingleton;
import ru.uskat.s5.server.startup.S5InitialSysdescrSingleton;

import io.netty.util.internal.StringUtil;

/**
 * Реализация синглтона инициализации бекенда
 *
 * @author mvk
 */
@Startup
@Singleton
@DependsOn( { //
    LOCAL_CONNECTIION_SINGLETON, //
// Должны быть перечисленны все поддержки бекенда используемые в проекте:
// BACKEND_SYSDESCR_SINGLETON, // уже включено неявным образом
// BACKEND_OBJECTS_SINGLETON, // уже включено неявным образом
// BACKEND_LINKS_SINGLETON, // уже включено неявным образом
// BACKEND_LOBS_SINGLETON, // уже включено неявным образом
// BACKEND_EVENTS_SINGLETON, // уже включено неявным образом
// BACKEND_COMMANDS_SINGLETON, // уже включено неявным образом
// BACKEND_RTDATA_SINGLETON, // уже включено неявным образом
// S5BackendDataQualitySingleton.BACKEND_DATA_QUALITY_ID, //
// S5BackendGatewaySingleton.BACKEND_GATEWAYS_ID, //
} )
@TransactionManagement( TransactionManagementType.CONTAINER )
@TransactionAttribute( TransactionAttributeType.SUPPORTS )
@ConcurrencyManagement( ConcurrencyManagementType.BEAN )
@AccessTimeout( value = ACCESS_TIMEOUT_DEFAULT, unit = TimeUnit.MILLISECONDS )
@Lock( LockType.READ )
@SuppressWarnings( "unused" )
public class ProjectInitialSysdescrSingleton
    extends S5InitialSysdescrSingleton
    implements IS5InitialSysdescrSingleton {

  private static final long serialVersionUID = 157157L;

  /**
   * Пароль root по умолчанию
   */
  private static final IAtomicValue ROOT_PSWD = DvUtils.avStr( "1" ); //$NON-NLS-1$

  // ------------------------------------------------------------------------------------
  // Реализация шаблонных методов S5InitialSysdescrSingleton
  //
  @Override
  protected void doCreateSysdescr() {
    // Установка пароля root по умолчанию
    ISkUser root = userService().find( ISkUser.ROOT_USER_LOGIN );
    if( root.password().equals( StringUtil.EMPTY_STRING ) == true ) {
      userService().setPassword( ISkUser.ROOT_USER_LOGIN, ROOT_PSWD.asString() );
    }
  }

  // ------------------------------------------------------------------------------------
  // Внутренние методы
  //
}
