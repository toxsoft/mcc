package ru.toxsoft.mcc.ws.journals.e4.uiparts;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.uskat.base.gui.conn.*;
import org.toxsoft.uskat.core.*;
import org.toxsoft.uskat.core.api.objserv.*;
import org.toxsoft.uskat.core.api.sysdescr.*;

import ru.toxsoft.mcc.ws.journals.e4.uiparts.engine.*;

/**
 * Плоское дерево (список объектов), используемое в диалоге редактирования фильтров, - все объекты, перечисленных в
 * модели классов корневых классов.
 *
 * @author max
 */
public class LibFilterPlaneObjectsTreeModel
    implements ILibSkObjectsTreeModel {

  protected ISkCoreApi coreApi;

  private IListEdit<ISkObject> objects = new ElemArrayList<>();

  private IList<ISkClassInfo> availableClasses;

  /**
   * Конструктор по модели классов - используется список корневых классов - загружаются все объекты этих классов.
   *
   * @param aClassesInfoTreeModel IClassesInfoTreeModel - модель классов.
   */
  public LibFilterPlaneObjectsTreeModel( ILibClassInfoesTreeModel aClassesInfoTreeModel ) {
    super();
    availableClasses = aClassesInfoTreeModel.getRootClasses();
  }

  @Override
  public void init( ITsGuiContext aContext ) {

    coreApi = aContext.get( ISkConnectionSupplier.class ).defConn().coreApi();
    objects.clear();
    // for( ISkClassInfo aClsInfo : availableClasses ) {
    // ISkObjectService objService = coreApi.objService();
    // objects.addAll( objService.listObjs( aClsInfo.id(), true ) );
    // }
  }

  @Override
  public IList<ISkObject> getRootObjects() {
    return objects;
  }

  @Override
  public IList<ISkObject> getChildren( ISkObject aObject ) {
    return IList.EMPTY;
  }
}
