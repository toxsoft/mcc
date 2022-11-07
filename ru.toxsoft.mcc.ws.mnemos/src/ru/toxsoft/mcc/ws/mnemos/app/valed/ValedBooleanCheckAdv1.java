package ru.toxsoft.mcc.ws.mnemos.app.valed;

import static org.toxsoft.core.tsgui.valed.api.IValedControlConstants.*;
import static org.toxsoft.core.tslib.av.EAtomicType.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;
import static ru.toxsoft.mcc.ws.mnemos.app.valed.IVjResources.*;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.graphics.colors.*;
import org.toxsoft.core.tsgui.graphics.fonts.*;
import org.toxsoft.core.tsgui.graphics.fonts.impl.*;
import org.toxsoft.core.tsgui.graphics.icons.*;
import org.toxsoft.core.tsgui.utils.layout.*;
import org.toxsoft.core.tsgui.valed.api.*;
import org.toxsoft.core.tsgui.valed.impl.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * "Продвинутый" редактор булевого значения.
 * <p>
 * Позволяет задавать текст и и изображения для <b>true</b> и <b>false</b>.
 *
 * @author vs
 */
public class ValedBooleanCheckAdv1
    extends AbstractValedControl<Boolean, Canvas> {

  /**
   * ID of context reference {@link #OPDEF_TRUE_ICON_ID}.
   */
  public static final String OPID_TRUE_ICON_ID = VALED_OPID_PREFIX + ".TrueIconId"; //$NON-NLS-1$

  /**
   * ID of context reference {@link #OPDEF_FALSE_ICON_ID}.
   */
  public static final String OPID_FALSE_ICON_ID = VALED_OPID_PREFIX + ".FalseIconId"; //$NON-NLS-1$

  /**
   * ID of context reference {@link #OPDEF_TRUE_TEXT}.
   */
  public static final String OPID_TRUE_TEXT = VALED_OPID_PREFIX + ".TrueText"; //$NON-NLS-1$

  /**
   * ID of context reference {@link #OPDEF_FALSE_TEXT}.
   */
  public static final String OPID_FALSE_TEXT = VALED_OPID_PREFIX + ".FalseText"; //$NON-NLS-1$

  /**
   * ID of context reference {@link #OPDEF_TRUE_TEXT}.
   */
  public static final String OPID_TRUE_FONT = VALED_OPID_PREFIX + ".TrueFont"; //$NON-NLS-1$

  /**
   * ID of context reference {@link #OPDEF_FALSE_TEXT}.
   */
  public static final String OPID_FALSE_FONT = VALED_OPID_PREFIX + ".FalseFont"; //$NON-NLS-1$

  /**
   * ID of context reference {@link #OPDEF_ICON_GAP}.
   */
  public static final String OPID_ICON_GAP = VALED_OPID_PREFIX + ".IconGap"; //$NON-NLS-1$

  /**
   * ID of context reference {@link #OPDEF_ICON_ON_LEFT}.
   */
  public static final String OPID_ICON_ON_LEFT = VALED_OPID_PREFIX + ".IconOnLeft"; //$NON-NLS-1$

  /**
   * ID of context reference {@link #OPDEF_ICON_ON_LEFT}.
   */
  public static final String OPID_ICON_SIZE = VALED_OPID_PREFIX + ".IconSize"; //$NON-NLS-1$

  /**
   * The icon to be shown on widget for <b>true</b>.<br>
   */
  public static final IDataDef OPDEF_TRUE_ICON_ID = DataDef.create( OPID_TRUE_ICON_ID, STRING, //
      TSID_NAME, STR_N_TRUE_ICON_ID, //
      TSID_DESCRIPTION, STR_D_TRUE_ICON_ID, //
      TSID_DEFAULT_VALUE, AV_STR_EMPTY //
  );

  /**
   * The icon to be shown on widget for <b>false</b>.<br>
   */
  public static final IDataDef OPDEF_FALSE_ICON_ID = DataDef.create( OPID_FALSE_ICON_ID, STRING, //
      TSID_NAME, STR_N_FALSE_ICON_ID, //
      TSID_DESCRIPTION, STR_D_FALSE_ICON_ID, //
      TSID_DEFAULT_VALUE, AV_STR_EMPTY //
  );

  /**
   * The text to be shown on widget for <b>true</b>.<br>
   */
  public static final IDataDef OPDEF_TRUE_TEXT = DataDef.create( OPID_TRUE_TEXT, STRING, //
      TSID_NAME, STR_N_TRUE_TEXT, //
      TSID_DESCRIPTION, STR_D_TRUE_TEXT, //
      TSID_DEFAULT_VALUE, AV_STR_EMPTY //
  );

  /**
   * The text to be shown on widget for <b>false</b>.<br>
   */
  public static final IDataDef OPDEF_FALSE_TEXT = DataDef.create( OPID_FALSE_TEXT, STRING, //
      TSID_NAME, STR_N_FALSE_TEXT, //
      TSID_DESCRIPTION, STR_D_FALSE_TEXT, //
      TSID_DEFAULT_VALUE, AV_STR_EMPTY //
  );

  /**
   * The text to be shown on widget for <b>false</b>.<br>
   */
  public static final IDataDef OPDEF_FALSE_FONT = DataDef.create( OPID_FALSE_FONT, VALOBJ, //
      TSID_NAME, STR_N_FALSE_FONT, //
      TSID_DESCRIPTION, STR_D_FALSE_FONT, //
      TSID_KEEPER_ID, FontInfo.KEEPER_ID, //
      TSID_DEFAULT_VALUE, AvUtils.AV_VALOBJ_NULL //
  );

  /**
   * The text to be shown on widget for <b>false</b>.<br>
   */
  public static final IDataDef OPDEF_TRUE_FONT = DataDef.create( OPID_TRUE_FONT, VALOBJ, //
      TSID_NAME, STR_N_TRUE_FONT, //
      TSID_DESCRIPTION, STR_D_TRUE_FONT, //
      TSID_KEEPER_ID, FontInfo.KEEPER_ID, //
      TSID_DEFAULT_VALUE, AvUtils.AV_VALOBJ_NULL //
  );

  /**
   * The gap between icon and text.<br>
   */
  public static final IDataDef OPDEF_ICON_GAP = DataDef.create( OPID_ICON_GAP, INTEGER, //
      TSID_NAME, STR_N_ICON_GAP, //
      TSID_DESCRIPTION, STR_D_ICON_GAP, //
      TSID_DEFAULT_VALUE, AvUtils.avInt( 4 ) //
  );

  /**
   * The gap between icon and text.<br>
   */
  public static final IDataDef OPDEF_ICON_ON_LEFT = DataDef.create( OPID_ICON_ON_LEFT, BOOLEAN, //
      TSID_NAME, STR_N_ICON_ON_LEFT, //
      TSID_DESCRIPTION, STR_D_ICON_ON_LEFT, //
      TSID_DEFAULT_VALUE, AvUtils.AV_TRUE //
  );

  /**
   * The gap between icon and text.<br>
   */
  public static final IDataDef OPDEF_ICON_SIZE = DataDef.create( OPID_ICON_SIZE, VALOBJ, //
      TSID_NAME, STR_N_ICON_SIZE, //
      TSID_DESCRIPTION, STR_D_ICON_SIZE, //
      TSID_DEFAULT_VALUE, AvUtils.avValobj( EIconSize.IS_16X16 ), //
      TSID_KEEPER_ID, EIconSize.KEEPER_ID //
  );

  /**
   * The factory name.
   */
  public static final String FACTORY_NAME = VALED_EDNAME_PREFIX + ".BooleanCheckAdv"; //$NON-NLS-1$

  /**
   * The factory class.
   *
   * @author vs
   */
  static class Factory
      extends AbstractValedControlFactory {

    protected Factory() {
      super( FACTORY_NAME );
    }

    @SuppressWarnings( "unchecked" )
    @Override
    protected IValedControl<Boolean> doCreateEditor( ITsGuiContext aContext ) {
      AbstractValedControl<Boolean, ?> e = new ValedBooleanCheckAdv1( aContext );
      e.setParamIfNull( OPDEF_IS_WIDTH_FIXED, AV_TRUE );
      e.setParamIfNull( OPDEF_IS_HEIGHT_FIXED, AV_TRUE );
      return e;
    }
  }

  /**
   * The factory singleton.
   */
  public static final AbstractValedControlFactory FACTORY = new Factory();

  private Canvas bkPanel;

  private Image imgCheckFalse = null;
  private Image imgCheckTrue  = null;

  private String trueText  = TsLibUtils.EMPTY_STRING;
  private String falseText = TsLibUtils.EMPTY_STRING;

  private Font trueFont  = null;
  private Font falseFont = null;

  private int imgX = 0;
  private int imgY = 0;
  private int txtX = 0;
  private int txtY = 0;

  private boolean value = false;

  /**
   * Constructor.
   *
   * @param aTsContext {@link ITsGuiContext} - th editor context
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException <code>enum</code> does not contains any constant
   */
  public ValedBooleanCheckAdv1( ITsGuiContext aTsContext ) {
    super( aTsContext );
  }

  // ------------------------------------------------------------------------------------
  // AbstractValedControl
  //

  @Override
  protected Canvas doCreateControl( Composite aParent ) {
    GC gc = null;

    try {
      int imgW = 0;
      int imgH = 0;

      IOptionSet params = tsContext().params();

      int imageGap = OPDEF_ICON_GAP.getValue( params ).asInt();
      EIconSize iconSize = OPDEF_ICON_SIZE.getValue( params ).asValobj();
      String iconId = OPDEF_FALSE_ICON_ID.getValue( params ).asString();
      if( iconId != null && !iconId.isBlank() ) {
        ITsIconManager im = tsContext().get( ITsIconManager.class );
        imgCheckFalse = im.loadStdIcon( iconId, iconSize );
        iconId = OPDEF_TRUE_ICON_ID.getValue( params ).asString();
        imgCheckTrue = im.loadStdIcon( iconId, iconSize );
        imgW = iconSize.size();
        imgH = iconSize.size();
      }

      FontInfo fi = OPDEF_FALSE_FONT.getValue( params ).asValobj();
      if( fi != null ) {
        falseFont = tsContext().get( ITsFontManager.class ).getFont( fi );
      }
      fi = OPDEF_TRUE_FONT.getValue( params ).asValobj();
      if( fi != null ) {
        trueFont = tsContext().get( ITsFontManager.class ).getFont( fi );
      }

      Point falseExtent = new Point( 0, 0 );
      Point trueExtent = new Point( 0, 0 );
      gc = new GC( tsContext().get( Display.class ) );
      falseText = OPDEF_FALSE_TEXT.getValue( params ).asString();
      trueText = OPDEF_TRUE_TEXT.getValue( params ).asString();
      if( falseText != null && !falseText.isBlank() ) {
        if( falseFont != null ) {
          gc.setFont( falseFont );
        }
        falseExtent = gc.textExtent( falseText, SWT.DRAW_TRANSPARENT );
      }
      if( trueText != null && !trueText.isBlank() ) {
        if( trueFont != null ) {
          gc.setFont( trueFont );
        }
        trueExtent = gc.textExtent( trueText, SWT.DRAW_TRANSPARENT );
      }
      bkPanel = new Canvas( aParent, SWT.NONE );

      int txtW = Math.max( falseExtent.x, trueExtent.x );
      int txtH = Math.max( falseExtent.y, trueExtent.y );

      if( txtW == 0 || imgW == 0 ) {
        imageGap = 0;
      }

      int w = imgW + imageGap + txtW;
      int h = Math.max( imgH, txtH );

      bkPanel.setSize( w, h );
      bkPanel.setData( AWTLayout.KEY_PREFERRED_SIZE, new java.awt.Dimension( w, h ) );
      // bkPanel.addControlListener( new ControlListener() {
      //
      // @Override
      // public void controlResized( ControlEvent aE ) {
      // // TODO Auto-generated method stub
      // Point p = bkPanel.getSize();
      // bkPanel.setSize( p.x, 24 );
      // }
      //
      // @Override
      // public void controlMoved( ControlEvent aE ) {
      // // TODO Auto-generated method stub
      //
      // }
      // } );

      // bkPanel.setData( AWTLayout.KEY_PREFERRED_SIZE, new java.awt.Dimension( w, h ) );
      imgY = (h - imgH) / 2;
      txtY = (h - txtH) / 2;
      if( OPDEF_ICON_ON_LEFT.getValue( params ).asBool() ) {
        imgX = 0;
        txtX = w - txtW;
      }
      else {
        imgX = w - imgW;
        txtX = 0;
      }

      bkPanel.addPaintListener( aE -> {
        bkPanel.setBackground( colorManager().getColor( ETsColor.RED ) );
        Font oldFont = aE.gc.getFont();
        if( !value ) {
          if( imgCheckFalse != null && !value ) {
            aE.gc.drawImage( imgCheckFalse, imgX, imgY );
          }
          if( falseText != null ) {
            if( falseFont != null ) {
              aE.gc.setFont( falseFont );
            }
            aE.gc.drawText( falseText, txtX, txtY, true );
          }
        }
        else {
          if( imgCheckTrue != null && value ) {
            aE.gc.drawImage( imgCheckTrue, imgX, imgY );
          }
          if( trueText != null ) {
            if( trueFont != null ) {
              aE.gc.setFont( trueFont );
            }
            aE.gc.drawText( trueText, txtX, txtY, true );
          }
        }
        aE.gc.setFont( oldFont );
      } );
      Point s = bkPanel.getSize();
      return bkPanel;
    }
    finally {
      if( gc != null ) {
        gc.dispose();
      }
    }
  }

  @Override
  protected void doSetEditable( boolean aEditable ) {
    // nop - not supported
  }

  @Override
  protected Boolean doGetUnvalidatedValue() {
    return Boolean.valueOf( value );
  }

  @Override
  protected void doSetUnvalidatedValue( Boolean aValue ) {
    value = aValue.booleanValue();
    bkPanel.redraw();
  }

  @Override
  protected void doClearValue() {
    doSetUnvalidatedValue( Boolean.valueOf( false ) );
  }

}
