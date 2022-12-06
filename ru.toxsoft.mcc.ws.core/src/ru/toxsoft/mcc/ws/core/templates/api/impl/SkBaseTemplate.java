package ru.toxsoft.mcc.ws.core.templates.api.impl;

import static ru.toxsoft.mcc.ws.core.templates.api.ISkTemplateEditorServiceHardConstants.*;

import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.gw.gwid.*;
import org.toxsoft.core.tslib.gw.skid.*;
import org.toxsoft.uskat.core.api.users.*;
import org.toxsoft.uskat.core.impl.*;

import ru.toxsoft.mcc.ws.core.templates.api.*;

/**
 * {@link ISkReportTemplate} implementation.
 *
 * @author dima
 * @param <T> тип параметров шаблона
 */
abstract class SkBaseTemplate<T extends ISkTemplateParam>
    extends SkObject
    implements ISkBaseTemplate<T> {

  private transient Gwid parametersGwid = null;

  SkBaseTemplate( Skid aSkid ) {
    super( aSkid );
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  protected Gwid parametersGwid() {
    if( parametersGwid == null ) {
      parametersGwid = Gwid.createClob( classId(), strid(), CLBID_TEMPLATE_PARAMS );
    }
    return parametersGwid;
  }

  // ------------------------------------------------------------------------------------
  // ISkReportTemplate
  //

  @Override
  abstract public IList<T> listParams();

  @Override
  public ISkUser author() {
    return (ISkUser)getLinkObjs( LNKID_TEMPLATE_AUTHOR ).first();
  }

}
