package ru.toxsoft.mcc.ws.core.templates.gui.m5;

import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.uskat.base.gui.km5.*;
import org.toxsoft.uskat.core.connection.*;
import org.toxsoft.uskat.core.impl.*;

import ru.toxsoft.mcc.ws.core.templates.api.*;

/**
 * Contributes M5-models for template entities.
 *
 * @author dima
 */
public class KM5TemplateContributor
    extends KM5AbstractContributor {

  private AbstractSkService                  reportTemplateService = null;
  private AbstractSkService                  graphTemplateService  = null;
  /**
   * Creator singleton.
   */
  public static final IKM5ContributorCreator CREATOR               = KM5TemplateContributor::new;

  private static final IStringList CONRTIBUTED_MODEL_IDS = new StringArrayList( //
      SkReportParamM5Model.MODEL_ID, //
      ISkReportTemplate.CLASS_ID, //
      ISkTemplateEditorServiceHardConstants.GRAPH_PARAM_MODEL_ID, //
      ISkGraphTemplate.CLASS_ID //
  );

  /**
   * Constructor.
   *
   * @param aConn {@link ISkConnection} - the connection
   * @param aDomain {@link IM5Domain} - connection domain
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public KM5TemplateContributor( ISkConnection aConn, IM5Domain aDomain ) {
    super( aConn, aDomain );
  }

  @Override
  protected IStringList papiCreateModels() {

    m5().addModel( new SkReportParamM5Model() );
    m5().addModel( new SkReportTemplateM5Model( skConn() ) );
    m5().addModel( new SkGraphParamM5Model() );
    m5().addModel( new SkGraphTemplateM5Model( skConn() ) );

    return CONRTIBUTED_MODEL_IDS;
  }

}
