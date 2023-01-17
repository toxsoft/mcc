package ru.toxsoft.mcc.ws.mnemos.app.controls;

import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.graphics.*;
import org.toxsoft.core.tslib.utils.errors.*;

public class MccBaloon {

  boolean disposed = false;

  Path baloonPath;

  Pattern gradientPattern;

  public MccBaloon( float aX, float aY, float aWidth, float aHeight, float aArcW, float aArcH, float aNoseLength,
      float aNoseWidth, ETsFulcrum aFulcrum ) {

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

    Path nosePath = new Path( Display.getDefault() );

    switch( aFulcrum ) {
      case LEFT_CENTER:
        break;
      case TOP_CENTER:
        break;
      case RIGHT_CENTER:
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

    baloonPath.addPath( nosePath );
    nosePath.dispose();

    Color c1 = new Color( new RGB( 64, 0, 128 ) );
    Color c2 = new Color( new RGB( 255, 0, 128 ) );

    gradientPattern = new Pattern( Display.getDefault(), aX, aY, aX + aWidth, aY + aHeight, c1, c2 );

  }

  public void paint( GC aGc ) {
    aGc.setBackgroundPattern( gradientPattern );
    aGc.fillPath( baloonPath );
    // aGc.drawPath( baloonPath );
  }

  public void dispose() {
    if( !disposed ) {
      disposed = true;
      baloonPath.dispose();
      gradientPattern.dispose();
    }
  }

}
