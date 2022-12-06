package ru.toxsoft.mcc.ws.mnemos.app.rt.chart;

import static org.toxsoft.core.tsgui.bricks.actions.ITsStdActionDefs.*;

import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.dialogs.datarec.*;
import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.gui.*;
import org.toxsoft.core.tsgui.m5.model.impl.*;
import org.toxsoft.core.tsgui.panels.*;
import org.toxsoft.core.tsgui.panels.toolbar.*;
import org.toxsoft.core.tsgui.utils.layout.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.uskat.core.connection.*;

import ru.toxsoft.mcc.ws.core.templates.api.*;

/**
 * Панель отображения графиков реального времени.<br>
 *
 * @author dima
 */
public class ChartsTabPanel
    extends TsPanel {

  private final TsToolbar toolbar;

  private CTabFolder tabFolder;

  /**
   * Конструктор панели.
   * <p>
   * Конструктор просто запоминает ссылку на контекст, без создания копии.
   *
   * @param aParent {@link Composite} - родительская панель
   * @param aConnection {@link ISkConnection} - соединение с сервером
   * @param aContext {@link ITsGuiContext} - контекст панели
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  public ChartsTabPanel( Composite aParent, ISkConnection aConnection, ITsGuiContext aContext ) {
    super( aParent, aContext );
    this.setLayout( new BorderLayout() );
    // toolbar
    toolbar = new TsToolbar( tsContext() );
    toolbar.setNameLabelText( "RtCharts" );
    toolbar.addActionDefs( //
        ACDEF_ADD, ACDEF_SEPARATOR //
    );
    toolbar.createControl( this );
    toolbar.getControl().setLayoutData( BorderLayout.NORTH );
    toolbar.addListener( aActionId -> {
      if( aActionId.equals( ACDEF_ADD.id() ) ) {
        ISkGraphParam newRtGraph = doAddItem();
        if( newRtGraph != null ) {
          // создаем новую закладку
          CTabItem tabItem = new CTabItem( tabFolder, SWT.CLOSE );
          // закладке дадим имя параметра
          tabItem.setText( newRtGraph.title() );
          RtChartPanel chartPanel = new RtChartPanel( tabFolder, tsContext(), newRtGraph, aConnection );
          tabItem.setControl( chartPanel );
          tabFolder.setSelection( tabItem );
        }
      }
    } );

    tabFolder = new CTabFolder( this, SWT.BORDER );
    tabFolder.setLayout( new BorderLayout() );
    tabFolder.setLayoutData( BorderLayout.CENTER );

  }

  protected ISkGraphParam doAddItem() {
    IM5Model<ISkGraphParam> model =
        m5().getModel( ISkTemplateEditorServiceHardConstants.GRAPH_PARAM_MODEL_ID, ISkGraphParam.class );
    ITsDialogInfo cdi = TsDialogInfo.forCreateEntity( tsContext() );
    IM5BunchEdit<ISkGraphParam> initVals = new M5BunchEdit<>( model );
    return M5GuiUtils.askCreate( tsContext(), model, initVals, cdi, model.getLifecycleManager( null ) );
  }

}
