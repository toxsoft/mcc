package ru.toxsoft.mcc.server.impl;

import javax.ejb.Local;

import ru.toxsoft.s5.server.IS5ServerApiLocal;

/**
 * Локальный итерфейс сервер {@link IMccServerApi}.
 *
 * @author goga
 */
@Local
public interface IMccServerApiLocal
    extends IS5ServerApiLocal, IMccServerApi {

  // нет специфичных локальных методов

}
