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

public class MccAnalogEngineControl
    extends AbstractMultiImageControl {

  final IList<Image> imgList;

  private final Color colorMagenta;

  /**
   * Конструктор.
   *
   * @param aOwner MccSchemePanel - родительская панель мнемосхемы
   * @param aObjGwid Gwid - ИД объекта
   * @param aImageIds IStringList - список ИДов изображений
   * @param aTsContext ITsGuiContext - соответствующий контекст
   */
  public MccAnalogEngineControl( MccSchemePanel aOwner, Gwid aObjGwid, IStringList aImageIds,
      ITsGuiContext aTsContext ) {
    super( aOwner, aObjGwid, aTsContext );

    imgList = new ElemArrayList<>();
    for( String imgId : aImageIds ) {
      Image img = AbstractUIPlugin.imageDescriptorFromPlugin( PLUGIN_ID, imgId ).createImage();
      ((ElemArrayList<Image>)imgList).add( img );
    }

    colorMagenta = colorManager().getColor( ETsColor.MAGENTA );
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
    PanelAnalogEngine.showDialog( ctx );
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
    values.put( "rtdAutoCtrl", IAtomicValue.NULL ); //$NON-NLS-1$

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

  // ------------------------------------------------------------------------------------
  // Implementation
  //

  private void update() {
    setBkColor( null );
    setFgColor( null );
    setTooltipText( null );

    EAnalogEngineState state = calcState( values );
    switch( state ) {
      case ON:
        stopAnimation( 0 );
        break;
      case OFF:
        stopAnimation( 1 );
        break;
      case FAULT:
        stopAnimation( 2 );
        break;
      case UNKNOWN:
        setBkColor( colorMagenta );
        stopAnimation( 3 );
        break;
      default:
        throw new TsNotAllEnumsUsedRtException();
    }

    StringBuilder strTooltip = new StringBuilder().append( state.nmName() );
    setTooltipText( strTooltip.toString() );
    schemePanel().redraw( bounds.x, bounds.y, bounds.width, bounds.height, false );
  }

  private static EAnalogEngineState calcState( IStringMap<IAtomicValue> aValuesMap ) {
    EAnalogEngineState state = EAnalogEngineState.UNKNOWN;

    IAtomicValue alarm = aValuesMap.getByKey( "rtdAlarm" ); //$NON-NLS-1$
    if( alarm != null && alarm.isAssigned() && alarm.asBool() ) {
      return EAnalogEngineState.FAULT;
    }

    IAtomicValue autoCtrl = aValuesMap.getByKey( "rtdAutoCtrl" ); //$NON-NLS-1$
    if( autoCtrl != null && autoCtrl.isAssigned() ) {
      if( autoCtrl.asBool() ) {
        return EAnalogEngineState.ON;
      }
      return EAnalogEngineState.OFF;
    }
    return state;
  }

}
