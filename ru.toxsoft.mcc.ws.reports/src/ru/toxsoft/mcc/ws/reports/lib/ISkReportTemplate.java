package ru.toxsoft.mcc.ws.reports.lib;

/**
 * Interface to specify template of doc report.
 *
 * @author dima
 */

public interface ISkReportTemplate
    extends ISkBaseTemplate<ISkReportParam> {

  /**
   * The {@link ISkReportTemplate} class identifier.
   */
  String CLASS_ID = ISkTemplateEditorServiceHardConstants.CLSID_REPORT_TEMPLATE;

  /**
   * Determines if report has summary.
   *
   * @return boolean - has report summary<br>
   *         <code>true</code> report has summary<br>
   *         <code>false</code> report has no summary
   */
  default boolean hasSummary() {
    return attrs().getBool( ISkTemplateEditorServiceHardConstants.ATRID_HAS_SUMMARY );
  }

}
