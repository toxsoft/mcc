package ru.toxsoft.mcc.ws.core.templates.gui.m5;

import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.model.impl.*;
import org.toxsoft.core.tslib.coll.helpers.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.uskat.base.gui.km5.*;
import org.toxsoft.uskat.core.connection.*;

import ru.toxsoft.mcc.ws.core.templates.api.*;

/**
 * Contributes M5-models for template entities.
 *
 * @author dima
 */
public class KM5TemplateContributor
    extends KM5AbstractContributor {

  /**
   * Creator singleton.
   */
  public static final IKM5ContributorCreator CREATOR = KM5TemplateContributor::new;

  private static final IStringList CONRTIBUTED_MODEL_IDS = new StringArrayList( //
      SkReportParamM5Model.MODEL_ID, //
      ISkReportTemplate.CLASS_ID, //
      ISkTemplateEditorServiceHardConstants.GRAPH_PARAM_MODEL_ID, //
      ISkGraphTemplate.CLASS_ID //
  );

  private final IStringListEdit myModels = new StringArrayList();

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
    SkReportTemplateM5Model m5ReportTemplateModel = new SkReportTemplateM5Model( skConn() );
    myModels.add( m5ReportTemplateModel.id() );
    m5().addModel( m5ReportTemplateModel );
    m5().addModel( new SkGraphParamM5Model() );
    SkGraphTemplateM5Model m5GraphTemplateModel = new SkGraphTemplateM5Model( skConn() );
    myModels.add( m5GraphTemplateModel.id() );
    m5().addModel( m5GraphTemplateModel );

    return CONRTIBUTED_MODEL_IDS;
  }

  @Override
  protected boolean papiUpdateModel( ECrudOp aOp, String aClassId ) {
    switch( aOp ) {
      case CREATE:
      case EDIT: {
        if( isMine( aClassId ) ) {
          m5().replaceModel( mineModel( aClassId ) );
        }
        break;
      }
      case REMOVE: {
        m5().removeModel( aClassId );
        break;
      }
      case LIST:
        throw new TsInternalErrorRtException();
      default:
        throw new TsNotAllEnumsUsedRtException();
    }
    return true;
  }

  private M5Model<?> mineModel( String aClassId ) {
    M5Model<?> retVal = new SkReportTemplateM5Model( skConn() );
    switch( aClassId ) {
      case ISkReportTemplate.CLASS_ID: {
        break;
      }
      case ISkGraphTemplate.CLASS_ID: {
        retVal = new SkGraphTemplateM5Model( skConn() );
        break;
      }
      default:
        throw new TsNotAllEnumsUsedRtException( aClassId );
    }
    return retVal;
  }

  private boolean isMine( String aClassId ) {
    return myModels.hasElem( aClassId );
  }

}