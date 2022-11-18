package ru.toxsoft.mcc.ws.mnemos.app;

import org.eclipse.jface.resource.*;
import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.ui.plugin.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.graphics.cursors.*;
import org.toxsoft.core.tsgui.panels.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.core.tslib.gw.gwid.*;
import org.toxsoft.uskat.base.gui.conn.*;

import ru.toxsoft.mcc.ws.mnemos.*;
import ru.toxsoft.mcc.ws.mnemos.app.controls.*;
import ru.toxsoft.mcc.ws.mnemos.app.rt.*;

/**
 * Панель отображения мнемосхемы.
 * <p>
 *
 * @author vs
 */
public class MccSchemePanel
    extends TsPanel {

  MouseMoveListener mouseMoveListener = aE -> {
    for( AbstractMccSchemeControl control : this.controls ) {
      if( control.contains( aE.x, aE.y ) ) {
        setCursor( cursorManager().getCursor( control.cursorType() ) );
        return;
      }
    }
    setCursor( null );
  };

  MouseListener mouseListener = new MouseListener() {

    @Override
    public void mouseUp( MouseEvent aE ) {
      // TODO Auto-generated method stub

    }

    @Override
    public void mouseDown( MouseEvent aE ) {
      if( aE.button == 1 ) {
        for( AbstractMccSchemeControl control : MccSchemePanel.this.controls ) {
          if( control.contains( aE.x, aE.y ) ) {
            control.showSettingDialog();
            return;
          }
        }
      }
    }

    @Override
    public void mouseDoubleClick( MouseEvent aE ) {
      // TODO Auto-generated method stub

    }
  };

  // private RtValedsPanel rtPanel;

  IListEdit<AbstractMccSchemeControl> controls = new ElemArrayList<>();

  IRtDataProvider dataProvider;

  Image imgScheme;

  /**
   * Конструктор.
   *
   * @param aParent Composite - родительская компонента
   * @param aContext ITsGuiContext - контекст панели
   */
  public MccSchemePanel( Composite aParent, ITsGuiContext aContext ) {
    super( aParent, aContext );
    // setLayout( new FillLayout() );
    setLayout( null );

    dataProvider = new MccRtDataProvider( aContext.get( ISkConnectionSupplier.class ).defConn(), aContext );

    // rtPanel = new RtValedsPanel( this, aContext );
    // rtPanel.setLayout( null );

    ImageDescriptor imd;
    imd = AbstractUIPlugin.imageDescriptorFromPlugin( Activator.PLUGIN_ID, "icons/mcc-main.png" ); //$NON-NLS-1$
    imgScheme = imd.createImage();

    // rtPanel.addPaintListener( aEvent -> {
    // aEvent.gc.drawImage( imgScheme, 0, 0 );
    // for( AbstractMccSchemeControl control : controls ) {
    // control.paint( aEvent.gc );
    // }
    // } );

    addPaintListener( aEvent -> {
      aEvent.gc.drawImage( imgScheme, 0, 0 );
      for( AbstractMccSchemeControl control : controls ) {
        control.paint( aEvent.gc );
      }
    } );

    Gwid gwid = Gwid.createObj( "mcc.IrreversibleEngine", "n2IE_Hydro" ); //$NON-NLS-1$ //$NON-NLS-2$

    IStringListEdit imgIds = new StringArrayList();
    imgIds.add( "icons/main_engine_on.png" ); //$NON-NLS-1$
    imgIds.add( "icons/main_engine_off.png" ); //$NON-NLS-1$
    imgIds.add( "icons/main_engine_fault.png" ); //$NON-NLS-1$

    MccMainEngineControl mainEngine = new MccMainEngineControl( this, gwid, imgIds, aContext );
    mainEngine.setLocation( 199, 126 );
    controls.add( mainEngine );

    imgIds.clear();
    imgIds.add( "icons/oil_pupm_starter_on.png" ); //$NON-NLS-1$
    imgIds.add( "icons/oil_pupm_starter_off.png" ); //$NON-NLS-1$
    imgIds.add( "icons/oil_pupm_starter_fault.png" ); //$NON-NLS-1$
    imgIds.add( "icons/oil_pupm_starter_unplugged.png" ); //$NON-NLS-1$

    gwid = Gwid.createObj( "mcc.IrreversibleEngine", "n2IE_Mn" ); //$NON-NLS-1$//$NON-NLS-2$
    MccReversibleEngineControl reversibleEngine = new MccReversibleEngineControl( this, gwid, imgIds, aContext );
    reversibleEngine.setLocation( 737, 706 );
    controls.add( reversibleEngine );

    imgIds.clear();
    imgIds.add( "icons/engine_on.png" ); //$NON-NLS-1$
    imgIds.add( "icons/engine_off.png" ); //$NON-NLS-1$
    imgIds.add( "icons/engine_fault.png" ); //$NON-NLS-1$

    gwid = Gwid.createObj( "mcc.IrreversibleEngine", "n2IE_VPU" ); //$NON-NLS-1$//$NON-NLS-2$
    mainEngine = new MccMainEngineControl( this, gwid, imgIds, aContext );
    mainEngine.setLocation( 1030, 181 );
    controls.add( mainEngine );

    // objs = coreApi.objService().listObjs( "mcc.ReversibleEngine", true ); //$NON-NLS-1$

    imgIds.clear();
    imgIds.add( "icons/valve_open.png" ); //$NON-NLS-1$
    imgIds.add( "icons/valve_close.png" ); //$NON-NLS-1$
    imgIds.add( "icons/valve_fault.png" ); //$NON-NLS-1$
    imgIds.add( "icons/valve_unplugged.png" ); //$NON-NLS-1$
    imgIds.add( "icons/valve_blinking.png" ); //$NON-NLS-1$
    gwid = Gwid.createObj( "mcc.ReversibleEngine", "n2RE_Zn" ); //$NON-NLS-1$ //$NON-NLS-2$
    MccValveControl valve = new MccValveControl( this, gwid, imgIds, aContext );
    valve.setLocation( 1281, 572 );
    controls.add( valve );

    gwid = Gwid.createObj( "mcc.ReversibleEngine", "n2RE_Kp" ); //$NON-NLS-1$ //$NON-NLS-2$
    valve = new MccValveControl( this, gwid, imgIds, aContext );
    valve.setLocation( 1308, 221 );
    controls.add( valve );

    imgIds.clear();
    imgIds.add( "icons/valve_open_vert.png" ); //$NON-NLS-1$
    imgIds.add( "icons/valve_close_vert.png" ); //$NON-NLS-1$
    imgIds.add( "icons/valve_fault_vert.png" ); //$NON-NLS-1$
    imgIds.add( "icons/valve_unplugged_vert.png" ); //$NON-NLS-1$
    imgIds.add( "icons/valve_blinking_vert.png" ); //$NON-NLS-1$
    gwid = Gwid.createObj( "mcc.ReversibleEngine", "n2RE_Zvs" ); //$NON-NLS-1$ //$NON-NLS-2$
    valve = new MccValveControl( this, gwid, imgIds, aContext );
    valve.setLocation( 1394, 136 );
    controls.add( valve );

    gwid = Gwid.createObj( "mcc.ReversibleEngine", "n2RE_Zb" ); //$NON-NLS-1$ //$NON-NLS-2$
    valve = new MccValveControl( this, gwid, imgIds, aContext );
    valve.setLocation( 1214, 344 );
    controls.add( valve );

    addDisposeListener( aE -> {
      dataProvider.dispose();
      imgScheme.dispose();
      for( AbstractMccSchemeControl control : controls ) {
        control.dispose();
      }
    } );

    // rtPanel.addMouseMoveListener( mouseMoveListener );
    // rtPanel.addMouseListener( mouseListener );
    addMouseMoveListener( mouseMoveListener );
    addMouseListener( mouseListener );
  }

  /**
   * Добавляет аналоговый вход для мониторинга его значения.
   *
   * @param aX int - x координата в пикселях
   * @param aY int - y координата в пикселях
   * @param aObjStrid String - ИД объекта
   */
  public void addAI( int aX, int aY, String aObjStrid ) {
    // ITsGuiContext ctx = new TsGuiContext( tsContext() );
    // IOptionSetEdit params = ctx.params();
    //
    // ValedMccAnalogInput.OPDEF_CLASS_ID.setValue( params, AvUtils.avStr( "mcc.AnalogInput" ) ); //$NON-NLS-1$
    // ValedMccAnalogInput.OPDEF_OBJ_STRID.setValue( params, AvUtils.avStr( aObjStrid ) );
    // ValedMccAnalogInput.OPDEF_DATA_ID.setValue( params, AvUtils.avStr( "rtdCurrentValue" ) ); //$NON-NLS-1$
    //
    // ValedMccAnalogInput valedAI = new ValedMccAnalogInput( ctx );
    // CLabel l = (CLabel)valedAI.createControl( rtPanel );
    // l.setLocation( aX, aY );
    // l.setSize( 50, 22 );
    //
    // rtPanel.defineRtData( valedAI.dataGwid(), valedAI );

    Gwid gwid = Gwid.createObj( "mcc.AnalogInput", aObjStrid );
    MccAnalogInputControl aiControl = new MccAnalogInputControl( gwid, tsContext(), null );
    CLabel l = (CLabel)aiControl.createControl( this, SWT.BORDER | SWT.CENTER );
    l.setLocation( aX, aY );
    l.setSize( 50, 22 );
    l.setCursor( cursorManager().getCursor( ECursorType.HAND ) );
    l.addMouseListener( new MouseAdapter() {

      @Override
      public void mouseDown( MouseEvent aE ) {
        aiControl.showSettingDialog();
      }
    } );

    dataProvider.addDataConsumer( aiControl );
  }

  /**
   * Запускает процесс мониторинга.
   */
  public void rtStart() {
    // rtPanel.rtStart();
    dataProvider.start();
  }

  // @Override
  // public void redraw() {
  // if( !isDisposed() ) {
  // super.redraw();
  // rtPanel.redraw();
  // }
  // }
  //
  // @Override
  // public void redraw( int aX, int aY, int aWidth, int aHeight, boolean aAll ) {
  // if( !isDisposed() ) {
  // super.redraw( aX, aY, aWidth, aHeight, aAll );
  // rtPanel.redraw( aX, aY, aWidth, aHeight, aAll );
  // }
  // }

}
