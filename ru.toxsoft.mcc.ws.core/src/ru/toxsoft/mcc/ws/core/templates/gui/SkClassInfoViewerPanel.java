package ru.toxsoft.mcc.ws.core.templates.gui;

import static org.toxsoft.core.tslib.av.impl.AvUtils.*;

import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.ctx.impl.*;
import org.toxsoft.core.tsgui.bricks.stdevents.*;
import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.gui.mpc.*;
import org.toxsoft.core.tsgui.m5.gui.panels.*;
import org.toxsoft.core.tsgui.panels.*;
import org.toxsoft.core.tsgui.utils.layout.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.gw.gwid.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.uskat.base.gui.conn.*;
import org.toxsoft.uskat.base.gui.km5.sgw.*;
import org.toxsoft.uskat.core.api.sysdescr.*;
import org.toxsoft.uskat.core.connection.*;

/**
 * Панель просмотра списка классов ts4.<br>
 *
 * @author dima
 */
public class SkClassInfoViewerPanel
    extends TsPanel {

  private final ITsSelectionChangeListener<ISkClassInfo> classChangeListener = ( aSource, aSelectedItem ) -> {
    this.selectedClass = aSelectedItem;
    if( this.skObjectPanel != null ) {
      this.skObjectPanel.setClass( this.selectedClass );
    }
    if( this.rtDataPanel != null ) {
      this.rtDataPanel.setClass( this.selectedClass );
    }
  };

  final ISkConnection                      conn;
  private IM5CollectionPanel<ISkClassInfo> classesPanel;
  private SkObjectViewerPanel              skObjectPanel = null;
  private RtDataInfoViewerPanel            rtDataPanel   = null;
  private ISkClassInfo                     selectedClass = null;

  /**
   * @return {#link ISkClassInfo} class selected by user
   */
  public ISkClassInfo getSelectedClass() {
    return selectedClass;
  }

  /**
   * @param aSkObjectPanel {#link SkObjectViewerPanel} panel to show objects of selected class
   */
  public void setSkObjectPanel( SkObjectViewerPanel aSkObjectPanel ) {
    skObjectPanel = aSkObjectPanel;
  }

  /**
   * @param aRtDataPanel {#link RtDataInfoViewerPanel} panel to show list of rtDatas
   */
  public void setRtDataPanel( RtDataInfoViewerPanel aRtDataPanel ) {
    rtDataPanel = aRtDataPanel;
  }

  /**
   * Конструктор панели.
   * <p>
   * Конструктор просто запоминает ссылку на контекст, без создания копии.
   *
   * @param aParent {@link Composite} - родительская панель
   * @param aContext {@link ITsGuiContext} - контекст панели
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  public SkClassInfoViewerPanel( Composite aParent, ITsGuiContext aContext ) {
    super( aParent, aContext );
    this.setLayout( new BorderLayout() );
    ISkConnectionSupplier connSup = aContext.get( ISkConnectionSupplier.class );
    conn = connSup.defConn();
    // тут получаем KM5 модель ISkClassInfo
    IM5Domain m5 = conn.scope().get( IM5Domain.class );
    IM5Model<ISkClassInfo> model = m5.getModel( ISgwM5Constants.MID_SGW_CLASS_INFO, ISkClassInfo.class );
    ITsGuiContext ctx = new TsGuiContext( aContext );
    ctx.params().addAll( aContext.params() );
    IMultiPaneComponentConstants.OPDEF_IS_DETAILS_PANE.setValue( ctx.params(), AvUtils.AV_FALSE );
    IMultiPaneComponentConstants.OPDEF_DETAILS_PANE_PLACE.setValue( ctx.params(),
        avValobj( EBorderLayoutPlacement.SOUTH ) );
    // добавляем в панель фильтр
    IMultiPaneComponentConstants.OPDEF_IS_FILTER_PANE.setValue( ctx.params(), AvUtils.AV_TRUE );

    classesPanel =
        model.panelCreator().createCollViewerPanel( ctx, model.findLifecycleManager( conn ).itemsProvider() );
    // setup
    classesPanel.addTsSelectionListener( classChangeListener );
    classesPanel.createControl( this );

  }

  /**
   * Установить выбранный класс
   *
   * @param aGwid выбранный параметр
   */
  public void select( Gwid aGwid ) {
    ISkClassInfo ci = conn.coreApi().sysdescr().findClassInfo( aGwid.classId() );
    classesPanel.setSelectedItem( ci );
  }
}
