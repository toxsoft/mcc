package ru.toxsoft.mcc.ws.mnemos.app.dialogs;

import static ru.toxsoft.mcc.ws.mnemos.app.dialogs.IVjResources.*;

import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
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
public class PanelIrreversibleEngineSettings
    extends AbstractMccDialogPanel {

  private final ISkObject skObject;

  protected PanelIrreversibleEngineSettings( Shell aParent, MccDialogContext aDlgContext ) {
    super( aParent, aDlgContext );
    skObject = aDlgContext.skObject();
    init();
    dataProvider().start();
  }

  void init() {

    GridLayout layout = createGridLayout( 1, false );

    setLayout( layout );

    Composite comp = new Composite( this, SWT.NONE );
    GridLayout gl = createGridLayout( 2, false );
    gl.verticalSpacing = 0;
    comp.setLayout( gl );

    createCheckCmdButton( comp, "cmdEnabled", "rtdEnabled", true );
    createCheckCmdButton( comp, "cmdImitation", "rtdImitation", true );

    createOperatingTimeGroup( this, true );

    GridData gd = new GridData();
    gd.widthHint = 100;

    CLabel l;
    MccRtTextEditor textEditor;
    l = new CLabel( comp, SWT.CENTER );
    l.setText( dataInfo( "rtdAuxTime" ).nmName() ); //$NON-NLS-1$
    textEditor = createRtTextEditor( "rtdAuxTime", "cmdAuxTime" ); //$NON-NLS-1$//$NON-NLS-2$
    textEditor.createControl( comp ).setLayoutData( gd );
  }

  /**
   * Показывает диалог настроек ревсивного двигателя.
   *
   * @param aX int - x координата левого верхнего угла дочернего диалога в пикселях
   * @param aY int - y координата левого верхнего угла дочернего диалога в пикселях
   * @param aContext MccDialogContext - контекст диалога
   */
  public static void showDialog( int aX, int aY, MccDialogContext aContext ) {
    ITsGuiContext ctx = aContext.tsContext();
    Shell shell = ctx.get( Shell.class ).getShell();

    MccDialogWindow wnd = new MccDialogWindow( shell, aContext.skObject().readableName() + " " + STR_SETTINGS ); //$NON-NLS-1$
    PanelIrreversibleEngineSettings panel = new PanelIrreversibleEngineSettings( wnd.shell(), aContext );
    panel.layout();
    wnd.open();
    wnd.wnd.setLocation( aX, aY );
  }

}
