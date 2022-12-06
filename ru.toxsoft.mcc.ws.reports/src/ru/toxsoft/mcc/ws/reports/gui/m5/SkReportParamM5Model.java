package ru.toxsoft.mcc.ws.reports.gui.m5;

import static org.toxsoft.core.tsgui.m5.IM5Constants.*;
import static org.toxsoft.core.tsgui.valed.api.IValedControlConstants.*;
import static org.toxsoft.core.tslib.av.EAtomicType.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;
import static org.toxsoft.core.tslib.utils.TsTestUtils.*;
import static ru.toxsoft.mcc.ws.reports.gui.m5.ISkResources.*;

import org.toxsoft.core.tsgui.chart.api.*;
import org.toxsoft.core.tsgui.m5.model.*;
import org.toxsoft.core.tsgui.m5.model.impl.*;
import org.toxsoft.core.tsgui.valed.controls.av.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.gw.gwid.*;
import org.toxsoft.uskat.base.gui.conn.*;
import org.toxsoft.uskat.core.connection.*;

import ru.toxsoft.mcc.ws.core.templates.api.*;
import ru.toxsoft.mcc.ws.reports.gui.valed.*;

/**
 * M5-model of {@link ISkReportParam}.
 *
 * @author dima
 */
public class SkReportParamM5Model
    extends M5Model<ISkReportParam> {

  /**
   * model id
   */
  public static final String MODEL_ID         = "sk.ReportParam"; //$NON-NLS-1$
  /**
   * id field of Gwid
   */
  public static final String FID_GWID         = "gwid";           //$NON-NLS-1$
  /**
   * title of param
   */
  public static final String FID_TITLE        = "title";          //$NON-NLS-1$
  /**
   * description of param
   */
  public static final String FID_DESCR        = "descr";          //$NON-NLS-1$
  /**
   * id field of aggregation func
   */
  public static final String FID_AGGR_FUNC    = "aggrFunc";       //$NON-NLS-1$
  /**
   * id field of display format
   */
  public static final String FID_DISPL_FORMAT = "displayFormat";  //$NON-NLS-1$

  /**
   * Attribute {@link ISkReportParam#gwid() } Green world ID
   */
  public M5AttributeFieldDef<ISkReportParam> GWID = new M5AttributeFieldDef<>( FID_GWID, VALOBJ, //
      TSID_NAME, STR_N_PARAM_GWID, //
      TSID_DESCRIPTION, STR_D_PARAM_GWID, //
      TSID_KEEPER_ID, Gwid.KEEPER_ID, //
      OPID_EDITOR_FACTORY_NAME, ValedAvValobjGwidEditor.FACTORY_NAME //
  ) {

    protected IAtomicValue doGetFieldValue( ISkReportParam aEntity ) {
      return avValobj( aEntity.gwid() );
    }

  };

  /**
   * Attribute {@link ISkReportParam#aggrFunc() } function of aggregation for values
   */
  public M5AttributeFieldDef<ISkReportParam> AGGR_FUNC = new M5AttributeFieldDef<>( FID_AGGR_FUNC, VALOBJ, //
      TSID_NAME, STR_N_PARAM_AGGR_FUNC, //
      TSID_DESCRIPTION, STR_D_PARAM_AGGR_FUNC, //
      TSID_KEEPER_ID, EAggregationFunc.KEEPER_ID, //
      TSID_DEFAULT_VALUE, avValobj( EAggregationFunc.AVERAGE ) ) {

    @Override
    protected void doInit() {
      setFlags( M5FF_COLUMN );
    }

    protected IAtomicValue doGetFieldValue( ISkReportParam aEntity ) {
      return avValobj( aEntity.aggrFunc() );
    }

  };

  /**
   * Attribute {@link ISkReportParam#displayFormat() } values display format
   */
  public M5AttributeFieldDef<ISkReportParam> DISPL_FORMAT = new M5AttributeFieldDef<>( FID_DISPL_FORMAT, VALOBJ, //
      TSID_NAME, STR_N_PARAM_DISPLAY_FORMAT, //
      TSID_DESCRIPTION, STR_D_PARAM_DISPLAY_FORMAT, //
      TSID_KEEPER_ID, EDisplayFormat.KEEPER_ID, //
      TSID_DEFAULT_VALUE, avValobj( EDisplayFormat.TWO_DIGIT ) ) {

    @Override
    protected void doInit() {
      setFlags( M5FF_COLUMN );
    }

    protected IAtomicValue doGetFieldValue( ISkReportParam aEntity ) {
      return avValobj( aEntity.displayFormat() );
    }

  };

  /**
   * Attribute {@link ISkReportParam#title() } title of parameter
   */
  public M5AttributeFieldDef<ISkReportParam> TITLE = new M5AttributeFieldDef<>( FID_TITLE, EAtomicType.STRING, //
      TSID_NAME, STR_N_PARAM_TITLE, //
      TSID_DESCRIPTION, STR_D_PARAM_TITLE, //
      OPID_EDITOR_FACTORY_NAME, ValedAvStringText.FACTORY_NAME //
  ) {

    @Override
    protected void doInit() {
      setFlags( M5FF_COLUMN );
    }

    protected IAtomicValue doGetFieldValue( ISkReportParam aEntity ) {
      return avStr( aEntity.title() );
    }

  };

  /**
   * Attribute {@link ISkReportParam#description() } description of parameter
   */
  public M5AttributeFieldDef<ISkReportParam> DESCR = new M5AttributeFieldDef<>( FID_DESCR, EAtomicType.STRING, //
      TSID_NAME, STR_N_PARAM_DESCRIPTION, //
      TSID_DESCRIPTION, STR_D_PARAM_DESCRIPTION, //
      OPID_EDITOR_FACTORY_NAME, ValedAvStringText.FACTORY_NAME //
  ) {

    @Override
    protected void doInit() {
      setFlags( M5FF_COLUMN );
    }

    protected IAtomicValue doGetFieldValue( ISkReportParam aEntity ) {
      return avStr( aEntity.description() );
    }

  };

  /**
   * Constructor
   */
  public SkReportParamM5Model() {
    super( MODEL_ID, ISkReportParam.class );

    addFieldDefs( GWID, TITLE, DESCR, AGGR_FUNC, DISPL_FORMAT );

  }

  @Override
  protected IM5LifecycleManager<ISkReportParam> doCreateDefaultLifecycleManager() {
    ISkConnectionSupplier cs = tsContext().get( ISkConnectionSupplier.class );
    // TODO which connection to use?
    ISkConnection conn = cs.defConn();
    return new SkReportParamM5LifecycleManager( this, conn );
  }

  @Override
  protected IM5LifecycleManager<ISkReportParam> doCreateLifecycleManager( Object aMaster ) {
    return new SkReportParamM5LifecycleManager( this, ISkConnection.class.cast( aMaster ) );
  }

  @SuppressWarnings( { "javadoc", "nls" } )
  public static void main( String[] aArgs ) {
    // sample of Gwid: classId[strid]$rtdata(rtDataId)
    pl( "USkat test #1." );
    Gwid gwid = Gwid.createRtdata( "classId", "strid", "rtDataId" );
    System.out.print( gwid.asString() );
    nl();
  }

}
