package ru.toxsoft.mcc.ws.reports.lib.impl;

import static org.toxsoft.core.tslib.bricks.strio.IStrioHardConstants.*;

import org.toxsoft.core.tsgui.chart.api.*;
import org.toxsoft.core.tsgui.graphics.colors.*;
import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.keeper.AbstractEntityKeeper.*;
import org.toxsoft.core.tslib.bricks.strio.*;
import org.toxsoft.core.tslib.gw.gwid.*;

import ru.toxsoft.mcc.ws.reports.lib.*;

/**
 * {@link ISkGraphParam} immutable implementation.
 *
 * @author dima
 */
public final class SkGraphParam
    extends SkReportParam
    implements ISkGraphParam {

  /**
   * Value-object keeper identifier.
   */
  @SuppressWarnings( "hiding" )
  public static final String KEEPER_ID = "SkGraphParam"; //$NON-NLS-1$

  /**
   * Keeper singleton.
   */
  @SuppressWarnings( "hiding" )
  public final static IEntityKeeper<ISkGraphParam> KEEPER =
      new AbstractEntityKeeper<>( ISkGraphParam.class, EEncloseMode.NOT_IN_PARENTHESES, null ) {

        @Override
        protected void doWrite( IStrioWriter aSw, ISkGraphParam aEntity ) {
          // пишем Gwid
          Gwid.KEEPER.write( aSw, aEntity.gwid() );
          aSw.writeChar( CHAR_ITEM_SEPARATOR );
          // name
          aSw.writeQuotedString( aEntity.title() );
          aSw.writeChar( CHAR_ITEM_SEPARATOR );
          // description
          aSw.writeQuotedString( aEntity.description() );
          aSw.writeChar( CHAR_ITEM_SEPARATOR );
          // unitId
          aSw.writeQuotedString( aEntity.unitId() );
          aSw.writeChar( CHAR_ITEM_SEPARATOR );
          // unit name
          aSw.writeQuotedString( aEntity.unitName() );
          aSw.writeChar( CHAR_ITEM_SEPARATOR );
          // функция агрегации
          aSw.writeAsIs( aEntity.aggrFunc().id() );
          aSw.writeChar( CHAR_ITEM_SEPARATOR );
          // формат отображения
          aSw.writeAsIs( aEntity.displayFormat().id() );
          aSw.writeChar( CHAR_ITEM_SEPARATOR );
          // цвет линии
          aSw.writeAsIs( aEntity.color().id() );
          aSw.writeChar( CHAR_ITEM_SEPARATOR );
          // толщина линии
          aSw.writeInt( aEntity.lineWidth() );
          aSw.writeChar( CHAR_ITEM_SEPARATOR );
          // рисовать 'лесенкой'
          aSw.writeBoolean( aEntity.isLadder() );
        }

        @Override
        protected ISkGraphParam doRead( IStrioReader aSr ) {
          Gwid gwid = Gwid.KEEPER.read( aSr );
          aSr.ensureChar( CHAR_ITEM_SEPARATOR );
          String title = aSr.readQuotedString();
          aSr.ensureChar( CHAR_ITEM_SEPARATOR );
          String descr = aSr.readQuotedString();
          aSr.ensureChar( CHAR_ITEM_SEPARATOR );
          String unitId = aSr.readQuotedString();
          aSr.ensureChar( CHAR_ITEM_SEPARATOR );
          String unitName = aSr.readQuotedString();
          aSr.ensureChar( CHAR_ITEM_SEPARATOR );
          EAggregationFunc aggrFunc = EAggregationFunc.getById( aSr.readIdName() );
          aSr.ensureChar( CHAR_ITEM_SEPARATOR );
          EDisplayFormat dispFormat = EDisplayFormat.getById( aSr.readIdName() );
          aSr.ensureChar( CHAR_ITEM_SEPARATOR );
          ETsColor color = ETsColor.getById( aSr.readIdName() );
          aSr.ensureChar( CHAR_ITEM_SEPARATOR );
          int lineWidth = aSr.readInt();
          aSr.ensureChar( CHAR_ITEM_SEPARATOR );
          boolean isLadder = aSr.readBoolean();
          return new SkGraphParam( gwid, title, descr, unitId, unitName, aggrFunc, dispFormat, color, lineWidth,
              isLadder );
        }
      };

  protected final String   unitId;
  protected final String   unitName;
  protected final ETsColor color;
  protected final int      lineWidth;
  protected final boolean  isLadder;

  /**
   * Constructor.
   *
   * @param aGwid {@link Gwid} green world id of that parameter
   * @param aTitle name of parameter
   * @param aDescr description of parameter
   * @param aUnitId id unit, need for Y scale
   * @param aUnitName name of unit, need for Y scale
   * @param aAggrFunc {@link EAggregationFunc} aggregation func
   * @param aDispFormat {@link EDisplayFormat} display format
   * @param aColor {@link ETsColor} line color
   * @param aLineWidth line width
   * @param aIsLadder draw as ladder
   */
  public SkGraphParam( Gwid aGwid, String aTitle, String aDescr, String aUnitId, String aUnitName,
      EAggregationFunc aAggrFunc, EDisplayFormat aDispFormat, ETsColor aColor, int aLineWidth, boolean aIsLadder ) {
    super( aGwid, aTitle, aDescr, aAggrFunc, aDispFormat );
    unitId = aUnitId;
    unitName = aUnitName;
    color = aColor;
    lineWidth = aLineWidth;
    isLadder = aIsLadder;
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  @Override
  public ETsColor color() {
    return color;
  }

  @Override
  public int lineWidth() {
    return lineWidth;
  }

  @Override
  public String unitId() {
    return unitId;
  }

  @Override
  public String unitName() {
    return unitName;
  }

  @Override
  public boolean isLadder() {
    return isLadder;
  }

}
