package ru.toxsoft.mcc.ws.core.chart_utils;

import org.eclipse.jface.resource.*;
import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.printing.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.ui.plugin.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.chart.api.*;
import org.toxsoft.core.tsgui.chart.impl.*;
import org.toxsoft.core.tsgui.graphics.*;
import org.toxsoft.core.tsgui.graphics.colors.*;
import org.toxsoft.core.tsgui.graphics.fonts.impl.*;
import org.toxsoft.core.tsgui.graphics.lines.*;
import org.toxsoft.core.tsgui.panels.*;
import org.toxsoft.core.tsgui.utils.*;
import org.toxsoft.core.tsgui.utils.layout.*;
import org.toxsoft.core.tsgui.valed.controls.basic.*;
import org.toxsoft.core.tslib.av.temporal.*;
import org.toxsoft.core.tslib.bricks.geometry.impl.*;
import org.toxsoft.core.tslib.bricks.strid.*;
import org.toxsoft.core.tslib.bricks.strid.impl.*;
import org.toxsoft.core.tslib.bricks.time.*;
import org.toxsoft.core.tslib.bricks.time.impl.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.uskat.base.gui.conn.*;
import org.toxsoft.uskat.core.*;
import org.toxsoft.uskat.core.connection.*;

import ru.toxsoft.mcc.ws.core.*;
import ru.toxsoft.mcc.ws.core.chart_utils.console.*;
import ru.toxsoft.mcc.ws.core.chart_utils.tools.axes_markup.*;
import ru.toxsoft.mcc.ws.core.templates.api.*;
import ru.toxsoft.mcc.ws.core.templates.utils.*;

/**
 * Панель для отображения отчета в виде графиков.
 *
 * @author vs
 * @author dima // conversion to ts4
 */
