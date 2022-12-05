package ru.toxsoft.mcc.ws.mnemos.app.utils;

import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Набор вспомогательных методов по работе с {@link IAtomicValue}.
 * <p>
 *
 * @author vs
 */
public class MccAvUtils {

  public static IAtomicValue parse( String aStrValue, EAtomicType aType ) {
    switch( aType ) {
      case BOOLEAN:
        return AvUtils.avBool( Boolean.parseBoolean( aStrValue ) );
      case FLOATING:
        return AvUtils.avFloat( Double.parseDouble( aStrValue ) );
      case INTEGER:
        return AvUtils.avInt( Long.parseLong( aStrValue ) );
      case NONE:
        return IAtomicValue.NULL;
      case STRING:
        return AvUtils.avStr( aStrValue );
      case TIMESTAMP:
      case VALOBJ:
      default:
        throw new TsNotAllEnumsUsedRtException();
    }
  }

  public static String formatString( EAtomicType aType ) {
    switch( aType ) {
      case BOOLEAN:
      case FLOATING:
      case INTEGER:
      case NONE:
      case STRING:
      case TIMESTAMP:
      case VALOBJ:
      default:
        throw new TsNotAllEnumsUsedRtException();
    }
  }

  /**
   * Запрет на создание экземпляров.
   */
  private MccAvUtils() {

  }
}
