package ru.toxsoft.mcc.ws.mnemos.app.controls;

import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.graphics.colors.*;
import org.toxsoft.core.tsgui.graphics.fonts.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.core.tslib.utils.*;

/**
 * Многострочный текст.
 *
 * @author vs
 */
public class MccMultiRowLabel
    implements ITsGuiContextable {

  private final ITsGuiContext tsContext;

  private Font font = null;

  private final Rectangle viewRect = new Rectangle( 0, 0, 0, 0 );

  private final IStringMapEdit<Pair<Integer, Integer>> strings = new StringMap<>();

  private int gap = 1;

  Color textColor;

  MccMultiRowLabel( ITsGuiContext aContext ) {
    tsContext = aContext;
    textColor = colorManager().getColor( ETsColor.BLACK );
  }

  // ------------------------------------------------------------------------------------
  // ITsGuiContextable
  //

  @Override
  public ITsGuiContext tsContext() {
    return tsContext;
  }

  // ------------------------------------------------------------------------------------
  //
  //

  /**
   * Осуществляет отрисовку текста.
   *
   * @param aGc GC - графически контекст
   */
  void paint( GC aGc ) {
    aGc.setFont( font );
    aGc.setForeground( textColor );
    aGc.setBackground( colorManager().getColor( ETsColor.WHITE ) );
    for( String str : strings.keys() ) {
      Pair<Integer, Integer> p = strings.getByKey( str );
      aGc.drawText( str, p.left().intValue(), p.right().intValue(), true );
    }
  }

  /**
   * Задает параметры шрифта для отображения текста.
   *
   * @param aFontInfo IFontInfo - параметры шрифта для отображения текста
   */
  void setFont( IFontInfo aFontInfo ) {
    font = fontManager().getFont( aFontInfo );
    update();
  }

  void setText( IStringList aStrings ) {
    strings.clear();
    for( String str : aStrings ) {
      strings.put( str, new Pair<>( Integer.valueOf( 0 ), Integer.valueOf( 0 ) ) );
    }
    update();
  }

  /**
   * Устанавливает область для расположения текста.<br>
   * Выравнивание и расположение текста будет осуществляться в этой области
   *
   * @param aRect - область для расположения текста
   */
  void setTextPlacementArea( Rectangle aRect ) {
    viewRect.x = aRect.x;
    viewRect.y = aRect.y;
    viewRect.width = aRect.width;
    viewRect.height = aRect.height;
    update();
  }

  void setRowGap( int aGap ) {
    gap = aGap;
    update();
  }

  // ------------------------------------------------------------------------------------
  // Implementation
  //

  void update() {
    GC gc = null;
    try {
      gc = new GC( getShell() );
      gc.setFont( font );
      Rectangle r = calcBounds( gc );
      int dx = viewRect.x + (viewRect.width - r.width) / 2;
      int dy = viewRect.y + (viewRect.height - r.height) / 2;
      int y = 0;
      int x = 0;
      for( String str : strings.keys() ) {
        Point p = gc.textExtent( str );
        x = dx + (r.width - p.x) / 2;
        Pair<Integer, Integer> location = new Pair<>( Integer.valueOf( x ), Integer.valueOf( dy + y ) );
        strings.put( str, location );
        y += gap + p.y;
      }
    }
    finally {
      if( gc != null ) {
        gc.dispose();
      }
    }
  }

  Rectangle calcBounds( GC aGc ) {
    Rectangle r = new Rectangle( 0, 0, 0, 0 );
    int strH = 0;
    if( strings.size() > 0 ) {
      aGc.setFont( font );
      Point p = aGc.textExtent( strings.keys().first() );
      strH = p.y;
    }
    int height = (strings.size() - 1) * gap + strings.size() * strH;
    int maxW = 0;
    for( String str : strings.keys() ) {
      Point p = aGc.textExtent( str );
      if( p.x > maxW ) {
        maxW = p.x;
      }
    }
    r.width = maxW;
    r.height = height;
    return r;
  }

}
