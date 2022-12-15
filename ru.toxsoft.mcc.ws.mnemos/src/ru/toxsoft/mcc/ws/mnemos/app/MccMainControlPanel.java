package ru.toxsoft.mcc.ws.mnemos.app;

import static ru.toxsoft.mcc.ws.mnemos.app.IVjResources.*;

import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.dialogs.*;
import org.toxsoft.core.tsgui.panels.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.core.tslib.gw.gwid.*;
import org.toxsoft.core.tslib.gw.skid.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.uskat.core.api.objserv.*;
import org.toxsoft.uskat.core.connection.*;
import org.toxsoft.uskat.core.utils.*;

import ru.toxsoft.mcc.ws.mnemos.app.controls.*;
import ru.toxsoft.mcc.ws.mnemos.app.dialogs.*;
import ru.toxsoft.mcc.ws.mnemos.app.rt.*;

/**
 * Панель выбора режима управления нагнетателем.
 * <p>
 *
 * @author vs
 */
public class MccMainControlPanel
    extends TsPanel
    implements ISkConnected, IRtDataConsumer {

  /**
   * ИД класса система управления
   */
  public static String CLSID_CTRL_SYSTEM = "mcc.CtrlSystem"; //$NON-NLS-1$
  /**
   * ИД объекта класса система управления
   */
  public static String OBJID_CTRL_SYSTEM = "n2CtrlSystem";   //$NON-NLS-1$

  private final ISkConnection skConn;

  final MccCommandSender cmdSender;

  private final IMapEdit<Gwid, IAtomicValue> valuesMap = new ElemMap<>();

  private final Gwid armCtrlGwid;
  private final Gwid panelCtrlGwid;
  private final Gwid localCtrlGwid;
  private final Gwid autoCtrlGwid;
  private final Gwid stepGwid;

  private final Button btnArm;
  private final Button btnPanel;
  private final Button btnAuto;

  // Этапы запуска
  static final IIntMapEdit<String> statesMap = new IntMap<>();

  static {
    statesMap.put( 0, "Работа без регулирования" );

    statesMap.put( 1, "Включение предпусковой сигнализации (7 сек.)" );
    statesMap.put( 2, "Активация регулятора масляного насоса" );
    statesMap.put( 4, "Включение вентсистемы ГЭД" );
    statesMap.put( 7, "Открытие задвижки байпаса" );
    statesMap.put( 8, "Закрытие дроссельной заслонки" );
    statesMap.put( 9, "Закрытие задвижки нагнетания" );
    statesMap.put( 10, "Открытие задвижки всаса" );
    statesMap.put( 11, "Включение гидрораспределителя ВПУ" );
    statesMap.put( 12, "Включение ВПУ" );
    statesMap.put( 13, "Прогрев ГЭД паром" );
    statesMap.put( 14, "Продувка ГЭД газом" );
    statesMap.put( 15, "Расцепление и отключение ВПУ" );
    statesMap.put( 16, "Ожидание включения ГЭД (20 минут)" );
    statesMap.put( 17, "Прогрев нагнетателя (20 минут)" );
    statesMap.put( 18, "Открытие дроссельной заслонки до 35 град." );
    statesMap.put( 19, "Открытие задвижки нагнетания" );
    statesMap.put( 20, "Запуск завершён" );

    statesMap.put( 25, "Работа с регулированием" );

    // Этапы останова
    statesMap.put( 31, "Закрытие дроссельной заслонки до 35 град." );
    statesMap.put( 32, "Открытие задвижки байпаса" );
    statesMap.put( 33, "Закрытие задвижки нагнетания" );
    statesMap.put( 34, "Отключение ГЭД" );
    statesMap.put( 35, "Открытие задвижки байпаса" );
    statesMap.put( 36, "Закрытие дроссельной заслонки" );
    statesMap.put( 37, "Закрытие задвижки нагнетания" );
    statesMap.put( 38, "Закрытие задвижки всаса" );
    statesMap.put( 39, "Останов нагнетателя (15 мин.)" );
    statesMap.put( 40, "Отключение вентсистемы ГЭД" );
    statesMap.put( 42, "Останов завершён" );

    statesMap.put( 50, "Остановлен" );
  }

  /**
   * Конструктор.
   *
   * @param aParent Composite - родительская панель
   * @param aContext ITsGuiContext - соответствующий
   * @param aSkConn ISkConnection - соединение с сервером
   */
  public MccMainControlPanel( Composite aParent, ISkConnection aSkConn, ITsGuiContext aContext ) {
    super( aParent, aContext );

    skConn = aSkConn;

    armCtrlGwid = Gwid.createRtdata( CLSID_CTRL_SYSTEM, OBJID_CTRL_SYSTEM, "rtdAwpCtrl" ); //$NON-NLS-1$
    panelCtrlGwid = Gwid.createRtdata( CLSID_CTRL_SYSTEM, OBJID_CTRL_SYSTEM, "rtdPanelCtrl" ); //$NON-NLS-1$
    localCtrlGwid = Gwid.createRtdata( CLSID_CTRL_SYSTEM, OBJID_CTRL_SYSTEM, "rtdLocalCtrl" ); //$NON-NLS-1$
    autoCtrlGwid = Gwid.createRtdata( CLSID_CTRL_SYSTEM, OBJID_CTRL_SYSTEM, "rtdAutoCtrl" ); //$NON-NLS-1$
    stepGwid = Gwid.createRtdata( CLSID_CTRL_SYSTEM, OBJID_CTRL_SYSTEM, "rtdStep" ); //$NON-NLS-1$

    valuesMap.put( armCtrlGwid, IAtomicValue.NULL );
    valuesMap.put( panelCtrlGwid, IAtomicValue.NULL );
    valuesMap.put( localCtrlGwid, IAtomicValue.NULL );
    valuesMap.put( autoCtrlGwid, IAtomicValue.NULL );
    valuesMap.put( stepGwid, IAtomicValue.NULL );

    cmdSender = new MccCommandSender( coreApi() );
    cmdSender.eventer().addListener( aSource -> {
      String errStr = cmdSender.errorString();
      if( errStr != null && !errStr.isBlank() ) {
        TsDialogUtils.error( getShell(), errStr );
      }
    } );

    GridLayout gl = new GridLayout( 1, false );
    setLayout( gl );

    GridData gd = new GridData();
    gd.widthHint = 80;

    Composite buttonBar = new Composite( this, SWT.NONE );
    gl = new GridLayout( 4, false );
    gl.marginHeight = 0;
    gl.verticalSpacing = 0;
    gl.marginTop = 0;
    gl.marginBottom = 0;
    buttonBar.setLayout( gl );
    // buttonBar.setBackground( colorManager().getColor( ETsColor.RED ) );

    CLabel l = new CLabel( buttonBar, SWT.NONE );
    l.setText( STR_CONTROL_MODE );

    btnArm = new Button( buttonBar, SWT.TOGGLE );
    btnArm.setText( STR_ARM_CTRL );
    btnArm.setLayoutData( gd );
    btnArm.addSelectionListener( new SelectionAdapter() {

      @Override
      public void widgetSelected( SelectionEvent aE ) {
        if( btnArm.getSelection() ) {
          Gwid cmdg = Gwid.createCmd( CLSID_CTRL_SYSTEM, OBJID_CTRL_SYSTEM, "cmdSetApwCtrl" ); //$NON-NLS-1$
          if( !cmdSender.sendCommand( cmdg, AvUtils.avBool( true ) ) ) {
            TsDialogUtils.error( getShell(), cmdSender.errorString() );
          }
        }
      }
    } );

    btnPanel = new Button( buttonBar, SWT.TOGGLE );
    btnPanel.setText( STR_PANEL_CTRL );
    btnPanel.setLayoutData( gd );
    btnPanel.addSelectionListener( new SelectionAdapter() {

      @Override
      public void widgetSelected( SelectionEvent aE ) {
        if( btnPanel.getSelection() ) {
          Gwid cmdg = Gwid.createCmd( CLSID_CTRL_SYSTEM, OBJID_CTRL_SYSTEM, "cmdSetPanelCtrl" ); //$NON-NLS-1$
          if( !cmdSender.sendCommand( cmdg, AvUtils.avBool( true ) ) ) {
            TsDialogUtils.error( getShell(), cmdSender.errorString() );
          }
        }
      }
    } );

    btnAuto = new Button( buttonBar, SWT.TOGGLE );
    btnAuto.setText( STR_AUTO_CTRL );
    btnAuto.setLayoutData( gd );
    btnAuto.addSelectionListener( new SelectionAdapter() {

      @Override
      public void widgetSelected( SelectionEvent aE ) {
        if( btnAuto.getSelection() ) {
          Gwid cmdg = Gwid.createCmd( CLSID_CTRL_SYSTEM, OBJID_CTRL_SYSTEM, "cmdSetAutoCtrl" ); //$NON-NLS-1$
          if( !cmdSender.sendCommand( cmdg, AvUtils.avBool( true ) ) ) {
            TsDialogUtils.error( getShell(), cmdSender.errorString() );
          }
        }
      }
    } );

    createLaunchPanel( this );
  }

  // ------------------------------------------------------------------------------------
  // ISkConnected
  //

  @Override
  public ISkConnection skConn() {
    return skConn;
  }

  // ------------------------------------------------------------------------------------
  // IRtDataConsumer
  //

  @Override
  public String id() {
    return "mcc.CtrlSystem.n2CtrlSystem"; //$NON-NLS-1$
  }

  @Override
  public IGwidList listNeededGwids() {
    GwidList gl = new GwidList();
    gl.addAll( valuesMap.keys() );
    return gl;
  }

  @Override
  public void setValues( Gwid[] aGwids, IAtomicValue[] aValues, int aCount ) {
    for( int i = 0; i < aCount; i++ ) {
      valuesMap.put( aGwids[i], aValues[i] );
    }
    internalUpdate();
  }

  // ------------------------------------------------------------------------------------
  // Implementation
  //

  private void internalUpdate() {
    IAtomicValue val;

    val = valuesMap.getByKey( armCtrlGwid );
    btnArm.setEnabled( val.isAssigned() );
    if( val.isAssigned() ) {
      btnArm.setSelection( val.asBool() );
    }
    val = valuesMap.getByKey( panelCtrlGwid );
    btnPanel.setEnabled( val.isAssigned() );
    if( val.isAssigned() ) {
      btnPanel.setSelection( val.asBool() );
    }
    val = valuesMap.getByKey( autoCtrlGwid );
    btnAuto.setEnabled( val.isAssigned() );
    if( val.isAssigned() ) {
      btnAuto.setSelection( val.asBool() );
    }
    val = valuesMap.getByKey( stepGwid );
    if( val.isAssigned() ) {
      int state = val.asInt();

      labelStepNo.setText( Integer.toString( state ) );
      labelStepDescr.setText( statesMap.getByKey( state ) );
    }
    else {
      labelStepNo.setText( "???" ); //$NON-NLS-1$
      labelStepDescr.setText( TsLibUtils.EMPTY_STRING );
    }
  }

  CLabel labelStepNo;
  CLabel labelStepDescr;

  private void createLaunchPanel( Composite aParent ) {
    Group stepHolder = new Group( aParent, SWT.NONE );
    stepHolder.setText( STR_START );
    stepHolder.setLayoutData( new GridData( SWT.FILL, SWT.TOP, true, false ) );
    stepHolder.setLayout( new GridLayout( 3, false ) );
    FontData fd = stepHolder.getFont().getFontData()[0];
    Font f = fontManager().getFont( fd.getName(), fd.getHeight(), SWT.BOLD );
    stepHolder.setFont( f );

    GridData gd = new GridData();
    gd.widthHint = 50;

    CLabel l = new CLabel( stepHolder, SWT.NONE );
    l.setText( STR_STEP );

    labelStepNo = new CLabel( stepHolder, SWT.CENTER | SWT.BORDER );
    labelStepNo.setLayoutData( gd );

    labelStepDescr = new CLabel( stepHolder, SWT.CENTER | SWT.BORDER );
    labelStepDescr.setLayoutData( new GridData( SWT.FILL, SWT.TOP, true, false ) );

    Composite buttonsHolder = new Composite( stepHolder, SWT.NONE );
    GridLayout gl = new GridLayout( 3, false );
    gl.marginHeight = 0;
    gl.marginWidth = 0;
    buttonsHolder.setLayout( gl );
    buttonsHolder.setLayoutData( new GridData( SWT.FILL, SWT.TOP, true, false, 3, 1 ) );
    // buttonsHolder.setBackground( colorManager().getColor( ETsColor.RED ) );

    Button btnStart = new Button( buttonsHolder, SWT.PUSH );
    btnStart.setText( STR_START_AUTO );
    // btnStart.setLayoutData( new GridData( SWT.LEFT, SWT.TOP, true, false ) );

    Button btnStop = new Button( buttonsHolder, SWT.PUSH );
    btnStop.setText( STR_STOP_AUTO );
    btnStop.setLayoutData( new GridData( SWT.LEFT, SWT.TOP, true, false ) );

    Button btnBlocks = new Button( buttonsHolder, SWT.PUSH );
    btnBlocks.setText( STR_BLOCKS );
    btnBlocks.setLayoutData( new GridData( SWT.RIGHT, SWT.TOP, true, false ) );
    btnBlocks.addSelectionListener( new SelectionAdapter() {

      @Override
      public void widgetSelected( SelectionEvent aE ) {
        ISkObject skObj = coreApi().objService().find( new Skid( CLSID_CTRL_SYSTEM, OBJID_CTRL_SYSTEM ) );
        MccDialogContext ctx = new MccDialogContext( tsContext(), skObj );
        MccBlockingPanel.showDialog( ctx );
      }
    } );
  }

}
