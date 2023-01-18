package ru.toxsoft.mcc.ws.mnemos.app.controls;

import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.bricks.strid.more.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.core.tslib.gw.gwid.*;

import ru.toxsoft.mcc.ws.mnemos.app.*;

/**
 * Отобразитель булевого РВ-данного в в виде "баллона".
 *
 * @author vs
 */
public class MccRtBaloon
    extends AbstractMccSchemeControl {

  MccMultiRowLabel mrLabel;

  MccBaloon baloon;

  Gwid dataGwid;

  IAtomicValue value = IAtomicValue.NULL;

  protected MccRtBaloon( MccSchemePanel aOwner, Gwid aDataGwid, ITsGuiContext aTsContext, IdChain aConnId ) {
    super( aOwner, aDataGwid, aTsContext, aConnId );
    dataGwid = aDataGwid;
  }

  // ------------------------------------------------------------------------------------
  // AbstractMccSchemeControl
  //

  @Override
  public IGwidList listNeededGwids() {
    GwidList gList = new GwidList();
    gList.add( dataGwid );
    return gList;
  }

  @Override
  public void setValues( Gwid[] aGwids, IAtomicValue[] aValues, int aCount ) {
    if( aCount == 1 ) {
      if( aGwids[0].equals( dataGwid ) ) {
        value = aValues[0];
      }
      update();
    }
  }

  @Override
  public void paint( GC aGc ) {
    baloon.paint( aGc );
    mrLabel.paint( aGc );
  }

  @Override
  public Rectangle bounds() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void dispose() {
    baloon.dispose();
  }

  @Override
  public void showSettingDialog() {
    // TODO Auto-generated method stub

  }

  // ------------------------------------------------------------------------------------
  // Implementation
  //

  private void update() {
    if( value == null || !value.isAssigned() ) {
      mrLabel.setText( new StringArrayList( "Нет данных" ) );
    }
    else {
      if( value.asBool() ) {

      }
      else {
      }
    }
    Rectangle r = bounds();
    schemePanel().redraw( r.x, r.y, r.width, r.height, false );
  }

}
