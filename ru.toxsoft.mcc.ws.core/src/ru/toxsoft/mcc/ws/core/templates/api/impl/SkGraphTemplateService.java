package ru.toxsoft.mcc.ws.core.templates.api.impl;

import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.gw.IGwHardConstants.*;
import static org.toxsoft.uskat.core.ISkHardConstants.*;
import static ru.toxsoft.mcc.ws.core.templates.api.ISkTemplateEditorServiceHardConstants.*;
import static ru.toxsoft.mcc.ws.core.templates.api.impl.ISkResources.*;

import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.bricks.ctx.*;
import org.toxsoft.core.tslib.bricks.events.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.bricks.strio.*;
import org.toxsoft.core.tslib.bricks.strio.chario.*;
import org.toxsoft.core.tslib.bricks.strio.chario.impl.*;
import org.toxsoft.core.tslib.bricks.strio.impl.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.bricks.validator.impl.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.helpers.*;
import org.toxsoft.core.tslib.gw.skid.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.tslib.utils.logs.impl.*;
import org.toxsoft.uskat.core.*;
import org.toxsoft.uskat.core.api.objserv.*;
import org.toxsoft.uskat.core.api.sysdescr.dto.*;
import org.toxsoft.uskat.core.api.users.*;
import org.toxsoft.uskat.core.connection.*;
import org.toxsoft.uskat.core.devapi.*;
import org.toxsoft.uskat.core.impl.*;
import org.toxsoft.uskat.core.impl.dto.*;
import org.toxsoft.uskat.s5.utils.*;

import ru.toxsoft.mcc.ws.core.templates.api.*;

/**
 * {@link ISkGraphTemplateService} implementation.
 *
 * @author dima
 */
