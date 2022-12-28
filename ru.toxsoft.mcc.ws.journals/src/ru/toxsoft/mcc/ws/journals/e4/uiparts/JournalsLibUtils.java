package ru.toxsoft.mcc.ws.journals.e4.uiparts;

import static ru.toxsoft.mcc.ws.journals.e4.uiparts.IMmFgdpLibCfgJournalsConstants.*;

import java.lang.reflect.*;
import java.util.*;

import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.utils.errors.*;

import ru.toxsoft.mcc.ws.journals.e4.uiparts.engine.*;

/**
 * Класс вспомогательных утилит работы с журналами.
 *
 * @author max
 */
public class JournalsLibUtils {

  private static final String ERR_MSG_CANT_LOAD_MODEL = "Cant load: %s"; //$NON-NLS-1$

  private static final String ERR_MSG_CANT_FIND_MODEL_IN_CONTEXT = "Cant find %s in context"; //$NON-NLS-1$

  private JournalsLibUtils() {

  }

  public static long getTimeInMiles( DateTime aControl ) {
    Calendar cal = Calendar.getInstance();
    cal.set( Calendar.YEAR, aControl.getYear() );
    cal.set( Calendar.MONTH, aControl.getMonth() );
    cal.set( Calendar.DAY_OF_MONTH, aControl.getDay() );
    cal.set( Calendar.HOUR_OF_DAY, aControl.getHours() );
    cal.set( Calendar.MINUTE, aControl.getMinutes() );
    cal.set( Calendar.SECOND, aControl.getSeconds() );
    return cal.getTimeInMillis();
  }

  public static long getTimeInMiles( DateTime aTimeControl, DateTime aDateControl ) {
    Calendar cal = Calendar.getInstance();
    cal.set( Calendar.YEAR, aDateControl.getYear() );
    cal.set( Calendar.MONTH, aDateControl.getMonth() );
    cal.set( Calendar.DAY_OF_MONTH, aDateControl.getDay() );
    cal.set( Calendar.HOUR_OF_DAY, aTimeControl.getHours() );
    cal.set( Calendar.MINUTE, aTimeControl.getMinutes() );
    cal.set( Calendar.SECOND, aTimeControl.getSeconds() );
    return cal.getTimeInMillis();
  }

  /**
   * Загружает в указанный контекст модель объектов для редактора фильтра.
   *
   * @param aContext ITsGuiContext - контекст, в который загружается модель объектов.
   * @param aModelId IAtomicOptionInfo - идентификатор модели.
   * @throws TsException - ошибка в случае невозможности или неудачной загрузки модели.
   */
  public static void loadObjectsTreeModel( ITsGuiContext aContext, IDataDef aModelId )
      throws TsException {

    if( aContext.hasKey( FILTER_OBJECTS_TREE_MODEL_LIB ) ) {
      return;
    }

    // ILibSkObjectsTreeModel objectsTreeModel;
    // if( aContext.hasKey( FILTER_CLASSES_TREE_MODEL_LIB ) ) {
    // ILibClassInfoesTreeModel classListModel = (ILibClassInfoesTreeModel)aContext.get( FILTER_CLASSES_TREE_MODEL_LIB
    // );
    // objectsTreeModel = new LibFilterPlaneObjectsTreeModel( classListModel );
    // }
    // else {
    // throw new TsItemNotFoundRtException( ERR_MSG_CANT_FIND_MODEL_IN_CONTEXT, FILTER_CLASSES_TREE_MODEL_LIB );
    // }

    // ITsModularWorkstationService mwService = aContext.get( ITsModularWorkstationService.class );
    IAtomicValue objectsModelJavaClassStr = aModelId.getValue( aContext.params() );

    ILibSkObjectsTreeModel objectsTreeModel;

    try {
      Class<?> objectsTreeModelJavaClass = Class.forName( objectsModelJavaClassStr.asString() );
      try {
        Constructor<?> constructor = objectsTreeModelJavaClass.getConstructor( ILibClassInfoesTreeModel.class );

        if( aContext.hasKey( FILTER_CLASSES_TREE_MODEL_LIB ) ) {
          ILibClassInfoesTreeModel classListModel =
              (ILibClassInfoesTreeModel)aContext.get( FILTER_CLASSES_TREE_MODEL_LIB );
          objectsTreeModel = (ILibSkObjectsTreeModel)constructor.newInstance( classListModel );
        }
        else {
          throw new TsItemNotFoundRtException( ERR_MSG_CANT_FIND_MODEL_IN_CONTEXT, FILTER_CLASSES_TREE_MODEL_LIB );
        }

      }
      catch( NoSuchMethodException e ) {
        objectsTreeModel = (ILibSkObjectsTreeModel)objectsTreeModelJavaClass.newInstance();
      }

      // TODO - нужен ли инит не понятно пока???
      // objectsTreeModel.init( aContext );
    }
    catch( Exception ex ) {
      ex.printStackTrace();
      throw new TsException( ex, ERR_MSG_CANT_LOAD_MODEL, aModelId.description() );
    }

    aContext.put( FILTER_OBJECTS_TREE_MODEL_LIB, objectsTreeModel );
  }

  /**
   * Загружает в указанный контекст модель классов для редактора фильтра.
   *
   * @param aContext ITsGuiContext - контекст, в который загружается модель классов.
   * @param aModelId IAtomicOptionInfo - идентификатор модели.
   * @throws TsException - ошибка в случае невозможности или неудачной загрузки модели.
   */
  public static void loadClassesTreeModel( ITsGuiContext aContext, IDataDef aModelId )
      throws TsException {

    if( aContext.hasKey( FILTER_CLASSES_TREE_MODEL_LIB ) ) {
      return;
    }

    // ITsModularWorkstationService mwService = aContext.get( ITsModularWorkstationService.class );

    // ILibClassInfoesTreeModel classListModel = new LibDefaultEventsFilterClassListModel();
    // classListModel.init( aContext );

    IAtomicValue classListModelJavaClassStr = aModelId.getValue( aContext.params() );
    ILibClassInfoesTreeModel classListModel;
    try {
      Class<?> classListModelJavaClass = Class.forName( classListModelJavaClassStr.asString() );
      classListModel = (ILibClassInfoesTreeModel)classListModelJavaClass.newInstance();
      classListModel.init( aContext );
    }
    catch( InstantiationException | IllegalAccessException | ClassNotFoundException ex ) {
      ex.printStackTrace();
      throw new TsException( ex, ERR_MSG_CANT_LOAD_MODEL, aModelId.description() );
    }

    aContext.put( FILTER_CLASSES_TREE_MODEL_LIB, classListModel );
  }
}
