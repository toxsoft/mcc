package ru.toxsoft.mcc.ws.mnemos.app.valed;

import static org.toxsoft.core.tsgui.valed.api.IValedControlConstants.*;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.valed.api.*;
import org.toxsoft.core.tsgui.valed.controls.av.*;
import org.toxsoft.core.tsgui.valed.impl.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link EAtomicType#BOOLEAN} editor using {@link ValedBooleanCheckAdv}.
 *
 * @author vs
 */
public class ValedAvBooleanCheckAdv
    extends AbstractAvWrapperValedControl<Boolean> {

  /**
   * The factory name.
   */
  public static final String FACTORY_NAME = VALED_EDNAME_PREFIX + ".AvBooleanCheckAdv"; //$NON-NLS-1$

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
      return new ValedAvBooleanCheckAdv( aContext );
    }

  }

  /**
   * The factory singleton.
   */
  public static final AbstractValedControlFactory FACTORY = new Factory();

  /**
   * Constructor.
   *
   * @param aTsContext {@link ITsGuiContext} - the editor context
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public ValedAvBooleanCheckAdv( ITsGuiContext aTsContext ) {
    super( aTsContext, EAtomicType.BOOLEAN, ValedBooleanCheckAdv.FACTORY );
  }

  @Override
  protected Boolean av2tv( IAtomicValue aAtomicValue ) {
    return Boolean.valueOf( aAtomicValue.asBool() );
  }

  @Override
  protected IAtomicValue tv2av( Boolean aValue ) {
    return AvUtils.avBool( aValue.booleanValue() );
  }

}
