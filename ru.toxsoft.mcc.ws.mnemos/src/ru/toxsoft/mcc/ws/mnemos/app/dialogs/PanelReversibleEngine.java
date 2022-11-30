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
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.gw.gwid.*;
import org.toxsoft.uskat.core.api.objserv.*;

import ru.toxsoft.mcc.ws.mnemos.*;
import ru.toxsoft.mcc.ws.mnemos.app.controls.*;
import ru.toxsoft.mcc.ws.mnemos.app.rt.*;

/**
 * Панель свойств аналогового сигнала.
 * <p>
 *
 * @author vs
 */
public class PanelReversibleEngine
    extends AbstractMccDialogPanel {

  private final ISkObject skObject;

  MccCommandSender stopCmdSender;

  boolean stopOpen  = false;
  boolean stopClose = false;

  Button btnStop;

  class OpenCloseDataConsumer
      implements IRtDataConsumer {

    @Override
    public String id() {
      return "openCloser"; //$NON-NLS-1$
    }

    @Override
    public IGwidList listNeededGwids() {
      GwidList gl = new GwidList();
      gl.add( Gwid.createRtdata( skObject.classId(), skObject.strid(), "rtdAwpOpenStart" ) ); //$NON-NLS-1$
      gl.add( Gwid.createRtdata( skObject.classId(), skObject.strid(), "rtdAwpCloseStart" ) ); //$NON-NLS-1$
      return gl;
    }

    @Override
    public void setValues( Gwid[] aGwids, IAtomicValue[] aValues, int aCount ) {
      for( int i = 0; i < aCount; i++ ) {
        if( aGwids[i].propId().equals( "rtdAwpOpenStart" ) ) { //$NON-NLS-1$
          stopOpen = false;
          if( aValues[i].isAssigned() ) {
            stopOpen = aValues[i].asBool();
          }
        }
        if( aGwids[i].propId().equals( "rtdAwpCloseStart" ) ) { //$NON-NLS-1$
          stopClose = false;
          if( aValues[i].isAssigned() ) {
            stopClose = aValues[i].asBool();
          }
        }
      }
      if( !stopOpen && !stopClose ) {
        btnStop.setEnabled( false );
      }
      else {
        btnStop.setEnabled( true );
      }
    }
  }

  protected PanelReversibleEngine( Shell aParent, MccDialogContext aDlgContext ) {
    super( aParent, aDlgContext );
    skObject = aDlgContext.skObject();
    init();
    stopCmdSender = new MccCommandSender( coreApi() );
    stopCmdSender.eventer().addListener( aSource -> {
      String errStr = stopCmdSender.errorString();
      if( errStr != null && !errStr.isBlank() ) {
        TsDialogUtils.error( getShell(), errStr );
      }
    } );
    dataProvider().addDataConsumer( new OpenCloseDataConsumer() );
    dataProvider().start();
  }

  void init() {

    GridLayout layout = new GridLayout( 1, false );
    setLayout( layout );

    MccControlModeComponent modeComp = createControlModeComponent( this, skObject, tsContext() );
    modeComp.getControl().setLayoutData( new GridData( SWT.FILL, SWT.TOP, true, false ) );

    createOpeningGroup( this );
    createClosingGroup( this );

    Composite bkPanel = new Composite( this, SWT.NONE );
    bkPanel.setLayout( new GridLayout( 2, true ) );

    createRtBooleanLabel( bkPanel, "rtdPwr", ICONID_RED_LAMP, ICONID_GREEN_LAMP ); //$NON-NLS-1$
    createRtBooleanLabel( bkPanel, "rtdPowerControl", ICONID_GRAY_LAMP, ICONID_GREEN_LAMP ); //$NON-NLS-1$
    createRtBooleanLabel( bkPanel, "rtdEnabled", ICONID_RED_LAMP, ICONID_GREEN_LAMP ); //$NON-NLS-1$
    createRtBooleanLabel( bkPanel, "rtdImitation", ICONID_GRAY_LAMP, ICONID_YELLOW_LAMP ); //$NON-NLS-1$

    CLabel l = new CLabel( bkPanel, SWT.NONE );
    l.setText( dataInfo( "rtdDegree" ).nmName() ); //$NON-NLS-1$
    createRtLabel( bkPanel, SWT.BORDER, "rtdDegree", tsContext() ); //$NON-NLS-1$

    createControlPanel( this );

    bkPanel = new Composite( this, SWT.NONE );
    bkPanel.setLayout( new GridLayout( 2, false ) );
    createOperatingTimeGroup( bkPanel, false );

    Composite buttonBar = new Composite( this, SWT.NONE );
    buttonBar.setLayout( new GridLayout( 2, false ) );

    GridData gd = new GridData();
    gd.widthHint = 100;

    MccPushCmdButton btnConfirm = createPushCmdButton( buttonBar, "cmdConfirmation" ); //$NON-NLS-1$
    btnConfirm.getControl().setText( STR_CONFIRMATION );
    btnConfirm.getControl().setLayoutData( gd );

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
    createRtBooleanLabel( g, "rtdOpen", grayLamp, ICONID_GREEN_LAMP ).getControl().setLayoutData( gd ); //$NON-NLS-1$
    createRtBooleanLabel( g, "rtdOpened", grayLamp, ICONID_GREEN_LAMP ).getControl().setLayoutData( gd ); //$NON-NLS-1$
    createRtBooleanLabel( g, "rtdAuxOpen", grayLamp, ICONID_GREEN_LAMP ).getControl().setLayoutData( gd ); //$NON-NLS-1$
    createRtBooleanLabel( g, "rtdLimitSwitchOpen", grayLamp, ICONID_GREEN_LAMP ).getControl().setLayoutData( gd ); //$NON-NLS-1$
    createRtBooleanLabel( g, "rtdOpenOnFailure", grayLamp, ICONID_RED_LAMP ).getControl().setLayoutData( gd ); //$NON-NLS-1$
    createRtBooleanLabel( g, "rtdOpenOffFailure", grayLamp, ICONID_RED_LAMP ).getControl().setLayoutData( gd ); //$NON-NLS-1$
    createRtBooleanLabel( g, "rtdOpenFailure", grayLamp, ICONID_RED_LAMP ).getControl().setLayoutData( gd ); //$NON-NLS-1$
  }

  private void createClosingGroup( Composite aParent ) {
    Group g = createGroup( aParent, "Закрывание", 2, true );
    String grayLamp = ICONID_GRAY_LAMP;
    GridData gd = new GridData( SWT.FILL, SWT.TOP, true, false );
    createRtBooleanLabel( g, "rtdClose", grayLamp, ICONID_GREEN_LAMP ).getControl().setLayoutData( gd ); //$NON-NLS-1$
    createRtBooleanLabel( g, "rtdClosed", grayLamp, ICONID_GREEN_LAMP ).getControl().setLayoutData( gd ); //$NON-NLS-1$
    createRtBooleanLabel( g, "rtdAuxClose", grayLamp, ICONID_GREEN_LAMP ).getControl().setLayoutData( gd ); //$NON-NLS-1$
    createRtBooleanLabel( g, "rtdLimitSwitchClose", grayLamp, ICONID_GREEN_LAMP ).getControl().setLayoutData( gd ); //$NON-NLS-1$
    createRtBooleanLabel( g, "rtdCloseOnFailure", grayLamp, ICONID_RED_LAMP ).getControl().setLayoutData( gd ); //$NON-NLS-1$
    createRtBooleanLabel( g, "rtdCloseOffFailure", grayLamp, ICONID_RED_LAMP ).getControl().setLayoutData( gd ); //$NON-NLS-1$
    createRtBooleanLabel( g, "rtdCloseFailure", grayLamp, ICONID_RED_LAMP ).getControl().setLayoutData( gd ); //$NON-NLS-1$
  }

  MccUpDownCmdButton leftBtn;
  MccUpDownCmdButton rightBtn;

  void createControlPanel( Composite aParent ) {
    String[] imgNames = { "double_left", "left", "stop", "right", "double_right" }; //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$//$NON-NLS-4$//$NON-NLS-5$
    Image[] images = new Image[imgNames.length];

    for( int i = 0; i < imgNames.length; i++ ) {
      ImageDescriptor imd;
      imd = AbstractUIPlugin.imageDescriptorFromPlugin( Activator.PLUGIN_ID, "icons/" + imgNames[i] + ".png" ); //$NON-NLS-1$ //$NON-NLS-2$
      images[i] = imd.createImage();
    }

    Composite comp = new Composite( aParent, SWT.NONE );
    comp.setLayout( new GridLayout( 5, true ) );

    MccPushCmdButton cmdButton = createPushCmdButton( comp, "cmdAwpOpenStart" ); //$NON-NLS-1$
    cmdButton.getControl().setImage( images[0] );

    Gwid cmdGwid = Gwid.createCmd( skObject.classId(), skObject.strid(), "cmdAwpOpenStart" ); //$NON-NLS-1$
    Gwid stopOpenGwid = Gwid.createCmd( skObject.classId(), skObject.strid(), "cmdAwpOpenStop" ); //$NON-NLS-1$
    leftBtn = new MccUpDownCmdButton( cmdGwid, stopOpenGwid, coreApi(), tsContext() );
    leftBtn.createControl( comp, SWT.PUSH ).setImage( images[1] );

    btnStop = new Button( comp, SWT.PUSH );
    btnStop.setImage( images[2] );
    btnStop.addSelectionListener( new SelectionAdapter() {

      @Override
      public void widgetSelected( SelectionEvent aE ) {
        if( stopClose ) {
          stopCmdSender.sendCommand( Gwid.createCmd( skObject.classId(), skObject.strid(), "cmdAwpCloseStop" ), true ); //$NON-NLS-1$
        }
        if( stopOpen ) {
          stopCmdSender.sendCommand( Gwid.createCmd( skObject.classId(), skObject.strid(), "cmdAwpOpenStop" ), true ); //$NON-NLS-1$
        }
      }
    } );

    cmdGwid = Gwid.createCmd( skObject.classId(), skObject.strid(), "cmdAwpCloseStart" ); //$NON-NLS-1$
    Gwid stopCloseGwid = Gwid.createCmd( skObject.classId(), skObject.strid(), "cmdAwpCloseStop" ); //$NON-NLS-1$
    rightBtn = new MccUpDownCmdButton( cmdGwid, stopCloseGwid, coreApi(), tsContext() );
    rightBtn.createControl( comp, SWT.PUSH ).setImage( images[3] );

    cmdButton = createPushCmdButton( comp, "cmdAwpCloseStart" ); //$NON-NLS-1$
    cmdButton.getControl().setImage( images[4] );

    comp.addDisposeListener( aE -> {
      for( int i = 0; i < images.length; i++ ) {
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
