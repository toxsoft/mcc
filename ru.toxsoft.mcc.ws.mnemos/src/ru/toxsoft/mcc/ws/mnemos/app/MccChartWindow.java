package ru.toxsoft.mcc.ws.mnemos.app;

import org.eclipse.swt.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.panels.*;
import org.toxsoft.core.tsgui.utils.layout.*;
import org.toxsoft.core.tslib.gw.gwid.*;

import ru.toxsoft.mcc.ws.core.chart_utils.*;
import ru.toxsoft.mcc.ws.core.templates.utils.*;

/**
 * Окно для отображения графика.
 *
 * @author vs
 */
public class MccChartWindow {

  private final Shell parent;

  private Shell wnd = null;

  /**
   * Конструктор для создания всплывающего графика
   *
   * @param aParent родитель
   * @param aX координата по X
   * @param aY координата по Y
   * @param aRtGwid параметр на графике
   * @param aTsContext контекст
   * @param aTitle заголовок графика
   * @param aDescription описание графика
   */
  public MccChartWindow( Shell aParent, int aX, int aY, Gwid aRtGwid, ITsGuiContext aTsContext, String aTitle,
      String aDescription ) {
    parent = aParent;

    wnd = new Shell( parent, SWT.BORDER | SWT.CLOSE | SWT.RESIZE );
    wnd.setText( aTitle );

    wnd.addDisposeListener( aE -> {
      // nop
    } );

    wnd.setLayout( new FillLayout() );
    TsPanel backPanel = new TsPanel( wnd, aTsContext );
    backPanel.setLayout( new BorderLayout() );
    // for debug
    // s5.Node[mcc.server]$rtdata(s5.node.statistic.LoadAverage.min)
    // aRtGwid = Gwid.createRtdata( "s5.Node", "mcc.server", "s5.node.statistic.LoadAverage.min" );
    ChartPanel chartPanel = ReportTemplateUtilities.popupChart( aTsContext, backPanel, aRtGwid, aTitle, aDescription );
    chartPanel.setLayoutData( BorderLayout.CENTER );
    wnd.pack();
    wnd.setSize( 900, 500 );
    wnd.setLocation( aX, aY );
  }

  public void show() {
    wnd.open();
  }
}
