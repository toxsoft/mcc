package ru.toxsoft.mcc.server;

import static ru.toxsoft.mcc.server.IMccResources.*;
import static ru.toxsoft.s5.utils.S5ManifestUtils.*;

import ru.toxsoft.s5.common.info.IServerInfo;
import ru.toxsoft.tslib.utils.version.DefaultTsVersion;
import ru.toxsoft.tslib.utils.version.ITsVersion;

/**
 * Жестко заданные константы сервера приложения MCC.
 *
 * @author goga
 */
@SuppressWarnings( { "nls" } )
public interface IMccServerHardConstants {

  /**
   * Идентификатор сервера {@link IServerInfo#id()}.
   */
  String MCC_SERVER_ID = "ru.toxsoft.mcc.server";

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
  ITsVersion MCC_SERVER_VERSION = new DefaultTsVersion( 2, 2, getBuildTime() );

  /**
   * Псевдоним (алиас) имени приложения, используемый для формирования идентификаторов.
   */
  String APP_ALIAS = "mcc";

}
