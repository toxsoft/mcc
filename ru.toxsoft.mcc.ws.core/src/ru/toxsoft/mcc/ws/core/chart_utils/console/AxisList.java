package ru.toxsoft.mcc.ws.core.chart_utils.console;

import org.eclipse.jface.viewers.*;
import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.chart.api.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;

public class AxisList {

  CheckboxTableViewer      table;
  private final IG2Chart   chart;
  private final IG2Console console;

  public AxisList( IG2Chart aChart ) {
    chart = aChart;
    console = chart.console();
  }

  public CheckboxTableViewer createControl( Composite aParent ) {
    table = CheckboxTableViewer.newCheckList( aParent, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL );
    table.getTable().setHeaderVisible( true );
    table.getTable().setLinesVisible( true );

    TableViewerColumn columnName;
    columnName = new TableViewerColumn( table, SWT.NONE );
    columnName.getColumn().setText( "Имя" );
    columnName.getColumn().setWidth( 60 );
    columnName.setLabelProvider( new CellLabelProvider() {

      @Override
      public void update( ViewerCell aCell ) {
        IYAxisDef yDef = (IYAxisDef)aCell.getElement();
        // aCell.setText( yDef.title() );
        aCell.setText( yDef.description() );
      }
    } );

    table.setContentProvider( new ArrayContentProvider() );
    table.setInput( chart.yAxisDefs().toArray() );
    return table;
  }

  public IList<IYAxisDef> checkedAxises() {
    IListEdit<IYAxisDef> axises = new ElemArrayList<>();
    Object[] elems = table.getCheckedElements();
    for( int i = 0; i < elems.length; i++ ) {
      axises.add( (IYAxisDef)elems[i] );
    }
    return axises;
  }
}