public class SkGraphTemplateService
    extends AbstractSkService
    implements ISkGraphTemplateService {

  /**
   * Service creator singleton.
   */
  public static final ISkServiceCreator<AbstractSkService> CREATOR = SkGraphTemplateService::new;

  /**
   * {@link ISkGraphTemplateService#svs()} implementation.
   *
   * @author dima
   */
  static class ValidationSupport
      extends AbstractTsValidationSupport<ISkGraphTemplateServiceValidator>
      implements ISkGraphTemplateServiceValidator {

    @Override
    public ISkGraphTemplateServiceValidator validator() {
      return this;
    }

    @Override
    public ValidationResult canCreateGraphTemplate( IDtoFullObject aGraphTemplateDto ) {
      TsNullArgumentRtException.checkNull( aGraphTemplateDto );
      ValidationResult vr = ValidationResult.SUCCESS;
      for( ISkGraphTemplateServiceValidator v : validatorsList() ) {
        vr = ValidationResult.firstNonOk( vr, v.canCreateGraphTemplate( aGraphTemplateDto ) );
      }
      return vr;
    }

    @Override
    public ValidationResult canEditGraphTemplate( IDtoFullObject aGraphTemplateDto,
        ISkGraphTemplate aOldGraphTemplate ) {
      TsNullArgumentRtException.checkNulls( aGraphTemplateDto, aOldGraphTemplate );
      ValidationResult vr = ValidationResult.SUCCESS;
      for( ISkGraphTemplateServiceValidator v : validatorsList() ) {
        vr = ValidationResult.firstNonOk( vr, v.canEditGraphTemplate( aGraphTemplateDto, aOldGraphTemplate ) );
      }
      return vr;
    }

    @Override
    public ValidationResult canRemoveGraphTemplate( String aGraphTemplateId ) {
      TsNullArgumentRtException.checkNull( aGraphTemplateId );
      ValidationResult vr = ValidationResult.SUCCESS;
      for( ISkGraphTemplateServiceValidator v : validatorsList() ) {
        vr = ValidationResult.firstNonOk( vr, v.canRemoveGraphTemplate( aGraphTemplateId ) );
      }
      return vr;
    }

  }

  /**
   * {@link ISkGraphTemplateService#eventer()} implementation.
   *
   * @author dima
   */
  class Eventer
      extends AbstractTsEventer<ISkGraphTemplateServiceListener> {

    private boolean isPendingGraphTemplates = false;

    @Override
    protected void doClearPendingEvents() {
      isPendingGraphTemplates = false;
    }

    @Override
    protected void doFirePendingEvents() {
      if( isPendingGraphTemplates ) {
        reallyFireGraphTemplate( ECrudOp.LIST, null );
      }
    }

    @Override
    protected boolean doIsPendingEvents() {
      return isPendingGraphTemplates;
    }

    private void reallyFireGraphTemplate( ECrudOp aOp, String aGraphTemplateId ) {
      for( ISkGraphTemplateServiceListener l : listeners() ) {
        try {
          l.onGraphTemplateChanged( coreApi(), aOp, aGraphTemplateId );
        }
        catch( Exception ex ) {
          LoggerUtils.errorLogger().error( ex );
        }
      }
    }

    void fireGraphTemplateChanged( ECrudOp aOp, String aGraphTemplateId ) {
      if( isFiringPaused() ) {
        isPendingGraphTemplates = true;
        return;
      }
      reallyFireGraphTemplate( aOp, aGraphTemplateId );
    }

  }

  /**
   * Builtin service validator.
   */
  private final ISkGraphTemplateServiceValidator builtinValidator = new ISkGraphTemplateServiceValidator() {

    @Override
    public ValidationResult canCreateGraphTemplate( IDtoFullObject aGraphTemplateDto ) {
      // check precondition
      if( !aGraphTemplateDto.skid().classId().equals( ISkGraphTemplate.CLASS_ID ) ) {
        return ValidationResult.error( FMT_ERR_NOT_REPORT_TEMPLATE_DPU, aGraphTemplateDto.classId(),
            ISkGraphTemplate.CLASS_ID );
      }
      // check if parameters list is empty
      // first, get string from Clob
      String templParamsStr =
          aGraphTemplateDto.clobs().getByKey( ISkTemplateEditorServiceHardConstants.CLBID_TEMPLATE_PARAMS );
      // next, convert string to collection
      ICharInputStream charInputStream = new CharInputStreamString( templParamsStr );
      IStrioReader sr = new StrioReader( charInputStream );
      IList<ISkGraphParam> params = SkGraphParam.KEEPER.readColl( sr );
      if( params == null || params.isEmpty() ) {
        return ValidationResult.error( MSG_ERR_NO_PARAMS );
      }
      return ValidationResult.SUCCESS;
    }

    @Override
    public ValidationResult canEditGraphTemplate( IDtoFullObject aGraphTemplateDto,
        ISkGraphTemplate aOldGraphTemplate ) {
      ValidationResult vr = ValidationResult.SUCCESS;
      // check precondition
      if( !aGraphTemplateDto.skid().classId().equals( ISkGraphTemplate.CLASS_ID ) ) {
        return ValidationResult.error( FMT_ERR_NOT_REPORT_TEMPLATE_DPU, aGraphTemplateDto.classId(),
            ISkGraphTemplate.CLASS_ID );
      }
      // check if parameters list is empty
      // first, get string from Clob
      String templParamsStr =
          aGraphTemplateDto.clobs().getByKey( ISkTemplateEditorServiceHardConstants.CLBID_TEMPLATE_PARAMS );
      // next, convert string to collection
      ICharInputStream charInputStream = new CharInputStreamString( templParamsStr );
      IStrioReader sr = new StrioReader( charInputStream );
      IList<ISkGraphParam> params = SkGraphParam.KEEPER.readColl( sr );
      if( params == null || params.isEmpty() ) {
        return ValidationResult.error( MSG_ERR_NO_PARAMS );
      }
      return vr;
    }

    @Override
    public ValidationResult canRemoveGraphTemplate( String aGraphTemplateId ) {
      // TODO check if current user can right to remove this template
      // ???
      return ValidationResult.SUCCESS;
    }

  };

  private final ValidationSupport          validationSupport = new ValidationSupport();
  private final Eventer                    eventer           = new Eventer();
  private final ClassClaimingCoreValidator claimingValidator = new ClassClaimingCoreValidator();
  private ISkConnection                    connection        = null;

  /**
   * Constructor.
   *
   * @param aCoreApi {@link IDevCoreApi} - owner core API implementation
   */
  SkGraphTemplateService( IDevCoreApi aCoreApi ) {
    super( SERVICE_ID, aCoreApi );
    validationSupport.addValidator( builtinValidator );
  }

  // ------------------------------------------------------------------------------------
  // AbstractSkCoreService
  //

  @Override
  protected void doInit( ITsContextRo aArgs ) {
    // create class for ISkGraphTemplate
    IDtoClassInfo graphTemplateCinf = internalCreateGraphTemplateClassDto();
    sysdescr().defineClass( graphTemplateCinf );
    objServ().registerObjectCreator( ISkGraphTemplate.CLASS_ID, SkGraphTemplate.CREATOR );
    //
    sysdescr().svs().addValidator( claimingValidator );
    objServ().svs().addValidator( claimingValidator );
    linkService().svs().addValidator( claimingValidator );
    clobService().svs().addValidator( claimingValidator );
  }

  @Override
  protected void doClose() {
    // nop
  }

  @Override
  protected boolean doIsClassClaimedByService( String aClassId ) {
    return switch( aClassId ) {
      case ISkGraphTemplate.CLASS_ID -> true;
      default -> false;
    };
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  /**
   * Creates DTO of {@link ISkGraphTemplate#CLASS_ID} class.
   *
   * @return {@link IDtoClassInfo} - {@link ISkGraphTemplate#CLASS_ID} class info
   */
  public static IDtoClassInfo internalCreateGraphTemplateClassDto() {
    DtoClassInfo cinf = new DtoClassInfo( ISkGraphTemplate.CLASS_ID, GW_ROOT_CLASS_ID, IOptionSet.NULL );
    // TODO need clarification
    OPDEF_SK_IS_SOURCE_CODE_DEFINED_CLASS.setValue( cinf.params(), AV_TRUE );
    OPDEF_SK_IS_SOURCE_USKAT_CORE_CLASS.setValue( cinf.params(), AV_TRUE );
    cinf.attrInfos().add( ATRINF_TITLE );
    cinf.attrInfos().add( ATRINF_AGGR_STEP );
    cinf.clobInfos().add( CLBINF_TEMPLATE_PARAMS );
    cinf.linkInfos().add( LNKINF_TEMPLATE_AUTHOR );
    return cinf;
  }

  private void pauseCoreValidation() {
    sysdescr().svs().pauseValidator( claimingValidator );
    objServ().svs().pauseValidator( claimingValidator );
    linkService().svs().pauseValidator( claimingValidator );
    clobService().svs().pauseValidator( claimingValidator );
  }

  private void resumeCoreValidation() {
    sysdescr().svs().resumeValidator( claimingValidator );
    objServ().svs().resumeValidator( claimingValidator );
    linkService().svs().resumeValidator( claimingValidator );
    clobService().svs().resumeValidator( claimingValidator );
  }

  // ------------------------------------------------------------------------------------
  // ISkGraphTemplateService
  //

  @Override
  public ITsValidationSupport<ISkGraphTemplateServiceValidator> svs() {
    return validationSupport;
  }

  @Override
  public ITsEventer<ISkGraphTemplateServiceListener> eventer() {
    return eventer;
  }

  @Override
  public IList<ISkGraphTemplate> listGraphTemplates() {
    IList<ISkGraphTemplate> ll = objServ().listObjs( ISkGraphTemplate.CLASS_ID, false );
    return new StridablesList<>( ll );
  }

  @Override
  public ISkGraphTemplate findGraphTemplate( String aTempId ) {
    TsNullArgumentRtException.checkNull( aTempId );
    return coreApi().objService().find( new Skid( ISkGraphTemplate.CLASS_ID, aTempId ) );
  }

  @Override
  public ISkGraphTemplate createGraphTemplate( IDtoFullObject aDtoGraphTemplate ) {
    TsValidationFailedRtException.checkError( validationSupport.canCreateGraphTemplate( aDtoGraphTemplate ) );
    pauseCoreValidation();
    try {
      ISkGraphTemplate retVal = DtoFullObject.defineFullObject( coreApi(), aDtoGraphTemplate );
      // установим ему автора
      if( connection != null ) {
        ISkUser currUser = S5ConnectionUtils.getConnectedUser( connection );
        coreApi().linkService().defineLink( retVal.skid(), LNKID_TEMPLATE_AUTHOR, null,
            new SkidList( currUser.skid() ) );
      }
      return retVal;
    }
    finally {
      resumeCoreValidation();
    }
  }

  @Override
  public ISkGraphTemplate editGraphTemplate( IDtoFullObject aDtoGraphTemplate ) {
    TsNullArgumentRtException.checkNull( aDtoGraphTemplate );
    TsIllegalArgumentRtException.checkFalse( aDtoGraphTemplate.classId().equals( ISkGraphTemplate.CLASS_ID ) );
    ISkGraphTemplate oldGraphTemplate = objServ().find( aDtoGraphTemplate.skid() );
    TsItemNotFoundRtException.checkNull( oldGraphTemplate );
    TsValidationFailedRtException
        .checkError( validationSupport.canEditGraphTemplate( aDtoGraphTemplate, oldGraphTemplate ) );
    pauseCoreValidation();
    try {
      return DtoFullObject.defineFullObject( coreApi(), aDtoGraphTemplate );
    }
    finally {
      resumeCoreValidation();
    }
  }

  @Override
  public void removeGraphTemplate( String aGraphTemplateId ) {
    TsValidationFailedRtException.checkError( svs().validator().canRemoveGraphTemplate( aGraphTemplateId ) );
    pauseCoreValidation();
    try {
      coreApi().objService().removeObject( new Skid( ISkGraphTemplate.CLASS_ID, aGraphTemplateId ) );
    }
    finally {
      resumeCoreValidation();
    }

  }

  @Override
  public void setConnection( ISkConnection aConnection ) {
    TsNullArgumentRtException.checkNull( aConnection );
    connection = aConnection;
  }

}
