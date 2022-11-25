package ru.toxsoft.mcc.ws.mnemos.app.controls;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.bricks.strid.more.*;
import org.toxsoft.core.tslib.gw.gwid.*;
import org.toxsoft.uskat.core.api.objserv.*;

/**
 * Базовый класс для контролей, работающих с единственным РВ-данным для проекта МосКокс.
 * <p>
 *
 * @author vs
 */
public abstract class AbstractMccSingleDataControl
    extends AbstractMccControl {

  IAtomicValue value = IAtomicValue.NULL;

  EAtomicType valueType = EAtomicType.NONE;

  protected AbstractMccSingleDataControl( ISkObject aSkObject, String aDataId, ITsGuiContext aTsContext,
      IdChain aConnId ) {
    super( aSkObject, aDataId, aTsContext, aConnId );
    valueType = dataInfo( aDataId ).dataType().atomicType();
  }

  // ------------------------------------------------------------------------------------
  // IRtDataConsumer
  //

  @Override
  public final IGwidList listNeededGwids() {
    GwidList gl = new GwidList();
    gl.add( Gwid.createRtdata( skObject().classId(), skObject().strid(), dataId() ) );
    return gl;
  }

  @Override
  public final void setValues( Gwid[] aGwids, IAtomicValue[] aValues, int aCount ) {
    if( aCount == 1 ) {
      value = aValues[0];
      update();
    }
  }
}
