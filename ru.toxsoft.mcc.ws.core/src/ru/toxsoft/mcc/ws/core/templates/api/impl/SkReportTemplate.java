package ru.toxsoft.mcc.ws.core.templates.api.impl;

import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.gw.skid.*;
import org.toxsoft.uskat.core.api.objserv.*;

import ru.toxsoft.mcc.ws.core.templates.api.*;

/**
 * {@link ISkReportTemplate} implementation.
 *
 * @author dima
 */
class SkReportTemplate
    extends SkBaseTemplate<ISkReportParam>
    implements ISkReportTemplate {

  static final ISkObjectCreator<SkReportTemplate> CREATOR = SkReportTemplate::new;

  SkReportTemplate( Skid aSkid ) {
    super( aSkid );
  }

  // ------------------------------------------------------------------------------------
  // ISkReportTemplate
  //

  @Override
  public IList<ISkReportParam> listParams() {
    String paramsStr = coreApi().clobService().readClob( parametersGwid() );
    return SkReportParam.KEEPER.str2coll( paramsStr );
  }

}
