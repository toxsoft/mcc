package ru.toxsoft.mcc.ws.mnemos.app.controls;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.graphics.*;
import org.toxsoft.core.tsgui.graphics.colors.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;

public class MccBaloon
    implements ITsGuiContextable {

  boolean disposed = false;

  Path baloonPath;
  Path nosePath;
  Path noseShadowPath;

  Pattern gradientPattern   = null;
  Pattern noseShadowPattern = null;

  Color colorBlack;

  // MccMultiRowLabel mrLabel;

  private Pair<Color, Color> bkColors;

  private final ITsGuiContext tsContext;

  int shadowDepth = 6;

  /**
   * Длина носика
   */
  private int noseLength;

  /**
   * Ширина носика
   */
  private int noseWidth;

  /**
   * Расположение носика
   */
  private ETsFulcrum noseFulcrum;

  public MccBaloon( float aX, float aY, float aWidth, float aHeight, float aArcW, float aArcH, float aNoseLength,
      float aNoseWidth, ETsFulcrum aFulcrum, ITsGuiContext aContext ) {

    tsContext = aContext;

    noseLength = (int)aNoseLength;
    noseWidth = (int)aNoseWidth;
    noseFulcrum = aFulcrum;

    // mrLabel = new MccMultiRowLabel( aContext );
    // mrLabel.setTextPlacementArea( new Rectangle( (int)aX, (int)aY, (int)aWidth, (int)aHeight ) );
    // mrLabel.setText( new StringArrayList( "МФ фильтр", "в", "норме" ) );

    ITsColorManager colorManager = aContext.get( ITsColorManager.class );
    colorBlack = colorManager.getColor( ETsColor.BLACK );

    baloonPath = new Path( Display.getDefault() );
    float cx = aX;
    float cy = aY;
    baloonPath.addArc( cx, cy, aArcW, aArcH, -180, -90 );
    // baloonPath.lineTo( (float)(aWidth - aArcW / 2.), 0 );
    cx = aX + aWidth - aArcW;
    baloonPath.addArc( cx, cy, aArcW, aArcH, 90, -90 );
    // baloonPath.lineTo( aWidth, (float)(aHeight - aArcH / 2.) );

    cy = aY + aHeight - aArcH;
    baloonPath.addArc( cx, cy, aArcW, aArcH, 0, -90 );

    // baloonPath.lineTo( (float)(aArcW / 2.), aHeight );

    cx = aX;
    cy = aY + aHeight - aArcH;
    baloonPath.addArc( cx, cy, aArcW, aArcH, -90, -90 );

    baloonPath.close();

    nosePath = new Path( getDisplay() );
    noseShadowPath = new Path( getDisplay() );

    switch( aFulcrum ) {
      case LEFT_CENTER:
        nosePath.moveTo( aX, aY + (float)(aHeight / 2. - aNoseWidth / 2.) );
        nosePath.lineTo( aX - aNoseLength, aY + aHeight / 2 );
        nosePath.lineTo( aX, aY + (float)(aHeight / 2. + aNoseWidth / 2.) );
        nosePath.close();
        break;

      case TOP_CENTER:
        nosePath.moveTo( aX + (float)(aWidth / 2. - aNoseWidth / 2.), aY );
        nosePath.lineTo( aX + (float)(aWidth / 2.), aY - aNoseLength );
        nosePath.lineTo( aX + (float)(aWidth / 2. + aNoseWidth / 2.), aY );
        nosePath.close();
        break;

      case RIGHT_CENTER:
        nosePath.moveTo( aX + aWidth, aY + (float)(aHeight / 2. - aNoseWidth / 2.) );
        nosePath.lineTo( aX + aWidth + aNoseLength, aY + aHeight / 2 );
        nosePath.lineTo( aX + aWidth, aY + (float)(aHeight / 2. + aNoseWidth / 2.) );
        nosePath.close();
        break;

      case BOTTOM_CENTER:
        nosePath.moveTo( aX + (float)(aWidth / 2. - aNoseWidth / 2.), aY + aHeight );
        nosePath.lineTo( aX + (float)(aWidth / 2.), aY + aHeight + aNoseLength );
        nosePath.lineTo( aX + (float)(aWidth / 2. + aNoseWidth / 2.), aY + aHeight );
        nosePath.close();
        break;

      case CENTER:
      case LEFT_BOTTOM:
      case LEFT_TOP:
      case RIGHT_BOTTOM:
      case RIGHT_TOP:
      default:
        throw new TsNotAllEnumsUsedRtException();
    }

    Color c1 = new Color( new RGB( 64, 0, 128 ) );
    Color c2 = new Color( new RGB( 255, 0, 128 ) );
    bkColors = new Pair<>( c1, c2 );

    gradientPattern = new Pattern( Display.getDefault(), aX, aY, aX + aWidth, aY + aHeight, c1, c2 );

    // float r[] = new float[4];
    // noseShadowPath.getBounds( r );
    // noseShadowPattern =
    // new Pattern( Display.getDefault(), r[0], r[1], r[0] + r[2], r[1] + r[3], colorBlack, 255, colorBlack, 0 );
    // shadowPattern =
    // new Pattern( Display.getDefault(), aX + 5, aY + 5, aX + 5 + aWidth, aY + 5 + aHeight, c1, 255, c2, 0 );

    update();
  }

  // ------------------------------------------------------------------------------------
  // ITsGuiContextable
  //

  @Override
  public ITsGuiContext tsContext() {
    return tsContext;
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Задает градиент балона.
   *
   * @param aColors Pair - градиент балона
   */
  void setBkColors( Pair<Color, Color> aColors ) {
    bkColors = new Pair<>( aColors.left(), aColors.right() );
    update();
  }

  /**
   * Задает глубину тени.
   *
   * @param aShadowDepth int - глубина тени в пикселях
   */
  void setShadowDepth( int aShadowDepth ) {
    shadowDepth = aShadowDepth;
    update();
  }

  /**
   * Осуществляет отрисовку, используя переданный контекст.
   *
   * @param aGc GC - графический контекст
   */
  public void paint( GC aGc ) {

    // отрисуем тень посредством последовательной отрисовки контура со смещение на 1 по x и y
    aGc.setAdvanced( true );
    aGc.setAntialias( SWT.ON );

    Transform tr = new Transform( aGc.getDevice() );

    aGc.setForeground( colorBlack );
    aGc.setLineWidth( 2 );
    aGc.drawPath( baloonPath );

    int alpha = 125;
    for( int i = 0; i < 6; i++ ) {
      tr.translate( 1, 1 ); // смещаемся на 1 вправо и на 1 вниз
      aGc.setTransform( tr );
      aGc.setAlpha( alpha );

      aGc.drawPath( baloonPath );

      alpha -= 20;
    }
    tr.dispose();

    aGc.setTransform( null );
    aGc.setAlpha( 255 );

    aGc.setBackgroundPattern( noseShadowPattern ); // нарисуем тень для носика
    aGc.fillPath( noseShadowPath );

    // заполним контур цветом - отдельно балон отдельно носик
    aGc.setBackgroundPattern( gradientPattern );
    aGc.fillPath( baloonPath );
    aGc.fillPath( nosePath );

    // mrLabel.paint( aGc );

    // aGc.drawRectangle( bounds() );
  }

  /**
   * Освобождает системные ресурсы.
   */
  public void dispose() {
    if( !disposed ) {
      disposed = true;
      baloonPath.dispose();
      nosePath.dispose();
      noseShadowPath.dispose();
      gradientPattern.dispose();
      noseShadowPattern.dispose();
    }
  }

  /**
   * Вовзращает описывающий прямоугольник (включая носик)
   *
   * @return Rectangle описывающий прямоугольник (включая носик), но без тени
   */
  public Rectangle bounds() {
    float[] br = new float[4]; // rect for baloon
    baloonPath.getBounds( br );
    br[2] += br[0];
    br[3] += br[1];

    float[] nr = new float[4]; // rect for nose
    nosePath.getBounds( nr );
    nr[2] += nr[0];
    nr[3] += nr[1];

    int x1 = (int)Math.min( br[0], nr[0] );
    int y1 = (int)Math.min( br[1], nr[1] );
    int x2 = (int)Math.max( br[2], nr[2] );
    int y2 = (int)Math.max( br[3], nr[3] );

    return new Rectangle( x1, y1, x2 - x1, y2 - y1 );
  }

  // ------------------------------------------------------------------------------------
  // Implementation
  //

  private void update() {
    if( gradientPattern != null && !gradientPattern.isDisposed() ) {
      gradientPattern.dispose();
    }

    Rectangle rr = bounds();
    gradientPattern =
        new Pattern( getDisplay(), rr.x, rr.y, rr.x + rr.width, rr.y + rr.height, bkColors.left(), bkColors.right() );

    updateNoseShadow();
  }

  private void updateNoseShadow() {
    if( noseShadowPath != null && !noseShadowPath.isDisposed() ) {
      noseShadowPath.dispose();
    }
    noseShadowPath = new Path( getDisplay() );

    if( noseShadowPattern != null && !noseShadowPattern.isDisposed() ) {
      noseShadowPattern.dispose();
    }

    float r[] = new float[4];
    baloonPath.getBounds( r );

    float x = (int)r[0];
    float y = (int)r[1];
    float width = (int)r[2];
    float height = (int)r[3];

    int sd = shadowDepth;

    switch( noseFulcrum ) {
      case LEFT_CENTER: {
        noseShadowPath.moveTo( x, y + (float)(height / 2. - noseWidth / 2.) );
        noseShadowPath.lineTo( x - noseLength, y + height / 2 );
        noseShadowPath.lineTo( x + sd, y + (float)(height / 2. + noseWidth / 2.) + sd + sd );
        noseShadowPath.close();
        noseShadowPath.getBounds( r );
        float h = r[1] + r[3];
        noseShadowPattern = new Pattern( getDisplay(), r[0], r[1], r[0], h, colorBlack, 255, colorBlack, 0 );
        return;
      }

      case TOP_CENTER: {
        noseShadowPath.moveTo( x + (float)(width / 2. - noseWidth / 2.), y );
        noseShadowPath.lineTo( x + (float)(width / 2.), y - noseLength );
        noseShadowPath.lineTo( x + (float)(width / 2. + noseWidth / 2.) + sd + sd, y );
        noseShadowPath.close();
        noseShadowPath.getBounds( r );
        float w = r[0] + r[2];
        noseShadowPattern = new Pattern( getDisplay(), r[0], r[1], w + sd, r[1], colorBlack, 255, colorBlack, 0 );
        return;
      }

      case RIGHT_CENTER: {
        noseShadowPath.moveTo( x + width, y + (float)(height / 2. - noseWidth / 2.) );
        noseShadowPath.lineTo( x + width + noseLength, y + height / 2 );
        noseShadowPath.lineTo( x + width - sd, y + (float)(height / 2. + noseWidth / 2.) + sd + sd );
        noseShadowPath.close();
        noseShadowPath.getBounds( r );
        float h = r[1] + r[3];
        noseShadowPattern = new Pattern( getDisplay(), r[0], r[1], r[0], h, colorBlack, 255, colorBlack, 0 );
        return;
      }

      case BOTTOM_CENTER: {
        noseShadowPath.moveTo( x + (float)(width / 2. - noseWidth / 2.), y + height );
        noseShadowPath.lineTo( x + (float)(width / 2.), y + height + noseLength );
        noseShadowPath.lineTo( x + (float)(width / 2. + noseWidth / 2.) + sd + sd, y + height );
        noseShadowPath.close();
        noseShadowPath.getBounds( r );
        float w = r[0] + r[2];
        noseShadowPattern = new Pattern( getDisplay(), r[0], r[1], w + sd, r[1], colorBlack, 255, colorBlack, 0 );
        return;
      }

      case CENTER:
      case LEFT_BOTTOM:
      case LEFT_TOP:
      case RIGHT_BOTTOM:
      case RIGHT_TOP:
      default:
        throw new TsNotAllEnumsUsedRtException();
    }
  }

}
