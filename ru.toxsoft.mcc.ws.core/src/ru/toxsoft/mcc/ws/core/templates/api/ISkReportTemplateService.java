package ru.toxsoft.mcc.ws.core.templates.api;

import org.toxsoft.core.tslib.bricks.events.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.bricks.validator.impl.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.uskat.core.*;
import org.toxsoft.uskat.core.api.*;
import org.toxsoft.uskat.core.api.objserv.*;

/**
 * The report template managment service.
 *
 * @author dima
 */
public interface ISkReportTemplateService
    extends ISkService {

  /**
   * Service identifier.
   */
  String SERVICE_ID = ISkHardConstants.SK_CORE_SERVICE_ID_PREFIX + ".ReportTemplate"; //$NON-NLS-1$

  /**
   * Returns the list of report tenmplates.
   *
   * @return {@link IList}&lt;{@link ISkReportTemplate}&gt; - the list of report templates
   */
  IList<ISkReportTemplate> listReportTemplates();

  /**
   * Finds report template.
   *
   * @param aTempId String - ID of template {@link ISkObject#strid()}
   * @return {@link ISkReportTemplate} - found template or <code>null</code>
   */
  ISkReportTemplate findReportTemplate( String aTempId );

  /**
   * Creates new report template.
   *
   * @param aDtoReportTemplate {@link IDtoFullObject} - the template data
   * @return {@link ISkReportTemplate} - created/update template object
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException object DTO does not refers to {@link ISkReportTemplate#CLASS_ID}
   * @throws TsValidationFailedRtException failed check of {@link ISkReportTemplateServiceValidator}
   */
  ISkReportTemplate createReportTemplate( IDtoFullObject aDtoReportTemplate );

  /**
   * Edits an existing template.
   * <p>
   *
   * @param aDtoReportTemplate {@link IDtoFullObject} - the template data
   * @return {@link ISkReportTemplate} - created/update template object
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsValidationFailedRtException failed check of {@link ISkReportTemplateServiceValidator}
   */
  ISkReportTemplate editReportTemplate( IDtoFullObject aDtoReportTemplate );

  /**
   * Removes the template.
   *
   * @param aReportTemplateId String - template ID and {@link ISkObject#strid()}
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsValidationFailedRtException failed check of {@link ISkReportTemplateServiceValidator}
   */
  void removeReportTemplate( String aReportTemplateId );

  // ------------------------------------------------------------------------------------
  // Service support

  /**
   * Returns the service validator.
   *
   * @return {@link ITsValidationSupport}&lt;{@link ISkReportTemplateServiceValidator}&gt; - the service validator
   */
  ITsValidationSupport<ISkReportTemplateServiceValidator> svs();

  /**
   * Returns the service eventer.
   *
   * @return {@link ITsEventer}&lt;{@link ISkReportTemplateServiceListener}&gt; - the service eventer
   */
  ITsEventer<ISkReportTemplateServiceListener> eventer();

}
