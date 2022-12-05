package ru.toxsoft.mcc.ws.core.chart_utils.tools.aux_types;

//@formatter:off
/**
* Причины, по которым описание оси не подходит
* для заданных параметров данных
* @author apr
*
*/
public enum Reason {
/**
* Диапазон оси
*/
RANGE,
/**
* Слищком узкий диапазон
* (уточнение для {@link #RANGE})
*/
NARROW_RANGE,
/**
* Слищком широкий диапазон
* (уточнение для {@link #RANGE})
*/
WIDE_RANGE,
/**
* Границы шкалы
*/
BOUNDS,
/**
* Не подходит нижняя граница
* (уточнение для {@link #BOUNDS})
*/
LOWER_BOUND,
/**
* Не подходит верхняя граница
* (уточнение для {@link #BOUNDS})
*/
UPPER_BOUND,
/**
* Неудачный выбор тиков шкалы
*/
TICKS,
/**
* Слишком короткие тики
* (уточнение для {@link #TICKS})
*/
SHORT_TICK,
/**
* Слишком длинные тики
* (уточнение для {@link #TICKS})
*/
LONG_TICK,
/**
* Неудачное число тиков шкалы
*/
QTTY,
/**
* Мало тиков
* (уточнение для {@link #QTTY})
*/
FEW_QTTY,
/**
* Много тиков
* (уточнение для {@link #QTTY})
*/
MANY_QTTY
}
//@formatter:on