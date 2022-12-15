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
import org.toxsoft.core.tslib.gw.gwid.*;
import org.toxsoft.core.tslib.gw.skid.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.uskat.core.api.users.*;
import org.toxsoft.uskat.core.connection.*;
import org.toxsoft.uskat.s5.utils.*;

import ru.toxsoft.mcc.ws.core.templates.api.*;

/**
 * Панель отображения графиков реального времени.<br>
 *
 * @author dima
 */
public class ChartsTabPanel
    extends TsPanel {

  private final TsToolbar toolbar;
  private CTabFolder      tabFolder;

  /**
   * ID атрибута класса - маркер того что это внутренний класс для работы с настройками ГДП.
   */
  private static final String AID_GUI_GW_PREFS_MARKER = "Class4RtChartsPanelPrefs"; //$NON-NLS-1$

  // TODO ts4 замена IOptionDef
  // private ISkGuiGwPrefsService ggpService;
  // private IGuiGwPrefsSection prefSection;
  //
  // /**
  // * Набор rtCharts у панели.
  // */
  // public static final IOptionDef RTСHARTS = AvMetaUtils.createFimbedColl2( "rtChartsOptId", //
  // SkGraphParam.KEEPER, //
  // TSID_DESCRIPTION, "настройки панели RtCharts", //
  // TSID_NAME, "настройки панели RtCharts", //
  // TSID_DEFAULT_VALUE, avValobj( IList.EMPTY ) ); // тут пустой список

  private final ISkConnection conn;

  private final Skid userSkid;
  private final Gwid userGwid;

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
    tabFolder.addCTabFolder2Listener( new CTabFolder2Adapter() {

      @Override
      public void close( CTabFolderEvent event ) {
        // TODO удаляем описание RtChart из настроек
      }
    } );
    // инициализируем настройки панели
    initPanelPrefs( aConnection );
    // восстанавливаем внешний вид панели
    restoreUserSettings();
  }

  private void restoreUserSettings() {
    // получаем список графиков и создаем для каждого свой RtChart
    // IOptionSet userPrefs = getUserPrefs();
    // теперь получаем список
    // TODO замена getFimbedColl
    // IList<SkGraphParam> rtCharts = RTСHARTS.getFimbedColl( userPrefs );

  }

  private void initPanelPrefs( ISkConnection aConnection ) {
    // ISkCoreApi coreApi = aConnection.coreApi();
    //
    // if( !coreApi.services().hasKey( ISkGuiGwPrefsService.SERVICE_ID ) ) {
    // coreApi.addService( SkGuiGwPrefsService.CREATOR );
    // }
    // ggpService = coreApi.getService( ISkGuiGwPrefsService.SERVICE_ID );
    // prefSection = GdpPrefUtils.section( GUI_GW_PREFS_SECTION_ID, docConn );
    //
    // // Задание опций
    // IStridablesListEdit<IOptionDef> panelPrefs = new StridablesList<>();
    // panelPrefs.add( RTСHARTS );
    //
    // IList<IOptionDef> currOpDefs = prefSection.listOptionDefs( systemSkid );
    // IStridablesListEdit<IOptionDef> newPrefDefs = new StridablesList<>();
    // // перебираем все устанавливаемые опции и добавляем только новые
    // for( IOptionDef addingOptDef : aDefs ) {
    // if( !hasOptionDef( currOpDefs, addingOptDef ) ) {
    // newPrefDefs.add( addingOptDef );
    // }
    // }
    // prefSection.bindOptions( userGwid, newPrefDefs );

  }

  // private IOptionSet getUserPrefs() {
  // // проверим наличие этого (специального) класса для настроек
  // ISkClassInfo clsInfo = conn.coreApi().sysdescr().classInfoManager().findClassInfo( userSkid.classId() );
  // if( clsInfo == null ) {
  // return IOptionSet.NULL;
  // }
  // // Проверяем существование объекта
  // ISkObject prefsObj = conn.coreApi().objService().find( userSkid );
  // if( prefsObj == null ) {
  // // тут проверяем что это объект который "на совести" helper'а
  // if( isPrefClass( userSkid.classId() ) ) {
  // // создаем новый объект
  // DpuObject dpu = DpuObject.create( userSkid.classId(), userSkid.strid(), //
  // AID_GUI_GW_PREFS_MARKER, avBool( true ) //
  // );
  // conn.coreApi().objService().defineObject( dpu );
  // }
  // }
  // return prefSection.getOptions( userSkid );
  // }

  protected ISkGraphParam doAddItem() {

    IM5Model<ISkGraphParam> model =
        m5().getModel( ISkTemplateEditorServiceHardConstants.GRAPH_PARAM_MODEL_ID, ISkGraphParam.class );
    ITsDialogInfo cdi = TsDialogInfo.forCreateEntity( tsContext() );
    IM5BunchEdit<ISkGraphParam> initVals = new M5BunchEdit<>( model );
    return M5GuiUtils.askCreate( tsContext(), model, initVals, cdi, model.getLifecycleManager( null ) );
  }

}
