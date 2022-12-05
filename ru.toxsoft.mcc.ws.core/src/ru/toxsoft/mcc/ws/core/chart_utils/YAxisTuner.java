package ru.toxsoft.mcc.ws.core.chart_utils;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.chart.api.*;
import org.toxsoft.core.tsgui.chart.impl.*;
import org.toxsoft.core.tsgui.chart.renderers.*;
import org.toxsoft.core.tsgui.graphics.*;
import org.toxsoft.core.tsgui.graphics.fonts.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.utils.*;

/**
 * Настройщик Y шкалы.
 * <p>
 * Класс специально был назван Tuner, а не Builder, чтобы не раздражать Гогу.<br>
 * <b>Мотивация:</b> Использование IOptionSet в качестве хранилища настроек - делает настройку параметров шкалы
 * неоправданно трудоемким и исключительно неудобочитаемым. Поэтому был создан публичный класс, который позволяет
 * настраивать парметры шкалы эффективно, в привычном виде.
 *
 * @author vs
 * @author dima // ts4 conversion
 */
public class YAxisTuner {

  IOptionSetEdit annoOps;
  IG2Params      annoParams;

  double startVal = 0;
  double endVal   = 100;
  double stepVal  = 10;

  String                      title            = TsLibUtils.EMPTY_STRING;
  ETsOrientation              titleOrientation = ETsOrientation.HORIZONTAL;
  String                      formatStr        = TsLibUtils.EMPTY_STRING;
  IFontInfo                   titleFontInfo    = null;
  private final ITsGuiContext context;

  /**
   * @param aContext контекст приложения
   */
  public YAxisTuner( ITsGuiContext aContext ) {
    context = aContext;
    annoOps = new OptionSet();
    String annoRendererClass = IStdG2AxisAnnotationRendererOptions.CONSUMER_NAME;
    annoParams = G2ChartUtils.createParams( annoRendererClass, annoOps, context );
  }

  public IYAxisDef createAxisDef( String aId, String aDescription, String aName ) {

    String annoRendererClass = IStdG2AxisAnnotationRendererOptions.CONSUMER_NAME;
    String bkgRendererClass = IGradientBackgroundRendererOptions.CONSUMER_NAME;

    IStdG2AxisAnnotationRendererOptions.ANNOTATION_FORMAT.setValue( annoOps, AvUtils.avStr( formatStr ) );
    IStdG2AxisAnnotationRendererOptions.TITLE.setValue( annoOps, AvUtils.avStr( title ) );
    IStdG2AxisAnnotationRendererOptions.TITLE_ORIENTATION.setValue( annoOps, AvUtils.avValobj( titleOrientation ) );
    if( titleFontInfo != null ) {
      IStdG2AxisAnnotationRendererOptions.TITLE_FONT_INFO.setValue( annoOps, AvUtils.avValobj( titleFontInfo ) );
    }
    annoParams = G2ChartUtils.createParams( annoRendererClass, annoOps, context );

    IOptionSetEdit rendererOps = new OptionSet();
    IOptionSetEdit bkgRendererOps = new OptionSet();
    IStdG2AxisRendererOptions.BACKGROUND_RENDERER_CLASS.setValue( rendererOps, AvUtils.avStr( bkgRendererClass ) );
    IGradientBackgroundRendererOptions.HORIZONTAL.setValue( bkgRendererOps, AvUtils.avBool( true ) );
    IStdG2AxisRendererOptions.BACKGROUND_RENDERER_OPS.setValue( rendererOps, AvUtils.avValobj( bkgRendererOps ) );

    IStdG2AxisRendererOptions.ANNOTATION_RENDERER_CLASS.setValue( rendererOps, AvUtils.avStr( annoRendererClass ) );
    IStdG2AxisRendererOptions.ANNOTATION_RENDERER_OPS.setValue( rendererOps, AvUtils.avValobj( annoParams.params() ) );

    IG2Params rendererParams =
        G2ChartUtils.createParams( IStdG2AxisRendererOptions.CONSUMER_NAME, rendererOps, context );
    return G2ChartUtils.createYAxisDef( aId, aDescription, aName, rendererParams, startVal, endVal, stepVal );
  }

  public void setStartValue( double aValue ) {
    startVal = aValue;
  }

  public void setEndValue( double aValue ) {
    endVal = aValue;
  }

  public void setStepValue( double aValue ) {
    stepVal = aValue;
  }

  public void setTitle( String aTitle ) {
    title = aTitle;
  }

  public void setTitleOrientation( ETsOrientation aOrientation ) {
    titleOrientation = aOrientation;
  }

  public void setTitleFont( IFontInfo aFontInfo ) {
    titleFontInfo = aFontInfo;
  }

  public void setFormatString( String aFormatStr ) {
    formatStr = aFormatStr;
  }
}
