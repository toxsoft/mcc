package ru.toxsoft.mcc.ws.mnemos.app.dialogs;

import static ru.toxsoft.mcc.ws.mnemos.IMccWsMnemosConstants.*;
import static ru.toxsoft.mcc.ws.mnemos.app.dialogs.IVjResources.*;

import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.dialogs.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.gw.gwid.*;
import org.toxsoft.uskat.core.api.objserv.*;

import ru.toxsoft.mcc.ws.mnemos.app.controls.*;
import ru.toxsoft.mcc.ws.mnemos.app.widgets.*;

/**
 * Панель свойств аналогового сигнала.
 * <p>
 *
 * @author vs
 */
public class PanelReversibleEngine
    extends AbstractMccDialogPanel {

  private final ISkObject skObject;

  protected PanelReversibleEngine( Shell aParent, MccDialogContext aDlgContext ) {
    super( aParent, aDlgContext );
    skObject = aDlgContext.skObject();
    init();
    dataProvider().start();
  }

  void init() {

    GridLayout layout = new GridLayout( 1, false );
    setLayout( layout );

    MccControlModeComponent modeComp = new MccControlModeComponent( skObject, tsContext(), null );
    dataProvider().addDataConsumer( modeComp );
    Control mc = modeComp.createControl( this, SWT.NONE );
    mc.setLayoutData( new GridData( SWT.FILL, SWT.TOP, true, false ) );

    createOpeningGroup( this );
    createClosingGroup( this );

    Composite bkPanel = new Composite( this, SWT.NONE );
    bkPanel.setLayout( new GridLayout( 2, true ) );

    // createRtBooleanLabel( bkPanel, "rtdOpen", ICONID_GRAY_LAMP, ICONID_GREEN_LAMP );
    // createRtBooleanLabel( bkPanel, "rtdOpened", ICONID_GRAY_LAMP, ICONID_GREEN_LAMP );
    // createRtBooleanLabel( bkPanel, "rtdClose", ICONID_GRAY_LAMP, ICONID_GREEN_LAMP );
    // createRtBooleanLabel( bkPanel, "rtdClosed", ICONID_GRAY_LAMP, ICONID_GREEN_LAMP );
    createRtBooleanLabel( bkPanel, "rtdPwr", ICONID_RED_LAMP, ICONID_GREEN_LAMP );
    createRtBooleanLabel( bkPanel, "rtdPowerControl", ICONID_GRAY_LAMP, ICONID_GREEN_LAMP );

    createRtBooleanLabel( bkPanel, "rtdEnabled", ICONID_RED_LAMP, ICONID_GREEN_LAMP );
    createRtBooleanLabel( bkPanel, "rtdOpenFailure", ICONID_GRAY_LAMP, ICONID_RED_LAMP );

    createRtBooleanLabel( bkPanel, "rtdImitation", ICONID_GRAY_LAMP, ICONID_YELLOW_LAMP );
    createRtBooleanLabel( bkPanel, "rtdCloseFailure", ICONID_GRAY_LAMP, ICONID_RED_LAMP );

    createRtBooleanLabel( bkPanel, "rtdOpenOnFailure", ICONID_GRAY_LAMP, ICONID_RED_LAMP );
    createRtBooleanLabel( bkPanel, "rtdOpenOffFailure", ICONID_GRAY_LAMP, ICONID_RED_LAMP );

    createRtBooleanLabel( bkPanel, "rtdCloseOnFailure", ICONID_GRAY_LAMP, ICONID_RED_LAMP );
    createRtBooleanLabel( bkPanel, "rtdCloseOffFailure", ICONID_GRAY_LAMP, ICONID_RED_LAMP );

    CLabel l = new CLabel( bkPanel, SWT.NONE );
    l.setText( dataInfo( "rtdDegree" ).nmName() );
    createRtLabel( bkPanel, SWT.BORDER, "rtdDegree", tsContext() );

    bkPanel = new Composite( this, SWT.NONE );
    bkPanel.setLayout( new GridLayout( 2, false ) );
    createOperatingTimeGroup( bkPanel );

    Composite buttonBar = new Composite( this, SWT.NONE );
    buttonBar.setLayout( new GridLayout( 2, false ) );

    GridData gd = new GridData();
    gd.widthHint = 100;
    Gwid cmdGwid = Gwid.createCmd( skObject.classId(), skObject.strid(), "cmdConfirmation" ); //$NON-NLS-1$
    OptionSet args = new OptionSet();
    args.setValue( "value", AvUtils.AV_TRUE );
    CmdPushButton btn = new CmdPushButton( buttonBar, STR_CONFIRMATION, cmdGwid, args, tsContext() );
    btn.button().setLayoutData( gd );

    Button btnSettins = new Button( buttonBar, SWT.PUSH );
    btnSettins.setText( STR_SETTINGS );
    btnSettins.setToolTipText( "Вызвать диалог настроек реверсивного двигателя" );
    btnSettins.setLayoutData( gd );
    btnSettins.addSelectionListener( new SelectionAdapter() {

      @Override
      public void widgetSelected( SelectionEvent e ) {
        // PanelAnalogInputSettings.showDialog( environ() );
      }
    } );

  }

  private void createOpeningGroup( Composite aParent ) {
    Group g = createGroup( aParent, "Открывание", 2, false );

    createRtBooleanLabel( g, "rtdOpen", ICONID_GRAY_LAMP, ICONID_GREEN_LAMP );
    createRtBooleanLabel( g, "rtdOpened", ICONID_GRAY_LAMP, ICONID_GREEN_LAMP );
    createRtBooleanLabel( g, "rtdAuxOpen", ICONID_GRAY_LAMP, ICONID_GREEN_LAMP );
    createRtBooleanLabel( g, "rtdLimitSwitchOpen", ICONID_GRAY_LAMP, ICONID_GREEN_LAMP );

  }

  private void createClosingGroup( Composite aParent ) {
    Group g = createGroup( aParent, "Закрывание", 2, false );

    createRtBooleanLabel( g, "rtdClose", ICONID_GRAY_LAMP, ICONID_GREEN_LAMP );
    createRtBooleanLabel( g, "rtdClosed", ICONID_GRAY_LAMP, ICONID_GREEN_LAMP );
    createRtBooleanLabel( g, "rtdAuxClose", ICONID_GRAY_LAMP, ICONID_GREEN_LAMP );
    createRtBooleanLabel( g, "rtdLimitSwitchClose", ICONID_GRAY_LAMP, ICONID_GREEN_LAMP );

  }

  private void createOperatingTimeGroup( Composite aParent ) {

    Group g = createGroup( aParent, "Наработка", 3, false );
    CLabel l = new CLabel( g, SWT.NONE );
    l.setText( dataInfo( "rtdHourMeterMin" ).nmName() );
    createRtLabel( g, SWT.BORDER, "rtdHourMeterMin", tsContext() );

    Button btnClear = new Button( g, SWT.PUSH );
    btnClear.setLayoutData( new GridData( SWT.LEFT, SWT.FILL, false, true, 1, 2 ) );
    btnClear.setText( "Сбросить..." );
    btnClear.addSelectionListener( new SelectionAdapter() {

      @Override
      public void widgetSelected( SelectionEvent aE ) {
        if( TsDialogUtils.askYesNoCancel( getShell(), "Сбросить время наработки?" ) == ETsDialogCode.YES ) {

        }
      }
    } );

    l = new CLabel( g, SWT.NONE );
    l.setText( dataInfo( "rtdStartCount" ).nmName() );
    createRtLabel( g, SWT.BORDER, "rtdStartCount", tsContext() );
  }

  /**
   * Показывает диалог реверсивного двигателя.
   *
   * @param aContext MccDialogContext - контекст диалога
   */
  public static void showDialog( MccDialogContext aContext ) {
    ITsGuiContext ctx = aContext.tsContext();
    Shell shell = ctx.get( Shell.class ).getShell();

    MccDialogWindow wnd = new MccDialogWindow( shell, aContext.skObject().readableName() );
    PanelReversibleEngine panel = new PanelReversibleEngine( wnd.shell(), aContext );
    panel.layout();
    wnd.open();
  }

}
