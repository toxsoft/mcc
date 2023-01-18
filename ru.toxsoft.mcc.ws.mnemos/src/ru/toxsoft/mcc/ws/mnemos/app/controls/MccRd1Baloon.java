package ru.toxsoft.mcc.ws.mnemos.app.controls;

import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.graphics.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.bricks.strid.more.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.core.tslib.gw.gwid.*;
import org.toxsoft.core.tslib.utils.*;

import ru.toxsoft.mcc.ws.mnemos.app.*;

/**
 * Отобразитель булевого РВ-данного в в виде "баллона".
 *
 * @author vs
 */
public class MccRd1Baloon
    extends AbstractMccSchemeControl {

  MccMultiRowLabel mrLabel;

  MccBaloon baloon;

  private static final Gwid dataGwid = Gwid.createRtdata( "mcc.DigInput", "n2DI_AirInGED_Norm", "rtdCurrentValue" ); //$NON-NLS-1$ //$NON-NLS-2$//$NON-NLS-3$

  IAtomicValue value = IAtomicValue.NULL;

  Color magenta1;
  Color magenta2;

  Color white1;
  Color white2;

  Color green1;
  Color green2;

  Pair<Color, Color> noDataColors;
  Pair<Color, Color> trueColors;
  Pair<Color, Color> falseColors;

  public MccRd1Baloon( MccSchemePanel aOwner, ITsGuiContext aTsContext, IdChain aConnId ) {
    super( aOwner, dataGwid, aTsContext, aConnId );
    mrLabel = new MccMultiRowLabel( aTsContext );
    mrLabel.setText( new StringArrayList( "РД1" ) );

    magenta1 = colorManager().getColor( new RGB( 255, 0, 220 ) );
    magenta2 = colorManager().getColor( new RGB( 180, 0, 220 ) );

    white1 = colorManager().getColor( new RGB( 255, 255, 255 ) );
    white2 = colorManager().getColor( new RGB( 200, 200, 200 ) );

    green1 = colorManager().getColor( new RGB( 0, 255, 0 ) );
    green2 = colorManager().getColor( new RGB( 0, 200, 0 ) );

    noDataColors = new Pair<>( magenta1, magenta2 );
    falseColors = new Pair<>( white1, white2 );
    trueColors = new Pair<>( green1, green2 );

    baloon = new MccBaloon( 220, 140, 50, 28, 12, 12, 12, 6, ETsFulcrum.BOTTOM_CENTER, aTsContext );
    mrLabel.setTextPlacementArea( new Rectangle( 220, 140, 50, 32 ) );
    baloon.setShadowDepth( 4 );
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
    return baloon.bounds();
  }

  @Override
  public void dispose() {
    baloon.dispose();
  }

  @Override
  public void showSettingDialog() {
    // nop
  }

  // ------------------------------------------------------------------------------------
  // Implementation
  //

  private void update() {
    if( value == null || !value.isAssigned() ) {
      mrLabel.setText( new StringArrayList( "???" ) ); //$NON-NLS-1$
      baloon.setBkColors( noDataColors );
    }
    else {
      mrLabel.setText( new StringArrayList( "РД1" ) );
      if( value.asBool() ) {
        baloon.setBkColors( trueColors );
      }
      else {
        baloon.setBkColors( falseColors );
      }
    }
    mrLabel.update();
    Rectangle r = bounds();
    schemePanel().redraw( r.x, r.y, r.width, r.height, false );
  }

}
