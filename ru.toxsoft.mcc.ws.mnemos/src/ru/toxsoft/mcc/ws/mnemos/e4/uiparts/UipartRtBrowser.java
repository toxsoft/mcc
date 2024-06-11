package ru.toxsoft.mcc.ws.mnemos.e4.uiparts;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.toxsoft.core.tsgui.widgets.TsComposite;
import org.toxsoft.uskat.core.gui.e4.uiparts.SkMwsAbstractPart;

import ru.toxsoft.mcc.ws.mnemos.app.rtb.TestPanel;

/**
 * Экран мнемосхемы нагнетателя.
 * <p>
 *
 * @author vs
 */
public class UipartRtBrowser
    extends SkMwsAbstractPart {

  @Override
  protected void doCreateContent( TsComposite aParent ) {
    init( aParent );
  }

  // private void init( final Composite aParent ) {
  //
  // // aParent.setLayout( new GridLayout( 2, true ) );
  // aParent.setLayout( new GridLayout( 1, true ) );
  //
  // GridData gd = new GridData( SWT.FILL, SWT.FILL, true, true );
  //
  // ISkConnection skConn = tsContext().get( ISkConnectionSupplier.class ).defConn();
  // PanelRtDataViewer dataPanel = new PanelRtDataViewer( aParent, skConn, tsContext() );
  // dataPanel.setLayoutData( gd );
  // GwidList gwidList = new GwidList();
  // for( ISkObject skObj : coreApi().objService().listObjs( "mcc.AnalogInput", true ) ) {
  // Gwid gwid = Gwid.createRtdata( "mcc.AnalogInput", skObj.strid(), "rtdCurrentValue" );
  // gwidList.add( gwid );
  // }
  // dataPanel.setGwidList( gwidList );
  // dataPanel.start();
  //
  // // PanelSkClassesList classesPanel = new PanelSkClassesList( aParent, tsContext() );
  // // classesPanel.setLayoutData( gd );
  // // PanelSkObjectsList objectsPanel = new PanelSkObjectsList( aParent, tsContext() );
  // // objectsPanel.setLayoutData( gd );
  // }

  private void init( final Composite aParent ) {
    aParent.setLayout( new GridLayout( 1, true ) );

    GridData gd = new GridData( SWT.FILL, SWT.FILL, true, true );

    TestPanel p = new TestPanel();
    p.createControl( aParent ).setLayoutData( gd );
  }

}
