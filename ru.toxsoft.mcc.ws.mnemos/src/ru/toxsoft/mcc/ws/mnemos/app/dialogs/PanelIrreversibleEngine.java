package ru.toxsoft.mcc.ws.mnemos.app.dialogs;

import static ru.toxsoft.mcc.ws.mnemos.IMccWsMnemosConstants.*;
import static ru.toxsoft.mcc.ws.mnemos.app.dialogs.IVjResources.*;

import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.uskat.core.api.objserv.*;

import ru.toxsoft.mcc.ws.mnemos.app.controls.*;

/**
 * Панель свойств аналогового сигнала.
 * <p>
 *
 * @author vs
 */
public class PanelIrreversibleEngine
    extends AbstractMccDialogPanel {

  private final ISkObject skObject;

  protected PanelIrreversibleEngine( Shell aParent, MccDialogContext aDlgContext ) {
    super( aParent, aDlgContext );
    skObject = aDlgContext.skObject();
    init();
    dataProvider().start();
  }

  void init() {

    GridLayout layout = createGridLayout( 1, false );
    setLayout( layout );

    MccControlModeComponent modeComp = createControlModeComponent( this, skObject, tsContext() );
    modeComp.getControl().setLayoutData( new GridData( SWT.FILL, SWT.TOP, true, false ) );

    Composite bkPanel = new Composite( this, SWT.NONE );
    bkPanel.setLayout( createGridLayout( 2, true ) );

    createRtBooleanLabel( bkPanel, "rtdAuxOn", ICONID_GRAY_LAMP, ICONID_GREEN_LAMP ); //$NON-NLS-1$
    createRtBooleanLabel( bkPanel, "rtdOn", ICONID_GRAY_LAMP, ICONID_YELLOW_LAMP ); //$NON-NLS-1$

    createRtBooleanLabel( bkPanel, "rtdPwr", ICONID_RED_LAMP, ICONID_GREEN_LAMP ); //$NON-NLS-1$
    createRtBooleanLabel( bkPanel, "rtdEnabled", ICONID_RED_LAMP, ICONID_GREEN_LAMP ); //$NON-NLS-1$
    createRtBooleanLabel( bkPanel, "rtdImitation", ICONID_GRAY_LAMP, ICONID_YELLOW_LAMP ); //$NON-NLS-1$
    createRtBooleanLabel( bkPanel, "rtdSwitchOnFailure", ICONID_GRAY_LAMP, ICONID_RED_LAMP ); //$NON-NLS-1$
    createRtBooleanLabel( bkPanel, "rtdSwitchOffFailure", ICONID_GRAY_LAMP, ICONID_RED_LAMP ); //$NON-NLS-1$
    createRtBooleanLabel( bkPanel, "rtdReady", ICONID_YELLOW_LAMP, ICONID_GREEN_LAMP ); //$NON-NLS-1$

    Composite buttonBar = new Composite( this, SWT.NONE );
    buttonBar.setLayout( createGridLayout( 2, false ) );

    GridData gd = new GridData();
    gd.widthHint = 100;

    MccPushCmdButton btnStart = createPushCmdButton( buttonBar, "cmdAwpStart" ); //$NON-NLS-1$
    btnStart.getControl().setText( STR_START );
    btnStart.getControl().setLayoutData( gd );

    MccPushCmdButton btnStop = createPushCmdButton( buttonBar, "cmdAwpStop" ); //$NON-NLS-1$
    btnStop.getControl().setText( STR_STOP );
    btnStop.getControl().setLayoutData( gd );

    createOperatingTimeGroup( this, false );

    buttonBar = new Composite( this, SWT.NONE );
    buttonBar.setLayout( createGridLayout( 2, false ) );

    MccPushCmdButton btnConfirm = createPushCmdButton( buttonBar, "cmdConfirmation" ); //$NON-NLS-1$
    btnConfirm.getControl().setText( STR_CONFIRMATION );
    btnConfirm.getControl().setLayoutData( gd );

    Button btnSettins = new Button( buttonBar, SWT.PUSH );
    btnSettins.setText( STR_SETTINGS );
    btnSettins.setToolTipText( STR_INVOKE_IRR_ENGINE_DIALOG );
    btnSettins.setLayoutData( gd );
    btnSettins.addSelectionListener( new SelectionAdapter() {

      @Override
      public void widgetSelected( SelectionEvent e ) {
        Point pl = getParent().getLocation();
        Point ps = getParent().getSize();
        PanelIrreversibleEngineSettings.showDialog( pl.x + ps.x, pl.y, dialogContext() );
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
    PanelIrreversibleEngine panel = new PanelIrreversibleEngine( wnd.shell(), aContext );
    panel.layout();
    wnd.open();
  }

}
