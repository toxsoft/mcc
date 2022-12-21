package ru.toxsoft.mcc.ws.mnemos.app.dialogs;

import static ru.toxsoft.mcc.ws.mnemos.IMccWsMnemosConstants.*;

import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;

import ru.toxsoft.mcc.ws.mnemos.app.controls.*;

public class PanelTwoPositionsRegulator
    extends AbstractMccDialogPanel {

  public PanelTwoPositionsRegulator( Shell aParent, MccDialogContext aDialogContext ) {
    super( aParent, aDialogContext );
    init();
    dataProvider().start();
  }

  void init() {
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
    l.setText( dataInfo( "rtdSetPointOn" ).nmName() );
    textEditor = createRtTextEditor( "rtdSetPointOn", "cmdSetPointOn" );
    textEditor.createControl( this ).setLayoutData( new GridData( SWT.FILL, SWT.CENTER, true, false, 2, 1 ) );

    l = new CLabel( this, SWT.CENTER );
    l.setText( dataInfo( "rtdSetPointOff" ).nmName() );
    textEditor = createRtTextEditor( "rtdSetPointOff", "cmdSetPointOff" );
    textEditor.createControl( this ).setLayoutData( new GridData( SWT.FILL, SWT.CENTER, true, false, 2, 1 ) );

    MccCheckCmdButton checkCmd;
    checkCmd = createCheckCmdButton( this, "cmdAuto", "rtdAuto", true );
    checkCmd.getControl().setLayoutData( new GridData( SWT.FILL, SWT.CENTER, true, false, 3, 1 ) );

    createRtBooleanLabel( this, "rtdOn", ICONID_GRAY_LAMP, ICONID_GREEN_LAMP );
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
    PanelTwoPositionsRegulator panel = new PanelTwoPositionsRegulator( wnd.shell(), aContext );
    panel.layout();
    wnd.open();
  }

}
