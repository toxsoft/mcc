package ru.toxsoft.mcc.ws.mnemos.app.rt;

import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.gw.gwid.*;
import org.toxsoft.core.tslib.utils.errors.*;

public class SingleDataConsumer
    implements IRtDataConsumer {

  private final String id;

  private final Gwid dataGwid;

  private IAtomicValue value = IAtomicValue.NULL;

  public SingleDataConsumer( Gwid aDataGwid ) {
    id = aDataGwid.classId() + "." + aDataGwid.strid() + "." + aDataGwid.propId(); //$NON-NLS-1$ //$NON-NLS-2$
    dataGwid = aDataGwid;
  }

  // ------------------------------------------------------------------------------------
  // IRtDataConsumer
  //

  @Override
  public String id() {
    return id;
  }

  @Override
  public IGwidList listNeededGwids() {
    return new GwidList( dataGwid );
  }

  @Override
  public void setValues( Gwid[] aGwids, IAtomicValue[] aValues, int aCount ) {
    TsIllegalArgumentRtException.checkFalse( aGwids[0].equals( dataGwid ) );
    value = aValues[0];
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  IAtomicValue value() {
    return value;
  }
}
