package ru.toxsoft.mcc.ws.mnemos.app.controls;

import static ru.toxsoft.mcc.ws.mnemos.Activator.*;

import org.eclipse.swt.graphics.*;
import org.eclipse.ui.plugin.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.core.tslib.gw.gwid.*;
import org.toxsoft.uskat.core.api.objserv.*;

import ru.toxsoft.mcc.ws.mnemos.app.*;
import ru.toxsoft.mcc.ws.mnemos.app.dialogs.*;

/**
 * Главный двигатель нагнетателя (проект МосКокс).
 * <p>
 *
 * @author vs
 */
public class MccMainEngineControl
    extends AbstractMultiImageControl {

  final IList<Image> imgList;

  /**
   * Коструктор.
   *
   * @param aOwner MccSchemePanel - родительская панель мнемосхемы
   * @param aObjGwid Gwid - конкретный ИД объекта
   * @param aImageIds IStringList - список ИДов изображений
   * @param aTsContext ITsGuiContext - соответствующий контекст
   */
  public MccMainEngineControl( MccSchemePanel aOwner, Gwid aObjGwid, IStringList aImageIds, ITsGuiContext aTsContext ) {
    super( aOwner, aObjGwid, aTsContext );

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
    Gwid gwid = Gwid.createObj( "mcc.MainSwitch", "n2MainSwitch" ); //$NON-NLS-1$ //$NON-NLS-2$
    ISkObject mainSwitch = coreApi().objService().find( gwid.skid() );
    // MccDialogContext ctx = new MccDialogContext( tsContext(), skObject() );
    // PanelIrreversibleEngine.showDialog( ctx );
    MccDialogContext ctx = new MccDialogContext( tsContext(), mainSwitch );
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
    // gl.add( Gwid.createRtdata( skObject().classId(), skObject().strid(), "rtdOn" ) ); //$NON-NLS-1$
    // gl.add( Gwid.createRtdata( skObject().classId(), skObject().strid(), "rtdAlarm" ) ); //$NON-NLS-1$
    // gl.add( Gwid.createRtdata( skObject().classId(), skObject().strid(), "rtdEnabled" ) ); //$NON-NLS-1$
    // gl.add( Gwid.createRtdata( skObject().classId(), skObject().strid(), "rtdImitation" ) ); //$NON-NLS-1$
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
  // API
  //

  public void setActiveImageIdx( int aIdx ) {
    setImageIndex( aIdx );
  }

  // ------------------------------------------------------------------------------------
  // Implementation
  //

  void update() {
    //
  }
}
