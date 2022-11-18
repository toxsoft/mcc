package ru.toxsoft.mcc.ws.mnemos.app.dialogs;

import org.eclipse.swt.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.ctx.impl.*;
import org.toxsoft.core.tsgui.dialogs.datarec.*;
import org.toxsoft.core.tsgui.graphics.icons.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.gw.gwid.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.uskat.base.gui.conn.*;
import org.toxsoft.uskat.base.gui.glib.*;
import org.toxsoft.uskat.core.*;
import org.toxsoft.uskat.core.api.sysdescr.*;
import org.toxsoft.uskat.core.api.sysdescr.dto.*;

import ru.toxsoft.mcc.ws.mnemos.app.valed.*;

/**
 * Базовый класс панелей для диалогов настройки проекта МосКокс.
 * <p>
 *
 * @author vs
 */
public abstract class AbstractMccRtPanel
    extends AbstractTsDialogPanel<Object, MccDialogContext> {

  private final ISkCoreApi coreApi;

  private RtValedsPanel rtPanel;

  /**
   * Конструктор.<br>
   *
   * @param aParent Composite - родительская компонента
   * @param aOwnerDialog TsDialog - родительский диалог
   */
  public AbstractMccRtPanel( Composite aParent, TsDialog<Object, MccDialogContext> aOwnerDialog ) {
    super( aParent, aOwnerDialog );
    this.setLayout( new FillLayout() );
    coreApi = environ().tsContext().get( ISkConnectionSupplier.class ).defConn().coreApi();
    rtPanel = new RtValedsPanel( this, tsContext() );
    // createContentPanel();
    // this.setLayout( new BorderLayout() );
    // init();
  }

  // ------------------------------------------------------------------------------------
  // AbstractTsDialogPanel
  //

  @Override
  protected void doSetDataRecord( Object aData ) {
    // TODO Auto-generated method stub

  }

  @Override
  protected Object doGetDataRecord() {
    // TODO Auto-generated method stub
    return null;
  }

  // public static void showDialog( MccDialogContext aContext ) {
  // IDialogPanelCreator<Object, MccDialogContext> creator = AbstractMccRtPanel::new;
  // ITsGuiContext ctx = aContext.tsContext();
  // ITsDialogInfo dlgInfo = new TsDialogInfo( aContext.tsContext(), "DLG_T_FILL_INFO", "STR_MSG_FILL_INFO" );
  // // ITsPoint p = new TsPoint( 4 * ABOUT_ICON_SIZE.size(), 2 * ABOUT_ICON_SIZE.size() + 50 );
  // // cdi.setMaxSize( p );
  // TsDialog<Object, MccDialogContext> d = new TsDialog<>( dlgInfo, null, aContext, creator );
  // d.execData();
  // }

  // ------------------------------------------------------------------------------------
  //
  //

  RtValedsPanel contentPanel() {
    return rtPanel;
  }

  Group createGroup( Composite aParent, String aName, int aColumnsCount, boolean aEqualSize ) {
    Group group = new Group( aParent, SWT.NONE );
    group.setText( aName );
    GridLayout gl = new GridLayout( aColumnsCount, aEqualSize );
    gl.verticalSpacing = 0;
    group.setLayout( gl );
    group.setLayoutData( new GridData( SWT.FILL, SWT.TOP, true, false ) );
    return group;
  }

  ValedBooleanCheckAdv createBooleanIndicator( Composite aParent, Gwid aGwid, String aText, String aTrueImageId,
      String aFalseImageId ) {

    ITsGuiContext ctx = new TsGuiContext( tsContext() );
    IOptionSetEdit params = ctx.params();
    ValedBooleanCheckAdv.OPDEF_TRUE_TEXT.setValue( params, AvUtils.avStr( aText ) );
    ValedBooleanCheckAdv.OPDEF_FALSE_TEXT.setValue( params, AvUtils.avStr( aText ) );

    ValedBooleanCheckAdv.OPDEF_TRUE_ICON_ID.setValue( params, AvUtils.avStr( aTrueImageId ) );
    ValedBooleanCheckAdv.OPDEF_FALSE_ICON_ID.setValue( params, AvUtils.avStr( aFalseImageId ) );

    ValedBooleanCheckAdv.OPDEF_ICON_SIZE.setValue( params, AvUtils.avValobj( EIconSize.IS_24X24 ) );

    ValedBooleanCheckAdv valed = new ValedBooleanCheckAdv( ctx );
    Control ctrl = valed.createControl( aParent );
    ctrl.setLayoutData( new GridData( SWT.FILL, SWT.TOP, true, false ) );
    return valed;
  }

  ValedBooleanCheckAdv createRtBooleanIndicator( Composite aParent, String aDataId, String aTrueImageId,
      String aFalseImageId ) {

    ISkClassInfo classInfo = coreApi.sysdescr().getClassInfo( environ().skObject().classId() );
    IStridablesList<IDtoRtdataInfo> rtdInfoes = classInfo.rtdata().list();
    String text = classInfo.rtdata().list().getByKey( aDataId ).nmName();

    ITsGuiContext ctx = new TsGuiContext( tsContext() );
    IOptionSetEdit params = ctx.params();
    ValedBooleanCheckAdv.OPDEF_TRUE_TEXT.setValue( params, AvUtils.avStr( text ) );
    ValedBooleanCheckAdv.OPDEF_FALSE_TEXT.setValue( params, AvUtils.avStr( text ) );

    ValedBooleanCheckAdv.OPDEF_TRUE_ICON_ID.setValue( params, AvUtils.avStr( aTrueImageId ) );
    ValedBooleanCheckAdv.OPDEF_FALSE_ICON_ID.setValue( params, AvUtils.avStr( aFalseImageId ) );

    ValedBooleanCheckAdv.OPDEF_ICON_SIZE.setValue( params, AvUtils.avValobj( EIconSize.IS_24X24 ) );

    ValedBooleanCheckAdv valed = new ValedBooleanCheckAdv( ctx );
    Control ctrl = valed.createControl( aParent );
    ctrl.setLayoutData( new GridData( SWT.FILL, SWT.TOP, true, false ) );
    return valed;
  }

  ValedFloatingTextCommand createFloatingEditor( Composite aParent, Gwid aDataGwid, String aCommandId ) {
    TsNullArgumentRtException.checkNulls( aParent, aDataGwid );
    TsGuiContext ctx = new TsGuiContext( tsContext() );
    IOptionSetEdit params = ctx.params();
    AbstractValedSkCommand.OPDEF_CLASS_ID.setValue( params, AvUtils.avStr( aDataGwid.classId() ) );
    AbstractValedSkCommand.OPDEF_OBJ_STRID.setValue( params, AvUtils.avStr( aDataGwid.strid() ) );
    AbstractValedSkCommand.OPDEF_DATA_ID.setValue( params, AvUtils.avStr( aDataGwid.propId() ) );
    AbstractValedSkCommand.OPDEF_COMMAND_ID.setValue( params, AvUtils.avStr( aCommandId ) );
    ValedFloatingTextCommand valed = new ValedFloatingTextCommand( ctx );
    valed.createControl( aParent );
    GridData gd = new GridData();
    gd.widthHint = 130;
    gd.minimumWidth = 130;
    valed.getControl().setLayoutData( gd );
    rtPanel.defineRtData( aDataGwid, valed );
    return valed;
  }

  MccValedAvBooleanCheckCommand createCheckEditor( Composite aParent, Gwid aDataGwid, String aCommandId,
      String aFalseImageId, String aTrueImageId ) {
    TsNullArgumentRtException.checkNulls( aParent, aDataGwid );
    TsGuiContext ctx = new TsGuiContext( tsContext() );
    IOptionSetEdit params = ctx.params();
    MccValedAvBooleanCheckCommand.OPDEF_FALSE_ICON_ID.setValue( params, AvUtils.avStr( aFalseImageId ) );
    MccValedAvBooleanCheckCommand.OPDEF_TRUE_ICON_ID.setValue( params, AvUtils.avStr( aTrueImageId ) );
    MccValedAvBooleanCheckCommand.OPDEF_ICON_SIZE.setValue( params, AvUtils.avValobj( EIconSize.IS_24X24 ) );

    MccValedAvBooleanCheckCommand.OPDEF_CLASS_ID.setValue( params, AvUtils.avStr( aDataGwid.classId() ) );
    MccValedAvBooleanCheckCommand.OPDEF_OBJ_STRID.setValue( params, AvUtils.avStr( aDataGwid.strid() ) );
    MccValedAvBooleanCheckCommand.OPDEF_DATA_ID.setValue( params, AvUtils.avStr( aDataGwid.propId() ) );
    MccValedAvBooleanCheckCommand.OPDEF_COMMAND_ID.setValue( params, AvUtils.avStr( aCommandId ) );
    MccValedAvBooleanCheckCommand valed = new MccValedAvBooleanCheckCommand( ctx );
    Control ctrl = valed.createControl( aParent );
    ctrl.setLayoutData( new GridData( SWT.CENTER, SWT.CENTER, false, false ) );

    rtPanel.defineRtData( aDataGwid, valed );

    return valed;
  }

  // protected abstract void createContentPanel();

}
