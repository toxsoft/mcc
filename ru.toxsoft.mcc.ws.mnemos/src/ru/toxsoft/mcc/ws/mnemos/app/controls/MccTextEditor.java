package ru.toxsoft.mcc.ws.mnemos.app.controls;

import static org.toxsoft.core.tsgui.graphics.icons.ITsStdIconIds.*;
import static org.toxsoft.core.tsgui.valed.IValedImplementationHelpers.*;
import static ru.toxsoft.mcc.ws.mnemos.app.controls.IVjResources.*;

import org.eclipse.jface.dialogs.*;
import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.graphics.colors.*;
import org.toxsoft.core.tsgui.graphics.icons.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Редактор РВ-данного, представляющий собой текстовое поле с прикрепленной в конце кнопкой. Текстовое поле отображает
 * текущее значение, а при нажатии на кнопку появляется диалог ввода нового значения. При подтверждении ввода нового
 * значения посылается соотвествующая команда.
 *
 * @author vs
 */
public class MccTextEditor
    implements ITsGuiContextable {

  private Composite board;
  private CLabel    label;
  private Button    button;

  private final ITsGuiContext tsContext;

  private final EAtomicType valueType;

  private final IEditingFinisher finisher;

  private IAtomicValue value = IAtomicValue.NULL;

  IAtomicValue newVal = IAtomicValue.NULL;

  /**
   * Конструктор.
   *
   * @param aFinisher IEditingFinisher - объект способный завершить редактирование
   * @param aValueType EAtomicType - тип редактируемого значения
   * @param aTsContext ITsGuiContext - соответствующий контекст
   */
  public MccTextEditor( IEditingFinisher aFinisher, EAtomicType aValueType, ITsGuiContext aTsContext ) {
    tsContext = aTsContext;
    valueType = aValueType;
    finisher = aFinisher;
  }

  // ------------------------------------------------------------------------------------
  // ITsGuiContextable
  //

  @Override
  public ITsGuiContext tsContext() {
    return tsContext;
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  public void setValue( IAtomicValue aValue ) {
    value = aValue;
    updateText();
  }

  /**
   * Создает SWT контроль.
   *
   * @param aParent Composite - родительская компонента
   * @return Composite - SWT контроль
   */
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
    int labelH = getLabelControl().getSize().y;

    EIconSize iconSize = EIconSize.IS_16X16;
    for( EIconSize is : EIconSize.values() ) {
      if( is.size() >= labelH ) {
        break;
      }
      iconSize = is;
    }

    getButtonControl().setText( TsLibUtils.EMPTY_STRING );
    getButtonControl().setImage( iconManager.loadStdIcon( ICONID_DOCUMENT_EDIT, iconSize ) );
    getLabelControl().setBackground( colorManager().getColor( ETsColor.WHITE ) );

    return board;
  }

  /**
   * Returns the label control.
   *
   * @return {@link CLabel} - the label control
   */
  public CLabel getLabelControl() {
    return label;
  }

  /**
   * Returns the button at right.
   *
   * @return Buntton - the button at right
   */
  public Button getButtonControl() {
    return button;
  }

  // ------------------------------------------------------------------------------------
  // Implementation
  //

  void processButtonPress() {
    newVal = editValue( value );
    if( newVal == null ) {
      return;
    }
    if( finisher.finishEditing( newVal ) ) {
      value = newVal;
    }
    updateText();
  }

  protected IAtomicValue editValue( IAtomicValue aValue ) {
    String strValue = TsLibUtils.EMPTY_STRING;
    if( value.isAssigned() ) {
      strValue = AvUtils.printAv( formatString( value.atomicType() ), aValue );
    }
    InputDialog dlg = new InputDialog( getShell(), STR_DLG_T_EDIT_VALUE, STR_DLG_M_VALUE, strValue, null );
    if( dlg.open() == IDialogConstants.OK_ID ) {
      if( valueType == EAtomicType.FLOATING ) {
        return AvUtils.avFloat( Double.parseDouble( dlg.getValue() ) );
      }
      if( valueType == EAtomicType.INTEGER ) {
        return AvUtils.avInt( Long.parseLong( dlg.getValue() ) );
      }
      return AvUtils.avStr( dlg.getValue() );
    }
    return null;
  }

  private void updateText() {
    label.setText( AvUtils.printAv( formatString( value.atomicType() ), value ) );
  }

  String formatString( EAtomicType aType ) {
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
