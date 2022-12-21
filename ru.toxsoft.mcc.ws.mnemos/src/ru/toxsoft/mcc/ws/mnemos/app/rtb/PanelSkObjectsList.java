package ru.toxsoft.mcc.ws.mnemos.app.rtb;

import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.ctx.impl.*;
import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.gui.panels.*;
import org.toxsoft.core.tsgui.m5.model.*;
import org.toxsoft.core.tsgui.m5.model.impl.*;
import org.toxsoft.core.tsgui.panels.*;
import org.toxsoft.core.tslib.bricks.strid.more.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.uskat.base.gui.conn.*;
import org.toxsoft.uskat.base.gui.km5.sgw.*;
import org.toxsoft.uskat.core.api.objserv.*;
import org.toxsoft.uskat.core.api.sysdescr.*;
import org.toxsoft.uskat.core.connection.*;
import org.toxsoft.uskat.core.utils.*;

/**
 * Панель для просмотра списка объектов указанного класса.
 * <p>
 *
 * @author vs
 */
public class PanelSkObjectsList
    extends TsStdEventsProducerPanel<ISkObject>
    implements ISkConnected {

  ISkConnectionListener connListener = ( aSkConn, aOldState ) -> {
    switch( aSkConn.state() ) {
      case ACTIVE:
        onConnectionActivated();
        break;
      case CLOSED:
        onConnectionClosed();
        break;
      case INACTIVE:
        onConnectionDeactivated();
        break;
      default:
        throw new TsNotAllEnumsUsedRtException();
    }

  };

  static class ItemsProvider
      implements IM5ItemsProvider<ISkObject> {

    ISkClassInfo  classInfo = null;
    ISkConnection skConn    = null;

    public ItemsProvider( ISkConnection aSkConn ) {
      skConn = aSkConn;
    }

    @Override
    public IList<ISkObject> listItems() {
      if( skConn != null ) {
        return skConn.coreApi().objService().listObjs( classInfo.id(), true );
      }
      return null;
    }

    void setSkConnection( ISkConnection aSkConn ) {
      skConn = aSkConn;
    }

    void setClassInfo( ISkClassInfo aClassInfo ) {
      classInfo = aClassInfo;
    }

  }

  static class LifecycleManager
      extends M5LifecycleManager<ISkObject, ISkClassInfo> {

    ItemsProvider itemsProvider;

    public LifecycleManager( IM5Model<ISkObject> aModel, ISkConnection aSkConn ) {
      super( aModel, false, false, false, true, null );
      itemsProvider = new ItemsProvider( aSkConn );
      ISkClassInfo clsInfo = aSkConn.coreApi().sysdescr().findClassInfo( "mcc.AnalogInput" );
      itemsProvider.setSkConnection( aSkConn );
      itemsProvider.setClassInfo( clsInfo );
    }

    @Override
    public IM5ItemsProvider<ISkObject> itemsProvider() {
      return itemsProvider;
    }

    void setClassInfo( ISkClassInfo aClassInfo ) {
      itemsProvider.setClassInfo( aClassInfo );
    }

    void setSkConnection( ISkConnection aSkConn ) {
      itemsProvider.setSkConnection( aSkConn );
    }

  }

  ISkConnection skConn = null;

  IM5CollectionPanel<ISkObject> internalPanel = null;

  LifecycleManager lcm;

  /**
   * Конструктор.
   *
   * @param aParent Composite - родительская панель
   * @param aContext ITsGuiContext - соответствующй контекст
   */
  public PanelSkObjectsList( Composite aParent, ITsGuiContext aContext ) {
    super( aParent, aContext );
    setLayout( new FillLayout() );
    skConn = aContext.get( ISkConnectionSupplier.class ).defConn();

    IM5Domain m5dom = skConn.scope().get( IM5Domain.class );
    IM5Model<ISkObject> model = m5dom.findModel( ISgwM5Constants.MID_SGW_SK_OBJECT );
    lcm = new LifecycleManager( model, skConn );

    if( skConn != null && skConn.state() == ESkConnState.ACTIVE ) {
      skConn.addConnectionListener( connListener );
      internalPanel = createIntenalPanel();
    }
  }

  @Override
  public ISkObject selectedItem() {
    return internalPanel.selectedItem();
  }

  @Override
  public void setSelectedItem( ISkObject aItem ) {
    internalPanel.setSelectedItem( aItem );
  }

  // ------------------------------------------------------------------------------------
  // ISkConnected
  //

  @Override
  public ISkConnection skConn() {
    return skConn;
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  void setConnectionId( IdChain aSkConnId ) {
    if( skConn != null ) {
      skConn.removeConnectionListener( connListener );
    }
    skConn = tsContext().get( ISkConnectionSupplier.class ).getConn( aSkConnId );
    skConn.addConnectionListener( connListener );
    onConnectionDeactivated();
    onConnectionActivated();
  }

  void setClassInfo( ISkClassInfo aClassInfo ) {
    lcm.setClassInfo( aClassInfo );
  }

  // ------------------------------------------------------------------------------------
  // Implementation
  //

  void onConnectionClosed() {
    internalPanel.getControl().dispose();
    internalPanel = null;
    lcm.setSkConnection( null );
  }

  void onConnectionActivated() {
    internalPanel = createIntenalPanel();
    lcm.setSkConnection( skConn );
  }

  void onConnectionDeactivated() {
    internalPanel.getControl().dispose();
    internalPanel = null;
    lcm.setSkConnection( null );
  }

  private IM5CollectionPanel<ISkObject> createIntenalPanel() {
    IM5Domain m5dom = skConn.scope().get( IM5Domain.class );
    IM5Model<ISkObject> model = m5dom.findModel( ISgwM5Constants.MID_SGW_SK_OBJECT );

    ITsGuiContext ctx = new TsGuiContext( tsContext() );
    IM5CollectionPanel<ISkObject> panel = model.panelCreator().createCollViewerPanel( ctx, lcm.itemsProvider() );

    panel.createControl( this );

    panel.addTsSelectionListener( selectionChangeEventHelper );
    panel.addTsDoubleClickListener( doubleClickEventHelper );
    return panel;
  }

}
