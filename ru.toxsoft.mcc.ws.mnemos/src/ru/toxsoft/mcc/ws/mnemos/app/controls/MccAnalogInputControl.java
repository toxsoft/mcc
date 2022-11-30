package ru.toxsoft.mcc.ws.mnemos.app.controls;

import org.eclipse.swt.custom.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.graphics.colors.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.bricks.strid.more.*;
import org.toxsoft.core.tslib.gw.gwid.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.uskat.base.gui.conn.*;
import org.toxsoft.uskat.core.api.objserv.*;
import org.toxsoft.uskat.core.connection.*;
import org.toxsoft.uskat.core.utils.*;

import ru.toxsoft.mcc.ws.mnemos.app.dialogs.*;
import ru.toxsoft.mcc.ws.mnemos.app.rt.*;

/**
 * Контроль для отображения значения аналогового входа на мнемосхеме и в диалогах.
 * <p>
 *
 * @author vs
 */
public class MccAnalogInputControl
    implements IRtDataConsumer, ITsGuiContextable, ISkConnected {

  /**
   * ИД класса - аналоговый вход
   */
  public static String CLS_ANALOG_INPUT = "mcc.AnalogInput"; //$NON-NLS-1$

  /**
   * ИД РВ-данного - текущее значение
   */
  public static String DI_CURRENT_VALUE = "rtdCurrentValue"; //$NON-NLS-1$

  /**
   * ИД РВ-данного - состояние
   */
  public static String DI_STATE = "rtdParamState"; //$NON-NLS-1$

  private IAtomicValue value = IAtomicValue.NULL;

  CLabel label = null;

  private final ITsGuiContext tsContext;

  private final ISkConnection skConnection;

  private final ISkObject skObject;

  private static RGB rgbGreen  = new RGB( 102, 255, 102 );
  private static RGB rgbBlue   = new RGB( 0, 153, 255 );
  private static RGB rgbGray   = new RGB( 178, 178, 178 );
  private static RGB rgbViolet = new RGB( 153, 51, 255 );
  private static RGB rgbYellow = new RGB( 255, 255, 0 );
  private static RGB rgbRed    = new RGB( 255, 51, 51 );

  private static RGB[] stateRgbs = { //
      new RGB( 102, 255, 102 ), // зеленый - норма
      // new RGB( 170, 249, 202 ), // зеленый - норма
      new RGB( 107, 195, 255 ), //
      new RGB( 198, 172, 125 ), //
      new RGB( 160, 160, 160 ), // заблокировано
      // new RGB( 255, 255, 255 ), // заблокировано
      new RGB( 85, 229, 230 ), //
      new RGB( 227, 179, 253 ), //
      new RGB( 239, 235, 138 ), //
      new RGB( 255, 142, 179 ) //
  };

  private static Color[] bkColors = new Color[8];

  private static Color colorWhite;
  private static Color colorMagenta;

  /**
   * Конструктор.
   *
   * @param aObjGwid Gwid - конкретны ИД объекта
   * @param aTsContext ITsGuiContext - соответствующий контекст
   * @param aConnectionId IdChain - ИД соединения
   */
  public MccAnalogInputControl( Gwid aObjGwid, ITsGuiContext aTsContext, IdChain aConnectionId ) {
    TsNullArgumentRtException.checkNulls( aObjGwid, aTsContext );
    TsIllegalArgumentRtException.checkTrue( aObjGwid.isAbstract() );
    tsContext = aTsContext;
    if( aConnectionId == null ) {
      skConnection = tsContext.get( ISkConnectionSupplier.class ).defConn();
    }
    else {
      skConnection = tsContext.get( ISkConnectionSupplier.class ).getConn( aConnectionId );
    }
    skObject = skObjServ().get( aObjGwid.skid() );

    colorWhite = colorManager().getColor( ETsColor.WHITE );
    colorMagenta = colorManager().getColor( ETsColor.MAGENTA );

    for( int i = 0; i < bkColors.length; i++ ) {
      bkColors[i] = colorManager().getColor( stateRgbs[i] );
    }

    // bkColors[0] = colorManager().getColor( rgbGreen );
    // bkColors[1] = colorManager().getColor( rgbBlue );
    // bkColors[2] = colorManager().getColor( rgbGray );
    // bkColors[3] = colorManager().getColor( rgbViolet );
    // bkColors[4] = colorManager().getColor( rgbViolet );
    // // bkColors[5] = colorManager().getColor( rgbViolet );
    // bkColors[5] = colorMagenta;
    // bkColors[6] = colorManager().getColor( rgbYellow );
    // bkColors[7] = colorManager().getColor( rgbRed );
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Создает SWT контроль.<br>
   *
   * @param aParent Composite - родительская компонента
   * @param aSwtStyle int - SWT стиль контроля
   * @return Control - SWT контроль
   */
  public Control createControl( Composite aParent, int aSwtStyle ) {
    label = new CLabel( aParent, aSwtStyle );
    return label;
  }

  /**
   * Вызывает диалог с настроками аналогового сигнала.<br>
   */
  public void showSettingDialog() {
    PanelAnalogInput.showDialog( new MccDialogContext( tsContext, skObject ) );
  }

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
  // ITsContextable
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
  // IRtDataConsumer
  //

  @Override
  public IGwidList listNeededGwids() {
    GwidList gList = new GwidList();
    gList.add( Gwid.createRtdata( CLS_ANALOG_INPUT, skObject.strid(), DI_CURRENT_VALUE ) );
    gList.add( Gwid.createRtdata( CLS_ANALOG_INPUT, skObject.strid(), DI_STATE ) );
    return gList;
  }

  @Override
  public void setValues( Gwid[] aGwids, IAtomicValue[] aValues, int aCount ) {
    for( int i = 0; i < aCount; i++ ) {
      Gwid gwid = aGwids[i];
      if( gwid.propId().equals( DI_CURRENT_VALUE ) ) {
        value = aValues[i];
      }
      if( gwid.propId().equals( DI_STATE ) ) {
        if( aValues[i] != null && aValues[i].isAssigned() ) {
          int state = aValues[i].asInt();
          if( state >= 0 && state < 7 ) {
            label.setBackground( bkColors[state] );
          }
          else {
            label.setBackground( colorMagenta );
          }
        }
        else {
          label.setBackground( colorMagenta );
        }
      }
    }
    label.setText( AvUtils.printAv( formatString( value.atomicType() ), value ) );
    label.redraw();
  }

  // ------------------------------------------------------------------------------------
  // Implementation
  //

  String formatString( EAtomicType aType ) {
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
