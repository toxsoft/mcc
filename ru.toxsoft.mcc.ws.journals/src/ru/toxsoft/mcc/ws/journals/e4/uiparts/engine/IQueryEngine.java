package ru.toxsoft.mcc.ws.journals.e4.uiparts.engine;

import org.eclipse.core.runtime.*;
import org.eclipse.jface.operation.*;
import org.toxsoft.core.tslib.bricks.time.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Движок запроса и получения историческийх сущностей (события, команды и т.д.)
 * <p>
 * Движок осуществляет запрос синхрнонно в методах {@link #run(IProgressMonitor)} или
 * {@link #query(ITimeInterval, IJournalQueryFilter, ICallback)}.
 *
 * @author goga
 * @param <T> - класс запрашиваемых историческийх сущностей.
 */
public interface IQueryEngine<T>
    extends IRunnableWithProgress {

  /**
   * Интерфейс обратного вызова по мере осуществления синхронного запроса.
   * <p>
   * Позволяет отслеживать процесс выполнения запроса, а также, прервать запрос.
   *
   * @author goga
   */
  interface ICallback {

    /**
     * "Нулевой" экземпляр для использования вместо <code>null</code>.
     */
    ICallback NULL = aDonePercent -> false;

    /**
     * Вызывается изнутри движка, когда выполнен очередной этап запроса.
     * <p>
     * Гарантий, что метод будет вызван хоть раз - нету! Например, запрос с пустыми параметрами врнет сразу пустой
     * список.
     * <p>
     * Пользователь в реализации может вернуть <code>true</code>, чтобы прекратить выполнение запроса.
     *
     * @param aDonePercent double - степень выполнения от 0.0 до 100.0 (в процентах)
     * @return boolean - признак прекращения запроса<br>
     *         <b>true</b> - метод {@link IQueryEngine#query(ITimeInterval, IJournalQueryFilter, ICallback)} прекратит
     *         запрос и вернет вбранные к этому моменту события;<br>
     *         <b>false</b> - выполнение запроса продолжится.
     */
    boolean onNextStep( double aDonePercent );

  }

  /**
   * Задает параметры для использования методом {@link #run(IProgressMonitor)}.
   *
   * @param aInterval {@link ITimeInterval} - интервал запроса
   * @param aFilter {@link IJournalQueryFilter} - параметры запроса
   */
  void setQueryParams( ITimeInterval aInterval, IJournalQueryFilter aFilter );

  /**
   * Возвращает результаты запроса методом {@link #run(IProgressMonitor)}.
   *
   * @return {@link IList} - список полученных сущностей
   */
  IList<T> getResult();

  /**
   * Синхронно запрашивает события у сервера и возвращает их в удобно-отображаемом виде.
   *
   * @param aInterval {@link ITimeInterval} - интервал запроса
   * @param aFilter {@link IConcerningEventsParams} - параметры (события и объекты) запроса
   * @param aCallback {@link ICallback} - интерфес обратного вызова по мере обработки запроса
   * @return {@link IList} - список полученных сущностей
   * @throws TsNullArgumentRtException любой аргумент = null
   * @throws TsIllegalStateRtException предыдущий запрос еще не завершен
   */
  IList<T> query( ITimeInterval aInterval, IJournalQueryFilter aFilter, ICallback aCallback );

}
