package ru.toxsoft.mcc.ws.reports.lib.impl;

import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.gw.skid.*;
import org.toxsoft.uskat.core.api.objserv.*;

import ru.toxsoft.mcc.ws.reports.lib.*;

/**
 * {@link ISkGraphTemplate} implementation.
 *
 * @author dima
 */
class SkGraphTemplate
    extends SkBaseTemplate<ISkGraphParam>
    implements ISkGraphTemplate {

  static final ISkObjectCreator<SkGraphTemplate> CREATOR = SkGraphTemplate::new;

  SkGraphTemplate( Skid aSkid ) {
    super( aSkid );
  }

  // ------------------------------------------------------------------------------------
  // ISkGraphTemplate
  //

  @Override
  public IList<ISkGraphParam> listParams() {
    String paramsStr = coreApi().clobService().readClob( parametersGwid() );
    return SkGraphParam.KEEPER.str2coll( paramsStr );
  }

}
