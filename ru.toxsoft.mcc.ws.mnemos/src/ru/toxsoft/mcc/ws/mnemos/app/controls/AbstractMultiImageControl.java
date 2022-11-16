package ru.toxsoft.mcc.ws.mnemos.app.controls;

import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tslib.bricks.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.gw.gwid.*;

import ru.toxsoft.mcc.ws.mnemos.app.*;

/**
 * Базовый класс элементов, имеющих перечислимое количество состояний, каждое из которых отображается своим изобржением.
 *
 * @author vs
 */
public abstract class AbstractMultiImageControl
    extends AbstractMccSchemeControl {

  IList<Image> images = null;

  Rectangle bounds = new Rectangle( 0, 0, 0, 0 );

  int imageIndex = 0;

  /**
   * Набор индексов изображений при анимации
   */
  int[] animationIndexes = null;

  int animationIndex = 0;

  private final IRealTimeSensitive animationHandler = aGwTime -> {
    onAnimationStep();
  };

  protected AbstractMultiImageControl( MccSchemePanel aOwner, Gwid aObjGwid, ITsGuiContext aTsContext ) {
    super( aOwner, aObjGwid, aTsContext );
  }

  @Override
  public void paint( GC aGc ) {
    if( images == null ) {
      images = listImages();
      updateBounds();
    }
    aGc.drawImage( images.get( imageIndex ), x(), y() );
  }

  @Override
  public Rectangle bounds() {
    if( images == null ) {
      images = listImages();
    }
    updateBounds();
    return bounds;
  }

  @Override
  public void dispose() {
    for( Image image : images ) {
      image.dispose();
    }
  }

  // ------------------------------------------------------------------------------------
  // to implement
  //

  protected abstract IList<Image> listImages();

  void startAnimation( int[] aImageIndexes ) {
    animationIndexes = aImageIndexes;
    animationIndex = 0;
    imageIndex = animationIndexes[animationIndex];
    guiTimersService().slowTimers().addListener( animationHandler );

  }

  void stopAnimation( int aImageIndex ) {
    guiTimersService().slowTimers().removeListener( animationHandler );
    imageIndex = aImageIndex;
    animationIndexes = null;
    animationIndex = -1;
  }

  // ------------------------------------------------------------------------------------
  // Implementation
  //

  private void onAnimationStep() {
    animationIndex = (animationIndex + 1) % animationIndexes.length;
    imageIndex = animationIndexes[animationIndex];
    schemePanel().redraw( bounds.x, bounds.y, bounds.width, bounds.height, false );
    // System.out.println( "Animation step: " + imageIndex ); //$NON-NLS-1$
  }

  private void updateBounds() {
    ImageData id = images.get( imageIndex ).getImageData();
    bounds.x = x();
    bounds.y = y();
    bounds.width = id.width;
    bounds.height = id.height;
  }

}
