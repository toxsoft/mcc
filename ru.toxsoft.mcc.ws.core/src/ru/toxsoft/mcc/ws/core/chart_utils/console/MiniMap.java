package ru.toxsoft.mcc.ws.core.chart_utils.console;

import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.utils.layout.*;
import org.toxsoft.core.tslib.bricks.events.change.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.utils.*;

class MiniMap
    implements IGenericChangeEventCapable {

  Composite bkPanel;

  Slider hSlider;
  Slider vSlider;

  boolean xDrag = false;
  boolean yDrag = false;

  int dragX = 0;
  int dragY = 0;

  double dragDX = 0;
  double dragDY = 0;

  private final GenericChangeEventer eventer;

  private final IListEdit<IGenericChangeListener> changeListeners = new ElemArrayList<>();

  public MiniMap( Composite aParent ) {
    eventer = new GenericChangeEventer( this );

    bkPanel = new Composite( aParent, SWT.NONE );
    bkPanel.setLayout( new BorderLayout() );

    hSlider = new Slider( bkPanel, SWT.HORIZONTAL );
    hSlider.setLayoutData( BorderLayout.SOUTH );
    hSlider.setMinimum( 0 );
    hSlider.setMaximum( 200 + hSlider.getThumb() );
    hSlider.setIncrement( 1 );
    hSlider.setSelection( 100 );

    hSlider.addSelectionListener( new SelectionAdapter() {

      @Override
      public void widgetSelected( SelectionEvent aEvent ) {
        if( xDrag ) {
          int value = hSlider.getSelection();
          dragDX = value - dragX;
          dragX = value;
          fireChangeEvent();
        }
      }
    } );

    hSlider.addMouseListener( new MouseAdapter() {

      @Override
      public void mouseDown( MouseEvent aE ) {
        xDrag = true;
        dragX = hSlider.getSelection();
      }

      @Override
      public void mouseUp( MouseEvent aE ) {
        xDrag = false;
        hSlider.setSelection( 100 );
        dragDX = 0;
      }
    } );

    vSlider = new Slider( bkPanel, SWT.VERTICAL );
    vSlider.setLayoutData( BorderLayout.EAST );
    vSlider.setMinimum( 0 );
    vSlider.setMaximum( 200 + vSlider.getThumb() );
    vSlider.setIncrement( 1 );
    vSlider.setSelection( 100 );

    vSlider.addSelectionListener( new SelectionAdapter() {

      @Override
      public void widgetSelected( SelectionEvent aEvent ) {
        if( yDrag ) {
          int value = vSlider.getSelection();
          dragDY = value - dragY;
          dragY = value;
          fireChangeEvent();
        }
      }
    } );

    vSlider.addMouseListener( new MouseAdapter() {

      @Override
      public void mouseDown( MouseEvent aE ) {
        yDrag = true;
        dragY = vSlider.getSelection();
      }

      @Override
      public void mouseUp( MouseEvent aE ) {
        yDrag = false;
        vSlider.setSelection( 100 );
        dragDY = 0;
      }
    } );

  }

  // ------------------------------------------------------------------------------------
  // API пакета
  //

  Pair<Double, Double> dragShift() {
    return new Pair<>( dragDX, dragDY );
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса IGenericChangeEventProducer
  //

  public void addGenericChangeListener( IGenericChangeListener aListener ) {
    if( !changeListeners.hasElem( aListener ) ) {
      changeListeners.add( aListener );
    }
  }

  public void removeGenericChangeListener( IGenericChangeListener aListener ) {
    changeListeners.remove( aListener );
  }

  // ------------------------------------------------------------------------------------
  // Внутренняя реализация
  //

  void fireChangeEvent() {
    for( IGenericChangeListener listener : changeListeners ) {
      listener.onGenericChangeEvent( this );
    }
  }

  @Override
  public IGenericChangeEventer genericChangeEventer() {
    return eventer;
  }

}
