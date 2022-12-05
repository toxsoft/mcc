package ru.toxsoft.mcc.ws.core.chart_utils;

import org.toxsoft.core.tsgui.chart.api.*;
import org.toxsoft.core.tslib.bricks.strid.*;
import org.toxsoft.core.tslib.utils.*;

/**
 * Контейнер данных одного графика
 *
 * @author vs
 * @author dima // ts4 conversion
 */
public class GraphicInfo {

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
