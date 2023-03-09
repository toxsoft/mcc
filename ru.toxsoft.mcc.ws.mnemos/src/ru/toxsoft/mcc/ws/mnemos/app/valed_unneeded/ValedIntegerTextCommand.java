package ru.toxsoft.mcc.ws.mnemos.app.valed_unneeded;

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
 * Редактор целого значения путем посылки команды.
 * <p>
 *
 * @author vs
 */
public class ValedIntegerTextCommand
    extends AbstractValedSkCommand {

  /**
   * The factory name.
   */
  public static final String FACTORY_NAME = VALED_OPID_PREFIX + ".ValedIntTextCmd"; //$NON-NLS-1$

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
      AbstractValedControl<IAtomicValue, ?> e = new ValedIntegerTextCommand( aContext );
      e.setParamIfNull( OPDEF_IS_WIDTH_FIXED, AV_FALSE );
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
  public ValedIntegerTextCommand( ITsGuiContext aContext ) {
    super( aContext );
  }

  @Override
  protected IAtomicValue editValue( IAtomicValue aValue ) {
    InputDialog dlg = new InputDialog( getShell(), STR_DLG_T_EDIT_VALUE, STR_DLG_M_VALUE, "" + aValue.asInt(), null ); //$NON-NLS-1$
    if( dlg.open() == IDialogConstants.OK_ID ) {
      return AvUtils.avInt( Integer.parseInt( dlg.getValue() ) );
    }
    return null;
  }

  @Override
  protected String value2text( IAtomicValue aValue ) {
    return AvUtils.printAv( "%d", aValue ); //$NON-NLS-1$
  }

}
