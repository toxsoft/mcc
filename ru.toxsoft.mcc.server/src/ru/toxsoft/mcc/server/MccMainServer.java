package ru.toxsoft.mcc.server;

import static ru.toxsoft.mcc.server.IMccServerHardConstants.*;

import org.toxsoft.core.tslib.av.EAtomicType;
import org.toxsoft.core.tslib.av.opset.IOptionSet;
import org.toxsoft.core.tslib.av.opset.IOptionSetEdit;
import org.toxsoft.core.tslib.av.opset.impl.OptionSet;
import org.toxsoft.core.tslib.bricks.strid.coll.IStridablesList;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.StridablesList;
import org.toxsoft.core.tslib.coll.IList;
import org.toxsoft.core.tslib.coll.IListEdit;
import org.toxsoft.core.tslib.coll.impl.ElemArrayList;
import org.toxsoft.core.tslib.gw.gwid.Gwid;
import org.toxsoft.skf.alarms.s5.addons.S5BaAlarmCreator;
import org.toxsoft.skf.dq.s5.addons.S5BaDataQualityCreator;
import org.toxsoft.uskat.s5.common.S5Module;
import org.toxsoft.uskat.s5.server.backend.addons.IS5BackendAddonCreator;
import org.toxsoft.uskat.s5.server.backend.supports.histdata.impl.sequences.S5HistdataSequencesUtils10;
import org.toxsoft.uskat.s5.server.sequences.IS5SequenceImplementation;
import org.toxsoft.uskat.s5.server.startup.*;

/**
 * Начальная, неизменяемая, проектно-зависимая конфигурация реализации бекенда mcc-сервера
 *
 * @author mvk
 */
public class MccMainServer
    extends S5InitialImplementation {

  private transient final IS5InitialImplementation uskatServer = new S5UskatServer();

  /**
   * Конструктор
   */
  public MccMainServer() {
    super( new S5Module( MCC_SERVER_ID, MCC_SERVER_NAME, MCC_SERVER_DESCR, MCC_SERVER_VERSION ) );
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
  protected IStridablesList<IS5BackendAddonCreator> doProjectSpecificBaCreators() {
    // Аддоны ситрол
    StridablesList<IS5BackendAddonCreator> retValue = new StridablesList<>( uskatServer.baCreators() );
    // Собственные аддоны
    retValue.addAll( //
        new S5BaDataQualityCreator(), //
        new S5BaAlarmCreator() //
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
