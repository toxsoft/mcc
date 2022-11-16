package ru.toxsoft.mcc.ws.mnemos.app.controls;

import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.graphics.cursors.*;
import org.toxsoft.core.tslib.gw.gwid.*;
import org.toxsoft.core.tslib.gw.skid.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.uskat.base.gui.conn.*;
import org.toxsoft.uskat.core.*;
import org.toxsoft.uskat.core.api.objserv.*;

import ru.toxsoft.mcc.ws.mnemos.app.*;

/**
 * Базовый класс элементов, размещаемых на мнемосхеме, для проекта МосКокс.
 *
 * @author vs
 */
public abstract class AbstractMccSchemeControl
    implements ITsGuiContextable, IInteractiveArea, I2dPlaceable {

  private final MccSchemePanel schemePanel;

  private final ITsGuiContext tsContext;

  private final ISkCoreApi coreApi;

  private final ISkObject skObject;

  private int x = 0;

  private int y = 0;

  /**
   * Конструктор для наследников.
   *
   * @param aObjGwid Gwid - конкретный ИД объекта
   * @param aTsContext ITsGuiContext - соответствующий контекст
   */
  protected AbstractMccSchemeControl( MccSchemePanel aOwner, Gwid aObjGwid, ITsGuiContext aTsContext ) {
    TsNullArgumentRtException.checkNulls( aOwner, aObjGwid, aTsContext );
    TsIllegalArgumentRtException.checkTrue( aObjGwid.isAbstract() );
    schemePanel = aOwner;
    tsContext = aTsContext;
    coreApi = tsContext.get( ISkConnectionSupplier.class ).defConn().coreApi();
    skObject = coreApi.objService().get( new Skid( aObjGwid.classId(), aObjGwid.strid() ) );
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
  // To use
  //

  ISkObject skObject() {
    return skObject;
  }

  MccSchemePanel schemePanel() {
    return schemePanel;
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
