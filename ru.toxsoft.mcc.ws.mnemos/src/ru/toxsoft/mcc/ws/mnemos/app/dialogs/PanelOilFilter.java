package ru.toxsoft.mcc.ws.mnemos.app.dialogs;

import static ru.toxsoft.mcc.ws.mnemos.IMccWsMnemosConstants.*;

import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;

import ru.toxsoft.mcc.ws.mnemos.app.controls.*;

public class PanelOilFilter
    extends AbstractMccDialogPanel {

  public PanelOilFilter( Shell aParent, MccDialogContext aDialogContext ) {
    super( aParent, aDialogContext );
    init();
    dataProvider().start();
  }

  void init() {
    GridLayout gl = createGridLayout( 3, false );
    setLayout( gl );

    GridData gd = new GridData();
    gd.widthHint = 80;

    MccRtBooleanLabel rtl = createRtBooleanLabel( this, "rtdOilFilterAlarm", ICONID_GREEN_LAMP, ICONID_RED_LAMP ); //$NON-NLS-1$
    rtl.getControl().setLayoutData( new GridData( SWT.FILL, SWT.CENTER, true, false, 3, 1 ) );

    MccRtTextEditor textEditor;

    CLabel l = new CLabel( this, SWT.CENTER );
    l.setText( dataInfo( "rtdOilPressDiff2On" ).nmName() ); //$NON-NLS-1$
    textEditor = createRtTextEditor( "rtdOilPressDiff2On", "cmdOilPressDiff2On" ); //$NON-NLS-1$ //$NON-NLS-2$
    textEditor.createControl( this ).setLayoutData( new GridData( SWT.FILL, SWT.CENTER, true, false, 2, 1 ) );

    l = new CLabel( this, SWT.CENTER );
    l.setText( dataInfo( "rtdOilPressDiff2Off" ).nmName() ); //$NON-NLS-1$
    textEditor = createRtTextEditor( "rtdOilPressDiff2Off", "cmdOilPressDiff2Off" ); //$NON-NLS-1$ //$NON-NLS-2$
    textEditor.createControl( this ).setLayoutData( new GridData( SWT.FILL, SWT.CENTER, true, false, 2, 1 ) );

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
    MccDialogWindow wnd = new MccDialogWindow( shell, "Маслофильтр" );
    PanelOilFilter panel = new PanelOilFilter( wnd.shell(), aContext );
    panel.layout();
    wnd.open();
  }

}
