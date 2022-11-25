package ru.toxsoft.mcc.ws.mnemos.app.controls;

import static ru.toxsoft.mcc.ws.mnemos.app.controls.IVjResources.*;

import org.eclipse.swt.custom.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.graphics.colors.*;
import org.toxsoft.core.tsgui.graphics.icons.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.gw.gwid.*;
import org.toxsoft.core.tslib.utils.*;

import ru.toxsoft.mcc.ws.mnemos.app.rt.*;

/**
 * Текст с картинкой для отображения логического значения (для прокта МосКокс).
 * <p>
 *
 * @author vs
 */
public class MccRtBooleanLabel
    implements IRtDataConsumer, ITsGuiContextable {

  private final ITsGuiContext tsContext;

  private final Gwid dataGwid;

  private CLabel label;

  private String description = TsLibUtils.EMPTY_STRING;

  private String name = TsLibUtils.EMPTY_STRING;

  private final Image falseImage;

  private final Image trueImage;

  private final Image warnImage;

  private final Color grayColor;

  IAtomicValue value = IAtomicValue.NULL;

  /**
   * Конструктор.
   *
   * @param aDataGwid Gwid - ИД данного
   * @param aFalseIconId String - ИД миниатюры для значения <b>false</b>
   * @param aTrueIconId String - ИД миниатюры для значения <b>true</b>
   * @param aIconSize EIconSize - размер миниатюры
   * @param aTsContext ITsGuiContext - соответствующий контекст
   */
  public MccRtBooleanLabel( Gwid aDataGwid, String aFalseIconId, String aTrueIconId, EIconSize aIconSize,
      ITsGuiContext aTsContext ) {
    dataGwid = aDataGwid;
    tsContext = aTsContext;
    falseImage = iconManager().loadStdIcon( aFalseIconId, aIconSize );
    trueImage = iconManager().loadStdIcon( aTrueIconId, aIconSize );
    grayColor = colorManager().getColor( ETsColor.DARK_GRAY );
    warnImage = iconManager().loadStdIcon( ITsStdIconIds.ICONID_DIALOG_WARNING, EIconSize.IS_16X16 );
  }

  /**
   * Создает контроль в понятиях SWT.
   *
   * @param aParent Composite - родительская компонента
   * @param aSwtStyle int - стиль SWT копоненты
   * @return CLabel - контроль в понятиях SWT
   */
  public CLabel createControl( Composite aParent, int aSwtStyle ) {
    label = new CLabel( aParent, aSwtStyle );
    label.setText( name );
    label.addPaintListener( aE -> {
      if( !value.isAssigned() ) {
        aE.gc.setAlpha( 160 );
        aE.gc.setBackground( grayColor );
        Point size = label.getSize();
        aE.gc.fillRectangle( 0, 0, size.x, size.y );
        aE.gc.setAlpha( 255 );
        aE.gc.drawImage( warnImage, 0, size.y - 16 );
        label.setToolTipText( STR_ERR_VALUE_NOT_SET );
      }
      else {
        label.setToolTipText( description );
      }
    } );
    update();
    return label;
  }

  /**
   * Возвращает созданный контроль или null если он еще не был создан.
   *
   * @return Button - созданный контроль или null
   */
  public CLabel getControl() {
    return label;
  }

  /**
   * Задает текст, который будет отображаться на контроле.
   *
   * @param aName String - текст, который будет отображаться на контроле
   */
  public void setName( String aName ) {
    name = aName;
    if( label != null ) {
      label.setText( name );
    }
  }

  /**
   * Задает текст всплывающей подсказки.
   *
   * @param aDescription String - текст всплывающей подсказки
   */
  public void setDescription( String aDescription ) {
    description = aDescription;
    if( label != null ) {
      label.setToolTipText( aDescription );
    }
  }

  // ------------------------------------------------------------------------------------
  // IStridable
  //

  @Override
  public String id() {
    return dataGwid.strid() + "." + dataGwid.propId(); //$NON-NLS-1$
  }

  @Override
  public String nmName() {
    return name;
  }

  @Override
  public String description() {
    return description;
  }

  // ------------------------------------------------------------------------------------
  // ITsGuiContextable
  //

  @Override
  public ITsGuiContext tsContext() {
    return tsContext;
  }

  // ------------------------------------------------------------------------------------
  // IRtDataConsumer
  //

  @Override
  public IGwidList listNeededGwids() {
    GwidList gl = new GwidList( dataGwid );
    return gl;
  }

  @Override
  public void setValues( Gwid[] aGwids, IAtomicValue[] aValues, int aCount ) {
    value = aValues[0];
    update();
  }

  // ------------------------------------------------------------------------------------
  // Implementation
  //

  void update() {
    if( value.isAssigned() && value.asBool() ) {
      label.setImage( trueImage );
    }
    else {
      label.setImage( falseImage );
    }
    label.redraw();
  }
}
