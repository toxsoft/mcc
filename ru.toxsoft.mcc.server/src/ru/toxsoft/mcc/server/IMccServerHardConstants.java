package ru.toxsoft.mcc.server;

import static org.toxsoft.uskat.s5.utils.S5ManifestUtils.*;
import static ru.toxsoft.mcc.server.IMccResources.*;

import org.toxsoft.core.tslib.utils.TsVersion;
import org.toxsoft.uskat.s5.server.IS5ServerHardConstants;

/**
 * Жестко заданные константы сервера приложения.
 *
 * @author mvk
 */
@SuppressWarnings( "nls" )
public interface IMccServerHardConstants
    extends IS5ServerHardConstants {

  /**
   * Префикс проекта mcc.
   */
  String PROJECT_PREFIX = "mcc";

  /**
   * Идентификатор сервера.
   */
  String SERVER_ID = PROJECT_PREFIX + ".server";

  /**
   * Имя сервера.
   */
  String SERVER_NAME = STR_N_SERVER_INFO;

  /**
   * Описание сервера
   */
  String SERVER_DESCR = STR_D_SERVER_INFO;

  /**
   * Номер версии
   */
  TsVersion SERVER_VERSION = new TsVersion( 2, 4, getBuildTime() );

  /**
   * Имя схемы базы данных сервера в СУБД (например, mysql)
   */
  String DB_SCHEME_NAME = PROJECT_PREFIX;

  /**
   * Глубина хранения значений исторических данных (сутки). 5 лет
   */
  int DB_STORAGE_DEPTH = 5 * 12 * 30;

  // ------------------------------------------------------------------------------------------------
  // Параметры подключения к серверу
  //
}
