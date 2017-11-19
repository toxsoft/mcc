package ru.toxsoft.mcc.ws.launcher.rcp;

import ru.toxsoft.mws.base.mwsservice.ITsModularWorkstationService;
import ru.toxsoft.tsgui.e4.app.TsActivator;

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

  /**
   * Экземпляр этого плагина - синглтона.
   */
  private static Activator instance = null;

  /**
   * Название ключа настроек программы в реестре ОС.
   */
  private static final String APP_SETTINGS_ROOT_NAME = "/ru/toxsoft/mcc"; //$NON-NLS-1$

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
