package ru.toxsoft.mcc.ws.core.chart_utils.tools.axes_markup;

/**
 * Интерфейс настройщика описания оси
 *
 * @author ND
 */
public interface IAxisMarkupTuner {

  /**
   * Возвращает описание шкалы, пригодное для нормального отображения данных.
   *
   * @param aStartValue double - начальное значение, которое должно быть видимо
   * @param aEndValue double - конечное значение, которое должно быть видимо
   * @param aMinQtty int - минимально допустимое число тиков на видимой части оси (рекомендация)
   * @param aMaxQtty int - максимально допустимое число тиков на видимой части оси (рекомендация)
   * @return {@link MarkUpInfo} - описание видимой части шкалы
   */
  MarkUpInfo tuneAxisMarkup( double aStartValue, double aEndValue, int aMinQtty, int aMaxQtty );

  // apr: Извини, но уж очень неудобно пользоваться Pair
  // в случае простой пары int,int :(
  // * @param aAllowedStepQtty Pair - пара, задающая интервал допустимых значений количества шагов. Левое значение -
  // public MarkUpInfo tuneAxisMarkup( double aStartValue, double aEndValue, Pair<Integer, Integer> aAllowedStepQtty );
}
