package ru.toxsoft.mcc.ws.mnemos.app.dialogs;

import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;

import ru.toxsoft.mcc.ws.mnemos.app.controls.*;

public class PanelTwoPositionsRegulator
    extends AbstractMccDialogPanel {

  public PanelTwoPositionsRegulator( Shell aParent, MccDialogContext aDialogContext ) {
    super( aParent, aDialogContext );
    init();
    dataProvider().start();
  }

  void init() {
    GridLayout gl = createGridLayout( 3, false );
    setLayout( gl );

    GridData gd = new GridData();
    gd.widthHint = 80;

    MccRtBooleanLabel rtLabel;

    CLabel l = new CLabel( this, SWT.CENTER );
    l.setText( dataInfo( "rtdCurrentValue" ).nmName() );

    MccRtLabel rtl = createRtLabel( this, SWT.BORDER, "rtdCurrentValue", tsContext() );
    rtl.getControl().setLayoutData( gd );

    l = new CLabel( this, SWT.CENTER );
    String strUnit = dialogContext().skObject().attrs().getStr( "atrMeasureValue" );
    if( strUnit.isBlank() ) {
      strUnit = "ед.изм.";
    }
    l.setText( strUnit );

    // Gwid gwid;
    // gwid = Gwid.createRtdata( "mcc.DigInput", "n2DI_VV_Alarm", "rtdCurrentValue" );
    // rtLabel = createRtBooleanLabel( this, gwid, ICONID_RED_LAMP, ICONID_GRAY_LAMP );
    //
    // gwid = Gwid.createRtdata( "mcc.DigInput", "n2DI_PRU_EmrStopVV", "rtdCurrentValue" );
    // rtLabel = createRtBooleanLabel( this, gwid, ICONID_RED_LAMP, ICONID_GRAY_LAMP );
    //
    // gwid = Gwid.createRtdata( "mcc.DigInput", "n2DI_Upwr380_Norm", "rtdCurrentValue" );
    // rtLabel = createRtBooleanLabel( this, gwid, ICONID_RED_LAMP, ICONID_GRAY_LAMP );
    //
    // gwid = Gwid.createRtdata( "mcc.CtrlSystem", "n2CtrlSystem", "rtdIrrEngineAlarm" );
    // rtLabel = createRtBooleanLabel( this, gwid, ICONID_GRAY_LAMP, ICONID_RED_LAMP );
    //
    // gwid = Gwid.createRtdata( "mcc.CtrlSystem", "n2CtrlSystem", "rtdIrrEngineBlock" );
    // rtLabel = createRtBooleanLabel( this, gwid, ICONID_GRAY_LAMP, ICONID_RED_LAMP );
    //
    // gwid = Gwid.createRtdata( "mcc.CtrlSystem", "n2CtrlSystem", "rtdRevEngineAlarm" );
    // rtLabel = createRtBooleanLabel( this, gwid, ICONID_GRAY_LAMP, ICONID_RED_LAMP );
    //
    // gwid = Gwid.createRtdata( "mcc.CtrlSystem", "n2CtrlSystem", "rtdRevEngineBlock" );
    // rtLabel = createRtBooleanLabel( this, gwid, ICONID_GRAY_LAMP, ICONID_RED_LAMP );
    //
    // gwid = Gwid.createRtdata( "mcc.CtrlSystem", "n2CtrlSystem", "rtdWaterAlarm" );
    // rtLabel = createRtBooleanLabel( this, gwid, ICONID_GRAY_LAMP, ICONID_RED_LAMP );
    //
    // gwid = Gwid.createRtdata( "mcc.CtrlSystem", "n2CtrlSystem", "rtdLoOil" );
    // rtLabel = createRtBooleanLabel( this, gwid, ICONID_GRAY_LAMP, ICONID_RED_LAMP );
    //
    // gwid = Gwid.createRtdata( "mcc.DigInput", "n2DI_PS_G2_Norm", "rtdCurrentValue" );
    // rtLabel = createRtBooleanLabel( this, gwid, ICONID_RED_LAMP, ICONID_GRAY_LAMP );
    //
    // gwid = Gwid.createRtdata( "mcc.DigInput", "n2DI_Usig_Norm", "rtdCurrentValue" );
    // rtLabel = createRtBooleanLabel( this, gwid, ICONID_RED_LAMP, ICONID_GRAY_LAMP );
    //
    // gwid = Gwid.createRtdata( "mcc.CtrlSystem", "n2CtrlSystem", "rtdEnableSiren" );
    // rtLabel = createRtBooleanLabel( this, gwid, ICONID_RED_LAMP, ICONID_GRAY_LAMP );
    //
  }

  /**
   * Показывает панель блокировок.
   *
   * @param aContext MccDialogContext - контекст диалога
   */
  public static void showDialog( MccDialogContext aContext ) {
    ITsGuiContext ctx = aContext.tsContext();
    Shell shell = ctx.get( Shell.class ).getShell();

    MccDialogWindow wnd = new MccDialogWindow( shell, aContext.skObject().nmName() );
    PanelTwoPositionsRegulator panel = new PanelTwoPositionsRegulator( wnd.shell(), aContext );
    panel.layout();
    wnd.open();
  }

}
