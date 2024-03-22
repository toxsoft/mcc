package ru.toxsoft.mcc.ws.mnemos.app.controls;

import static ru.toxsoft.mcc.ws.mnemos.app.controls.IVjResources.*;

import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.ctx.impl.*;
import org.toxsoft.core.tsgui.dialogs.*;
import org.toxsoft.core.tsgui.graphics.icons.*;
import org.toxsoft.core.tsgui.panels.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.gw.gwid.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.tslib.utils.logs.impl.*;
import org.toxsoft.uskat.core.api.objserv.*;
import org.toxsoft.uskat.core.api.sysdescr.*;
import org.toxsoft.uskat.core.api.sysdescr.dto.*;
import org.toxsoft.uskat.core.connection.*;
import org.toxsoft.uskat.core.gui.conn.*;
import org.toxsoft.uskat.core.utils.*;

import ru.toxsoft.mcc.ws.mnemos.app.dialogs.*;
import ru.toxsoft.mcc.ws.mnemos.app.rt.*;
import ru.toxsoft.mcc.ws.mnemos.app.rt.chart.data_aliases.*;
import ru.toxsoft.mcc.ws.mnemos.app.utils.*;
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

  MccCommandSender cmdSender;

  private final RtDataAliasHelper aliasHelper;

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

    cmdSender = new MccCommandSender( coreApi() );
    cmdSender.eventer().addListener( aSource -> {
      String errStr = cmdSender.errorString();
      if( cmdSender.errorString() != null && !errStr.isBlank() ) {
        LoggerUtils.errorLogger().error( errStr );
        TsDialogUtils.error( getShell(), errStr );
      }
    } );

    aliasHelper = new RtDataAliasHelper( skConn() );
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
   * Создает однострочный текст, отображающий значение РВ-данного, на основе {@link CLabel}.<br>
   *
   * @param aParent Composite - родительская компонента
   * @param aStyle int - swt стиль контроля
   * @param aDataGwid Gwid - ИД данного
   * @param aTsContext ITsGuiContext - соответствующий контекст
   * @return MccRtLabel - однострочный текст, отображающий значение РВ-данного
   */
  public MccRtLabel createRtLabel( Composite aParent, int aStyle, Gwid aDataGwid, ITsGuiContext aTsContext ) {
    ISkObject obj = coreApi().objService().find( aDataGwid.skid() );
    MccRtLabel rtl = new MccRtLabel( obj, aDataGwid.propId(), aTsContext, null );
    dataProvider.addDataConsumer( rtl );
    rtl.createControl( aParent, aStyle );
    return rtl;
  }

  /**
   * Создает checkbox для посылки команды.<b>
   *
   * @param aParent Composite - родительская компонента
   * @param aSkObj ISkObject - серверный объект
   * @param aCmdId String - ИД команды
   * @param aDataId String - ИД данного
   * @param aAutoText boolean - признак автогенерации текста
   * @return MccCheckCmdButton - checkbox для посылки команды
   */
  public MccCheckCmdButton createCheckCmdButton( Composite aParent, ISkObject aSkObj, String aCmdId, String aDataId,
      boolean aAutoText ) {
    Gwid cmdGwid = Gwid.createCmd( aSkObj.classId(), aSkObj.strid(), aCmdId );
    Gwid dataGwid = Gwid.createRtdata( aSkObj.classId(), aSkObj.strid(), aDataId );
    MccCheckCmdButton btn = new MccCheckCmdButton( cmdGwid, dataGwid, tsContext() );
    Button b = btn.createControl( aParent, SWT.CHECK );
    dataProvider.addDataConsumer( btn );
    if( aAutoText ) {
      // b.setText( dataInfo( aDataId ).nmName() );
      b.setText( dataName( dataGwid ) );
    }
    return btn;
  }

  /**
   * Создает checkbox для посылки команды.<b>
   *
   * @param aParent Composite - родительская компонента
   * @param aCmdId String - ИД команды
   * @param aDataId String - ИД данного
   * @param aAutoText boolean - признак автогенерации текста
   * @return MccCheckCmdButton - checkbox для посылки команды
   */
  public MccCheckCmdButton createCheckCmdButton( Composite aParent, String aCmdId, String aDataId, boolean aAutoText ) {
    ISkObject skObj = dialogContext().skObject();
    Gwid cmdGwid = Gwid.createCmd( skObj.classId(), skObj.strid(), aCmdId );
    Gwid dataGwid = Gwid.createRtdata( skObj.classId(), skObj.strid(), aDataId );
    MccCheckCmdButton btn = new MccCheckCmdButton( cmdGwid, dataGwid, tsContext() );
    Button b = btn.createControl( aParent, SWT.CHECK );
    dataProvider.addDataConsumer( btn );
    if( aAutoText ) {
      // b.setText( dataInfo( aDataId ).nmName() );
      b.setText( dataInfo( dataGwid ).nmName() );
    }
    return btn;
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  // RtValedsPanel contentPanel() {
  // return rtPanel;
  // }

  /**
   * Создает {@link GridLayout} с нулевыми полями и отступами.<br>
   *
   * @param aColumnCount - количество колонок
   * @param aEqualSize - признак одинаковой ширины клонок
   * @return GridLayout
   */
  public GridLayout createGridLayout( int aColumnCount, boolean aEqualSize ) {
    GridLayout gl = new GridLayout( aColumnCount, aEqualSize );
    gl.verticalSpacing = 0;
    gl.horizontalSpacing = 0;
    gl.marginTop = 0;
    gl.marginBottom = 0;
    gl.marginHeight = 0;
    gl.marginWidth = 0;
    return gl;
  }

  public Group createGroup( Composite aParent, String aName, int aColumnsCount, boolean aEqualSize ) {
    Group group = new Group( aParent, SWT.NONE );
    group.setText( aName );
    GridLayout gl = createGridLayout( aColumnsCount, aEqualSize );
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

    // IDtoRtdataInfo dInfo = dataInfo( aDataId );
    IDtoRtdataInfo dInfo = dataInfo( gwid );
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
   * @param aFalseImageId String- ИД картинки для значения <b>false</b>
   * @param aTrueImageId String - ИД картинки для значения <b>true</b>
   * @return MccRtBooleanLabel - созданный контроль
   */
  public MccRtBooleanLabel createRtBooleanLabel( Composite aParent, String aDataId, String aFalseImageId,
      String aTrueImageId ) {
    ISkObject skObj = dlgContext.skObject();
    Gwid gwid = Gwid.createRtdata( skObj.classId(), skObj.strid(), aDataId );

    return createRtBooleanLabel( aParent, gwid, aFalseImageId, aTrueImageId );
  }

  /**
   * Создает надпись с иконкой, отображающей логическое значение.
   * <p>
   * Надпись берется из описания данного.
   *
   * @param aParent Composite - родительская компонента
   * @param aDataGwid Gwid - конкретный ИД данного
   * @param aFalseImageId String- ИД картинки для значения <b>true</b>
   * @param aTrueImageId String - ИД картинки для значения <b>true</b>
   * @return MccRtBooleanLabel - созданный контроль
   */
  public MccRtBooleanLabel createRtBooleanLabel( Composite aParent, Gwid aDataGwid, String aFalseImageId,
      String aTrueImageId ) {
    // IDtoRtdataInfo dInfo = dataInfo( aDataGwid.propId() );
    IDtoRtdataInfo dInfo = dataInfo( aDataGwid );
    MccRtBooleanLabel rtLabel;
    rtLabel = new MccRtBooleanLabel( aDataGwid, aFalseImageId, aTrueImageId, EIconSize.IS_24X24, tsContext() );
    CLabel l = rtLabel.createControl( aParent, SWT.NONE );
    l.setText( dInfo.nmName() );
    l.setToolTipText( dInfo.description() );
    rtLabel.setDescription( dInfo.description() );

    dataProvider.addDataConsumer( rtLabel );

    return rtLabel;
  }

  /**
   * Создает редактор атрибута в виде текста.
   * <p>
   *
   * @param aParent Composite - родительская компонента
   * @param aAttrId String - ИД атрибута
   * @return MccAttrEditor - редактор атрибута в виде текста
   */
  public MccAttrEditor createAttrEditor( Composite aParent, String aAttrId ) {
    MccAttrEditor attrEditor = new MccAttrEditor( dialogContext().skObject(), aAttrId, tsContext(), null );
    attrEditor.createControl( aParent );
    return attrEditor;
  }

  /**
   * Создает редактор значения в виде текста. При для изменения значения посылает соответствующую команду.
   * <p>
   *
   * @param aDataId String - ИД данного
   * @param aCmdId String - ИД команды
   * @return MccRtTextEditor - редактор значения в виде текста
   */
  public MccRtTextEditor createRtTextEditor( String aDataId, String aCmdId ) {
    MccRtTextEditor rtEditor = new MccRtTextEditor( dialogContext().skObject(), aDataId, aCmdId, tsContext() );
    dataProvider.addDataConsumer( rtEditor );
    return rtEditor;
  }

  /**
   * Создает редактор значения в виде текста. При для изменения значения посылает соответствующую команду.
   * <p>
   *
   * @param aSkObj ISkObject - серверный объект
   * @param aDataId String - ИД данного
   * @param aCmdId String - ИД команды
   * @return MccRtTextEditor - редактор значения в виде текста
   */
  public MccRtTextEditor createRtTextEditor( ISkObject aSkObj, String aDataId, String aCmdId ) {
    MccRtTextEditor rtEditor = new MccRtTextEditor( aSkObj, aDataId, aCmdId, tsContext() );
    dataProvider.addDataConsumer( rtEditor );
    return rtEditor;
  }

  /**
   * Создает нажимающуюся кнопку для посылки одной команды.<br>
   *
   * @param aParent Composite - родительская компонента
   * @param aCmdId String - ИД команды
   * @return MccPushCmdButton нажимающуюся кнопку для посылки одной команды
   */
  public MccPushCmdButton createPushCmdButton( Composite aParent, String aCmdId ) {
    ISkObject skObj = dialogContext().skObject();
    Gwid cmdGwid = Gwid.createCmd( skObj.classId(), skObj.strid(), aCmdId );
    MccPushCmdButton btn = new MccPushCmdButton( cmdGwid, coreApi(), tsContext() );
    btn.createControl( aParent, SWT.PUSH );
    return btn;
  }

  /**
   * Возвращает компоненту, отображающую режим управления (АРМ, Панель и т.д.)
   *
   * @param aParent Composite - родительская компонента
   * @param aSkObject ISkObject - серверный объект
   * @param aTsContext ITsGuiContext - соответствующий контекст
   * @return MccControlModeComponent - компонента, отображающую режим управления
   */
  public MccControlModeComponent createControlModeComponent( Composite aParent, ISkObject aSkObject,
      ITsGuiContext aTsContext ) {
    MccControlModeComponent comp = new MccControlModeComponent( aSkObject, aTsContext, null );
    comp.createControl( aParent, SWT.NONE );
    dataProvider.addDataConsumer( comp );
    return comp;
  }

  /**
   * Создает группу "Наработка"
   *
   * @param aParent Composite - родительская компонента
   * @param aEditable boolean - признак наличия кнопки "Сбросить..."
   * @return Group
   */
  public Group createOperatingTimeGroup( Composite aParent, boolean aEditable ) {
    int columns = 2;
    if( aEditable ) {
      columns = 3;
    }
    Group g = createGroup( aParent, STR_OPERATING_TIME, columns, false );

    GridData gd = new GridData();
    gd.widthHint = 80;
    CLabel l = new CLabel( g, SWT.NONE );
    l.setText( dataInfo( "rtdHourMeterMin" ).nmName() ); //$NON-NLS-1$
    MccRtLabel rtLabel = createRtLabel( g, SWT.BORDER, "rtdHourMeterMin", tsContext() ); //$NON-NLS-1$
    rtLabel.getControl().setLayoutData( gd );
    rtLabel.setValueFormatter( aValue -> {
      if( aValue == null ) {
        return "NULL"; //$NON-NLS-1$
      }
      if( !aValue.isAssigned() ) {
        return "none"; //$NON-NLS-1$
      }
      int minutes = aValue.asInt();
      return "" + minutes / 60 + ":" + minutes % 60; //$NON-NLS-1$ //$NON-NLS-2$
    } );

    if( aEditable ) {
      Button btnClear = new Button( g, SWT.PUSH );
      btnClear.setLayoutData( new GridData( SWT.LEFT, SWT.FILL, false, true, 1, 2 ) );
      btnClear.setText( STR_CLEAR );
      btnClear.addSelectionListener( new SelectionAdapter() {

        @Override
        public void widgetSelected( SelectionEvent aE ) {
          if( TsDialogUtils.askYesNoCancel( getShell(), STR_CLEAR_OPERATING_TIME ) == ETsDialogCode.YES ) {
            ISkObject skObject = dialogContext().skObject();
            boolean r;
            Gwid cmdg = Gwid.createCmd( skObject.classId(), skObject.strid(), "cmdHourMeter" ); //$NON-NLS-1$
            r = cmdSender.sendCommand( cmdg, AvUtils.avInt( 0 ) );
            if( !r ) {
              TsDialogUtils.error( getShell(), cmdSender.errorString() );
            }
            cmdg = Gwid.createCmd( skObject.classId(), skObject.strid(), "cmdHourMeterMin" ); //$NON-NLS-1$
            r = cmdSender.sendCommand( cmdg, AvUtils.avInt( 0 ) );
            if( !r ) {
              TsDialogUtils.error( getShell(), cmdSender.errorString() );
            }
            cmdg = Gwid.createCmd( skObject.classId(), skObject.strid(), "cmdStartCount" ); //$NON-NLS-1$
            r = cmdSender.sendCommand( cmdg, AvUtils.avInt( 0 ) );
            if( !r ) {
              TsDialogUtils.error( getShell(), cmdSender.errorString() );
            }
          }
        }
      } );
    }

    l = new CLabel( g, SWT.NONE );
    l.setText( dataInfo( "rtdStartCount" ).nmName() );
    rtLabel = createRtLabel( g, SWT.BORDER, "rtdStartCount", tsContext() );
    rtLabel.getControl().setLayoutData( gd );

    return g;
  }

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

  protected String dataName( Gwid aGwid ) {
    IDataNameAlias alias = aliasHelper.alias( aGwid );
    if( alias != null ) {
      return alias.title();
    }
    return dataInfo( aGwid ).nmName();
  }

  protected String dataName( String aDataId ) {
    ISkObject skObj = dlgContext.skObject();
    return dataName( Gwid.createRtdata( skObj.classId(), skObj.id(), aDataId ) );
  }

  protected IDtoRtdataInfo dataInfo( Gwid aDataGwid ) {
    ISkClassInfo clsInfo = coreApi().sysdescr().getClassInfo( aDataGwid.classId() );
    return clsInfo.rtdata().list().getByKey( aDataGwid.propId() );
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
