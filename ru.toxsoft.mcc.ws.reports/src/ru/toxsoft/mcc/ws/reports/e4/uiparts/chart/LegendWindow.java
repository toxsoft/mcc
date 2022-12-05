package ru.toxsoft.mcc.ws.reports.e4.uiparts.chart;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.chart.api.*;
import org.toxsoft.core.tsgui.chart.renderers.*;
import org.toxsoft.core.tsgui.graphics.colors.*;
import org.toxsoft.core.tslib.bricks.geometry.impl.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;

class LegendWindow {

  Shell         wnd = null;
  final Control parent;
  Canvas        canvas;

  Color normColor;
  Color lowColor;
  Color hiColor;

  final IStridablesList<IPlotDef> plotDefs;

  private final ITsGuiContext context;

  LegendWindow( Control aParent, IStridablesList<IPlotDef> aPlotDefs, ITsGuiContext aContext ) {
    parent = aParent;
    plotDefs = aPlotDefs;
    context = aContext;
    normColor = colorManager().getColor( ETsColor.BLACK );
    lowColor = colorManager().getColor( ETsColor.BLUE );
    hiColor = colorManager().getColor( ETsColor.RED );

    Shell aShell = aParent.getShell();
    wnd = new Shell( aShell, SWT.BORDER | SWT.CLOSE );
    FillLayout layout = new FillLayout();
    wnd.setLayout( layout );
    canvas = new Canvas( wnd, SWT.NONE );
    canvas.addPaintListener( aE -> {
      int x = 24;
      int y = 4;

      Color oldColor = aE.gc.getForeground();
      for( IPlotDef pd : plotDefs ) {
        Point p = aE.gc.textExtent( pd.nmName() );
        aE.gc.drawText( pd.nmName(), x, y, true );
        // dima 04.11.22 ts4 conversion
        // TsLineInfo lineInfo =
        // IStdG2GraphicRendererOptions.GRAPHIC_LINE_INFO.getValue( pd.rendererParams().params() ).asValobj();
        RGBA rgba = IStdG2GraphicRendererOptions.GRAPHIC_RGBA.getValue( pd.rendererParams().params() ).asValobj();
        Color rgbColor = colorManager().getColor( rgba.rgb );

        aE.gc.setForeground( colorManager().getColor( ETsColor.BLACK ) );
        aE.gc.setLineWidth( 8 );
        aE.gc.drawLine( 4, y + p.y / 2, 20, y + p.y / 2 );
        aE.gc.setForeground( rgbColor );

        aE.gc.setLineWidth( 6 );
        aE.gc.drawLine( 5, y + p.y / 2, 19, y + p.y / 2 );
        aE.gc.setForeground( oldColor );
        y += p.y;
      }
    } );

    TsPoint p = computeSize();
    wnd.setSize( p.x(), p.y() );
    setLocation( 100, 100 );
    wnd.open();
  }

  void setLocation( int aX, int aY ) {
    Point p = parent.toDisplay( aX, aY );
    wnd.setLocation( p.x, p.y + 2 );
  }

  void dispose() {
    if( wnd != null && !wnd.isDisposed() ) {
      wnd.dispose();
      wnd = null;
    }
  }

  void refresh() {
    if( wnd != null && !wnd.isDisposed() ) {
      TsPoint p = computeSize();
      wnd.setSize( p.x(), p.y() );
      wnd.redraw();
      // System.out.println( "New value: " + cell.getText() );
    }
  }

  TsPoint computeSize() {
    int width = 0;
    int height = 0;

    GC gc = new GC( Display.getCurrent() );

    for( IPlotDef pd : plotDefs ) {
      Point p = gc.textExtent( pd.nmName() );
      if( p.x > width ) {
        width = p.x;
      }
      height += p.y;
    }

    gc.dispose();
    return new TsPoint( width + 26 + 16, height + 48 );
  }

  public Shell shell() {
    return wnd;
  }

  ITsColorManager colorManager() {
    return context.get( ITsColorManager.class );
  }

}
