package ru.toxsoft.mcc.ws.core.templates.api;

import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;
import static ru.toxsoft.mcc.ws.core.templates.api.ISkResources.*;

import org.toxsoft.core.tsgui.chart.api.ETimeUnit;
import org.toxsoft.core.tslib.av.impl.AvUtils;
import org.toxsoft.core.tslib.coll.helpers.CollConstraint;
import org.toxsoft.core.tslib.coll.primtypes.impl.SingleStringList;
import org.toxsoft.uskat.core.ISkHardConstants;
import org.toxsoft.uskat.core.api.sysdescr.dto.*;
import org.toxsoft.uskat.core.api.users.ISkUser;
import org.toxsoft.uskat.core.impl.dto.*;

/**
 * Unchangeable constants of the template editor service.
 *
 * @author dima
 */
public interface ISkTemplateEditorServiceHardConstants {

  // ------------------------------------------------------------------------------------
  // ISkBaseTemplate

  /**
   * ID of link {@link #LNKID_TEMPLATE_AUTHOR }.
   */
  String LNKID_TEMPLATE_AUTHOR = "author"; //$NON-NLS-1$

  /**
   * ID of CLOB {@link #CLBID_TEMPLATE_PARAMS}.
   */
  String CLBID_TEMPLATE_PARAMS = "templateParams"; //$NON-NLS-1$

  /**
   * CLOB {@link ISkBaseTemplate#listParams()}.
   */
  IDtoClobInfo CLBINF_TEMPLATE_PARAMS = DtoClobInfo.create2( CLBID_TEMPLATE_PARAMS, //
      TSID_NAME, STR_N_CLB_TEMPLATE_PARAMS, //
      TSID_DESCRIPTION, STR_D_CLB_TEMPLATE_PARAMS //
  );

  // ------------------------------------------------------------------------------------
  // ISkReportTemplate

  /**
   * Report template class ID.
   */
  String CLSID_REPORT_TEMPLATE = ISkHardConstants.SK_ID + ".ReportTemplate"; //$NON-NLS-1$

  /**
   * ID of attribute {@link ISkReportTemplate#title()}.
   */
  String ATRID_TITLE = "title"; //$NON-NLS-1$

  /**
   * Attribute {@link ISkReportTemplate#title()}.
   */
  IDtoAttrInfo ATRINF_TITLE = DtoAttrInfo.create2( ATRID_TITLE, DDEF_STRING, //
      TSID_NAME, STR_N_TITLE, //
      TSID_DESCRIPTION, STR_D_TITLE );

  /**
   * ID of attribute {@link ISkReportTemplate#hasSummary()}.
   */
  String ATRID_HAS_SUMMARY = "hasSummary"; //$NON-NLS-1$

  /**
   * Attribute {@link ISkReportTemplate#hasSummary()}.
   */
  IDtoAttrInfo ATRINF_HAS_SUMMARY = DtoAttrInfo.create2( ATRID_HAS_SUMMARY, DDEF_BOOLEAN, //
      TSID_NAME, STR_N_HAS_SUMMARY, //
      TSID_DESCRIPTION, STR_D_HAS_SUMMARY );

  // ------------------------------------------------------------------------------------
  // ISkGraphTemplate

  /**
   * Report template class ID.
   */
  String CLSID_GRAPH_TEMPLATE = ISkHardConstants.SK_ID + ".GraphTemplate"; //$NON-NLS-1$

  /**
   * Link {@link ISkGraphTemplate#author()}.
   */
  IDtoLinkInfo LNKINF_TEMPLATE_AUTHOR = DtoLinkInfo.create2( LNKID_TEMPLATE_AUTHOR, //
      new SingleStringList( ISkUser.CLASS_ID ), new CollConstraint( 1, true, true, true ), //
      TSID_NAME, STR_N_TEMPLATE_AUTHOR, //
      TSID_DESCRIPTION, STR_D_TEMPLATE_AUTHOR //
  );

  /**
   * ID of attribute {@link ISkGraphTemplate#aggrStep()}.
   */
  String ATRID_AGGR_STEP = "aggrStep"; //$NON-NLS-1$

  /**
   * Attribute {@link ISkGraphTemplate#aggrStep()}.
   */
  IDtoAttrInfo ATRINF_AGGR_STEP = DtoAttrInfo.create2( ATRID_AGGR_STEP, DDEF_VALOBJ, //
      TSID_NAME, STR_N_AGGR_STEP, //
      TSID_DESCRIPTION, STR_D_AGGR_STEP, //
      TSID_DEFAULT_VALUE, ETimeUnit.MIN01 );

  /**
   * ID of attribute {@link ISkGraphTemplate#maxExecutionTime()}.
   */
  String ATRID_MAX_EXECUTION_TIME = "maxExecutionTime"; //$NON-NLS-1$

  /**
   * Attribute {@link ISkGraphTemplate#maxExecutionTime()}.
   */
  IDtoAttrInfo ATRINF_MAX_EXECUTION_TIME = DtoAttrInfo.create2( ATRID_MAX_EXECUTION_TIME, DDEF_INTEGER, //
      TSID_NAME, STR_N_MAX_EXECUTION_TIME, //
      TSID_DESCRIPTION, STR_D_MAX_EXECUTION_TIME, //
      TSID_DEFAULT_VALUE, AvUtils.avInt( 10000 ) );

  /**
   * SkGraphParamM5Model model id
   */
  String GRAPH_PARAM_MODEL_ID = "sk.GraphParam"; //$NON-NLS-1$

}
