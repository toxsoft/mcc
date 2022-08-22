package org.toxsoft.mcc.server;

import static org.toxsoft.mcc.server.IMccResources.*;
import static org.toxsoft.uskat.s5.utils.S5ManifestUtils.*;

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
  TsVersion MCC_SERVER_VERSION = new TsVersion( 2, 1, getBuildTime() );

  // ------------------------------------------------------------------------------------------------
  // Параметры подключения к серверу
  //
}
