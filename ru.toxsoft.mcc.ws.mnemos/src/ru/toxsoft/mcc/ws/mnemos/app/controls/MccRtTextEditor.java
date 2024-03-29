package ru.toxsoft.mcc.ws.mnemos.app.controls;

import static org.toxsoft.core.tsgui.graphics.icons.ITsStdIconIds.*;
import static org.toxsoft.core.tsgui.valed.IValedImplementationHelpers.*;
import static ru.toxsoft.mcc.ws.mnemos.app.controls.IVjResources.*;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.toxsoft.core.tsgui.bricks.ctx.ITsGuiContext;
import org.toxsoft.core.tsgui.bricks.ctx.ITsGuiContextable;
import org.toxsoft.core.tsgui.dialogs.TsDialogUtils;
import org.toxsoft.core.tsgui.graphics.colors.ETsColor;
import org.toxsoft.core.tsgui.graphics.icons.EIconSize;
import org.toxsoft.core.tsgui.graphics.icons.ITsIconManager;
import org.toxsoft.core.tslib.av.EAtomicType;
import org.toxsoft.core.tslib.av.IAtomicValue;
import org.toxsoft.core.tslib.av.impl.AvUtils;
import org.toxsoft.core.tslib.av.opset.IOptionSetEdit;
import org.toxsoft.core.tslib.av.opset.impl.OptionSet;
import org.toxsoft.core.tslib.bricks.events.change.IGenericChangeListener;
import org.toxsoft.core.tslib.gw.gwid.*;
import org.toxsoft.core.tslib.gw.skid.Skid;
import org.toxsoft.core.tslib.utils.TsLibUtils;
import org.toxsoft.core.tslib.utils.errors.TsNotAllEnumsUsedRtException;
import org.toxsoft.core.tslib.utils.logs.impl.LoggerUtils;
import org.toxsoft.uskat.core.ISkCoreApi;
import org.toxsoft.uskat.core.api.cmdserv.*;
import org.toxsoft.uskat.core.api.objserv.ISkObject;
import org.toxsoft.uskat.core.api.sysdescr.ISkClassInfo;
import org.toxsoft.uskat.core.api.sysdescr.dto.IDtoCmdInfo;
import org.toxsoft.uskat.core.api.users.ISkUser;
import org.toxsoft.uskat.core.gui.conn.ISkConnectionSupplier;

import ru.toxsoft.mcc.ws.mnemos.app.CmdUtils;
import ru.toxsoft.mcc.ws.mnemos.app.rt.IRtDataConsumer;

/**
 * Редактор РВ-данного, представляющий собой текстовое поле с прикрепленной в конце кнопкой. Текстовое поле отображает
 * текущее значение, а при нажатии на кнопку появляется диалог ввода нового значения. При подтверждении ввода нового
 * значения посылается соотвествующая команда.
 *
 * @author vs
 */
