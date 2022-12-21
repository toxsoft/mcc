package ru.toxsoft.mcc.ws.mnemos.app.rtb;

import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.ctx.impl.*;
import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.gui.panels.*;
import org.toxsoft.core.tsgui.m5.model.*;
import org.toxsoft.core.tslib.bricks.strid.more.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.uskat.base.gui.conn.*;
import org.toxsoft.uskat.base.gui.glib.*;
import org.toxsoft.uskat.base.gui.km5.sgw.*;
import org.toxsoft.uskat.core.api.sysdescr.*;
import org.toxsoft.uskat.core.connection.*;
import org.toxsoft.uskat.core.utils.*;

/**
 * Панель для просмотра списка классов.
 * <p>
 *
 * @author vs
 */
public class PanelSkClassesList
    extends SkStdEventsProducerPanel<ISkClassInfo>
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

  ISkConnection skConn = null;

  IM5CollectionPanel<ISkClassInfo> internalPanel = null;

  /**
   * Конструктор.
   *
   * @param aParent Composite - родительская панель
   * @param aContext ITsGuiContext - соответствующй контекст
   */
  public PanelSkClassesList( Composite aParent, ITsGuiContext aContext ) {
    super( aParent, aContext );
    setLayout( new FillLayout() );
    skConn = aContext.get( ISkConnectionSupplier.class ).defConn();
    if( skConn != null && skConn.state() == ESkConnState.ACTIVE ) {
      skConn.addConnectionListener( connListener );
      internalPanel = createIntenalPanel();
    }

  }

  @Override
  public ISkClassInfo selectedItem() {
    return internalPanel.selectedItem();
  }

  @Override
  public void setSelectedItem( ISkClassInfo aItem ) {
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

  // ------------------------------------------------------------------------------------
  // Implementation
  //

  void onConnectionClosed() {
    internalPanel.getControl().dispose();
    internalPanel = null;
  }

  void onConnectionActivated() {
    internalPanel = createIntenalPanel();
  }

  void onConnectionDeactivated() {
    internalPanel.getControl().dispose();
    internalPanel = null;
  }

  private IM5CollectionPanel<ISkClassInfo> createIntenalPanel() {
    IM5Domain m5dom = skConn.scope().get( IM5Domain.class );

    IM5Model<ISkClassInfo> model = m5dom.findModel( ISgwM5Constants.MID_SGW_CLASS_INFO );

    IM5LifecycleManager<ISkClassInfo> lcm = model.findLifecycleManager( skConn );
    ITsGuiContext ctx = new TsGuiContext( tsContext() );
    IM5CollectionPanel<ISkClassInfo> panel = model.panelCreator().createCollViewerPanel( ctx, lcm.itemsProvider() );
    panel.createControl( this );

    panel.addTsSelectionListener( selectionChangeEventHelper );
    panel.addTsDoubleClickListener( doubleClickEventHelper );
    return panel;
  }

}
