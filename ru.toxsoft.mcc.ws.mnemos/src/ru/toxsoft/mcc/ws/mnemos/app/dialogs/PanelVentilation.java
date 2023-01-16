package ru.toxsoft.mcc.ws.mnemos.app.dialogs;

import static ru.toxsoft.mcc.ws.mnemos.IMccWsMnemosConstants.*;

import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;

import ru.toxsoft.mcc.ws.mnemos.app.controls.*;

public class PanelVentilation
    extends AbstractMccDialogPanel {

  public PanelVentilation( Shell aParent, MccDialogContext aDialogContext ) {
    super( aParent, aDialogContext );
    init();
    dataProvider().start();
  }

  void init() {
    GridLayout gl = createGridLayout( 2, false );
    setLayout( gl );

    GridData gd = new GridData();
    gd.widthHint = 80;

    createRtBooleanLabel( this, "rtdAutoBlow", ICONID_GRAY_LAMP, ICONID_GREEN_LAMP ); //$NON-NLS-1$
    createRtBooleanLabel( this, "rtdBlowCmplt", ICONID_GRAY_LAMP, ICONID_GREEN_LAMP ); //$NON-NLS-1$

    MccRtTextEditor textEditor;

    CLabel l = new CLabel( this, SWT.CENTER );
    l.setText( dataInfo( "rtdBlowTime" ).nmName() );
    textEditor = createRtTextEditor( "rtdBlowTime", "cmdBlowTime" );
    textEditor.createControl( this ).setLayoutData( new GridData( SWT.FILL, SWT.CENTER, true, false ) );
    // textEditor.createControl( this ).setLayoutData( new GridData( SWT.FILL, SWT.CENTER, true, false, 2, 1 ) );

    l = new CLabel( this, SWT.CENTER );
    l.setText( dataInfo( "rtdTimeWaitAfterVentStop" ).nmName() );
    textEditor = createRtTextEditor( "rtdTimeWaitAfterVentStop", "cmdTimeWaitAfterVentStop" );
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
    PanelVentilation panel = new PanelVentilation( wnd.shell(), aContext );
    panel.layout();
    wnd.open();
  }

}
