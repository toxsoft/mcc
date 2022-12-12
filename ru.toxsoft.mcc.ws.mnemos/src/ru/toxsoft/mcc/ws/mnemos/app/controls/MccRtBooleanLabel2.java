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
 * Текст с картинкой для отображения логического значения, формируемого из значения 2-х сигналов (для прокта МосКокс).
 * <p>
 *
 * @author vs
 */
public class MccRtBooleanLabel2
    implements IRtDataConsumer, ITsGuiContextable {

  private final ITsGuiContext tsContext;

  private final Gwid dataGwid1;

  private final Gwid dataGwid2;

  private CLabel label;

  private String description = TsLibUtils.EMPTY_STRING;

  private String name = TsLibUtils.EMPTY_STRING;

  private final Image falseImage;

  private final Image trueImage;

  private final Image warnImage;

  private final Color grayColor;

  private final Color redColor;

  IAtomicValue valueForTrue = IAtomicValue.NULL;

  IAtomicValue valueForFalse = IAtomicValue.NULL;

  /**
   * Конструктор.
   *
   * @param aTrueGwid Gwid - ИД данного для значения <b>true</b> ( если <b>true</b> то <b>true</b> )
   * @param aFalseGwid Gwid - ИД данного для значения <b>false</b> ( если <b>true</b> то <b>false</b> )
   * @param aFalseIconId String - ИД миниатюры для значения <b>false</b>
   * @param aTrueIconId String - ИД миниатюры для значения <b>true</b>
   * @param aIconSize EIconSize - размер миниатюры
   * @param aTsContext ITsGuiContext - соответствующий контекст
   */
  public MccRtBooleanLabel2( Gwid aTrueGwid, Gwid aFalseGwid, String aFalseIconId, String aTrueIconId,
      EIconSize aIconSize, ITsGuiContext aTsContext ) {
    dataGwid1 = aTrueGwid;
    dataGwid2 = aFalseGwid;
    tsContext = aTsContext;
    falseImage = iconManager().loadStdIcon( aFalseIconId, aIconSize );
    trueImage = iconManager().loadStdIcon( aTrueIconId, aIconSize );
    grayColor = colorManager().getColor( ETsColor.DARK_GRAY );
    redColor = colorManager().getColor( ETsColor.RED );
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
      if( !isValid() ) {
        aE.gc.setAlpha( 160 );
        aE.gc.setBackground( redColor );
        Point size = label.getSize();
        aE.gc.fillRectangle( 0, 0, size.x, size.y );
        aE.gc.setAlpha( 255 );
        aE.gc.drawImage( warnImage, 0, size.y - 16 );
        label.setToolTipText( STR_ERR_VALUES_COMBINATION + ": " + dataGwid1.propId() + " и " + dataGwid2.propId() ); //$NON-NLS-1$ //$NON-NLS-2$
      }
      else {
        if( !valueForTrue.isAssigned() || !valueForFalse.isAssigned() ) {
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
    return dataGwid1.strid() + "." + dataGwid1.propId() + "." + dataGwid2.propId(); //$NON-NLS-1$ //$NON-NLS-2$
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
    GwidList gl = new GwidList( dataGwid1, dataGwid2 );
    return gl;
  }

  @Override
  public void setValues( Gwid[] aGwids, IAtomicValue[] aValues, int aCount ) {
    for( int i = 0; i < aCount; i++ ) {
      if( aGwids[i].equals( dataGwid1 ) ) {
        valueForTrue = aValues[i];
      }
      if( aGwids[i].equals( dataGwid2 ) ) {
        valueForFalse = aValues[i];
      }
    }
    update();
  }

  // ------------------------------------------------------------------------------------
  // Implementation
  //

  void update() {
    if( valueForTrue.isAssigned() && valueForTrue.asBool() ) {
      label.setImage( trueImage );
    }
    else {
      label.setImage( falseImage );
    }
    label.redraw();
  }

  boolean isValid() {
    if( valueForTrue.isAssigned() && valueForFalse.isAssigned() ) {
      if( valueForTrue.equals( valueForFalse ) ) {
        return false;
      }
    }
    return true;
  }

}
