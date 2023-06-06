package ru.toxsoft.mcc.ws.mnemos.app.rt.chart;

import static org.toxsoft.core.tsgui.bricks.actions.ITsStdActionDefs.*;
import static ru.toxsoft.mcc.ws.mnemos.app.rt.chart.ISkResources.*;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.widgets.Composite;
import org.toxsoft.core.tsgui.bricks.ctx.ITsGuiContext;
import org.toxsoft.core.tsgui.dialogs.datarec.ITsDialogInfo;
import org.toxsoft.core.tsgui.dialogs.datarec.TsDialogInfo;
import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.gui.M5GuiUtils;
import org.toxsoft.core.tsgui.m5.model.impl.M5BunchEdit;
import org.toxsoft.core.tsgui.panels.TsPanel;
import org.toxsoft.core.tsgui.panels.toolbar.TsToolbar;
import org.toxsoft.core.tsgui.utils.layout.BorderLayout;
import org.toxsoft.core.tslib.av.impl.AvUtils;
import org.toxsoft.core.tslib.av.metainfo.IDataDef;
import org.toxsoft.core.tslib.av.opset.IOptionSet;
import org.toxsoft.core.tslib.av.opset.IOptionSetEdit;
import org.toxsoft.core.tslib.av.opset.impl.OptionSet;
import org.toxsoft.core.tslib.bricks.strid.coll.IStridablesListEdit;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.StridablesList;
import org.toxsoft.core.tslib.coll.IList;
import org.toxsoft.core.tslib.gw.gwid.Gwid;
import org.toxsoft.core.tslib.gw.skid.Skid;
import org.toxsoft.core.tslib.gw.skid.SkidList;
import org.toxsoft.core.tslib.utils.errors.TsNullArgumentRtException;
import org.toxsoft.skf.ggprefs.lib.IGuiGwPrefsSection;
import org.toxsoft.skf.ggprefs.lib.ISkGuiGwPrefsService;
import org.toxsoft.skf.ggprefs.lib.impl.SkGuiGwPrefsService;
import org.toxsoft.skf.reports.templates.service.IVtGraphTemplate;
import org.toxsoft.uskat.core.ISkCoreApi;
import org.toxsoft.uskat.core.api.users.ISkUser;
import org.toxsoft.uskat.core.connection.ISkConnection;
import org.toxsoft.uskat.s5.utils.S5ConnectionUtils;

/**
 * Панель отображения графиков реального времени.<br>
 *
 * @author dima
 */
