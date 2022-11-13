package ru.toxsoft.mcc.ws.mnemos.app.valed;

import static org.toxsoft.core.tsgui.valed.api.IValedControlConstants.*;
import static org.toxsoft.core.tslib.av.EAtomicType.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;
import static ru.toxsoft.mcc.ws.mnemos.app.valed.IVjResources.*;

import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.graphics.colors.*;
import org.toxsoft.core.tsgui.graphics.cursors.*;
import org.toxsoft.core.tsgui.valed.impl.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.gw.gwid.*;
import org.toxsoft.core.tslib.gw.skid.*;
import org.toxsoft.uskat.base.gui.conn.*;
import org.toxsoft.uskat.core.*;
import org.toxsoft.uskat.core.api.objserv.*;

import ru.toxsoft.mcc.ws.mnemos.app.dialogs.*;

/**
 * Редактор атомарного значения аналогового входа.
 * <p>
 * Значение представляет собой {@link IAtomicValue} типа {@link EAtomicType#FLOATING}.
 *
 * @author vs
 */
public class ValedMccAnalogInput
    extends AbstractValedControl<IAtomicValue, CLabel> {

  /**
   * ID of context reference {@link #OPDEF_SWT_STYLE}.
   */
  public static final String OPID_SWT_STYLE = VALED_OPID_PREFIX + ".swtStyle"; //$NON-NLS-1$

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
   * Object strid.<br>
   */
  public static final IDataDef OPDEF_SWT_STYLE = DataDef.create( OPID_SWT_STYLE, INTEGER, //
      TSID_NAME, STR_N_SWT_STYLE, //
      TSID_DESCRIPTION, STR_D_SWT_STYLE, //
      TSID_DEFAULT_VALUE, AvUtils.avInt( SWT.BORDER | SWT.CENTER ) //
  );

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

  private CLabel label;

  private Color bkColor;

  private Color fgColor;

  private IAtomicValue value = IAtomicValue.NULL;

  private String formatStr = "%.2f"; //$NON-NLS-1$

  MouseListener mouseListener = new MouseAdapter() {

    @Override
    public void mouseDown( MouseEvent aE ) {
      if( aE.button == 1 ) {
        PanelAnalogInput.showDialog( new MccDialogContext( tsContext(), skObj ) );
      }
    }
  };

  private final String classId;
  private final String objStrid;
  private final String dataId;

  private final Gwid dataGwid;

  private final ISkObject skObj;

  public ValedMccAnalogInput( ITsGuiContext aTsContext ) {
    super( aTsContext );
    IOptionSet params = aTsContext.params();

    classId = OPDEF_CLASS_ID.getValue( params ).asString();
    objStrid = OPDEF_OBJ_STRID.getValue( params ).asString();
    dataId = OPDEF_DATA_ID.getValue( params ).asString();

    dataGwid = Gwid.createRtdata( classId, objStrid, dataId );

    ISkCoreApi coreApi = aTsContext.get( ISkConnectionSupplier.class ).defConn().coreApi();
    skObj = coreApi.objService().find( new Skid( classId, objStrid ) );

    bkColor = colorManager().getColor( ETsColor.WHITE );
    fgColor = colorManager().getColor( ETsColor.BLACK );
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Возвращает конкретный ИД данного.
   *
   * @return Gwid - конкретный ИД данного
   */
  public Gwid dataGwid() {
    return dataGwid;
  }

  /**
   * Возвращает серверный объект, соответствующий аналоговому входу.
   *
   * @return ISkObject - серверный объект, соответствующий аналоговому входу
   */
  public ISkObject skObject() {
    return skObj;
  }

  // ------------------------------------------------------------------------------------
  // AbstractValedControl
  //

  @Override
  protected CLabel doCreateControl( Composite aParent ) {
    IOptionSet params = tsContext().params();
    int style = OPDEF_SWT_STYLE.getValue( params ).asInt();

    label = new CLabel( aParent, style );

    label.setSize( style, SWT.DEFAULT );
    label.setBackground( bkColor );
    label.setForeground( fgColor );

    label.addMouseListener( mouseListener );
    label.setCursor( cursorManager().getCursor( ECursorType.HAND ) );

    return label;
  }

  @Override
  protected void doSetEditable( boolean aEditable ) {
    // nop не предназначен для редактирования
  }

  @Override
  protected void doClearValue() {
    value = IAtomicValue.NULL;
    updateText();
  }

  @Override
  protected IAtomicValue doGetUnvalidatedValue() {
    return value;
  }

  @Override
  protected void doSetUnvalidatedValue( IAtomicValue aValue ) {
    value = aValue;
    updateText();
  }

  // ------------------------------------------------------------------------------------
  // Implementation
  //

  void updateText() {
    label.setText( AvUtils.printAv( formatStr, value ) );
  }

}
