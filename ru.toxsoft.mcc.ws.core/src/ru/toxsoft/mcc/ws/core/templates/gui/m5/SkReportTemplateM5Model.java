package ru.toxsoft.mcc.ws.core.templates.gui.m5;

import static org.toxsoft.core.tsgui.m5.IM5Constants.*;
import static org.toxsoft.core.tslib.av.EAtomicType.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;
import static ru.toxsoft.mcc.ws.core.templates.api.ISkTemplateEditorServiceHardConstants.*;
import static ru.toxsoft.mcc.ws.core.templates.gui.m5.ISkResources.*;

import org.toxsoft.core.tsgui.chart.api.*;
import org.toxsoft.core.tsgui.m5.model.*;
import org.toxsoft.core.tsgui.m5.model.impl.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.uskat.base.gui.km5.*;
import org.toxsoft.uskat.core.connection.*;

import ru.toxsoft.mcc.ws.core.templates.api.*;

/**
 * M5-model of {@link ISkReportTemplate}.
 *
 * @author dima
 */
public class SkReportTemplateM5Model
    extends KM5ModelBasic<ISkReportTemplate> {

  /**
   * Структура для описания поля типа списка параметров которые хранятся ВМЕСТЕ с сущностью. Ключевое отличие от связи с
   * объектам в том что по связи объекты хранятся отдельно от сущности.
   */
  public final IM5MultiModownFieldDef<ISkReportTemplate, ISkReportParam> REPORT_PARAMS =
      new M5MultiModownFieldDef<>( CLBID_TEMPLATE_PARAMS, SkReportParamM5Model.MODEL_ID ) {

        @Override
        protected void doInit() {
          setNameAndDescription( STR_N_TEMPLATE_PARAMS, STR_D_TEMPLATE_PARAMS );
          setFlags( M5FF_DETAIL );
        }

        protected IList<ISkReportParam> doGetFieldValue( ISkReportTemplate aEntity ) {
          return aEntity.listParams();
        }

      };

  /**
   * Flag existence summary zone
   */
  public final KM5AttributeFieldDef<ISkReportTemplate> HAS_SUMMARY =
      new KM5AttributeFieldDef<>( ATRID_HAS_SUMMARY, IAvMetaConstants.DDEF_TS_BOOL ) {

        @Override
        protected void doInit() {
          setNameAndDescription( STR_N_FDEF_HAS_SUMMARY, STR_D_FDEF_HAS_SUMMARY );
          setFlags( M5FF_COLUMN );
        }

        protected IAtomicValue doGetFieldValue( ISkReportTemplate aEntity ) {
          return AvUtils.avBool( aEntity.hasSummary() );
        }

      };

  /**
   * id field of aggregation step
   */
  public static final String FID_AGGR_STEP = "aggrStep"; //$NON-NLS-1$

  /**
   * Attribute {@link ISkReportTemplate#aggrStep() } step of aggregation for values
   */
  public M5AttributeFieldDef<ISkReportTemplate> AGGR_STEP = new M5AttributeFieldDef<>( FID_AGGR_STEP, VALOBJ, //
      TSID_NAME, STR_N_PARAM_AGGR_STEP, //
      TSID_DESCRIPTION, STR_D_PARAM_AGGR_STEP, //
      TSID_KEEPER_ID, ETimeUnit.KEEPER_ID, //
      TSID_DEFAULT_VALUE, avValobj( ETimeUnit.MIN01 ) ) {

    @Override
    protected void doInit() {
      setFlags( M5FF_COLUMN );
    }

    protected IAtomicValue doGetFieldValue( ISkReportTemplate aEntity ) {
      return avValobj( aEntity.aggrStep() );
    }

  };

  /**
   * Constructor.
   *
   * @param aConn {@link ISkConnection} - the connection
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public SkReportTemplateM5Model( ISkConnection aConn ) {
    super( ISkReportTemplate.CLASS_ID, ISkReportTemplate.class, aConn );
    setNameAndDescription( STR_N_REPORT_TEMPLATE, STR_D_REPORT_TEMPLATE );

    // add fields
    addFieldDefs( NAME, DESCRIPTION, AGGR_STEP, HAS_SUMMARY, REPORT_PARAMS );
    // panels creator
    // FIXME 4 debug use default
    // setPanelCreator( new M5DefaultPanelCreator<>() {
    //
    // protected IM5CollectionPanel<ISkReportTemplate> doCreateCollEditPanel( ITsGuiContext aContext,
    // IM5ItemsProvider<ISkReportTemplate> aItemsProvider,
    // IM5LifecycleManager<ISkReportTemplate> aLifecycleManager ) {
    // OPDEF_IS_SUPPORTS_TREE.setValue( aContext.params(), AV_TRUE );
    // OPDEF_IS_ACTIONS_CRUD.setValue( aContext.params(), AV_TRUE );
    // MultiPaneComponentModown<ISkReportTemplate> mpc =
    // new SkReportTemplateMpc( aContext, model(), aItemsProvider, aLifecycleManager );
    // return new M5CollectionPanelMpcModownWrapper<>( mpc, false );
    // }
    // } );
  }

  @Override
  protected IM5LifecycleManager<ISkReportTemplate> doCreateDefaultLifecycleManager() {
    return new SkReportTemplateM5LifecycleManager( this, skConn() );
  }

  @Override
  protected IM5LifecycleManager<ISkReportTemplate> doCreateLifecycleManager( Object aMaster ) {
    ISkConnection master = ISkConnection.class.cast( aMaster );
    return new SkReportTemplateM5LifecycleManager( this, master );
  }

}
