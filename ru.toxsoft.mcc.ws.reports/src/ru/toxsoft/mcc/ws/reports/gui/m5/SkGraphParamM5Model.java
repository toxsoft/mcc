package ru.toxsoft.mcc.ws.reports.gui.m5;

import static org.toxsoft.core.tsgui.m5.IM5Constants.*;
import static org.toxsoft.core.tsgui.valed.api.IValedControlConstants.*;
import static org.toxsoft.core.tslib.av.EAtomicType.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;
import static ru.toxsoft.mcc.ws.reports.gui.m5.ISkResources.*;

import org.toxsoft.core.tsgui.chart.api.*;
import org.toxsoft.core.tsgui.graphics.colors.*;
import org.toxsoft.core.tsgui.m5.model.*;
import org.toxsoft.core.tsgui.m5.model.impl.*;
import org.toxsoft.core.tsgui.valed.controls.av.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.gw.gwid.*;
import org.toxsoft.uskat.base.gui.conn.*;
import org.toxsoft.uskat.core.connection.*;

import ru.toxsoft.mcc.ws.core.templates.api.*;
import ru.toxsoft.mcc.ws.reports.gui.valed.*;

/**
 * M5-model of {@link ISkGraphParam}.
 *
 * @author dima
 */
public class SkGraphParamM5Model
    extends M5Model<ISkGraphParam> {

  /**
   * model id
   */
  // public static final String MODEL_ID = "sk.GraphParam"; //$NON-NLS-1$
  /**
   * id field of Gwid
   */
  public static final String FID_GWID         = "gwid";          //$NON-NLS-1$
  /**
   * title of param
   */
  public static final String FID_TITLE        = "title";         //$NON-NLS-1$
  /**
   * description of param
   */
  public static final String FID_DESCR        = "descr";         //$NON-NLS-1$
  /**
   * unit id of param
   */
  public static final String FID_UNIT_ID      = "unitId";        //$NON-NLS-1$
  /**
   * unit name for
   */
  public static final String FID_UNIT_NAME    = "unitName";      //$NON-NLS-1$
  /**
   * id field of aggregation func
   */
  public static final String FID_AGGR_FUNC    = "aggrFunc";      //$NON-NLS-1$
  /**
   * id field of display format
   */
  public static final String FID_DISPL_FORMAT = "displayFormat"; //$NON-NLS-1$
  /**
   * id field of color
   */
  public static final String FID_COLOR        = "color";         //$NON-NLS-1$
  /**
   * id field of line width
   */
  public static final String FID_LINE_WIDTH   = "lineWidth";     //$NON-NLS-1$
  /**
   * id field of flag "draw ladder"
   */
  public static final String FID_IS_LADDER    = "isLadder";      //$NON-NLS-1$

  /**
   * Attribute {@link ISkGraphParam#gwid() } Green world ID
   */
  public M5AttributeFieldDef<ISkGraphParam> GWID = new M5AttributeFieldDef<>( FID_GWID, VALOBJ, //
      TSID_NAME, STR_N_PARAM_GWID, //
      TSID_DESCRIPTION, STR_D_PARAM_GWID, //
      TSID_KEEPER_ID, Gwid.KEEPER_ID, //
      OPID_EDITOR_FACTORY_NAME, ValedAvValobjGwidEditor.FACTORY_NAME //
  ) {

    protected IAtomicValue doGetFieldValue( ISkGraphParam aEntity ) {
      return avValobj( aEntity.gwid() );
    }

  };

  /**
   * Attribute {@link ISkGraphParam#aggrFunc() } function of aggregation for values
   */
  public M5AttributeFieldDef<ISkGraphParam> AGGR_FUNC = new M5AttributeFieldDef<>( FID_AGGR_FUNC, VALOBJ, //
      TSID_NAME, STR_N_PARAM_AGGR_FUNC, //
      TSID_DESCRIPTION, STR_D_PARAM_AGGR_FUNC, //
      TSID_KEEPER_ID, EAggregationFunc.KEEPER_ID, //
      TSID_DEFAULT_VALUE, avValobj( EAggregationFunc.AVERAGE ) ) {

    protected IAtomicValue doGetFieldValue( ISkGraphParam aEntity ) {
      return avValobj( aEntity.aggrFunc() );
    }

  };

  /**
   * Attribute {@link ISkGraphParam#displayFormat() } values display format
   */
  public M5AttributeFieldDef<ISkGraphParam> DISPL_FORMAT = new M5AttributeFieldDef<>( FID_DISPL_FORMAT, VALOBJ, //
      TSID_NAME, STR_N_PARAM_DISPLAY_FORMAT, //
      TSID_DESCRIPTION, STR_D_PARAM_DISPLAY_FORMAT, //
      TSID_KEEPER_ID, EDisplayFormat.KEEPER_ID, //
      TSID_DEFAULT_VALUE, avValobj( EDisplayFormat.TWO_DIGIT ) ) {

    protected IAtomicValue doGetFieldValue( ISkGraphParam aEntity ) {
      return avValobj( aEntity.displayFormat() );
    }

  };

  /**
   * Attribute {@link ISkGraphParam#title() } title of parameter
   */
  public M5AttributeFieldDef<ISkGraphParam> TITLE = new M5AttributeFieldDef<>( FID_TITLE, EAtomicType.STRING, //
      TSID_NAME, STR_N_PARAM_TITLE, //
      TSID_DESCRIPTION, STR_D_PARAM_TITLE, //
      OPID_EDITOR_FACTORY_NAME, ValedAvStringText.FACTORY_NAME //
  ) {

    @Override
    protected void doInit() {
      setFlags( M5FF_COLUMN );
    }

    protected IAtomicValue doGetFieldValue( ISkGraphParam aEntity ) {
      return avStr( aEntity.title() );
    }

  };

  /**
   * Attribute {@link ISkGraphParam#description() } description of parameter
   */
  public M5AttributeFieldDef<ISkGraphParam> DESCR = new M5AttributeFieldDef<>( FID_DESCR, EAtomicType.STRING, //
      TSID_NAME, STR_N_PARAM_DESCRIPTION, //
      TSID_DESCRIPTION, STR_D_PARAM_DESCRIPTION, //
      OPID_EDITOR_FACTORY_NAME, ValedAvStringText.FACTORY_NAME //
  ) {

    @Override
    protected void doInit() {
      setFlags( M5FF_COLUMN );
    }

    protected IAtomicValue doGetFieldValue( ISkGraphParam aEntity ) {
      return avStr( aEntity.description() );
    }

  };

  /**
   * Attribute {@link ISkGraphParam#unitId() } id of unit
   */
  public M5AttributeFieldDef<ISkGraphParam> UNIT_ID = new M5AttributeFieldDef<>( FID_UNIT_ID, EAtomicType.STRING, //
      TSID_NAME, STR_N_PARAM_UNIT_ID, //
      TSID_DESCRIPTION, STR_D_PARAM_UNIT_ID, //
      OPID_EDITOR_FACTORY_NAME, ValedAvStringText.FACTORY_NAME, //
      TSID_IS_NULL_ALLOWED, AV_FALSE, //
      TSID_DEFAULT_VALUE, AvUtils.avStr( "T" ) // оставлено для примера //$NON-NLS-1$
  ) {

    @Override
    protected void doInit() {
      setFlags( M5FF_COLUMN );
    }

    protected IAtomicValue doGetFieldValue( ISkGraphParam aEntity ) {
      return avStr( aEntity.unitId() );
    }

  };

  /**
   * Attribute {@link ISkGraphParam#unitName() } name of unit
   */
  public M5AttributeFieldDef<ISkGraphParam> UNIT_NAME = new M5AttributeFieldDef<>( FID_UNIT_NAME, EAtomicType.STRING, //
      TSID_NAME, STR_N_PARAM_UNIT_NAME, //
      TSID_DESCRIPTION, STR_D_PARAM_UNIT_NAME, //
      OPID_EDITOR_FACTORY_NAME, ValedAvStringText.FACTORY_NAME, //
      TSID_IS_NULL_ALLOWED, AV_FALSE, //
      TSID_DEFAULT_VALUE, AvUtils.avStr( "°С" ) // оставлено для примера //$NON-NLS-1$
  ) {

    @Override
    protected void doInit() {
      setFlags( M5FF_COLUMN );
    }

    protected IAtomicValue doGetFieldValue( ISkGraphParam aEntity ) {
      return avStr( aEntity.unitName() );
    }

  };

  /**
   * Attribute {@link ISkGraphParam#color() } description of parameter
   */
  public M5AttributeFieldDef<ISkGraphParam> COLOR = new M5AttributeFieldDef<>( FID_COLOR, VALOBJ, //
      TSID_NAME, STR_N_PARAM_COLOR, //
      TSID_DESCRIPTION, STR_D_PARAM_COLOR, //
      TSID_KEEPER_ID, ETsColor.KEEPER_ID, //
      TSID_DEFAULT_VALUE, avValobj( ETsColor.BLACK ) ) {

    @Override
    protected void doInit() {
      setFlags( M5FF_COLUMN );
    }

    protected IAtomicValue doGetFieldValue( ISkGraphParam aEntity ) {
      return avValobj( aEntity.color() );
    }

  };

  /**
   * Attribute {@link ISkGraphParam#lineWidth() } description of parameter
   */
  public M5AttributeFieldDef<ISkGraphParam> LINE_WIDTH = new M5AttributeFieldDef<>( FID_LINE_WIDTH, EAtomicType.INTEGER, //
      TSID_NAME, STR_N_PARAM_LINE_WIDTH, //
      TSID_DESCRIPTION, STR_D_PARAM_LINE_WIDTH, //
      OPID_EDITOR_FACTORY_NAME, ValedAvIntegerSpinner.FACTORY_NAME, //
      TSID_DEFAULT_VALUE, avInt( 3 ) //
  ) {

    @Override
    protected void doInit() {
      setFlags( M5FF_COLUMN );
    }

    protected IAtomicValue doGetFieldValue( ISkGraphParam aEntity ) {
      return avInt( aEntity.lineWidth() );
    }

  };

  /**
   * Attribute {@link ISkGraphParam#isLadder() } description of parameter
   */
  public M5AttributeFieldDef<ISkGraphParam> IS_LADDER = new M5AttributeFieldDef<>( FID_IS_LADDER, EAtomicType.BOOLEAN, //
      TSID_NAME, STR_N_IS_LADDER, //
      TSID_DESCRIPTION, STR_D_IS_LADDER, //
      TSID_DEFAULT_VALUE, avBool( false ) //

  ) {

    @Override
    protected void doInit() {
      setFlags( M5FF_COLUMN );
    }

    protected IAtomicValue doGetFieldValue( ISkGraphParam aEntity ) {
      return avBool( aEntity.isLadder() );
    }

  };

  /**
   * Constructor
   */
  public SkGraphParamM5Model() {
    super( ISkTemplateEditorServiceHardConstants.GRAPH_PARAM_MODEL_ID, ISkGraphParam.class );

    addFieldDefs( GWID, TITLE, DESCR, UNIT_ID, UNIT_NAME, AGGR_FUNC, DISPL_FORMAT, COLOR, LINE_WIDTH, IS_LADDER );

  }

  @Override
  protected IM5LifecycleManager<ISkGraphParam> doCreateDefaultLifecycleManager() {
    ISkConnectionSupplier cs = tsContext().get( ISkConnectionSupplier.class );
    // TODO which connection to use?
    ISkConnection conn = cs.defConn();
    return new SkGraphParamM5LifecycleManager( this, conn );
  }

  @Override
  protected IM5LifecycleManager<ISkGraphParam> doCreateLifecycleManager( Object aMaster ) {
    return new SkGraphParamM5LifecycleManager( this, ISkConnection.class.cast( aMaster ) );
  }

}
