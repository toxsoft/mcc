package ru.toxsoft.mcc.ws.mnemos.app.utils;

import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.gw.gwid.*;
import org.toxsoft.core.tslib.gw.skid.*;
import org.toxsoft.skf.ggprefs.lib.*;
import org.toxsoft.skf.ggprefs.lib.impl.*;
import org.toxsoft.uskat.core.*;
import org.toxsoft.uskat.core.backend.api.*;
import org.toxsoft.uskat.core.connection.*;
import org.toxsoft.uskat.s5.server.*;

import ru.toxsoft.mcc.ws.mnemos.app.rt.chart.*;
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

  private final Skid systemSkid;

  private final Gwid systemGwid;

  private IGuiGwPrefsSection prefSection;

  private IDataNameAliasesList aliasesList;

  /**
   * Конструктор.
   *
   * @param aSkConn ISkConnection - соединение с сервером
   */
  public RtDataAliasHelper( ISkConnection aSkConn ) {
    ISkBackendInfo info = aSkConn.backendInfo();
    systemSkid = IS5ServerHardConstants.OP_SERVER_ID.getValue( info.params() ).asValobj();
    systemGwid = Gwid.createObj( systemSkid.classId(), systemSkid.strid() );

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