public class ChartsTabPanel
    extends TsPanel {

  /**
   * признак того что панель сверху
   */
  private final boolean   top;
  private final TsToolbar toolbar;
  private CTabFolder      tabFolder;

  private IGuiGwPrefsSection prefSection;

  private final ISkConnection conn;

  private final Skid userSkid;
  private final Gwid userGwid;
  SkidList           rtChartSkids = new SkidList();

  /**
   * Конструктор панели.
   * <p>
   * Конструктор просто запоминает ссылку на контекст, без создания копии.
   *
   * @param aParent {@link Composite} - родительская панель
   * @param aConnection {@link ISkConnection} - соединение с сервером
   * @param aContext {@link ITsGuiContext} - контекст панели
   * @param isTop признак того что панель сверху
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  public ChartsTabPanel( Composite aParent, ISkConnection aConnection, ITsGuiContext aContext, boolean isTop ) {
    super( aParent, aContext );
    this.setLayout( new BorderLayout() );
    conn = aConnection;
    top = isTop;
    ISkUser currUser = S5ConnectionUtils.getConnectedUser( aConnection.coreApi() );
    userSkid = new Skid( ISkUser.CLASS_ID, currUser.strid() );
    userGwid = Gwid.createObj( ISkUser.CLASS_ID, currUser.strid() );

    // toolbar
    toolbar = new TsToolbar( tsContext() );
    toolbar.setNameLabelText( RTCHARTS_TOOLBAR_TITLE );
    toolbar.addActionDefs( //
        ACDEF_ADD, ACDEF_SEPARATOR, ACDEF_EDIT, ACDEF_SEPARATOR, ACDEF_REMOVE //
    );
    toolbar.createControl( this );
    toolbar.getControl().setLayoutData( BorderLayout.NORTH );
    toolbar.addListener( aActionId -> {
      if( aActionId.equals( ACDEF_ADD.id() ) ) {
        IVtGraphTemplate newRtGraphTemplate = doAddTemplate();
        addRtChartTemplate( newRtGraphTemplate );
      }
      if( aActionId.equals( ACDEF_REMOVE.id() ) ) {
        // получаем текущий график
        CTabItem selTab = tabFolder.getSelection();
        IVtGraphTemplate selGraphTemplate = (IVtGraphTemplate)selTab.getData();
        rtChartSkids.remove( selGraphTemplate.skid() );
        // гасим RtChart
        RtChartPanel chartPanel = (RtChartPanel)selTab.getControl();
        chartPanel.dispose();
        selTab.dispose();
      }
      if( aActionId.equals( ACDEF_EDIT.id() ) ) {
        // получаем текущий график
        CTabItem selTab = tabFolder.getSelection();
        IVtGraphTemplate selGraphTemplate = (IVtGraphTemplate)selTab.getData();
        IVtGraphTemplate newRtGraphTemplate = doEditTemplate( selGraphTemplate );
        if( newRtGraphTemplate != null ) {
          rtChartSkids.remove( selGraphTemplate.skid() );
          // гасим RtChart
          RtChartPanel chartPanel = (RtChartPanel)selTab.getControl();
          chartPanel.dispose();
          selTab.dispose();
          addRtChartTemplate( newRtGraphTemplate );
        }
      }
    } );

    tabFolder = new CTabFolder( this, SWT.BORDER );
    tabFolder.setLayout( new BorderLayout() );
    tabFolder.setLayoutData( BorderLayout.CENTER );
    tabFolder.addCTabFolder2Listener( new CTabFolder2Adapter() {

      @Override
      public void close( CTabFolderEvent event ) {
        // удаляем описание RtChart из настроек
        IVtGraphTemplate graphTemplate = (IVtGraphTemplate)event.item.getData();
        rtChartSkids.remove( graphTemplate.skid() );
        saveUserSettings();
      }
    } );
    // инициализируем настройки панели
    initPanelPrefs();
    // восстанавливаем внешний вид панели
    restoreUserSettings();
  }

  private IVtGraphTemplate doEditTemplate( IVtGraphTemplate aSelGraphTemplate ) {
    IM5Domain m5 = conn.scope().get( IM5Domain.class );
    IM5Model<IVtGraphTemplate> model = m5.getModel( IVtGraphTemplate.CLASS_ID, IVtGraphTemplate.class );
    ITsDialogInfo cdi = TsDialogInfo.forCreateEntity( tsContext() );
    return M5GuiUtils.askEdit( tsContext(), model, aSelGraphTemplate, cdi, model.getLifecycleManager( null ) );
  }

  private void addRtChartTemplate( IVtGraphTemplate aRtGraphTemplate ) {
    if( aRtGraphTemplate != null ) {
      addRtChartTemplateToTabPanel( aRtGraphTemplate );
      rtChartSkids.add( aRtGraphTemplate.skid() );
      saveUserSettings();
    }
  }

  private void addRtChartTemplateToTabPanel( IVtGraphTemplate aRtGraphTemplate ) {
    // создаем новую закладку
    CTabItem tabItem = new CTabItem( tabFolder, SWT.NONE );
    // закладке дадим имя параметра
    tabItem.setText( aRtGraphTemplate.nmName() );
    RtChartPanel chartPanel = new RtChartPanel( tabFolder, tsContext(), aRtGraphTemplate, conn );
    tabItem.setControl( chartPanel );
    tabFolder.setSelection( tabItem );
    tabItem.setData( aRtGraphTemplate );
  }

  private void restoreUserSettings() {
    // получаем список графиков и создаем для каждого свой RtChart
    IOptionSet userPrefs = getUserPrefs();
    rtChartSkids = RtChartPanelOptions.RTCHART_SKIDS.getValue( userPrefs ).asValobj();
    for( Skid rtChartTemplateSkid : rtChartSkids ) {
      IVtGraphTemplate graphTemplate = conn.coreApi().objService().get( rtChartTemplateSkid );
      addRtChartTemplateToTabPanel( graphTemplate );
    }
  }

  private void saveUserSettings() {
    IOptionSetEdit userPrefs = new OptionSet( getUserPrefs() );
    RtChartPanelOptions.RTCHART_SKIDS.setValue( userPrefs, AvUtils.avValobj( rtChartSkids ) );
    prefSection.setOptions( userSkid, userPrefs );
  }

  private void initPanelPrefs() {
    ISkCoreApi coreApi = conn.coreApi();

    if( !coreApi.services().hasKey( ISkGuiGwPrefsService.SERVICE_ID ) ) {
      coreApi.addService( SkGuiGwPrefsService.CREATOR );
    }
    prefSection = PrefUtils
        .section( top ? PrefUtils.TOP_RTCHARTS_PREFS_SECTION_ID : PrefUtils.BOTTOM_RTCHARTS_PREFS_SECTION_ID, conn );

    // Задание опций
    IStridablesListEdit<IDataDef> panelPrefs = new StridablesList<>();
    panelPrefs.addAll( RtChartPanelOptions.allOptions() );

    IList<IDataDef> currOpDefs = prefSection.listOptionDefs( userSkid );
    IStridablesListEdit<IDataDef> newPrefDefs = new StridablesList<>();
    // перебираем все устанавливаемые опции и добавляем только новые
    for( IDataDef addingOptDef : RtChartPanelOptions.allOptions() ) {
      if( !PrefUtils.hasOptionDef( currOpDefs, addingOptDef ) ) {
        newPrefDefs.add( addingOptDef );
      }
    }
    prefSection.bindOptions( userGwid, newPrefDefs );
  }

  private IOptionSet getUserPrefs() {
    return prefSection.getOptions( userSkid );
  }

  protected IVtGraphTemplate doAddTemplate() {
    IM5Domain m5 = conn.scope().get( IM5Domain.class );
    IM5Model<IVtGraphTemplate> model = m5.getModel( IVtGraphTemplate.CLASS_ID, IVtGraphTemplate.class );
    ITsDialogInfo cdi = TsDialogInfo.forCreateEntity( tsContext() );
    IM5BunchEdit<IVtGraphTemplate> initVals = new M5BunchEdit<>( model );
    return M5GuiUtils.askCreate( tsContext(), model, initVals, cdi, model.getLifecycleManager( null ) );
  }

}
