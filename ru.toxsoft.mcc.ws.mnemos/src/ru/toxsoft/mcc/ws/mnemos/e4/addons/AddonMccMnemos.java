package ru.toxsoft.mcc.ws.mnemos.e4.addons;

import static org.toxsoft.core.tslib.av.impl.AvUtils.*;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.swt.widgets.Display;
import org.toxsoft.core.tsgui.bricks.ctx.ITsGuiContext;
import org.toxsoft.core.tsgui.bricks.ctx.impl.TsGuiContext;
import org.toxsoft.core.tsgui.mws.bases.MwsAbstractAddon;
import org.toxsoft.core.tsgui.valed.impl.ValedControlFactoriesRegistry;
import org.toxsoft.core.tslib.bricks.ctx.ITsContext;
import org.toxsoft.core.tslib.bricks.ctx.impl.TsContext;
import org.toxsoft.core.tslib.bricks.strid.more.IdChain;
import org.toxsoft.core.tslib.coll.primtypes.IIntList;
import org.toxsoft.core.tslib.coll.primtypes.IStringList;
import org.toxsoft.core.tslib.coll.primtypes.impl.IntArrayList;
import org.toxsoft.core.tslib.coll.primtypes.impl.StringArrayList;
import org.toxsoft.core.tslib.utils.logs.impl.LoggerUtils;
import org.toxsoft.uskat.base.gui.conn.ISkConnectionSupplier;
import org.toxsoft.uskat.base.gui.conn.SkConnectionSupplier;
import org.toxsoft.uskat.concurrent.S5SynchronizedConnection;
import org.toxsoft.uskat.core.connection.ISkConnection;
import org.toxsoft.uskat.core.impl.ISkCoreConfigConstants;
import org.toxsoft.uskat.s5.client.IS5ConnectionParams;
import org.toxsoft.uskat.s5.client.remote.S5RemoteBackendProvider;
import org.toxsoft.uskat.s5.common.S5Host;
import org.toxsoft.uskat.s5.common.S5HostList;
import org.toxsoft.uskat.s5.server.IS5ServerHardConstants;
import org.toxsoft.uskat.s5.utils.threads.impl.S5Lockable;

import ru.toxsoft.mcc.ws.mnemos.Activator;
import ru.toxsoft.mcc.ws.mnemos.IMccWsMnemosConstants;
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
    // TODO Auto-generated constructor stub
  }

  @Override
  protected void initApp( IEclipseContext aAppContext ) {
    // TODO Auto-generated method stub
  }

  @Override
  protected void initWin( IEclipseContext aWinContext ) {
    IMccWsMnemosConstants.init( aWinContext );

    ValedControlFactoriesRegistry fr = aWinContext.get( ValedControlFactoriesRegistry.class );
    fr.registerFactory( ValedBooleanCheckAdv.FACTORY );
    fr.registerFactory( ValedAvBooleanCheckAdv.FACTORY );
    fr.registerFactory( ValedIntegerTextCommand.FACTORY );

    ISkConnectionSupplier connSupplier = new SkConnectionSupplier();
    createConnection( connSupplier, aWinContext );
    aWinContext.set( ISkConnectionSupplier.class, connSupplier );
  }

  private void createConnection( ISkConnectionSupplier aConnSupp, IEclipseContext aWinContext ) {
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
      // SkUtils.OP_EXT_SERV_PROVIDER_CLASS.setValue( ctx.params(), initializator );
      ITsGuiContext guiCtx = new TsGuiContext( aWinContext );
      IdChain connIdc = new IdChain( "connection.default" ); //$NON-NLS-1$
      ISkConnection conn = aConnSupp.createConnection( connIdc, guiCtx );
      ISkConnection syncConn = S5SynchronizedConnection.createSynchronizedConnection( conn );
      syncConn.open( ctx );
      LoggerUtils.defaultLogger().info( "Connection opened" ); //$NON-NLS-1$
      // LoggerUtils.defaultLogger().info( "Connection opened, IDC= %s", connIdc.toString() );
      aConnSupp.setDefaultConnection( connIdc );
    }
    catch( Exception ex ) {
      LoggerUtils.errorLogger().error( ex );
    }
  }
}
