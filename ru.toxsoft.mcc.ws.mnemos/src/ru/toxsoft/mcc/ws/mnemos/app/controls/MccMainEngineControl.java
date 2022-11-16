package ru.toxsoft.mcc.ws.mnemos.app.controls;

import static ru.toxsoft.mcc.ws.mnemos.Activator.*;

import org.eclipse.swt.graphics.*;
import org.eclipse.ui.plugin.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.gw.gwid.*;

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

  public MccMainEngineControl( MccSchemePanel aOwner, Gwid aObjGwid, IStringList aImageIds, ITsGuiContext aTsContext ) {
    super( aOwner, aObjGwid, aTsContext );

    imgList = new ElemArrayList<>();

    for( String imgId : aImageIds ) {
      Image img = AbstractUIPlugin.imageDescriptorFromPlugin( PLUGIN_ID, imgId ).createImage();
      ((ElemArrayList<Image>)imgList).add( img );
    }
    // startAnimation( new int[] { 0, 1, 2 } );
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
    PanelIrreversibleEngine.showDialog( ctx );
  }

}
