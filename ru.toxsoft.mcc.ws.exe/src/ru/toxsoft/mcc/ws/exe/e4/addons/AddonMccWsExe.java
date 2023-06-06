package ru.toxsoft.mcc.ws.exe.e4.addons;

import static org.toxsoft.core.tslib.av.impl.AvUtils.*;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.basic.MTrimmedWindow;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.swt.widgets.Display;
import org.toxsoft.core.tsgui.bricks.quant.IQuantRegistrator;
import org.toxsoft.core.tsgui.graphics.icons.EIconSize;
import org.toxsoft.core.tsgui.graphics.icons.impl.TsIconManagerUtils;
import org.toxsoft.core.tsgui.mws.Activator;
import org.toxsoft.core.tsgui.mws.IMwsCoreConstants;
import org.toxsoft.core.tsgui.mws.bases.MwsAbstractAddon;
import org.toxsoft.core.tslib.bricks.ctx.ITsContext;
import org.toxsoft.core.tslib.bricks.ctx.impl.TsContext;
import org.toxsoft.core.tslib.coll.primtypes.IIntList;
import org.toxsoft.core.tslib.coll.primtypes.IStringList;
import org.toxsoft.core.tslib.coll.primtypes.impl.IntArrayList;
import org.toxsoft.core.tslib.coll.primtypes.impl.StringArrayList;
import org.toxsoft.core.tslib.utils.logs.impl.LoggerUtils;
import org.toxsoft.skf.onews.gui.QuantSkOneWsGui;
import org.toxsoft.skf.reports.gui.IReportsGuiConstants;
import org.toxsoft.skf.users.gui.QuantSkUsersGui;
import org.toxsoft.uskat.concurrent.S5SynchronizedConnection;
import org.toxsoft.uskat.core.api.users.ISkLoggedUserInfo;
import org.toxsoft.uskat.core.connection.ISkConnection;
import org.toxsoft.uskat.core.gui.QuantSkCoreGui;
import org.toxsoft.uskat.core.gui.conn.ISkConnectionSupplier;
import org.toxsoft.uskat.core.impl.ISkCoreConfigConstants;
import org.toxsoft.uskat.s5.client.IS5ConnectionParams;
import org.toxsoft.uskat.s5.client.remote.S5RemoteBackendProvider;
import org.toxsoft.uskat.s5.common.S5Host;
import org.toxsoft.uskat.s5.common.S5HostList;
import org.toxsoft.uskat.s5.server.IS5ServerHardConstants;
import org.toxsoft.uskat.s5.utils.threads.impl.S5Lockable;

/**
 * Application addon.
 *
 * @author hazard157
 */
public class AddonMccWsExe
    extends MwsAbstractAddon {

  /**
   * Constructor.
   */
  public AddonMccWsExe() {
    super( Activator.PLUGIN_ID );
  }

  @Override
  protected void doRegisterQuants( IQuantRegistrator aQuantRegistrator ) {
    // регистрация М5 моделей работы с пользователями и правами доступа
    aQuantRegistrator.registerQuant( new QuantSkUsersGui() );
    aQuantRegistrator.registerQuant( new QuantSkOneWsGui() );
    // 2023-06-06 mvk TODO: ???
    aQuantRegistrator.registerQuant( new QuantSkCoreGui() );
  }

  @Override
  protected void initApp( IEclipseContext aAppContext ) {
    // application and main window icon
    MApplication app = aAppContext.get( MApplication.class );
    EModelService modelService = aAppContext.get( EModelService.class );
    MTrimmedWindow mainWindow = (MTrimmedWindow)modelService.find( IMwsCoreConstants.MWSID_WINDOW_MAIN, app );
    mainWindow.setIconURI( TsIconManagerUtils.makeStdIconUriString( org.toxsoft.skf.reports.gui.Activator.PLUGIN_ID,
        IReportsGuiConstants.ICONID_APP_ICON, EIconSize.IS_48X48 ) );
  }

  @Override
  protected void initWin( IEclipseContext aWinContext ) {
    // setup connection to server
    ISkConnectionSupplier connSupplier = aWinContext.get( ISkConnectionSupplier.class );
    openConnection( connSupplier.defConn(), aWinContext );
    aWinContext.set( ISkConnectionSupplier.class, connSupplier );
  }

  private void openConnection( ISkConnection aConn, IEclipseContext aWinContext ) {
    String login = "root"; //$NON-NLS-1$
    String password = "1"; //$NON-NLS-1$
    IStringList hostnames = new StringArrayList( "localhost" ); //$NON-NLS-1$
    // IStringList hostnames = new StringArrayList( "192.168.2.100" ); //$NON-NLS-1$
    IIntList ports = new IntArrayList( 8080 );
    int connectTimeout = 30000;
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
      ISkLoggedUserInfo userInfo = aConn.coreApi().getCurrentUserInfo();
      LoggerUtils.defaultLogger().info( "%s", userInfo );
    }
    catch( Exception ex ) {
      LoggerUtils.errorLogger().error( ex );
    }
  }
}
