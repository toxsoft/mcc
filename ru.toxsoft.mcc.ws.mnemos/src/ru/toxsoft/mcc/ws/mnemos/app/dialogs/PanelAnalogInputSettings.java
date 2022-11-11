package ru.toxsoft.mcc.ws.mnemos.app.dialogs;

import static ru.toxsoft.mcc.ws.mnemos.IMccWsMnemosConstants.*;
import static ru.toxsoft.mcc.ws.mnemos.app.dialogs.IVjResources.*;

import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.dialogs.datarec.*;
import org.toxsoft.core.tsgui.graphics.fonts.impl.*;
import org.toxsoft.core.tslib.gw.gwid.*;
import org.toxsoft.uskat.core.api.objserv.*;

/**
 * Панель свойств аналогового сигнала.
 * <p>
 *
 * @author vs
 */
public class PanelAnalogInputSettings
    extends AbstractMccRtPanel {

  private final ISkObject skObject;

  protected PanelAnalogInputSettings( Composite aParent, TsDialog<Object, MccDialogContext> aOwnerDialog ) {
    super( aParent, aOwnerDialog );
    skObject = environ().skObject();
    init();
    contentPanel().rtStart();
  }

  void init() {

    GridLayout layout = new GridLayout( 1, false );
    contentPanel().setLayout( layout );

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
    Group group = createGroup( contentPanel(), "Масштабирование", 2, false );

    CLabel l;
    int fieldWidth = 60;
    String formatStr = "%.2f"; //$NON-NLS-1$

    l = new CLabel( group, SWT.CENTER );
    l.setText( "Значение входного сигнала X0" );
    Gwid gwid = Gwid.createRtdata( skObject.classId(), skObject.strid(), "rtdX0" );
    createFloatingEditor( group, gwid, "cmdX0" );
    // createFloatingEditor( group, "x0", "x0", formatStr, fieldWidth ); //$NON-NLS-1$//$NON-NLS-2$

    l = new CLabel( group, SWT.CENTER );
    l.setText( "Значение входного сигнала X1" );
    gwid = Gwid.createRtdata( skObject.classId(), skObject.strid(), "rtdX1" );
    createFloatingEditor( group, gwid, "cmdX1" );
    // createFloatingEditor( group, "x1", "x1", formatStr, fieldWidth ); //$NON-NLS-1$//$NON-NLS-2$

    l = new CLabel( group, SWT.CENTER );
    l.setText( "Значение входного сигнала Y0" );
    gwid = Gwid.createRtdata( skObject.classId(), skObject.strid(), "rtdY0" );
    createFloatingEditor( group, gwid, "cmdY0" );
    // createFloatingEditor( group, "y0", "y0", formatStr, fieldWidth ); //$NON-NLS-1$//$NON-NLS-2$

    l = new CLabel( group, SWT.CENTER );
    l.setText( "Значение входного сигнала Y1" );
    gwid = Gwid.createRtdata( skObject.classId(), skObject.strid(), "rtdY1" );
    createFloatingEditor( group, gwid, "cmdY1" );
    // createFloatingEditor( group, "y1", "y1", formatStr, fieldWidth ); //$NON-NLS-1$//$NON-NLS-2$

  }

  Group createValueGroup() {
    Group g = createGroup( contentPanel(), STR_OUTPUT_VALUE, 2, true );

    CLabel l = new CLabel( g, SWT.CENTER );
    l.setLayoutData( new GridData( SWT.CENTER, SWT.CENTER, false, false, 1, 3 ) );
    FontInfo fi = new FontInfo( "Arial", 24, true, false ); //$NON-NLS-1$
    l.setFont( fontManager().getFont( fi ) );
    l.setText( "123.45" );

    Composite rightComp = new Composite( g, SWT.NONE );
    GridLayout gl = new GridLayout( 1, false );
    gl.verticalSpacing = 0;
    rightComp.setLayout( gl );
    rightComp.setLayoutData( new GridData( SWT.FILL, SWT.TOP, true, false ) );

    createBooleanIndicator( rightComp, null, STR_ALARM, ICONID_RED_LAMP, ICONID_GRAY_LAMP );
    createBooleanIndicator( rightComp, null, STR_WARNING, ICONID_YELLOW_LAMP, ICONID_GRAY_LAMP );
    createBooleanIndicator( rightComp, null, STR_BLOCKING_IS_ON, ICONID_GRAY_LAMP, ICONID_YELLOW_LAMP );

    return g;
  }

  Group createLimitsGroup() {
    Group g = createGroup( contentPanel(), STR_LIMIT_VALUES, 4, false );

    CLabel l = new CLabel( g, SWT.CENTER );
    l.setLayoutData( new GridData( SWT.LEFT, SWT.TOP, false, false, 2, 1 ) );

    l = new CLabel( g, SWT.CENTER );
    l.setText( STR_INDICATION );
    l.setLayoutData( new GridData( SWT.LEFT, SWT.CENTER, false, false, 1, 1 ) );

    l = new CLabel( g, SWT.CENTER );
    l.setText( STR_GENERATION );
    l.setLayoutData( new GridData( SWT.LEFT, SWT.CENTER, false, false, 1, 1 ) );

    createLimitsRow( 1, g, STR_HI_ALARM_LIMIT, ICONID_GREEN_LAMP );
    createLimitsRow( 2, g, STR_HI_WARN_LIMIT, ICONID_GREEN_LAMP );
    createLimitsRow( 3, g, STR_LOW_WARN_LIMIT, ICONID_GREEN_LAMP );
    createLimitsRow( 4, g, STR_LOW_ALARM_LIMIT, ICONID_GREEN_LAMP );

    return g;
  }

  private void createLimitsRow( int aNum, Composite aParent, String aText, String aTrueImageId ) {
    CLabel l = new CLabel( aParent, SWT.CENTER );
    l.setText( aText );

    Gwid gwid = Gwid.createRtdata( skObject.classId(), skObject.strid(), "rtdSetPoint" + aNum );
    createFloatingEditor( aParent, gwid, "cmdSetPoint" + aNum );

    gwid = Gwid.createRtdata( skObject.classId(), skObject.strid(), "rtdSetPoint" + aNum + "indication" );
    createCheckEditor( aParent, gwid, "cmdSetPoint" + aNum + "indication", ICONID_GRAY_LAMP, aTrueImageId );
    gwid = Gwid.createRtdata( skObject.classId(), skObject.strid(), "rtdSetPoint" + aNum + "generation" );
    createCheckEditor( aParent, gwid, "cmdSetPoint" + aNum + "generation", ICONID_GRAY_LAMP, aTrueImageId );
  }

  /**
   * Показывает диалог Аналогового сигнала.
   *
   * @param aContext MccDialogContext - контекст диалога
   */
  public static void showDialog( MccDialogContext aContext ) {
    IDialogPanelCreator<Object, MccDialogContext> creator = PanelAnalogInputSettings::new;
    ITsGuiContext ctx = aContext.tsContext();
    Shell shell = ctx.get( Shell.class ).getShell();
    int flags = ITsDialogConstants.DF_NO_APPROVE;
    ITsDialogInfo dlgInfo = new TsDialogInfo( ctx, shell, aContext.skObject().readableName(), DLG_SETTINGS_MSG, flags );
    TsDialog<Object, MccDialogContext> d = new TsDialog<>( dlgInfo, null, aContext, creator );
    d.execData();
  }

}
