package ru.toxsoft.mcc.ws.mnemos.app;

import org.eclipse.jface.action.*;
import org.eclipse.jface.resource.*;
import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.ui.plugin.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.graphics.*;
import org.toxsoft.core.tsgui.graphics.colors.*;
import org.toxsoft.core.tsgui.graphics.cursors.*;
import org.toxsoft.core.tsgui.graphics.fonts.*;
import org.toxsoft.core.tsgui.panels.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.core.tslib.gw.gwid.*;
import org.toxsoft.core.tslib.gw.skid.*;
import org.toxsoft.uskat.base.gui.conn.*;
import org.toxsoft.uskat.core.api.objserv.*;
import org.toxsoft.uskat.core.connection.*;
import org.toxsoft.uskat.core.utils.*;

import ru.toxsoft.mcc.ws.mnemos.*;
import ru.toxsoft.mcc.ws.mnemos.app.controls.*;
import ru.toxsoft.mcc.ws.mnemos.app.dialogs.*;
import ru.toxsoft.mcc.ws.mnemos.app.rt.*;

/**
 * Панель отображения мнемосхемы.
 * <p>
 *
 * @author vs
 */
public class MccSchemePanel
    extends TsPanel
    implements ISkConnected {

  MouseMoveListener mouseMoveListener = aE -> {
    for( AbstractMccSchemeControl control : this.controls ) {
      if( control.contains( aE.x, aE.y ) ) {
        setCursor( cursorManager().getCursor( control.cursorType() ) );
        setToolTipText( control.tooltipText() );
        return;
      }
    }
    setCursor( null );
    setToolTipText( null );
  };

  MenuDetectListener menuListener = aEvent -> {
    // Point p = toControl( aEvent.x, aEvent.y );
    MenuManager mm = new MenuManager();

    mm.add( new Action( "Настройки 2-х позиционного регулятора" ) {

      @Override
      public void run() {
        // TsDialogUtils.info( getShell(), "To be done" );
        ISkObject skObj = coreApi().objService().get( new Skid( "mcc.TwoPositionReg", "n2TwoPositionReg_MN" ) );
        MccDialogContext ctx = new MccDialogContext( tsContext(), skObj );
        PanelTwoPositionsRegulator.showDialog( ctx );
      }
    } );

    mm.add( new Action( "Настройки аналогового регулятора" ) {

      @Override
      public void run() {
        // TsDialogUtils.info( getShell(), "To be done" );
        ISkObject skObj = coreApi().objService().get( new Skid( "mcc.AnalogReg", "n2AnalogReg_DZ" ) );
        MccDialogContext ctx = new MccDialogContext( tsContext(), skObj );
        PanelAnalogRegulator.showDialog( ctx );
      }
    } );

    mm.add( new Action( "Уставки автозапуска" ) {

      @Override
      public void run() {
        // TsDialogUtils.info( getShell(), "To be done" );
        ISkObject skObj = coreApi().objService().get( new Skid( "mcc.CtrlSystem", "n2CtrlSystem" ) );
        MccDialogContext ctx = new MccDialogContext( tsContext(), skObj );
        PanelAutoStart.showDialog( ctx );
      }
    } );

    mm.add( new Action( "Вентиляция" ) {

      @Override
      public void run() {
        // TsDialogUtils.info( getShell(), "To be done" );
        ISkObject skObj = coreApi().objService().get( new Skid( "mcc.CtrlSystem", "n2CtrlSystem" ) );
        MccDialogContext ctx = new MccDialogContext( tsContext(), skObj );
        PanelVentilation.showDialog( ctx );
      }
    } );

    mm.add( new Action( "Маслофильтр" ) {

      @Override
      public void run() {
        // TsDialogUtils.info( getShell(), "To be done" );
        ISkObject skObj = coreApi().objService().get( new Skid( "mcc.CtrlSystem", "n2CtrlSystem" ) );
        MccDialogContext ctx = new MccDialogContext( tsContext(), skObj );
        PanelOilFilter.showDialog( ctx );
      }
    } );

    mm.add( new Action( "Маслобак" ) {

      @Override
      public void run() {
        // TsDialogUtils.info( getShell(), "To be done" );
        ISkObject skObj = coreApi().objService().get( new Skid( "mcc.DigInput", "n2DI_L1_LOil" ) );
        MccDialogContext ctx = new MccDialogContext( tsContext(), skObj );
        PanelOilTank.showDialog( ctx );
      }
    } );

    Menu contextMenu = mm.createContextMenu( this );
    contextMenu.setLocation( aEvent.x, aEvent.y );
    contextMenu.setVisible( true );
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

  private final IMapEdit<Point, String> unitStrings = new ElemMap<>();

  private final Font unitFont;

  private final Color colorBlack;

  Image imgScheme;

  ISkConnection skConn;

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

    unitFont = tsContext().get( ITsFontManager.class ).getFont( "Arial", 8, SWT.NONE ); //$NON-NLS-1$
    colorBlack = colorManager().getColor( ETsColor.BLACK );

    skConn = aContext.get( ISkConnectionSupplier.class ).defConn();
    dataProvider = new MccRtDataProvider( skConn, aContext );

    IList<ISkObject> objs = skConn.coreApi().objService().listObjs( "mcc.MainSwitch", true ); //$NON-NLS-1$
    for( ISkObject obj : objs ) {
      System.out.println( obj.nmName() + ": " + obj.strid() ); //$NON-NLS-1$
    }

    ImageDescriptor imd;
    imd = AbstractUIPlugin.imageDescriptorFromPlugin( Activator.PLUGIN_ID, "icons/mcc-main.png" ); //$NON-NLS-1$
    imgScheme = imd.createImage();

    addPaintListener( aEvent -> {

      // paintBaloon( aEvent.gc );

      aEvent.gc.drawImage( imgScheme, 0, 0 );
      for( AbstractMccSchemeControl control : controls ) {
        control.paint( aEvent.gc );
      }

      Font oldFont = aEvent.gc.getFont();
      aEvent.gc.setFont( unitFont );
      aEvent.gc.setForeground( colorBlack );
      for( Point p : unitStrings.keys() ) {
        aEvent.gc.drawString( unitStrings.getByKey( p ), p.x, p.y, true );
      }
      aEvent.gc.setFont( oldFont );

    } );

    Gwid gwid = Gwid.createObj( "mcc.MainSwitch", "n2MainSwitch" ); //$NON-NLS-1$ //$NON-NLS-2$

    IStringListEdit imgIds = new StringArrayList();
    imgIds.add( "icons/main_engine_on.png" ); //$NON-NLS-1$
    imgIds.add( "icons/main_engine_off.png" ); //$NON-NLS-1$
    imgIds.add( "icons/main_engine_fault.png" ); //$NON-NLS-1$

    MccMainEngineControl mainEngine = new MccMainEngineControl( this, gwid, imgIds, aContext );
    mainEngine.setLocation( 199, 126 );
    controls.add( mainEngine );
    // dataProvider.addDataConsumer( mainEngine );

    // Добавим гидрораспределитель
    gwid = Gwid.createObj( "mcc.IrreversibleEngine", "n2IE_Hydro" );
    imgIds.clear();
    imgIds.add( "icons/gidro-On.png" ); //$NON-NLS-1$
    imgIds.add( "icons/gidro-Off.png" ); //$NON-NLS-1$
    imgIds.add( "icons/gr_fault.png" ); //$NON-NLS-1$

    MccHydraulicValveControl hydValve = new MccHydraulicValveControl( this, gwid, imgIds, aContext );
    hydValve.setLocation( 1096, 305 );
    controls.add( hydValve );
    dataProvider.addDataConsumer( hydValve );

    imgIds.clear();
    imgIds.add( "icons/oil_pump_starter_on.png" ); //$NON-NLS-1$
    imgIds.add( "icons/oil_pump_starter_off.png" ); //$NON-NLS-1$
    imgIds.add( "icons/oil_pump_starter_fault.png" ); //$NON-NLS-1$
    imgIds.add( "icons/oil_pupm_starter_unplugged.png" ); //$NON-NLS-1$

    gwid = Gwid.createObj( "mcc.IrreversibleEngine", "n2IE_Mn" ); //$NON-NLS-1$//$NON-NLS-2$
    MccIrreversibleEngineControl reversibleEngine = new MccIrreversibleEngineControl( this, gwid, imgIds, aContext );
    reversibleEngine.setLocation( 735, 715 );
    controls.add( reversibleEngine );
    dataProvider.addDataConsumer( reversibleEngine );

    imgIds.clear();
    imgIds.add( "icons/vpu_engine_on.png" ); //$NON-NLS-1$
    imgIds.add( "icons/vpu_engine_off.png" ); //$NON-NLS-1$
    imgIds.add( "icons/vpu_engine_fault.png" ); //$NON-NLS-1$

    gwid = Gwid.createObj( "mcc.IrreversibleEngine", "n2IE_VPU" ); //$NON-NLS-1$//$NON-NLS-2$
    MccIrreversibleEngineControl engine = new MccIrreversibleEngineControl( this, gwid, imgIds, aContext );
    engine.setLocation( 1036, 169 );
    controls.add( engine );
    dataProvider.addDataConsumer( engine );

    imgIds.clear();
    imgIds.add( "icons/reserve-fan-green.png" ); //$NON-NLS-1$
    imgIds.add( "icons/reserve-fan-gray.png" ); //$NON-NLS-1$
    imgIds.add( "icons/reserve-fan-red.png" ); //$NON-NLS-1$

    gwid = Gwid.createObj( "mcc.IrreversibleEngine", "n2IE_Vent1" ); //$NON-NLS-1$//$NON-NLS-2$
    MccIrreversibleEngineControl irreversibleEngine = new MccIrreversibleEngineControl( this, gwid, imgIds, aContext );
    irreversibleEngine.setLocation( 231, 51 );
    controls.add( irreversibleEngine );
    dataProvider.addDataConsumer( irreversibleEngine );

    imgIds.clear();
    imgIds.add( "icons/main-fan-green.png" ); //$NON-NLS-1$
    imgIds.add( "icons/main-fan-gray.png" ); //$NON-NLS-1$
    imgIds.add( "icons/main-fan-red.png" ); //$NON-NLS-1$

    gwid = Gwid.createObj( "mcc.IrreversibleEngine", "n2IE_Vent2" ); //$NON-NLS-1$//$NON-NLS-2$
    irreversibleEngine = new MccIrreversibleEngineControl( this, gwid, imgIds, aContext );
    irreversibleEngine.setLocation( 330, 51 );
    controls.add( irreversibleEngine );
    dataProvider.addDataConsumer( irreversibleEngine );

    imgIds.clear();
    imgIds.add( "icons/small_valve_open.png" ); //$NON-NLS-1$
    imgIds.add( "icons/small_valve_close.png" ); //$NON-NLS-1$
    imgIds.add( "icons/small_valve_fault.png" ); //$NON-NLS-1$
    imgIds.add( "icons/small_valve_unplugged.png" ); //$NON-NLS-1$
    imgIds.add( "icons/small_valve_blinking.png" ); //$NON-NLS-1$
    gwid = Gwid.createObj( "mcc.ReversibleEngine", "n2RE_Kp" ); //$NON-NLS-1$ //$NON-NLS-2$
    MccValveControl valve = new MccValveControl( this, gwid, imgIds, aContext );
    valve.setLocation( 105, 89 );
    controls.add( valve );
    dataProvider.addDataConsumer( valve );

    imgIds.clear();
    imgIds.add( "icons/valve_open.png" ); //$NON-NLS-1$
    imgIds.add( "icons/valve_close.png" ); //$NON-NLS-1$
    imgIds.add( "icons/valve_fault.png" ); //$NON-NLS-1$
    imgIds.add( "icons/valve_unplugged.png" ); //$NON-NLS-1$
    imgIds.add( "icons/valve_blinking.png" ); //$NON-NLS-1$
    gwid = Gwid.createObj( "mcc.ReversibleEngine", "n2RE_Zn" ); //$NON-NLS-1$ //$NON-NLS-2$
    valve = new MccValveControl( this, gwid, imgIds, aContext );
    valve.setLocation( 1294, 591 );
    controls.add( valve );
    dataProvider.addDataConsumer( valve );

    imgIds.clear();
    imgIds.add( "icons/valve_open.png" ); //$NON-NLS-1$
    imgIds.add( "icons/valve_close.png" ); //$NON-NLS-1$
    imgIds.add( "icons/valve_fault.png" ); //$NON-NLS-1$
    imgIds.add( "icons/valve_unplugged.png" ); //$NON-NLS-1$
    gwid = Gwid.createObj( "mcc.AnalogEngine", "n2AE_Dz" ); //$NON-NLS-1$ //$NON-NLS-2$
    MccAnalogEngineControl ae = new MccAnalogEngineControl( this, gwid, imgIds, aContext );
    ae.setLocation( 1316, 239 );
    controls.add( ae );
    dataProvider.addDataConsumer( ae );

    imgIds.clear();
    imgIds.add( "icons/valve_open_vert.png" ); //$NON-NLS-1$
    imgIds.add( "icons/valve_close_vert.png" ); //$NON-NLS-1$
    imgIds.add( "icons/valve_fault_vert.png" ); //$NON-NLS-1$
    imgIds.add( "icons/valve_unplugged_vert.png" ); //$NON-NLS-1$
    imgIds.add( "icons/valve_blinking_vert.png" ); //$NON-NLS-1$
    gwid = Gwid.createObj( "mcc.ReversibleEngine", "n2RE_Zvs" ); //$NON-NLS-1$ //$NON-NLS-2$
    valve = new MccValveControl( this, gwid, imgIds, aContext );
    valve.setLocation( 1398, 143 );
    controls.add( valve );
    dataProvider.addDataConsumer( valve );

    gwid = Gwid.createObj( "mcc.ReversibleEngine", "n2RE_Zb" ); //$NON-NLS-1$ //$NON-NLS-2$
    valve = new MccValveControl( this, gwid, imgIds, aContext );
    // valve.setLocation( 1214, 344 );
    valve.setLocation( 1219, 354 );
    controls.add( valve );
    dataProvider.addDataConsumer( valve );

    // Добавим MainSwitch (высоковольтный выключатель)
    imgIds.clear();
    imgIds.add( "icons/main_switch_on.png" ); //$NON-NLS-1$
    imgIds.add( "icons/main_switch_off.png" ); //$NON-NLS-1$
    imgIds.add( "icons/main_switch_fault.png" ); //$NON-NLS-1$
    gwid = Gwid.createObj( "mcc.MainSwitch", "n2MainSwitch" ); //$NON-NLS-1$ //$NON-NLS-2$
    MccMainSwitchControl ms = new MccMainSwitchControl( this, mainEngine, gwid, imgIds, aContext );
    ms.setLocation( 28, 135 );
    controls.add( ms );
    dataProvider.addDataConsumer( ms );

    MccRd1Baloon rd1Baloon = new MccRd1Baloon( this, aContext, null );
    controls.add( rd1Baloon );
    dataProvider.addDataConsumer( rd1Baloon );

    MccPurgeBaloon purgeBaloon = new MccPurgeBaloon( this, aContext, null );
    controls.add( purgeBaloon );
    dataProvider.addDataConsumer( purgeBaloon );

    MccOilFilterBaloon ofBaloon = new MccOilFilterBaloon( this, aContext, null );
    controls.add( ofBaloon );
    dataProvider.addDataConsumer( ofBaloon );

    MccOilTankBaloon otBaloon = new MccOilTankBaloon( this, aContext, null );
    controls.add( otBaloon );
    dataProvider.addDataConsumer( otBaloon );

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
    addMenuDetectListener( menuListener );
    addMouseListener( mouseListener );
  }

  // ------------------------------------------------------------------------------------
  // ISkConnected
  //

  @Override
  public ISkConnection skConn() {
    return skConn;
  }

  // ------------------------------------------------------------------------------------
  // API
  //

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

    Gwid gwid = Gwid.createObj( "mcc.AnalogInput", aObjStrid ); //$NON-NLS-1$
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

    ISkConnection skConn = tsContext().get( ISkConnectionSupplier.class ).defConn();
    ISkObject skObj = skConn.coreApi().objService().get( gwid.skid() );
    IOptionSet attrs = skObj.attrs();
    String unitStr = attrs.getStr( "atrMeasureValue" ); //$NON-NLS-1$
    if( unitStr.isEmpty() ) {
      unitStr = "ед.изм."; //$NON-NLS-1$
    }

    GC gc = null;
    try {
      gc = new GC( tsContext().get( Display.class ) );
      Point extent = gc.textExtent( unitStr, SWT.TRANSPARENT );
      Point p = new Point( 0, 0 );
      p.x = aX + (50 - extent.x) / 2 + 1;
      p.y = aY + 20 + (22 - extent.y) / 2;
      unitStrings.put( p, unitStr );
    }
    finally {
      if( gc != null ) {
        gc.dispose();
      }
    }

  }

  /**
   * Запускает процесс мониторинга.
   */
  public void rtStart() {
    // rtPanel.rtStart();
    dataProvider.start();
  }

  public IRtDataProvider dataProvider() {
    return dataProvider;
  }

  void paintBaloon( GC aGc ) {
    MccBaloon mccB = new MccBaloon( 100, 150, 200, 90, 40, 40, 32, 16, ETsFulcrum.LEFT_CENTER, tsContext() );
    mccB.paint( aGc );
    mccB.dispose();
  }
}
