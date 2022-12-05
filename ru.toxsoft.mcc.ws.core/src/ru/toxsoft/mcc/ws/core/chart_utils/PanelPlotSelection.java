package ru.toxsoft.mcc.ws.core.chart_utils;

import org.eclipse.jface.resource.*;
import org.eclipse.jface.viewers.*;
import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.ui.plugin.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.chart.api.*;
import org.toxsoft.core.tsgui.dialogs.datarec.*;
import org.toxsoft.core.tsgui.utils.layout.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.utils.*;

import ru.toxsoft.mcc.ws.core.*;

/**
 * @author vs
 * @author dima // conversion ts4
 */
public class PanelPlotSelection
    extends AbstractTsDialogPanel<Pair<IList<IPlotDef>, IList<IPlotDef>>, ITsGuiContext> {

  static IDialogPanelCreator<Pair<IList<IPlotDef>, IList<IPlotDef>>, ITsGuiContext> creator = PanelPlotSelection::new;

  ListViewer          leftViewer;
  ListViewer          rightViewer;
  IListEdit<IPlotDef> visiblePlots;
  IListEdit<IPlotDef> hiddenPlots;

  /**
   * Конструктор для использования внутри диалога.
   *
   * @param aParent Composite - родительская панель
   * @param aOwnerDialog CommonDialogBase - родительский диалог
   */
  public PanelPlotSelection( Composite aParent,
      TsDialog<Pair<IList<IPlotDef>, IList<IPlotDef>>, ITsGuiContext> aOwnerDialog ) {
    super( aParent, aOwnerDialog );
    setLayout( new FillLayout() );
    init();
  }

  void init() {
    String pluginId = Activator.PLUGIN_ID;
    SashForm sash = new SashForm( this, SWT.HORIZONTAL );

    Composite leftPane = new Composite( sash, SWT.NONE );
    leftPane.setLayout( new BorderLayout() );
    Composite tbPanel = new Composite( leftPane, SWT.NONE );
    tbPanel.setLayoutData( BorderLayout.NORTH );
    GridLayout gl = new GridLayout( 2, false );
    gl.marginWidth = 8;
    gl.marginHeight = 8;
    tbPanel.setLayout( gl );
    CLabel l = new CLabel( tbPanel, SWT.CENTER );
    l.setText( "Видимые графики:" );
    Button btnHide = new Button( tbPanel, SWT.PUSH );
    ImageDescriptor imgDescr = AbstractUIPlugin.imageDescriptorFromPlugin( pluginId, "icons/is24x24/visible_off.png" ); //$NON-NLS-1$
    btnHide.setImage( imgDescr.createImage() );
    // явно удаляем ранее загруженную картинку
    btnHide.addDisposeListener( aE -> btnHide.getImage().dispose() );
    btnHide.setToolTipText( "Скрыть график" );
    btnHide.setEnabled( false );
    btnHide.addSelectionListener( new SelectionAdapter() {

      @Override
      public void widgetSelected( SelectionEvent e ) {
        IStructuredSelection s = (IStructuredSelection)leftViewer.getSelection();
        if( !s.isEmpty() ) {
          hidePlotDef( (IPlotDef)s.getFirstElement() );
        }
      }
    } );

    leftViewer = new ListViewer( leftPane, SWT.BORDER );
    leftViewer.getList().setLayoutData( BorderLayout.CENTER );
    leftViewer.addSelectionChangedListener( aEvent -> btnHide.setEnabled( !aEvent.getSelection().isEmpty() ) );
    leftViewer.setLabelProvider( new LabelProvider() {

      @Override
      public String getText( Object aElement ) {
        IPlotDef plotDef = (IPlotDef)aElement;
        return plotDef.nmName();
      }
    } );
    leftViewer.getList().addMouseListener( new MouseAdapter() {

      @Override
      public void mouseDoubleClick( MouseEvent aE ) {
        IStructuredSelection s = (IStructuredSelection)leftViewer.getSelection();
        if( !s.isEmpty() ) {
          hidePlotDef( (IPlotDef)s.getFirstElement() );
        }
      }
    } );

    leftViewer.setContentProvider( new ArrayContentProvider() );

    Composite rightPane = new Composite( sash, SWT.NONE );
    rightPane.setLayout( new BorderLayout() );
    tbPanel = new Composite( rightPane, SWT.NONE );
    tbPanel.setLayoutData( BorderLayout.NORTH );
    tbPanel.setLayout( gl );
    l = new CLabel( tbPanel, SWT.CENTER );
    l.setText( "Скрытые графики:" );
    Button btnShow = new Button( tbPanel, SWT.PUSH );
    imgDescr = AbstractUIPlugin.imageDescriptorFromPlugin( pluginId, "icons/is24x24/visible_on.png" ); //$NON-NLS-1$
    btnShow.setImage( imgDescr.createImage() );
    // явно удаляем ранее загруженную картинку
    btnShow.addDisposeListener( aE -> btnShow.getImage().dispose() );
    btnShow.setToolTipText( "Показать график" );
    btnShow.setEnabled( false );
    btnShow.addSelectionListener( new SelectionAdapter() {

      @Override
      public void widgetSelected( SelectionEvent e ) {
        IStructuredSelection s = (IStructuredSelection)rightViewer.getSelection();
        if( !s.isEmpty() ) {
          showPlotDef( (IPlotDef)s.getFirstElement() );
        }
      }
    } );

    rightViewer = new ListViewer( rightPane, SWT.BORDER );
    rightViewer.getList().setLayoutData( BorderLayout.CENTER );
    rightViewer.addSelectionChangedListener( aEvent -> btnShow.setEnabled( !aEvent.getSelection().isEmpty() ) );
    rightViewer.setLabelProvider( new LabelProvider() {

      @Override
      public String getText( Object aElement ) {
        IPlotDef plotDef = (IPlotDef)aElement;
        return plotDef.nmName();
      }
    } );
    rightViewer.getList().addMouseListener( new MouseAdapter() {

      @Override
      public void mouseDoubleClick( MouseEvent aE ) {
        IStructuredSelection s = (IStructuredSelection)rightViewer.getSelection();
        if( !s.isEmpty() ) {
          showPlotDef( (IPlotDef)s.getFirstElement() );
        }
      }
    } );

    rightViewer.setContentProvider( new ArrayContentProvider() );

    sash.setWeights( 1, 1 );
  }

  // ------------------------------------------------------------------------------------
  // Реализация методов AbstractDataRecordPanel
  //

  @Override
  protected void doSetDataRecord( Pair<IList<IPlotDef>, IList<IPlotDef>> aData ) {
    visiblePlots = new ElemArrayList<>( aData.left() );
    hiddenPlots = new ElemArrayList<>( aData.right() );
    updateLists();
  }

  @Override
  protected Pair<IList<IPlotDef>, IList<IPlotDef>> doGetDataRecord() {
    return new Pair<>( visiblePlots, hiddenPlots );
  }

  // ------------------------------------------------------------------------------------
  // Статический метод вызова диалога
  //

  public static Pair<IList<IPlotDef>, IList<IPlotDef>> selectPlots( Shell aShell,
      Pair<IList<IPlotDef>, IList<IPlotDef>> aPlots, ITsGuiContext aContext ) {
    String caption = "Выбор графиков для отображения";
    String title = "Двойной щелчек мышкой меняет видимость выбранного графика";
    // CommonDialogBase<Pair<IList<IPlotDef>, IList<IPlotDef>>, ITsGuiContext> dlg;
    // dlg = new CommonDialogBase<>( aShell, aPlots, aContext, caption, title, 0, creator );
    IDialogPanelCreator<Pair<IList<IPlotDef>, IList<IPlotDef>>, ITsGuiContext> locCreator = PanelPlotSelection::new;
    ITsDialogInfo dlgInfo = new TsDialogInfo( aContext, caption, title );
    TsDialog<Pair<IList<IPlotDef>, IList<IPlotDef>>, ITsGuiContext> dlg =
        new TsDialog<>( dlgInfo, aPlots, aContext, locCreator );

    return dlg.execData();
  }

  // ------------------------------------------------------------------------------------
  // Внутренняя реализация
  //

  void updateLists() {
    leftViewer.setInput( visiblePlots.toArray() );
    rightViewer.setInput( hiddenPlots.toArray() );
  }

  void hidePlotDef( IPlotDef aPlotDef ) {
    visiblePlots.remove( aPlotDef );
    hiddenPlots.add( aPlotDef );
    updateLists();
  }

  void showPlotDef( IPlotDef aPlotDef ) {
    visiblePlots.add( aPlotDef );
    hiddenPlots.remove( aPlotDef );
    updateLists();
  }

}
