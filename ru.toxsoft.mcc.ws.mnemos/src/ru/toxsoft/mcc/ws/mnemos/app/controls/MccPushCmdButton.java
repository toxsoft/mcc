package ru.toxsoft.mcc.ws.mnemos.app.controls;

import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.dialogs.*;
import org.toxsoft.core.tsgui.graphics.cursors.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.gw.gwid.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.uskat.core.*;

import ru.toxsoft.mcc.ws.mnemos.app.rt.*;

/**
 * Копка в виде checkbox для посылки команды для проекта МосКокс.
 * <p>
 *
 * @author vs
 */
public class MccPushCmdButton
    implements IRtDataConsumer, ITsGuiContextable {

  private final Gwid cmdGwid;

  private final ITsGuiContext tsContext;

  private String name = TsLibUtils.EMPTY_STRING;

  private String description = TsLibUtils.EMPTY_STRING;

  Button button = null;

  MccCommandSender commandSender;

  /**
   * Конструктор.
   *
   * @param aCmdGwid Gwid - ИД посылаемой команды
   * @param aCoreApi API сервера
   * @param aTsContext ITsGuiContext - соответствующий контекст
   */
  public MccPushCmdButton( Gwid aCmdGwid, ISkCoreApi aCoreApi, ITsGuiContext aTsContext ) {
    tsContext = aTsContext;
    cmdGwid = aCmdGwid;
    commandSender = new MccCommandSender( aCmdGwid, aCoreApi );
    commandSender.eventer().addListener( aSource -> {
      button.setEnabled( true );
      button.setCursor( null );
      String errStr = commandSender.errorString();
      if( errStr != null && !errStr.isBlank() ) {
        TsDialogUtils.error( getShell(), errStr );
      }
    } );
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
    button = new Button( aParent, SWT.PUSH | aSwtStyle );

    button.addSelectionListener( new SelectionAdapter() {

      @Override
      public void widgetSelected( SelectionEvent aE ) {
        if( !commandSender.sendCommand( true ) ) {
          TsDialogUtils.error( getShell(), commandSender.errorString() );
          return;
        }
        button.setEnabled( false );
        button.setCursor( cursorManager().getCursor( ECursorType.WAIT ) );
      }
    } );

    return button;
  }

  /**
   * Возвращает созданный контроль или null если он еще не был создан.
   *
   * @return Button - созданный контроль или null
   */
  public Button getControl() {
    return button;
  }

  /**
   * Задает текст, который будет отображаться на контроле.
   *
   * @param aName String - текст, который будет отображаться на контроле
   */
  public void setName( String aName ) {
    name = aName;
    if( button != null ) {
      button.setText( name );
    }
  }

  /**
   * Задает текст всплывающей подсказки.
   *
   * @param aDescription String - текст всплывающей подсказки
   */
  public void setDescription( String aDescription ) {
    description = aDescription;
    if( button != null ) {
      button.setToolTipText( aDescription );
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
    return IGwidList.EMPTY;
  }

  @Override
  public void setValues( Gwid[] aGwids, IAtomicValue[] aValues, int aCount ) {
    // nop
  }

}
