package ru.toxsoft.mcc.server;

import static ru.toxsoft.mcc.server.IMccServerHardConstants.*;

import ru.toxsoft.tslib.datavalue.EAtomicType;
import ru.toxsoft.tslib.greenworld.gwid.Gwid;
import ru.toxsoft.tslib.patterns.opset.*;
import ru.toxsoft.tslib.strids.stridable.IStridablesList;
import ru.toxsoft.tslib.strids.stridable.impl.StridablesList;
import ru.toxsoft.tslib.utils.collections.IList;
import ru.toxsoft.tslib.utils.collections.IListEdit;
import ru.toxsoft.tslib.utils.collections.impl.ElemArrayList;
import ru.uskat.s5.client.remote.connection.options.impl.S5ModuleOptionValue;
import ru.uskat.s5.ext.dataquality.backend.addons.S5DataQualityAddon;
import ru.uskat.s5.ext.reports.backend.addons.S5ReportAddon;
import ru.uskat.s5.server.backend.addons.*;
import ru.uskat.s5.server.backend.addons.batch.S5BatchOperationsAddon;
import ru.uskat.s5.server.backend.addons.realtime.S5RealtimeAddon;
import ru.uskat.s5.server.backend.supports.histdata.sequences.S5HistdataSequencesUtils10;
import ru.uskat.s5.server.sequences.IS5SequenceImplementation;
import ru.uskat.s5.server.startup.S5InitialImplementation;
import ru.uskat.sysext.alarms.addon.SkAlarmAddon;

/**
 * Начальная, неизменяемая, проектно-зависимая конфигурация реализации бекенда mcc-сервера
 *
 * @author mvk
 */
public class MccMainServer
    extends S5InitialImplementation {

  /**
   * Конструктор
   */
  public MccMainServer() {
    super( new S5ModuleOptionValue( MCC_SERVER_ID, MCC_SERVER_NAME, MCC_SERVER_DESCR, MCC_SERVER_VERSION ) );
  }

  // ------------------------------------------------------------------------------------
  // S5InitialImplementation
  //
  @Override
  protected IOptionSet doProjectSpecificParams() {
    // Параметры sitrol
    IOptionSetEdit retValue = new OptionSet();
    // TODO: 2020-09-02 mvkd !!!
    // Запрет формирования хранимых данных (исторические данные, история команд и события)
    // retValue.setBool( OP_BACKEND_DATA_WRITE_DISABLE, true );
    return retValue;
  }

  @Override
  protected IStridablesList<IS5BackendAddon> doProjectSpecificAddons() {
    // Аддоны ситрол
    StridablesList<IS5BackendAddon> retValue = new StridablesList<>();
    // Собственные аддоны
    retValue.addAll( //
        new S5RegRefAddon(), //
        new S5RefbookAddon(), //
        new S5DataQualityAddon(), //
        new SkAlarmAddon(), //
        new S5OneWsAddon(), //
        new S5BatchOperationsAddon(), //
        new S5RealtimeAddon(), //
        new S5ReportAddon(), //
        new S5GuiGwPrefsAddon() //
    );
    return retValue;
  }

  @Override
  protected IOptionSet doProjectSpecificCreateClassParams( String aClassId ) {
    // Параметры sitrol
    IOptionSetEdit params = new OptionSet();
    // TODO: Собственные определения классов
    return params;
  }

  @Override
  protected IS5SequenceImplementation doFindHistDataImplementation( Gwid aGwid, EAtomicType aType, boolean aSync ) {
    return S5HistdataSequencesUtils10.findHistDataImplementation( aGwid, aType, aSync );
  }

  @Override
  protected IList<IS5SequenceImplementation> doGetHistDataImplementations() {
    // Таблицы хранения sitrol
    IListEdit<IS5SequenceImplementation> retValue = new ElemArrayList<>();
    // Собственные таблицы хранения (10 таблиц на тип)
    retValue.addAll( S5HistdataSequencesUtils10.getHistDataImplementations() );
    // Возвращаение полного списка таблиц хранения
    return retValue;
  }
}
