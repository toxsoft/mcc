package ru.toxsoft.mcc.server.impl;

import static ru.toxsoft.s5.server.IS5ServerHardConstants.*;

import java.util.concurrent.TimeUnit;

import javax.ejb.*;

import ru.toxsoft.s5.server.IS5ServerApiLocal;
import ru.toxsoft.s5.server.IS5ServerApiRemote;
import ru.toxsoft.s5.server.impl.session.S5AbstractServerApiSessionImpl;
import ru.toxsoft.s5.server.impl.singletons.IS5SingletonApi;
import ru.toxsoft.s5.sysaddons.common.services.alarms.IAlarmService;
import ru.toxsoft.s5.sysaddons.common.services.reports.IReportService;
import ru.toxsoft.s5.sysaddons.common.services.wscfg.IWorkstationConfigService;
import ru.toxsoft.s5.sysaddons.server.services.alarms.IS5AlarmServiceLocal;
import ru.toxsoft.s5.sysaddons.server.services.reports.IS5ReportServiceLocal;
import ru.toxsoft.s5.sysaddons.server.services.wscfg.IS5WorkstationConfigServiceLocal;

/**
 * Серверная сессионая реализация API сервера {@link IMccServerApi}.
 *
 * @author mvk
 */
@Stateful
@StatefulTimeout( value = STATEFULL_SERVICE_TIMEOUT_DEFAULT, unit = TimeUnit.MILLISECONDS )
@AccessTimeout( value = ACCESS_TIMEOUT_DEFAULT, unit = TimeUnit.MILLISECONDS )
@TransactionAttribute( TransactionAttributeType.SUPPORTS )
// Зависимости гарантируют что при close следующие синглтоны будут еще доступны.
// @DependsOn эффективно только в конечном бине
@DependsOn( { //
    "S5EventServiceSessionImpl", //
    "S5CommandServiceSessionImpl", //
    "S5HistDataServiceSessionImpl", //
    "S5CurrDataServiceSessionImpl", //
    "S5PrefsServiceSessionImpl", //
    "S5RefbookServiceSessionImpl", //
    "S5UserServiceSessionImpl", //
    "S5LinkServiceSessionImpl", //
    "S5ObjectServiceSessionImpl", //
    "S5ClassServiceSessionImpl", //
    "S5ServerMessageServiceSessionImpl", //
    "S5WorkstationConfigServiceSessionImpl", //
    "S5AlarmServiceSessionImpl", //
    "S5ReportServiceSessionImpl", //
    // Все сессии должны закрыться ДО завершения API
    "MccSingletonApi", //
} )
public class MccServerApiSessionImpl
    extends S5AbstractServerApiSessionImpl
    implements IMccServerApiLocal, IMccServerApiRemote {

  private static final long serialVersionUID = 157157L;

  @EJB
  private IMccSingletonApi singleton;

  @EJB
  private IS5WorkstationConfigServiceLocal workstationConfigServiceLocal;

  @EJB
  private IS5AlarmServiceLocal alarmServiceLocal;

  @EJB
  private IS5ReportServiceLocal reportServiceLocal;

  /**
   * Конструктор
   */
  public MccServerApiSessionImpl() {
    super();
  }

  // ------------------------------------------------------------------------------------
  // IMccServerApi
  //

  @Override
  public IWorkstationConfigService workstationConfigService() {
    return workstationConfigServiceLocal;
  }

  @Override
  public IAlarmService alarmService() {
    return alarmServiceLocal;
  }

  @Override
  public IReportService reportService() {
    return reportServiceLocal;
  }

  // ------------------------------------------------------------------------------------
  // Переопределение методов базового класса
  //

  // @PostConstruct
  // @TransactionAttribute( TransactionAttributeType.REQUIRES_NEW )
  // @Override
  // public void init() {
  // super.init();
  // }

  @Remove
  @TransactionAttribute( TransactionAttributeType.REQUIRED )
  @Override
  public void close() {
    super.close();
  }

  // ------------------------------------------------------------------------------------
  // Реализация шаблонных методов AbstractS5ServerApiSessionImpl
  //

  @Override
  protected IS5SingletonApi getSingletonApi() {
    return singleton;
  }

  @Override
  protected Class<? extends IS5ServerApiLocal> getLocalView() {
    return IMccServerApiLocal.class;
  }

  @Override
  protected Class<? extends IS5ServerApiRemote> getRemoteView() {
    return IMccServerApiRemote.class;
  }

  @Override
  protected void doRegisterAppSpecServices() {
    // регистрация служб из s5 addons
    registerService( workstationConfigServiceLocal );
    registerService( alarmServiceLocal );
    registerService( reportServiceLocal );
    // Регистрация приложение-специфичных служб
  }

}
