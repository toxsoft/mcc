package ru.toxsoft.mcc.ws.journals.e4.uiparts.engine;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.uskat.core.api.sysdescr.*;
import org.toxsoft.uskat.core.api.sysdescr.dto.*;

/**
 * Модель дерева классов и описаний.
 *
 * @author max
 * @author dima
 * @param <T> - класс описания параметра класса (события, команды)
 */
public interface ILibClassInfoesTreeModel<T extends IDtoClassPropInfoBase> {

  /**
   * Инициализирует модель дерева параметров по контексту приложения
   *
   * @param aContext {@link ITsGuiContext} - контекст.
   */
  void init( ITsGuiContext aContext );

  /**
   * Возвращает массив корневых объектов дерева.
   *
   * @return IStridablesList - массив корневых объектов ддерева.
   */
  IStridablesList<ISkClassInfo> getRootClasses();

  /**
   * Возвращает дочерние объекты родительского объекта.
   *
   * @param aParentClass ISkClassInfo - родительский объект.
   * @return IStridablesList - массив дочерних объектов.
   */
  IStridablesList<ISkClassInfo> getChildren( ISkClassInfo aParentClass );

  /**
   * Возвращает описание параметров класса, которые должны быть доступны в отчёте.
   *
   * @param aClass ISkClassInfo - класс.
   * @return IStridablesList - массив описаний данных.
   */
  IStridablesList<T> getParamsInfo( ISkClassInfo aClass );
}
