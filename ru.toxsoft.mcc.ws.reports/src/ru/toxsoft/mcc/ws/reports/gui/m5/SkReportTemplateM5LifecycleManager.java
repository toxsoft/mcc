package ru.toxsoft.mcc.ws.reports.gui.m5;

import static org.toxsoft.uskat.core.ISkHardConstants.*;
import static ru.toxsoft.mcc.ws.core.templates.api.ISkTemplateEditorServiceHardConstants.*;

import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tslib.bricks.strid.idgen.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.gw.skid.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.uskat.base.gui.km5.*;
import org.toxsoft.uskat.core.api.objserv.*;
import org.toxsoft.uskat.core.api.users.*;
import org.toxsoft.uskat.core.connection.*;
import org.toxsoft.uskat.core.impl.dto.*;
import org.toxsoft.uskat.s5.utils.*;

import ru.toxsoft.mcc.ws.core.templates.api.*;
import ru.toxsoft.mcc.ws.core.templates.api.impl.*;

/**
 * Lifecycle manager for {@link SkReportTemplateM5Model}.
 *
 * @author dima
 */
public class SkReportTemplateM5LifecycleManager
    extends KM5LifecycleManagerBasic<ISkReportTemplate, ISkConnection> {

  private static SimpleStridGenaretor stridGenerator = new SimpleStridGenaretor();

  /**
   * Constructor.
   *
   * @param aModel {@link IM5Model}&lt;T&gt; - the model
   * @param aMaster &lt;M&gt; - master object, may be <code>null</code>
   * @throws TsNullArgumentRtException model is <code>null</code>
   */
  public SkReportTemplateM5LifecycleManager( IM5Model<ISkReportTemplate> aModel, ISkConnection aMaster ) {
    super( aModel, true, true, true, true, aMaster );
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  private ISkReportTemplateService reportTemplateService() {
    return master().coreApi().getService( ISkReportTemplateService.SERVICE_ID );
  }

  // ------------------------------------------------------------------------------------
  // M5LifecycleManager
  //

  @Override
  protected ValidationResult doBeforeCreate( IM5Bunch<ISkReportTemplate> aValues ) {
    IDtoFullObject dtoReportTemplate = makeReportTemplateDto( aValues, master() );
    return reportTemplateService().svs().validator().canCreateReportTemplate( dtoReportTemplate );
  }

  @Override
  protected ISkReportTemplate doCreate( IM5Bunch<ISkReportTemplate> aValues ) {
    IDtoFullObject dtoReportTemplate = makeReportTemplateDto( aValues, master() );
    return reportTemplateService().createReportTemplate( dtoReportTemplate );
  }

  @Override
  protected ValidationResult doBeforeEdit( IM5Bunch<ISkReportTemplate> aValues ) {
    IDtoFullObject dtoReportTemplate = makeReportTemplateDto( aValues, master() );
    return reportTemplateService().svs().validator().canEditReportTemplate( dtoReportTemplate,
        aValues.originalEntity() );
  }

  @Override
  protected ISkReportTemplate doEdit( IM5Bunch<ISkReportTemplate> aValues ) {
    IDtoFullObject dtoReportTemplate = makeReportTemplateDto( aValues, master() );
    return reportTemplateService().editReportTemplate( dtoReportTemplate );
  }

  @Override
  protected ValidationResult doBeforeRemove( ISkReportTemplate aEntity ) {
    return reportTemplateService().svs().validator().canRemoveReportTemplate( aEntity.id() );
  }

  @Override
  protected void doRemove( ISkReportTemplate aEntity ) {
    reportTemplateService().removeReportTemplate( aEntity.id() );
  }

  @Override
  protected IList<ISkReportTemplate> doListEntities() {
    return reportTemplateService().listReportTemplates();
  }

  // ------------------------------------------------------------------------------------
  // Package API
  //

  static IDtoFullObject makeReportTemplateDto( IM5Bunch<ISkReportTemplate> aValues, ISkConnection aConnection ) {
    Skid skid = new Skid( ISkReportTemplate.CLASS_ID,
        aValues.originalEntity() == null ? stridGenerator.nextId() : aValues.originalEntity().id() );
    DtoFullObject dtoReportTemplate = DtoFullObject.createDtoFullObject( skid, aConnection.coreApi() );
    dtoReportTemplate.attrs().setValue( AID_NAME, aValues.getAsAv( AID_NAME ) );
    dtoReportTemplate.attrs().setValue( AID_DESCRIPTION, aValues.getAsAv( AID_DESCRIPTION ) );
    dtoReportTemplate.attrs().setValue( ATRID_HAS_SUMMARY, aValues.getAsAv( ATRID_HAS_SUMMARY ) );
    dtoReportTemplate.attrs().setValue( SkReportTemplateM5Model.FID_AGGR_STEP,
        aValues.getAsAv( SkReportTemplateM5Model.FID_AGGR_STEP ) );

    IList<ISkReportParam> paramsList = aValues.getAs( CLBID_TEMPLATE_PARAMS, IList.class );
    String paramsStr = SkReportParam.KEEPER.coll2str( paramsList );
    dtoReportTemplate.clobs().put( CLBID_TEMPLATE_PARAMS, paramsStr );
    // установим ему автора
    ISkUser currUser = S5ConnectionUtils.getConnectedUser( aConnection );
    dtoReportTemplate.links().map().put( LNKID_TEMPLATE_AUTHOR, new SkidList( currUser.skid() ) );

    return dtoReportTemplate;
  }

}
