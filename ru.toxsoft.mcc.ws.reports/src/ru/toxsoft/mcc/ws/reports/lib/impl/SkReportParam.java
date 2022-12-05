package ru.toxsoft.mcc.ws.reports.lib.impl;

import static org.toxsoft.core.tslib.bricks.strio.IStrioHardConstants.*;

import org.toxsoft.core.tsgui.chart.api.*;
import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.keeper.AbstractEntityKeeper.*;
import org.toxsoft.core.tslib.bricks.strio.*;
import org.toxsoft.core.tslib.gw.gwid.*;

import ru.toxsoft.mcc.ws.reports.lib.*;

/**
 * {@link ISkReportParam} immutable implementation.
 *
 * @author dima
 */
public class SkReportParam
    implements ISkReportParam {

  /**
   * Value-object keeper identifier.
   */
  public static final String KEEPER_ID = "SkReportParam"; //$NON-NLS-1$

  /**
   * Keeper singleton.
   */
  public final static IEntityKeeper<ISkReportParam> KEEPER =
      new AbstractEntityKeeper<>( ISkReportParam.class, EEncloseMode.NOT_IN_PARENTHESES, null ) {

        @Override
        protected void doWrite( IStrioWriter aSw, ISkReportParam aEntity ) {
          // пишем Gwid
          Gwid.KEEPER.write( aSw, aEntity.gwid() );
          aSw.writeChar( CHAR_ITEM_SEPARATOR );
          // name
          aSw.writeQuotedString( aEntity.title() );
          aSw.writeChar( CHAR_ITEM_SEPARATOR );
          // description
          aSw.writeQuotedString( aEntity.description() );
          aSw.writeChar( CHAR_ITEM_SEPARATOR );
          // функция агрегации
          aSw.writeAsIs( aEntity.aggrFunc().id() );
          aSw.writeChar( CHAR_ITEM_SEPARATOR );
          // формат отображения
          aSw.writeAsIs( aEntity.displayFormat().id() );
        }

        @Override
        protected ISkReportParam doRead( IStrioReader aSr ) {
          Gwid gwid = Gwid.KEEPER.read( aSr );
          aSr.ensureChar( CHAR_ITEM_SEPARATOR );
          String title = aSr.readQuotedString();
          aSr.ensureChar( CHAR_ITEM_SEPARATOR );
          String descr = aSr.readQuotedString();
          aSr.ensureChar( CHAR_ITEM_SEPARATOR );
          EAggregationFunc aggrFunc = EAggregationFunc.getById( aSr.readIdName() );
          aSr.ensureChar( CHAR_ITEM_SEPARATOR );
          EDisplayFormat dispFormat = EDisplayFormat.getById( aSr.readIdName() );
          return new SkReportParam( gwid, title, descr, aggrFunc, dispFormat );
        }
      };

  protected final Gwid             gwid;
  protected final String           title;
  protected final String           description;
  protected final EAggregationFunc aggrFunc;
  protected final EDisplayFormat   dispFormat;

  /**
   * Constructor.
   *
   * @param aGwid {@link Gwid} green world id of that parameter
   * @param aTitle name of parameter
   * @param aDescr description of parameter
   * @param aAggrFunc {@link EAggregationFunc} aggregation func
   * @param aDispFormat {@link EDisplayFormat} display format
   */
  public SkReportParam( Gwid aGwid, String aTitle, String aDescr, EAggregationFunc aAggrFunc,
      EDisplayFormat aDispFormat ) {
    gwid = aGwid;
    title = aTitle;
    description = aDescr;
    aggrFunc = aAggrFunc;
    dispFormat = aDispFormat;
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  @Override
  public Gwid gwid() {
    return gwid;
  }

  @Override
  public EAggregationFunc aggrFunc() {
    return aggrFunc;
  }

  @Override
  public EDisplayFormat displayFormat() {
    return dispFormat;
  }

  @Override
  public String title() {
    return title;
  }

  @Override
  public String description() {
    return description;
  }

}
