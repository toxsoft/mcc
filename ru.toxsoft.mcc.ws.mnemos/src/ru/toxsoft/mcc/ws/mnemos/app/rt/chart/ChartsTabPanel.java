package ru.toxsoft.mcc.ws.mnemos.app.rt.chart;

import static org.toxsoft.core.tsgui.bricks.actions.ITsStdActionDefs.*;
import static ru.toxsoft.mcc.ws.mnemos.app.rt.chart.ISkResources.*;

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
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.gw.gwid.*;
import org.toxsoft.core.tslib.gw.skid.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.uskat.core.*;
import org.toxsoft.uskat.core.api.users.*;
import org.toxsoft.uskat.core.connection.*;
import org.toxsoft.uskat.ggprefs.gui.*;
import org.toxsoft.uskat.ggprefs.lib.*;
import org.toxsoft.uskat.ggprefs.lib.impl.*;
import org.toxsoft.uskat.s5.utils.*;

import ru.toxsoft.mcc.ws.core.templates.api.*;
import ru.toxsoft.mcc.ws.core.templates.api.impl.*;
import ru.toxsoft.mcc.ws.mnemos.app.rt.chart.data_aliases.*;
import ru.toxsoft.mcc.ws.mnemos.app.rt.chart.data_aliases.impl.*;

/**
 * Панель отображения графиков реального времени.<br>
 *
 * @author dima
 */
