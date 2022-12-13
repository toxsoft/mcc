package ru.toxsoft.mcc.ws.mnemos.app.dialogs;

import static ru.toxsoft.mcc.ws.mnemos.IMccWsMnemosConstants.*;

import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tslib.gw.gwid.*;

import ru.toxsoft.mcc.ws.mnemos.app.controls.*;

public class MccBlockingPanel
    extends AbstractMccDialogPanel {

  public MccBlockingPanel( Shell aParent, MccDialogContext aDialogContext ) {
    super( aParent, aDialogContext );
    init();
  }

  void init() {
    GridLayout gl = createGridLayout( 1, false );
    setLayout( gl );

    MccRtBooleanLabel rtLabel;
    Gwid gwid;
    gwid = Gwid.createRtdata( "mcc.DigInput", "n2DI_VV_Alarm", "rtdCurrentValue" );
    rtLabel = createRtBooleanLabel( this, gwid, ICONID_RED_LAMP, ICONID_GRAY_LAMP );

    gwid = Gwid.createRtdata( "mcc.DigInput", "n2DI_PRU_EmrStopVV", "rtdCurrentValue" );
    rtLabel = createRtBooleanLabel( this, gwid, ICONID_RED_LAMP, ICONID_GRAY_LAMP );

    gwid = Gwid.createRtdata( "mcc.DigInput", "n2DI_Upwr380_Norm", "rtdCurrentValue" );
    rtLabel = createRtBooleanLabel( this, gwid, ICONID_RED_LAMP, ICONID_GRAY_LAMP );

    gwid = Gwid.createRtdata( "mcc.CtrlSystem", "n2CtrlSystem", "rtdIrrEngineAlarm" );
    rtLabel = createRtBooleanLabel( this, gwid, ICONID_GRAY_LAMP, ICONID_RED_LAMP );

    gwid = Gwid.createRtdata( "mcc.CtrlSystem", "n2CtrlSystem", "rtdIrrEngineBlock" );
    rtLabel = createRtBooleanLabel( this, gwid, ICONID_GRAY_LAMP, ICONID_RED_LAMP );

    gwid = Gwid.createRtdata( "mcc.CtrlSystem", "n2CtrlSystem", "rtdRevEngineAlarm" );
    rtLabel = createRtBooleanLabel( this, gwid, ICONID_GRAY_LAMP, ICONID_RED_LAMP );

    gwid = Gwid.createRtdata( "mcc.CtrlSystem", "n2CtrlSystem", "rtdRevEngineBlock" );
    rtLabel = createRtBooleanLabel( this, gwid, ICONID_GRAY_LAMP, ICONID_RED_LAMP );

    gwid = Gwid.createRtdata( "mcc.CtrlSystem", "n2CtrlSystem", "rtdWaterAlarm" );
    rtLabel = createRtBooleanLabel( this, gwid, ICONID_GRAY_LAMP, ICONID_RED_LAMP );

    gwid = Gwid.createRtdata( "mcc.CtrlSystem", "n2CtrlSystem", "rtdLoOil" );
    rtLabel = createRtBooleanLabel( this, gwid, ICONID_GRAY_LAMP, ICONID_RED_LAMP );

    gwid = Gwid.createRtdata( "mcc.DigInput", "n2DI_PS_G2_Norm", "rtdCurrentValue" );
    rtLabel = createRtBooleanLabel( this, gwid, ICONID_RED_LAMP, ICONID_GRAY_LAMP );

    gwid = Gwid.createRtdata( "mcc.DigInput", "n2DI_Usig_Norm", "rtdCurrentValue" );
    rtLabel = createRtBooleanLabel( this, gwid, ICONID_RED_LAMP, ICONID_GRAY_LAMP );

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

    MccDialogWindow wnd = new MccDialogWindow( shell, aContext.skObject().readableName() );
    MccBlockingPanel panel = new MccBlockingPanel( wnd.shell(), aContext );
    panel.layout();
    wnd.open();
  }

}
