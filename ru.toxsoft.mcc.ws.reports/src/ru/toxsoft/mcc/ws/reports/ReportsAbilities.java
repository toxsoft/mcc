package ru.toxsoft.mcc.ws.reports;

import static org.toxsoft.core.tsgui.m5.IM5Constants.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;
import static ru.toxsoft.mcc.ws.reports.ISkResources.*;

import org.toxsoft.core.tslib.bricks.strid.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.bricks.strid.impl.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.uskat.onews.lib.*;

/**
 * Константы (возможности) функционала генератора отчетов и графиков, управляемый службой {@link ISkOneWsService}.
 *
 * @author dima
 */
public class ReportsAbilities {

  // ------------------------------------------------------------------------------------
  // Типы допусков специфичные для данного функционала (плагина)

  /**
   * TODO сделано для примера, переименовать при появлении реальной сущности
   */
  public static final String REPORT_TEMPL_XXX_ABILITY_KIND_ID = "REPORT_TEMPL_XXX_ABILITY_KIND_ID"; //$NON-NLS-1$

  static IStridableParameterized REPORT_TEMPL_XXX_ABILITY_KIND =
      StridableParameterized.create( REPORT_TEMPL_XXX_ABILITY_KIND_ID, //
          TSID_NAME, STR_N_REPORT_TEMPL_XXX_ABILITY_KIND, //
          TSID_DESCRIPTION, STR_D_REPORT_TEMPL_XXX_ABILITY_KIND//
      );

  // ------------------------------------------------------------------------------------
  // id конкретных прав доступа специфичные для данного функционала (плагина)

  /**
   * id права на возможность YYY
   */
  static final String REPORT_TEMPL_YYY_ABILITY_ID = "report.templ.YYY.ability.id"; //$NON-NLS-1$

  /**
   * возможность видеть данную перспективу 'Шаблоны отчетов/графиков'
   */
  public static final IOneWsAbility REPORT_TEMPL_YYY_ABILITY = DefaultOneWsAbility.create( REPORT_TEMPL_YYY_ABILITY_ID,
      REPORT_TEMPL_XXX_ABILITY_KIND_ID, FID_NAME, STR_N_REPORT_TEMPL_YYY_ABILITY );

  /**
   * @return возвращает список всех возможностей
   */
  public static IList<IOneWsAbility> listAbilities() {
    IListEdit<IOneWsAbility> retVal = new ElemArrayList<>();
    retVal.add( REPORT_TEMPL_YYY_ABILITY );
    return retVal;
  }

  /**
   * @return возвращает список всех типов возможностей
   */
  public static IStridablesList<IStridableParameterized> listAbilityKinds() {
    IStridablesList<IStridableParameterized> retVal = new StridablesList<>( //
        REPORT_TEMPL_XXX_ABILITY_KIND );
    return retVal;
  }

}
