package ru.toxsoft.mcc.client.connection;

import ru.toxsoft.s5.client.IServerClientApi;
import ru.toxsoft.s5.client.connection.IS5ClientServicesInitializer;
import ru.toxsoft.s5.client.impl.S5AbstractServiceClientImpl;
import ru.toxsoft.s5.sysaddons.client.services.alarms.S5AlarmServiceClientImpl;
import ru.toxsoft.s5.sysaddons.client.services.reports.S5ReportServiceClientImpl;
import ru.toxsoft.s5.sysaddons.client.services.wscfg.S5WorkstationConfigServiceClientImpl;
import ru.toxsoft.tslib.utils.collections.IList;
import ru.toxsoft.tslib.utils.collections.IListEdit;
import ru.toxsoft.tslib.utils.collections.impl.ElemLinkedList;

/**
 * Реализация интерфейса {@link IS5ClientServicesInitializer}
 *
 * @author mvk
 * @author goga
 */
public class MccServicesInitializer
    implements IS5ClientServicesInitializer {

  /**
   * Сигнлтон инициализатора служб.
   */
  public static IS5ClientServicesInitializer INSTANCE = new MccServicesInitializer();

  // ------------------------------------------------------------------------------------
  // IS5ClientServicesInitializer
  //

  @Override
  public IList<S5AbstractServiceClientImpl<?>> createClientServices( IServerClientApi aLocalApi ) {
    IListEdit<S5AbstractServiceClientImpl<?>> services = new ElemLinkedList<>();
    services.add( new S5AlarmServiceClientImpl( aLocalApi ) );
    services.add( new S5ReportServiceClientImpl( aLocalApi ) );
    services.add( new S5WorkstationConfigServiceClientImpl( aLocalApi ) );
    return services;
  }
}
