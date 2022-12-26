package ru.toxsoft.mcc.ws.journals.e4.uiparts.engine;

import org.toxsoft.core.tslib.bricks.filter.*;
import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.coll.*;

/**
 * Перечень фильтров для выборки интересующих сущностей из соответствующей службы
 * <p>
 * Используется как аргумент для выполнения запроса движком {@link IQueryEngine}.
 *
 * @author dima
 */
public interface IJournalQueryFilter
    extends IKeepableEntity {

  /**
   * Возвращает все элементы перечня фильтров.
   *
   * @return {@link IList}&lt;{@link ITsCombiFilterParams}&gt; - список всех фильров
   */
  IList<ITsCombiFilterParams> items();

}
