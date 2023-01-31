package ru.toxsoft.mcc.ws.mnemos.app.dialogs;

import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;

import ru.toxsoft.mcc.ws.mnemos.app.controls.*;

public class PanelAnalogRegulator
    extends AbstractMccDialogPanel {

  public PanelAnalogRegulator( Shell aParent, MccDialogContext aDialogContext ) {
    super( aParent, aDialogContext );
    init();
    dataProvider().start();
  }

  void init() {

    // for( IDtoRtdataInfo dataInfo : dialogContext().skObject().classInfo().rtdata().list() ) {
    // System.out.println( dataInfo.id() + " " + dataInfo.nmName() );
    // }
    //
    // System.out.println( "-----------------------------------------------------" );
    //
    // for( IDtoCmdInfo cmdInfo : dialogContext().skObject().classInfo().cmds().list() ) {
    // System.out.println( cmdInfo.id() + " " + cmdInfo.nmName() );
    // }

    GridLayout gl = createGridLayout( 3, false );
    setLayout( gl );

    GridData gd = new GridData();
    gd.widthHint = 80;

    MccRtBooleanLabel rtLabel;

    CLabel l = new CLabel( this, SWT.CENTER );
    l.setText( dataInfo( "rtdCurrentValue" ).nmName() );

    MccRtLabel rtl = createRtLabel( this, SWT.BORDER, "rtdCurrentValue", tsContext() );
    rtl.getControl().setLayoutData( gd );

    l = new CLabel( this, SWT.CENTER );
    String strUnit = dialogContext().skObject().attrs().getStr( "atrMeasureValue" );
    if( strUnit.isBlank() ) {
      strUnit = "ед.изм.";
    }
    l.setText( strUnit );

    MccRtTextEditor textEditor;

    l = new CLabel( this, SWT.CENTER );
    l.setText( dataInfo( "rtdTask" ).nmName() );
    textEditor = createRtTextEditor( "rtdTask", "cmdTask" );
    textEditor.createControl( this ).setLayoutData( new GridData( SWT.FILL, SWT.CENTER, true, false, 2, 1 ) );

    l = new CLabel( this, SWT.CENTER );
    l.setText( dataInfo( "rtdCurrentError" ).nmName() );

    rtl = createRtLabel( this, SWT.BORDER, "rtdCurrentError", tsContext() );
    rtl.getControl().setLayoutData( new GridData( SWT.FILL, SWT.CENTER, true, false, 2, 1 ) );

    l = new CLabel( this, SWT.CENTER );
    l.setText( dataInfo( "rtdDead" ).nmName() );
    textEditor = createRtTextEditor( "rtdDead", "cmdDead" );
    textEditor.createControl( this ).setLayoutData( new GridData( SWT.FILL, SWT.CENTER, true, false, 2, 1 ) );

    l = new CLabel( this, SWT.CENTER );
    l.setText( dataInfo( "rtdKp" ).nmName() );
    textEditor = createRtTextEditor( "rtdKp", "cmdKp" );
    textEditor.createControl( this ).setLayoutData( new GridData( SWT.FILL, SWT.CENTER, true, false, 2, 1 ) );

    l = new CLabel( this, SWT.CENTER );
    l.setText( dataInfo( "rtdTi" ).nmName() );
    textEditor = createRtTextEditor( "rtdTi", "cmdTi" );
    textEditor.createControl( this ).setLayoutData( new GridData( SWT.FILL, SWT.CENTER, true, false, 2, 1 ) );

    l = new CLabel( this, SWT.CENTER );
    l.setText( dataInfo( "rtdTd" ).nmName() );
    textEditor = createRtTextEditor( "rtdTd", "cmdTd" );
    textEditor.createControl( this ).setLayoutData( new GridData( SWT.FILL, SWT.CENTER, true, false, 2, 1 ) );

    MccCheckCmdButton checkCmd;
    checkCmd = createCheckCmdButton( this, "cmdEnKp", "rtdEnKp", true );
    checkCmd.getControl().setLayoutData( new GridData( SWT.FILL, SWT.CENTER, true, false, 3, 1 ) );

    checkCmd = createCheckCmdButton( this, "cmdEnKi", "rtdEnKi", true );
    checkCmd.getControl().setLayoutData( new GridData( SWT.FILL, SWT.CENTER, true, false, 3, 1 ) );

    checkCmd = createCheckCmdButton( this, "cmdEnKd", "rtdEnKd", true );
    checkCmd.getControl().setLayoutData( new GridData( SWT.FILL, SWT.CENTER, true, false, 3, 1 ) );

    l = new CLabel( this, SWT.CENTER );
    l.setText( dataInfo( "rtdFullTime" ).nmName() );
    textEditor = createRtTextEditor( "rtdFullTime", "cmdFullTime" );
    textEditor.createControl( this ).setLayoutData( new GridData( SWT.FILL, SWT.CENTER, true, false, 2, 1 ) );

    l = new CLabel( this, SWT.CENTER );
    l.setText( dataInfo( "rtdMax" ).nmName() );
    textEditor = createRtTextEditor( "rtdMax", "cmdMax" );
    textEditor.createControl( this ).setLayoutData( new GridData( SWT.FILL, SWT.CENTER, true, false, 2, 1 ) );

    l = new CLabel( this, SWT.CENTER );
    l.setText( dataInfo( "rtdMin" ).nmName() );
    textEditor = createRtTextEditor( "rtdMin", "cmdMin" );
    textEditor.createControl( this ).setLayoutData( new GridData( SWT.FILL, SWT.CENTER, true, false, 2, 1 ) );

    checkCmd = createCheckCmdButton( this, "cmdEnable", "rtdEnable", true );
    checkCmd.getControl().setLayoutData( new GridData( SWT.FILL, SWT.CENTER, true, false, 3, 1 ) );
  }

  /**
   * Показывает панель блокировок.
   *
   * @param aContext MccDialogContext - контекст диалога
   */
  public static void showDialog( MccDialogContext aContext ) {
    ITsGuiContext ctx = aContext.tsContext();
    Shell shell = ctx.get( Shell.class ).getShell();

    MccDialogWindow wnd = new MccDialogWindow( shell, aContext.skObject().nmName() );
    PanelAnalogRegulator panel = new PanelAnalogRegulator( wnd.shell(), aContext );
    panel.layout();
    wnd.open();
  }

}
