package ru.toxsoft.mcc.ws.mnemos.app.valed;

import static org.toxsoft.core.tsgui.graphics.icons.ITsStdIconIds.*;
import static org.toxsoft.core.tsgui.valed.api.IValedControlConstants.*;
import static org.toxsoft.core.tslib.av.EAtomicType.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;
import static ru.toxsoft.mcc.ws.mnemos.app.valed.IVjResources.*;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.graphics.colors.*;
import org.toxsoft.core.tsgui.graphics.icons.*;
import org.toxsoft.core.tsgui.valed.controls.helpers.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.gw.gwid.*;
import org.toxsoft.core.tslib.gw.skid.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.uskat.base.gui.conn.*;
import org.toxsoft.uskat.core.*;
import org.toxsoft.uskat.core.api.cmdserv.*;
import org.toxsoft.uskat.core.api.sysdescr.*;
import org.toxsoft.uskat.core.api.sysdescr.dto.*;

/**
 * Базовый класс для установки одного атомарного значения посредством посылки команды.
 * <p>
 *
 * @author vs
 */
public abstract class AbstractValedSkCommand
    extends AbstractValedLabelAndButton<IAtomicValue> {

  private IAtomicValue value = IAtomicValue.NULL;

  /**
   * ID of context reference {@link #OPDEF_CLASS_ID}.
   */
  public static final String OPID_CLASS_ID = VALED_OPID_PREFIX + ".classId"; //$NON-NLS-1$

  /**
   * ID of context reference {@link #OPDEF_OBJ_STRID}.
   */
  public static final String OPID_OBJ_STRID = VALED_OPID_PREFIX + ".objStrid"; //$NON-NLS-1$

  /**
   * ID of context reference {@link #OPDEF_DATA_ID}.
   */
  public static final String OPID_DATA_ID = VALED_OPID_PREFIX + ".dataId"; //$NON-NLS-1$

  /**
   * ID of context reference {@link #OPDEF_COMMAND_ID}.
   */
  public static final String OPID_COMMAND_ID = VALED_OPID_PREFIX + ".commandId"; //$NON-NLS-1$

  /**
   * Class Id.<br>
   */
  public static final IDataDef OPDEF_CLASS_ID = DataDef.create( OPID_CLASS_ID, STRING, //
      TSID_NAME, STR_N_CLASS_ID, //
      TSID_DESCRIPTION, STR_D_CLASS_ID, //
      TSID_DEFAULT_VALUE, AV_STR_EMPTY //
  );

  /**
   * Object strid.<br>
   */
  public static final IDataDef OPDEF_OBJ_STRID = DataDef.create( OPID_OBJ_STRID, STRING, //
      TSID_NAME, STR_N_OBJ_STRID, //
      TSID_DESCRIPTION, STR_D_OBJ_STRID, //
      TSID_DEFAULT_VALUE, AV_STR_EMPTY //
  );

  /**
   * Data Id.<br>
   */
  public static final IDataDef OPDEF_DATA_ID = DataDef.create( OPID_DATA_ID, STRING, //
      TSID_NAME, STR_N_DATA_ID, //
      TSID_DESCRIPTION, STR_D_DATA_ID, //
      TSID_DEFAULT_VALUE, AV_STR_EMPTY //
  );

  /**
   * Command Id that will be used to send command for setting new value.<br>
   */
  public static final IDataDef OPDEF_COMMAND_ID = DataDef.create( OPID_COMMAND_ID, STRING, //
      TSID_NAME, STR_N_COMMAND_ID, //
      TSID_DESCRIPTION, STR_D_COMMAND_ID, //
      TSID_DEFAULT_VALUE, AV_STR_EMPTY //
  );

  private final String classId;
  private final String objStrid;
  private final String dataId;
  private final String commandId;
  private final String cmdArgId;

  /**
   * Constructor for subclasses.
   *
   * @param aTsContext {@link ITsGuiContext} - the valed context
   * @throws TsNullArgumentRtException аргумент = null
   */
  protected AbstractValedSkCommand( ITsGuiContext aTsContext ) {
    super( aTsContext );
    IOptionSet params = tsContext().params();
    classId = OPDEF_CLASS_ID.getValue( params ).asString();
    objStrid = OPDEF_OBJ_STRID.getValue( params ).asString();
    dataId = OPDEF_DATA_ID.getValue( params ).asString();
    commandId = OPDEF_COMMAND_ID.getValue( params ).asString();

    String argId = TsLibUtils.EMPTY_STRING;
    ISkCoreApi coreApi = tsContext().get( ISkConnectionSupplier.class ).defConn().coreApi();
    ISkClassInfo clsInfo = coreApi.sysdescr().findClassInfo( classId );
    for( IDtoCmdInfo cmdInfo : clsInfo.cmds().list() ) {
      if( cmdInfo.id().equals( commandId ) ) {
        argId = cmdInfo.argDefs().first().id();
      }
    }
    cmdArgId = argId;
  }

  // ------------------------------------------------------------------------------------
  // AbstractValedTextAndButton
  //

  @Override
  final protected void doAfterControlCreated() {
    ITsIconManager iconManager = tsContext().get( ITsIconManager.class );
    int labelH = getLabelControl().getSize().y;

    EIconSize iconSize = EIconSize.IS_16X16;
    for( EIconSize is : EIconSize.values() ) {
      if( is.size() >= labelH ) {
        break;
      }
      iconSize = is;
    }

    getButtonControl().setText( TsLibUtils.EMPTY_STRING );
    // EIconSize iconSize = hdpiService().getJFaceCellIconsSize();
    getButtonControl().setImage( iconManager.loadStdIcon( ICONID_DOCUMENT_EDIT, iconSize ) );
    getLabelControl().setBackground( colorManager().getColor( ETsColor.WHITE ) );
    updateTextControl();
  }

  @Override
  final protected void doProcessButtonPress() {
    IAtomicValue newVal = editValue( value );
    if( newVal == null ) {
      return;
    }
    ISkCoreApi coreApi = tsContext().get( ISkConnectionSupplier.class ).defConn().coreApi();
    ISkCommandService cmdService = coreApi.cmdService();
    IOptionSetEdit args = new OptionSet();
    args.setValue( cmdArgId, newVal );
    ISkCommand command = cmdService.sendCommand( commandGwid(), new Skid( "sk.User", "root" ), args );
    // value = newVal;
    // updateTextControl();
    fireModifyEvent( true );
  }

  @Override
  protected IAtomicValue doGetUnvalidatedValue() {
    return value;
  }

  @Override
  protected void doSetUnvalidatedValue( IAtomicValue aValue ) {
    value = aValue;
  }

  // ------------------------------------------------------------------------------------
  // Implementation
  //

  private void updateTextControl() {
    getLabelControl().setText( value2text( value ) );
  }

  // ------------------------------------------------------------------------------------
  // For descendants
  //

  /**
   * Возвращает Gwid данного, которое должно отображаться в редакторе.
   *
   * @return Gwid - ИД данного, которое должно отображаться в редакторе
   */
  protected Gwid dataGwid() {
    return Gwid.createRtdata( classId, objStrid, dataId );
  }

  protected Gwid commandGwid() {
    return Gwid.createCmd( classId, objStrid, commandId );
  }

  protected String commandArgId() {
    return cmdArgId;
  }

  // ------------------------------------------------------------------------------------
  // To implement
  //

  /**
   * Возвращает отредактированное значение или null при отказе от редактирования.
   *
   * @param aValue IAtomicValue - текущее значение
   * @return IAtomicValue новое значение или null
   */
  protected abstract IAtomicValue editValue( IAtomicValue aValue );

  /**
   * Возвращает текстовое представление значения.
   *
   * @param aValue IAtomicValue - значение
   * @return Sring - текстовое представление значения
   */
  protected abstract String value2text( IAtomicValue aValue );

}