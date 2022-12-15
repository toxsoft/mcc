package ru.toxsoft.mcc.ws.mnemos.app.rt;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.bricks.*;
import org.toxsoft.core.tslib.bricks.strid.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.gw.gwid.*;
import org.toxsoft.core.tslib.utils.logs.impl.*;
import org.toxsoft.uskat.core.api.rtdserv.*;
import org.toxsoft.uskat.core.connection.*;
import org.toxsoft.uskat.core.utils.*;

/**
 * Реализация {@link IRtDataProvider} для проекта "МосКокс".
 * <p>
 *
 * @author vs
 */
public class MccRtDataProvider
    implements IRtDataProvider, ITsGuiContextable, ISkConnected {

  static class KnownConsumer
      implements IStridable, ICooperativeMultiTaskable {

    private final IRtDataConsumer consumer;

    private final Gwid[] gwids;

    private final IAtomicValue[] values;

    private final Gwid[] allGwids;

    private final ISkReadCurrDataChannel[] channels;

    private final IAtomicValue[] oldValues;

    KnownConsumer( IRtDataConsumer aConsumer ) {
      consumer = aConsumer;
      IGwidList gl = aConsumer.listNeededGwids();
      allGwids = new Gwid[gl.size()];
      for( int i = 0; i < allGwids.length; i++ ) {
        allGwids[i] = gl.get( i );
      }
      oldValues = new IAtomicValue[gl.size()];
      gwids = new Gwid[gl.size()];
      values = new IAtomicValue[gl.size()];
      channels = new ISkReadCurrDataChannel[gl.size()];
    }

    // ------------------------------------------------------------------------------------
    // IStridable
    //

    @Override
    public String id() {
      return consumer.id();
    }

    @Override
    public String nmName() {
      return consumer.nmName();
    }

    @Override
    public String description() {
      return consumer.nmName();
    }

    // ------------------------------------------------------------------------------------
    // ICooperativeMultiTaskable
    //

    @Override
    public void doJob() {
      int count = 0;
      for( int i = 0; i < channels.length; i++ ) {
        IAtomicValue av = channels[i].getValue();
        if( !av.equals( oldValues[i] ) ) {
          gwids[count] = allGwids[i];
          values[count] = av;
          oldValues[i] = av;
          count++;
        }
      }
      if( count > 0 ) {
        consumer.setValues( gwids, values, count );
      }
    }

    // ------------------------------------------------------------------------------------
    // API
    //

    void selectChannels( IMap<Gwid, ISkReadCurrDataChannel> aChannels ) {
      for( int i = 0; i < allGwids.length; i++ ) {
        if( !aChannels.hasKey( allGwids[i] ) ) {
          LoggerUtils.errorLogger().error( "Can not create read chanel fo gwid" + allGwids[i].toString() ); //$NON-NLS-1$
        }
        ISkReadCurrDataChannel channel = aChannels.getByKey( allGwids[i] );
        channels[i] = channel;
        oldValues[i] = channel.getValue();
        gwids[i] = allGwids[i];
        values[i] = oldValues[i];
      }
      consumer.setValues( gwids, values, allGwids.length );
    }

    void dispose() {
      for( int i = 0; i < channels.length; i++ ) {
        // FIXME разобраться с закрытием канала
        // channels[i].close();
      }
    }

  }

  private final GwidList gwidList = new GwidList();

  private final IStridablesListEdit<IRtDataConsumer> consumers = new StridablesList<>();

  private final IStridablesListEdit<KnownConsumer> knownConsumers = new StridablesList<>();

  private final ITsGuiContext tsContext;

  private final ISkConnection skConnection;

  private boolean running = false;

  private final IRealTimeSensitive timerHandler = aGwTime -> {
    for( KnownConsumer consumer : this.knownConsumers ) {
      consumer.doJob();
    }
  };

  /**
   * Конструктор.<br>
   *
   * @param aSkConnection ISkConnection - соединение с сервером
   * @param aTsContext ITsGuiContext - соответствующий контекст
   */
  public MccRtDataProvider( ISkConnection aSkConnection, ITsGuiContext aTsContext ) {
    skConnection = aSkConnection;
    tsContext = aTsContext;
  }

  // ------------------------------------------------------------------------------------
  // ISkConnected
  //

  @Override
  public ITsGuiContext tsContext() {
    return tsContext;
  }

  // ------------------------------------------------------------------------------------
  // ISkConnected
  //

  @Override
  public ISkConnection skConn() {
    return skConnection;
  }

  // ------------------------------------------------------------------------------------
  // IRtDataProvider
  //

  @Override
  public void start() {
    if( running ) {
      return;
    }
    running = true;
    guiTimersService().slowTimers().addListener( timerHandler );
    IMap<Gwid, ISkReadCurrDataChannel> map = skRtdataServ().createReadCurrDataChannels( gwidList );
    for( KnownConsumer kc : knownConsumers ) {
      kc.selectChannels( map );
    }
  }

  @Override
  public void addDataConsumer( IRtDataConsumer aConsumer ) {
    if( !consumers.hasKey( aConsumer.id() ) ) {
      consumers.add( aConsumer );
      knownConsumers.add( new KnownConsumer( aConsumer ) );
      gwidList.addAll( aConsumer.listNeededGwids() );
    }
  }

  @Override
  public IRtDataConsumer removeDataConsumer( String aConsumerId ) {
    KnownConsumer kc = knownConsumers.removeById( aConsumerId );
    if( kc != null ) {
      kc.dispose();
    }
    return consumers.removeById( aConsumerId );
  }

  @Override
  public IStridablesList<IRtDataConsumer> listConsumers() {
    return consumers;
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Освобождает ресурсы
   */
  @Override
  public void dispose() {
    running = false;
    guiTimersService().slowTimers().removeListener( timerHandler );
    for( KnownConsumer consumer : knownConsumers ) {
      consumer.dispose();
    }
    gwidList.clear();
    consumers.clear();
    knownConsumers.clear();
  }

  // ------------------------------------------------------------------------------------
  // Implementation
  //

}
