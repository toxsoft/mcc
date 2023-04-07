package ru.toxsoft.mcc.ws.mnemos.app.rt.chart.data_aliases;

import static org.toxsoft.skf.ggprefs.lib.IGuiGwPrefsConstants.*;

import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.impl.DataDef.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;
import static ru.toxsoft.mcc.ws.mnemos.app.rt.chart.data_aliases.ISkResources.*;

import org.toxsoft.core.tslib.av.EAtomicType;
import org.toxsoft.core.tslib.av.impl.DataDef;
import org.toxsoft.core.tslib.av.metainfo.IDataDef;
import org.toxsoft.core.tslib.av.opset.IOptionSet;
import org.toxsoft.core.tslib.av.opset.IOptionSetEdit;
import org.toxsoft.core.tslib.av.opset.impl.OptionSet;
import org.toxsoft.core.tslib.bricks.strid.coll.IStridablesList;
import org.toxsoft.core.tslib.bricks.strid.coll.IStridablesListEdit;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.StridablesList;

import ru.toxsoft.mcc.ws.mnemos.app.rt.chart.data_aliases.impl.DataNameAliasesList;

/**
 * Параметры настроек АРМа Москокса".
 * <p>
 *
 * @author dima
 */
public class MccSystemOptions {

  /**
   * Идентификатор опции, содержащей имя
   */
  public static final String NAME_OPTION_ID = "band.name"; //$NON-NLS-1$

  /**
   * id группы "Mcc System options"
   */
  static String MCC_SYSTEM_OPT_GROUP_ID = "MccSystemOptions"; //$NON-NLS-1$

  /**
   * путь к группе
   */
  static String MCC_SYSTEM_OPT_GROUP_PATH = "/mccSystemOptions/group"; //$NON-NLS-1$

  /**
   * описание группы
   */
  public static IDataDef GROUP_OPTION_DEF = create( MCC_SYSTEM_OPT_GROUP_ID, EAtomicType.STRING, //
      TSID_NAME, STR_N_MCC_SYSTEM_OPTIONS_GROUP, //
      TSID_DESCRIPTION, STR_D_MCC_SYSTEM_OPTIONS_GROUP, //
      OPID_TREE_PATH1, MCC_SYSTEM_OPT_GROUP_PATH, //
      TSID_DEFAULT_VALUE, avStr( TREE_PATH1_ROOT ) ); // путь к группе

  // ------------------------------------------------------------------------------------
  // параметры панели
  //

  /**
   * Data name aliases list <br>
   */
  public static final IDataDef DATA_NAME_ALIASES = DataDef.create( createId( "dataNameAliases" ), EAtomicType.VALOBJ, // //$NON-NLS-1$
      TSID_DESCRIPTION, STR_D_DATA_NAME_ALIASES, //
      TSID_NAME, STR_N_DATA_NAME_ALIASES, //
      TSID_KEEPER_ID, DataNameAliasesList.KEEPER_ID, //
      TSID_DEFAULT_VALUE, avValobj( new DataNameAliasesList() ), //
      OPID_TREE_PATH1, MCC_SYSTEM_OPT_GROUP_PATH );

  /**
   * Все опции в виде списка
   *
   * @return IStridablesListEdit&lt;IDataDef> - все опции в виде списка
   */
  public static IStridablesList<IDataDef> allOptions() {
    IStridablesListEdit<IDataDef> options = new StridablesList<>();
    options.add( DATA_NAME_ALIASES );
    return options;
  }

  /**
   * Возвращает набор опций, инициализированный набором значений по-умолчанию
   *
   * @return IOptionSet - набор опций, инициализированный набором значений по-умолчанию
   */
  public static IOptionSet defaultOptions() {
    IOptionSetEdit opSet = new OptionSet();
    opSet.setValue( DATA_NAME_ALIASES, DATA_NAME_ALIASES.defaultValue() );

    return opSet;
  }

  // ------------------------------------------------------------------------------------
  // Внутренняя реализация
  //

  static String createId( String aShortId ) {
    return MccSystemOptions.class.getSimpleName() + "." + aShortId; //$NON-NLS-1$
  }

}
