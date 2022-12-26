package ru.toxsoft.mcc.ws.mnemos.app.dialogs;

import static ru.toxsoft.mcc.ws.mnemos.IMccWsMnemosConstants.*;
import static ru.toxsoft.mcc.ws.mnemos.app.dialogs.IVjResources.*;

import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.dialogs.*;
import org.toxsoft.core.tsgui.graphics.icons.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.gw.gwid.*;
import org.toxsoft.uskat.core.api.objserv.*;

import ru.toxsoft.mcc.ws.mnemos.app.controls.*;
import ru.toxsoft.mcc.ws.mnemos.app.rt.*;
import ru.toxsoft.mcc.ws.mnemos.app.rt.chart.data_aliases.*;
import ru.toxsoft.mcc.ws.mnemos.app.utils.*;

/**
 * Панель свойств высоковольтного выключателя.
 * <p>
 *
 * @author vs
 */
public class PanelMainSwitch
    extends AbstractMccDialogPanel {

  private final ISkObject skObject;

  final MccCommandSender cmdSender;

  SingleDataConsumer switchOff;

  RtDataAliasHelper aliasHelper;

  protected PanelMainSwitch( Shell aParent, MccDialogContext aDlgContext ) {
    super( aParent, aDlgContext );
    skObject = aDlgContext.skObject();
    init();
    cmdSender = new MccCommandSender( coreApi() );
    cmdSender.eventer().addListener( aSource -> {
      String errStr = cmdSender.errorString();
      if( errStr != null && !errStr.isBlank() ) {
        TsDialogUtils.error( getShell(), errStr );
      }
    } );
    dataProvider().start();
  }

  void init() {

    aliasHelper = new RtDataAliasHelper( skConn() );

    GridLayout layout = createGridLayout( 1, false );
    setLayout( layout );

    MccControlModeComponent modeComp = createControlModeComponent( this, skObject, tsContext() );
    modeComp.getControl().setLayoutData( new GridData( SWT.FILL, SWT.TOP, true, false ) );

    Group comp = createGroup( this, "Состояние", 2, false );
    comp.setLayout( createGridLayout( 2, true ) );

    switchOff = new SingleDataConsumer( Gwid.createRtdata( skObject.classId(), skObject.strid(), "rtdOff" ) );
    dataProvider().addDataConsumer( switchOff );

    Gwid g1 = Gwid.createRtdata( skObject.classId(), skObject.strid(), "rtdAuxOn" );
    Gwid g2 = Gwid.createRtdata( skObject.classId(), skObject.strid(), "rtdAuxOff" );
    MccRtBooleanLabel2 bl;
    bl = new MccRtBooleanLabel2( g1, g2, ICONID_GRAY_LAMP, ICONID_GREEN_LAMP, EIconSize.IS_24X24, tsContext() );
    bl.createControl( comp, SWT.NONE ).setText( "Включен ДК" );
    dataProvider().addDataConsumer( bl );

    g1 = Gwid.createRtdata( skObject.classId(), skObject.strid(), "rtdOn" );
    g2 = Gwid.createRtdata( skObject.classId(), skObject.strid(), "rtdOff" );
    bl = new MccRtBooleanLabel2( g1, g2, ICONID_GRAY_LAMP, ICONID_GREEN_LAMP, EIconSize.IS_24X24, tsContext() );
    bl.createControl( comp, SWT.NONE ).setText( "Включен" );
    dataProvider().addDataConsumer( bl );

    createRtBooleanLabel( comp, "rtdReady2Start", ICONID_RED_LAMP, ICONID_GREEN_LAMP );
    createRtBooleanLabel( comp, "rtdSwitchOnFailure", ICONID_GRAY_LAMP, ICONID_RED_LAMP );
    createRtBooleanLabel( comp, "rtdSwitchOffFailure", ICONID_GRAY_LAMP, ICONID_RED_LAMP );

    comp = createGroup( this, "Блокировка пуска", 2, true );
    createRtBooleanLabel( comp, "rtdMainSwitchAlarm", ICONID_GRAY_LAMP, ICONID_RED_LAMP );
    createRtBooleanLabel( comp, "rtdEmergencyStop", ICONID_GRAY_LAMP, ICONID_RED_LAMP );
    Gwid gwid = Gwid.createRtdata( "mcc.DigInput", "n2DI_AirInGED_Norm", "rtdCurrentValue" );
    createRtLabel( comp, gwid, ICONID_RED_LAMP, ICONID_GREEN_LAMP );
    // createRtBooleanLabel( comp, gwid, ICONID_RED_LAMP, ICONID_GREEN_LAMP );
    createRtBooleanLabel( comp, "rtdAlarm", ICONID_GRAY_LAMP, ICONID_RED_LAMP );

    // Создадим кнопки управления
    Composite btnsHolder = new Composite( this, SWT.NONE );
    btnsHolder.setLayout( createGridLayout( 2, false ) );
    btnsHolder.setLayoutData( new GridData( SWT.FILL, SWT.TOP, true, false ) );

    Button btnStart = new Button( btnsHolder, SWT.PUSH );
    btnStart.setText( "ПУСК" );
    btnStart.addSelectionListener( new SelectionAdapter() {

      @Override
      public void widgetSelected( SelectionEvent aE ) {
        if( TsDialogUtils.askYesNoCancel( getShell(), "Запустить?" ) == ETsDialogCode.YES ) {
          Gwid cmdg = Gwid.createCmd( skObject.classId(), skObject.strid(), "cmdAwpStart" ); //$NON-NLS-1$
          if( !cmdSender.sendCommand( cmdg, AvUtils.avBool( true ) ) ) {
            TsDialogUtils.error( getShell(), cmdSender.errorString() );
          }
        }
      }
    } );

    Button btnStop = new Button( btnsHolder, SWT.PUSH );
    btnStop.setText( "СТОП" );
    btnStop.addSelectionListener( new SelectionAdapter() {

      @Override
      public void widgetSelected( SelectionEvent aE ) {
        if( TsDialogUtils.askYesNoCancel( getShell(), "Остановить?" ) == ETsDialogCode.YES ) {
          Gwid cmdg = Gwid.createCmd( skObject.classId(), skObject.strid(), "cmdAwpStop" ); //$NON-NLS-1$
          if( !cmdSender.sendCommand( cmdg, AvUtils.avBool( true ) ) ) {
            TsDialogUtils.error( getShell(), cmdSender.errorString() );
          }
        }
      }
    } );

    Composite bkPanel = new Composite( this, SWT.NONE );
    bkPanel.setLayout( new GridLayout( 2, false ) );
    createOperatingTimeGroup( bkPanel, false );

    Composite buttonBar = new Composite( this, SWT.NONE );
    buttonBar.setLayout( createGridLayout( 2, false ) );

    GridData gd = new GridData();
    gd.widthHint = 100;

    MccPushCmdButton btnConfirm = createPushCmdButton( buttonBar, "cmdConfirmation" ); //$NON-NLS-1$
    btnConfirm.getControl().setText( STR_CONFIRMATION );
    btnConfirm.getControl().setLayoutData( gd );

    Button btnSettins = new Button( buttonBar, SWT.PUSH );
    btnSettins.setText( STR_SETTINGS );
    btnSettins.setToolTipText( STR_INVOKE_SETTINGS_DLG );
    btnSettins.setLayoutData( gd );
    btnSettins.addSelectionListener( new SelectionAdapter() {

      @Override
      public void widgetSelected( SelectionEvent e ) {
        Point pl = getParent().getLocation();
        Point ps = getParent().getSize();
        PanelMainSwitchSettings.showDialog( pl.x + ps.x, pl.y, dialogContext() );
      }
    } );

  }

  /**
   * Показывает диалог Аналогового сигнала.
   *
   * @param aContext MccDialogContext - контекст диалога
   */
  public static void showDialog( MccDialogContext aContext ) {
    ITsGuiContext ctx = aContext.tsContext();
    Shell shell = ctx.get( Shell.class ).getShell();

    MccDialogWindow wnd = new MccDialogWindow( shell, aContext.skObject().readableName() );
    PanelMainSwitch panel = new PanelMainSwitch( wnd.shell(), aContext );
    panel.layout();
    wnd.open();
  }

  // ------------------------------------------------------------------------------------
  // Implementation
  //

  private MccRtBooleanLabel createRtLabel( Composite aParent, Gwid aGwid, String aFalseIconId, String aTrueIconId ) {
    MccRtBooleanLabel rtLabel = createRtBooleanLabel( aParent, aGwid, aFalseIconId, aTrueIconId );
    IDataNameAlias alias = aliasHelper.alias( aGwid );
    if( alias != null ) {
      rtLabel.setName( alias.title() );
      rtLabel.setDescription( alias.description() );
    }
    return rtLabel;
  }

}
