package ru.toxsoft.mcc.ws.mnemos.app.dialogs;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.ITsGuiContext;
import org.toxsoft.core.tsgui.bricks.ctx.impl.TsGuiContext;
import org.toxsoft.core.tsgui.dialogs.datarec.AbstractTsDialogPanel;
import org.toxsoft.core.tsgui.dialogs.datarec.TsDialog;
import org.toxsoft.core.tsgui.graphics.icons.EIconSize;
import org.toxsoft.core.tslib.av.impl.AvUtils;
import org.toxsoft.core.tslib.av.opset.IOptionSetEdit;
import org.toxsoft.core.tslib.bricks.strid.coll.IStridablesList;
import org.toxsoft.core.tslib.gw.gwid.Gwid;
import org.toxsoft.core.tslib.utils.errors.TsNullArgumentRtException;
import org.toxsoft.uskat.core.api.objserv.ISkObject;
import org.toxsoft.uskat.core.api.sysdescr.ISkClassInfo;
import org.toxsoft.uskat.core.api.sysdescr.dto.IDtoRtdataInfo;
import org.toxsoft.uskat.core.connection.ISkConnection;
import org.toxsoft.uskat.core.gui.conn.ISkConnectionSupplier;
import org.toxsoft.uskat.core.utils.ISkConnected;

import ru.toxsoft.mcc.ws.mnemos.app.controls.MccRtBooleanLabel;
import ru.toxsoft.mcc.ws.mnemos.app.controls.MccRtTextEditor;
import ru.toxsoft.mcc.ws.mnemos.app.rt.MccRtDataProvider;
import ru.toxsoft.mcc.ws.mnemos.app.valed.MccValedAvBooleanCheckCommand;
import ru.toxsoft.mcc.ws.mnemos.app.valed.ValedBooleanCheckAdv;

/**
 * Базовый класс панелей для диалогов настройки проекта МосКокс.
 * <p>
 *
 * @author vs
 */
