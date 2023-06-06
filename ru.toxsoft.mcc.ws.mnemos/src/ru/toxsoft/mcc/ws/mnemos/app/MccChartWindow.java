package ru.toxsoft.mcc.ws.mnemos.app;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.toxsoft.core.tsgui.bricks.ctx.ITsGuiContext;
import org.toxsoft.core.tslib.gw.gwid.Gwid;
import org.toxsoft.skf.reports.chart.utils.gui.panels.ChartPanel;
import org.toxsoft.skf.reports.gui.utils.ReportTemplateUtilities;
import org.toxsoft.skf.reports.templates.service.IVtGraphTemplate;
import org.toxsoft.uskat.core.gui.conn.ISkConnectionSupplier;

import ru.toxsoft.mcc.ws.mnemos.app.rt.chart.RtChartPanel;

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
    SashForm sf = new SashForm( wnd, SWT.HORIZONTAL );
    // left part
    // TsPanel backPanel = new TsPanel( wnd, aTsContext );
    // backPanel.setLayout( new BorderLayout() );
    // for debug
    // s5.Node[mcc.server]$rtdata(s5.node.statistic.LoadAverage.min)
    // aRtGwid = Gwid.createRtdata( "s5.Node", "mcc.server", "s5.node.statistic.LoadAverage.min" );
    aRtGwid = Gwid.createRtdata( aRtGwid.classId(), aRtGwid.strid(), "rtdCurrentValue" );
    ChartPanel chartPanel = ReportTemplateUtilities.popupChart( aTsContext, sf, aRtGwid, aTitle, aDescription );
    // chartPanel.setLayoutData( BorderLayout.CENTER );
    RtChartPanel rtChartPanel = popupRtChart( aTsContext, sf, aRtGwid, aTitle, aDescription );
    // rtChartPanel.setLayoutData( BorderLayout.SOUTH );
    wnd.pack();
    wnd.setSize( 1000, 500 );
    wnd.setLocation( aX, aY );
  }

  public void show() {
    wnd.open();
  }

  /**
   * Создает RTChart панель для графика одного параметра
   *
   * @param aContext контекст
   * @param aParent родительский компонент
   * @param aParamGwid параметр отображаемый на графике
   * @param aTitle название параметра
   * @param aDescription описание параметра
   * @return панель для графика
   */
  public static RtChartPanel popupRtChart( ITsGuiContext aContext, Composite aParent, Gwid aParamGwid, String aTitle,
      String aDescription ) {
    IVtGraphTemplate selTemplate = ReportTemplateUtilities.createTemplate( aParamGwid, aTitle, aDescription );
    ISkConnectionSupplier connSupp = aContext.get( ISkConnectionSupplier.class );

    // создаем новую панель
    RtChartPanel popupRtChart = new RtChartPanel( aParent, aContext, selTemplate, connSupp.defConn() );
    return popupRtChart;
  }

}
