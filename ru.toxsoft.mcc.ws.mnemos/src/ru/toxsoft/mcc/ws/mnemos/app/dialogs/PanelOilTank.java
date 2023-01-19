package ru.toxsoft.mcc.ws.mnemos.app.dialogs;

import static ru.toxsoft.mcc.ws.mnemos.IMccWsMnemosConstants.*;

import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tslib.gw.gwid.*;

import ru.toxsoft.mcc.ws.mnemos.app.controls.*;
import ru.toxsoft.mcc.ws.mnemos.app.rt.chart.data_aliases.*;
import ru.toxsoft.mcc.ws.mnemos.app.utils.*;

public class PanelOilTank
    extends AbstractMccDialogPanel {

  static final String CLSID_DIG_INPUT = "mcc.DigInput"; //$NON-NLS-1$

  RtDataAliasHelper aliasHelper;

  public PanelOilTank( Shell aParent, MccDialogContext aDialogContext ) {
    super( aParent, aDialogContext );
    init();
    dataProvider().start();
  }

  void init() {
    GridLayout gl = createGridLayout( 1, false );
    setLayout( gl );

    aliasHelper = new RtDataAliasHelper( skConn() );

    Gwid gwid;
    gwid = Gwid.createRtdata( CLSID_DIG_INPUT, "n2DI_L1_LOil", "rtdCurrentValue" ); //$NON-NLS-1$ //$NON-NLS-2$
    createRtLabel( gwid, ICONID_GREEN_LAMP, ICONID_RED_LAMP );

    gwid = Gwid.createRtdata( CLSID_DIG_INPUT, "n2DI_L1_HOil", "rtdCurrentValue" ); //$NON-NLS-1$ //$NON-NLS-2$
    createRtLabel( gwid, ICONID_GREEN_LAMP, ICONID_YELLOW_LAMP );
  }

  /**
   * Показывает панель блокировок.
   *
   * @param aContext MccDialogContext - контекст диалога
   */
  public static void showDialog( MccDialogContext aContext ) {
    ITsGuiContext ctx = aContext.tsContext();
    Shell shell = ctx.get( Shell.class ).getShell();

    MccDialogWindow wnd = new MccDialogWindow( shell, "Маслобак" );
    PanelOilTank panel = new PanelOilTank( wnd.shell(), aContext );
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
