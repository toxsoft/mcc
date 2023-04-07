package ru.toxsoft.mcc.ws.mnemos.app.valed;

import static org.toxsoft.core.tsgui.valed.api.IValedControlConstants.*;
import static org.toxsoft.core.tslib.av.EAtomicType.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;
import static ru.toxsoft.mcc.ws.mnemos.app.valed.IVjResources.*;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.toxsoft.core.tsgui.bricks.ctx.ITsGuiContext;
import org.toxsoft.core.tsgui.dialogs.TsDialogUtils;
import org.toxsoft.core.tsgui.graphics.icons.EIconSize;
import org.toxsoft.core.tsgui.valed.api.IValedControl;
import org.toxsoft.core.tsgui.valed.impl.AbstractValedControl;
import org.toxsoft.core.tsgui.valed.impl.AbstractValedControlFactory;
import org.toxsoft.core.tslib.av.IAtomicValue;
import org.toxsoft.core.tslib.av.impl.AvUtils;
import org.toxsoft.core.tslib.av.impl.DataDef;
import org.toxsoft.core.tslib.av.metainfo.IDataDef;
import org.toxsoft.core.tslib.av.opset.IOptionSet;
import org.toxsoft.core.tslib.av.opset.impl.OptionSet;
import org.toxsoft.core.tslib.bricks.events.change.IGenericChangeListener;
import org.toxsoft.core.tslib.gw.gwid.Gwid;
import org.toxsoft.core.tslib.gw.gwid.GwidList;
import org.toxsoft.core.tslib.gw.skid.Skid;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.tslib.utils.logs.impl.LoggerUtils;
import org.toxsoft.uskat.core.ISkCoreApi;
import org.toxsoft.uskat.core.api.cmdserv.*;
import org.toxsoft.uskat.core.api.rtdserv.ISkWriteCurrDataChannel;
import org.toxsoft.uskat.core.api.sysdescr.ISkClassInfo;
import org.toxsoft.uskat.core.api.sysdescr.dto.IDtoCmdInfo;
import org.toxsoft.uskat.core.api.users.ISkUser;
import org.toxsoft.uskat.core.gui.conn.ISkConnectionSupplier;

import ru.toxsoft.mcc.ws.mnemos.app.CmdUtils;

/**
 * Специализированный (для МосКокса) редактор логического значения.
 * <p>
 * 21 июня 2001 г.
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

  private boolean value  = false;
  private boolean newVal = false;

  private Image imgFalse = null;
  private Image imgTrue  = null;

  private final Gwid dataGwid;
  private final Gwid cmdGwid;

  ISkCommandExecutor commandExecutor = aCmd -> {
    ISkCoreApi coreApi = tsContext().get( ISkConnectionSupplier.class ).defConn().coreApi();
    ISkWriteCurrDataChannel channel;
    GwidList gwil = new GwidList();

    gwil.add( dataGwid() );

    channel = coreApi.rtdService().createWriteCurrDataChannels( gwil ).values().first();
    channel.setValue( AvUtils.avBool( newVal ) );
    channel.close();

    ISkCommandService cmdService = coreApi.cmdService();
    SkCommandState state = new SkCommandState( System.currentTimeMillis(), ESkCommandState.SUCCESS );
    cmdService.changeCommandState( new DtoCommandStateChangeInfo( aCmd.instanceId(), state ) );
  };

  IGenericChangeListener commandListener = aSource -> {
    ISkCommand cmd = (ISkCommand)aSource;
    SkCommandState cmdState = cmd.state();
    switch( cmdState.state() ) {
      case EXECUTING:
        break;
      case SENDING:
        break;
      case FAILED:
        cmd.stateEventer().removeListener( this.commandListener );
        break;
      case SUCCESS:
        cmd.stateEventer().removeListener( this.commandListener );
        break;
      case TIMEOUTED:
        cmd.stateEventer().removeListener( this.commandListener );
        break;
      case UNHANDLED:
        cmd.stateEventer().removeListener( this.commandListener );
        break;
      default:
        throw new TsNotAllEnumsUsedRtException();
    }
    LoggerUtils.errorLogger().info( "command %s state changed %s", cmd.cmdGwid(), cmdState.state() );
    if( cmd.isComplete() ) {
      cmd.stateEventer().removeListener( this.commandListener );
      if( cmd.isComplete() && cmd.state().state() != ESkCommandState.SUCCESS ) {
        TsDialogUtils.error( getShell(), cmd.state().state().description() );
      }
      // getButtonControl().setEnabled( true );
    }
  };

  /**
   * Constructor.
   *
   * @param aTsContext {@link ITsGuiContext} - the editor context
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException <code>enum</code> does not contains any constant
   */
  public MccValedAvBooleanCheckCommand( ITsGuiContext aTsContext ) {
    super( aTsContext );
    IOptionSet params = aTsContext.params();
    String classId = OPDEF_CLASS_ID.getValue( params ).asString();
    String objStrid = OPDEF_OBJ_STRID.getValue( params ).asString();
    String dataId = OPDEF_DATA_ID.getValue( params ).asString();
    String cmdId = OPDEF_COMMAND_ID.getValue( params ).asString();
    dataGwid = Gwid.createRtdata( classId, objStrid, dataId );
    cmdGwid = Gwid.createCmd( classId, objStrid, cmdId );
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
        newVal = button.getSelection();
        if( !sendCommand( button.getSelection() ) ) {
          button.setSelection( !newVal );
        }
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

  // ------------------------------------------------------------------------------------
  // Implementation
  //

  boolean sendCommand( boolean aArg ) {

    ISkCoreApi coreApi = tsContext().get( ISkConnectionSupplier.class ).defConn().coreApi();
    ISkCommandService cmdService = coreApi.cmdService();

    // GwidList gwil = new GwidList();
    // gwil.add( cmdGwid );
    // cmdService.registerExecutor( commandExecutor, gwil );

    ISkClassInfo classInfo = coreApi.sysdescr().findClassInfo( cmdGwid.classId() );
    IDtoCmdInfo cmdInfo = classInfo.cmds().list().getByKey( cmdGwid.propId() );
    String argId = cmdInfo.argDefs().first().id();

    OptionSet cmdArgs = new OptionSet();
    cmdArgs.setValue( argId, AvUtils.avBool( aArg ) );
    ISkCommand cmd = cmdService.sendCommand( cmdGwid, new Skid( ISkUser.CLASS_ID, "root" ), cmdArgs );
    CmdUtils.logCommandHistory( cmd );
    String errStr = CmdUtils.errorString( cmd );
    if( errStr != null ) {
      TsDialogUtils.error( getShell(), errStr );
      return false;
    }
    cmd.stateEventer().addListener( commandListener );
    fireModifyEvent( true );
    return true;
  }

  Gwid dataGwid() {
    return dataGwid;
  }

}
