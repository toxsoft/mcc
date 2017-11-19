package ru.toxsoft.mcc.server.impl;

import javax.ejb.Remote;

import ru.toxsoft.s5.server.IS5ServerApiRemote;

/**
 * Удаленный итерфейс сервера {@link IMccServerApi}.
 * 
 * @author goga
 */
@Remote
public interface IMccServerApiRemote
    extends IS5ServerApiRemote, IMccServerApi {

  // нет специфичных удаленных методов

}
