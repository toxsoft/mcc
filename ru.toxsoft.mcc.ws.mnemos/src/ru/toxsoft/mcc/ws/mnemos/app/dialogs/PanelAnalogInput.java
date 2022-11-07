package ru.toxsoft.mcc.ws.mnemos.app.dialogs;

import static ru.toxsoft.mcc.ws.mnemos.IMccWsMnemosConstants.*;

import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.dialogs.datarec.*;
import org.toxsoft.core.tsgui.graphics.fonts.impl.*;
import org.toxsoft.core.tsgui.graphics.icons.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.gw.gwid.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.uskat.base.gui.conn.*;
import org.toxsoft.uskat.core.*;
import org.toxsoft.uskat.core.api.objserv.*;
import org.toxsoft.uskat.core.api.sysdescr.*;
import org.toxsoft.uskat.core.api.sysdescr.dto.*;

/**
 * @author vs
 */
public class PanelAnalogInput
    extends AbstractMccRtPanel {

  public PanelAnalogInput( Composite aParent, TsDialog<Object, MccDialogContext> aOwnerDialog ) {
    super( aParent, aOwnerDialog );
    init();
    contentPanel().rtStart();
  }

  void init() {

    GridLayout layout = new GridLayout( 1, false );
    contentPanel().setLayout( layout );

    createValueGroup();
    createLimitsGroup();

    // setLayout( new FillLayout() );
    // Composite bkPanel = new Composite( this, SWT.NONE );
    // bkPanel.setLayout( new RowLayout( SWT.VERTICAL ) );
    // CLabel l = new CLabel( bkPanel, SWT.CENTER );
    // l.setText( "Test string" );
    //
    // ITsGuiContext ctx = new TsGuiContext( tsContext() );
    // IOptionSetEdit params = ctx.params();
    // ValedBooleanCheckAdv.OPDEF_TRUE_TEXT.setValue( params, AvUtils.avStr( "true" ) ); //$NON-NLS-1$
    // ValedBooleanCheckAdv.OPDEF_FALSE_TEXT.setValue( params, AvUtils.avStr( "false" ) ); //$NON-NLS-1$
    //
    // ValedBooleanCheckAdv.OPDEF_TRUE_ICON_ID.setValue( params, AvUtils.avStr( ICONID_CHECK_TRUE ) );
    // ValedBooleanCheckAdv.OPDEF_FALSE_ICON_ID.setValue( params, AvUtils.avStr( ICONID_CHECK_FALSE ) );
    //
    // ValedAvBooleanCheckAdv v1 = new ValedAvBooleanCheckAdv( ctx );
    // v1.createControl( bkPanel );
    // v1.setValue( AvUtils.AV_TRUE );
    //
    // ValedIntegerTextCommand t1 = new ValedIntegerTextCommand( ctx );
    // t1.createControl( bkPanel );
    // t1.setValue( AvUtils.AV_0 );
    //
    // ValedIntegerTextCommand t2 = new ValedIntegerTextCommand( ctx );
    // t2.createControl( bkPanel );
    // t2.setValue( AvUtils.AV_0 );
  }

  Group createValueGroup() {
    Group g = createGroup( contentPanel(), "Выходное значение", 2 );

    Composite leftComp = new Composite( g, SWT.NONE );
    // leftComp.setLayout( new FillLayout() );
    // leftComp.setLayout( new BorderLayout() );
    leftComp.setLayout( new GridLayout( 1, false ) );
    leftComp.setLayoutData( new GridData( SWT.FILL, SWT.TOP, true, false, 1, 3 ) );
    // leftComp.setData( AWTLayout.KEY_PREFERRED_SIZE, new java.awt.Dimension( -1, 63 ) );

    CLabel l = new CLabel( leftComp, SWT.CENTER );
    l.setLayoutData( new GridData( SWT.FILL, SWT.CENTER, true, true ) );
    // l.setLayoutData( BorderLayout.CENTER );
    FontInfo fi = new FontInfo( "Arial", 24, true, false ); //$NON-NLS-1$
    l.setFont( fontManager().getFont( fi ) );
    l.setText( "123.45" );

    Composite rightComp = new Composite( g, SWT.NONE );
    GridLayout gl = new GridLayout( 1, false );
    gl.verticalSpacing = 0;
    rightComp.setLayout( gl );
    rightComp.setLayoutData( new GridData( SWT.FILL, SWT.TOP, true, false ) );

    createBooleanIndicator( rightComp, null, "Авария", ICONID_RED_LAMP, ICONID_GRAY_LAMP );
    createBooleanIndicator( rightComp, null, "Предупреждение", ICONID_YELLOW_LAMP, ICONID_GRAY_LAMP );
    createBooleanIndicator( rightComp, null, "Блокировка включена", ICONID_GRAY_LAMP, ICONID_YELLOW_LAMP );

    return g;
  }

  Group createLimitsGroup() {
    Group g = createGroup( contentPanel(), "Предельные значения", 6 );

    CLabel l = new CLabel( g, SWT.CENTER );
    l.setLayoutData( new GridData( SWT.LEFT, SWT.TOP, false, false, 2, 1 ) );

    l = new CLabel( g, SWT.CENTER );
    l.setText( "Индикация" );
    l.setLayoutData( new GridData( SWT.LEFT, SWT.CENTER, false, false, 2, 1 ) );

    l = new CLabel( g, SWT.CENTER );
    l.setText( "Генерация" );
    l.setLayoutData( new GridData( SWT.LEFT, SWT.CENTER, false, false, 2, 1 ) );

    createLimitsRow( g, "Верхний аварийный предел: ", ICONID_RED_LAMP );
    createLimitsRow( g, "Верхний предупредительный предел: ", ICONID_YELLOW_LAMP );

    return g;
  }

  private void createLimitsRow( Composite aParent, String aText, String aTrueImageId ) {
    CLabel l = new CLabel( aParent, SWT.CENTER );
    l.setText( aText );

    // ITsGuiContext valedContext = new TsGuiContext( tsContext() );
    // ValedFloatingTextCommand valed = new ValedFloatingTextCommand( valedContext );
    // valed.createControl( aParent );

    String classId = "mcc.AnalogInput";
    String dataId = "rtdSetPoint1";
    String objId = "n2AI_P62";

    ISkCoreApi coreApi = tsContext().get( ISkConnectionSupplier.class ).defConn().coreApi();
    IList<ISkObject> objs = coreApi.objService().listObjs( classId, true );
    for( ISkObject obj : objs ) {
      System.out.println( obj.skid().toString() );
    }

    ISkClassInfo clsInfo = coreApi.sysdescr().getClassInfo( "mcc.AnalogInput" );
    ISkClassProps<IDtoRtdataInfo> info = clsInfo.rtdata();
    for( IDtoRtdataInfo dInfo : info.list() ) {
      System.out.println( dInfo.id() );
    }

    Gwid gwid = Gwid.createRtdata( classId, objId, dataId );

    createFloatinEditor( aParent, gwid, "cmdSetPoint1" );

    // createBooleanIndicator( aParent, null, TsLibUtils.EMPTY_STRING, aTrueImageId, ICONID_GRAY_LAMP );
    createBooleanIndicator( aParent, null, "a", aTrueImageId, ICONID_GRAY_LAMP );
    Button btn = new Button( aParent, SWT.CHECK );
    btn.setText( "a" );
    btn.setImage( iconManager().loadStdIcon( aTrueImageId, EIconSize.IS_24X24 ) );

    createBooleanIndicator( aParent, null, TsLibUtils.EMPTY_STRING, aTrueImageId, ICONID_GRAY_LAMP );
    btn = new Button( aParent, SWT.CHECK );
  }

  public static void showDialog( MccDialogContext aContext ) {
    IDialogPanelCreator<Object, MccDialogContext> creator = PanelAnalogInput::new;
    ITsGuiContext ctx = aContext.tsContext();
    ITsDialogInfo dlgInfo = new TsDialogInfo( ctx, "DLG_T_FILL_INFO", "STR_MSG_FILL_INFO" );
    // ITsPoint p = new TsPoint( 4 * ABOUT_ICON_SIZE.size(), 2 * ABOUT_ICON_SIZE.size() + 50 );
    // cdi.setMaxSize( p );
    TsDialog<Object, MccDialogContext> d = new TsDialog<>( dlgInfo, null, aContext, creator );
    d.execData();
  }

}
