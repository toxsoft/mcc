package ru.toxsoft.mcc.ws.mnemos.e4.uiparts;

import java.awt.*;

import org.eclipse.jface.resource.*;
import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.plugin.*;
import org.toxsoft.core.tsgui.utils.layout.*;
import org.toxsoft.core.tsgui.utils.layout.BorderLayout;
import org.toxsoft.core.tsgui.widgets.*;
import org.toxsoft.core.tslib.utils.logs.impl.*;
import org.toxsoft.uskat.base.gui.conn.*;
import org.toxsoft.uskat.base.gui.e4.uiparts.*;
import org.toxsoft.uskat.core.connection.*;

import ru.toxsoft.mcc.ws.mnemos.*;
import ru.toxsoft.mcc.ws.mnemos.app.dialogs.*;

/**
 * Экран мнемосхемы нагнетателя.
 * <p>
 *
 * @author vs
 */
public class UipartMccMainScheme
    extends SkMwsAbstractPart {
  // extends MwsAbstractPart {

  @Override
  public ISkConnection skConn() {
    ISkConnectionSupplier cs = tsContext().get( ISkConnectionSupplier.class );
    if( cs != null ) {
      return cs.defConn();
    }
    LoggerUtils.errorLogger().error( "ISkConnectionSupplier - null" ); //$NON-NLS-1$
    return null;
  }

  @Override
  protected void doCreateContent( TsComposite aParent ) {
    init( aParent );
  }

  // @Override
  // protected void doInit( Composite aParent ) {
  protected void init( Composite aParent ) {

    aParent.setLayout( new BorderLayout() );

    Composite schemeComp = new Composite( aParent, SWT.NONE );
    schemeComp.setLayoutData( BorderLayout.CENTER );

    ImageDescriptor imd;
    imd = AbstractUIPlugin.imageDescriptorFromPlugin( Activator.PLUGIN_ID, "icons/mcc-main.png" );
    Image imgScheme = imd.createImage();

    schemeComp.addPaintListener( aEvent -> aEvent.gc.drawImage( imgScheme, 0, 0 ) );

    Composite rightComp = new Composite( aParent, SWT.NONE );
    rightComp.setLayoutData( BorderLayout.EAST );
    rightComp.setData( AWTLayout.KEY_PREFERRED_SIZE, new Dimension( 438, -1 ) );

    schemeComp.addMouseListener( new MouseListener() {

      @Override
      public void mouseUp( MouseEvent aE ) {
        // TODO Auto-generated method stub

      }

      @Override
      public void mouseDown( MouseEvent aE ) {
        // TODO Auto-generated method stub

      }

      @Override
      public void mouseDoubleClick( MouseEvent aE ) {
        // TsDialogUtils.info( null, "Здесь будет диалог" );
        PanelAnalogInput.showDialog( new MccDialogContext( tsContext() ) );
      }
    } );
  }
}