public abstract class AbstractMccRtPanel
    extends AbstractTsDialogPanel<Object, MccDialogContext>
    implements ISkConnected {

  // private final ISkCoreApi coreApi;

  // private RtValedsPanel rtPanel;

  private final MccRtDataProvider dataProvider;

  private final ISkConnection skConnection;

  /**
   * Конструктор.<br>
   *
   * @param aParent Composite - родительская компонента
   * @param aOwnerDialog TsDialog - родительский диалог
   */
  public AbstractMccRtPanel( Composite aParent, TsDialog<Object, MccDialogContext> aOwnerDialog ) {
    super( aParent, aOwnerDialog );
    skConnection = environ().tsContext().get( ISkConnectionSupplier.class ).defConn();
    dataProvider = new MccRtDataProvider( skConnection, environ().tsContext() );
    // this.setLayout( new FillLayout() );
    // coreApi = environ().tsContext().get( ISkConnectionSupplier.class ).defConn().coreApi();
    // rtPanel = new RtValedsPanel( this, tsContext() );
    // createContentPanel();
    // this.setLayout( new BorderLayout() );
    // init();
    addDisposeListener( aE -> dataProvider.dispose() );
  }

  // ------------------------------------------------------------------------------------
  // ISkConnected
  //

  @Override
  public ISkConnection skConn() {
    return skConnection;
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

  MccRtDataProvider dataProvider() {
    return dataProvider;
  }

  // ------------------------------------------------------------------------------------
  // Implementation
  //

  // RtValedsPanel contentPanel() {
  // return rtPanel;
  // }

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

    ISkClassInfo classInfo = coreApi().sysdescr().getClassInfo( environ().skObject().classId() );
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

  /**
   * Создает иконку для отображения логического значения без надписи.
   * <p>
   *
   * @param aParent Composite - родительская компонента
   * @param aDataId String - ИД данного
   * @param aFalseImageId String- ИД картинки для значения <b>true</b>
   * @param aTrueImageId String - ИД картинки для значения <b>true</b>
   * @return MccRtBooleanLabel - созданный контроль
   */
  MccRtBooleanLabel createRtBooleanIcon( Composite aParent, String aDataId, String aTrueImageId,
      String aFalseImageId ) {
    ISkObject skObj = environ().skObject();
    Gwid gwid = Gwid.createRtdata( skObj.classId(), skObj.strid(), aDataId );

    IDtoRtdataInfo dInfo = dataInfo( aDataId );
    MccRtBooleanLabel rtLabel;
    rtLabel = new MccRtBooleanLabel( gwid, aFalseImageId, aTrueImageId, EIconSize.IS_24X24, tsContext() );
    CLabel l = rtLabel.createControl( aParent, SWT.NONE );
    l.setToolTipText( dInfo.description() );

    dataProvider.addDataConsumer( rtLabel );
    return rtLabel;
  }

  /**
   * Создает надпись с иконкой, отображающей логическое значение.
   * <p>
   * Надпись берется из описания данного.
   *
   * @param aParent Composite - родительская компонента
   * @param aDataId String - ИД данного
   * @param aFalseImageId String- ИД картинки для значения <b>true</b>
   * @param aTrueImageId String - ИД картинки для значения <b>true</b>
   * @return MccRtBooleanLabel - созданный контроль
   */
  MccRtBooleanLabel createRtBooleanLabel( Composite aParent, String aDataId, String aFalseImageId,
      String aTrueImageId ) {
    ISkObject skObj = environ().skObject();
    Gwid gwid = Gwid.createRtdata( skObj.classId(), skObj.strid(), aDataId );

    IDtoRtdataInfo dInfo = dataInfo( aDataId );
    MccRtBooleanLabel rtLabel;
    rtLabel = new MccRtBooleanLabel( gwid, aFalseImageId, aTrueImageId, EIconSize.IS_24X24, tsContext() );
    CLabel l = rtLabel.createControl( aParent, SWT.NONE );
    l.setText( dInfo.nmName() );
    l.setToolTipText( dInfo.description() );

    dataProvider.addDataConsumer( rtLabel );

    return rtLabel;
  }

  MccRtTextEditor createRtTextEditor( ISkObject aSkObj, String aDataId, String aCmdId, ITsGuiContext aTsContext ) {
    MccRtTextEditor rtEditor = new MccRtTextEditor( aSkObj, aDataId, aCmdId, aTsContext );
    dataProvider.addDataConsumer( rtEditor );
    return rtEditor;
  }

  // ValedFloatingTextCommand createFloatingEditor( Composite aParent, Gwid aDataGwid, String aCommandId ) {
  // TsNullArgumentRtException.checkNulls( aParent, aDataGwid );
  // TsGuiContext ctx = new TsGuiContext( tsContext() );
  // IOptionSetEdit params = ctx.params();
  // AbstractValedSkCommand.OPDEF_CLASS_ID.setValue( params, AvUtils.avStr( aDataGwid.classId() ) );
  // AbstractValedSkCommand.OPDEF_OBJ_STRID.setValue( params, AvUtils.avStr( aDataGwid.strid() ) );
  // AbstractValedSkCommand.OPDEF_DATA_ID.setValue( params, AvUtils.avStr( aDataGwid.propId() ) );
  // AbstractValedSkCommand.OPDEF_COMMAND_ID.setValue( params, AvUtils.avStr( aCommandId ) );
  // ValedFloatingTextCommand valed = new ValedFloatingTextCommand( ctx );
  // valed.createControl( aParent );
  // GridData gd = new GridData();
  // gd.widthHint = 130;
  // gd.minimumWidth = 130;
  // valed.getControl().setLayoutData( gd );
  // rtPanel.defineRtData( aDataGwid, valed );
  // return valed;
  // }

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

    // rtPanel.defineRtData( aDataGwid, valed );

    return valed;
  }

  private IDtoRtdataInfo dataInfo( String aDataId ) {
    ISkClassInfo clsInfo = coreApi().sysdescr().getClassInfo( environ().skObject().classId() );
    return clsInfo.rtdata().list().getByKey( aDataId );
  }

}
