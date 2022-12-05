package ru.toxsoft.mcc.ws.core.chart_utils.tools.aux_types;

/**
 * Представление double в форме, удобной для подбора описания шкалы:<br>
 * value = sign * norm * 10^power, где
 * <li>sign - знак {-1,0,1}</li>
 * <li>norm - псевдомантисса [10..100)</li>
 * <li>power - степень числа 10</li>
 *
 * @author apr
 */
public class StdDoubleDsc {

  /**
   * Модуль числа
   */
  public double abs;
  /**
   * псевдомантисса (10..100)
   */
  public double norm;
  /**
   * степень числа 10, на которую нужно уможить norm, чтобы получить модуль value
   */
  public int    power;
  /**
   * Знак числа (+/-1,0)
   */
  public int    sign;

  /**
   * Конструктор с инициализацией
   *
   * @param aValue - значение для инициализации
   */
  public StdDoubleDsc( double aValue ) {
    init( aValue );
  }

  /**
   * Реинициализирует объект по значению аргумента
   *
   * @param aValue - новое значение
   */
  public void init( double aValue ) {
    sign = (aValue > 0 ? 1 : (aValue < 0 ? -1 : 0));
    switch( sign ) {
      case 1:
        abs = aValue;
        break;
      case -1:
        abs = -aValue;
        break;
      // case 0: // Особый случай
      default:
        abs = 0;
        norm = 10;
        power = 0;
        return;
    }

    double log = Math.log10( abs );
    double logFloor = Math.floor( log ) - 1;
    // d = 10^{logFloor + [1..2) }
    norm = Math.pow( 10, log - logFloor );
    power = (int)logFloor;
    return;
  }

  /**
   * Возвращает само число
   *
   * @return double - само число
   */
  public double value() {
    return (sign > 0 ? abs : (sign < 0 ? -abs : 0));
  }
}
