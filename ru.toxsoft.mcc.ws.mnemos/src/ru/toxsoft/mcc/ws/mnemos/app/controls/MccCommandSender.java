package ru.toxsoft.mcc.ws.mnemos.app.controls;

import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.bricks.events.change.*;
import org.toxsoft.core.tslib.gw.gwid.*;
import org.toxsoft.core.tslib.gw.skid.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.tslib.utils.logs.impl.*;
import org.toxsoft.uskat.core.*;
import org.toxsoft.uskat.core.api.cmdserv.*;
import org.toxsoft.uskat.core.api.sysdescr.*;
import org.toxsoft.uskat.core.api.sysdescr.dto.*;
import org.toxsoft.uskat.core.api.users.*;

import ru.toxsoft.mcc.ws.mnemos.app.*;

/**
 * "Посылатель" команд для проекта МосКокс.
 * <p>
 *
 * @author vs
 */
public class MccCommandSender {

  private final ISkCoreApi coreApi;

  private String errStr = TsLibUtils.EMPTY_STRING;

  private GenericChangeEventer eventer = null;

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
      if( cmd.isComplete() && cmd.state().state() != ESkCommandState.SUCCESS ) {
        errStr = cmd.state().state().description();
      }
      eventer.fireChangeEvent();
    }
  };

  /**
   * Конструктор.<br>
   *
   * @param aCoreApi ISkCoreApi - API сервера
   */
  public MccCommandSender( ISkCoreApi aCoreApi ) {
    coreApi = aCoreApi;
    eventer = new GenericChangeEventer( this );
  }

  /**
   * Посылает указанную команду без аргументов.<br>
   *
   * @param aCmdGwid Gwid - конкретный ИД команды
   * @return <b>true</b> - посылка команды прошла успешно (не означает, что команда выполнится с успехом)<br>
   *         <b>false</b> - не удалось послать команду
   */
  public boolean sendCommand( Gwid aCmdGwid ) {

    ISkCommandService cmdService = coreApi.cmdService();

    ISkCommand cmd = cmdService.sendCommand( aCmdGwid, new Skid( ISkUser.CLASS_ID, "root" ), IOptionSet.NULL );
    CmdUtils.logCommandHistory( cmd );
    errStr = CmdUtils.errorString( cmd );
    if( errStr != null ) {
      return false;
    }
    cmd.stateEventer().addListener( commandListener );
    return true;
  }

  /**
   * Посылает указанную команду с булевым аргументом.<br>
   *
   * @param aCmdGwid Gwid - конкретный ИД команды
   * @param aArg boolean - аргумент команды
   * @return <b>true</b> - посылка команды прошла успешно (не означает, что команда выполнится с успехом)<br>
   *         <b>false</b> - не удалось послать команду
   */
  public boolean sendCommand( Gwid aCmdGwid, boolean aArg ) {

    ISkCommandService cmdService = coreApi.cmdService();

    ISkClassInfo classInfo = coreApi.sysdescr().findClassInfo( aCmdGwid.classId() );
    IDtoCmdInfo cmdInfo = classInfo.cmds().list().getByKey( aCmdGwid.propId() );
    String argId = cmdInfo.argDefs().first().id();

    OptionSet cmdArgs = new OptionSet();
    cmdArgs.setValue( argId, AvUtils.avBool( aArg ) );
    ISkCommand cmd = cmdService.sendCommand( aCmdGwid, new Skid( ISkUser.CLASS_ID, "root" ), cmdArgs );
    CmdUtils.logCommandHistory( cmd );
    errStr = CmdUtils.errorString( cmd );
    if( errStr != null ) {
      return false;
    }
    cmd.stateEventer().addListener( commandListener );
    return true;
  }

  /**
   * Возвращает описание ошибки или <b>null</b> если ошибки не было.
   *
   * @return String - описание ошибки или <b>null</b> если ошибки не было
   */
  public String errorString() {
    return errStr;
  }

  /**
   * Возвращает помощника по работе с событием изменения состояния.<br>
   *
   * @return IGenericChangeEventer - помощник по работе с событием изменения состояния
   */
  public IGenericChangeEventer eventer() {
    return eventer;
  }

}
