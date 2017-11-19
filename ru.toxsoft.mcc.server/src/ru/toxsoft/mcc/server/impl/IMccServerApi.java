package ru.toxsoft.mcc.server.impl;

import static ru.toxsoft.mcc.server.IMccServerHardConstants.*;

import ru.toxsoft.s5.common.api.IServerApi;
import ru.toxsoft.s5.sysaddons.common.services.alarms.IAlarmService;
import ru.toxsoft.s5.sysaddons.common.services.reports.IReportService;
import ru.toxsoft.s5.sysaddons.common.services.wscfg.IWorkstationConfigService;

/**
 * Точка входа в сервер.
 *
 * @author goga
 */
@SuppressWarnings( "nls" )
public interface IMccServerApi
    extends IServerApi {

  /**
   * Имя модуля приложения развернутого на сервере без суффикса ".jar".
   */
  String APP_MODULE_NAME = APP_ALIAS + "-server-deploy";

  /**
   * Имя класса интерфейса (тип удаленной ссылки) API пользователя на сервере.
   */
  String API_INTERFACE_NAME = IMccServerApiRemote.class.getName();

  /**
   * Имя класса бина реализующий API сессии пользователя на сервере
   */
  String API_BEAN_NAME = MccServerApiSessionImpl.class.getSimpleName();

  /**
   * Домен безопасности в котором работает сервер pr2.server.
   */
  String SECURITY_DOMAIN = APP_ALIAS + "_security_domain";

  /**
   * Возвращает интерфейс службы работы с конфигурацией АРМов.
   *
   * @return {@link IWorkstationConfigService} - интерфейс службы конфигурации АРМов
   */
  IWorkstationConfigService workstationConfigService();

  /**
   * Возвращает интерфейс службы отчетов.
   *
   * @return {@link IAlarmService} - интерфейс службы алармов
   */
  IAlarmService alarmService();

  /**
   * Возвращает интерфейс службы отчетов.
   *
   * @return {@link IReportService} - интерфейс службы отчетов
   */
  IReportService reportService();

}
