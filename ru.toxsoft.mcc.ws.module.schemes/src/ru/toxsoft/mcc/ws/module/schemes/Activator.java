package ru.toxsoft.mcc.ws.module.schemes;

import ru.toxsoft.mws.base.mwsservice.impl.TsWorkstationModuleActivator;

/**
 * Активатор плагина.
 *
 * @author goga
 */
public class Activator
    extends TsWorkstationModuleActivator {

  /**
   * Идентификатор плагина.
   */
  public static final String PLUGIN_ID = "ru.toxsoft.mcc.ws.module.schemes"; //$NON-NLS-1$

  // ссылка на свой единственный экземпляр
  private static Activator instance = null;

  /**
   * Пустой конструктор.
   */
  public Activator() {
    super( PLUGIN_ID );
    checkInstance( instance );
    instance = this;
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
