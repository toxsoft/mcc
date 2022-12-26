package ru.toxsoft.mcc.ws.journals.e4.uiparts.engine;

import org.eclipse.e4.core.contexts.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.panels.lazy.*;
import org.toxsoft.core.tslib.bricks.events.change.*;
import org.toxsoft.core.tslib.bricks.time.*;

/**
 * Панель выбора параметров просмотра журнала.
 * <p>
 * Предназначен для расположения в качестве панели инструментов сверху над таблицей в одном из вью просмотра журнала.
 * <p>
 * Содержит контроли для выбора следующих параметров выборки : интервал времени, объекты, идентификаторы параметров
 * событий. Кроме того, есть кнопка "запуск", нажатие на которое приводит к генерации события
 * {@link IGenericChangeListener#onGenericChanged(Object)}, в ответ на который должен быть сделан запрос к серверу и
 * должна обновиться таблица событий.
 *
 * @author goga, dima
 */
public interface IJournalParamsPanel
    extends ILazyControl<Control>, IGenericChangeEventer {

  /**
   * Текущее действие выполненное пользователем на панели по которому сгенерировано событие onGenericChanged
   *
   * @author dima
   */
  public enum ECurrentAction {
    /**
     * Запросить все сущности
     */
    QUERY_ALL(),

    /**
     * Запросить сущности в соответствии с фильтром
     */
    QUERY_SELECTED(),

    /**
     * Export to Excel
     */
    EXPORT_EXCEL(),

    /**
     * Поиск в отображаемом списке.
     */
    SEARCH_IN_LIST(),

    /**
     * Печать списка.
     */
    PRINT(),

    ;

  }

  /**
   * Возвращает интервал времени запроса.
   * <p>
   * Метод возвращает тот интервал, который введен в данный момент в экранные контроли (виджеты).
   *
   * @return {@link ITimeInterval} - интервал времени запроса
   */
  ITimeInterval interval();

  /**
   * Возвращает параметры запроса (перечень объектов-источников и их событий).
   * <p>
   * Метод возвращает те параметры, которые введены в данный момент в экранные контроли (виджеты) редактирования
   * параметров.
   *
   * @return {@link IConcerningEventsParams} - параметры запроса
   */
  IConcerningEventsParams selectedParams();

  /**
   * Установить контекст приложения
   *
   * @param aAppContext контекст приложения
   */
  void setAppContext( IEclipseContext aAppContext );

  /**
   * Установить контекст
   *
   * @param aContext контекст
   */
  void setContext( ITsGuiContext aContext );

  /**
   * Получить текущее действие пользователя вызвавшее событие onGenericChanged
   *
   * @return ECurrentAction текущее действие пользователя
   */
  ECurrentAction currentAction();

  /**
   * Получить имя файла для экспорта в Excel.
   *
   * @return String - имя файла для экспорта в Excel
   */
  String xlsFileName();

  /**
   * Получить строку поиска.
   *
   * @return String - строка для поиска в списке.
   */
  String searchString();
}
