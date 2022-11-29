package ru.toxsoft.mcc.ws.mnemos.app.dialogs;

import static ru.toxsoft.mcc.ws.mnemos.IMccWsMnemosConstants.*;
import static ru.toxsoft.mcc.ws.mnemos.app.dialogs.IVjResources.*;

import org.eclipse.jface.resource.*;
import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.ui.plugin.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.dialogs.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.gw.gwid.*;
import org.toxsoft.uskat.core.api.objserv.*;

import ru.toxsoft.mcc.ws.mnemos.*;
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

    createRtBooleanLabel( bkPanel, "rtdPwr", ICONID_RED_LAMP, ICONID_GREEN_LAMP );
    createRtBooleanLabel( bkPanel, "rtdPowerControl", ICONID_GRAY_LAMP, ICONID_GREEN_LAMP );
    createRtBooleanLabel( bkPanel, "rtdEnabled", ICONID_RED_LAMP, ICONID_GREEN_LAMP );
    createRtBooleanLabel( bkPanel, "rtdImitation", ICONID_GRAY_LAMP, ICONID_YELLOW_LAMP );

    CLabel l = new CLabel( bkPanel, SWT.NONE );
    l.setText( dataInfo( "rtdDegree" ).nmName() );
    createRtLabel( bkPanel, SWT.BORDER, "rtdDegree", tsContext() );

    createControlPanel( this );

    bkPanel = new Composite( this, SWT.NONE );
    bkPanel.setLayout( new GridLayout( 2, false ) );
    createOperatingTimeGroup( bkPanel, false );

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
        Point pl = getParent().getLocation();
        Point ps = getParent().getSize();
        PanelReversibleEngineSettings.showDialog( pl.x + ps.x, pl.y, dialogContext() );
      }
    } );

  }

  private void createOpeningGroup( Composite aParent ) {
    Group g = createGroup( aParent, "Открывание", 2, true );
    String grayLamp = ICONID_GRAY_LAMP;
    GridData gd = new GridData( SWT.FILL, SWT.TOP, true, false );
    createRtBooleanLabel( g, "rtdOpen", grayLamp, ICONID_GREEN_LAMP ).getControl().setLayoutData( gd );
    createRtBooleanLabel( g, "rtdOpened", grayLamp, ICONID_GREEN_LAMP ).getControl().setLayoutData( gd );
    createRtBooleanLabel( g, "rtdAuxOpen", grayLamp, ICONID_GREEN_LAMP ).getControl().setLayoutData( gd );
    createRtBooleanLabel( g, "rtdLimitSwitchOpen", grayLamp, ICONID_GREEN_LAMP ).getControl().setLayoutData( gd );
    createRtBooleanLabel( g, "rtdOpenOnFailure", grayLamp, ICONID_RED_LAMP ).getControl().setLayoutData( gd );
    createRtBooleanLabel( g, "rtdOpenOffFailure", grayLamp, ICONID_RED_LAMP ).getControl().setLayoutData( gd );
    createRtBooleanLabel( g, "rtdOpenFailure", grayLamp, ICONID_RED_LAMP ).getControl().setLayoutData( gd );
  }

  private void createClosingGroup( Composite aParent ) {
    Group g = createGroup( aParent, "Закрывание", 2, true );
    String grayLamp = ICONID_GRAY_LAMP;
    GridData gd = new GridData( SWT.FILL, SWT.TOP, true, false );
    createRtBooleanLabel( g, "rtdClose", grayLamp, ICONID_GREEN_LAMP ).getControl().setLayoutData( gd );
    createRtBooleanLabel( g, "rtdClosed", grayLamp, ICONID_GREEN_LAMP ).getControl().setLayoutData( gd );
    createRtBooleanLabel( g, "rtdAuxClose", grayLamp, ICONID_GREEN_LAMP ).getControl().setLayoutData( gd );
    createRtBooleanLabel( g, "rtdLimitSwitchClose", grayLamp, ICONID_GREEN_LAMP ).getControl().setLayoutData( gd );
    createRtBooleanLabel( g, "rtdCloseOnFailure", grayLamp, ICONID_RED_LAMP ).getControl().setLayoutData( gd );
    createRtBooleanLabel( g, "rtdCloseOffFailure", grayLamp, ICONID_RED_LAMP ).getControl().setLayoutData( gd );
    createRtBooleanLabel( g, "rtdCloseFailure", grayLamp, ICONID_RED_LAMP ).getControl().setLayoutData( gd );
  }

  private void createOperatingTimeGroup( Composite aParent, boolean aEditable ) {

    int columns = 2;
    Group g = createGroup( aParent, "Наработка", columns, false );
    if( aEditable ) {
      columns = 3;
    }
    CLabel l = new CLabel( g, SWT.NONE );
    l.setText( dataInfo( "rtdHourMeterMin" ).nmName() );
    MccRtLabel rtLabel = createRtLabel( g, SWT.BORDER, "rtdHourMeterMin", tsContext() );
    rtLabel.setValueFormatter( aValue -> {
      if( aValue == null ) {
        return "NULL"; //$NON-NLS-1$
      }
      if( !aValue.isAssigned() ) {
        return "none"; //$NON-NLS-1$
      }
      int minutes = aValue.asInt();
      return "" + minutes / 60 + ":" + minutes % 60; //$NON-NLS-1$ //$NON-NLS-2$
    } );

    if( aEditable ) {
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
    }

    l = new CLabel( g, SWT.NONE );
    l.setText( dataInfo( "rtdStartCount" ).nmName() );
    createRtLabel( g, SWT.BORDER, "rtdStartCount", tsContext() );
  }

  void createControlPanel( Composite aParent ) {
    String[] imgNames = { "double_left", "left", "stop", "right", "double_right" }; //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$//$NON-NLS-4$//$NON-NLS-5$
    Image[] images = new Image[imgNames.length];

    for( int i = 0; i < imgNames.length; i++ ) {
      ImageDescriptor imd;
      imd = AbstractUIPlugin.imageDescriptorFromPlugin( Activator.PLUGIN_ID, "icons/" + imgNames[i] + ".png" );
      images[i] = imd.createImage();
    }

    Composite comp = new Composite( aParent, SWT.NONE );
    comp.setLayout( new GridLayout( 5, true ) );

    Gwid cmdGwid = Gwid.createCmd( skObject.classId(), skObject.strid(), "cmdAwpOpenStart" ); //$NON-NLS-1$
    MccPushCmdButton cmdButton = new MccPushCmdButton( cmdGwid, coreApi(), tsContext() );
    Button b = cmdButton.createControl( comp, SWT.PUSH );
    b.setImage( images[0] );

    for( int i = 1; i < imgNames.length - 1; i++ ) {
      Button btn = new Button( comp, SWT.PUSH );
      btn.setImage( images[i] );
    }

    cmdGwid = Gwid.createCmd( skObject.classId(), skObject.strid(), "cmdAwpCloseStart" ); //$NON-NLS-1$
    cmdButton = new MccPushCmdButton( cmdGwid, coreApi(), tsContext() );
    b = cmdButton.createControl( comp, SWT.PUSH );
    b.setImage( images[4] );

    comp.addDisposeListener( aE -> {
      for( int i = 1; i < images.length; i++ ) {
        images[i].dispose();
      }
    } );
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
