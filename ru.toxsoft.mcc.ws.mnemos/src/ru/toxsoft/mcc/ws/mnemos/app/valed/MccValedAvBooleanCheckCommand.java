package ru.toxsoft.mcc.ws.mnemos.app.valed;

import static org.toxsoft.core.tsgui.valed.api.IValedControlConstants.*;
import static org.toxsoft.core.tslib.av.EAtomicType.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;
import static ru.toxsoft.mcc.ws.mnemos.app.valed.IVjResources.*;

import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.graphics.icons.*;
import org.toxsoft.core.tsgui.valed.api.*;
import org.toxsoft.core.tsgui.valed.impl.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Специализированный (для МосКокса) редактор логического значения.
 * <p>
 *
 * @author vs
 */
public class MccValedAvBooleanCheckCommand
    extends AbstractValedControl<IAtomicValue, Button> {

  /**
   * ID of context reference {@link #OPDEF_TRUE_ICON_ID}.
   */
  public static final String OPID_TRUE_ICON_ID = VALED_OPID_PREFIX + ".TrueIconId"; //$NON-NLS-1$

  /**
   * ID of context reference {@link #OPDEF_FALSE_ICON_ID}.
   */
  public static final String OPID_FALSE_ICON_ID = VALED_OPID_PREFIX + ".FalseIconId"; //$NON-NLS-1$

  /**
   * ID of context reference {@link #OPDEF_ICON_SIZE}.
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
   * The icon size.<br>
   */
  public static final IDataDef OPDEF_ICON_SIZE = DataDef.create( OPID_ICON_SIZE, VALOBJ, //
      TSID_NAME, STR_N_ICON_SIZE, //
      TSID_DESCRIPTION, STR_D_ICON_SIZE, //
      TSID_DEFAULT_VALUE, AvUtils.avValobj( EIconSize.IS_16X16 ), //
      TSID_KEEPER_ID, EIconSize.KEEPER_ID //
  );

  /**
   * ID of context reference {@link #OPDEF_CLASS_ID}.
   */
  public static final String OPID_CLASS_ID = VALED_OPID_PREFIX + ".classId"; //$NON-NLS-1$

  /**
   * ID of context reference {@link #OPDEF_OBJ_STRID}.
   */
  public static final String OPID_OBJ_STRID = VALED_OPID_PREFIX + ".objStrid"; //$NON-NLS-1$

  /**
   * ID of context reference {@link #OPDEF_DATA_ID}.
   */
  public static final String OPID_DATA_ID = VALED_OPID_PREFIX + ".dataId"; //$NON-NLS-1$

  /**
   * ID of context reference {@link #OPDEF_COMMAND_ID}.
   */
  public static final String OPID_COMMAND_ID = VALED_OPID_PREFIX + ".commandId"; //$NON-NLS-1$

  /**
   * Class Id.<br>
   */
  public static final IDataDef OPDEF_CLASS_ID = DataDef.create( OPID_CLASS_ID, STRING, //
      TSID_NAME, STR_N_CLASS_ID, //
      TSID_DESCRIPTION, STR_D_CLASS_ID, //
      TSID_DEFAULT_VALUE, AV_STR_EMPTY //
  );

  /**
   * Object strid.<br>
   */
  public static final IDataDef OPDEF_OBJ_STRID = DataDef.create( OPID_OBJ_STRID, STRING, //
      TSID_NAME, STR_N_OBJ_STRID, //
      TSID_DESCRIPTION, STR_D_OBJ_STRID, //
      TSID_DEFAULT_VALUE, AV_STR_EMPTY //
  );

  /**
   * Data Id.<br>
   */
  public static final IDataDef OPDEF_DATA_ID = DataDef.create( OPID_DATA_ID, STRING, //
      TSID_NAME, STR_N_DATA_ID, //
      TSID_DESCRIPTION, STR_D_DATA_ID, //
      TSID_DEFAULT_VALUE, AV_STR_EMPTY //
  );

  /**
   * Command Id that will be used to send command for setting new value.<br>
   */
  public static final IDataDef OPDEF_COMMAND_ID = DataDef.create( OPID_COMMAND_ID, STRING, //
      TSID_NAME, STR_N_COMMAND_ID, //
      TSID_DESCRIPTION, STR_D_COMMAND_ID, //
      TSID_DEFAULT_VALUE, AV_STR_EMPTY //
  );

  /**
   * The factory name.
   */
  public static final String FACTORY_NAME = VALED_EDNAME_PREFIX + ".MccAvBooleanCheckCmd"; //$NON-NLS-1$

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
    protected IValedControl<IAtomicValue> doCreateEditor( ITsGuiContext aContext ) {
      AbstractValedControl<IAtomicValue, ?> e = new MccValedAvBooleanCheckCommand( aContext );
      e.setParamIfNull( OPDEF_IS_WIDTH_FIXED, AV_TRUE );
      e.setParamIfNull( OPDEF_IS_HEIGHT_FIXED, AV_TRUE );
      return e;
    }
  }

  /**
   * The factory singleton.
   */
  public static final AbstractValedControlFactory FACTORY = new Factory();

  private Button button = null;

  private boolean value = false;

  private Image imgFalse = null;
  private Image imgTrue  = null;

  /**
   * Constructor.
   *
   * @param aTsContext {@link ITsGuiContext} - the editor context
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException <code>enum</code> does not contains any constant
   */
  public MccValedAvBooleanCheckCommand( ITsGuiContext aTsContext ) {
    super( aTsContext );
  }

  // ------------------------------------------------------------------------------------
  // AbstractValedControl
  //

  @Override
  protected Button doCreateControl( Composite aParent ) {

    IOptionSet params = tsContext().params();

    EIconSize iconSize = OPDEF_ICON_SIZE.getValue( params ).asValobj();
    String falseIconId = OPDEF_FALSE_ICON_ID.getValue( params ).asString();
    String trueIconId = OPDEF_TRUE_ICON_ID.getValue( params ).asString();

    imgFalse = iconManager().loadStdIcon( falseIconId, iconSize );
    imgTrue = iconManager().loadStdIcon( trueIconId, iconSize );

    button = new Button( aParent, SWT.CHECK );
    button.setSelection( value );
    button.setImage( imgFalse );

    button.addSelectionListener( new SelectionAdapter() {

      @Override
      public void widgetSelected( SelectionEvent aEvent ) {
        // FIXME реализовать
        throw new TsUnderDevelopmentRtException();
      }
    } );

    return button;
  }

  @Override
  protected void doSetEditable( boolean aEditable ) {
    button.setEnabled( aEditable );
  }

  @Override
  protected IAtomicValue doGetUnvalidatedValue() {
    return AvUtils.avBool( value );
  }

  @Override
  protected void doSetUnvalidatedValue( IAtomicValue aValue ) {
    value = aValue.asBool();
    button.setSelection( value );
    if( value ) {
      button.setImage( imgTrue );
    }
    else {
      button.setImage( imgFalse );
    }
  }

  @Override
  protected void doClearValue() {
    // button.setImage( null );
  }

}
