package ru.toxsoft.mcc.ws.journals.e4.uiparts;

import static org.toxsoft.core.tslib.av.EAtomicType.*;
import static org.toxsoft.core.tslib.av.impl.DataDef.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.av.metainfo.*;

import ru.toxsoft.mcc.ws.journals.e4.uiparts.engine.*;

/**
 * Конфигурационные параметры
 *
 * @author max
 */
@SuppressWarnings( "nls" )
public interface IMmFgdpLibCfgJournalsConstants {

  /**
   * Идентификатор модели дерева классов (параметров) в диалоге редактора фильтра запроса.
   */
  String FILTER_CLASSES_TREE_MODEL_LIB = "filter.classes.tree.model.lib";

  /**
   * Идентификатор модели дерева объектов в диалоге редактора фильтра запроса.
   */
  String FILTER_OBJECTS_TREE_MODEL_LIB = "filter.objects.tree.model.lib";

  /**
   * Наименование java класса модели классов и событий, используемой в диалоге редактирования фильтра событий. В диалоге
   * используются только корневые классы (список). Модель должна реализовывать интерфейс
   * {@link ILibClassInfoesTreeModel}.
   */

  IDataDef ADMIN_EVENTS_FILTER_CLASSES_TREE_MODEL_LIB =
      create( "admin.events.filter.classes.tree.model.class.lib", STRING, TSID_NAME, "AdminEventsFilterClassesModel",
          TSID_DESCRIPTION, "Admin events filter classes tree model", TSID_DEFAULT_VALUE,
          AvUtils.avStr( LibAdminEventsFilterClassListModel.class.getName() ), TSID_IS_MANDATORY, AvUtils.AV_FALSE );

  /**
   * Наименование java класса модели классов и событий, используемой в диалоге редактирования фильтра событий. В диалоге
   * используются только корневые классы (список). Модель должна реализовывать интерфейс
   * {@link ILibClassInfoesTreeModel}.
   */
  IDataDef EVENTS_FILTER_CLASSES_TREE_MODEL_LIB = create( "events.filter.classes.tree.model.class.lib", STRING,
      TSID_DESCRIPTION, "Events filter classes tree model", TSID_NAME, "EventsFilterClassesModel", TSID_DEFAULT_VALUE,
      AvUtils.avStr( LibDefaultEventsFilterClassListModel.class.getName() ), TSID_IS_MANDATORY, AvUtils.AV_FALSE );

  /**
   * Наименование java класса древовидной модели объектов, используемой в диалоге редактирования фильтра событий. Модель
   * должна реализовывать интерфейс {@link ILibSkObjectsTreeModel}. Модель может иметь конструктор с параметром
   * {@link ILibClassInfoesTreeModel}.
   */
  IDataDef EVENTS_FILTER_OBJECTS_TREE_MODEL_LIB = create( "events.filter.objects.tree.model.class.lib", STRING,
      TSID_DESCRIPTION, "Events filter objects tree model", TSID_NAME, "EventsFilterObjectsModel", TSID_DEFAULT_VALUE,
      AvUtils.avStr( LibFilterPlaneObjectsTreeModel.class.getName() ), TSID_IS_MANDATORY, AvUtils.AV_FALSE );

  /**
   * Наименование java класса модели классов и команд, используемой в диалоге редактирования фильтра команд. В диалоге
   * используются только корневые классы (список). Модель должна реализовывать интерфейс
   * {@link ILibClassInfoesTreeModel}.
   */
  IDataDef COMMANDS_FILTER_CLASSES_TREE_MODEL_LIB = create( "commands.filter.classes.tree.model.class.lib", STRING,
      TSID_DESCRIPTION, "Commands filter classes tree model", TSID_NAME, "CmdsFilterClassesModel", TSID_DEFAULT_VALUE,
      AvUtils.avStr( LibDefaultCommandsFilterClassListModel.class.getName() ), TSID_IS_MANDATORY, AvUtils.AV_FALSE );

  /**
   * Наименование java класса древовидной модели объектов, используемой в диалоге редактирования фильтра команд. Модель
   * должна реализовывать интерфейс {@link ILibSkObjectsTreeModel}. Модель может иметь конструктор с параметром
   * {@link ILibClassInfoesTreeModel}.
   */
  IDataDef COMMANDS_FILTER_OBJECTS_TREE_MODEL_LIB = create( "commands.filter.objects.tree.model.class.lib", STRING,
      TSID_DESCRIPTION, "Commands filter objects tree model", TSID_NAME, "CmdsFilterObjectsModel", TSID_DEFAULT_VALUE,
      AvUtils.avStr( LibFilterPlaneObjectsTreeModel.class.getName() ), TSID_IS_MANDATORY, AvUtils.AV_FALSE );
}
