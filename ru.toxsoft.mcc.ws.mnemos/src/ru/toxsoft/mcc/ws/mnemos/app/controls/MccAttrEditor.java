package ru.toxsoft.mcc.ws.mnemos.app.controls;

import static org.toxsoft.core.tsgui.graphics.icons.ITsStdIconIds.*;
import static org.toxsoft.core.tsgui.valed.IValedImplementationHelpers.*;
import static ru.toxsoft.mcc.ws.mnemos.app.controls.IVjResources.*;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.toxsoft.core.tsgui.bricks.ctx.ITsGuiContext;
import org.toxsoft.core.tsgui.bricks.ctx.ITsGuiContextable;
import org.toxsoft.core.tsgui.graphics.colors.ETsColor;
import org.toxsoft.core.tsgui.graphics.icons.EIconSize;
import org.toxsoft.core.tsgui.graphics.icons.ITsIconManager;
import org.toxsoft.core.tslib.av.EAtomicType;
import org.toxsoft.core.tslib.av.IAtomicValue;
import org.toxsoft.core.tslib.av.impl.AvUtils;
import org.toxsoft.core.tslib.bricks.strid.more.IdChain;
import org.toxsoft.core.tslib.utils.TsLibUtils;
import org.toxsoft.core.tslib.utils.errors.TsNotAllEnumsUsedRtException;
import org.toxsoft.uskat.core.api.objserv.ISkObject;
import org.toxsoft.uskat.core.api.sysdescr.dto.IDtoAttrInfo;
import org.toxsoft.uskat.core.connection.ISkConnection;
import org.toxsoft.uskat.core.gui.conn.ISkConnectionSupplier;
import org.toxsoft.uskat.core.impl.dto.DtoFullObject;
import org.toxsoft.uskat.core.utils.ISkConnected;

import ru.toxsoft.mcc.ws.mnemos.app.utils.MccAvUtils;

public class MccAttrEditor
    implements ITsGuiContextable, ISkConnected {

  private Composite board;
  private CLabel    label;
  private Button    button;

  private final String attrId;

  private final ITsGuiContext tsContext;

  private final ISkObject skObject;

  private final ISkConnection skConnection;

  EAtomicType attrType = EAtomicType.NONE;

  MccAttrEditor( ISkObject aSkObject, String aAttrId, ITsGuiContext aTsContext, IdChain aConnId ) {
    attrId = aAttrId;
    tsContext = aTsContext;
    skObject = aSkObject;
    if( aConnId == null ) {
      skConnection = tsContext.get( ISkConnectionSupplier.class ).defConn();
    }
    else {
      skConnection = tsContext.get( ISkConnectionSupplier.class ).getConn( aConnId );
    }

    IDtoAttrInfo attrInfo = coreApi().sysdescr().getClassInfo( skObject.classId() ).attrs().list().getByKey( aAttrId );
    attrType = attrInfo.dataType().atomicType();
  }

  // ------------------------------------------------------------------------------------
  // ITsGuiContextable
  //

  @Override
  public ITsGuiContext tsContext() {
    return tsContext;
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

  public Composite createControl( Composite aParent ) {
    board = new Composite( aParent, SWT.NO_FOCUS );
    GridLayout gl = new GridLayout( 2, false );
    gl.verticalSpacing = 0;
    gl.horizontalSpacing = 0;
    gl.marginTop = 0;
    gl.marginBottom = 0;
    gl.marginHeight = 0;
    gl.marginWidth = 0;
    board.setLayout( gl );
    // label
    label = new CLabel( board, SWT.BORDER | SWT.LEFT );
    label.setLayoutData( new GridData( SWT.FILL, SWT.CENTER, true, false, 1, 1 ) );
    // button
    button = new Button( board, SWT.PUSH | SWT.FLAT );
    button.setLayoutData( new GridData( SWT.FILL, SWT.TOP, false, false, 1, 1 ) );
    button.addSelectionListener( new SelectionAdapter() {

      @Override
      public void widgetSelected( SelectionEvent e ) {
        processButtonPress();
      }
    } );
    button.setText( STR_ELLIPSIS );

    ITsIconManager iconManager = tsContext().get( ITsIconManager.class );
    int labelH = label.getSize().y;

    EIconSize iconSize = EIconSize.IS_16X16;
    for( EIconSize is : EIconSize.values() ) {
      if( is.size() >= labelH ) {
        break;
      }
      iconSize = is;
    }

    button.setText( TsLibUtils.EMPTY_STRING );
    button.setImage( iconManager.loadStdIcon( ICONID_DOCUMENT_EDIT, iconSize ) );
    label.setBackground( colorManager().getColor( ETsColor.WHITE ) );

    update();
    return board;
  }

  public Composite getControl() {
    return board;
  }

  // ------------------------------------------------------------------------------------
  // Implementation
  //

  void update() {
    IAtomicValue value = skObject.attrs().getValue( attrId );
    label.setText( AvUtils.printAv( null, value ) );
  }

  void processButtonPress() {
    IAtomicValue value = skObject.attrs().getValue( attrId );
    IAtomicValue newVal = editValue( value );
    if( newVal == null ) {
      return;
    }

    DtoFullObject dto = DtoFullObject.createDtoFullObject( skObject.skid(), coreApi() );
    dto.attrs().setValue( attrId, newVal );

    coreApi().objService().defineObject( dto );
    update();
  }

  protected IAtomicValue editValue( IAtomicValue aValue ) {
    String strValue = TsLibUtils.EMPTY_STRING;
    if( aValue.isAssigned() ) {
      strValue = AvUtils.printAv( formatString( attrType ), aValue );
    }
    InputDialog dlg = new InputDialog( getShell(), STR_DLG_T_EDIT_VALUE, STR_DLG_M_VALUE, strValue, null );
    if( dlg.open() == IDialogConstants.OK_ID ) {
      return MccAvUtils.parse( dlg.getValue(), attrType );
    }
    return null;
  }

  /**
   * Возвращает строку форматирования для атомарного типа.<br>
   *
   * @param aType EAtomicType - атомарный тип
   * @return String - строка форматирования
   */
  public static String formatString( EAtomicType aType ) {
    switch( aType ) {
      case BOOLEAN:
        return "%b"; //$NON-NLS-1$
      case FLOATING:
        return "%.2f"; //$NON-NLS-1$
      case INTEGER:
        return "%d"; //$NON-NLS-1$
      case NONE:
      case STRING:
      case TIMESTAMP:
      case VALOBJ:
        return "%s"; //$NON-NLS-1$
      default:
        throw new TsNotAllEnumsUsedRtException();
    }
  }

}
