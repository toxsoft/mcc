package ru.toxsoft.mcc.ws.core.templates.gui.m5;

import static org.toxsoft.core.tsgui.m5.IM5Constants.*;
import static org.toxsoft.core.tslib.av.EAtomicType.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;
import static ru.toxsoft.mcc.ws.core.templates.api.ISkTemplateEditorServiceHardConstants.*;
import static ru.toxsoft.mcc.ws.core.templates.gui.m5.ISkResources.*;

import org.toxsoft.core.tsgui.chart.api.ETimeUnit;
import org.toxsoft.core.tsgui.m5.model.IM5LifecycleManager;
import org.toxsoft.core.tsgui.m5.model.IM5MultiModownFieldDef;
import org.toxsoft.core.tsgui.m5.model.impl.M5AttributeFieldDef;
import org.toxsoft.core.tsgui.m5.model.impl.M5MultiModownFieldDef;
import org.toxsoft.core.tslib.av.IAtomicValue;
import org.toxsoft.core.tslib.coll.IList;
import org.toxsoft.core.tslib.utils.errors.TsNullArgumentRtException;
import org.toxsoft.uskat.base.gui.km5.KM5ModelBasic;
import org.toxsoft.uskat.core.connection.ISkConnection;

import ru.toxsoft.mcc.ws.core.templates.api.*;

/**
 * M5-model of {@link ISkGraphTemplate}.
 *
 * @author dima
 */
public class SkGraphTemplateM5Model
    extends KM5ModelBasic<ISkGraphTemplate> {

  /**
   * Структура для описания поля типа списка параметров которые хранятся ВМЕСТЕ с сущностью. Ключевое отличие от связи с
   * объектам в том что по связи объекты хранятся отдельно от сущности.
   */
  public final IM5MultiModownFieldDef<ISkGraphTemplate, ISkGraphParam> REPORT_PARAMS =
      new M5MultiModownFieldDef<>( CLBID_TEMPLATE_PARAMS, ISkTemplateEditorServiceHardConstants.GRAPH_PARAM_MODEL_ID ) {

        @Override
        protected void doInit() {
          setNameAndDescription( STR_N_TEMPLATE_PARAMS, STR_D_TEMPLATE_PARAMS );
          setFlags( M5FF_DETAIL );
        }

        protected IList<ISkGraphParam> doGetFieldValue( ISkGraphTemplate aEntity ) {
          return aEntity.listParams();
        }

      };

  /**
   * Attribute {@link ISkGraphTemplate#aggrStep() } step of aggregation for values
   */
  public M5AttributeFieldDef<ISkGraphTemplate> AGGR_STEP = new M5AttributeFieldDef<>( ATRID_AGGR_STEP, VALOBJ, //
      TSID_NAME, STR_N_PARAM_AGGR_STEP, //
      TSID_DESCRIPTION, STR_D_PARAM_AGGR_STEP, //
      TSID_KEEPER_ID, ETimeUnit.KEEPER_ID, //
      TSID_DEFAULT_VALUE, avValobj( ETimeUnit.MIN01 ) ) {

    @Override
    protected void doInit() {
      setFlags( M5FF_COLUMN );
    }

    protected IAtomicValue doGetFieldValue( ISkGraphTemplate aEntity ) {
      return avValobj( aEntity.aggrStep() );
    }

  };

  /**
   * Attribute {@link ISkGraphTemplate#maxExecutionTime() } query max execution time (msec)
   */
  public M5AttributeFieldDef<ISkGraphTemplate> MAX_EXECUTION_TIME =
      new M5AttributeFieldDef<>( ATRID_MAX_EXECUTION_TIME, INTEGER, //
          TSID_NAME, STR_N_PARAM_MAX_EXECUTION_TIME, //
          TSID_DESCRIPTION, STR_D_PARAM_MAX_EXECUTION_TIME, //
          TSID_DEFAULT_VALUE, avInt( 10000 ) ) {

        @Override
        protected void doInit() {
          setFlags( M5FF_COLUMN );
        }

        protected IAtomicValue doGetFieldValue( ISkGraphTemplate aEntity ) {
          return avInt( aEntity.maxExecutionTime() );
        }

      };

  /**
   * Constructor.
   *
   * @param aConn {@link ISkConnection} - the connection
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public SkGraphTemplateM5Model( ISkConnection aConn ) {
    super( ISkGraphTemplate.CLASS_ID, ISkGraphTemplate.class, aConn );
    setNameAndDescription( STR_N_GRAPH_TEMPLATE, STR_D_GRAPH_TEMPLATE );

    // add fields
    addFieldDefs( NAME, DESCRIPTION, AGGR_STEP, MAX_EXECUTION_TIME, REPORT_PARAMS );
  }

  @Override
  protected IM5LifecycleManager<ISkGraphTemplate> doCreateDefaultLifecycleManager() {
    return new SkGraphTemplateM5LifecycleManager( this, skConn() );
  }

  @Override
  protected IM5LifecycleManager<ISkGraphTemplate> doCreateLifecycleManager( Object aMaster ) {
    ISkConnection master = ISkConnection.class.cast( aMaster );
    return new SkGraphTemplateM5LifecycleManager( this, master );
  }

}
