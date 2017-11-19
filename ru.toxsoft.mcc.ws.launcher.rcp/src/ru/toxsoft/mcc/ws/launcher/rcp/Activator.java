package ru.toxsoft.mcc.ws.launcher.rcp;

import static ru.toxsoft.tslib.datavalue.impl.DvUtils.*;

import ru.toxsoft.mws.base.mwsservice.ITsModularWorkstationService;
import ru.toxsoft.mws.module.tsproject.IMwsModuleTsProjectConstants;
import ru.toxsoft.tsgui.e4.app.TsActivator;
import ru.toxsoft.tslib.patterns.txtprojs.proj.TsProjectFileFormatInfo;

/**
 * Активатор плагина.
 *
 * @author goga
 */
public class Activator
    extends TsActivator {

  /**
   * Идентификатор плагина.
   */
  public static final String PLUGIN_ID = "ru.toxsoft.mcc.ws.launcher.rcp"; //$NON-NLS-1$
  private static Activator   instance  = null;

  private static final String APP_SETTINGS_ROOT_NAME    = "/com/hazard157/psx4"; //$NON-NLS-1$
  private static final int    PROJ_FILE_FORMAT_VERSTION = 4;
  private static final String PROJ_FILE_APP_ID          = "com.hazard157.psx4";  //$NON-NLS-1$

  private static final TsProjectFileFormatInfo PFF_INFO =
      new TsProjectFileFormatInfo( PROJ_FILE_APP_ID, PROJ_FILE_FORMAT_VERSTION );

  /**
   * Пустой конструктор.
   */
  public Activator() {
    super( PLUGIN_ID );
    checkInstance( instance );
    instance = this;
  }

  /**
   * Настраивает конфигурацию модулей, которые будут запскаться.
   */
  @Override
  protected void doStart() {
    ITsModularWorkstationService mws = Activator.getInstance().getOsgiService( ITsModularWorkstationService.class );
    IMwsModuleTsProjectConstants.ALWAYS_USE_FILE_MENU.setValue( mws.mwsContext().params(), DV_TRUE );
    IMwsModuleTsProjectConstants.REF_PROJECT_FILE_FORMAT_INFO.setValue( mws.mwsContext(), PFF_INFO );
    mws.setAppSettingsRootName( APP_SETTINGS_ROOT_NAME );
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
