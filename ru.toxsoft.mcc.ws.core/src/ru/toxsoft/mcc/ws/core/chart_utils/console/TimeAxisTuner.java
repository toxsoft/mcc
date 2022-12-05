package ru.toxsoft.mcc.ws.core.chart_utils.console;

import static org.toxsoft.core.tsgui.chart.renderers.IStdG2TimeAxisAnnotationRendererOptions.*;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.chart.api.*;
import org.toxsoft.core.tsgui.chart.impl.*;
import org.toxsoft.core.tsgui.chart.renderers.*;
import org.toxsoft.core.tsgui.graphics.fonts.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.bricks.time.impl.*;

/**
 * Настройщик временной шкалы.
 * <p>
 * Класс специально был назван Tuner, а не Builder, чтобы не раздражать Гогу.<br>
 * <b>Мотивация:</b> Использование IOptionSet в качестве хранилища настроек - делает настройку параметров шкалы
 * неоправданно трудоемким и исключительно неудобочитаемым. Поэтому был создан публичный класс, который позволяет
 * настраивать парметры шкалы эффективно, в привычном виде.
 *
 * @author vs
 * @author dima // ts4 conversion
 */
public class TimeAxisTuner {

  IOptionSetEdit annoOps;
  IG2Params      annoParams;
  ETimeUnit      timeUnit = ETimeUnit.HOUR01;

  TimeInterval                timeInterval;
  AxisMarkingDef              markingDef = null;// new AxisMarkingDef();
  private final ITsGuiContext context;

  /**
   * @param aContext котнекст приложения
   */
  public TimeAxisTuner( ITsGuiContext aContext ) {
    context = aContext;
    annoOps = new OptionSet();
    String annoRendererClass = IStdG2TimeAxisAnnotationRendererOptions.CONSUMER_NAME;
    annoParams = G2ChartUtils.createParams( annoRendererClass, annoOps, context );
    long time = System.currentTimeMillis();
    timeInterval = new TimeInterval( time, time + 10 * 3600 * 1000 );
  }

  public void setTimeInterval( TimeInterval aInterval, boolean aAutoFitTimeUnit ) {
    timeInterval = aInterval;
    if( aAutoFitTimeUnit ) {
      for( ETimeUnit tu : ETimeUnit.values() ) {
        long qtty = aInterval.duration() / tu.timeInMills();
        if( (qtty > 3 && qtty <= 10) || (qtty > 12) ) {
          timeUnit = tu;
          break;
        }
      }
    }
  }

  public void setDateFormat( String aFormat ) {
    DATE_FORMAT.setValue( annoOps, AvUtils.avStr( aFormat ) );
  }

  public void setTimeFormat( String aFormat ) {
    TIME_FORMAT.setValue( annoOps, AvUtils.avStr( aFormat ) );
  }

  public void setDateWrap( boolean aWrap ) {
    DATE_WRAP.setValue( annoOps, AvUtils.avBool( aWrap ) );
  }

  public void setTimeUnit( ETimeUnit aTimeUnit ) {
    timeUnit = aTimeUnit;
  }

  public ETimeUnit timeUnit() {
    return timeUnit;
  }

  public void setFont( IFontInfo aFontInfo ) {
    FONT_INFO.setValue( annoOps, AvUtils.avValobj( aFontInfo ) );
  }

  public IXAxisDef createAxisDef() {

    String annoRendererClass = IStdG2TimeAxisAnnotationRendererOptions.CONSUMER_NAME;
    if( timeUnit.timeInMills() < 60000 ) {
      TIME_FORMAT.setValue( annoOps, AvUtils.avStr( "%1$tH:%1$tM:%1tS" ) );
    }
    annoParams = G2ChartUtils.createParams( annoRendererClass, annoOps, context );

    IOptionSetEdit rendererOps = new OptionSet();
    IStdG2AxisRendererOptions.ANNOTATION_RENDERER_CLASS.setValue( rendererOps, AvUtils.avStr( annoRendererClass ) );
    IStdG2AxisRendererOptions.ANNOTATION_RENDERER_OPS.setValue( rendererOps, AvUtils.avValobj( annoParams.params() ) );
    IG2Params rendererParams =
        G2ChartUtils.createParams( IStdG2AxisRendererOptions.CONSUMER_NAME, rendererOps, context );
    if( markingDef == null ) {
      markingDef = new AxisMarkingDef( 12, 7, 3, timeUnit.midTicksQtty(), timeUnit.littTicksQtty() );
    }

    return G2ChartUtils.createXAxisDef( rendererParams, timeInterval.startTime(), timeInterval.endTime(), timeUnit,
        markingDef );
  }

}
