package ru.toxsoft.mcc.ws.mnemos.app.valed;

import static org.toxsoft.core.tsgui.valed.api.IValedControlConstants.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static ru.toxsoft.mcc.ws.mnemos.app.valed.IVjResources.*;

import org.eclipse.jface.dialogs.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.valed.api.*;
import org.toxsoft.core.tsgui.valed.impl.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Редактор значения с плавающей запятой путем посылки команды.
 * <p>
 *
 * @author vs
 */
public class ValedFloatingTextCommand
    extends AbstractValedSkCommand {

  /**
   * The factory name.
   */
  public static final String FACTORY_NAME = VALED_OPID_PREFIX + ".ValedFloatTextCmd"; //$NON-NLS-1$

  /**
   * The factory class.
   *
   * @author hazard157
   */
  static class Factory
      extends AbstractValedControlFactory {

    protected Factory() {
      super( FACTORY_NAME );
    }

    @SuppressWarnings( "unchecked" )
    @Override
    protected IValedControl<IAtomicValue> doCreateEditor( ITsGuiContext aContext ) {
      AbstractValedControl<IAtomicValue, ?> e = new ValedFloatingTextCommand( aContext );
      e.setParamIfNull( OPDEF_IS_WIDTH_FIXED, AV_TRUE );
      e.setParamIfNull( OPDEF_IS_HEIGHT_FIXED, AV_FALSE );
      return e;
    }

  }

  /**
   * The factory singleton.
   */
  public static final AbstractValedControlFactory FACTORY = new Factory();

  /**
   * Constructor.
   *
   * @param aContext {@link ITsGuiContext} - the editor context
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemNotFoundRtException context does not contains mandatory information
   * @throws TsIllegalArgumentRtException <code>enum</code> does not contains any constant
   */
  public ValedFloatingTextCommand( ITsGuiContext aContext ) {
    super( aContext );
    setValue( AvUtils.avFloat( 12345.67 ) );
  }

  @Override
  protected IAtomicValue editValue( IAtomicValue aValue ) {
    InputDialog dlg =
        new InputDialog( getShell(), STR_DLG_T_EDIT_VALUE, STR_DLG_M_VALUE, "" + aValue.asDouble(), null ); //$NON-NLS-1$
    if( dlg.open() == IDialogConstants.OK_ID ) {
      return AvUtils.avFloat( Double.parseDouble( dlg.getValue() ) );
    }
    return null;
  }

  @Override
  protected String value2text( IAtomicValue aValue ) {
    return AvUtils.printAv( "%f", aValue ); //$NON-NLS-1$
  }

}
