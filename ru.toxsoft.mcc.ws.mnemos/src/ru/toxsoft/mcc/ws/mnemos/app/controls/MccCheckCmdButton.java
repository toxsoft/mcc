package ru.toxsoft.mcc.ws.mnemos.app.controls;

import static ru.toxsoft.mcc.ws.mnemos.app.controls.IVjResources.*;

import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.dialogs.*;
import org.toxsoft.core.tsgui.graphics.colors.*;
import org.toxsoft.core.tsgui.graphics.icons.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.bricks.events.change.*;
import org.toxsoft.core.tslib.gw.gwid.*;
import org.toxsoft.core.tslib.gw.skid.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.tslib.utils.logs.impl.*;
import org.toxsoft.uskat.base.gui.conn.*;
import org.toxsoft.uskat.core.*;
import org.toxsoft.uskat.core.api.cmdserv.*;
import org.toxsoft.uskat.core.api.sysdescr.*;
import org.toxsoft.uskat.core.api.sysdescr.dto.*;
import org.toxsoft.uskat.core.api.users.*;

import ru.toxsoft.mcc.ws.mnemos.app.*;
import ru.toxsoft.mcc.ws.mnemos.app.rt.*;

/**
 * Копка в виде checkbox для посылки команды для проекта МосКокс.
 * <p>
 *
 * @author vs
 */
public class MccCheckCmdButton
    implements IRtDataConsumer, ITsGuiContextable {

  private final Gwid cmdGwid;

  private final Gwid dataGwid;

  private final ITsGuiContext tsContext;

  private String name = TsLibUtils.EMPTY_STRING;

  private String description = TsLibUtils.EMPTY_STRING;

  Button checkbox = null;

  IAtomicValue        value = IAtomicValue.NULL;
  private final Image warnImage;

  private final Color grayColor;

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
        TsDialogUtils.error( getShell(), cmd.state().state().description() );
      }
    }
    checkbox.update();
  };

  /**
   * Конструктор.
   *
   * @param aCmdGwid Gwid - ИД посылаемой команды
   * @param aDataGwid Gwid - ИД данного, по которому ставится галочка
   * @param aTsContext ITsGuiContext - соответствующий контекст
   */
  MccCheckCmdButton( Gwid aCmdGwid, Gwid aDataGwid, ITsGuiContext aTsContext ) {
    tsContext = aTsContext;
    cmdGwid = aCmdGwid;
    dataGwid = aDataGwid;
    grayColor = colorManager().getColor( ETsColor.DARK_GRAY );
    warnImage = iconManager().loadStdIcon( ITsStdIconIds.ICONID_DIALOG_WARNING, EIconSize.IS_16X16 );
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Создает контроль в понятиях SWT.
   *
   * @param aParent Composite - родительская компонента
   * @param aSwtStyle int - стиль SWT копоненты
   * @return Button - контроль в понятиях SWT
   */
  public Button createControl( Composite aParent, int aSwtStyle ) {
    checkbox = new Button( aParent, SWT.CHECK | aSwtStyle );
    checkbox.setText( name );
    checkbox.addPaintListener( aE -> {
      if( !value.isAssigned() ) {
        aE.gc.setAlpha( 160 );
        aE.gc.setBackground( grayColor );
        Point size = checkbox.getSize();
        aE.gc.fillRectangle( 0, 0, size.x, size.y );
        aE.gc.setAlpha( 255 );
        aE.gc.drawImage( warnImage, 0, size.y - 16 );
        checkbox.setToolTipText( STR_ERR_VALUE_NOT_SET );
      }
      else {
        checkbox.setToolTipText( description );
      }
    } );

    checkbox.addSelectionListener( new SelectionAdapter() {

      @Override
      public void widgetSelected( SelectionEvent aE ) {
        boolean newVal = checkbox.getSelection();
        if( !sendCommand( checkbox.getSelection() ) ) {
          checkbox.setSelection( !newVal );
        }
      }
    } );

    update();
    return checkbox;
  }

  /**
   * Возвращает созданный контроль или null если он еще не был создан.
   *
   * @return Button - созданный контроль или null
   */
  public Button getControl() {
    return checkbox;
  }

  /**
   * Задает текст, который будет отображаться на контроле.
   *
   * @param aName String - текст, который будет отображаться на контроле
   */
  public void setName( String aName ) {
    name = aName;
    if( checkbox != null ) {
      checkbox.setText( name );
    }
  }

  /**
   * Задает текст всплывающей подсказки.
   *
   * @param aDescription String - текст всплывающей подсказки
   */
  public void setDescription( String aDescription ) {
    description = aDescription;
    if( checkbox != null ) {
      checkbox.setToolTipText( aDescription );
    }
  }

  // ------------------------------------------------------------------------------------
  // IStridable
  //

  @Override
  public String id() {
    return cmdGwid.classId() + "." + cmdGwid.strid() + "." + cmdGwid.propId(); //$NON-NLS-1$//$NON-NLS-2$
  }

  @Override
  public String nmName() {
    return name;
  }

  @Override
  public String description() {
    return description;
  }

  // ------------------------------------------------------------------------------------
  // ITsGuiContextable
  //

  @Override
  public ITsGuiContext tsContext() {
    return tsContext;
  }

  // ------------------------------------------------------------------------------------
  // IRtDataConsumer
  //

  @Override
  public IGwidList listNeededGwids() {
    GwidList gl = new GwidList( dataGwid );
    return gl;
  }

  @Override
  public void setValues( Gwid[] aGwids, IAtomicValue[] aValues, int aCount ) {
    value = aValues[0];
    update();
  }

  // ------------------------------------------------------------------------------------
  // Implementation
  //

  void update() {
    if( value.isAssigned() ) {
      checkbox.setSelection( value.asBool() );
    }
    checkbox.redraw();
  }

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
    return true;
  }

}
