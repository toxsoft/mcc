package ru.toxsoft.mcc.ws.reports.gui.m5;

import static org.toxsoft.uskat.core.ISkHardConstants.*;
import static ru.toxsoft.mcc.ws.reports.lib.ISkTemplateEditorServiceHardConstants.*;

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

import ru.toxsoft.mcc.ws.reports.lib.*;
import ru.toxsoft.mcc.ws.reports.lib.impl.*;

/**
 * Lifecycle manager for {@link SkGraphTemplateM5Model}.
 *
 * @author dima
 */
public class SkGraphTemplateM5LifecycleManager
    extends KM5LifecycleManagerBasic<ISkGraphTemplate, ISkConnection> {

  private static SimpleStridGenaretor stridGenerator = new SimpleStridGenaretor();

  /**
   * Constructor.
   *
   * @param aModel {@link IM5Model}&lt;T&gt; - the model
   * @param aMaster &lt;M&gt; - master object, may be <code>null</code>
   * @throws TsNullArgumentRtException model is <code>null</code>
   */
  public SkGraphTemplateM5LifecycleManager( IM5Model<ISkGraphTemplate> aModel, ISkConnection aMaster ) {
    super( aModel, true, true, true, true, aMaster );
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  private ISkGraphTemplateService graphTemplateService() {
    return master().coreApi().getService( ISkGraphTemplateService.SERVICE_ID );
  }

  // ------------------------------------------------------------------------------------
  // M5LifecycleManager
  //

  @Override
  protected ValidationResult doBeforeCreate( IM5Bunch<ISkGraphTemplate> aValues ) {
    IDtoFullObject dtoGraphTemplate = makeGraphTemplateDto( aValues, master() );
    return graphTemplateService().svs().validator().canCreateGraphTemplate( dtoGraphTemplate );
  }

  @Override
  protected ISkGraphTemplate doCreate( IM5Bunch<ISkGraphTemplate> aValues ) {
    IDtoFullObject dtoGraphTemplate = makeGraphTemplateDto( aValues, master() );
    ISkGraphTemplate retVal = graphTemplateService().createGraphTemplate( dtoGraphTemplate );
    return retVal;
  }

  @Override
  protected ValidationResult doBeforeEdit( IM5Bunch<ISkGraphTemplate> aValues ) {
    IDtoFullObject dtoGraphTemplate = makeGraphTemplateDto( aValues, master() );
    return graphTemplateService().svs().validator().canEditGraphTemplate( dtoGraphTemplate, aValues.originalEntity() );
  }

  @Override
  protected ISkGraphTemplate doEdit( IM5Bunch<ISkGraphTemplate> aValues ) {
    IDtoFullObject dtoGraphTemplate = makeGraphTemplateDto( aValues, master() );
    return graphTemplateService().editGraphTemplate( dtoGraphTemplate );
  }

  @Override
  protected ValidationResult doBeforeRemove( ISkGraphTemplate aEntity ) {
    return graphTemplateService().svs().validator().canRemoveGraphTemplate( aEntity.id() );
  }

  @Override
  protected void doRemove( ISkGraphTemplate aEntity ) {
    graphTemplateService().removeGraphTemplate( aEntity.id() );
  }

  @Override
  protected IList<ISkGraphTemplate> doListEntities() {
    return graphTemplateService().listGraphTemplates();
  }

  // ------------------------------------------------------------------------------------
  // Package API
  //

  static IDtoFullObject makeGraphTemplateDto( IM5Bunch<ISkGraphTemplate> aValues, ISkConnection aConnection ) {

    Skid skid = (aValues.originalEntity() == null) ? new Skid( ISkGraphTemplate.CLASS_ID, stridGenerator.nextId() )
        : aValues.originalEntity().skid();

    DtoFullObject dtoGraphTemplate = DtoFullObject.createDtoFullObject( skid, aConnection.coreApi() );
    dtoGraphTemplate.attrs().setValue( AID_NAME, aValues.getAsAv( AID_NAME ) );
    dtoGraphTemplate.attrs().setValue( AID_DESCRIPTION, aValues.getAsAv( AID_DESCRIPTION ) );
    dtoGraphTemplate.attrs().setValue( SkGraphTemplateM5Model.FID_AGGR_STEP,
        aValues.getAsAv( SkGraphTemplateM5Model.FID_AGGR_STEP ) );

    IList<ISkGraphParam> paramsList = aValues.getAs( CLBID_TEMPLATE_PARAMS, IList.class );
    String paramsStr = SkGraphParam.KEEPER.coll2str( paramsList );
    dtoGraphTemplate.clobs().put( CLBID_TEMPLATE_PARAMS, paramsStr );
    // установим ему автора
    ISkUser currUser = S5ConnectionUtils.getConnectedUser( aConnection );
    dtoGraphTemplate.links().map().put( LNKID_TEMPLATE_AUTHOR, new SkidList( currUser.skid() ) );

    return dtoGraphTemplate;
  }

}
