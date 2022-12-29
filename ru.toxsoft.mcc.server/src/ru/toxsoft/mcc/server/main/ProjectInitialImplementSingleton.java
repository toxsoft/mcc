package ru.toxsoft.mcc.server.main;

import static org.toxsoft.uskat.s5.server.IS5ImplementConstants.*;

import java.util.concurrent.TimeUnit;

import javax.ejb.*;

import org.toxsoft.uskat.s5.server.startup.IS5InitialImplementSingleton;
import org.toxsoft.uskat.s5.server.startup.S5InitialImplementSingleton;

import ru.toxsoft.mcc.server.MccMainServer;

/**
 * Реализация синглтона инициализации бекенда.
 *
 * @author mvk
 */
@Startup
@Singleton
// @LocalBean
@DependsOn( { //
    INITIAL_SINGLETON//
} )
@TransactionManagement( TransactionManagementType.CONTAINER )
@TransactionAttribute( TransactionAttributeType.SUPPORTS )
@ConcurrencyManagement( ConcurrencyManagementType.BEAN )
@AccessTimeout( value = ACCESS_TIMEOUT_DEFAULT, unit = TimeUnit.MILLISECONDS )
@Lock( LockType.READ )
@SuppressWarnings( "unused" )
public class ProjectInitialImplementSingleton
    extends S5InitialImplementSingleton
    implements IS5InitialImplementSingleton {

  private static final long serialVersionUID = 157157L;

  /**
   * Конструктор
   */
  public ProjectInitialImplementSingleton() {
    super( new MccMainServer() );
  }
}
