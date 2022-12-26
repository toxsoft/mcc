package ru.toxsoft.mcc.ws.journals.e4.uiparts.engine;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.uskat.core.api.objserv.*;

/**
 * Модель объектов для отображения в дереве иерархия в которой не совпадает с системным описанием
 *
 * @author max, dima
 */
public interface ILibSkObjectsTreeModel {

  /**
   * Инициализирует модель дерева параметров по контексту приложения
   *
   * @param aContext {@link ITsGuiContext} - контекст.
   */
  void init( ITsGuiContext aContext );

  /**
   * Возвращает массив корневых объектов дерева.
   *
   * @return IList - массив корневых объектов ддерева.
   */
  IList<ISkObject> getRootObjects();

  /**
   * Возвращает дочерние объекты родительского объекта.
   *
   * @param aParentObject ISkObject - родительский объект.
   * @return IList - массив дочерних объектов.
   */
  IList<ISkObject> getChildren( ISkObject aParentObject );
}
