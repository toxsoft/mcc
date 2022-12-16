package ru.toxsoft.mcc.ws.mnemos.app.dialogs;

import static ru.toxsoft.mcc.ws.mnemos.IMccWsMnemosConstants.*;
import static ru.toxsoft.mcc.ws.mnemos.app.dialogs.IVjResources.*;

import org.eclipse.swt.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;

import ru.toxsoft.mcc.ws.mnemos.app.controls.*;

/**
 * Панель свойств высоковольтного выключателя сигнала.
 * <p>
 *
 * @author vs
 */
public class PanelMainSwitchSettings
    extends AbstractMccDialogPanel {

  // private final ISkObject skObject;

  protected PanelMainSwitchSettings( Shell aParent, MccDialogContext aDlgContext ) {
    super( aParent, aDlgContext );
    // skObject = aDlgContext.skObject();
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

    createRtBooleanIcon( comp, "rtdImitation", ICONID_YELLOW_LAMP, ICONID_GRAY_LAMP ); //$NON-NLS-1$
    createCheckCmdButton( comp, "cmdImitation", "rtdImitation", true );

    createOperatingTimeGroup( this, true );
  }

  /**
   * Показывает диалог настроек высоковольтного выключателя.
   *
   * @param aX int - x координата левого верхнего угла дочернего диалога в пикселях
   * @param aY int - y координата левого верхнего угла дочернего диалога в пикселях
   * @param aContext MccDialogContext - контекст диалога
   */
  public static void showDialog( int aX, int aY, MccDialogContext aContext ) {
    ITsGuiContext ctx = aContext.tsContext();
    Shell shell = ctx.get( Shell.class ).getShell();

    MccDialogWindow wnd = new MccDialogWindow( shell, aContext.skObject().readableName() + " " + STR_SETTINGS ); //$NON-NLS-1$
    PanelMainSwitchSettings panel = new PanelMainSwitchSettings( wnd.shell(), aContext );
    panel.layout();
    wnd.open();
    wnd.wnd.setLocation( aX, aY );
  }

}
