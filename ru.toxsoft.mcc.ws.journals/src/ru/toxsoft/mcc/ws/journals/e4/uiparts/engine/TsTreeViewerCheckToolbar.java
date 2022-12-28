package ru.toxsoft.mcc.ws.journals.e4.uiparts.engine;

import static org.toxsoft.core.tsgui.bricks.actions.ITsStdActionDefs.*;

import org.eclipse.swt.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.graphics.icons.*;
import org.toxsoft.core.tsgui.panels.*;
import org.toxsoft.core.tsgui.panels.toolbar.*;
import org.toxsoft.core.tsgui.utils.layout.*;

/**
 * Стандартный тул-бар (наименование, выделить всё, снаять всё) для чек-деревьев
 *
 * @author max
 */
public class TsTreeViewerCheckToolbar
    extends TsPanel {

  /*
   * Картинка для действия "Фильтр запроса"
   */
  // static Image selectedAllImage =
  // AbstractUIPlugin.imageDescriptorFromPlugin( Activator.PLUGIN_ID, "icons/is16x16/check-on-m.png" ).createImage();
  // //$NON-NLS-1$

  /*
   * Картинка для действия "Экспорт в Excel" page_excel.png
   */
  // static Image deselectedAllImage =
  // AbstractUIPlugin.imageDescriptorFromPlugin( Activator.PLUGIN_ID, "icons/is16x16/check-off-m.png" ).createImage();
  // //$NON-NLS-1$

  // private final static TsActionDef ACDEF_ITEMS_CHECK_ALL =
  // TsActionDef.ofPush2( ACTID_ITEMS_CHECK_ALL, STR_T_FIRST_PAGE, STR_P_FIRST_PAGE, ICONID_ITEMS_CHECK_ALL );
  //
  // private final static TsActionDef ACDEF_PREV_PAGE_REPORT =
  // TsActionDef.ofPush2( ACTID_PREV_PAGE_REPORT, STR_T_PREV_PAGE, STR_P_PREV_PAGE, ICONID_ITEMS_UNCHECK_ALL );

  /**
   * Конструктор тул-бара по чек-дереву и наименованию.
   *
   * @param aParent Composite - родительский компонент.
   * @param aContext ITsGuiContext - контекст.
   * @param treeViewerCheck TsTreeViewerCheck - чек-дерево.
   * @param aTitle String - наименование дерева.
   */
  public TsTreeViewerCheckToolbar( Composite aParent, ITsGuiContext aContext, TsTreeViewerCheck treeViewerCheck,
      String aTitle ) {
    super( aParent, aContext );
    setMinimumHeight( 20 );
    setLayout( new FillLayout() );
    setMaximumHeight( 20 );

    setLayout( new BorderLayout() );
    Label l = new Label( this, SWT.LEFT );

    l.setText( String.format( " %s ", aTitle ) ); //$NON-NLS-1$
    l.setLayoutData( BorderLayout.CENTER );

    // Заголовок
    TsToolbar toolBar = new TsToolbar( aContext );
    toolBar.setIconSize( EIconSize.IS_16X16 );

    toolBar.addActionDef( ACDEF_CHECK_ALL );
    toolBar.addActionDef( ACDEF_UNCHECK_ALL );

    Control toolbarCtrl = toolBar.createControl( this );
    toolbarCtrl.setLayoutData( BorderLayout.CENTER );

    toolBar.addListener( aActionId -> {
      if( aActionId.equals( ACDEF_CHECK_ALL.id() ) ) {
        treeViewerCheck.checkAll();
      }
      if( aActionId.equals( ACDEF_UNCHECK_ALL.id() ) ) {
        treeViewerCheck.uncheckAll();
      }
    } );

    // Кнопка "Выбрать все."
    // toolBar.addContributionItem( new ControlContribution( "checkAll" ) { //$NON-NLS-1$
    //
    // @Override
    // protected Control createControl( Composite aParent1 ) {
    // Button btn = new Button( aParent1, SWT.PUSH | SWT.FLAT );
    // // ITsIconManager iconManager = aContext.get( ITsIconManager.class );
    // // btn.setImage( selectedAllImage );
    // btn.setText( "selAll" );
    // btn.setToolTipText( STR_P_CHECK_ALL );
    // btn.addSelectionListener( new SelectionAdapter() {
    //
    // @Override
    // public void widgetSelected( SelectionEvent e ) {
    // treeViewerCheck.checkAll();
    // }
    // } );
    // return btn;
    // }
    // } );
    // Кнопка "Отменить все."
    // toolBar.addContributionItem( new ControlContribution( "uncheckAll" ) { //$NON-NLS-1$
    //
    // @Override
    // protected Control createControl( Composite aParent1 ) {
    // Button btn = new Button( aParent1, SWT.PUSH | SWT.FLAT );
    // // ITsIconManager iconManager = aContext.get( ITsIconManager.class );
    // // btn.setImage( deselectedAllImage );
    // btn.setText( "deselAll" );
    // btn.setToolTipText( STR_P_UNCHECK_ALL );
    // btn.addSelectionListener( new SelectionAdapter() {
    //
    // @Override
    // public void widgetSelected( SelectionEvent e ) {
    // treeViewerCheck.uncheckAll();
    // }
    // } );
    // return btn;
    // }
    // } );
  }

}
