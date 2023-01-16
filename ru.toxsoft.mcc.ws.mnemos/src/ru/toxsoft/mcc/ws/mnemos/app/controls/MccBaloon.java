package ru.toxsoft.mcc.ws.mnemos.app.controls;

import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;

public class MccBaloon {

  boolean disposed = false;

  Path baloonPath;

  public MccBaloon( float aWidth, float aHeight, float aArcW, float aArcH, float aNoseLength, float aNoseWidth ) {

    baloonPath = new Path( Display.getDefault() );
    float cx = (float)(aArcW / 2.);
    float cy = (float)(aArcH / 2.);
    cx = 0;
    cy = 0;
    baloonPath.addArc( cx, cy, aArcW, aArcH, -180, -90 );
    // baloonPath.lineTo( (float)(aWidth - aArcW / 2.), 0 );
    cx = aWidth - aArcW;
    baloonPath.addArc( cx, cy, aArcW, aArcH, 90, -90 );
    // baloonPath.lineTo( aWidth, (float)(aHeight - aArcH / 2.) );

    cy = aHeight - aArcH;
    baloonPath.addArc( cx, cy, aArcW, aArcH, 0, -90 );

    // baloonPath.lineTo( (float)(aArcW / 2.), aHeight );

    cx = 0;
    cy = aHeight - aArcH;
    baloonPath.addArc( cx, cy, aArcW, aArcH, -90, -90 );

    baloonPath.close();
  }

  public void paint( GC aGc ) {
    aGc.drawPath( baloonPath );
  }

  public void dispose() {
    if( !disposed ) {
      disposed = true;
      baloonPath.dispose();
    }
  }

}
