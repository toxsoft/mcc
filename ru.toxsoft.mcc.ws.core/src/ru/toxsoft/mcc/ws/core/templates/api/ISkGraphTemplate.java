package ru.toxsoft.mcc.ws.core.templates.api;

/**
 * Interface to specify template of graph.
 *
 * @author dima
 */

public interface ISkGraphTemplate
    extends ISkBaseTemplate<ISkGraphParam> {

  /**
   * The {@link ISkGraphTemplate} class identifier.
   */
  String CLASS_ID = ISkTemplateEditorServiceHardConstants.CLSID_GRAPH_TEMPLATE;

}