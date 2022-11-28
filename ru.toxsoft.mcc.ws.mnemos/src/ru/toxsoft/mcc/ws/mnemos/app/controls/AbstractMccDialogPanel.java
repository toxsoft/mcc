package ru.toxsoft.mcc.ws.mnemos.app.controls;

import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.ctx.impl.*;
import org.toxsoft.core.tsgui.graphics.icons.*;
import org.toxsoft.core.tsgui.panels.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.gw.gwid.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.uskat.base.gui.conn.*;
import org.toxsoft.uskat.core.api.objserv.*;
import org.toxsoft.uskat.core.api.sysdescr.*;
import org.toxsoft.uskat.core.api.sysdescr.dto.*;
import org.toxsoft.uskat.core.connection.*;
import org.toxsoft.uskat.core.utils.*;

import ru.toxsoft.mcc.ws.mnemos.app.dialogs.*;
import ru.toxsoft.mcc.ws.mnemos.app.rt.*;
import ru.toxsoft.mcc.ws.mnemos.app.valed.*;

/**
 * Базовый класс панелей для диалогов настройки проекта МосКокс.
 * <p>
 *
 * @author vs
 */
public abstract class AbstractMccDialogPanel
    extends TsPanel
    implements ISkConnected {

  private final MccRtDataProvider dataProvider;

  private final ISkConnection skConnection;

  private final MccDialogContext dlgContext;

  /**
   * Конструктор.<br>
   *
   * @param aParent Composite - родительская компонента
   * @param aDialogContext MccDialogContext - контекст диалога
   */
  public AbstractMccDialogPanel( Composite aParent, MccDialogContext aDialogContext ) {
    super( aParent, aDialogContext.tsContext() );
    dlgContext = aDialogContext;
    skConnection = tsContext().get( ISkConnectionSupplier.class ).defConn();
    dataProvider = new MccRtDataProvider( skConnection, tsContext() );
    addDisposeListener( aE -> dataProvider.dispose() );
  }

  // ------------------------------------------------------------------------------------
  // ITsContextable
  //
  @Override
  public ITsGuiContext tsContext() {
    return dlgContext.tsContext();
  }

  // ------------------------------------------------------------------------------------
  // ISkConnected
  //

  @Override
  public ISkConnection skConn() {
    return skConnection;
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  public MccDialogContext dialogContext() {
    return dlgContext;
  }

  public MccRtDataProvider dataProvider() {
    return dataProvider;
  }

  /**
   * Создает однострочный текст, отображающий значение РВ-данного, на основе {@link CLabel}.<br>
   *
   * @param aParent Composite - родительская компонента
   * @param aStyle int - swt стиль контроля
   * @param aDataId String - ИД данного
   * @param aTsContext ITsGuiContext - соответствующий контекст
   * @return MccRtLabel - однострочный текст, отображающий значение РВ-данного
   */
  public MccRtLabel createRtLabel( Composite aParent, int aStyle, String aDataId, ITsGuiContext aTsContext ) {
    MccRtLabel rtl = new MccRtLabel( dlgContext.skObject(), aDataId, aTsContext, null );
    dataProvider.addDataConsumer( rtl );
    rtl.createControl( aParent, aStyle );
    return rtl;
  }

  /**
   * Создает checkbox для посылки команды.<b>
   *
   * @param aParent Composite - родительская компонента
   * @param aCmdId String - ИД команды
   * @param aDataId String - ИД данного
   * @param aTsContext ITsGuiContext - соответствующий контекст
   * @return MccCheckCmdButton - checkbox для посылки команды
   */
  public MccCheckCmdButton createCheckCmdButton( Composite aParent, String aCmdId, String aDataId,
      ITsGuiContext aTsContext ) {
    ISkObject skObj = dialogContext().skObject();
    Gwid cmdGwid = Gwid.createCmd( skObj.classId(), skObj.strid(), aCmdId );
    Gwid dataGwid = Gwid.createRtdata( skObj.classId(), skObj.strid(), aDataId );
    MccCheckCmdButton btn = new MccCheckCmdButton( cmdGwid, dataGwid, aTsContext );
    btn.createControl( aParent, SWT.CHECK );
    dataProvider.addDataConsumer( btn );
    return btn;
  }

  // ------------------------------------------------------------------------------------
  // Implementation
  //

  // RtValedsPanel contentPanel() {
  // return rtPanel;
  // }

  public Group createGroup( Composite aParent, String aName, int aColumnsCount, boolean aEqualSize ) {
    Group group = new Group( aParent, SWT.NONE );
    group.setText( aName );
    GridLayout gl = new GridLayout( aColumnsCount, aEqualSize );
    gl.verticalSpacing = 2;
    gl.marginTop = 0;
    gl.marginBottom = 0;
    group.setLayout( gl );
    group.setLayoutData( new GridData( SWT.FILL, SWT.TOP, true, false ) );

    FontData fd = group.getFont().getFontData()[0];
    Font f = fontManager().getFont( fd.getName(), (int)(fd.getHeight() * 1.2), SWT.BOLD );
    group.setFont( f );
    return group;
  }

  public ValedBooleanCheckAdv createBooleanIndicator( Composite aParent, Gwid aGwid, String aText, String aTrueImageId,
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

  public ValedBooleanCheckAdv createRtBooleanIndicator( Composite aParent, String aDataId, String aTrueImageId,
      String aFalseImageId ) {

    ISkClassInfo classInfo = coreApi().sysdescr().getClassInfo( dlgContext.skObject().classId() );
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
  public MccRtBooleanLabel createRtBooleanIcon( Composite aParent, String aDataId, String aTrueImageId,
      String aFalseImageId ) {
    ISkObject skObj = dlgContext.skObject();
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
  public MccRtBooleanLabel createRtBooleanLabel( Composite aParent, String aDataId, String aFalseImageId,
      String aTrueImageId ) {
    ISkObject skObj = dlgContext.skObject();
    Gwid gwid = Gwid.createRtdata( skObj.classId(), skObj.strid(), aDataId );

    IDtoRtdataInfo dInfo = dataInfo( aDataId );
    MccRtBooleanLabel rtLabel;
    rtLabel = new MccRtBooleanLabel( gwid, aFalseImageId, aTrueImageId, EIconSize.IS_24X24, tsContext() );
    CLabel l = rtLabel.createControl( aParent, SWT.NONE );
    l.setText( dInfo.nmName() );
    l.setToolTipText( dInfo.description() );
    rtLabel.setDescription( dInfo.description() );

    dataProvider.addDataConsumer( rtLabel );

    return rtLabel;
  }

  public MccAttrEditor createAttrEditor( Composite aParent, ISkObject aSkObject, String aAttrId,
      ITsGuiContext aTsContext ) {
    MccAttrEditor attrEditor = new MccAttrEditor( aSkObject, aAttrId, aTsContext, null );
    attrEditor.createControl( aParent );
    return attrEditor;
  }

  public MccRtTextEditor createRtTextEditor( ISkObject aSkObj, String aDataId, String aCmdId,
      ITsGuiContext aTsContext ) {
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

  public MccValedAvBooleanCheckCommand createCheckEditor( Composite aParent, Gwid aDataGwid, String aCommandId,
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

  protected IDtoRtdataInfo dataInfo( String aDataId ) {
    ISkClassInfo clsInfo = coreApi().sysdescr().getClassInfo( dlgContext.skObject().classId() );
    return clsInfo.rtdata().list().getByKey( aDataId );
  }

  protected IDtoAttrInfo attrInfo( String aAttrId ) {
    ISkClassInfo clsInfo = coreApi().sysdescr().getClassInfo( dlgContext.skObject().classId() );
    return clsInfo.attrs().list().getByKey( aAttrId );
  }

}
