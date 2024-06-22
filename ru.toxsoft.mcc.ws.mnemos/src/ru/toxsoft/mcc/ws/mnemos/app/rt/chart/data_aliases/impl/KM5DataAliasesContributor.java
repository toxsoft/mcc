package ru.toxsoft.mcc.ws.mnemos.app.rt.chart.data_aliases.impl;

import org.toxsoft.core.tsgui.m5.IM5Domain;
import org.toxsoft.core.tslib.coll.primtypes.IStringList;
import org.toxsoft.core.tslib.coll.primtypes.impl.StringArrayList;
import org.toxsoft.core.tslib.utils.errors.TsNullArgumentRtException;
import org.toxsoft.uskat.core.connection.ISkConnection;
import org.toxsoft.uskat.core.gui.km5.IKM5ContributorCreator;
import org.toxsoft.uskat.core.gui.km5.KM5AbstractContributor;

/**
 * Contributes M5-models for template entities.
 *
 * @author dima
 */
public class KM5DataAliasesContributor
    extends KM5AbstractContributor {

  /**
   * Creator singleton.
   */
  public static final IKM5ContributorCreator CREATOR = KM5DataAliasesContributor::new;

  private static final IStringList CONRTIBUTED_MODEL_IDS = new StringArrayList( //
      MccDataAliasM5Model.MODEL_ID //
  );

  /**
   * Constructor.
   *
   * @param aConn {@link ISkConnection} - the connection
   * @param aDomain {@link IM5Domain} - connection domain
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public KM5DataAliasesContributor( ISkConnection aConn, IM5Domain aDomain ) {
    super( aConn, aDomain );
  }

  @Override
  protected IStringList papiCreateModels() {
    m5().addModel( new MccDataAliasM5Model() );
    return CONRTIBUTED_MODEL_IDS;
  }

}
