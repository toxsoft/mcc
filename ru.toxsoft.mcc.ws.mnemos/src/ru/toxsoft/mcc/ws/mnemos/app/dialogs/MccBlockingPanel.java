package ru.toxsoft.mcc.ws.mnemos.app.dialogs;

import static ru.toxsoft.mcc.ws.mnemos.IMccWsMnemosConstants.*;

import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tslib.gw.gwid.*;

import ru.toxsoft.mcc.ws.mnemos.app.controls.*;
import ru.toxsoft.mcc.ws.mnemos.app.rt.chart.data_aliases.*;
import ru.toxsoft.mcc.ws.mnemos.app.utils.*;

public class MccBlockingPanel
    extends AbstractMccDialogPanel {

  RtDataAliasHelper aliasHelper;

  public MccBlockingPanel( Shell aParent, MccDialogContext aDialogContext ) {
    super( aParent, aDialogContext );
    init();
  }

  void init() {
    GridLayout gl = createGridLayout( 1, false );
    setLayout( gl );

    aliasHelper = new RtDataAliasHelper( skConn() );

    MccRtBooleanLabel rtLabel;
    Gwid gwid;
    gwid = Gwid.createRtdata( "mcc.DigInput", "n2DI_VV_Alarm", "rtdCurrentValue" );
    rtLabel = createRtLabel( gwid, ICONID_RED_LAMP, ICONID_GRAY_LAMP );

    gwid = Gwid.createRtdata( "mcc.DigInput", "n2DI_PRU_EmrStopVV", "rtdCurrentValue" );
    rtLabel = createRtLabel( gwid, ICONID_RED_LAMP, ICONID_GRAY_LAMP );

    gwid = Gwid.createRtdata( "mcc.DigInput", "n2DI_Upwr380_Norm", "rtdCurrentValue" );
    rtLabel = createRtLabel( gwid, ICONID_RED_LAMP, ICONID_GRAY_LAMP );

    gwid = Gwid.createRtdata( "mcc.CtrlSystem", "n2CtrlSystem", "rtdIrrEngineAlarm" );
    rtLabel = createRtLabel( gwid, ICONID_GRAY_LAMP, ICONID_RED_LAMP );

    gwid = Gwid.createRtdata( "mcc.CtrlSystem", "n2CtrlSystem", "rtdIrrEngineBlock" );
    rtLabel = createRtLabel( gwid, ICONID_GRAY_LAMP, ICONID_RED_LAMP );

    gwid = Gwid.createRtdata( "mcc.CtrlSystem", "n2CtrlSystem", "rtdRevEngineAlarm" );
    rtLabel = createRtLabel( gwid, ICONID_GRAY_LAMP, ICONID_RED_LAMP );

    gwid = Gwid.createRtdata( "mcc.CtrlSystem", "n2CtrlSystem", "rtdRevEngineBlock" );
    rtLabel = createRtLabel( gwid, ICONID_GRAY_LAMP, ICONID_RED_LAMP );

    gwid = Gwid.createRtdata( "mcc.CtrlSystem", "n2CtrlSystem", "rtdWaterAlarm" );
    rtLabel = createRtLabel( gwid, ICONID_GRAY_LAMP, ICONID_RED_LAMP );

    gwid = Gwid.createRtdata( "mcc.CtrlSystem", "n2CtrlSystem", "rtdLoOil" );
    rtLabel = createRtLabel( gwid, ICONID_GRAY_LAMP, ICONID_RED_LAMP );

    gwid = Gwid.createRtdata( "mcc.DigInput", "n2DI_PS_G2_Norm", "rtdCurrentValue" );
    rtLabel = createRtLabel( gwid, ICONID_RED_LAMP, ICONID_GRAY_LAMP );

    gwid = Gwid.createRtdata( "mcc.DigInput", "n2DI_Usig_Norm", "rtdCurrentValue" );
    rtLabel = createRtLabel( gwid, ICONID_RED_LAMP, ICONID_GRAY_LAMP );

    gwid = Gwid.createRtdata( "mcc.CtrlSystem", "n2CtrlSystem", "rtdEnableSiren" );
    rtLabel = createRtBooleanLabel( this, gwid, ICONID_RED_LAMP, ICONID_GRAY_LAMP );

  }

  /**
   * Показывает панель блокировок.
   *
   * @param aContext MccDialogContext - контекст диалога
   */
  public static void showDialog( MccDialogContext aContext ) {
    ITsGuiContext ctx = aContext.tsContext();
    Shell shell = ctx.get( Shell.class ).getShell();

    MccDialogWindow wnd = new MccDialogWindow( shell, "Блокировки" );
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
