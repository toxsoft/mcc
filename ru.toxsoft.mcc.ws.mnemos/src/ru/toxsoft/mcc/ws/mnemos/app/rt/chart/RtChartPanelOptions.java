package ru.toxsoft.mcc.ws.mnemos.app.rt.chart;

import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.impl.DataDef.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;
import static org.toxsoft.uskat.ggprefs.lib.IGuiGwPrefsConstants.*;
import static ru.toxsoft.mcc.ws.mnemos.app.rt.chart.ISkResources.*;

import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;

import ru.toxsoft.mcc.ws.core.templates.api.impl.*;

/**
 * Параметры настроек панели "RtCharts".
 * <p>
 *
 * @author dima
 */
public class RtChartPanelOptions {

  /**
   * Идентификатор опции, содержащей имя
   */
  public static final String NAME_OPTION_ID = "band.name"; //$NON-NLS-1$

  /**
   * id группы "RtCharts panel options"
   */
  static String RTCHARTS_PANEL_GROUP_ID = "rtChartsPanelGroup"; //$NON-NLS-1$

  /**
   * путь к группе
   */
  static String RTCHARTS_PANEL_GROUP_PATH = "/rtChartsPanel/group"; //$NON-NLS-1$

  /**
   * описание группы
   */
  public static IDataDef GROUP_OPTION_DEF = create( RTCHARTS_PANEL_GROUP_ID, EAtomicType.STRING, //
      TSID_NAME, STR_N_RTCHARTS_PANEL_GROUP, //
      TSID_DESCRIPTION, STR_D_RTCHARTS_PANEL_GROUP, //
      OPID_TREE_PATH1, RTCHARTS_PANEL_GROUP_PATH, //
      TSID_DEFAULT_VALUE, avStr( TREE_PATH1_ROOT ) ); // путь к группе

  // ------------------------------------------------------------------------------------
  // параметры панели
  //

  /**
   * Список отображаемых графиков <br>
   */
  public static final IDataDef RTCHARTS = DataDef.create( createId( "rtCharts" ), EAtomicType.VALOBJ, // //$NON-NLS-1$
      TSID_DESCRIPTION, STR_D_RTCHARTS, //
      TSID_NAME, STR_N_RTCHARTS, //
      // EValedControlParam.EDITOR_FACTORY_NAME, ValedAvListLookupEditor.FACTORY_NAME, //
      TSID_KEEPER_ID, SkGraphParamsList.KEEPER_ID, //
      TSID_DEFAULT_VALUE, avValobj( new SkGraphParamsList() ), //
      OPID_TREE_PATH1, RTCHARTS_PANEL_GROUP_PATH );

  /**
   * Все опции в виде списка
   *
   * @return IStridablesListEdit&lt;IDataDef> - все опции в виде списка
   */
  public static IStridablesList<IDataDef> allOptions() {
    IStridablesListEdit<IDataDef> options = new StridablesList<>();
    options.add( RTCHARTS );
    return options;
  }

  /**
   * Возвращает набор опций, инициализированный набором значений по-умолчанию
   *
   * @return IOptionSet - набор опций, инициализированный набором значений по-умолчанию
   */
  public static IOptionSet defaultOptions() {
    IOptionSetEdit opSet = new OptionSet();
    opSet.setValue( RTCHARTS, RTCHARTS.defaultValue() );

    return opSet;
  }

  // ------------------------------------------------------------------------------------
  // Внутренняя реализация
  //

  static String createId( String aShortId ) {
    return RtChartPanelOptions.class.getSimpleName() + "." + aShortId; //$NON-NLS-1$
  }

}
