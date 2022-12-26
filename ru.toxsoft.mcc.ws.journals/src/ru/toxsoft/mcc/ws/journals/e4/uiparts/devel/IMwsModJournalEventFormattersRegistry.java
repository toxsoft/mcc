package ru.toxsoft.mcc.ws.journals.e4.uiparts.devel;

import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.gw.gwid.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Реестр форматировщиков текста для отображения событий.
 * <p>
 * Этот модуль (плагин) работы с журналами использует ссылку на этот реестр, который должен находится в контексте
 * приложения. Если модель при старте не обнаруживает ссылку в контексте, то он сам создает и размещает в контекст.
 * Форматировщики из этого реестра используются для отображения и печати событий в журналах.
 *
 * @author goga
 */
public interface IMwsModJournalEventFormattersRegistry {

  /**
   * Находит форматировщик для запрошенного события.
   *
   * @param aClassId String - идентификатор класса
   * @param aEventId String - идентификатор события
   * @return {@link IMwsModJournalEventFormatter} - найденный форматировщик или <code>null</code>
   */
  IMwsModJournalEventFormatter find( String aClassId, String aEventId );

  /**
   * Возвращает все зарегистрированные форматировщики.
   *
   * @return {@link IMap}&lt;{@link Gwid},{@link IMwsModJournalEventFormatter}&gt; - карта Gwid - форматировщик
   */
  IMap<Gwid, IMwsModJournalEventFormatter> formattersMap();

  /**
   * Регистрирует форматировщик.
   * <p>
   * Заменяет существующий форматировщик.
   *
   * @param aEventGwid {@link Gwid} - UGWI события регистрируемого форматировщика
   * @param aFormatter {@link IMwsModJournalEventFormatter} - регистрируемый форматировщик
   * @throws TsNullArgumentRtException любой аргумент = <code>null</code>
   * @throws TsIllegalArgumentRtException аргумент-GWID не имеет тип {@link EGwidKind#GW_EVENT}
   */
  void registerFomatter( Gwid aEventGwid, IMwsModJournalEventFormatter aFormatter );

  /**
   * Удаляет зарегистрированный форматировщик.
   * <p>
   * Если под запрошенным GWID форматировщик не был зарегистрирован, метод ничего не делает.
   *
   * @param aEventGwid {@link Gwid} - GWID события удаляемого форматировщика
   * @throws TsNullArgumentRtException любой аргумент = <code>null</code>
   */
  void unregisterFormatter( Gwid aEventGwid );

  /**
   * Находит форматировщик для запрошенного события.
   *
   * @param aEventGwid {@link Gwid} - GWID запрашиваемого события
   * @return {@link IMwsModJournalEventFormatter} - найденный форматировщик или <code>null</code>
   * @throws TsNullArgumentRtException любой аргумент = <code>null</code>
   * @throws TsIllegalArgumentRtException аргумент-GWID не имеет тип {@link EGwidKind#GW_EVENT}
   */
  default IMwsModJournalEventFormatter find( Gwid aEventGwid ) {
    TsNullArgumentRtException.checkNull( aEventGwid );
    TsIllegalArgumentRtException.checkTrue( aEventGwid.kind() == EGwidKind.GW_EVENT );
    return find( aEventGwid.classId(), aEventGwid.strid() );
  }
}
