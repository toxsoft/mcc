package ru.toxsoft.mcc.ws.core.chart_utils.tools.axes_markup;

import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

import ru.toxsoft.mcc.ws.core.chart_utils.tools.aux_types.*;

/**
 * Базовый класс для настройщиков описания оси графика. Настройка - полностью автоматическая. Критерии: </br>
 * <li>TODO: Диапазон изменения данных составляет 20-50% шкалы</li>
 * <li>По возможности начало или конец шкалы совпадает с нулём</li>
 * <li></li>
 * <li></li>
 *
 * @author apr
 */
public class AxisMarkupTuner
    implements IAxisMarkupTuner {

  /**
   * По умолчанию: Минимальный диапазон изменения данных в видимой области (проценты)
   */
  static final double DFLT_RANGE_PERCENTS_MIN = 20;
  /**
   * По умолчанию: Максимальный диапазон изменения данных в видимой области (проценты)
   */
  static final double DFLT_RANGE_PERCENTS_MAX = 50;

  /**
   * Минимальный диапазон изменения данных в видимой области (проценты)
   */
  protected double range_precents_min;
  /**
   * Максимальный диапазон изменения данных в видимой области (проценты)
   */
  protected double range_precents_max;

  /**
   * Список по умолчанию приемлемых описаний осей. Описания заданы для данных в стандартной форме (см.
   * {@link StdDoubleDsc}).
   */
  private static IListEdit<MarkUpInfo> acceptableAxeDflt = null;

  /**
   * Рабочий список приемлемых описаний осей. Описания заданы для данных в стандартной форме (см.{@link StdDoubleDsc}).
   */
  protected IList<MarkUpInfo> acceptableAxe = null;

  /**
   * Конструктор по умолчанию
   */
  public AxisMarkupTuner() {
    range_precents_min = DFLT_RANGE_PERCENTS_MIN;
    range_precents_max = DFLT_RANGE_PERCENTS_MAX;
    initAcceptableAxes();
  }

  /**
   * Конструктор с заданием диапазона изменения данных в процентах от полнойш шкалы.
   *
   * @param aMinRange - Минимальный диапазон изменения данных в видимой области [% шкалы]
   * @param aMaxRange - Максимальный диапазон изменения данных в видимой области [% шкалы]
   */
  public AxisMarkupTuner( double aMinRange, double aMaxRange ) {
    range_precents_min = aMinRange;
    range_precents_max = aMaxRange;
    initAcceptableAxes();
  }

  @Override
  public MarkUpInfo tuneAxisMarkup( double aStartValue, double aEndValue, int aMinQtty, int aMaxQtty ) {
    // защита от дурака
    double bgn = aStartValue, end = aEndValue;
    if( end < bgn ) {
      end = bgn;
      bgn = aEndValue;
    }
    int minQtty = aMinQtty, maxQtty = aMaxQtty;
    if( minQtty > maxQtty ) {
      minQtty = maxQtty;
      maxQtty = aMinQtty;
    }

    // Диапазон изменения данных в стандартной форме
    StdDoubleDsc dataRange = new StdDoubleDsc( end - bgn );
    double delta = dataRange.abs;
    // Максимальное значение
    StdDoubleDsc dataMax = new StdDoubleDsc( end );
    double max = dataMax.abs;
    // Минимальное значение
    StdDoubleDsc dataMin = new StdDoubleDsc( bgn );
    double min = dataMin.abs;
    if( min > max ) {
      double tmp = min;
      min = max;
      max = tmp;
      StdDoubleDsc tmpDsc = dataMin;
      dataMin = dataMax;
      dataMax = tmpDsc;
    }

    // Диапазон шкалы данных в стандартной форме
    // StdDoubleDsc axeRange = new StdDoubleDsc( max >= delta ? max : max + min );
    // double range = axeRange.abs;

    if( dataRange.sign == 0 ) {
      // Случай, когда bgn == end
      return markupConst( dataMax, minQtty, maxQtty );
    }
    if( max < delta ) {
      // Случай, когда bgn и end имеют разные знаки
      return markupDiffSign( dataMin, dataMax, minQtty, maxQtty );
    }
    // Штатный вариант - bgn и end имеют одинаковые знаки
    return markupSameSign( dataMin, dataMax, minQtty, maxQtty );
  }

  /**
   * Уствновить рабочий список приемлемых описаний осей.
   *
   * @param anAcceptableAxes - список описаний, заданных для данных в стандартной форме {@link StdDoubleDsc}. То есть к
   *          описанию будет примеряться исключительно величины, лежащие в диапазоне [10..100). Функцию имеет смысл
   *          использовать только когда стандартный набор описаний (см.{@link #initAcceptableAxes()}) не удлвлетворяет
   *          пользьзователя.
   */
  protected void setAcceptableAxeList( IList<MarkUpInfo> anAcceptableAxes ) {
    acceptableAxe = new ElemArrayList<>( anAcceptableAxes );
  }

  /**
   * Инициализация статического списка разумных наборов параметров осей для данных, представленных в стандартном формате
   * {@link StdDoubleDsc} и установка его в качестве рабочего списка.
   */
  private void initAcceptableAxes() {
    if( acceptableAxeDflt == null ) {
      acceptableAxeDflt = new ElemArrayList<>();
      // для norm = 10..12
      acceptableAxeDflt.add( new MarkUpInfo( 0, 2.5, 5 ) );
      // для norm = 12..14
      acceptableAxeDflt.add( new MarkUpInfo( 0, 2.5, 6 ) );
      // для norm = 14..18
      acceptableAxeDflt.add( new MarkUpInfo( 0, 5, 4 ) );
      // для norm = 18..20
      acceptableAxeDflt.add( new MarkUpInfo( 0, 5, 5 ) );
      // для norm = 20..25
      acceptableAxeDflt.add( new MarkUpInfo( 0, 5, 6 ) );
      // для norm = 25..45
      acceptableAxeDflt.add( new MarkUpInfo( 0, 10, 5 ) );
      // для norm = 45..65
      acceptableAxeDflt.add( new MarkUpInfo( 0, 20, 4 ) );
      // для norm = 65..90
      acceptableAxeDflt.add( new MarkUpInfo( 0, 20, 5 ) );
      // для max = 90..100
      acceptableAxeDflt.add( new MarkUpInfo( 0, 25, 5 ) );
    }

    setAcceptableAxeList( acceptableAxeDflt );
  }

  /**
   * Служебная функция. Выбирает набор характеристик оси из списка стандартных наборов.
   *
   * @param aConst - значение данного
   * @return - описание оси
   */
  private MarkUpInfo stdDscForConst( StdDoubleDsc aConst ) {
    // Максимум + ~10%
    double maxPlus10 = 1.1 * aConst.norm;
    for( MarkUpInfo info : acceptableAxe ) {
      double infoRange = info.step * info.qttyOfSteps;
      if( infoRange >= maxPlus10 ) {
        return new MarkUpInfo( info );
      }
    }
    // ??? Этого не может быть: norm < 100!
    throw new TsIllegalArgumentRtException( "AxisMarkupTuner.stdDscForConst(StdDoubleDsc)" ); //$NON-NLS-1$
  }

  /**
   * Подобрать характеристики оси для графика константы. для случая, когда диапазон данных нулевой или близко к этому.
   *
   * @param aConst - константа
   * @param minQtty - минимально допустимое число тиков. Рассматривается как рекомендация.
   * @param maxQtty - максимально допустимое число тиков Рассматривается как рекомендация.
   * @return описание оси
   */
  protected MarkUpInfo markupConst( StdDoubleDsc aConst, int minQtty, int maxQtty ) {
    MarkUpInfo info = stdDscForConst( aConst );
    if( info.qttyOfSteps > maxQtty ) {
      // Не хочется заморачиваться с точным подбором соотношения шаг/число шагов
      // Просто предполагаем, что maxQtty < 2-3 - это уже клиника
      info.step = Math.scalb( info.step, 1 );
      info.qttyOfSteps = (info.qttyOfSteps + 1) / 2;
    }
    if( info.qttyOfSteps < minQtty ) {
      // Не хочется заморачиваться с точным подбором соотношения шаг/число шагов
      // Хотели - получите!
      info.qttyOfSteps = minQtty;
    }

    // Для отрицательного числа перевернём ось
    if( aConst.sign < 0 ) {
      info.bgnValue = -info.step * info.qttyOfSteps;
    }
    // Восстанавливаем степень
    double p = Math.pow( 10, aConst.power );
    info.bgnValue = info.bgnValue * p;
    info.step = info.step * p;
    return info;
  }

  /**
   * Подбор оси для штатного случая - min и max имеют одинаковые знаки
   *
   * @param arg1 - минимум данных
   * @param arg2 - максимум данных
   * @param minQtty - минимально допустимое число тиков
   * @param maxQtty - максимально допустимое число тиков
   * @return описание оси
   */
  private MarkUpInfo markupSameSign( StdDoubleDsc arg1, StdDoubleDsc arg2, int minQtty, int maxQtty ) {
    // Пока не обращаем внимания на критерий "достаточно заметного размаха"
    StdDoubleDsc max = (arg1.abs >= arg2.abs ? arg1 : arg2);
    return markupConst( max, minQtty, maxQtty );
  }

  /**
   * Подбор оси для случая, когда min и max имеют разные знаки
   *
   * @param arg1 - минимум данных
   * @param arg2 - максимум данных
   * @param minQtty - минимально допустимое число тиков
   * @param maxQtty - максимально допустимое число тиков
   * @return описание оси
   */
  private MarkUpInfo markupDiffSign( StdDoubleDsc arg1, StdDoubleDsc arg2, int minQtty, int maxQtty ) {
    StdDoubleDsc min, max;
    // Защита от дурака
    if( arg2.value() >= arg1.value() ) {
      min = arg1;
      max = arg2;
    }
    else {
      min = arg2;
      max = arg1;
    }

    // Стандартное представление 110% от диапазона данных
    double d = 1.1 * (max.abs + min.abs);
    StdDoubleDsc range = new StdDoubleDsc( d );
    // Стандартное описание оси для этого диапазона
    MarkUpInfo info = markupConst( range, minQtty, maxQtty );

    // Конвертация описания диапазона данных в описание оси графика

    // Подстройка начала шкалы
    double absOfMin = min.abs + 0.5 * info.step;
    int negQtty = (int)Math.ceil( absOfMin / info.step );
    info.bgnValue = -negQtty * info.step;

    // Увеличим при необходимости число шагов шкалы
    double axeMax = info.bgnValue + info.step * info.qttyOfSteps;
    if( axeMax < max.value() ) {
      info.qttyOfSteps++;
    }

    return info;
  }

  static IMapEdit<Reason, Double> scale = null;

  void initScale() {
    if( scale != null ) {
      return;
    }
    scale = new ElemMap<>();
    // Пока все причины отклонений равноправны
    scale.put( Reason.RANGE, Double.valueOf( 25 ) );
    scale.put( Reason.BOUNDS, Double.valueOf( 25 ) );
    scale.put( Reason.TICKS, Double.valueOf( 25 ) );
    scale.put( Reason.QTTY, Double.valueOf( 25 ) );
    // Нормируем вес причин под диапазон 0..100
    IList<Reason> keyList = scale.keys();
    IList<Double> weightList = scale.values();
    double sum = sumDoubleList( weightList );

    for( int i = 0, N = keyList.size(); i < N; i++ ) {
      double val = 100. * weightList.get( i ).doubleValue() / sum;
      scale.put( keyList.get( i ), Double.valueOf( val ) );
    }
  }

  protected double sumDoubleList( IList<Double> aList ) {
    double sum = 0;
    for( Double d : aList ) {
      sum += d.doubleValue();
    }
    return sum;
  }
}
