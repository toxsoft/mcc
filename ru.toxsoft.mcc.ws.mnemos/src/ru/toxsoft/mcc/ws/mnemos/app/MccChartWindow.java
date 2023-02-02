package ru.toxsoft.mcc.ws.mnemos.app;

import org.eclipse.swt.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tslib.gw.gwid.*;

/**
 * Окно для отображения графика.
 *
 * @author vs
 */
public class MccChartWindow {

  private final Shell parent;

  private Shell wnd = null;

  public MccChartWindow( Shell aParent, int aX, int aY, Gwid aRtGwid, ITsGuiContext aTsContext, String aTitle,
      String aDescription ) {
    parent = aParent;

    wnd = new Shell( parent, SWT.BORDER | SWT.CLOSE | SWT.RESIZE );
    wnd.setText( aTitle );

    wnd.addDisposeListener( aE -> {
      // TODO Auto-generated method stub

    } );

    wnd.setLayout( new FillLayout() );

    wnd.pack();
    wnd.setSize( 600, 400 );
    wnd.setLocation( aX, aY );
  }

  public void show() {
    wnd.open();
  }
}
