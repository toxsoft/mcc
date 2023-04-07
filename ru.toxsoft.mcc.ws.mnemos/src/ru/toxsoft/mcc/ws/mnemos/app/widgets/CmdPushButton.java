package ru.toxsoft.mcc.ws.mnemos.app.widgets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.toxsoft.core.tsgui.bricks.ctx.ITsGuiContext;
import org.toxsoft.core.tslib.av.opset.IOptionSet;
import org.toxsoft.core.tslib.bricks.ctx.ITsContext;
import org.toxsoft.core.tslib.bricks.ctx.ITsContextable;
import org.toxsoft.core.tslib.bricks.events.change.IGenericChangeListener;
import org.toxsoft.core.tslib.gw.gwid.Gwid;
import org.toxsoft.core.tslib.gw.skid.Skid;
import org.toxsoft.core.tslib.utils.errors.TsNotAllEnumsUsedRtException;
import org.toxsoft.uskat.core.ISkCoreApi;
import org.toxsoft.uskat.core.api.cmdserv.*;
import org.toxsoft.uskat.core.api.sysdescr.ISkClassInfo;
import org.toxsoft.uskat.core.api.sysdescr.dto.IDtoCmdInfo;
import org.toxsoft.uskat.core.api.users.ISkUser;
import org.toxsoft.uskat.core.gui.conn.ISkConnectionSupplier;

/**
 * SWT кнопка для однократной посылки команды при нажатии.
 * <p>
 *
 * @author vs
 */
public class CmdPushButton
    implements ITsContextable {

  private final Button        button;
  private final Gwid          cmdGwid;
  private final ITsGuiContext tsContext;
  private final String        cmdName;
  private final IOptionSet    cmdArgs;

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
    if( cmd.isComplete() ) {
      cmd.stateEventer().removeListener( this.commandListener );
      button().setEnabled( true );
      button().setToolTipText( "Послать команду: " + commandName() );
    }
  };

  /**
   * @param aParent Composite - родительская компонента
   * @param aText String - надпись на кнопке
   * @param aCmdGwid Gwid - конкретный ИД команды
   * @param aTsContext ITsGuiContext - GUI контекст
   */
  public CmdPushButton( Composite aParent, String aText, Gwid aCmdGwid, IOptionSet aCmdArgs,
      ITsGuiContext aTsContext ) {
    button = new Button( aParent, SWT.PUSH );
    button.setText( aText );

    cmdName = getCommandName( aCmdGwid, aTsContext );
    button.setToolTipText( "Послать команду: " + cmdName );
    cmdGwid = aCmdGwid;
    cmdArgs = aCmdArgs;
    tsContext = aTsContext;

    button.addSelectionListener( new SelectionAdapter() {

      @Override
      public void widgetSelected( SelectionEvent aEvent ) {
        sendCommand();
      }
    } );
  }

  // ------------------------------------------------------------------------------------
  // ITsContextable
  //

  @Override
  public ITsContext tsContext() {
    return tsContext;
  }

  // ------------------------------------------------------------------------------------
  // API
  //
  /**
   * Возвращает конкретный идентификатор команды.
   *
   * @return Gwid - конкретный идентификатор команды
   */
  public Gwid commandGwid() {
    return cmdGwid;
  }

  /**
   * Возвращает {@link Button} SWT кнопку.
   *
   * @return Button - SWT кнопка
   */
  public Button button() {
    return button;
  }

  // ------------------------------------------------------------------------------------
  // Implementation
  //

  String commandName() {
    return cmdName;
  }

  private String getCommandName( Gwid aCmdGwid, ITsGuiContext aCtx ) {
    ISkCoreApi coreApi = aCtx.get( ISkConnectionSupplier.class ).defConn().coreApi();
    ISkClassInfo classInfo = coreApi.sysdescr().findClassInfo( aCmdGwid.classId() );
    if( classInfo != null ) {
      IDtoCmdInfo cmdInfo = classInfo.cmds().list().findByKey( aCmdGwid.propId() );
      if( cmdInfo != null ) {
        if( cmdInfo.nmName() != null && !cmdInfo.nmName().isBlank() ) {
          return cmdInfo.nmName();
        }
      }
    }
    return aCmdGwid.propId();
  }

  void sendCommand() {
    button.setEnabled( false );
    button.setToolTipText( "Команда в процессе выполнения" );

    ISkCoreApi coreApi = tsContext().get( ISkConnectionSupplier.class ).defConn().coreApi();
    ISkCommandService cmdService = coreApi.cmdService();

    // GwidList gwil = new GwidList();
    // gwil.add( commandGwid() );
    // cmdService.registerExecutor( commandExecutor, gwil );

    ISkCommand cmd = cmdService.sendCommand( commandGwid(), new Skid( ISkUser.CLASS_ID, "root" ), cmdArgs );
    cmd.stateEventer().addListener( commandListener );

  }
}
