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
public class MccOilTankBaloon
    extends AbstractMccSchemeControl {

  MccMultiRowLabel mrLabel;

  MccBaloon baloon;

  IAtomicValue value = IAtomicValue.NULL;

  Color magenta1;
  Color magenta2;

  Color red1;
  Color red2;

  Color green1;
  Color green2;

  Color yellow1;
  Color yellow2;

  Pair<Color, Color> noDataColors;
  Pair<Color, Color> hiColors;
  Pair<Color, Color> lowColors;
  Pair<Color, Color> normColors;

  IAtomicValue hiLevel  = IAtomicValue.NULL;
  IAtomicValue lowLevel = IAtomicValue.NULL;

  private static final Gwid lowGwid = Gwid.createRtdata( "mcc.DigInput", "n2DI_L1_LOil", "rtdCurrentValue" ); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
  private static final Gwid hiGwid  = Gwid.createRtdata( "mcc.DigInput", "n2DI_L1_HOil", "rtdCurrentValue" ); //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$

  /**
   * Конструктор.
   *
   * @param aOwner MccSchemePanel - родительская панель мнемосхемы
   * @param aTsContext ITsGuiContext - соответствующий контекст
   * @param aConnId IdChain - ИД соединения
   */
  public MccOilTankBaloon( MccSchemePanel aOwner, ITsGuiContext aTsContext, IdChain aConnId ) {
    super( aOwner, lowGwid, aTsContext, aConnId );
    mrLabel = new MccMultiRowLabel( aTsContext );
    mrLabel.setRowGap( -6 );
    mrLabel.setText( new StringArrayList( "Нет", "данных" ) );

    magenta1 = colorManager().getColor( new RGB( 255, 0, 220 ) );
    magenta2 = colorManager().getColor( new RGB( 180, 0, 220 ) );

    red1 = colorManager().getColor( new RGB( 255, 0, 0 ) );
    red2 = colorManager().getColor( new RGB( 200, 0, 0 ) );

    green1 = colorManager().getColor( new RGB( 0, 255, 0 ) );
    green2 = colorManager().getColor( new RGB( 0, 200, 0 ) );

    yellow1 = colorManager().getColor( new RGB( 0, 255, 255 ) );
    yellow2 = colorManager().getColor( new RGB( 0, 200, 200 ) );

    noDataColors = new Pair<>( magenta1, magenta2 );
    hiColors = new Pair<>( yellow1, yellow2 );
    lowColors = new Pair<>( red1, red2 );
    normColors = new Pair<>( green1, green2 );

    baloon = new MccBaloon( 800, 550, 80, 36, 16, 16, 15, 7, ETsFulcrum.LEFT_CENTER, aTsContext );
    mrLabel.setTextPlacementArea( new Rectangle( 800, 550, 80, 36 ) );
    baloon.setShadowDepth( 4 );
  }

  // ------------------------------------------------------------------------------------
  // AbstractMccSchemeControl
  //

  @Override
  public IGwidList listNeededGwids() {
    GwidList gList = new GwidList();
    gList.add( lowGwid );
    gList.add( hiGwid );
    return gList;
  }

  @Override
  public void setValues( Gwid[] aGwids, IAtomicValue[] aValues, int aCount ) {
    for( int i = 0; i < aCount; i++ ) {
      if( aGwids[i].equals( lowGwid ) ) {
        lowLevel = aValues[i];
      }
      if( aGwids[i].equals( hiGwid ) ) {
        hiLevel = aValues[i];
      }
    }
    update();
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
    if( !lowLevel.isAssigned() || !hiLevel.isAssigned() ) {
      mrLabel.setText( new StringArrayList( "Нет", "данных" ) );
      baloon.setBkColors( noDataColors );
    }
    else {
      if( lowLevel.asBool() ) {
        mrLabel.setText( new StringArrayList( "Низкий", "уровень" ) );
        baloon.setBkColors( lowColors );
      }
      else {
        if( hiLevel.asBool() ) {
          mrLabel.setText( new StringArrayList( "Высокий", "уровень" ) );
          baloon.setBkColors( hiColors );
        }
        else {
          mrLabel.setText( new StringArrayList( "Уровень", "в норме" ) );
          baloon.setBkColors( normColors );
        }
      }
    }
    mrLabel.update();
    Rectangle r = bounds();
    schemePanel().redraw( r.x, r.y, r.width, r.height, false );
  }

}
