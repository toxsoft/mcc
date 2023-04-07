package ru.toxsoft.mcc.ws.mnemos.app.dialogs;

import org.eclipse.jface.viewers.*;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.toxsoft.core.tsgui.bricks.ctx.ITsGuiContext;
import org.toxsoft.core.tsgui.dialogs.datarec.*;
import org.toxsoft.core.tslib.coll.IList;
import org.toxsoft.uskat.core.ISkCoreApi;
import org.toxsoft.uskat.core.api.objserv.ISkObject;
import org.toxsoft.uskat.core.gui.conn.ISkConnectionSupplier;

/**
 * Диалог выбора аналогового входа (для тестовых целей).
 * <p>
 *
 * @author vs
 */
public class PanelSelectAnalogInput
    extends AbstractTsDialogPanel<ISkObject, ITsGuiContext> {

  protected PanelSelectAnalogInput( Composite aParent, TsDialog<ISkObject, ITsGuiContext> aOwnerDialog ) {
    super( aParent, aOwnerDialog );
    init( aParent );
  }

  // ------------------------------------------------------------------------------------
  // AbstractTsDialogPanel
  //

  @Override
  protected void doSetDataRecord( ISkObject aData ) {
    // TODO Auto-generated method stub

  }

  @Override
  protected ISkObject doGetDataRecord() {
    IStructuredSelection selection = viewer.getStructuredSelection();
    if( !selection.isEmpty() ) {
      return (ISkObject)selection.getFirstElement();
    }
    return null;
  }

  // ------------------------------------------------------------------------------------
  // Статический метод вызова
  //

  /**
   * Показывает диалог выбора.<br>
   *
   * @param aTsContext ITsGuiContext - контекст диалога
   * @return ISkObject - выбранный аналоговый выход или null при отказе от выбора
   */
  public static ISkObject selectAnalogInput( ITsGuiContext aTsContext ) {
    IDialogPanelCreator<ISkObject, ITsGuiContext> creator = PanelSelectAnalogInput::new;
    String dlgMsg = "Выберите требуемый объект и нажмите \"OK\""; //$NON-NLS-1$
    ITsDialogInfo dlgInfo = new TsDialogInfo( aTsContext, "Аналоговый вход", dlgMsg ); //$NON-NLS-1$
    TsDialog<ISkObject, ITsGuiContext> d = new TsDialog<>( dlgInfo, null, aTsContext, creator );
    return d.execData();
  }

  // ------------------------------------------------------------------------------------
  // Implementation
  //

  TableViewer viewer;

  void init( Composite aParent ) {
    aParent.setLayout( new FillLayout() );
    setLayout( new FillLayout() );
    viewer = new TableViewer( this, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL | SWT.FULL_SELECTION );
    viewer.getTable().setHeaderVisible( true );
    viewer.getTable().setLinesVisible( true );

    TableViewerColumn stridColumn = new TableViewerColumn( viewer, SWT.NONE );
    stridColumn.getColumn().setText( "ИД" ); //$NON-NLS-1$
    stridColumn.getColumn().setWidth( 80 );
    stridColumn.setLabelProvider( new CellLabelProvider() {

      @Override
      public void update( ViewerCell aCell ) {
        ISkObject obj = (ISkObject)aCell.getElement();
        aCell.setText( obj.strid() );
      }
    } );

    TableViewerColumn nameColumn = new TableViewerColumn( viewer, SWT.NONE );
    nameColumn.getColumn().setText( "Имя" ); //$NON-NLS-1$
    nameColumn.getColumn().setWidth( 200 );
    nameColumn.setLabelProvider( new CellLabelProvider() {

      @Override
      public void update( ViewerCell aCell ) {
        ISkObject obj = (ISkObject)aCell.getElement();
        aCell.setText( obj.readableName() );
      }
    } );

    TableViewerColumn descrColumn = new TableViewerColumn( viewer, SWT.NONE );
    descrColumn.getColumn().setText( "Описание" ); //$NON-NLS-1$
    descrColumn.getColumn().setWidth( 500 );
    descrColumn.setLabelProvider( new CellLabelProvider() {

      @Override
      public void update( ViewerCell aCell ) {
        ISkObject obj = (ISkObject)aCell.getElement();
        aCell.setText( obj.description() );
      }
    } );

    viewer.setContentProvider( new ArrayContentProvider() );
    ISkCoreApi coreApi = tsContext().get( ISkConnectionSupplier.class ).defConn().coreApi();
    IList<ISkObject> objects = coreApi.objService().listObjs( "mcc.AnalogInput", false ); //$NON-NLS-1$
    viewer.setInput( objects.toArray() );
  }

}
