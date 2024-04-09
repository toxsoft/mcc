package ru.toxsoft.mcc.ws.mnemos.app.dialogs;

import static ru.toxsoft.mcc.ws.mnemos.IMccWsMnemosConstants.*;
import static ru.toxsoft.mcc.ws.mnemos.app.dialogs.IVjResources.*;

import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tslib.gw.gwid.*;

import ru.toxsoft.mcc.ws.mnemos.app.controls.*;
import ru.toxsoft.mcc.ws.mnemos.app.rt.chart.data_aliases.*;
import ru.toxsoft.mcc.ws.mnemos.app.utils.*;

/**
 * Панель блокировок.
 * <p>
 *
 * @author vs
 */
public class MccBlockingPanel
    extends AbstractMccDialogPanel {

  static final String CLSID_DIG_INPUT = "mcc.DigInput"; //$NON-NLS-1$

  static final String CLSID_CTRL_SYSTEM = "mcc.CtrlSystem"; //$NON-NLS-1$

  RtDataAliasHelper aliasHelper;

  /**
   * Конструктор.
   *
   * @param aParent Shell - родительское окно
   * @param aDialogContext MccDialogContext - контекст панели
   */
  public MccBlockingPanel( Shell aParent, MccDialogContext aDialogContext ) {
    super( aParent, aDialogContext );
    init();
    dataProvider().start();
  }

  void init() {
    GridLayout gl = createGridLayout( 1, false );
    setLayout( gl );

    aliasHelper = new RtDataAliasHelper( skConn() );

    Gwid gwid;
    gwid = Gwid.createRtdata( CLSID_DIG_INPUT, "n2DI_VV_Alarm", "rtdCurrentValue" ); //$NON-NLS-1$ //$NON-NLS-2$
    createRtLabel( gwid, ICONID_RED_LAMP, ICONID_GRAY_LAMP );

    // gwid = Gwid.createRtdata( CLSID_DIG_INPUT, "n2DI_PRU_EmrStopVV", "rtdCurrentValue" ); //$NON-NLS-1$ //$NON-NLS-2$
    // gwid = Gwid.createRtdata( CLSID_CTRL_SYSTEM, "n2CtrlSystem", "rtdEmergencyStop" ); //$NON-NLS-1$ //$NON-NLS-2$
    // createRtLabel( gwid, ICONID_RED_LAMP, ICONID_GRAY_LAMP );
    // gwid = Gwid.createRtdata( CLSID_CTRL_SYSTEM, "n2CtrlSystem", "rtdEmergencyStop" ); //$NON-NLS-1$ //$NON-NLS-2$
    gwid = Gwid.createRtdata( "mcc.MainSwitch", "n2MainSwitch", "rtdEmergencyStop" ); //$NON-NLS-1$ //$NON-NLS-2$
    createRtLabel( gwid, ICONID_RED_LAMP, ICONID_GRAY_LAMP );

    gwid = Gwid.createRtdata( CLSID_DIG_INPUT, "n2DI_Upwr380_Norm", "rtdCurrentValue" ); //$NON-NLS-1$ //$NON-NLS-2$
    createRtLabel( gwid, ICONID_RED_LAMP, ICONID_GRAY_LAMP );

    gwid = Gwid.createRtdata( CLSID_CTRL_SYSTEM, "n2CtrlSystem", "rtdIrrEngineAlarm" ); //$NON-NLS-1$ //$NON-NLS-2$
    createRtLabel( gwid, ICONID_GRAY_LAMP, ICONID_RED_LAMP );

    gwid = Gwid.createRtdata( CLSID_CTRL_SYSTEM, "n2CtrlSystem", "rtdIrrEngineBlock" ); //$NON-NLS-1$ //$NON-NLS-2$
    createRtLabel( gwid, ICONID_GRAY_LAMP, ICONID_RED_LAMP );

    gwid = Gwid.createRtdata( CLSID_CTRL_SYSTEM, "n2CtrlSystem", "rtdRevEngineAlarm" ); //$NON-NLS-1$ //$NON-NLS-2$
    createRtLabel( gwid, ICONID_GRAY_LAMP, ICONID_RED_LAMP );

    gwid = Gwid.createRtdata( CLSID_CTRL_SYSTEM, "n2CtrlSystem", "rtdRevEngineBlock" ); //$NON-NLS-1$ //$NON-NLS-2$
    createRtLabel( gwid, ICONID_GRAY_LAMP, ICONID_RED_LAMP );

    gwid = Gwid.createRtdata( CLSID_CTRL_SYSTEM, "n2CtrlSystem", "rtdWaterAlarm" ); //$NON-NLS-1$ //$NON-NLS-2$
    createRtLabel( gwid, ICONID_GRAY_LAMP, ICONID_RED_LAMP );

    gwid = Gwid.createRtdata( CLSID_CTRL_SYSTEM, "n2CtrlSystem", "rtdLoOil" ); //$NON-NLS-1$ //$NON-NLS-2$
    createRtLabel( gwid, ICONID_GRAY_LAMP, ICONID_RED_LAMP );

    gwid = Gwid.createRtdata( CLSID_DIG_INPUT, "n2DI_PS_G2_Norm", "rtdCurrentValue" ); //$NON-NLS-1$ //$NON-NLS-2$
    createRtLabel( gwid, ICONID_RED_LAMP, ICONID_GRAY_LAMP );

    gwid = Gwid.createRtdata( CLSID_DIG_INPUT, "n2DI_Usig_Norm", "rtdCurrentValue" ); //$NON-NLS-1$ //$NON-NLS-2$
    createRtLabel( gwid, ICONID_RED_LAMP, ICONID_GRAY_LAMP );

    gwid = Gwid.createRtdata( CLSID_CTRL_SYSTEM, "n2CtrlSystem", "rtdEnableSiren" ); //$NON-NLS-1$ //$NON-NLS-2$
    createRtBooleanLabel( this, gwid, ICONID_RED_LAMP, ICONID_GRAY_LAMP );
  }

  /**
   * Показывает панель блокировок.
   *
   * @param aContext MccDialogContext - контекст диалога
   */
  public static void showDialog( MccDialogContext aContext ) {
    ITsGuiContext ctx = aContext.tsContext();
    Shell shell = ctx.get( Shell.class ).getShell();

    MccDialogWindow wnd = new MccDialogWindow( shell, STR_BLOCKS );
    MccBlockingPanel panel = new MccBlockingPanel( wnd.shell(), aContext );
    panel.layout();
    wnd.open();
  }

  // ------------------------------------------------------------------------------------
  // Implementation
  //

  private MccRtBooleanLabel createRtLabel( Gwid aGwid, String aFalseIconId, String aTrueIconId ) {
    MccRtBooleanLabel rtLabel = createRtBooleanLabel( this, aGwid, aFalseIconId, aTrueIconId );
    IDataNameAlias alias = aliasHelper.alias( aGwid );
    if( alias != null ) {
      rtLabel.setName( alias.title() );
      rtLabel.setDescription( alias.description() );
    }
    return rtLabel;
  }

}
