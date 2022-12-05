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
public class PanelReversibleEngineSettings
    extends AbstractMccDialogPanel {

  private final ISkObject skObject;

  protected PanelReversibleEngineSettings( Shell aParent, MccDialogContext aDlgContext ) {
    super( aParent, aDlgContext );
    skObject = aDlgContext.skObject();
    init();
    dataProvider().start();
  }

  void init() {

    GridLayout layout = createGridLayout( 1, false );
    layout.marginLeft = 0;
    layout.marginTop = 0;
    layout.marginRight = 0;
    layout.marginBottom = 0;
    layout.verticalSpacing = 0;
    layout.horizontalSpacing = 0;

    setLayout( layout );

    Composite comp = new Composite( this, SWT.NONE );
    GridLayout gl = createGridLayout( 2, false );
    gl.verticalSpacing = 0;
    comp.setLayout( gl );

    CLabel l = new CLabel( comp, SWT.CENTER );
    l.setText( dataInfo( "rtdDegreeReal" ).nmName() ); //$NON-NLS-1$
    GridData gd = new GridData();
    gd.widthHint = 80;
    GridData gd1 = new GridData( SWT.LEFT, SWT.TOP, false, false );
    gd1.widthHint = 80;
    createRtLabel( comp, SWT.BORDER, "rtdDegreeReal", tsContext() ).getControl().setLayoutData( gd1 ); //$NON-NLS-1$

    l = new CLabel( comp, SWT.CENTER );
    l.setText( attrInfo( "atrMeasurePhisical" ).nmName() );
    createAttrEditor( comp, "atrMeasurePhisical" ).getControl().setLayoutData( gd );

    createCheckCmdButton( comp, "cmdEnabled", "rtdEnabled", true );
    createCheckCmdButton( comp, "cmdImitation", "rtdImitation", true );

    createOperatingTimeGroup( this, true );
    createTimeParamsGroup( this );
  }

  private void createTimeParamsGroup( Composite aParent ) {
    Group g = createGroup( aParent, "Параметры времени", 2, false );

    GridData gd = new GridData();
    gd.widthHint = 100;

    CLabel l;
    MccRtTextEditor textEditor;
    l = new CLabel( g, SWT.CENTER );
    l.setText( dataInfo( "rtdAuxTime" ).nmName() ); //$NON-NLS-1$
    textEditor = createRtTextEditor( "rtdAuxTime", "cmdAuxTime" ); //$NON-NLS-1$//$NON-NLS-2$
    textEditor.createControl( g ).setLayoutData( gd );

    l = new CLabel( g, SWT.CENTER );
    l.setText( dataInfo( "rtdOpenTime" ).nmName() ); //$NON-NLS-1$
    textEditor = createRtTextEditor( "rtdOpenTime", "cmdOpenTime" ); //$NON-NLS-1$//$NON-NLS-2$
    textEditor.createControl( g ).setLayoutData( gd );

    l = new CLabel( g, SWT.CENTER );
    l.setText( dataInfo( "rtdCloseTime" ).nmName() ); //$NON-NLS-1$
    textEditor = createRtTextEditor( "rtdCloseTime", "cmdCloseTime" ); //$NON-NLS-1$ //$NON-NLS-2$
    textEditor.createControl( g ).setLayoutData( gd );
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
    PanelReversibleEngineSettings panel = new PanelReversibleEngineSettings( wnd.shell(), aContext );
    panel.layout();
    wnd.open();
    wnd.wnd.setLocation( aX, aY );
  }

}
