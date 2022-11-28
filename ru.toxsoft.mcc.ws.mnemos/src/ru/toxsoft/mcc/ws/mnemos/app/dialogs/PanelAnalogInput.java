package ru.toxsoft.mcc.ws.mnemos.app.dialogs;

import static ru.toxsoft.mcc.ws.mnemos.IMccWsMnemosConstants.*;
import static ru.toxsoft.mcc.ws.mnemos.app.dialogs.IVjResources.*;

import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.graphics.fonts.impl.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.gw.gwid.*;
import org.toxsoft.uskat.core.api.objserv.*;
import org.toxsoft.uskat.core.api.sysdescr.*;
import org.toxsoft.uskat.core.api.sysdescr.dto.*;

import ru.toxsoft.mcc.ws.mnemos.app.controls.*;
import ru.toxsoft.mcc.ws.mnemos.app.widgets.*;

/**
 * Панель свойств аналогового сигнала.
 * <p>
 *
 * @author vs
 */
public class PanelAnalogInput
    extends AbstractMccDialogPanel {

  private final ISkObject skObject;

  protected PanelAnalogInput( Shell aParent, MccDialogContext aDlgContext ) {
    super( aParent, aDlgContext );
    skObject = aDlgContext.skObject();
    init();
    dataProvider().start();
  }

  void init() {

    GridLayout layout = new GridLayout( 1, false );
    setLayout( layout );

    createValueGroup();
    createLimitsGroup();

    Composite buttonBar = new Composite( this, SWT.NONE );
    buttonBar.setLayout( new GridLayout( 2, false ) );

    GridData gd = new GridData();
    gd.widthHint = 100;
    Gwid cmdGwid = Gwid.createCmd( skObject.classId(), skObject.strid(), "cmdConfirmation" ); //$NON-NLS-1$
    OptionSet args = new OptionSet();
    args.setValue( "value", AvUtils.AV_TRUE ); //$NON-NLS-1$
    CmdPushButton btn = new CmdPushButton( buttonBar, STR_CONFIRMATION, cmdGwid, args, tsContext() );
    btn.button().setLayoutData( gd );

    Button btnSettins = new Button( buttonBar, SWT.PUSH );
    btnSettins.setText( STR_SETTINGS );
    btnSettins.setToolTipText( STR_INVOKE_SETTINGS_DLG );
    btnSettins.setLayoutData( gd );
    btnSettins.addSelectionListener( new SelectionAdapter() {

      @Override
      public void widgetSelected( SelectionEvent e ) {
        Point pl = getParent().getLocation();
        Point ps = getParent().getSize();
        PanelAnalogInputSettings.showDialog( pl.x + ps.x, pl.y, dialogContext() );
      }
    } );

  }

  Group createValueGroup() {

    IOptionSet attrs = dialogContext().skObject().attrs();
    String unitStr = attrs.getStr( "atrMeasureValue" ); //$NON-NLS-1$
    if( unitStr.isEmpty() ) {
      unitStr = "ед.изм. "; //$NON-NLS-1$
    }

    Group g = createGroup( this, STR_OUTPUT_VALUE + " " + unitStr, 3, false ); //$NON-NLS-1$

    Gwid gwid = Gwid.createRtdata( skObject.classId(), skObject.strid(), MccAnalogInputControl.DI_CURRENT_VALUE );
    MccAnalogInputControl aiControl = new MccAnalogInputControl( gwid, tsContext(), null );
    Control ctrl = aiControl.createControl( g, SWT.BORDER );
    FontInfo fi = new FontInfo( "Arial", 24, true, false ); //$NON-NLS-1$
    ctrl.setFont( fontManager().getFont( fi ) );
    GridData gd = new GridData( SWT.CENTER, SWT.FILL, false, true, 1, 2 );
    gd.widthHint = 130;
    gd.minimumWidth = 130;
    ctrl.setLayoutData( gd );
    dataProvider().addDataConsumer( aiControl );

    createRtBooleanLabel( g, "rtdAlarm", ICONID_GRAY_LAMP, ICONID_RED_LAMP ); //$NON-NLS-1$
    createRtBooleanLabel( g, "rtdWarn", ICONID_GRAY_LAMP, ICONID_YELLOW_LAMP ); //$NON-NLS-1$
    createRtBooleanLabel( g, "rtdImitation", ICONID_GRAY_LAMP, ICONID_YELLOW_LAMP ); //$NON-NLS-1$
    createRtBooleanLabel( g, "rtdBlockAlarm", ICONID_GRAY_LAMP, ICONID_YELLOW_LAMP ); //$NON-NLS-1$

    return g;
  }

  Group createLimitsGroup() {
    Group g = createGroup( this, STR_LIMIT_VALUES, 4, false );

    CLabel l = new CLabel( g, SWT.CENTER );
    l.setLayoutData( new GridData( SWT.LEFT, SWT.TOP, false, false, 2, 1 ) );

    l = new CLabel( g, SWT.CENTER );
    l.setText( STR_INDICATION );
    l.setLayoutData( new GridData( SWT.LEFT, SWT.CENTER, false, false, 1, 1 ) );

    l = new CLabel( g, SWT.CENTER );
    l.setText( STR_GENERATION );
    l.setLayoutData( new GridData( SWT.LEFT, SWT.CENTER, false, false, 1, 1 ) );

    createLimitsRow( 1, g, "rtdAlarmMaxIndication", "rtdAlarmMaxGeneration", ICONID_RED_LAMP ); //$NON-NLS-1$//$NON-NLS-2$
    createLimitsRow( 2, g, "rtdWarningMaxIndication", "rtdWarningMaxGeneration", ICONID_YELLOW_LAMP ); //$NON-NLS-1$//$NON-NLS-2$
    createLimitsRow( 3, g, "rtdWarningMinIndication", "rtdWarningMinGeneration", ICONID_YELLOW_LAMP ); //$NON-NLS-1$//$NON-NLS-2$
    createLimitsRow( 4, g, "rtdAlarmMinIndication", "rtdAlarmMinGeneration", ICONID_RED_LAMP ); //$NON-NLS-1$ //$NON-NLS-2$

    return g;
  }

  private void createLimitsRow( int aNum, Composite aParent, String aIndicationId, String aGenerationId,
      String aTrueImageId ) {
    ISkClassInfo clsInfo = coreApi().sysdescr().getClassInfo( MccAnalogInputControl.CLS_ANALOG_INPUT );
    IDtoRtdataInfo di = clsInfo.rtdata().list().getByKey( "rtdSetPoint" + aNum ); //$NON-NLS-1$

    CLabel l = new CLabel( aParent, SWT.CENTER );
    l.setText( di.nmName() );

    MccRtTextEditor rtText = createRtTextEditor( skObject, "rtdSetPoint" + aNum, "cmdSetPoint" + aNum, tsContext() ); //$NON-NLS-1$//$NON-NLS-2$
    Control ctrl = rtText.сreateControl( aParent );
    GridData gd = new GridData();
    gd.widthHint = 130;
    gd.minimumWidth = 130;
    ctrl.setLayoutData( gd );

    Composite bkPane = new Composite( aParent, SWT.NONE );
    bkPane.setLayout( new GridLayout( 2, false ) );

    createRtBooleanIcon( bkPane, aIndicationId, aTrueImageId, ICONID_GRAY_LAMP );

    String cmdPrefix = "cmdSetPoint" + aNum; //$NON-NLS-1$
    String rtdPrefix = "rtdSetPoint" + aNum; //$NON-NLS-1$

    createCheckCmdButton( bkPane, cmdPrefix + "indication", rtdPrefix + "indication", tsContext() ); //$NON-NLS-1$ //$NON-NLS-2$

    bkPane = new Composite( aParent, SWT.NONE );
    bkPane.setLayout( new GridLayout( 2, false ) );
    createRtBooleanIcon( bkPane, aGenerationId, aTrueImageId, ICONID_GRAY_LAMP );

    createCheckCmdButton( bkPane, cmdPrefix + "generation", rtdPrefix + "generation", tsContext() ); //$NON-NLS-1$//$NON-NLS-2$
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
    PanelAnalogInput panel = new PanelAnalogInput( wnd.shell(), aContext );
    panel.layout();
    wnd.open();
  }

}
