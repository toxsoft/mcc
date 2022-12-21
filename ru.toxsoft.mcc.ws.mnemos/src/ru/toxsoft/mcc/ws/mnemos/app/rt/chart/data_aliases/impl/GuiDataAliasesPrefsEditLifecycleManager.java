package ru.toxsoft.mcc.ws.mnemos.app.rt.chart.data_aliases.impl;

import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.model.impl.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.gw.skid.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.uskat.ggprefs.lib.*;

import ru.toxsoft.mcc.ws.mnemos.app.rt.chart.data_aliases.*;

/**
 * Менеджер жц списка псевдонимов данных
 *
 * @author dima
 */
public class GuiDataAliasesPrefsEditLifecycleManager
    extends M5LifecycleManager<Skid, IGuiGwPrefsSection> {

  /**
   * Конструктор.
   *
   * @param aModel {@link IM5Model} - модель сущностей
   * @param aMaster {@link IGuiGwPrefsSection} - мастер-объект
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  public GuiDataAliasesPrefsEditLifecycleManager( IM5Model<Skid> aModel, IGuiGwPrefsSection aMaster ) {
    super( aModel, false, true, false, false, aMaster );
    TsNullArgumentRtException.checkNulls( aModel, aMaster );
  }

  @Override
  protected Skid doEdit( IM5Bunch<Skid> aValues ) {

    IStridablesList<IDataDef> optDefs = master().listOptionDefs( aValues.originalEntity() );

    IOptionSetEdit optValues = new OptionSet();

    for( IDataDef optDef : optDefs ) {
      // из всего набора опций нас вданном контексте интересуют только псевдонимы данных
      if( optDef.id().equals( MccSystemOptions.DATA_NAME_ALIASES.id() ) ) {
        // в пучке они лежат сразу списком
        IList<IDataNameAlias> daList = aValues.get( GuiDataAliasesPrefsEditModel.DATA_ALIASES_MODEL_ID );
        // оформляем их специальной сущностью которая нужна только для того чтобы быть контейнером псевдонимов и
        // удовлетворять методологии m5
        IDataNameAliasesList dataAliasesList = new DataNameAliasesList( daList );
        // устанавливаем новое значение этой опции
        MccSystemOptions.DATA_NAME_ALIASES.setValue( optValues, AvUtils.avValobj( dataAliasesList ) );
        // больше здесь делать нечего
        break;
      }
    }

    master().setOptions( aValues.originalEntity(), optValues );

    return aValues.originalEntity();
  }
}
