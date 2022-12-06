package ru.toxsoft.mcc.ws.core.templates.api;

import org.toxsoft.core.tslib.bricks.events.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.bricks.validator.impl.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.uskat.core.*;
import org.toxsoft.uskat.core.api.*;
import org.toxsoft.uskat.core.api.objserv.*;
import org.toxsoft.uskat.core.connection.*;

/**
 * The graph template managment service.
 *
 * @author dima
 */
public interface ISkGraphTemplateService
    extends ISkService {

  /**
   * Service identifier.
   */
  String SERVICE_ID = ISkHardConstants.SK_CORE_SERVICE_ID_PREFIX + ".GraphTemplate"; //$NON-NLS-1$

  /**
   * Returns the list of graph templates.
   *
   * @return {@link IList}&lt;{@link ISkGraphTemplate}&gt; - the list of graph templates
   */
  IList<ISkGraphTemplate> listGraphTemplates();

  /**
   * Finds graph template.
   *
   * @param aTempId String - ID of template {@link ISkObject#strid()}
   * @return {@link ISkReportTemplate} - found template or <code>null</code>
   */
  ISkGraphTemplate findGraphTemplate( String aTempId );

  /**
   * Creates new graph template.
   *
   * @param aDtoGraphTemplate {@link IDtoFullObject} - the template data
   * @return {@link ISkReportTemplate} - created/update template object
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException object DTO does not refers to {@link ISkGraphTemplate#CLASS_ID}
   * @throws TsValidationFailedRtException failed check of {@link ISkReportTemplateServiceValidator}
   */
  ISkGraphTemplate createGraphTemplate( IDtoFullObject aDtoGraphTemplate );

  /**
   * Edits an existing template.
   * <p>
   *
   * @param aDtoGraphTemplate {@link IDtoFullObject} - the template data
   * @return {@link ISkReportTemplate} - created/update template object
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException object DTO does not refers to {@link ISkGraphTemplate#CLASS_ID}
   * @throws TsValidationFailedRtException failed check of {@link ISkGraphTemplateServiceValidator}
   */
  ISkGraphTemplate editGraphTemplate( IDtoFullObject aDtoGraphTemplate );

  /**
   * Removes the template.
   *
   * @param aGraphTemplateId String - template ID and {@link ISkObject#strid()}
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsValidationFailedRtException failed check of {@link ISkReportTemplateServiceValidator}
   */
  void removeGraphTemplate( String aGraphTemplateId );

  /**
   * Set current connection.
   *
   * @param aConnection {@link ISkConnection} - connection info
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  void setConnection( ISkConnection aConnection );

  // ------------------------------------------------------------------------------------
  // Service support

  /**
   * Returns the service validator.
   *
   * @return {@link ITsValidationSupport}&lt;{@link ISkGraphTemplateServiceValidator}&gt; - the service validator
   */
  ITsValidationSupport<ISkGraphTemplateServiceValidator> svs();

  /**
   * Returns the service eventer.
   *
   * @return {@link ITsEventer}&lt;{@link ISkGraphTemplateServiceListener}&gt; - the service eventer
   */
  ITsEventer<ISkGraphTemplateServiceListener> eventer();

}
