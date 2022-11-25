package ru.toxsoft.mcc.ws.mnemos.app.dialogs;

import static ru.toxsoft.mcc.ws.mnemos.IMccWsMnemosConstants.*;

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

    GridLayout layout = new GridLayout( 1, false );
    setLayout( layout );
    // setLayout( new RowLayout( SWT.VERTICAL ) );

    Composite comp = new Composite( this, SWT.NONE );
    GridLayout gl = new GridLayout( 2, false );
    gl.verticalSpacing = 0;
    comp.setLayout( gl );

    CLabel l = new CLabel( comp, SWT.NONE );
    l.setText( dataInfo( "rtdChannelAddress" ).nmName() );
    MccRtLabel rtLabel = createRtLabel( comp, SWT.BORDER, "rtdChannelAddress", tsContext() );
    Control ctrl = rtLabel.getControl();
    GridData gd = new GridData( SWT.LEFT, SWT.FILL, false, true );
    gd.widthHint = 130;
    gd.heightHint = -1;
    ctrl.setLayoutData( gd );

    l = new CLabel( comp, SWT.NONE );
    IAtomicValue unitVal = skObject.attrs().findByKey( "atrMeasurePhisical" );
    String unitStr = "ед.изм.";
    if( unitVal != null && unitVal.isAssigned() && !unitVal.asString().isBlank() ) {
      unitStr = unitVal.asString();
    }
    l.setText( dataInfo( "rtdPhysicalValue" ).nmName() + " " + unitStr );
    rtLabel = createRtLabel( comp, SWT.BORDER, "rtdPhysicalValue", tsContext() );
    ctrl = rtLabel.getControl();
    ctrl.setLayoutData( gd );

    MccRtBooleanLabel rtbLabel;

    comp = new Composite( this, SWT.NONE );
    comp.setLayout( new GridLayout( 1, false ) );

    rtbLabel = createRtBooleanLabel( comp, "rtdCalibrationError", ICONID_GRAY_LAMP, ICONID_RED_LAMP ); //$NON-NLS-1$
    dataProvider().addDataConsumer( rtbLabel );

    rtbLabel = createRtBooleanLabel( comp, "rtdCalibrationWarning", ICONID_GRAY_LAMP, ICONID_YELLOW_LAMP ); //$NON-NLS-1$
    dataProvider().addDataConsumer( rtbLabel );

    createScaleGroup();

    comp = new Composite( this, SWT.NONE );
    // comp.setBackground( colorManager().getColor( ETsColor.GREEN ) );
    // comp.setLayout( new GridLayout( 2, false ) );
    comp.setLayout( gl );

    MccAttrEditor attrEditor;

    l = new CLabel( comp, SWT.NONE );
    l.setText( attrInfo( "atrMeasureValue" ).nmName() );
    attrEditor = createAttrEditor( comp, skObject, "atrMeasureValue", tsContext() );
    attrEditor.getControl().setLayoutData( gd );

    l = new CLabel( comp, SWT.NONE );
    l.setText( attrInfo( "atrMeasurePhisical" ).nmName() );
    attrEditor = createAttrEditor( comp, skObject, "atrMeasurePhisical", tsContext() );
    attrEditor.getControl().setLayoutData( gd );

    l = new CLabel( comp, SWT.NONE );
    l.setText( attrInfo( "atrDecimalPoint" ).nmName() );
    attrEditor = createAttrEditor( comp, skObject, "atrDecimalPoint", tsContext() );
    attrEditor.getControl().setLayoutData( gd );

  }

  void createScaleGroup() {

    Group group = createGroup( this, "Масштабирование", 2, false );

    CLabel l;
    int fieldWidth = 60;
    String formatStr = "%.2f"; //$NON-NLS-1$

    l = new CLabel( group, SWT.CENTER );
    // l.setText( "Значение входного сигнала X0" );
    l.setText( dataInfo( "rtdX0" ).nmName() ); //$NON-NLS-1$
    // Gwid gwid = Gwid.createRtdata( skObject.classId(), skObject.strid(), "rtdX0" );
    // createFloatingEditor( group, gwid, "cmdX0" );
    // createFloatingEditor( group, "x0", "x0", formatStr, fieldWidth ); //$NON-NLS-1$//$NON-NLS-2$
    MccRtTextEditor rtText;
    rtText = createRtTextEditor( skObject, "rtdX0", "cmdX0", tsContext() ); //$NON-NLS-1$ //$NON-NLS-2$
    Control ctrl = rtText.сreateControl( group );
    GridData gd = new GridData();
    gd.widthHint = 130;
    gd.minimumWidth = 130;
    ctrl.setLayoutData( gd );

    l = new CLabel( group, SWT.CENTER );
    // l.setText( "Значение входного сигнала X1" );
    l.setText( dataInfo( "rtdX1" ).nmName() ); //$NON-NLS-1$
    // gwid = Gwid.createRtdata( skObject.classId(), skObject.strid(), "rtdX1" );
    // createFloatingEditor( group, gwid, "cmdX1" );
    // createFloatingEditor( group, "x1", "x1", formatStr, fieldWidth ); //$NON-NLS-1$//$NON-NLS-2$
    rtText = createRtTextEditor( skObject, "rtdX1", "cmdX1", tsContext() ); //$NON-NLS-1$ //$NON-NLS-2$
    ctrl = rtText.сreateControl( group );
    gd = new GridData();
    gd.widthHint = 130;
    gd.minimumWidth = 130;
    ctrl.setLayoutData( gd );

    l = new CLabel( group, SWT.CENTER );
    // l.setText( "Значение входного сигнала Y0" );
    l.setText( dataInfo( "rtdY0" ).nmName() ); //$NON-NLS-1$
    // gwid = Gwid.createRtdata( skObject.classId(), skObject.strid(), "rtdY0" );
    // createFloatingEditor( group, gwid, "cmdY0" );
    // createFloatingEditor( group, "y0", "y0", formatStr, fieldWidth ); //$NON-NLS-1$//$NON-NLS-2$
    rtText = createRtTextEditor( skObject, "rtdY0", "cmdY0", tsContext() ); //$NON-NLS-1$ //$NON-NLS-2$
    ctrl = rtText.сreateControl( group );
    gd = new GridData();
    gd.widthHint = 130;
    gd.minimumWidth = 130;
    ctrl.setLayoutData( gd );

    l = new CLabel( group, SWT.CENTER );
    // l.setText( "Значение входного сигнала Y1" );
    l.setText( dataInfo( "rtdY1" ).nmName() ); //$NON-NLS-1$
    // gwid = Gwid.createRtdata( skObject.classId(), skObject.strid(), "rtdY1" );
    // createFloatingEditor( group, gwid, "cmdY1" );
    // createFloatingEditor( group, "y1", "y1", formatStr, fieldWidth ); //$NON-NLS-1$//$NON-NLS-2$
    rtText = createRtTextEditor( skObject, "rtdY1", "cmdY1", tsContext() ); //$NON-NLS-1$ //$NON-NLS-2$
    ctrl = rtText.сreateControl( group );
    gd = new GridData();
    gd.widthHint = 130;
    gd.minimumWidth = 130;
    ctrl.setLayoutData( gd );

  }

  /**
   * Показывает диалог Аналогового сигнала.
   *
   * @param aContext MccDialogContext - контекст диалога
   */
  public static void showDialog( int aX, int aY, MccDialogContext aContext ) {
    // IDialogPanelCreator<Object, MccDialogContext> creator = PanelAnalogInputSettings::new;
    // ITsGuiContext ctx = aContext.tsContext();
    // Shell shell = ctx.get( Shell.class ).getShell();
    // int flags = ITsDialogConstants.DF_NO_APPROVE;
    // ITsDialogInfo dlgInfo = new TsDialogInfo( ctx, shell, aContext.skObject().readableName(), DLG_SETTINGS_MSG, flags
    // );
    // TsDialog<Object, MccDialogContext> d = new TsDialog<>( dlgInfo, null, aContext, creator );
    // d.execData();

    ITsGuiContext ctx = aContext.tsContext();
    Shell shell = ctx.get( Shell.class ).getShell();

    MccDialogWindow wnd = new MccDialogWindow( shell, aContext.skObject().readableName() + " настройки" );
    PanelAnalogInputSettings panel = new PanelAnalogInputSettings( wnd.shell(), aContext );
    panel.layout();
    wnd.open();
    wnd.wnd.setLocation( aX, aY );
  }

}
