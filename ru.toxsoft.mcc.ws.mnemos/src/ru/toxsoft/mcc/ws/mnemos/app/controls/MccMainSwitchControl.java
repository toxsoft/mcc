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
 * Главный "включатель" нагнетателя (проект МосКокс).
 * <p>
 *
 * @author vs
 */
public class MccMainSwitchControl
    extends AbstractMultiImageControl {

  final IList<Image> imgList;

  private final Color colorRed;
  private final Color colorImitation;
  private final Color colorMagenta;

  private final MccMainEngineControl mainEngine;

  /**
   * Конструктор.
   *
   * @param aOwner MccSchemePanel - родительская панель мнемосхемы
   * @param aObjGwid Gwid - конкретный ИД объекта
   * @param aImageIds IStringList - список ИДов изображений
   * @param aTsContext ITsGuiContext - соотвествующий контекст
   */
  public MccMainSwitchControl( MccSchemePanel aOwner, MccMainEngineControl aMainEngine, Gwid aObjGwid,
      IStringList aImageIds, ITsGuiContext aTsContext ) {
    super( aOwner, aObjGwid, aTsContext );

    mainEngine = TsNullArgumentRtException.checkNull( aMainEngine );

    colorRed = colorManager().getColor( ETsColor.RED );
    colorImitation = colorManager().getColor( new RGB( 107, 195, 255 ) );
    colorMagenta = colorManager().getColor( ETsColor.MAGENTA );

    imgList = new ElemArrayList<>();

    for( String imgId : aImageIds ) {
      Image img = AbstractUIPlugin.imageDescriptorFromPlugin( PLUGIN_ID, imgId ).createImage();
      ((ElemArrayList<Image>)imgList).add( img );
    }

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
    PanelMainSwitch.showDialog( ctx );
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
    GwidList gl = new GwidList();
    gl.add( Gwid.createRtdata( skObject().classId(), skObject().strid(), "rtdOn" ) ); //$NON-NLS-1$
    gl.add( Gwid.createRtdata( skObject().classId(), skObject().strid(), "rtdMainSwitchAlarm" ) ); //$NON-NLS-1$
    gl.add( Gwid.createRtdata( skObject().classId(), skObject().strid(), "rtdReady2Start" ) ); //$NON-NLS-1$
    gl.add( Gwid.createRtdata( skObject().classId(), skObject().strid(), "rtdImitation" ) ); //$NON-NLS-1$
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

    EIrreversibleEngineState state = calcState( values );
    switch( state ) {
      case ON:
        setImageIndex( 0 );
        mainEngine.setActiveImageIdx( 0 );
        break;
      case OFF:
        setImageIndex( 1 );
        mainEngine.setActiveImageIdx( 1 );
        break;
      case UNKNOWN:
        setImageIndex( 1 );
        mainEngine.setActiveImageIdx( 1 );
        setBkColor( colorMagenta );
        break;
      case FAULT:
        setImageIndex( 2 );
        mainEngine.setActiveImageIdx( 2 );
        break;
      default:
        throw new TsNotAllEnumsUsedRtException();
    }

    StringBuilder tooltipStr = new StringBuilder().append( state.nmName() );

    IAtomicValue val = values.getByKey( "rtdReady2Start" ); //$NON-NLS-1$
    if( val != null && val.isAssigned() && !val.asBool() ) {
      // setBkColor( colorDarkGray );
      setFgColor( colorRed );
      tooltipStr.append( "/нет готовности" );
    }
    val = values.getByKey( "rtdImitation" ); //$NON-NLS-1$
    if( val != null && val.isAssigned() && val.asBool() ) {
      setFgColor( colorImitation );
      tooltipStr.append( "/имитация" );
    }
    setTooltipText( tooltipStr.toString() );
    schemePanel().redraw( bounds.x, bounds.y, bounds.width, bounds.height, false );
  }

  EIrreversibleEngineState calcState( IStringMap<IAtomicValue> aValuesMap ) {
    IAtomicValue alarm = aValuesMap.getByKey( "rtdMainSwitchAlarm" ); //$NON-NLS-1$
    if( alarm != null && alarm.isAssigned() && alarm.asBool() ) {
      return EIrreversibleEngineState.FAULT;
    }

    IAtomicValue on = aValuesMap.getByKey( "rtdOn" ); //$NON-NLS-1$
    if( !on.isAssigned() ) {
      return EIrreversibleEngineState.UNKNOWN;
    }

    if( on.asBool() ) {
      return EIrreversibleEngineState.ON;
    }
    return EIrreversibleEngineState.OFF;
  }

}
