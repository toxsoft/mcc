package ru.toxsoft.mcc.server.impl;

import javax.ejb.Local;

import ru.toxsoft.s5.server.impl.singletons.IS5SingletonApi;

/**
 * Локальный интерфейс, "собирающий" все синглтоны служб сервера.
 *
 * @author mvk
 */
@Local
public interface IMccSingletonApi
    extends IS5SingletonApi {
  // nop
}
