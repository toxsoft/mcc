package ru.toxsoft.mcc.server;

import static ru.toxsoft.mcc.server.IMccResources.*;
import static ru.uskat.s5.utils.S5ManifestUtils.*;

import ru.toxsoft.tslib.utils.version.DefaultTsVersion;
import ru.toxsoft.tslib.utils.version.ITsVersion;
import ru.uskat.s5.server.IS5ServerHardConstants;

/**
 * Жестко заданные константы сервера приложения.
 *
 * @author mvk
 */
@SuppressWarnings( "nls" )
public interface IMccServerHardConstants
    extends IS5ServerHardConstants {

  /**
   * Идентификатор сервера
   */
  String MCC_SERVER_ID = "mcc.server";

  /**
   * Имя сервера
   */
  String MCC_SERVER_NAME = STR_N_MCC_SERVER_INFO;

  /**
   * Описание сервера
   */
  String MCC_SERVER_DESCR = STR_D_MCC_SERVER_INFO;

  /**
   * Номер версии
   */
  ITsVersion MCC_SERVER_VERSION = new DefaultTsVersion( 2, 1, getBuildTime() );

  // ------------------------------------------------------------------------------------------------
  // Параметры подключения к серверу
  //
}
