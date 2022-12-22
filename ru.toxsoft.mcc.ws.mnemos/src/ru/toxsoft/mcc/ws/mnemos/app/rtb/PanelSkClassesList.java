package ru.toxsoft.mcc.ws.mnemos.app.rtb;

import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.ctx.impl.*;
import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.gui.panels.*;
import org.toxsoft.core.tsgui.m5.model.*;
import org.toxsoft.uskat.base.gui.glib.*;
import org.toxsoft.uskat.base.gui.km5.sgw.*;
import org.toxsoft.uskat.core.api.sysdescr.*;

/**
 * Панель для просмотра списка классов.
 * <p>
 *
 * @author vs
 */
public class PanelSkClassesList
    extends AbstractSkStdEventsProducerLazyPanel<ISkClassInfo> {

  IM5CollectionPanel<ISkClassInfo> panel = null;

  /**
   * Конструктор.
   *
   * @param aParent Composite - родительская панель
   * @param aContext ITsGuiContext - соответствующй контекст
   */
  public PanelSkClassesList( Composite aParent, ITsGuiContext aContext ) {
    super( aContext );
  }

  @Override
  protected void doInitGui( Composite aParent ) {
    aParent.setLayout( new FillLayout() );
    IM5Model<ISkClassInfo> model = m5().getModel( ISgwM5Constants.MID_SGW_CLASS_INFO, ISkClassInfo.class );
    IM5LifecycleManager<ISkClassInfo> lcm = model.findLifecycleManager( skConn() );
    ITsGuiContext ctx = new TsGuiContext( tsContext() );
    panel = model.panelCreator().createCollViewerPanel( ctx, lcm.itemsProvider() );
    panel.createControl( getControl() );
    panel.addTsSelectionListener( selectionChangeEventHelper );
    panel.addTsDoubleClickListener( doubleClickEventHelper );
  }

  @Override
  public ISkClassInfo selectedItem() {
    if( isPanelContent() ) {
      return panel.selectedItem();
    }
    return null;
  }

  @Override
  public void setSelectedItem( ISkClassInfo aItem ) {
    if( isPanelContent() ) {
      panel.setSelectedItem( aItem );
    }
  }

}
