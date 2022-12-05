package ru.toxsoft.mcc.ws.reports.lib;

import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.uskat.core.api.objserv.*;

/**
 * {@link ISkGraphTemplateService} service validator.
 *
 * @author dima
 */
public interface ISkGraphTemplateServiceValidator {

  /**
   * Checks if template can be created.
   *
   * @param aTemplateDto {@link IDtoFullObject} - template data
   * @return {@link ValidationResult} - validation result
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  ValidationResult canCreateGraphTemplate( IDtoFullObject aTemplateDto );

  /**
   * Checks if template can be edited.
   *
   * @param aTemplateDto {@link IDtoFullObject} - template data including links
   * @param aOldTemplate {@link ISkReportTemplate} - current template data
   * @return {@link ValidationResult} - validation result
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  ValidationResult canEditGraphTemplate( IDtoFullObject aTemplateDto, ISkGraphTemplate aOldTemplate );

  /**
   * Checks if template may removed.
   *
   * @param aTemplateId String - template ID
   * @return {@link ValidationResult} - validation result
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  ValidationResult canRemoveGraphTemplate( String aTemplateId );

}
