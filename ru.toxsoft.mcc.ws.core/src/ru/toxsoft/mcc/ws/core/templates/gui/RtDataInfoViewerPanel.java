package ru.toxsoft.mcc.ws.core.templates.gui;

import static org.toxsoft.core.tslib.av.impl.AvUtils.*;

import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.ctx.impl.*;
import org.toxsoft.core.tsgui.bricks.stdevents.*;
import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.gui.mpc.*;
import org.toxsoft.core.tsgui.m5.gui.panels.*;
import org.toxsoft.core.tsgui.m5.model.*;
import org.toxsoft.core.tsgui.panels.*;
import org.toxsoft.core.tsgui.utils.layout.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.gw.gwid.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.uskat.base.gui.conn.*;
import org.toxsoft.uskat.base.gui.km5.sgw.*;
import org.toxsoft.uskat.core.api.sysdescr.*;
import org.toxsoft.uskat.core.api.sysdescr.dto.*;
import org.toxsoft.uskat.core.connection.*;

/**
 * Панель просмотра списка параметров класса {#link IDtoRtdataInfo}.<br>
 *
 * @author dima
 */
public class RtDataInfoViewerPanel
    extends TsPanel
    implements IM5ItemsProvider<IDtoRtdataInfo> {

  private final ITsSelectionChangeListener<IDtoRtdataInfo> dataChangeListener = ( aSource, aSelectedItem ) -> {
    this.selectedRtData = aSelectedItem;
  };

  private final ISkConnection                conn;
  private IM5CollectionPanel<IDtoRtdataInfo> rtDataInfoPanel;
  private ISkClassInfo                       currClass;
  private IDtoRtdataInfo                     selectedRtData = null;
  private final PanelGwidSelector            panelGwidSelector;

  /**
   * @return {#link IDtoRtdataInfo} parameter selected by user
   */
  public IDtoRtdataInfo getSelectedRtData() {
    return selectedRtData;
  }

  /**
   * Конструктор панели.
   * <p>
   * Конструктор просто запоминает ссылку на контекст, без создания копии.
   *
   * @param aParent {@link Composite} - родительская панель
   * @param aContext {@link ITsGuiContext} - контекст панели
   * @param aPanelGwidSelector {@link PanelGwidSelector} - диалог в который вставлена панель
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  public RtDataInfoViewerPanel( Composite aParent, ITsGuiContext aContext, PanelGwidSelector aPanelGwidSelector ) {
    super( aParent, aContext );
    panelGwidSelector = aPanelGwidSelector;
    this.setLayout( new BorderLayout() );
    ISkConnectionSupplier connSup = aContext.get( ISkConnectionSupplier.class );
    conn = connSup.defConn();
    IM5Domain m5Domain = conn.scope().get( IM5Domain.class );
    // тут получаем KM5 модель ISkObject
    IM5Model<IDtoRtdataInfo> model = m5Domain.getModel( ISgwM5Constants.MID_SGW_RTDATA_INFO, IDtoRtdataInfo.class );
    ITsGuiContext ctx = new TsGuiContext( aContext );
    ctx.params().addAll( aContext.params() );
    IMultiPaneComponentConstants.OPDEF_IS_DETAILS_PANE.setValue( ctx.params(), AvUtils.AV_FALSE );
    IMultiPaneComponentConstants.OPDEF_DETAILS_PANE_PLACE.setValue( ctx.params(),
        avValobj( EBorderLayoutPlacement.SOUTH ) );
    // добавляем в панель фильтр
    IMultiPaneComponentConstants.OPDEF_IS_FILTER_PANE.setValue( ctx.params(), AvUtils.AV_TRUE );

    rtDataInfoPanel = model.panelCreator().createCollViewerPanel( ctx, this );
    rtDataInfoPanel.addTsSelectionListener( dataChangeListener );
    rtDataInfoPanel.addTsSelectionListener( ( aSource, aSelectedItem ) -> panelGwidSelector.fireContentChangeEvent() );

    rtDataInfoPanel.setItemsProvider( this );
    rtDataInfoPanel.createControl( this );

  }

  /**
   * @param aClassInfo {@link ISkClassInfo} выбранный класс объекты которого нужно отобразить
   */
  public void setClass( ISkClassInfo aClassInfo ) {
    currClass = aClassInfo;
    rtDataInfoPanel.refresh();
  }

  @Override
  public IList<IDtoRtdataInfo> listItems() {
    if( currClass != null ) {
      return conn.coreApi().sysdescr().getClassInfo( currClass.id() ).rtdata().list();
    }
    return IList.EMPTY;
  }

  /**
   * Установить выбранный параметр
   *
   * @param aGwid выбранный параметр
   */
  public void select( Gwid aGwid ) {
    ISkClassInfo classInfo = conn.coreApi().sysdescr().findClassInfo( aGwid.classId() );
    IDtoRtdataInfo dataInfo = classInfo.rtdata().list().findByKey( aGwid.propId() );
    rtDataInfoPanel.setSelectedItem( dataInfo );
  }
}