public class ChartsTabPanel
    extends TsPanel {

  private final TsToolbar toolbar;
  private CTabFolder      tabFolder;

  private IGuiGwPrefsSection prefSection;

  private final ISkConnection conn;

  private final Skid       userSkid;
  private final Gwid       userGwid;
  IListEdit<ISkGraphParam> rtCharts = new ElemArrayList<>();

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
    conn = aConnection;
    ISkUser currUser = S5ConnectionUtils.getConnectedUser( aConnection.coreApi() );
    userSkid = new Skid( ISkUser.CLASS_ID, currUser.strid() );
    userGwid = Gwid.createObj( ISkUser.CLASS_ID, currUser.strid() );

    // toolbar
    toolbar = new TsToolbar( tsContext() );
    toolbar.setNameLabelText( RTCHARTS_TOOLBAR_TITLE );
    toolbar.addActionDefs( //
        ACDEF_ADD, ACDEF_SEPARATOR, ACDEF_EDIT //
    );
    toolbar.createControl( this );
    toolbar.getControl().setLayoutData( BorderLayout.NORTH );
    toolbar.addListener( aActionId -> {
      if( aActionId.equals( ACDEF_ADD.id() ) ) {
        ISkGraphParam newRtGraph = doAddItem();
        addRtChart( newRtGraph );
      }
      if( aActionId.equals( ACDEF_EDIT.id() ) ) {
        editGwGuiPrefs( aContext.get( Shell.class ) );
        // получаем текущий график
        CTabItem selTab = tabFolder.getSelection();
        ISkGraphParam selelectedGraphParam = (ISkGraphParam)selTab.getData();
        ISkGraphParam newRtGraph = doEditItem( selelectedGraphParam );
        if( newRtGraph != null ) {
          rtCharts.remove( selelectedGraphParam );
          // гасим RtChart
          RtChartPanel chartPanel = (RtChartPanel)selTab.getControl();
          chartPanel.dispose();
          selTab.dispose();
          addRtChart( newRtGraph );
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
        ISkGraphParam graphParam = (ISkGraphParam)event.item.getData();
        rtCharts.remove( graphParam );
        saveUserSettings();
      }
    } );
    // инициализируем настройки панели
    initPanelPrefs();
    // восстанавливаем внешний вид панели
    restoreUserSettings();
  }

  private ISkGraphParam doEditItem( ISkGraphParam aSelelectedGraphParam ) {
    IM5Domain m5 = conn.scope().get( IM5Domain.class );
    IM5Model<ISkGraphParam> model =
        m5.getModel( ISkTemplateEditorServiceHardConstants.GRAPH_PARAM_MODEL_ID, ISkGraphParam.class );
    ITsDialogInfo cdi = TsDialogInfo.forCreateEntity( tsContext() );
    return M5GuiUtils.askEdit( tsContext(), model, aSelelectedGraphParam, cdi, model.getLifecycleManager( null ) );
  }

  private void addRtChart( ISkGraphParam aRtGraph ) {
    if( aRtGraph != null ) {
      addRtChartToTabPanel( aRtGraph );
      rtCharts.add( aRtGraph );
      saveUserSettings();
    }
  }

  private void addRtChartToTabPanel( ISkGraphParam aRtGraph ) {
    // создаем новую закладку
    CTabItem tabItem = new CTabItem( tabFolder, SWT.CLOSE );
    // закладке дадим имя параметра
    tabItem.setText( aRtGraph.title() );
    RtChartPanel chartPanel = new RtChartPanel( tabFolder, tsContext(), aRtGraph, conn );
    tabItem.setControl( chartPanel );
    tabFolder.setSelection( tabItem );
    tabItem.setData( aRtGraph );
  }

  private void restoreUserSettings() {
    // получаем список графиков и создаем для каждого свой RtChart
    IOptionSet userPrefs = getUserPrefs();
    ISkGraphParamsList rtChartsList = RtChartPanelOptions.RTCHARTS.getValue( userPrefs ).asValobj();
    rtCharts.addAll( rtChartsList.items() );
    for( ISkGraphParam rtChart : rtCharts ) {
      addRtChartToTabPanel( rtChart );
    }
  }

  private void saveUserSettings() {
    IOptionSetEdit userPrefs = new OptionSet( getUserPrefs() );
    ISkGraphParamsList rtChartsList = new SkGraphParamsList( rtCharts );
    RtChartPanelOptions.RTCHARTS.setValue( userPrefs, AvUtils.avValobj( rtChartsList ) );
    prefSection.setOptions( userSkid, userPrefs );
  }

  private void initPanelPrefs() {
    ISkCoreApi coreApi = conn.coreApi();

    if( !coreApi.services().hasKey( ISkGuiGwPrefsService.SERVICE_ID ) ) {
      coreApi.addService( SkGuiGwPrefsService.CREATOR );
    }
    prefSection = PrefUtils.section( PrefUtils.RTCHARTS_PREFS_SECTION_ID, conn );

    // Задание опций
    IStridablesListEdit<IDataDef> panelPrefs = new StridablesList<>();
    panelPrefs.addAll( RtChartPanelOptions.allOptions() );

    IList<IDataDef> currOpDefs = prefSection.listOptionDefs( userSkid );
    IStridablesListEdit<IDataDef> newPrefDefs = new StridablesList<>();
    // перебираем все устанавливаемые опции и добавляем только новые
    for( IDataDef addingOptDef : RtChartPanelOptions.allOptions() ) {
      if( !hasOptionDef( currOpDefs, addingOptDef ) ) {
        newPrefDefs.add( addingOptDef );
      }
    }
    prefSection.bindOptions( userGwid, newPrefDefs );
  }

  /**
   * Проверяет наличие описание опции в текущем списке
   *
   * @param aCurrOpDefs текущий список описания опций
   * @param aOptDef описание добавляемой опции
   * @return true опция уже определена
   */
  private static boolean hasOptionDef( IList<IDataDef> aCurrOpDefs, IDataDef aOptDef ) {
    for( IDataDef currOptDef : aCurrOpDefs ) {
      if( currOptDef.id().equals( aOptDef.id() ) ) {
        return true;
      }
    }
    return false;
  }

  private IOptionSet getUserPrefs() {
    return prefSection.getOptions( userSkid );
  }

  protected ISkGraphParam doAddItem() {
    IM5Domain m5 = conn.scope().get( IM5Domain.class );
    IM5Model<ISkGraphParam> model =
        m5.getModel( ISkTemplateEditorServiceHardConstants.GRAPH_PARAM_MODEL_ID, ISkGraphParam.class );
    ITsDialogInfo cdi = TsDialogInfo.forCreateEntity( tsContext() );
    IM5BunchEdit<ISkGraphParam> initVals = new M5BunchEdit<>( model );
    return M5GuiUtils.askCreate( tsContext(), model, initVals, cdi, model.getLifecycleManager( null ) );
  }

  // ------------------------------------------------------------------------------------
  // Кусок реализации настроек для АРМа, здесь временно после отладки <br>
  // TODO перенести в нужное место в АРМе
  //

  /**
   * TODO перенести в нужное место <br>
   * Вызывает редактор настроек GUI.
   *
   * @param aShell окно
   * @return boolean true - признак, что пользователь сделал изменения, а false отказался от правок
   */
  private boolean editGwGuiPrefs( Shell aShell ) {
    ITsDialogInfo dialogInfo = new TsDialogInfo( tsContext(), DLC_C_PREFS_EDIT, DLC_T_PREFS_EDIT );

    // подготовка и вызов диалога редактирования
    // return GuiGwPrefsUtils.editGuiGwPrefs( tsContext(), dialogInfo, conn, prefSection, new SkidList( userSkid ),
    // groupDefs );

    GuiDataAliasesPrefsEditModel model = new GuiDataAliasesPrefsEditModel( prefSection, userSkid );

    IM5Domain d = conn.scope().get( IM5Domain.class );
    d.initTemporaryModel( model );

    GuiGwPrefsEditLifecycleManager manager = new GuiGwPrefsEditLifecycleManager( model, prefSection );

    return M5GuiUtils.askEdit( tsContext(), model, userSkid, dialogInfo, manager ) != null;

  }

  private final IStridablesListEdit<IDataDef> groupDefs            = new StridablesList<>();
  private static final String                 GUI_PREFS_SECTION_ID = "VJ_GUI_PREF_SECTION";

  private void initGroupDefs() {
    // data aliases
    groupDefs.addAll( MccSystemOptions.GROUP_OPTION_DEF );
  }

  private void initGuiPrefs() {
    ISkCoreApi coreApi = conn.coreApi();

    if( !coreApi.services().hasKey( ISkGuiGwPrefsService.SERVICE_ID ) ) {
      coreApi.addService( SkGuiGwPrefsService.CREATOR );
    }
    prefSection = PrefUtils.section( GUI_PREFS_SECTION_ID, conn );

    // Задание опций
    IStridablesListEdit<IDataDef> guiPrefs = new StridablesList<>();
    guiPrefs.addAll( MccSystemOptions.allOptions() );

    IList<IDataDef> currOpDefs = prefSection.listOptionDefs( userSkid );
    IStridablesListEdit<IDataDef> newPrefDefs = new StridablesList<>();
    // перебираем все устанавливаемые опции и добавляем только новые
    for( IDataDef addingOptDef : MccSystemOptions.allOptions() ) {
      if( !hasOptionDef( currOpDefs, addingOptDef ) ) {
        newPrefDefs.add( addingOptDef );
      }
    }
    prefSection.bindOptions( userGwid, newPrefDefs );
  }

}
