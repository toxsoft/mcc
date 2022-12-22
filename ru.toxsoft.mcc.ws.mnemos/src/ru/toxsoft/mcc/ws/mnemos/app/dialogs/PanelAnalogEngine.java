package ru.toxsoft.mcc.ws.mnemos.app.dialogs;

import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.dialogs.*;
import org.toxsoft.core.tsgui.graphics.fonts.impl.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.gw.gwid.*;
import org.toxsoft.uskat.core.api.objserv.*;

import ru.toxsoft.mcc.ws.mnemos.app.controls.*;
import ru.toxsoft.mcc.ws.mnemos.app.rt.*;
import ru.toxsoft.mcc.ws.mnemos.app.utils.*;

/**
 * Панель свойств аналогового сигнала.
 * <p>
 *
 * @author vs
 */
public class PanelAnalogEngine
    extends AbstractMccDialogPanel {

  private final ISkObject skObject;

  MccCommandSender commandSender;

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

  /**
   * ИД команды задания для дроссельной заслонки
   */
  private final Gwid cmdGwid;

  protected PanelAnalogEngine( Shell aParent, MccDialogContext aDlgContext ) {
    super( aParent, aDlgContext );
    skObject = aDlgContext.skObject();
    init();

    cmdGwid = Gwid.createCmd( "mcc.AnalogEngine", "n2AI_DDZ", "cmdTaskDz" ); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

    commandSender = new MccCommandSender( coreApi() );
    commandSender.eventer().addListener( aSource -> {
      String errStr = commandSender.errorString();
      if( errStr != null && !errStr.isBlank() ) {
        TsDialogUtils.error( getShell(), errStr );
      }
    } );

    dataProvider().start();
  }

  void init() {

    GridLayout layout = new GridLayout( 1, false );
    setLayout( layout );

    MccControlModeComponent modeComp = createControlModeComponent( this, skObject, tsContext() );
    modeComp.getControl().setLayoutData( new GridData( SWT.FILL, SWT.TOP, true, false ) );

    Gwid gwid = Gwid.createRtdata( "mcc.AnalogInput", "n2AI_DDZ", MccAnalogInputControl.DI_CURRENT_VALUE );
    MccAnalogInputControl aiControl = new MccAnalogInputControl( gwid, tsContext(), null );
    Control ctrl = aiControl.createControl( this, SWT.BORDER );
    FontInfo fi = new FontInfo( "Arial", 24, true, false ); //$NON-NLS-1$
    ctrl.setFont( fontManager().getFont( fi ) );
    GridData gd = new GridData( SWT.CENTER, SWT.FILL, false, true, 1, 2 );
    gd.widthHint = 130;
    gd.minimumWidth = 130;
    ctrl.setLayoutData( gd );
    dataProvider().addDataConsumer( aiControl );

    createControlPanel( this );
  }

  void createControlPanel( Composite aParent ) {
    Composite comp = new Composite( aParent, SWT.NONE );
    comp.setLayout( new GridLayout( 4, false ) );

    GridData gd = new GridData();
    gd.widthHint = 80;

    CLabel l = new CLabel( comp, SWT.CENTER );
    l.setText( "Задание ДЗ:" );

    createRtLabel( comp, SWT.CENTER | SWT.BORDER, "rtdTaskDz", tsContext() ).getControl().setLayoutData( gd );

    Button btn = new Button( comp, SWT.PUSH );
    btn.setText( " - " );
    btn.addSelectionListener( new SelectionAdapter() {

      @Override
      public void widgetSelected( SelectionEvent aE ) {
        Gwid gwid = Gwid.createRtdata( "mcc.AnalogEngine", "n2AE_Dz", "rtdTaskDz" );
        IAtomicValue taskVal = MccRtUtils.readRtData( gwid, coreApi() );
        gwid = Gwid.createRtdata( "mcc.AnalogEngine", "n2AE_Dz", "rtdStepControl" );
        IAtomicValue stepVal = MccRtUtils.readRtData( gwid, coreApi() );
        if( taskVal.isAssigned() && stepVal.isAssigned() ) {
          double val = taskVal.asDouble() - stepVal.asDouble();
          if( !commandSender.sendCommand( cmdGwid, AvUtils.avFloat( val ) ) ) {
            TsDialogUtils.error( getShell(), commandSender.errorString() );
            return;
          }
        }
      }
    } );

    btn = new Button( comp, SWT.PUSH );
    btn.setText( "+" );
    btn.addSelectionListener( new SelectionAdapter() {

      @Override
      public void widgetSelected( SelectionEvent aE ) {
        Gwid gwid = Gwid.createRtdata( "mcc.AnalogEngine", "n2AE_Dz", "rtdTaskDz" );
        IAtomicValue taskVal = MccRtUtils.readRtData( gwid, coreApi() );
        gwid = Gwid.createRtdata( "mcc.AnalogEngine", "n2AE_Dz", "rtdStepControl" );
        IAtomicValue stepVal = MccRtUtils.readRtData( gwid, coreApi() );
        if( taskVal.isAssigned() && stepVal.isAssigned() ) {
          double val = taskVal.asDouble() + stepVal.asDouble();
          if( !commandSender.sendCommand( cmdGwid, AvUtils.avFloat( val ) ) ) {
            TsDialogUtils.error( getShell(), commandSender.errorString() );
            return;
          }
        }
      }
    } );

    l = new CLabel( comp, SWT.CENTER );
    l.setText( "Шаг изменения:" );

    Gwid stepControlGwid = Gwid.createRtdata( "mcc.AnalogEngine", "n2AE_Dz", "rtdStepControl" );

    IEditingFinisher finisher = aNewValue -> {
      MccRtUtils.writeRtData( aNewValue, stepControlGwid, coreApi() );
      return true;
    };

    MccTextEditor stepEditor = new MccTextEditor( finisher, EAtomicType.FLOATING, tsContext() );
    // MccRtTextEditor stepEditor = createRtTextEditor( "rtdStepControl", "cmdStepControl" );
    gd = new GridData( SWT.FILL, SWT.CENTER, true, false, 3, 1 );
    stepEditor.createControl( comp ).setLayoutData( gd );

    IAtomicValue v = MccRtUtils.readRtData( stepControlGwid, coreApi() );
    stepEditor.setValue( v );

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
    PanelAnalogEngine panel = new PanelAnalogEngine( wnd.shell(), aContext );
    panel.layout();
    wnd.open();
  }

}
