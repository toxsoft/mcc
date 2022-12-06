package ru.toxsoft.mcc.ws.mnemos.app.controls;

import static ru.toxsoft.mcc.ws.mnemos.Activator.*;

import org.eclipse.swt.graphics.*;
import org.eclipse.ui.plugin.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.graphics.colors.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.core.tslib.gw.gwid.*;
import org.toxsoft.core.tslib.utils.errors.*;

import ru.toxsoft.mcc.ws.mnemos.app.*;
import ru.toxsoft.mcc.ws.mnemos.app.dialogs.*;

/**
 * Задвижка (проект МосКокс).
 * <p>
 *
 * @author vs
 */
public class MccValveControl
    extends AbstractMultiImageControl {

  final IList<Image> imgList;

  private final Color colorDarkGray;
  private final Color colorImitation;
  private final Color colorMagenta;
  private final Color colorRed;

  /**
   * Конструктор.
   *
   * @param aOwner MccSchemePanel - родительская панель мнемосхемы
   * @param aObjGwid Gwid - ИД объекта
   * @param aImageIds IStringList - список ИДов изображений
   * @param aTsContext ITsGuiContext - соответствующий контекст
   */
  public MccValveControl( MccSchemePanel aOwner, Gwid aObjGwid, IStringList aImageIds, ITsGuiContext aTsContext ) {
    super( aOwner, aObjGwid, aTsContext );

    imgList = new ElemArrayList<>();
    for( String imgId : aImageIds ) {
      Image img = AbstractUIPlugin.imageDescriptorFromPlugin( PLUGIN_ID, imgId ).createImage();
      ((ElemArrayList<Image>)imgList).add( img );
    }

    colorDarkGray = colorManager().getColor( ETsColor.DARK_GRAY );
    colorMagenta = colorManager().getColor( ETsColor.MAGENTA );
    colorRed = colorManager().getColor( ETsColor.RED );
    colorImitation = colorManager().getColor( new RGB( 107, 195, 255 ) );
    // startAnimation( new int[] { 0, 4 } );
  }

  // ------------------------------------------------------------------------------------
  // AbstractMultiImageControl
  //

  @Override
  protected IList<Image> listImages() {
    return imgList;
  }

  @Override
  public void showSettingDialog() {
    MccDialogContext ctx = new MccDialogContext( tsContext(), skObject() );
    PanelReversibleEngine.showDialog( ctx );
  }

  // ------------------------------------------------------------------------------------
  // IRtDataConsumer
  //

  /**
   * Карта значений необходимых данных
   */
  private final IStringMapEdit<IAtomicValue> values = new StringMap<>();

  @Override
  public IGwidList listNeededGwids() {

    values.put( "rtdAlarm", IAtomicValue.NULL ); //$NON-NLS-1$
    values.put( "rtdEnabled", IAtomicValue.NULL ); //$NON-NLS-1$
    values.put( "rtdImitation", IAtomicValue.NULL ); //$NON-NLS-1$
    values.put( "rtdOpened", IAtomicValue.NULL ); //$NON-NLS-1$
    values.put( "rtdClosed", IAtomicValue.NULL ); //$NON-NLS-1$
    values.put( "rtdOpen", IAtomicValue.NULL ); //$NON-NLS-1$
    values.put( "rtdClose", IAtomicValue.NULL ); //$NON-NLS-1$

    String classId = skObject().classId();
    String strid = skObject().strid();
    GwidList gl = new GwidList();
    for( String dataId : values.keys() ) {
      Gwid gwid = Gwid.createRtdata( classId, strid, dataId );
      gl.add( gwid );
    }
    return gl;
  }

  @Override
  public void setValues( Gwid[] aGwids, IAtomicValue[] aValues, int aCount ) {
    for( int i = 0; i < aCount; i++ ) {
      values.put( aGwids[i].propId(), aValues[i] );
    }
    update();
  }

  private void update() {
    setBkColor( null );
    setFgColor( null );
    setTooltipText( null );

    EReversibleEngineState state = calcState( values );
    switch( state ) {
      case UNKNOWN:
        setBkColor( colorMagenta );
        stopAnimation( 3 );
        break;
      case INVALID:
        stopAnimation( 3 );
        break;
      case CLOSED:
        stopAnimation( 1 );
        break;
      case CLOSING:
        startAnimation( new int[] { 1, 4 } );
        break;
      case FAULT:
        stopAnimation( 2 );
        break;
      case OPENED:
        stopAnimation( 0 );
        break;
      case OPENING:
        startAnimation( new int[] { 0, 4 } );
        break;
      case PARTIALLY_OPENED:
        stopAnimation( 4 );
        break;
      default:
        throw new TsNotAllEnumsUsedRtException();
    }

    StringBuilder strTooltip = new StringBuilder().append( state.nmName() );

    IAtomicValue val = values.getByKey( "rtdEnabled" ); //$NON-NLS-1$
    if( val != null && val.isAssigned() && !val.asBool() ) {
      // setBkColor( colorDarkGray );
      setFgColor( colorRed );
      strTooltip.append( "/блокировка" );
    }
    val = values.getByKey( "rtdImitation" ); //$NON-NLS-1$
    if( val != null && val.isAssigned() && val.asBool() ) {
      setFgColor( colorImitation );
      strTooltip.append( "/имитация" );
    }

    setTooltipText( strTooltip.toString() );
    schemePanel().redraw( bounds.x, bounds.y, bounds.width, bounds.height, false );
  }

  private static EReversibleEngineState calcState( IStringMap<IAtomicValue> aValuesMap ) {
    EReversibleEngineState state = EReversibleEngineState.UNKNOWN;

    IAtomicValue alarm = aValuesMap.getByKey( "rtdAlarm" ); //$NON-NLS-1$
    if( alarm != null && alarm.isAssigned() && alarm.asBool() ) {
      return EReversibleEngineState.FAULT;
    }

    IAtomicValue opened = aValuesMap.getByKey( "rtdOpened" ); //$NON-NLS-1$
    IAtomicValue closed = aValuesMap.getByKey( "rtdClosed" ); //$NON-NLS-1$
    IAtomicValue opening = aValuesMap.getByKey( "rtdOpen" ); //$NON-NLS-1$
    IAtomicValue closing = aValuesMap.getByKey( "rtdClose" ); //$NON-NLS-1$

    if( opened == null || !opened.isAssigned() || //
        closed == null || !opened.isAssigned() || //
        opening == null || !opened.isAssigned() || //
        closing == null || !opened.isAssigned() ) {
      return EReversibleEngineState.UNKNOWN;
    }

    int count = 0;
    state = EReversibleEngineState.PARTIALLY_OPENED;
    if( opened.asBool() ) {
      state = EReversibleEngineState.OPENED;
      count++;
    }
    if( closed.asBool() ) {
      state = EReversibleEngineState.CLOSED;
      count++;
    }
    if( opening.asBool() ) {
      state = EReversibleEngineState.OPENING;
      count++;
    }
    if( closing.asBool() ) {
      state = EReversibleEngineState.CLOSING;
      count++;
    }

    if( count > 1 ) {
      return EReversibleEngineState.INVALID;
    }

    return state;
  }

}
