package ru.toxsoft.mcc.server.main;

import static org.toxsoft.uskat.s5.server.IS5ImplementConstants.*;

import java.util.concurrent.*;

import javax.ejb.*;

import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.uskat.core.*;
import org.toxsoft.uskat.s5.server.startup.*;

/**
 * Реализация синглтона инициализации бекенда.
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
// BACKEND_CLOBS_SINGLETON, // уже включено неявным образом
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
  private static final IAtomicValue ROOT_PSWD = AvUtils.avStr( "1" ); //$NON-NLS-1$

  // ------------------------------------------------------------------------------------
  // Реализация шаблонных методов S5InitialSysdescrSingleton
  //
  @Override
  protected void doCreateSysdescr( ISkCoreApi aCoreApi ) {
    // Установка пароля root по умолчанию
    // ISkUser root = userService().listUsers().findByKey( ISkUserServiceHardConstants.USER_ID_ROOT );
    // if( root.password().equals( TsLibUtils.EMPTY_STRING ) ) {
    // userService().setUserPassword( ISkUserServiceHardConstants.USER_ID_ROOT, ROOT_PSWD.asString() );
    // }
  }

  // ------------------------------------------------------------------------------------
  // Внутренние методы
  //
}
