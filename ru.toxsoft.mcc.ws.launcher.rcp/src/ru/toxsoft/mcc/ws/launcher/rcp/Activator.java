package ru.toxsoft.mcc.ws.launcher.rcp;

import ru.toxsoft.mcc.client.connection.MccServicesInitializer;
import ru.toxsoft.mcc.server.impl.IMccServerApi;
import ru.toxsoft.mws.base.incub.tsappinfo.ITsApplicationInfo;
import ru.toxsoft.mws.base.incub.tsappinfo.TsApplicationInfo;
import ru.toxsoft.mws.base.mwsservice.ITsModularWorkstationService;
import ru.toxsoft.mws.module.s5.conn.EOpenCmdStartegy;
import ru.toxsoft.mws.module.s5.conn.IMwsModuleS5ConnConstants;
import ru.toxsoft.mws.module.s5.conn.cfg.IS5ConnParameters;
import ru.toxsoft.mws.module.s5.conn.cfg.impl.S5ConnConfig;
import ru.toxsoft.tsgui.e4.app.TsActivator;
import ru.toxsoft.tslib.utils.login.ILoginInfo;
import ru.toxsoft.tslib.utils.login.LoginInfo;
import ru.toxsoft.tslib.utils.version.DefaultTsVersion;
import ru.toxsoft.tslib.utils.version.ITsVersion;

/**
 * Активатор плагина.
 *
 * @author goga
 */
public class Activator extends TsActivator {

	/**
	 * Идентификатор плагина.
	 */
	public static final String PLUGIN_ID = "ru.toxsoft.mcc.ws.launcher.rcp"; //$NON-NLS-1$

	/**
	 * Экземпляр этого плагина - синглтона.
	 */
	private static Activator instance = null;

	/**
	 * Название ключа настроек программы в реестре ОС.
	 */
	// private static final String APP_SETTINGS_ROOT_NAME = "/ru/toxsoft/mcc";
	// //$NON-NLS-1$

	// -----------------------------
	String STR_N_APP = "МОСКОКС";
	String STR_D_APP = "Модульный клиент системы МОСКОКС";

	String APP_ID = "ru.toxsoft.mcc.mws";
	String APP_ALIAS = "mcc";
	ITsVersion APP_VERSION = new DefaultTsVersion(1, 0);

	ITsApplicationInfo APP_INFO = new TsApplicationInfo(APP_ID, STR_N_APP, STR_D_APP, APP_ALIAS, APP_VERSION);
	// ------------------------------

	/**
	 * Пустой конструктор.
	 */
	public Activator() {
		super(PLUGIN_ID);
		checkInstance(instance);
		instance = this;
	}

	/**
	 * Настраивает конфигурацию модулей, которые будут запскаться.
	 */
	@SuppressWarnings("nls")
	@Override
	protected void doStart() {
		ITsModularWorkstationService mws = Activator.getInstance().getOsgiService(ITsModularWorkstationService.class);
		mws.setAppInfo(APP_INFO);
		// mws.setAppSettingsRootName( APP_SETTINGS_ROOT_NAME );
		// настройка умолчаний соединения с сервером (модуль mws.module.s5.conn)
		mws.mwsContext().params().setBool(IMwsModuleS5ConnConstants.ALWAYS_USE_FILE_MENU, true);

		S5ConnConfig scc = new S5ConnConfig();
		scc.params().setStr(IS5ConnParameters.API_BEAN_NAME, IMccServerApi.API_BEAN_NAME);
		scc.params().setStr(IS5ConnParameters.API_INTERFACE, IMccServerApi.API_INTERFACE_NAME);
		scc.params().setStr(IS5ConnParameters.MODULE_NAME, IMccServerApi.APP_MODULE_NAME);
		scc.params().setStr(IS5ConnParameters.CLIENT_SERVICE_INITER_CLASS_NAME, MccServicesInitializer.class.getName());
		scc.params().setStr(IS5ConnParameters.HOST_NAME, "localhost");

		IMwsModuleS5ConnConstants.CONFIG_DEFAULTS.setValue(mws.mwsContext().params(), scc);

		ILoginInfo dli = new LoginInfo("root", "1");
		IMwsModuleS5ConnConstants.DEFAULT_LOGIN_INFO.setValue(mws.mwsContext().params(), dli);
		IMwsModuleS5ConnConstants.OPEN_CMD_STRATEGY.setValue(mws.mwsContext().params(), EOpenCmdStartegy.OPEN_LAST);
	}

	/**
	 * Возвращает ссылку на единственный экземпляр этого активатора.
	 *
	 * @return {@link Activator} - ссылка на единственный экземпляр
	 */
	public static Activator getInstance() {
		return instance;
	}

}
