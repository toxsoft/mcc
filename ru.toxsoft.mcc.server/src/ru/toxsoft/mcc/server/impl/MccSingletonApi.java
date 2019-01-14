package ru.toxsoft.mcc.server.impl;

import static ru.toxsoft.mcc.server.IMccServerHardConstants.*;
import static ru.toxsoft.mcc.server.impl.IMccResources.*;

import javax.annotation.*;
import javax.ejb.*;

import ru.toxsoft.s5.client.connection.options.impl.S5ModuleOptionValue;
import ru.toxsoft.s5.common.annotations.S5AnnotationParser;
import ru.toxsoft.s5.common.bsobj.IBsObject;
import ru.toxsoft.s5.common.sysdescr.IClassInfo;
import ru.toxsoft.s5.server.impl.singletons.IS5SingletonApi;
import ru.toxsoft.s5.server.impl.singletons.S5AbstractSingletonApi;
import ru.toxsoft.s5.server.services.objs.S5ObjectEntity;
import ru.toxsoft.s5.server.services.sysdescr.IS5ClassServiceSingleton;
import ru.toxsoft.s5.sysaddons.server.services.alarms.impl.S5AlarmServiceSingleton;
import ru.toxsoft.s5.sysaddons.server.services.reports.impl.S5ReportServiceSingleton;
import ru.toxsoft.s5.sysaddons.server.services.wscfg.impl.S5WorkstationConfigServiceSingleton;
import ru.toxsoft.tslib.error.TsNullArgumentRtException;

/**
 * Реализация синглтона {@link IS5SingletonApi}.
 *
 * @author mvk
 */
@Startup
@Singleton
@DependsOn( { //
    "S5EventServiceSingleton", //
    "S5CommandServiceSingleton", //
    "S5HistDataServiceSingleton", //
    "S5CurrDataServiceSingleton", //
    "S5PrefsServiceSingleton", //
    "S5RefbookServiceSingleton", //
    "S5UserServiceSingleton", //
    "S5LinkServiceSingleton", //
    "S5ObjectServiceSingleton", //
    "S5ClassServiceSingleton", //
    "S5ServerMessageServiceSingleton", //
    "S5GatewayServiceSingleton", //
    "S5AlarmServiceSingleton", //
    "S5ReportServiceSingleton", //
    "S5WorkstationConfigServiceSingleton" //

} )
@TransactionAttribute( TransactionAttributeType.SUPPORTS )
@SuppressWarnings( "unused" )
// @RunAsPrincipal( "root" )
// @RunAs( "guest, user, PowerUser" )
// @SecurityDomain( IMtServerApi.SECURITY_DOMAIN )
public class MccSingletonApi
    extends S5AbstractSingletonApi
    // Внимание! Несмотря на то что IMtSingletonApi расширяет IS5SingletonApi требуется указать оба интерфейса
    // (ограничение wildfly 9.0.1). Как следствие, приходится делать аннотацию @SuppressWarnings( "unused" )
    implements IMccSingletonApi, IS5SingletonApi {

  @EJB
  // Внимание! Должны использоваться РЕАЛИЗАЦИИ (не интерфейсы) - proxy должен реализовать IS5ServiceSingletonCtrl
  private S5AlarmServiceSingleton alarmServiceSingleton;

  @EJB
  // Внимание! Должны использоваться РЕАЛИЗАЦИИ (не интерфейсы) - proxy должен реализовать IS5ServiceSingletonCtrl
  private S5ReportServiceSingleton reportServiceSingleton;

  @EJB
  // Внимание! Должны использоваться РЕАЛИЗАЦИИ (не интерфейсы) - proxy должен реализовать IS5ServiceSingletonCtrl
  private S5WorkstationConfigServiceSingleton workstationConfigServiceSingleton;

  @Resource
  private SessionContext sessionContext;

  /**
   * Конструктор
   */
  public MccSingletonApi() {
    super( new S5ModuleOptionValue( MCC_SERVER_ID, MCC_SERVER_NAME, MCC_SERVER_DESCR, MCC_SERVER_VERSION ) );
  }

  // ------------------------------------------------------------------------------------
  // Реализация шаблонных методов AbstractS5SingletonApi
  //

  @Override
  protected void doRegisterAppSpecSingletons() {
    // Регистрация синглетонов служб проекта
    registerSingleton( alarmServiceSingleton );
    registerSingleton( reportServiceSingleton );
    registerSingleton( workstationConfigServiceSingleton );
  }

  @Override
  protected IS5SingletonApi getBusinessObject() {
    return sessionContext.getBusinessObject( IMccSingletonApi.class );
  }

  // ------------------------------------------------------------------------------------
  // Переопределение методов базового класса
  //
  // @PostConstruct эффективно только в конечном бине
  @PostConstruct
  @TransactionAttribute( TransactionAttributeType.REQUIRED )
  @Override
  public void init() {
    super.init();
    // IS5ClassServiceSingleton cs = (IS5ClassServiceSingleton)singletons().get( IClassService.SERVICE_ID );
    // Регистрация приложение-специфичных классов
    // tryCreateIfNeeded( cs, IMtSomeClass.CLASS_ID, MtSomeClassEntity.class, IMtSomeClass.class );
  }

  // @PreDestroy эффективно только в конечном бине
  @Override
  @PreDestroy
  @TransactionAttribute( TransactionAttributeType.REQUIRED )
  public void close() {
    super.close();
  }

  // ------------------------------------------------------------------------------------
  // Внутренние методы
  //

  /**
   * Проверяет существование класса сущности и создает ее класс если его нет в системе
   *
   * @param aService {@link IS5ClassServiceSingleton} служба управления классами
   * @param aClassId String идентификатор класса
   * @param aEntityInterface {@link Class}&lt; {@link S5ObjectEntity}&gt; интерфейс сущности
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  private void tryCreateIfNeeded( IS5ClassServiceSingleton aService, String aClassId,
      Class<? extends IBsObject> aEntityInterface ) {
    TsNullArgumentRtException.checkNulls( aClassId, aEntityInterface );
    IClassInfo classInfo = aService.findClassInfo( aClassId );
    if( classInfo == null ) {
      S5AnnotationParser parser = new S5AnnotationParser();
      classInfo = aService.createClass( parser.parseS5Class( S5ObjectEntity.class, aEntityInterface ), true );
      logger().info( FMT_INFO_MCC_CLASS_CREATION, classInfo.id(), classInfo.description() );
    }
  }

}
