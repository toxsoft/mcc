package ru.toxsoft.mcc.ws.core.templates.gui.valed;

import static org.toxsoft.core.tsgui.valed.api.IValedControlConstants.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static ru.toxsoft.mcc.ws.core.templates.gui.valed.ITsResources.*;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.valed.api.*;
import org.toxsoft.core.tsgui.valed.controls.helpers.*;
import org.toxsoft.core.tsgui.valed.impl.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.gw.gwid.*;
import org.toxsoft.core.tslib.gw.skid.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.uskat.core.api.objserv.*;

import ru.toxsoft.mcc.ws.core.templates.gui.*;

/**
 * Allows to select {@link Skid} by accessing {@link ISkObjectService}.
 *
 * @author hazard157
 * @author dima
 */
public class ValedSkidEditor
    extends AbstractValedTextAndButton<Skid> {

  /**
   * The factory name.
   */
  public static final String FACTORY_NAME = VALED_EDNAME_PREFIX + ".SkidEditor"; //$NON-NLS-1$

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
    protected IValedControl<Skid> doCreateEditor( ITsGuiContext aContext ) {
      AbstractValedControl<Skid, ?> e = new ValedSkidEditor( aContext );
      return e;
    }

  }

  /**
   * The factory singleton.
   */
  public static final AbstractValedControlFactory FACTORY = new Factory();

  /**
   * Constructor for subclasses.
   *
   * @param aContext {@link ITsGuiContext} - the valed context
   * @throws TsNullArgumentRtException аргумент = null
   */
  public ValedSkidEditor( ITsGuiContext aContext ) {
    super( aContext );
    setParamIfNull( OPDEF_IS_WIDTH_FIXED, AV_FALSE );
    setParamIfNull( OPDEF_IS_HEIGHT_FIXED, AV_TRUE );
    setParamIfNull( OPDEF_VERTICAL_SPAN, AV_1 );

  }

  @Override
  protected boolean doProcessButtonPress() {
    // create and dispaly Skid selector
    Gwid initVal = canGetValue().isOk() ? Gwid.createObj( getValue() ) : null;
    Gwid gwid = PanelGwidSelector.selectGwid( initVal, tsContext() );
    if( gwid != null ) {
      doSetUnvalidatedValue( gwid.skid() );
      return true;
    }
    return false;
  }

  @Override
  public ValidationResult canGetValue() {
    try {
      Gwid g = Gwid.of( getTextControl().getText() );
      if( g.kind() == EGwidKind.GW_CLASS && !g.isAbstract() ) {
        return ValidationResult.SUCCESS;
      }
      return ValidationResult.error( MSG_ERR_INV_SKID_FORMAT );
    }
    catch( @SuppressWarnings( "unused" ) Exception ex ) {
      return ValidationResult.error( MSG_ERR_INV_SKID_FORMAT );
    }
  }

  @Override
  protected void doUpdateTextControl() {
    // nop
  }

  @Override
  protected Skid doGetUnvalidatedValue() {
    return Gwid.of( getTextControl().getText() ).skid();
  }

  @Override
  protected void doDoSetUnvalidatedValue( Skid aValue ) {
    String txt = TsLibUtils.EMPTY_STRING;
    if( aValue != null ) {
      txt = aValue.toString();
    }
    getTextControl().setText( txt );
  }

}
