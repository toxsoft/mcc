package ru.toxsoft.mcc.ws.mnemos.app.dialogs;

import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;

import ru.toxsoft.mcc.ws.mnemos.app.controls.*;

public class PanelAutoStart
    extends AbstractMccDialogPanel {

  public PanelAutoStart( Shell aParent, MccDialogContext aDialogContext ) {
    super( aParent, aDialogContext );
    init();
    dataProvider().start();
  }

  void init() {
    GridLayout gl = createGridLayout( 2, false );
    setLayout( gl );

    GridData gd = new GridData();
    gd.widthHint = 80;

    MccRtTextEditor textEditor;

    CLabel l = new CLabel( this, SWT.CENTER );
    l.setText( dataInfo( "rtdGEDTime" ).nmName() );
    textEditor = createRtTextEditor( "rtdGEDTime", "cmdGEDTime" );
    textEditor.createControl( this ).setLayoutData( new GridData( SWT.FILL, SWT.CENTER, true, false ) );
    // textEditor.createControl( this ).setLayoutData( new GridData( SWT.FILL, SWT.CENTER, true, false, 2, 1 ) );

    l = new CLabel( this, SWT.CENTER );
    l.setText( dataInfo( "rtdCaseHeatTarget" ).nmName() );
    textEditor = createRtTextEditor( "rtdCaseHeatTarget", "cmdCaseHeatTarget" );
    textEditor.createControl( this ).setLayoutData( new GridData( SWT.FILL, SWT.CENTER, true, false ) );

    l = new CLabel( this, SWT.CENTER );
    l.setText( dataInfo( "rtdSetOilTemp" ).nmName() );
    textEditor = createRtTextEditor( "rtdSetOilTemp", "cmdSetOilTemp" );
    textEditor.createControl( this ).setLayoutData( new GridData( SWT.FILL, SWT.CENTER, true, false ) );
  }

  /**
   * Показывает панель блокировок.
   *
   * @param aContext MccDialogContext - контекст диалога
   */
  public static void showDialog( MccDialogContext aContext ) {
    ITsGuiContext ctx = aContext.tsContext();
    Shell shell = ctx.get( Shell.class ).getShell();

    // MccDialogWindow wnd = new MccDialogWindow( shell, aContext.skObject().nmName() );
    MccDialogWindow wnd = new MccDialogWindow( shell, "Вентиляция" );
    PanelAutoStart panel = new PanelAutoStart( wnd.shell(), aContext );
    panel.layout();
    wnd.open();
  }

}
