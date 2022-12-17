package ru.toxsoft.mcc.ws.mnemos.app.rtb;

import org.eclipse.jface.viewers.*;
import org.eclipse.swt.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.panels.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.bricks.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.gw.gwid.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.uskat.core.api.objserv.*;
import org.toxsoft.uskat.core.api.rtdserv.*;
import org.toxsoft.uskat.core.api.sysdescr.*;
import org.toxsoft.uskat.core.api.sysdescr.dto.*;
import org.toxsoft.uskat.core.connection.*;
import org.toxsoft.uskat.core.utils.*;

import ru.toxsoft.mcc.ws.mnemos.app.rt.*;

public class PanelRtDataViewer
    extends TsPanel
    implements ISkConnected {

  class TableRow {

    private final Gwid         gwid;
    private final ISkClassInfo classInfo;
    private final ISkObject    skObject;
    private final String       formatString;
    private IAtomicValue       value = IAtomicValue.NULL;

    TableRow( Gwid aGwid ) {
      gwid = aGwid;
      classInfo = coreApi().sysdescr().findClassInfo( aGwid.classId() );
      IDtoRtdataInfo dataInfo = classInfo.rtdata().list().getByKey( aGwid.propId() );
      formatString = dataInfo.dataType().formatString();
      skObject = coreApi().objService().find( aGwid.skid() );
    }

    Gwid gwid() {
      return gwid;
    }

    String id() {
      return skObject.id();
    }

    String name() {
      return skObject.nmName();
    }

    String valueStr() {
      return AvUtils.printAv( formatString, value );
    }

    IAtomicValue value() {
      return value;
    }

    void setValue( IAtomicValue aValue ) {
      value = aValue;
    }

    void dispose() {
      // nop
    }
  }

  private final ISkConnection skConn;

  private final IRtDataProvider dataProvider;

  TreeViewer  treeViewer;
  TableViewer tableViewer;

  private final IMapEdit<Gwid, TableRow> rows = new ElemMap<>();

  private GwidList gwidList = null;

  private IMap<Gwid, ISkReadCurrDataChannel> channelsMap = null;

  private final IRealTimeSensitive timerHandler = aGwTime -> {
    doJob();
  };

  public PanelRtDataViewer( Composite aParent, ISkConnection aSkConn, ITsGuiContext aTsContext ) {
    super( aParent, aTsContext );
    setLayout( new FillLayout() );
    skConn = aSkConn;
    dataProvider = new MccRtDataProvider( aSkConn, aTsContext );
    createTableViewer( this, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL | SWT.FULL_SELECTION );
  }

  public void setGwidList( GwidList aGwids ) {
    gwidList = aGwids;
    for( TableRow row : rows.values() ) {
      row.dispose();
    }
    rows.clear();
    for( Gwid gwid : aGwids ) {
      TableRow row = new TableRow( gwid );
      rows.put( gwid, row );
    }
    tableViewer.setInput( rows.values().toArray() );
  }

  boolean started = false;

  public void start() {
    if( !started ) {
      started = true;
      channelsMap = skRtdataServ().createReadCurrDataChannels( gwidList );
      guiTimersService().slowTimers().addListener( timerHandler );
    }
  }

  void addGwid( Gwid aRtDataGwid ) {
    TsIllegalArgumentRtException.checkTrue( aRtDataGwid.isAbstract() );
    dataProvider.addDataConsumer( new SingleDataConsumer( aRtDataGwid ) );
  }

  // ------------------------------------------------------------------------------------
  // ISkConnected
  //

  @Override
  public ISkConnection skConn() {
    return skConn;
  }

  // ------------------------------------------------------------------------------------
  // Implementation
  //

  private void createTableViewer( Composite aParent, int aStyle ) {
    tableViewer = new TableViewer( aParent, aStyle );
    tableViewer.getTable().setHeaderVisible( true );
    tableViewer.getTable().setLinesVisible( false );

    TableViewerColumn columnId = new TableViewerColumn( tableViewer, SWT.NONE );
    columnId.getColumn().setText( "ИД" );
    columnId.getColumn().setWidth( 80 );
    columnId.setLabelProvider( new CellLabelProvider() {

      @Override
      public void update( ViewerCell aCell ) {
        TableRow row = (TableRow)aCell.getElement();
        aCell.setText( row.id() );
      }
    } );

    TableViewerColumn columnName = new TableViewerColumn( tableViewer, SWT.NONE );
    columnName.getColumn().setText( "Имя" );
    columnName.getColumn().setWidth( 120 );
    columnName.setLabelProvider( new CellLabelProvider() {

      @Override
      public void update( ViewerCell aCell ) {
        TableRow row = (TableRow)aCell.getElement();
        aCell.setText( row.name() );
      }
    } );

    TableViewerColumn columnValue = new TableViewerColumn( tableViewer, SWT.NONE );
    columnValue.getColumn().setText( "Значение" );
    columnValue.getColumn().setWidth( 120 );
    columnValue.setLabelProvider( new CellLabelProvider() {

      @Override
      public void update( ViewerCell aCell ) {
        TableRow row = (TableRow)aCell.getElement();
        aCell.setText( row.valueStr() );
      }
    } );

    tableViewer.setContentProvider( new ArrayContentProvider() );
  }

  void doJob() {
    IListEdit<TableRow> items2update = new ElemArrayList<>();
    for( Gwid gwid : channelsMap.keys() ) {
      ISkReadCurrDataChannel channel = channelsMap.getByKey( gwid );
      TableRow row = rows.getByKey( gwid );
      IAtomicValue value = channel.getValue();
      if( !value.equals( row.value ) ) {
        items2update.add( row );
        row.setValue( value );
      }
    }
    tableViewer.update( items2update.toArray(), null );
  }

  // private void createViewer( Composite aParent, int aStyle ) {
  // viewer = new TreeViewer( aParent, aStyle );
  // viewer.getTree().setHeaderVisible( true );
  // viewer.getTree().setLinesVisible( false );
  //
  // TreeViewerColumn columnName = new TreeViewerColumn( viewer, SWT.NONE );
  // columnName.getColumn().setText( "ИД" );
  // columnName.getColumn().setWidth( 80 );
  //
  // TreeViewerColumn columnValue = new TreeViewerColumn( viewer, SWT.NONE );
  // columnValue.getColumn().setText( "Значение" );
  // columnValue.getColumn().setWidth( 120 );
  //
  // // TreeViewerColumn columnValue;
  // // TreeViewerColumn columnDescription;
  //
  // viewer.setContentProvider( null );
  // }

}
