package ru.toxsoft.mcc.ws.mnemos.app.utils;

import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.gw.gwid.*;
import org.toxsoft.uskat.core.*;
import org.toxsoft.uskat.core.api.rtdserv.*;

/**
 * Набор вспомогательных методов для работы с РВ-данными.
 * <p>
 *
 * @author vs
 */
public class MccRtUtils {

  /**
   * Возвращает значение РВ-данного.<br>
   *
   * @param aGwid Gwid - конкретный ИД РВ-данного
   * @param aCoreApi - API сервера
   * @return IAtomicValue - значение данного
   */
  public static IAtomicValue readRtData( Gwid aGwid, ISkCoreApi aCoreApi ) {
    ISkRtdataService rtServ = aCoreApi.rtdService();
    IMap<Gwid, ISkReadCurrDataChannel> channels = rtServ.createReadCurrDataChannels( new GwidList( aGwid ) );
    ISkReadCurrDataChannel rtdChannel = rtServ.createReadCurrDataChannels( new GwidList( aGwid ) ).values().first();
    return rtdChannel.getValue();
  }

  /**
   * Записывает значение РВ-данного.<br>
   *
   * @param aValue IAtomicValue - значение, которое необходимо записать
   * @param aGwid Gwid - конкретный ИД РВ-данного
   * @param aCoreApi ISkCoreApi - API сервера
   */
  public static void writeRtData( IAtomicValue aValue, Gwid aGwid, ISkCoreApi aCoreApi ) {
    ISkRtdataService rtServ = aCoreApi.rtdService();
    ISkWriteCurrDataChannel rtdChannel = rtServ.createWriteCurrDataChannels( new GwidList( aGwid ) ).values().first();
    rtdChannel.setValue( aValue );
    rtdChannel.close();
  }

  /**
   * Запрет на создание экземпляров.
   */
  private MccRtUtils() {
    // nop
  }
}
