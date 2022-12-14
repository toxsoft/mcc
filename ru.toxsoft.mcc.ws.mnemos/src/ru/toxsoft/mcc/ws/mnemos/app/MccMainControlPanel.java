package ru.toxsoft.mcc.ws.mnemos.app;

import static ru.toxsoft.mcc.ws.mnemos.app.IVjResources.*;

import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.panels.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.gw.gwid.*;
import org.toxsoft.core.tslib.gw.skid.*;
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
  public static String OBJID_CTRL_SYSTEM = "mcc.CtrlSystem"; //$NON-NLS-1$

  private final ISkConnection skConn;

  final MccCommandSender cmdSender;

  private final IMapEdit<Gwid, IAtomicValue> valuesMap = new ElemMap<>();

  private final Gwid armCtrlGwid;
  private final Gwid panelCtrlGwid;
  private final Gwid localCtrlGwid;
  private final Gwid autoCtrlGwid;

  private final Button btnArm;
  private final Button btnPanel;
  private final Button btnAuto;

  /**
   * Конструктор.
   *
   * @param aParent Composite - родительская панель
   * @param aDataProvider IRtDataProvider - поставщик данных
   * @param aContext ITsGuiContext - соответствующий
   * @param aSkConn ISkConnection - соединение с сервером
   */
  public MccMainControlPanel( Composite aParent, IRtDataProvider aDataProvider, ISkConnection aSkConn,
      ITsGuiContext aContext ) {
    super( aParent, aContext );

    skConn = aSkConn;

    armCtrlGwid = Gwid.createRtdata( CLSID_CTRL_SYSTEM, OBJID_CTRL_SYSTEM, "rtdAwpCtrl" ); //$NON-NLS-1$
    panelCtrlGwid = Gwid.createRtdata( CLSID_CTRL_SYSTEM, OBJID_CTRL_SYSTEM, "rtdPanelCtrl" ); //$NON-NLS-1$
    localCtrlGwid = Gwid.createRtdata( CLSID_CTRL_SYSTEM, OBJID_CTRL_SYSTEM, "rtdLocalCtrl" ); //$NON-NLS-1$
    autoCtrlGwid = Gwid.createRtdata( CLSID_CTRL_SYSTEM, OBJID_CTRL_SYSTEM, "rtdAutoCtrl" ); //$NON-NLS-1$

    valuesMap.put( armCtrlGwid, IAtomicValue.NULL );
    valuesMap.put( panelCtrlGwid, IAtomicValue.NULL );
    valuesMap.put( localCtrlGwid, IAtomicValue.NULL );
    valuesMap.put( autoCtrlGwid, IAtomicValue.NULL );

    cmdSender = new MccCommandSender( coreApi() );

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

    btnPanel = new Button( buttonBar, SWT.TOGGLE );
    btnPanel.setText( STR_PANEL_CTRL );
    btnPanel.setLayoutData( gd );

    btnAuto = new Button( buttonBar, SWT.TOGGLE );
    btnAuto.setText( STR_AUTO_CTRL );
    btnAuto.setLayoutData( gd );

    createLaunchPanel( this );

    aDataProvider.addDataConsumer( this );
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
    if( val.isAssigned() ) {
      btnArm.setSelection( val.asBool() );
    }
    val = valuesMap.getByKey( panelCtrlGwid );
    if( val.isAssigned() ) {
      btnPanel.setSelection( val.asBool() );
    }
    val = valuesMap.getByKey( autoCtrlGwid );
    if( val.isAssigned() ) {
      btnAuto.setSelection( val.asBool() );
    }
  }

  private void createLaunchPanel( Composite aParent ) {
    Group stepHolder = new Group( this, SWT.NONE );
    stepHolder.setText( "Запуск" );
    stepHolder.setLayoutData( new GridData( SWT.FILL, SWT.TOP, true, false ) );
    stepHolder.setLayout( new GridLayout( 3, false ) );
    FontData fd = stepHolder.getFont().getFontData()[0];
    Font f = fontManager().getFont( fd.getName(), fd.getHeight(), SWT.BOLD );
    stepHolder.setFont( f );

    GridData gd = new GridData();
    gd.widthHint = 50;

    CLabel l = new CLabel( stepHolder, SWT.NONE );
    l.setText( "Шаг: " );

    l = new CLabel( stepHolder, SWT.CENTER | SWT.BORDER );
    l.setLayoutData( gd );

    l = new CLabel( stepHolder, SWT.CENTER | SWT.BORDER );
    l.setLayoutData( new GridData( SWT.FILL, SWT.TOP, true, false ) );

    Composite buttonsHolder = new Composite( stepHolder, SWT.NONE );
    GridLayout gl = new GridLayout( 3, false );
    gl.marginHeight = 0;
    gl.marginWidth = 0;
    buttonsHolder.setLayout( gl );
    buttonsHolder.setLayoutData( new GridData( SWT.FILL, SWT.TOP, true, false, 3, 1 ) );
    // buttonsHolder.setBackground( colorManager().getColor( ETsColor.RED ) );

    Button btnStart = new Button( buttonsHolder, SWT.PUSH );
    btnStart.setText( "Старт АВТ" );
    // btnStart.setLayoutData( new GridData( SWT.LEFT, SWT.TOP, true, false ) );

    Button btnStop = new Button( buttonsHolder, SWT.PUSH );
    btnStop.setText( "Стоп АВТ" );
    btnStop.setLayoutData( new GridData( SWT.LEFT, SWT.TOP, true, false ) );

    Button btnBlocks = new Button( buttonsHolder, SWT.PUSH );
    btnBlocks.setText( "Блокировки" );
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