public class MccRtTextEditor
    implements IRtDataConsumer, ITsGuiContextable {

  private Composite board;
  private CLabel    label;
  private Button    button;

  private final ITsGuiContext tsContext;

  private final ISkObject skObject;

  private final String dataId;

  private final String commandId;
  private final String cmdArgId;

  private final EAtomicType valueType;

  private IAtomicValue value = IAtomicValue.NULL;

  IAtomicValue newVal = IAtomicValue.NULL;

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
    LoggerUtils.errorLogger().info( "command %s state changed %s", cmd.cmdGwid(), cmdState.state() ); //$NON-NLS-1$
    if( cmd.isComplete() ) {
      cmd.stateEventer().removeListener( this.commandListener );
      getButtonControl().setEnabled( true );
      if( cmd.isComplete() && cmd.state().state() != ESkCommandState.SUCCESS ) {
        TsDialogUtils.error( getShell(), cmd.state().state().description() );
      }
    }
  };

  /**
   * @param aSkObject ISkObject - серверный объект
   * @param aDataId String - ИД данного
   * @param aCmdId String - ИД команды
   * @param aTsContext ITsGuiContext - соответствующий контекст
   */
  public MccRtTextEditor( ISkObject aSkObject, String aDataId, String aCmdId, ITsGuiContext aTsContext ) {
    skObject = aSkObject;
    dataId = aDataId;
    commandId = aCmdId;
    tsContext = aTsContext;

    String argId = TsLibUtils.EMPTY_STRING;
    ISkCoreApi coreApi = tsContext().get( ISkConnectionSupplier.class ).defConn().coreApi();
    ISkClassInfo clsInfo = coreApi.sysdescr().findClassInfo( skObject.classId() );
    for( IDtoCmdInfo cmdInfo : clsInfo.cmds().list() ) {
      if( cmdInfo.id().equals( commandId ) ) {
        argId = cmdInfo.argDefs().first().id();
      }
    }
    cmdArgId = argId;
    valueType = clsInfo.rtdata().list().getByKey( aDataId ).dataType().atomicType();
  }

  // ------------------------------------------------------------------------------------
  // IStridable
  //

  @Override
  public String id() {
    return skObject.id() + "." + dataId; //$NON-NLS-1$
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
  // IRtDataConsumer
  //

  @Override
  public IGwidList listNeededGwids() {
    GwidList gl = new GwidList();
    gl.add( Gwid.createRtdata( skObject.classId(), skObject.strid(), dataId ) );
    return gl;
  }

  @Override
  public void setValues( Gwid[] aGwids, IAtomicValue[] aValues, int aCount ) {
    if( aCount == 1 ) {
      value = aValues[0];
      updateText();
    }
  }

  // ------------------------------------------------------------------------------------
  // ITsGuiContextable
  //

  @Override
  public ITsGuiContext tsContext() {
    return tsContext;
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Создает SWT контроль.
   *
   * @param aParent Composite - родительская компонента
   * @return Composite - SWT контроль
   */
  public Composite createControl( Composite aParent ) {
    board = new Composite( aParent, SWT.NO_FOCUS );
    GridLayout gl = new GridLayout( 2, false );
    gl.verticalSpacing = 0;
    gl.horizontalSpacing = 0;
    gl.marginTop = 0;
    gl.marginBottom = 0;
    gl.marginHeight = 0;
    gl.marginWidth = 0;
    board.setLayout( gl );
    // label
    label = new CLabel( board, SWT.BORDER | SWT.LEFT );
    label.setLayoutData( new GridData( SWT.FILL, SWT.CENTER, true, false, 1, 1 ) );
    // button
    button = new Button( board, SWT.PUSH | SWT.FLAT );
    button.setLayoutData( new GridData( SWT.FILL, SWT.TOP, false, false, 1, 1 ) );
    button.addSelectionListener( new SelectionAdapter() {

      @Override
      public void widgetSelected( SelectionEvent e ) {
        processButtonPress();
      }
    } );
    button.setText( STR_ELLIPSIS );

    ITsIconManager iconManager = tsContext().get( ITsIconManager.class );
    int labelH = getLabelControl().getSize().y;

    EIconSize iconSize = EIconSize.IS_16X16;
    for( EIconSize is : EIconSize.values() ) {
      if( is.size() >= labelH ) {
        break;
      }
      iconSize = is;
    }

    getButtonControl().setText( TsLibUtils.EMPTY_STRING );
    getButtonControl().setImage( iconManager.loadStdIcon( ICONID_DOCUMENT_EDIT, iconSize ) );
    getLabelControl().setBackground( colorManager().getColor( ETsColor.WHITE ) );

    return board;
  }

  /**
   * Returns the label control.
   *
   * @return {@link CLabel} - the label control
   */
  public CLabel getLabelControl() {
    return label;
  }

  /**
   * Returns the button at right.
   *
   * @return Buntton - the button at right
   */
  public Button getButtonControl() {
    return button;
  }

  // ------------------------------------------------------------------------------------
  // Implementation
  //

  void processButtonPress() {
    newVal = editValue( value );
    if( newVal == null ) {
      return;
    }
    ISkCoreApi coreApi = tsContext().get( ISkConnectionSupplier.class ).defConn().coreApi();
    ISkCommandService cmdService = coreApi.cmdService();

    // GwidList gwil = new GwidList();
    // gwil.add( commandGwid() );
    // cmdService.registerExecutor( commandExecutor, gwil );

    IOptionSetEdit args = new OptionSet();
    args.setValue( cmdArgId, newVal );
    ISkCommand command = cmdService.sendCommand( commandGwid(), new Skid( ISkUser.CLASS_ID, "root" ), args );
    CmdUtils.logCommandHistory( command );
    String errStr = CmdUtils.errorString( command );
    if( errStr != null ) {
      TsDialogUtils.error( getShell(), errStr );
      return;
    }

    command.stateEventer().addListener( commandListener );
    getButtonControl().setEnabled( false );
    getLabelControl().setEnabled( false );
    getLabelControl().setText( "???" ); //$NON-NLS-1$
    // value = newVal;
    // updateTextControl();
    // fireModifyEvent( true );
  }

  protected IAtomicValue editValue( IAtomicValue aValue ) {
    String strValue = TsLibUtils.EMPTY_STRING;
    if( value.isAssigned() ) {
      strValue = AvUtils.printAv( formatString( value.atomicType() ), aValue );
    }
    InputDialog dlg = new InputDialog( getShell(), STR_DLG_T_EDIT_VALUE, STR_DLG_M_VALUE, strValue, null );
    if( dlg.open() == IDialogConstants.OK_ID ) {
      if( valueType == EAtomicType.FLOATING ) {
        String valStr = dlg.getValue();
        double dVal = 0;
        try {
          dVal = Double.parseDouble( valStr );
        }
        catch( NumberFormatException e ) {
          int idx = valStr.indexOf( ',' );
          if( idx != -1 ) {
            valStr = valStr.replace( ',', '.' );
            dVal = Double.parseDouble( valStr );
          }
          else {
            idx = valStr.indexOf( '.' );
            valStr = valStr.replace( '.', ',' );
            dVal = Double.parseDouble( valStr );
          }
        }
        return AvUtils.avFloat( dVal );
      }
      if( valueType == EAtomicType.INTEGER ) {
        return AvUtils.avInt( Long.parseLong( dlg.getValue() ) );
      }
      return AvUtils.avStr( dlg.getValue() );
    }
    return null;
  }

  private void updateText() {
    label.setText( AvUtils.printAv( formatString( value.atomicType() ), value ) );
  }

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

  protected Gwid commandGwid() {
    return Gwid.createCmd( skObject.classId(), skObject.strid(), commandId );
  }

}
