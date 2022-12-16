package ru.toxsoft.mcc.ws.mnemos.app.dialogs;

import static ru.toxsoft.mcc.ws.mnemos.IMccWsMnemosConstants.*;
import static ru.toxsoft.mcc.ws.mnemos.app.dialogs.IVjResources.*;

import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.uskat.core.api.objserv.*;

import ru.toxsoft.mcc.ws.mnemos.app.controls.*;

/**
 * Панель свойств аналогового сигнала.
 * <p>
 *
 * @author vs
 */
public class PanelAnalogInputSettings
    extends AbstractMccDialogPanel {

  private final ISkObject skObject;

  protected PanelAnalogInputSettings( Shell aParent, MccDialogContext aDlgContext ) {
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

    CLabel l = new CLabel( comp, SWT.NONE );
    l.setText( dataInfo( "rtdChannelAddress" ).nmName() ); //$NON-NLS-1$
    MccRtLabel rtLabel = createRtLabel( comp, SWT.BORDER, "rtdChannelAddress", tsContext() ); //$NON-NLS-1$
    Control ctrl = rtLabel.getControl();
    GridData gd = new GridData( SWT.LEFT, SWT.FILL, false, true );
    gd.widthHint = 130;
    gd.heightHint = -1;
    ctrl.setLayoutData( gd );

    l = new CLabel( comp, SWT.NONE );
    IAtomicValue unitVal = skObject.attrs().findByKey( "atrMeasurePhisical" ); //$NON-NLS-1$
    String unitStr = STR_UNIT;
    if( unitVal != null && unitVal.isAssigned() && !unitVal.asString().isBlank() ) {
      unitStr = unitVal.asString();
    }
    l.setText( dataInfo( "rtdPhysicalValue" ).nmName() + " " + unitStr ); //$NON-NLS-1$//$NON-NLS-2$
    rtLabel = createRtLabel( comp, SWT.BORDER, "rtdPhysicalValue", tsContext() ); //$NON-NLS-1$
    ctrl = rtLabel.getControl();
    ctrl.setLayoutData( gd );

    l = new CLabel( comp, SWT.NONE );
    l.setText( dataInfo( "rtdFilterConst" ).nmName() ); //$NON-NLS-1$

    MccRtTextEditor filterEditor = createRtTextEditor( "rtdFilterConst", "cmdFilterConst" );
    filterEditor.createControl( comp ).setLayoutData( gd );
    // rtLabel = createRtLabel( comp, SWT.BORDER, "rtdFilterConst", tsContext() ); //$NON-NLS-1$
    // ctrl = rtLabel.getControl();
    // ctrl.setLayoutData( gd );

    MccRtBooleanLabel rtbLabel;

    comp = new Composite( this, SWT.NONE );
    comp.setLayout( createGridLayout( 1, false ) );

    rtbLabel = createRtBooleanLabel( comp, "rtdCalibrationError", ICONID_GRAY_LAMP, ICONID_RED_LAMP ); //$NON-NLS-1$
    dataProvider().addDataConsumer( rtbLabel );

    rtbLabel = createRtBooleanLabel( comp, "rtdCalibrationWarning", ICONID_GRAY_LAMP, ICONID_YELLOW_LAMP ); //$NON-NLS-1$
    dataProvider().addDataConsumer( rtbLabel );

    // MccCheckCmdButton checkBtn = createCheckCmdButton( comp, "cmdEnblAlarmTreat", "rtdEnblAlarmTreat", true );
    // //$NON-NLS-1$//$NON-NLS-2$
    // checkBtn.getControl().setText( dataInfo( "rtdEnblAlarmTreat" ).nmName() ); //$NON-NLS-1$
    createCheckCmdButton( comp, "cmdEnblAlarmTreat", "rtdEnblAlarmTreat", true ); //$NON-NLS-1$//$NON-NLS-2$

    createImitationGroup();
    createScaleGroup();

    comp = new Composite( this, SWT.NONE );
    comp.setLayout( gl );

    MccAttrEditor attrEditor;

    l = new CLabel( comp, SWT.NONE );
    l.setText( attrInfo( "atrMeasureValue" ).nmName() ); //$NON-NLS-1$
    attrEditor = createAttrEditor( comp, "atrMeasureValue" ); //$NON-NLS-1$
    attrEditor.getControl().setLayoutData( gd );

    l = new CLabel( comp, SWT.NONE );
    l.setText( attrInfo( "atrMeasurePhisical" ).nmName() ); //$NON-NLS-1$
    attrEditor = createAttrEditor( comp, "atrMeasurePhisical" ); //$NON-NLS-1$
    attrEditor.getControl().setLayoutData( gd );

    l = new CLabel( comp, SWT.NONE );
    l.setText( attrInfo( "atrDecimalPoint" ).nmName() ); //$NON-NLS-1$
    attrEditor = createAttrEditor( comp, "atrDecimalPoint" ); //$NON-NLS-1$
    attrEditor.getControl().setLayoutData( gd );
  }

  void createScaleGroup() {

    Group group = createGroup( this, STR_SCALING, 2, false );

    CLabel l;
    l = new CLabel( group, SWT.CENTER );
    l.setText( dataInfo( "rtdX0" ).nmName() ); //$NON-NLS-1$

    MccRtTextEditor rtText;
    rtText = createRtTextEditor( "rtdX0", "cmdX0" ); //$NON-NLS-1$ //$NON-NLS-2$
    Control ctrl = rtText.createControl( group );
    GridData gd = new GridData();
    gd.widthHint = 130;
    gd.minimumWidth = 130;
    ctrl.setLayoutData( gd );

    l = new CLabel( group, SWT.CENTER );
    l.setText( dataInfo( "rtdX1" ).nmName() ); //$NON-NLS-1$

    rtText = createRtTextEditor( "rtdX1", "cmdX1" ); //$NON-NLS-1$ //$NON-NLS-2$
    ctrl = rtText.createControl( group );
    gd = new GridData();
    gd.widthHint = 130;
    gd.minimumWidth = 130;
    ctrl.setLayoutData( gd );

    l = new CLabel( group, SWT.CENTER );
    l.setText( dataInfo( "rtdY0" ).nmName() ); //$NON-NLS-1$

    rtText = createRtTextEditor( "rtdY0", "cmdY0" ); //$NON-NLS-1$ //$NON-NLS-2$
    ctrl = rtText.createControl( group );
    gd = new GridData();
    gd.widthHint = 130;
    gd.minimumWidth = 130;
    ctrl.setLayoutData( gd );

    l = new CLabel( group, SWT.CENTER );
    l.setText( dataInfo( "rtdY1" ).nmName() ); //$NON-NLS-1$

    rtText = createRtTextEditor( "rtdY1", "cmdY1" ); //$NON-NLS-1$ //$NON-NLS-2$
    ctrl = rtText.createControl( group );
    gd = new GridData();
    gd.widthHint = 130;
    gd.minimumWidth = 130;
    ctrl.setLayoutData( gd );

  }

  void createImitationGroup() {
    Group group = createGroup( this, STR_IMITATION, 2, false );

    MccCheckCmdButton checkBtn = createCheckCmdButton( group, "cmdImitation", "rtdImitation", true ); //$NON-NLS-1$//$NON-NLS-2$
    Button btn = checkBtn.getControl();
    // btn.setText( dataInfo( "rtdImitation" ).nmName() ); //$NON-NLS-1$
    btn.setLayoutData( new GridData( SWT.LEFT, SWT.TOP, true, false, 2, 1 ) );

    CLabel l = new CLabel( group, SWT.NONE );
    l.setText( dataInfo( "rtdImitationValue" ).nmName() ); //$NON-NLS-1$

    MccRtTextEditor rtText = createRtTextEditor( "rtdImitationValue", "cmdImitationValue" ); // $NON-NLS-1$ //$NON-NLS-1$//$NON-NLS-2$
    Control ctrl = rtText.createControl( group );
    GridData gd = new GridData();
    gd.widthHint = 130;
    gd.minimumWidth = 130;
    ctrl.setLayoutData( gd );
  }

  /**
   * Показывает диалог Аналогового сигнала.
   *
   * @param aX int - x координата левого верхнего угла дочернего диалога в пикселях
   * @param aY int - y координата левого верхнего угла дочернего диалога в пикселях
   * @param aContext MccDialogContext - контекст диалога
   */
  public static void showDialog( int aX, int aY, MccDialogContext aContext ) {
    ITsGuiContext ctx = aContext.tsContext();
    Shell shell = ctx.get( Shell.class ).getShell();

    MccDialogWindow wnd = new MccDialogWindow( shell, aContext.skObject().readableName() + " " + STR_SETTINGS ); //$NON-NLS-1$
    PanelAnalogInputSettings panel = new PanelAnalogInputSettings( wnd.shell(), aContext );
    panel.layout();
    wnd.open();
    wnd.wnd.setLocation( aX, aY );
  }

}
