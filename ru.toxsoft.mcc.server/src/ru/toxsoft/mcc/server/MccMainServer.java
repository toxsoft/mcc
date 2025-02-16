package ru.toxsoft.mcc.server;

import static ru.toxsoft.mcc.server.IMccServerHardConstants.*;

import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.gw.gwid.*;
import org.toxsoft.skf.dq.s5.addons.*;
import org.toxsoft.skf.legacy.alarms.s5.addons.*;
import org.toxsoft.uskat.s5.common.*;
import org.toxsoft.uskat.s5.server.backend.addons.*;
import org.toxsoft.uskat.s5.server.backend.supports.histdata.impl.sequences.*;
import org.toxsoft.uskat.s5.server.sequences.*;
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
    super( SERVER_ID, SERVER_ID, new S5Module( SERVER_ID, SERVER_NAME, SERVER_DESCR, SERVER_VERSION ) );
  }

  // ------------------------------------------------------------------------------------
  // S5InitialImplementation
  //
  @Override
  protected IOptionSet doProjectSpecificParams() {
    // Параметры бекенда
    IOptionSetEdit retValue = new OptionSet();
    // // Схема базы данных сервера
    // retValue.setStr( OP_BACKEND_DB_SCHEME_NAME, DB_SCHEME_NAME );
    // // Глубина хранения исторических данных
    // retValue.setInt( IS5ServerHardConstants.OP_DB_STORAGE_DEPTH, DB_STORAGE_DEPTH );

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
