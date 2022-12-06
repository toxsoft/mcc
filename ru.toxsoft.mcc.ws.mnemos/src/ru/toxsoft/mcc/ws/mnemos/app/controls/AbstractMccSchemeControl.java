package ru.toxsoft.mcc.ws.mnemos.app.controls;

import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.graphics.cursors.*;
import org.toxsoft.core.tslib.bricks.strid.more.*;
import org.toxsoft.core.tslib.gw.gwid.*;
import org.toxsoft.core.tslib.gw.skid.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.uskat.base.gui.conn.*;
import org.toxsoft.uskat.core.api.objserv.*;
import org.toxsoft.uskat.core.connection.*;
import org.toxsoft.uskat.core.utils.*;

import ru.toxsoft.mcc.ws.mnemos.app.*;
import ru.toxsoft.mcc.ws.mnemos.app.rt.*;

/**
 * Базовый класс элементов, размещаемых на мнемосхеме, для проекта МосКокс.
 *
 * @author vs
 */
public abstract class AbstractMccSchemeControl
    implements ITsGuiContextable, ISkConnected, IInteractiveArea, I2dPlaceable, IRtDataConsumer {

  private final MccSchemePanel schemePanel;

  private final ITsGuiContext tsContext;

  private final ISkObject skObject;

  private final ISkConnection skConnection;

  private int x = 0;

  private int y = 0;

  private String tooltipText = null;

  /**
   * Конструктор для наследников, созданных на основе SWT контролей.
   *
   * @param aObjGwid Gwid - конкретный ИД объекта
   * @param aTsContext ITsGuiContext - соответствующий контекст
   * @param aConnId IdChain - ИД соединения
   */
  protected AbstractMccSchemeControl( Gwid aObjGwid, ITsGuiContext aTsContext, IdChain aConnId ) {
    TsNullArgumentRtException.checkNulls( aObjGwid, aTsContext );
    TsIllegalArgumentRtException.checkTrue( aObjGwid.isAbstract() );
    schemePanel = null;
    tsContext = aTsContext;
    if( aConnId == null ) {
      skConnection = tsContext.get( ISkConnectionSupplier.class ).defConn();
    }
    else {
      skConnection = tsContext.get( ISkConnectionSupplier.class ).getConn( aConnId );
    }
    skObject = coreApi().objService().get( new Skid( aObjGwid.classId(), aObjGwid.strid() ) );
  }

  /**
   * Конструктор для наследников, которые рисуют себя сами.
   *
   * @param aOwner MccSchemePanel - панель мнемосхемы, содержащая данный контроль
   * @param aObjGwid Gwid - конкретный ИД объекта
   * @param aTsContext ITsGuiContext - соответствующий контекст
   * @param aConnId IdChain - ИД соединения
   */
  protected AbstractMccSchemeControl( MccSchemePanel aOwner, Gwid aObjGwid, ITsGuiContext aTsContext,
      IdChain aConnId ) {
    TsNullArgumentRtException.checkNulls( aOwner, aObjGwid, aTsContext );
    TsIllegalArgumentRtException.checkTrue( aObjGwid.isAbstract() );
    schemePanel = aOwner;
    tsContext = aTsContext;
    if( aConnId == null ) {
      skConnection = tsContext.get( ISkConnectionSupplier.class ).defConn();
    }
    else {
      skConnection = tsContext.get( ISkConnectionSupplier.class ).getConn( aConnId );
    }
    skObject = coreApi().objService().get( new Skid( aObjGwid.classId(), aObjGwid.strid() ) );
  }

  // ------------------------------------------------------------------------------------
  // To implement
  //

  /**
   * Отрисовывает элемент с использованием переданного контекста.
   *
   * @param aGc GC - графический контекст
   */
  public abstract void paint( GC aGc );

  /**
   * Возвращает описывающий прямоугоьник.
   *
   * @return Rectangle - описывающий прямоугоьник
   */
  public abstract Rectangle bounds();

  /**
   * Освобождает системные ресурсы
   */
  public abstract void dispose();

  /**
   * Вызывает диалог настроек.
   */
  public abstract void showSettingDialog();

  // ------------------------------------------------------------------------------------
  // IStridable
  //

  @Override
  public String id() {
    return skObject.id();
  }

  @Override
  public String description() {
    return skObject.description();
  }

  @Override
  public String nmName() {
    return skObject.nmName();
  }

  // ------------------------------------------------------------------------------------
  // IInteractiveArea
  //

  @Override
  public boolean contains( int aX, int aY ) {
    return bounds().contains( aX, aY );
  }

  @Override
  public ECursorType cursorType() {
    return ECursorType.HAND;
  }

  @Override
  public String tooltipText() {
    if( tooltipText == null ) {
      return nmName();
    }
    return nmName() + "/" + tooltipText; //$NON-NLS-1$
  }

  // ------------------------------------------------------------------------------------
  // I2dPlaceable
  //

  @Override
  public int x() {
    return x;
  }

  @Override
  public int y() {
    return y;
  }

  @Override
  public void setLocation( int aX, int aY ) {
    if( x != aX || y != aY ) {
      x = aX;
      y = aY;
      onLocationChanged();
    }
  }

  // ------------------------------------------------------------------------------------
  // ITsGuiContextable
  //

  @Override
  public ITsGuiContext tsContext() {
    return tsContext;
  }

  // ------------------------------------------------------------------------------------
  // ISkConnected
  //

  @Override
  public ISkConnection skConn() {
    return skConnection;
  }

  // ------------------------------------------------------------------------------------
  // To use
  //

  ISkObject skObject() {
    return skObject;
  }

  MccSchemePanel schemePanel() {
    return schemePanel;
  }

  void setTooltipText( String aText ) {
    tooltipText = aText;
  }

  // ------------------------------------------------------------------------------------
  // Possible to override
  //

  protected void onLocationChanged() {
    // nop
  }

  public boolean hasSettingDialog() {
    return true;
  }

}
