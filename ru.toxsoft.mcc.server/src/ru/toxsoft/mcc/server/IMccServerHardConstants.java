package ru.toxsoft.mcc.server;

import ru.toxsoft.s5.common.info.IServerInfo;

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
   * Псевдоним (алиас) имени приложения, используемый для формирования идентификаторов.
   */
  String APP_ALIAS = "mcc";

}
