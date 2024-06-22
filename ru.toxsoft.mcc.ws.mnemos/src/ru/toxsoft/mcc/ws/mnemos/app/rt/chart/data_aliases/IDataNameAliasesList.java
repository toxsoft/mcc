package ru.toxsoft.mcc.ws.mnemos.app.rt.chart.data_aliases;

import org.toxsoft.core.tslib.coll.*;

/**
 * Interface to specify list of data name aliases. <br>
 *
 * @author dima
 */

public interface IDataNameAliasesList {

  /**
   * @return list of aliases
   */
  IList<IDataNameAlias> items();

}