public class ChartPanel
    extends TsPanel {

  public static class GraphicInfo {

    private final IStridable           nameable;
    private final String               axisId;
    private final String               dataSetId;
    private final Pair<Double, Double> minMax;
    private final boolean              ladder;
    private IPlotDef                   plotDef = null;
    private boolean                    visible = true;

    public GraphicInfo( IStridable aNameable, String aAxisId, String aDataSetId, Pair<Double, Double> aMinMax,
        boolean aIsLadder ) {
      nameable = aNameable;
      axisId = aAxisId;
      dataSetId = aDataSetId;
      minMax = aMinMax;
      ladder = aIsLadder;
    }

    public String id() {
      return nameable.id();
    }

    IPlotDef plotDef() {
      return plotDef;
    }

    public Pair<Double, Double> minMax() {
      return minMax;
    }

    boolean isVisibe() {
      return visible;
    }

    void setVisible( boolean aVisible ) {
      visible = aVisible;
    }

    public IPlotDef createPlotDef( PlotDefTuner aPlotTuner ) {
      if( ladder ) {
        aPlotTuner.setRenderingKind( EGraphicRenderingKind.LADDER );
      }
      plotDef = aPlotTuner.createPlotDef( nameable, axisId, dataSetId, "mainCanvas" );
      return plotDef;
    }
  }

  static class YAxisInfo {

    private final String                      id;
    private final Pair<String, String>        unitInfo;
    private final IStringMapEdit<GraphicInfo> graphicInfoes = new StringMap<>();

    private Double min = null;
    private Double max = null;

    YAxisInfo( String aId, Pair<String, String> aUnitInfo ) {
      id = aId;
      unitInfo = aUnitInfo;
    }

    String id() {
      return id;
    }

    IStringMapEdit<GraphicInfo> graphicInfoes() {
      return graphicInfoes;
    }

    void putGraphicInfo( GraphicInfo aGraphInfo ) {
      Pair<Double, Double> minMax = aGraphInfo.minMax();
      if( min == null || min > minMax.left() ) {
        min = minMax.left();
      }
      if( max == null || max < minMax.right() ) {
        max = minMax.right();
      }
      graphicInfoes.put( aGraphInfo.id(), aGraphInfo );
    }

    Pair<String, String> unitInfo() {
      return unitInfo;
    }
  }

  Button btnPageLeft;
  Button btnStepLeft;
  Button btnStepRight;
  Button btnPageRight;
  Button btnSelect;

  Button btnVisir;
  Button btnLegend;

  Button btnConsole;
  Button btnPrint;

  G2Chart                  chart        = null;
  ETimeUnit                axisTimeUnit = null;
  G2ChartConsole           console      = null;
  private ISkGraphTemplate template     = null;
  final ISkCoreApi         serverApi;

  ValedComboSelector<ETimeUnit> timeUnitCombo;

  final IStringMapEdit<GraphicInfo> graphicInfoes = new StringMap<>();
  final IStringMapEdit<YAxisInfo>   axisInfoes    = new StringMap<>();
  LegendWindow                      legendWindow  = null;
  ConsoleWindow                     consoleWindow = null;

  /**
   * Конструктор панели
   *
   * @param aParent панель родителя
   * @param aContext контекст приложения
   */
  public ChartPanel( Composite aParent, ITsGuiContext aContext ) {
    super( aParent, aContext );
    setLayout( new BorderLayout() );

    ISkConnectionSupplier connSup = tsContext().get( ISkConnectionSupplier.class );
    ISkConnection conn = connSup.defConn();
    serverApi = conn.coreApi();
    createToolBar();
  }

  void createToolBar() {

    String pluginId = Activator.PLUGIN_ID;
    ImageDescriptor imgDescr;

    Composite comp = new Composite( this, SWT.NONE );
    // comp.setLayout( new RowLayout() );
    comp.setLayout( new GridLayout( 20, false ) );
    comp.setLayoutData( BorderLayout.NORTH );

    btnPageLeft = new Button( comp, SWT.PUSH );
    btnPageLeft.setToolTipText( "Экран назад" );
    imgDescr = AbstractUIPlugin.imageDescriptorFromPlugin( pluginId, "icons/is24x24/sdvig_ekran_left.png" ); //$NON-NLS-1$
    btnPageLeft.setImage( imgDescr.createImage() );
    // явно удаляем ранее загруженную картинку
    btnPageLeft.addDisposeListener( aE -> btnPageLeft.getImage().dispose() );

    btnPageLeft.addSelectionListener( new SelectionAdapter() {

      @Override
      public void widgetSelected( SelectionEvent e ) {
        if( console != null && axisTimeUnit != null ) {
          console.locateX( console.getX1() - (console.getX2() - console.getX1()) );
          chart.refresh();
        }
      }
    } );

    btnStepLeft = new Button( comp, SWT.PUSH );
    btnStepLeft.setToolTipText( "Назад" );
    imgDescr = AbstractUIPlugin.imageDescriptorFromPlugin( pluginId, "icons/is24x24/sdvig_shag_left.png" ); //$NON-NLS-1$
    btnStepLeft.setImage( imgDescr.createImage() );
    // явно удаляем ранее загруженную картинку
    btnStepLeft.addDisposeListener( aE -> btnStepLeft.getImage().dispose() );

    btnStepLeft.addSelectionListener( new SelectionAdapter() {

      @Override
      public void widgetSelected( SelectionEvent e ) {
        if( console != null && axisTimeUnit != null ) {
          console.locateX( console.getX1() - axisTimeUnit.timeInMills() );
          chart.refresh();
        }
      }
    } );

    btnStepRight = new Button( comp, SWT.PUSH );
    btnStepRight.setToolTipText( "Вперед" );
    imgDescr = AbstractUIPlugin.imageDescriptorFromPlugin( pluginId, "icons/is24x24/sdvig_shag_right.png" ); //$NON-NLS-1$
    btnStepRight.setImage( imgDescr.createImage() );
    // явно удаляем ранее загруженную картинку
    btnStepRight.addDisposeListener( aE -> btnStepRight.getImage().dispose() );

    btnStepRight.addSelectionListener( new SelectionAdapter() {

      @Override
      public void widgetSelected( SelectionEvent e ) {
        if( chart != null && axisTimeUnit != null ) {
          console.locateX( console.getX1() + axisTimeUnit.timeInMills() );
          chart.refresh();
        }
      }
    } );

    btnPageRight = new Button( comp, SWT.PUSH );
    btnPageRight.setToolTipText( "Экран вперед" );
    imgDescr = AbstractUIPlugin.imageDescriptorFromPlugin( pluginId, "icons/is24x24/sdvig_ekran_right.png" ); //$NON-NLS-1$
    btnPageRight.setImage( imgDescr.createImage() );
    // явно удаляем ранее загруженную картинку
    btnPageRight.addDisposeListener( aE -> btnPageRight.getImage().dispose() );

    btnPageRight.addSelectionListener( new SelectionAdapter() {

      @Override
      public void widgetSelected( SelectionEvent e ) {
        if( console != null && axisTimeUnit != null ) {
          console.locateX( console.getX1() + (console.getX2() - console.getX1()) );
          chart.refresh();
        }
      }
    } );

    btnSelect = new Button( comp, SWT.PUSH );
    btnSelect.setText( "Выбор" );
    imgDescr = AbstractUIPlugin.imageDescriptorFromPlugin( pluginId, "icons/is24x24/grafic_list.png" ); //$NON-NLS-1$
    btnSelect.setImage( imgDescr.createImage() );
    // явно удаляем ранее загруженную картинку
    btnSelect.addDisposeListener( aE -> btnSelect.getImage().dispose() );

    btnSelect.addSelectionListener( new SelectionAdapter() {

      @Override
      public void widgetSelected( SelectionEvent e ) {
        IListEdit<IPlotDef> visiblePlots = new ElemArrayList<>();
        IListEdit<IPlotDef> hiddenPlots = new ElemArrayList<>();

        for( GraphicInfo graphInfo : graphicInfoes ) {
          if( graphInfo.visible ) {
            visiblePlots.add( graphInfo.plotDef );
          }
          else {
            hiddenPlots.add( graphInfo.plotDef );
          }
        }

        Pair<IList<IPlotDef>, IList<IPlotDef>> result;
        result = PanelPlotSelection.selectPlots( getShell(), new Pair<>( visiblePlots, hiddenPlots ), tsContext() );
        if( result != null ) {
          for( GraphicInfo graphInfo : graphicInfoes ) {
            if( result.left().hasElem( graphInfo.plotDef ) ) {
              graphInfo.setVisible( true );
              console.setPlotVisible( graphInfo.plotDef().id(), true );
            }
            else {
              graphInfo.setVisible( false );
              console.setPlotVisible( graphInfo.plotDef().id(), false );
            }
          }
          chart.refresh();
        }
      }
    } );

    btnVisir = new Button( comp, SWT.CHECK );
    btnVisir.setText( "Визир" );
    btnVisir.setSelection( false );
    imgDescr = AbstractUIPlugin.imageDescriptorFromPlugin( pluginId, "icons/is24x24/vizir.png" ); //$NON-NLS-1$
    btnVisir.setImage( imgDescr.createImage() );
    // явно удаляем ранее загруженную картинку
    btnVisir.addDisposeListener( aE -> btnVisir.getImage().dispose() );

    btnVisir.addSelectionListener( new SelectionAdapter() {

      @Override
      public void widgetSelected( SelectionEvent e ) {
        chart.visir().setVisible( btnVisir.getSelection() );
        chart.refresh();
      }
    } );

    btnLegend = new Button( comp, SWT.CHECK );
    btnLegend.setText( "Легенда" );
    imgDescr = AbstractUIPlugin.imageDescriptorFromPlugin( pluginId, "icons/is24x24/legenda_on.png" ); //$NON-NLS-1$
    btnLegend.setImage( imgDescr.createImage() );
    // явно удаляем ранее загруженную картинку
    btnLegend.addDisposeListener( aE -> btnLegend.getImage().dispose() );

    btnLegend.setSelection( false );
    btnLegend.addSelectionListener( new SelectionAdapter() {

      @Override
      public void widgetSelected( SelectionEvent e ) {
        if( !btnLegend.getSelection() && legendWindow != null ) {
          legendWindow.dispose();
          legendWindow = null;
        }
        else {
          legendWindow = new LegendWindow( getParent(), chart.plotDefs(), tsContext() );
          legendWindow.shell().addDisposeListener( aE -> {
            btnLegend.setSelection( false );
            legendWindow = null;
          } );
        }
      }
    } );

    btnConsole = new Button( comp, SWT.CHECK );
    btnConsole.setText( "Пульт" );
    imgDescr = AbstractUIPlugin.imageDescriptorFromPlugin( pluginId, "icons/is24x24/manage_pult.png" ); //$NON-NLS-1$
    btnConsole.setImage( imgDescr.createImage() );
    // явно удаляем ранее загруженную картинку
    btnConsole.addDisposeListener( aE -> btnConsole.getImage().dispose() );

    btnConsole.setSelection( false );
    btnConsole.addSelectionListener( new SelectionAdapter() {

      @Override
      public void widgetSelected( SelectionEvent e ) {
        if( !btnConsole.getSelection() && consoleWindow != null ) {
          consoleWindow.dispose();
          consoleWindow = null;
        }
        else {
          consoleWindow = new ConsoleWindow( getParent(), chart, tsContext() );
          consoleWindow.shell().addDisposeListener( aE -> {
            btnConsole.setSelection( false );
            consoleWindow = null;
          } );
        }
      }
    } );

    btnPrint = new Button( comp, SWT.PUSH );
    btnPrint.setText( "Печать" );
    imgDescr = AbstractUIPlugin.imageDescriptorFromPlugin( pluginId, "icons/is24x24/document-print.png" ); //$NON-NLS-1$
    btnPrint.setImage( imgDescr.createImage() );
    // явно удаляем ранее загруженную картинку
    btnPrint.addDisposeListener( aE -> btnPrint.getImage().dispose() );

    btnPrint.addSelectionListener( new SelectionAdapter() {

      @Override
      public void widgetSelected( SelectionEvent e ) {
        PrintDialog dialog = new PrintDialog( getShell(), SWT.NULL );
        PrinterData printerDefaults = new PrinterData();
        printerDefaults.scope = PrinterData.PAGE_RANGE;
        printerDefaults.orientation = PrinterData.LANDSCAPE;
        dialog.setPrinterData( printerDefaults );

        PrinterData printerData = dialog.open();

        if( printerData == null ) {
          return; // отказ от печати
        }
        Printer printer = new Printer( printerData );
        GC printerGc = createPrintGc( printer, new TsPoint( 5, 5 ), new TsPoint( 5, 5 ) );
        try {
          if( printer.startJob( "printChart" ) ) { //$NON-NLS-1$
            chart.print( printerGc );
            // напечатаем название шаблона
            Point chartSize = chart.getControl().getSize();
            Color oldColor = printerGc.getForeground();
            printerGc.setForeground( colorManager().getColor( ETsColor.BLACK ) );
            String chartTitle = template.title().strip().length() > 0 ? template.title() : template.nmName();
            Point titleSize = printerGc.textExtent( chartTitle );
            printerGc.drawText( chartTitle, chartSize.x / 2 - titleSize.x / 2, (int)(chartSize.y * 0.05), true );
            printerGc.setForeground( oldColor );
            if( legendWindow != null ) {
              // напечатаем еще название шаблона отчетов
              Point p = legendWindow.shell().getLocation();
              p = toControl( p );
              Transform tr = new Transform( printer );
              printerGc.getTransform( tr );
              tr.translate( p.x, p.y );
              printerGc.setTransform( tr );
              tr.dispose();
              legendWindow.print( printerGc );
            }
            printer.endPage();
          }
        }
        finally {
          if( printerGc != null ) {
            printerGc.dispose();
            printer.endJob();
            printer.dispose();
          }
        }
      }
    } );

    CLabel l = new CLabel( comp, SWT.CENTER );
    l.setText( "Цена деления:" );

    IList<ETimeUnit> values = new ElemArrayList<>( ETimeUnit.values() );
    ITsVisualsProvider<ETimeUnit> visualsProvider = ETimeUnit::nmName;
    timeUnitCombo = new ValedComboSelector<>( tsContext(), values, visualsProvider );
    timeUnitCombo.createControl( comp );
    timeUnitCombo.eventer().addListener( ( aSource, aEditFinished ) -> {
      ETimeUnit tu = timeUnitCombo.selectedItem();
      if( tu != null ) {
        axisTimeUnit = tu;
        chart.console().setTimeUnit( tu );
        chart.refresh();
      }
    } );

  }

  void clear() {
    graphicInfoes.clear();
    axisInfoes.clear();
  }

  /**
   * Отобразить набор данных на графической компоненте
   *
   * @param aAnswer - набор данных для отображения
   * @param aTemplate - шаблон описания графика
   */
  public void setReportAnswer( IList<IG2DataSet> aAnswer, ISkGraphTemplate aTemplate ) {

    clear();
    // проверяем что есть смысл строить график
    if( aAnswer.isEmpty() ) {
      return;
    }
    template = aTemplate;
    // создаем компоненту график
    createChart( aAnswer, aTemplate );
    // наполняем ее данными отчета
    Composite chartComp = chart.createControl( this );
    fillChartData( aAnswer, aTemplate );
    createYAxises( chart );
    createPlots( aTemplate );
    chartComp.setLayoutData( BorderLayout.CENTER );
    console = new G2ChartConsole( chart );
  }

  private void fillChartData( IList<IG2DataSet> aAnswer, ISkGraphTemplate aTemplate ) {
    for( int i = 0; i < aAnswer.size(); i++ ) {
      ISkGraphParam param = aTemplate.listParams().get( i );
      IList<ITemporalAtomicValue> values = aAnswer.get( i ).getValues( ITimeInterval.NULL );
      Pair<Double, Double> minMax = calcMinMax( values );

      String graphDataSetId = ReportTemplateUtilities.graphDataSetId( param );

      YAxisInfo axisInfo;
      if( axisInfoes.hasKey( param.unitId() ) ) {
        axisInfo = axisInfoes.getByKey( param.unitId() );
      }
      else {
        axisInfo = new YAxisInfo( graphDataSetId, new Pair<>( param.unitId(), param.unitName() ) );
        axisInfoes.put( param.unitId(), axisInfo );
      }

      chart.dataSets().add( aAnswer.get( i ) );

      IStridable graphStridable = new Stridable( graphDataSetId, param.title(), param.description() );

      GraphicInfo graphInfo =
          new GraphicInfo( graphStridable, axisInfo.id(), graphDataSetId, minMax, param.isLadder() );
      graphicInfoes.put( graphDataSetId, graphInfo );
      axisInfo.putGraphicInfo( graphInfo );
    }
  }

  private void createChart( IList<IG2DataSet> aAnswer, ISkGraphTemplate aTemplate ) {

    TimeAxisTuner tuner = new TimeAxisTuner( tsContext() );
    // настройка шкалы времении
    axisTimeUnit = getAxisTimeUnit( aTemplate );
    timeUnitCombo.setSelectedItem( axisTimeUnit );
    tuner.setTimeUnit( axisTimeUnit );
    // диапазон данных
    IG2DataSet dataSet = aAnswer.first();
    // настройка шкалы времении - диапазон значений
    long startTime = System.currentTimeMillis() - 12 * axisTimeUnit.timeInMills();
    if( !dataSet.getValues( ITimeInterval.NULL ).isEmpty() ) {
      startTime = dataSet.getValues( ITimeInterval.NULL ).first().timestamp();
    }
    long endTime = startTime + 12 * axisTimeUnit.timeInMills();

    tuner.setTimeInterval( new TimeInterval( startTime, endTime ), false );
    IXAxisDef xAxisDef = tuner.createAxisDef();
    chart = (G2Chart)G2ChartUtils.createChart( tsContext() );

    chart.setXAxisDef( xAxisDef );

  }

  // настройка шкалы времении в зависимости от шага агрегации
  private static ETimeUnit getAxisTimeUnit( ISkGraphTemplate aTemplate ) {
    ETimeUnit retVal = ETimeUnit.MIN10;
    ETimeUnit aggrStep = aTemplate.aggrStep();
    retVal = switch( aggrStep ) {
      case DAY -> ETimeUnit.WEEK;
      case HOUR01 -> ETimeUnit.HOUR04;
      case HOUR02 -> ETimeUnit.HOUR08;
      case HOUR04 -> ETimeUnit.HOUR12;
      case HOUR08 -> ETimeUnit.HOUR12;
      case HOUR12 -> ETimeUnit.DAY;
      case MIN01 -> ETimeUnit.MIN10;
      case MIN05 -> ETimeUnit.MIN15;
      case MIN10 -> ETimeUnit.HOUR01;
      case MIN15 -> ETimeUnit.HOUR01;
      case MIN20 -> ETimeUnit.HOUR01;
      case MIN30 -> ETimeUnit.HOUR02;
      case SEC01 -> ETimeUnit.SEC10;
      case SEC02 -> ETimeUnit.SEC20;
      case SEC03 -> ETimeUnit.SEC30;
      case SEC05 -> ETimeUnit.MIN01;
      case SEC10 -> ETimeUnit.MIN01;
      case SEC15 -> ETimeUnit.MIN01;
      case SEC20 -> ETimeUnit.MIN01;
      case SEC30 -> ETimeUnit.MIN05;
      case WEEK -> ETimeUnit.HOUR12;
      case YEAR -> ETimeUnit.WEEK;
    };
    return retVal;
  }

  /**
   * Вычисление min & max диапазона значений
   *
   * @param aValues значения выборки с сервиса данных
   * @return пара значений {@link Pair}
   */
  Pair<Double, Double> calcMinMax( IList<ITemporalAtomicValue> aValues ) {

    if( aValues.size() > 0 ) {
      double max = Double.MIN_VALUE;
      double min = Double.MAX_VALUE;
      // счетчик присвоенных значений
      int assignedValuesCounter = 0;
      for( ITemporalAtomicValue value : aValues ) {
        if( !(value.value().isAssigned()) ) {
          continue;
        }
        assignedValuesCounter++;
        double dVal = value.value().asDouble();
        if( dVal < min ) {
          min = dVal;
        }
        if( dVal > max ) {
          max = dVal;
        }
      }

      if( min > 0 ) {
        min = 0;
      }

      min = Math.ceil( min );

      return assignedValuesCounter >= 2 ? new Pair<>( Double.valueOf( min ), Double.valueOf( max ) )
          : new Pair<>( Double.valueOf( 0 ), Double.valueOf( 10 ) );
    }
    return new Pair<>( Double.valueOf( 0 ), Double.valueOf( 10 ) );
  }

  void createYAxises( IG2Chart aChart ) {
    for( YAxisInfo axisInfo : axisInfoes ) {
      double min = axisInfo.graphicInfoes().values().get( 0 ).minMax().left().doubleValue();
      double max = axisInfo.graphicInfoes().values().get( 0 ).minMax().right().doubleValue();
      for( GraphicInfo graphInfo : axisInfo.graphicInfoes() ) {
        if( graphInfo.minMax().left().doubleValue() < min ) {
          min = graphInfo.minMax().left().doubleValue();
        }
        if( graphInfo.minMax().right().doubleValue() > max ) {
          max = graphInfo.minMax().right().doubleValue();
        }
      }
      IYAxisDef yAxisDef = createYAxisDef( axisInfo.id(), min, max, "%.1f", axisInfo.unitInfo() );
      aChart.yAxisDefs().add( yAxisDef );
    }
  }

  IYAxisDef createYAxisDef( String aId, double aMin, double aMax, String aFormatStr, Pair<String, String> aUnitInfo ) {
    YAxisTuner yTuner = new YAxisTuner( tsContext() );

    // yTuner.setStartValue( aMin );
    // yTuner.setEndValue( aMax );
    yTuner.setFormatString( aFormatStr );

    double val = Math.abs( (aMax - aMin) / 10 );
    // int exp = calcExponent( val );

    AxisMarkupTuner mt = new AxisMarkupTuner( aMin, aMax );
    MarkUpInfo mi = mt.tuneAxisMarkup( aMin, aMax, 5, 15 );

    yTuner.setStartValue( mi.bgnValue );
    yTuner.setEndValue( mi.bgnValue + mi.step * mi.qttyOfSteps );
    yTuner.setStepValue( mi.step );

    yTuner.setTitle( aUnitInfo.right() );
    yTuner.setTitleOrientation( ETsOrientation.VERTICAL );
    yTuner.setTitleFont( new FontInfo( "Arial", 18, false, false ) );

    return yTuner.createAxisDef( aId, aUnitInfo.right(), aUnitInfo.left() );
  }

  void createPlots( ISkGraphTemplate aTemplate ) {
    int idx = 0;
    for( GraphicInfo graphInfo : graphicInfoes ) {
      PlotDefTuner plotTuner = new PlotDefTuner( tsContext() );
      RGB plotColor = aTemplate.listParams().get( idx ).color().rgb();
      int lineWidth = aTemplate.listParams().get( idx ).lineWidth();
      EDisplayFormat displayFormat = aTemplate.listParams().get( idx ).displayFormat();
      plotTuner.setLineInfo( TsLineInfo.ofWidth( lineWidth ) );
      plotTuner.setRGBA( new RGBA( plotColor.red, plotColor.green, plotColor.blue, 255 ) );
      plotTuner.setDisplayFormat( displayFormat );
      ISkGraphParam graphParam = aTemplate.listParams().get( idx );
      plotTuner.setSetPointsList( graphParam.setPoints() );
      IPlotDef plotDef = graphInfo.createPlotDef( plotTuner );
      chart.plotDefs().add( plotDef );
      idx++;
    }
  }

  /**
   * Обновляем всю компоненту
   */
  public void refresh() {
    chart.refresh();
  }

  /**
   * Возвращает подготовленный для печати графический контекст.
   * <p>
   *
   * @param aPrinter Printer - принтер
   * @param aLtMargins TsPoint - левый и верхний отступы в миллиметрах
   * @param aRbMargins TsPoint - правый и нижний отступы в миллиметрах
   * @return GC - подготовленный для печати графический контекст
   */
  private GC createPrintGc( Printer aPrinter, TsPoint aLtMargins, TsPoint aRbMargins ) {
    GC gc = new GC( aPrinter );

    Point printerDpi = aPrinter.getDPI();
    double dpp = Math.min( printerDpi.x / 24.5, printerDpi.y / 24.5 );
    int marginLeft = (int)(aLtMargins.x() * dpp);
    int marginTop = (int)(aLtMargins.y() * dpp);
    int marginRight = (int)(aRbMargins.x() * dpp);
    int marginBottom = (int)(aRbMargins.y() * dpp);

    Rectangle pr = aPrinter.getClientArea();
    pr.width -= (marginLeft + marginRight);
    pr.height -= (marginTop + marginBottom);

    Point cs = chart.getControl().getSize();

    float k = Math.min( (float)pr.width / cs.x, (float)pr.height / cs.y );

    Transform tr = new Transform( aPrinter );
    tr.translate( marginLeft, marginTop );
    tr.scale( k, k );
    gc.setTransform( tr );
    tr.dispose();

    return gc;
  }
}
