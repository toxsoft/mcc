package ru.toxsoft.mcc.ws.mnemos.app.utils;

import org.toxsoft.core.tslib.av.metainfo.IDataDef;
import org.toxsoft.core.tslib.av.opset.IOptionSet;
import org.toxsoft.core.tslib.bricks.strid.coll.IStridablesListEdit;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.StridablesList;
import org.toxsoft.core.tslib.coll.IList;
import org.toxsoft.core.tslib.gw.gwid.Gwid;
import org.toxsoft.core.tslib.gw.skid.Skid;
import org.toxsoft.skf.ggprefs.lib.IGuiGwPrefsSection;
import org.toxsoft.skf.ggprefs.lib.ISkGuiGwPrefsService;
import org.toxsoft.skf.ggprefs.lib.impl.SkGuiGwPrefsService;
import org.toxsoft.uskat.core.ISkCoreApi;
import org.toxsoft.uskat.core.connection.ISkConnection;
import org.toxsoft.uskat.s5.legacy.ISkSystem;

import ru.toxsoft.mcc.ws.mnemos.app.rt.chart.PrefUtils;
import ru.toxsoft.mcc.ws.mnemos.app.rt.chart.data_aliases.*;

/**
 * Вспомогательный класс получения псевдонимов RtData по Gwid'у.
 * <p>
 *
 * @author vs
 */
public class RtDataAliasHelper {

  /**
   * ИД раздела настроек {@link IGuiGwPrefsSection} для работы с настройками мнемосхемы.
   */
  public static final String MNEMOS_PREFS_SECTION_ID = "MccMnemosSection"; //$NON-NLS-1$

  private final ISkConnection skConn;

  private final Skid systemSkid = new Skid( ISkSystem.CLASS_ID, ISkSystem.THIS_SYSTEM );

  private final Gwid systemGwid = Gwid.createObj( ISkSystem.CLASS_ID, ISkSystem.THIS_SYSTEM );

  private IGuiGwPrefsSection prefSection;

  private IDataNameAliasesList aliasesList;

  /**
   * Конструктор.
   *
   * @param aSkConn ISkConnection - соединение с сервером
   */
  public RtDataAliasHelper( ISkConnection aSkConn ) {
    skConn = aSkConn;
    initMnemosPrefs();
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Возвращает псевдоним для конретного ИДа РВ-данного.
   *
   * @param aGwid Gwid - конкретный ИД РВ-данного
   * @return IDataNameAlias - псевдоним для конретного ИДа РВ-данного
   */
  public IDataNameAlias alias( Gwid aGwid ) {
    for( IDataNameAlias alias : aliasesList.items() ) {
      if( alias.gwid().equals( aGwid ) ) {
        return alias;
      }
    }
    return null;
  }

  // ------------------------------------------------------------------------------------
  // Implementation
  //

  private void initMnemosPrefs() {
    ISkCoreApi coreApi = skConn.coreApi();

    if( !coreApi.services().hasKey( ISkGuiGwPrefsService.SERVICE_ID ) ) {
      coreApi.addService( SkGuiGwPrefsService.CREATOR );
    }
    prefSection = PrefUtils.section( MNEMOS_PREFS_SECTION_ID, skConn );

    // Задание опций
    IStridablesListEdit<IDataDef> panelPrefs = new StridablesList<>();
    panelPrefs.addAll( MccSystemOptions.allOptions() );

    IList<IDataDef> currOpDefs = prefSection.listOptionDefs( systemSkid );
    IStridablesListEdit<IDataDef> newPrefDefs = new StridablesList<>();
    // перебираем все устанавливаемые опции и добавляем только новые
    for( IDataDef addingOptDef : MccSystemOptions.allOptions() ) {
      if( !PrefUtils.hasOptionDef( currOpDefs, addingOptDef ) ) {
        newPrefDefs.add( addingOptDef );
      }
    }
    prefSection.bindOptions( systemGwid, newPrefDefs );

    IOptionSet systemPrefs = prefSection.getOptions( systemSkid );
    aliasesList = MccSystemOptions.DATA_NAME_ALIASES.getValue( systemPrefs ).asValobj();
  }

}
