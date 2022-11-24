package ru.toxsoft.mcc.ws.mnemos.app.dialogs;

import static ru.toxsoft.mcc.ws.mnemos.IMccWsMnemosConstants.*;

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

    Composite comp = new Composite( this, SWT.NONE );
    comp.setLayout( new GridLayout( 2, false ) );

    CLabel l = new CLabel( this, SWT.NONE );
    l.setText( dataInfo( "rtdChannelAddress" ).nmName() );
    MccRtLabel rtLabel = new MccRtLabel( skObject, "rtdChannelAddress", tsContext(), null );
    Control ctrl = rtLabel.createControl( comp, SWT.BORDER );
    GridData gd = new GridData();
    gd.widthHint = 130;
    ctrl.setLayoutData( gd );

    gd = new GridData( SWT.LEFT, SWT.TOP, false, false, 2, 1 );
    MccRtBooleanLabel rtbLabel;
    rtbLabel = createRtBooleanLabel( comp, "rtdCalibrationError", ICONID_GRAY_LAMP, ICONID_RED_LAMP ); //$NON-NLS-1$
    rtbLabel.getControl().setLayoutData( gd );
    rtbLabel = createRtBooleanLabel( comp, "rtdCalibrationWarning", ICONID_GRAY_LAMP, ICONID_YELLOW_LAMP ); //$NON-NLS-1$
    rtbLabel.getControl().setLayoutData( gd );

    createScaleGroup();

    // createValueGroup();
    // createLimitsGroup();
    //
    // Composite buttonBar = new Composite( contentPanel(), SWT.NONE );
    // buttonBar.setLayout( new GridLayout( 2, false ) );
    //
    // GridData gd = new GridData();
    // gd.widthHint = 100;
    // Gwid cmdGwid = Gwid.createCmd( skObject.classId(), skObject.strid(), "cmdConfirmation" ); //$NON-NLS-1$
    // OptionSet args = new OptionSet();
    // args.setValue( "value", AvUtils.AV_TRUE );
    // CmdPushButton btn = new CmdPushButton( buttonBar, STR_CONFIRMATION, cmdGwid, args, tsContext() );
    // btn.button().setLayoutData( gd );
    //
    // Button btnSettins = new Button( buttonBar, SWT.PUSH );
    // btnSettins.setText( STR_SETTINGS );
    // btnSettins.setToolTipText( "Вызвать диалог настроек аналогового синала" );
    // btnSettins.setLayoutData( gd );
    // btnSettins.addSelectionListener( new SelectionAdapter() {
    //
    // @Override
    // public void widgetSelected( SelectionEvent e ) {
    // // PanelAnalogInputSettings.showDialog( context(), getShell().getText(), "Установите требуемые параметры" );
    // }
    // } );

  }

  void createScaleGroup() {

    Group group = createGroup( this, "Масштабирование", 2, false );

    CLabel l;
    int fieldWidth = 60;
    String formatStr = "%.2f"; //$NON-NLS-1$

    l = new CLabel( group, SWT.CENTER );
    // l.setText( "Значение входного сигнала X0" );
    l.setText( dataInfo( "rtdX0" ).nmName() );
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
    l.setText( "Значение входного сигнала X1" );
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
    l.setText( "Значение входного сигнала Y0" );
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
    l.setText( "Значение входного сигнала Y1" );
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
