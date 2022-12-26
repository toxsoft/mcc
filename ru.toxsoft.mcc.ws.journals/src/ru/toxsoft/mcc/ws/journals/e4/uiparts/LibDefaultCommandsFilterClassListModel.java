package ru.toxsoft.mcc.ws.journals.e4.uiparts;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.uskat.core.api.sysdescr.*;
import org.toxsoft.uskat.core.api.sysdescr.dto.*;
import org.toxsoft.uskat.core.connection.*;

import ru.toxsoft.mcc.ws.journals.e4.uiparts.engine.*;

/**
 * Модель классов, используемых в фильтре команд, по умолчанию - список всех классов, у которых есть команды.
 *
 * @author max
 */
public class LibDefaultCommandsFilterClassListModel
    implements ILibClassInfoesTreeModel<IDtoCmdInfo> {

  private ITsGuiContext context;

  private IStridablesList<ISkClassInfo> classes;

  @Override
  public void init( ITsGuiContext aContext ) {
    context = aContext;
    classes = listNeededClasses();
  }

  @Override
  public IStridablesList<ISkClassInfo> getRootClasses() {
    return classes;
  }

  @Override
  public IStridablesList<ISkClassInfo> getChildren( ISkClassInfo aParentClass ) {
    return IStridablesList.EMPTY;
  }

  @Override
  public IStridablesList<IDtoCmdInfo> getParamsInfo( ISkClassInfo aClass ) {
    return aClass.cmds().list();
  }

  protected IStridablesList<ISkClassInfo> listNeededClasses() {
    IStridablesListEdit<ISkClassInfo> eventClasses = new StridablesList<>();

    for( ISkClassInfo classInfo : context.get( ISkConnection.class ).coreApi().sysdescr().listClasses() ) {
      if( classInfo.cmds().list().size() > 0
      // && s5conn().serverApi().objectService().list( classInfo.id(), false ).size() > 0
      ) {
        eventClasses.add( classInfo );
      }
    }
    return eventClasses;
  }
}
