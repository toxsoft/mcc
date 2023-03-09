package ru.toxsoft.mcc.ws.mnemos.e4.addons;

import static org.toxsoft.core.tslib.av.impl.AvUtils.*;

import org.eclipse.e4.core.contexts.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.quant.*;
import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.mws.bases.*;
import org.toxsoft.core.tsgui.valed.impl.*;
import org.toxsoft.core.tslib.bricks.ctx.*;
import org.toxsoft.core.tslib.bricks.ctx.impl.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.core.tslib.utils.logs.impl.*;
import org.toxsoft.uskat.base.gui.conn.*;
import org.toxsoft.uskat.concurrent.*;
import org.toxsoft.uskat.core.connection.*;
import org.toxsoft.uskat.core.impl.*;
import org.toxsoft.uskat.s5.client.*;
import org.toxsoft.uskat.s5.client.remote.*;
import org.toxsoft.uskat.s5.common.*;
import org.toxsoft.uskat.s5.server.*;
import org.toxsoft.uskat.s5.utils.threads.impl.*;

import ru.toxsoft.mcc.ws.mnemos.*;
import ru.toxsoft.mcc.ws.mnemos.app.rt.alarm.*;
import ru.toxsoft.mcc.ws.mnemos.app.rt.chart.data_aliases.*;
import ru.toxsoft.mcc.ws.mnemos.app.valed.*;

/**
 * Plugin addon.
 *
 * @author vs
 */
public class AddonMccMnemos
    extends MwsAbstractAddon {

  /**
   * Constructor.
   */
  public AddonMccMnemos() {
    super( Activator.PLUGIN_ID );
  }

  @Override
  protected void initApp( IEclipseContext aAppContext ) {
    // nop
  }

  @Override
  protected void doRegisterQuants( IQuantRegistrator aQuantRegistrator ) {
    aQuantRegistrator.registerQuant( new QuantDataAliases() );
  }

  @Override
  protected void initWin( IEclipseContext aWinContext ) {
    IMccWsMnemosConstants.init( aWinContext );

    ValedControlFactoriesRegistry fr = aWinContext.get( ValedControlFactoriesRegistry.class );
    fr.registerFactory( ValedBooleanCheckAdv.FACTORY );
    fr.registerFactory( ValedAvBooleanCheckAdv.FACTORY );
    // GOGA 2023-03-09 fr.registerFactory( ValedIntegerTextCommand.FACTORY );

    ISkConnectionSupplier connSupplier = aWinContext.get( ISkConnectionSupplier.class );
    openConnection( connSupplier.defConn(), aWinContext );
    aWinContext.set( ISkConnectionSupplier.class, connSupplier );

    // регистрируем свои m5 модели TODO перенести в специальное место, см. KM5DataAliasesContributor
    IM5Domain m5 = aWinContext.get( IM5Domain.class );
    m5.addModel( new SkAlarmM5Model( false ) );
  }

  private void openConnection( ISkConnection aConn, IEclipseContext aWinContext ) {
    String login = "root"; //$NON-NLS-1$
    String password = "1"; //$NON-NLS-1$
    IStringList hostnames = new StringArrayList( "localhost" ); //$NON-NLS-1$
    IIntList ports = new IntArrayList( 8080 );
    int connectTimeout = 3000;
    int failureTimeout = 120000;
    int currdataTimeout = 1000;
    int histdataTimeout = 10000;

    // Создание соединения
    S5HostList hosts = new S5HostList();
    for( int index = 0, n = hostnames.size(); index < n; index++ ) {
      hosts.add( new S5Host( hostnames.get( index ), ports.getValue( index ) ) );
    }
    ITsContext ctx = new TsContext();
    ISkCoreConfigConstants.REFDEF_BACKEND_PROVIDER.setRef( ctx, new S5RemoteBackendProvider() );
    IS5ConnectionParams.OP_USERNAME.setValue( ctx.params(), avStr( login ) );
    IS5ConnectionParams.OP_PASSWORD.setValue( ctx.params(), avStr( password ) );

    IS5ConnectionParams.OP_HOSTS.setValue( ctx.params(), avValobj( hosts ) );
    IS5ConnectionParams.OP_CLIENT_PROGRAM.setValue( ctx.params(), avStr( "skadmin" ) ); //$NON-NLS-1$
    IS5ConnectionParams.OP_CLIENT_VERSION.setValue( ctx.params(), avValobj( IS5ServerHardConstants.version ) );
    IS5ConnectionParams.OP_CONNECT_TIMEOUT.setValue( ctx.params(), avInt( connectTimeout ) );
    IS5ConnectionParams.OP_FAILURE_TIMEOUT.setValue( ctx.params(), avInt( failureTimeout ) );
    IS5ConnectionParams.OP_CURRDATA_TIMEOUT.setValue( ctx.params(), avInt( currdataTimeout ) );
    IS5ConnectionParams.OP_HISTDATA_TIMEOUT.setValue( ctx.params(), avInt( histdataTimeout ) );
    IS5ConnectionParams.REF_CONNECTION_LOCK.setRef( ctx, new S5Lockable() );
    // 2022-10-25 mvk обязательно для RCP
    IS5ConnectionParams.REF_CLASSLOADER.setRef( ctx, getClass().getClassLoader() );

    ISkCoreConfigConstants.REFDEF_THREAD_SEPARATOR.setRef( ctx, SwtThreadSeparatorService.CREATOR );
    SwtThreadSeparatorService.REF_DISPLAY.setRef( ctx, aWinContext.get( Display.class ) );

    try {
      ISkConnection syncConn = S5SynchronizedConnection.createSynchronizedConnection( aConn );
      syncConn.open( ctx );
      LoggerUtils.defaultLogger().info( "Connection opened" ); //$NON-NLS-1$
    }
    catch( Exception ex ) {
      LoggerUtils.errorLogger().error( ex );
    }
  }
}
