package ru.toxsoft.mcc.ws.mnemos.app.controls;

import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.bricks.strid.more.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.uskat.base.gui.conn.*;
import org.toxsoft.uskat.core.api.objserv.*;
import org.toxsoft.uskat.core.api.sysdescr.*;
import org.toxsoft.uskat.core.api.sysdescr.dto.*;
import org.toxsoft.uskat.core.connection.*;

import ru.toxsoft.mcc.ws.mnemos.app.rt.*;

/**
 * Базовый класс для контролей диалогов, работающих с РВ-данными в проекте МосКокс.
 *
 * @author vs
 */
public abstract class AbstractMccControl
    implements ITsGuiContextable, IRtDataConsumer {

  private final ITsGuiContext tsContext;

  private final ISkObject skObject;

  private final String dataId;

  private final ISkConnection skConnection;

  private final IDtoRtdataInfo dataInfo;

  private String name = TsLibUtils.EMPTY_STRING;

  private String description = TsLibUtils.EMPTY_STRING;

  /**
   * Конструктор для наследников.<br>
   *
   * @param aSkObject ISkObject - серверный объект
   * @param aDataId String - ИД РВ-данного
   * @param aTsContext ITsGuiContext - соответствующий контекст
   * @param aConnId IdChain - ИД соединения
   */
  protected AbstractMccControl( ISkObject aSkObject, String aDataId, ITsGuiContext aTsContext, IdChain aConnId ) {
    skObject = aSkObject;
    dataId = aDataId;
    tsContext = aTsContext;
    if( aConnId == null ) {
      skConnection = tsContext.get( ISkConnectionSupplier.class ).defConn();
    }
    else {
      skConnection = tsContext.get( ISkConnectionSupplier.class ).getConn( aConnId );
    }
    dataInfo = dataInfo( dataId );
    description = dataInfo.description();
    name = dataInfo.nmName();
  }

  // ------------------------------------------------------------------------------------
  // IStridable
  //

  @Override
  public final String id() {
    return skObject.strid() + "." + dataId; //$NON-NLS-1$
  }

  @Override
  public final String description() {
    return description;
  }

  @Override
  public final String nmName() {
    return name;
  }

  // ------------------------------------------------------------------------------------
  // ITsGuiContextable
  //

  @Override
  public ITsGuiContext tsContext() {
    return tsContext;
  }

  // ------------------------------------------------------------------------------------
  // To implement
  //

  /**
   * Создает swt контроль.<br>
   *
   * @param aParent Composite - родительская компонента
   * @param aSwtStyle int - swt стиль контроля
   * @return Control - swt контроль
   */
  public abstract Control createControl( Composite aParent, int aSwtStyle );

  /**
   * Возвращает созданный контроль или <b>null</b>.<br>
   *
   * @return Control - созданный контроль или <b>null</b>
   */
  public abstract Control getControl();

  /**
   * Обновляет все внутренние структуры и данные.
   */
  public abstract void update();

  // ------------------------------------------------------------------------------------
  //
  //

  public ISkObject skObject() {
    return skObject;
  }

  public String dataId() {
    return dataId;
  }

  protected IDtoRtdataInfo dataInfo( String aDataId ) {
    ISkClassInfo clsInfo = skConnection.coreApi().sysdescr().getClassInfo( skObject.classId() );
    return clsInfo.rtdata().list().getByKey( aDataId );
  }

  /**
   * Возвращает строку форматирования для атомарного типа.<br>
   *
   * @param aType EAtomicType - атомарный тип
   * @return String - строка форматирования
   */
  public static String formatString( EAtomicType aType ) {
    switch( aType ) {
      case BOOLEAN:
        return "%b"; //$NON-NLS-1$
      case FLOATING:
        return "%.2f"; //$NON-NLS-1$
      case INTEGER:
        return "%d"; //$NON-NLS-1$
      case NONE:
      case STRING:
      case TIMESTAMP:
      case VALOBJ:
        return "%s"; //$NON-NLS-1$
      default:
        throw new TsNotAllEnumsUsedRtException();
    }
  }

}
