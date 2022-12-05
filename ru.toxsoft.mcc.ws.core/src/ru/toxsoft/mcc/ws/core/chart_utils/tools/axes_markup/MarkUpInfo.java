package ru.toxsoft.mcc.ws.core.chart_utils.tools.axes_markup;

/**
 * Информация о настройке видимой части шкалы.
 * <p>
 * Содержит:
 * <ul>
 * <li>начальное значение шкалы</li>
 * <li>шаг шкалы</li>
 * <li>количество шагов</li>
 * </ul>
 * Конечное значение шкалы определяется как начальное значение плюс произведение шага шкалы на количество шагов
 *
 * @author vs
 * @author ND
 */
public class MarkUpInfo {

  /**
   * Начальное значение шкалы.
   */
  public double bgnValue;
  /**
   * Шаг шкалы - дельта между большими засечками.
   */
  public double step;
  /**
   * Количество больших тиков.
   */
  public int    qttyOfSteps;

  /**
   * Конструктор копирования
   *
   * @param aSample - образец. Если null, то устанавливаются некие "значения по умолчанию", особого смысла не имеющие
   */
  public MarkUpInfo( MarkUpInfo aSample ) {
    if( aSample != null ) {
      set( aSample.bgnValue, aSample.step, aSample.qttyOfSteps );
    }
    else {
      // Вариант - поднять исключение
      set( 0, 20, 5 );
    }
  }

  /**
   * Конструктор с явным заданием полей.
   *
   * @param aStart - Начальное значение шкалы.
   * @param aStep - Шаг шкалы - дельта между большими засечками.
   * @param aStepQtty - Количество больших тиков.
   */
  public MarkUpInfo( double aStart, double aStep, int aStepQtty ) {
    set( aStart, aStep, aStepQtty );
  }

  /**
   * Конструктор по умолчанию. Поля инициализируются непротиворечивыми значениями, которые никакого сакрального смысла
   * не имеют.
   */
  protected MarkUpInfo() {
    set( 0, 20, 5 );
  }

  /**
   * Установка всех полей
   *
   * @param aStart - Начальное значение шкалы.
   * @param aStep - Шаг шкалы - дельта между большими засечками.
   * @param aStepQtty - Количество больших тиков.
   */
  public void set( double aStart, double aStep, int aStepQtty ) {
    bgnValue = aStart;
    step = aStep;
    qttyOfSteps = aStepQtty;
  }

}
