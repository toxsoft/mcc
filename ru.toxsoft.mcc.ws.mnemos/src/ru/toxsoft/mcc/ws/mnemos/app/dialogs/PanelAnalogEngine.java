package ru.toxsoft.mcc.ws.mnemos.app.dialogs;

import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.dialogs.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.gw.gwid.*;
import org.toxsoft.uskat.core.api.objserv.*;

import ru.toxsoft.mcc.ws.mnemos.app.controls.*;
import ru.toxsoft.mcc.ws.mnemos.app.utils.*;

/**
 * Панель свойств аналогового сигнала.
 * <p>
 *
 * @author vs
 */
public class PanelAnalogEngine
    extends AbstractMccDialogPanel {

  /**
   * ИД класса система управления
   */
  public static String CLSID_CTRL_SYSTEM = "mcc.CtrlSystem"; //$NON-NLS-1$
  /**
   * ИД объекта класса система управления
   */
  public static String OBJID_CTRL_SYSTEM = "n2CtrlSystem";   //$NON-NLS-1$

  private final ISkObject skObject;

  MccCommandSender commandSender;

  boolean stopOpen  = false;
  boolean stopClose = false;

  Button btnStop;

  /**
   * ИД команды задания для дроссельной заслонки
   */
  private final Gwid cmdGwid;

  protected PanelAnalogEngine( Shell aParent, MccDialogContext aDlgContext ) {
    super( aParent, aDlgContext );
    skObject = aDlgContext.skObject();
    init();

    cmdGwid = Gwid.createCmd( "mcc.AnalogEngine", "n2AE_Dz", "cmdTaskDz" ); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

    commandSender = new MccCommandSender( coreApi() );
    commandSender.eventer().addListener( aSource -> {
      String errStr = commandSender.errorString();
      if( errStr != null && !errStr.isBlank() ) {
        TsDialogUtils.error( getShell(), errStr );
      }
    } );

    dataProvider().start();
  }

  void init() {

    GridLayout layout = new GridLayout( 1, false );
    setLayout( layout );

    MccControlModeComponent modeComp = createControlModeComponent( this, skObject, tsContext() );
    modeComp.getControl().setLayoutData( new GridData( SWT.FILL, SWT.TOP, true, false ) );

    Gwid gwid = Gwid.createRtdata( "mcc.AnalogInput", "n2AI_DDZ", MccAnalogInputControl.DI_CURRENT_VALUE );
    // MccAnalogInputControl aiControl = new MccAnalogInputControl( gwid, tsContext(), null );
    // Control ctrl = aiControl.createControl( this, SWT.BORDER );
    // FontInfo fi = new FontInfo( "Arial", 24, true, false ); //$NON-NLS-1$
    // ctrl.setFont( fontManager().getFont( fi ) );
    GridData gd = new GridData( SWT.CENTER, SWT.FILL, false, true, 1, 2 );
    gd.widthHint = 130;
    gd.minimumWidth = 130;
    // ctrl.setLayoutData( gd );
    // dataProvider().addDataConsumer( aiControl );

    Composite c = new Composite( this, SWT.NONE );
    c.setLayout( createGridLayout( 2, false ) );
    c.setLayoutData( new GridData( SWT.FILL, SWT.TOP, true, false ) );

    CLabel l = new CLabel( c, SWT.CENTER );
    String name = dataName( gwid ) + ":  ";
    l.setText( name );

    MccRtLabel rtlDz = createRtLabel( c, SWT.BORDER, gwid, tsContext() );
    gd = new GridData( SWT.LEFT, SWT.CENTER, false, false );
    gd.widthHint = 130;
    rtlDz.getControl().setLayoutData( gd );

    createControlPanel( this );

    Group pidGroup = createGroup( this, "Регулятор", 3, false );
    // new Group( this, SWT.NONE );
    // pidGroup.setText( "Регулятор" );
    // pidGroup.setLayoutData( new GridData( SWT.FILL, SWT.FILL, true, false ) );
    // pidGroup.setLayout( createGridLayout( 3, false ) );

    Gwid pidGwid = Gwid.createRtdata( "mcc.AnalogReg", "n2AnalogReg_DZ", "rtdCurrentValue" );
    ISkObject pidObj = coreApi().objService().find( pidGwid.skid() );

    l = new CLabel( pidGroup, SWT.CENTER );
    name = dataName( pidGwid ) + ":  ";
    l.setText( name );

    MccRtLabel rtl = createRtLabel( pidGroup, SWT.BORDER, pidGwid, tsContext() );
    gd = new GridData( SWT.LEFT, SWT.CENTER, false, false );
    gd.widthHint = 130;
    rtl.getControl().setLayoutData( gd );

    l = new CLabel( pidGroup, SWT.CENTER );
    String strUnit = dialogContext().skObject().attrs().getStr( "atrMeasureValue" );
    if( strUnit.isBlank() ) {
      strUnit = "ед.изм.";
    }
    l.setText( strUnit );

    MccRtTextEditor textEditor;

    l = new CLabel( pidGroup, SWT.CENTER );
    name = dataName( Gwid.createRtdata( "mcc.AnalogReg", "n2AnalogReg_DZ", "rtdTask" ) ) + ":  ";
    l.setText( name );
    textEditor = createRtTextEditor( pidObj, "rtdTask", "cmdTask" );
    textEditor.createControl( pidGroup ).setLayoutData( new GridData( SWT.FILL, SWT.CENTER, true, false, 2, 1 ) );

    MccCheckCmdButton checkCmd = createCheckCmdButton( pidGroup, pidObj, "cmdEnable", "rtdEnable", true );
    checkCmd.getControl().setLayoutData( new GridData( SWT.FILL, SWT.CENTER, true, false, 3, 1 ) );

    // Composite buttonBar = new Composite( pidGroup, SWT.NONE );
    // GridLayout gl = new GridLayout( 4, false );
    // gl.marginHeight = 0;
    // gl.verticalSpacing = 0;
    // gl.marginTop = 6;
    // gl.marginBottom = 6;
    // gl.marginLeft = 0;
    // buttonBar.setLayout( gl );
    // buttonBar.setLayoutData( new GridData( SWT.FILL, SWT.CENTER, true, false, 3, 1 ) );
    //
    // l = new CLabel( buttonBar, SWT.CENTER );
    // l.setText( "Управление: " );
    //
    // Button btnArm = new Button( buttonBar, SWT.PUSH );
    // btnArm.setText( "АРМ" );
    // gd = new GridData();
    // gd.widthHint = 80;
    // btnArm.setLayoutData( gd );
    // btnArm.addSelectionListener( new SelectionAdapter() {
    //
    // @Override
    // public void widgetSelected( SelectionEvent aE ) {
    // // if( btnArm.getSelection() ) {
    // Gwid cmdg = Gwid.createCmd( CLSID_CTRL_SYSTEM, OBJID_CTRL_SYSTEM, "cmdSetApwCtrl" ); //$NON-NLS-1$
    // if( !commandSender.sendCommand( cmdg, AvUtils.avBool( true ) ) ) {
    // // internalUpdate();
    // TsDialogUtils.error( getShell(), commandSender.errorString() );
    // }
    // // }
    // }
    // } );
    //
    // Button btnAuto = new Button( buttonBar, SWT.PUSH );
    // btnAuto.setText( "Автомат" );
    // gd = new GridData();
    // gd.widthHint = 80;
    // btnAuto.setLayoutData( gd );
    // btnAuto.addSelectionListener( new SelectionAdapter() {
    //
    // @Override
    // public void widgetSelected( SelectionEvent aE ) {
    // // if( btnAuto.getSelection() ) {
    // Gwid cmdg = Gwid.createCmd( CLSID_CTRL_SYSTEM, OBJID_CTRL_SYSTEM, "cmdSetAutoCtrl" ); //$NON-NLS-1$
    // if( !commandSender.sendCommand( cmdg, AvUtils.avBool( true ) ) ) {
    // // internalUpdate();
    // TsDialogUtils.error( getShell(), commandSender.errorString() );
    // }
    // // }
    // }
    // } );

  }

  void createControlPanel( Composite aParent ) {
    Composite comp = new Composite( aParent, SWT.NONE );
    comp.setLayout( new GridLayout( 4, false ) );

    GridData gd = new GridData();
    gd.widthHint = 80;

    CLabel l = new CLabel( comp, SWT.CENTER );
    l.setText( "Задание ДЗ:" );

    createRtLabel( comp, SWT.CENTER | SWT.BORDER, "rtdTaskDz", tsContext() ).getControl().setLayoutData( gd );

    Button btn = new Button( comp, SWT.PUSH );
    btn.setText( " - " );
    btn.addSelectionListener( new SelectionAdapter() {

      @Override
      public void widgetSelected( SelectionEvent aE ) {
        Gwid gwid = Gwid.createRtdata( "mcc.AnalogEngine", "n2AE_Dz", "rtdTaskDz" );
        IAtomicValue taskVal = MccRtUtils.readRtData( gwid, coreApi() );
        gwid = Gwid.createRtdata( "mcc.AnalogEngine", "n2AE_Dz", "rtdStepControl" );
        IAtomicValue stepVal = MccRtUtils.readRtData( gwid, coreApi() );
        if( taskVal.isAssigned() && stepVal.isAssigned() ) {
          double val = taskVal.asDouble() - stepVal.asDouble();
          if( !commandSender.sendCommand( cmdGwid, AvUtils.avFloat( val ) ) ) {
            TsDialogUtils.error( getShell(), commandSender.errorString() );
            return;
          }
        }
      }
    } );

    btn = new Button( comp, SWT.PUSH );
    btn.setText( "+" );
    btn.addSelectionListener( new SelectionAdapter() {

      @Override
      public void widgetSelected( SelectionEvent aE ) {
        Gwid gwid = Gwid.createRtdata( "mcc.AnalogEngine", "n2AE_Dz", "rtdTaskDz" );
        IAtomicValue taskVal = MccRtUtils.readRtData( gwid, coreApi() );
        gwid = Gwid.createRtdata( "mcc.AnalogEngine", "n2AE_Dz", "rtdStepControl" );
        IAtomicValue stepVal = MccRtUtils.readRtData( gwid, coreApi() );
        if( taskVal.isAssigned() && stepVal.isAssigned() ) {
          double val = taskVal.asDouble() + stepVal.asDouble();
          if( !commandSender.sendCommand( cmdGwid, AvUtils.avFloat( val ) ) ) {
            TsDialogUtils.error( getShell(), commandSender.errorString() );
            return;
          }
        }
      }
    } );

    l = new CLabel( comp, SWT.CENTER );
    l.setText( "Шаг изменения:" );

    Gwid stepControlGwid = Gwid.createRtdata( "mcc.AnalogEngine", "n2AE_Dz", "rtdStepControl" );

    IEditingFinisher finisher = aNewValue -> {
      MccRtUtils.writeRtData( aNewValue, stepControlGwid, coreApi() );
      return true;
    };

    MccTextEditor stepEditor = new MccTextEditor( finisher, EAtomicType.FLOATING, tsContext() );
    // MccRtTextEditor stepEditor = createRtTextEditor( "rtdStepControl", "cmdStepControl" );
    gd = new GridData( SWT.FILL, SWT.CENTER, true, false, 3, 1 );
    stepEditor.createControl( comp ).setLayoutData( gd );

    IAtomicValue v = MccRtUtils.readRtData( stepControlGwid, coreApi() );
    stepEditor.setValue( v );

  }

  /**
   * Показывает диалог реверсивного двигателя.
   *
   * @param aContext MccDialogContext - контекст диалога
   */
  public static void showDialog( MccDialogContext aContext ) {
    ITsGuiContext ctx = aContext.tsContext();
    Shell shell = ctx.get( Shell.class ).getShell();

    MccDialogWindow wnd = new MccDialogWindow( shell, aContext.skObject().readableName() );
    PanelAnalogEngine panel = new PanelAnalogEngine( wnd.shell(), aContext );
    panel.layout();
    wnd.open();
  }

}
