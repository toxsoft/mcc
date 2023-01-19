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
 * Отобразитель состояния масло-фильтра в виде "баллона".
 *
 * @author vs
 */
public class MccOilFilterBaloon
    extends AbstractMccSchemeControl {

  MccMultiRowLabel mrLabel;

  MccBaloon baloon;

  private static final Gwid dataGwid = Gwid.createRtdata( "mcc.CtrlSystem", "n2CtrlSystem", "rtdOilFilterAlarm" ); //$NON-NLS-1$ //$NON-NLS-2$//$NON-NLS-3$

  IAtomicValue value = IAtomicValue.NULL;

  Color magenta1;
  Color magenta2;

  Color red1;
  Color red2;

  Color green1;
  Color green2;

  Pair<Color, Color> noDataColors;
  Pair<Color, Color> trueColors;
  Pair<Color, Color> falseColors;

  public MccOilFilterBaloon( MccSchemePanel aOwner, ITsGuiContext aTsContext, IdChain aConnId ) {
    super( aOwner, dataGwid, aTsContext, aConnId );
    mrLabel = new MccMultiRowLabel( aTsContext );
    mrLabel.setRowGap( -8 );
    mrLabel.setText( new StringArrayList( "МФ", "в норме" ) );

    magenta1 = colorManager().getColor( new RGB( 255, 0, 220 ) );
    magenta2 = colorManager().getColor( new RGB( 180, 0, 220 ) );

    red1 = colorManager().getColor( new RGB( 255, 0, 0 ) );
    red2 = colorManager().getColor( new RGB( 180, 0, 0 ) );

    green1 = colorManager().getColor( new RGB( 0, 255, 0 ) );
    green2 = colorManager().getColor( new RGB( 0, 200, 0 ) );

    noDataColors = new Pair<>( magenta1, magenta2 );
    falseColors = new Pair<>( green1, green2 );
    trueColors = new Pair<>( red1, red2 );

    baloon = new MccBaloon( 516, 550, 80, 36, 16, 16, 15, 7, ETsFulcrum.LEFT_CENTER, aTsContext );
    mrLabel.setTextPlacementArea( new Rectangle( 516, 550, 80, 36 ) );
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
      mrLabel.setText( new StringArrayList( "Нет", "данных" ) );
      baloon.setBkColors( noDataColors );
    }
    else {
      if( value.asBool() ) {
        mrLabel.setText( new StringArrayList( "МФ", "Загрязнен" ) );
        baloon.setBkColors( trueColors );
      }
      else {
        mrLabel.setText( new StringArrayList( "МФ", "В норме" ) );
        baloon.setBkColors( falseColors );
      }
    }
    mrLabel.update();
    Rectangle r = bounds();
    schemePanel().redraw( r.x, r.y, r.width, r.height, false );
  }

}
