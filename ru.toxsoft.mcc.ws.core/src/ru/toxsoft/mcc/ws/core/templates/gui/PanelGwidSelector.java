package ru.toxsoft.mcc.ws.core.templates.gui;

import static ru.toxsoft.mcc.ws.core.templates.gui.ISkResources.*;

import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.dialogs.datarec.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.gw.gwid.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link PanelGwidSelector} wrapped by {@link AbstractTsDialogPanel}.
 *
 * @author dima
 */
public class PanelGwidSelector
    extends AbstractTsDialogPanel<Gwid, ITsGuiContext> {

  private SkClassInfoViewerPanel classesPanel;
  private SkObjectViewerPanel    skObjectsPanel;
  private RtDataInfoViewerPanel  rtDataPanel;

  /**
   * Конструктор панели, предназаначенной для вставки в диалог {@link TsDialog}.
   *
   * @param aParent Composite - родительская компонента
   * @param aOwnerDialog TsDialog - родительский диалог
   */
  public PanelGwidSelector( Composite aParent, TsDialog<Gwid, ITsGuiContext> aOwnerDialog ) {
    super( aParent, aOwnerDialog );

    // this.setLayout( new GridLayout( 2, true ) );
    FillLayout fillLayout = new FillLayout();
    fillLayout.marginHeight = 5;
    fillLayout.marginWidth = 5;
    this.setLayout( fillLayout );
    init();
  }

  void init() {
    // create vertical sash
    SashForm verticalSashForm = new SashForm( this, SWT.VERTICAL );
    SashForm horizontalSashForm = new SashForm( verticalSashForm, SWT.HORIZONTAL );
    // panel for class selection
    classesPanel = new SkClassInfoViewerPanel( horizontalSashForm, tsContext() );
    // classesPanel.setLayoutData( new GridData( SWT.FILL, SWT.FILL, true, true ) );

    // panel for skObject selection
    skObjectsPanel = new SkObjectViewerPanel( horizontalSashForm, tsContext(), this );
    // skObjectsPanel.setLayoutData( new GridData( SWT.FILL, SWT.FILL, true, true ) );
    classesPanel.setSkObjectPanel( skObjectsPanel );
    horizontalSashForm.setWeights( 1, 1 );
    horizontalSashForm.setSashWidth( 5 );
    // verticalSashForm.setWeights( 1, 1 );
    verticalSashForm.setSashWidth( 5 );

    // panel for RtData
    rtDataPanel = new RtDataInfoViewerPanel( verticalSashForm, tsContext(), this );
    // rtDataPanel.setLayoutData( new GridData( SWT.FILL, SWT.FILL, true, true, 2, 1 ) );
    classesPanel.setRtDataPanel( rtDataPanel );
  }

  @Override
  protected void doSetDataRecord( Gwid aGwid ) {
    if( aGwid != null ) {
      classesPanel.select( aGwid );
      skObjectsPanel.select( aGwid );
      rtDataPanel.select( aGwid );
    }
  }

  @Override
  protected Gwid doGetDataRecord() {
    Gwid retVal = Gwid.createRtdata( classesPanel.getSelectedClass().id(), skObjectsPanel.getSelectedObj().strid(),
        rtDataPanel.getSelectedRtData().id() );
    return retVal;
  }

  /**
   * Выводит диалог выбора Gwid.
   * <p>
   *
   * @param aGwid {@link Gwid} для инициализации
   * @param aContext {@link ITsGuiContext} - контекст
   * @return TsFillInfo - параметры заливки или <b>null</b> в случает отказа от редактирования
   */
  public static final Gwid selectGwid( Gwid aGwid, ITsGuiContext aContext ) {
    TsNullArgumentRtException.checkNull( aContext );
    IDialogPanelCreator<Gwid, ITsGuiContext> creator = PanelGwidSelector::new;
    ITsDialogInfo dlgInfo = new TsDialogInfo( aContext, DLG_T_GWID_SEL, STR_MSG_GWID_SELECTION );
    TsDialog<Gwid, ITsGuiContext> d = new TsDialog<>( dlgInfo, aGwid, aContext, creator );
    return d.execData();
  }

  @Override
  protected ValidationResult doValidate() {
    // check selected object
    if( skObjectsPanel.getSelectedObj() == null ) {
      return ValidationResult.error( STR_MSG_SELECT_OBJ );
    }
    // check selected rtData
    if( rtDataPanel.getSelectedRtData() == null ) {
      return ValidationResult.error( STR_MSG_SELECT_DATA );
    }
    return ValidationResult.SUCCESS;
  }
}
