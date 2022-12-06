package ru.toxsoft.mcc.ws.mnemos.app.controls;

import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tslib.bricks.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.gw.gwid.*;
import org.toxsoft.core.tslib.utils.errors.*;

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

  /**
   * Цвет фона (заливки описывающего прямоугольника)
   */
  Color bkColor = null;

  /**
   * Цвет границы описывающего прямоугольника
   */
  Color fgColor = null;

  LineAttributes lineAttrs = new LineAttributes( 3 );

  private boolean animated = false;

  private final IRealTimeSensitive animationHandler = aGwTime -> {
    onAnimationStep();
  };

  protected AbstractMultiImageControl( MccSchemePanel aOwner, Gwid aObjGwid, ITsGuiContext aTsContext ) {
    super( aOwner, aObjGwid, aTsContext, null );
  }

  @Override
  public void paint( GC aGc ) {
    if( images == null ) {
      images = listImages();
      updateBounds();
    }
    if( bkColor != null ) {
      aGc.setBackground( bkColor );
      aGc.fillRectangle( bounds );
    }
    if( fgColor != null ) {
      aGc.setLineAttributes( lineAttrs );
      aGc.setForeground( fgColor );
      aGc.drawRectangle( bounds );
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
  // to use
  //

  protected void setImageIndex( int aIndex ) {
    if( images == null ) {
      images = listImages();
      updateBounds();
    }
    TsIllegalArgumentRtException.checkTrue( aIndex < 0 || aIndex >= images.size() );
    imageIndex = aIndex;
  }

  /**
   * Задает цвет фона, которым закрашивется описывающий прямоугольник. Если <b>null</b>, то фон не рисуется.
   *
   * @param aColor Color - цвет заливки описывающего прямоугольника
   */
  protected void setBkColor( Color aColor ) {
    bkColor = aColor;
  }

  /**
   * Задает цвет границы описывающего прямоугольника. Если <b>null</b>, то граница не рисуется.
   *
   * @param aColor Color - цвет границы описывающего прямоугольника
   */
  protected void setFgColor( Color aColor ) {
    fgColor = aColor;
  }

  /**
   * Задает атрибуты границы описывающего прямоугольника.
   *
   * @param aAttrs LineAttributes - атрибуты границы описывающего прямоугольника
   */
  protected void setLineAttrubutes( LineAttributes aAttrs ) {
    lineAttrs = aAttrs;
  }

  // ------------------------------------------------------------------------------------
  // to implement
  //

  protected abstract IList<Image> listImages();

  void startAnimation( int[] aImageIndexes ) {
    animationIndexes = aImageIndexes;
    animationIndex = 0;
    imageIndex = animationIndexes[animationIndex];
    animated = true;
    guiTimersService().slowTimers().addListener( animationHandler );

  }

  void stopAnimation( int aImageIndex ) {
    animated = false;
    guiTimersService().slowTimers().removeListener( animationHandler );
    imageIndex = aImageIndex;
    animationIndexes = null;
    animationIndex = -1;
  }

  // ------------------------------------------------------------------------------------
  // Implementation
  //

  private void onAnimationStep() {
    if( animated ) {
      animationIndex = (animationIndex + 1) % animationIndexes.length;
      imageIndex = animationIndexes[animationIndex];
      schemePanel().redraw( bounds.x, bounds.y, bounds.width, bounds.height, false );
      // System.out.println( "Animation step: " + imageIndex ); //$NON-NLS-1$
    }
  }

  private void updateBounds() {
    ImageData id = images.get( imageIndex ).getImageData();
    bounds.x = x();
    bounds.y = y();
    bounds.width = id.width;
    bounds.height = id.height;
  }

}
