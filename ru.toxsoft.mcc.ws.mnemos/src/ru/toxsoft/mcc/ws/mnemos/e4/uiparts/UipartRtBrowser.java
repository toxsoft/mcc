package ru.toxsoft.mcc.ws.mnemos.e4.uiparts;

import org.eclipse.swt.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.widgets.*;
import org.toxsoft.core.tslib.utils.logs.impl.*;
import org.toxsoft.uskat.base.gui.conn.*;
import org.toxsoft.uskat.base.gui.e4.uiparts.*;
import org.toxsoft.uskat.core.connection.*;

import ru.toxsoft.mcc.ws.mnemos.app.rtb.*;

/**
 * Экран мнемосхемы нагнетателя.
 * <p>
 *
 * @author vs
 */
public class UipartRtBrowser
    extends SkMwsAbstractPart {

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

  private void init( final Composite aParent ) {

    aParent.setLayout( new GridLayout( 2, true ) );

    GridData gd = new GridData( SWT.FILL, SWT.FILL, true, true );

    PanelSkClassesList classesPanel = new PanelSkClassesList( aParent, tsContext() );
    classesPanel.setLayoutData( gd );
    // classePanel.setLayoutData( BorderLayout.CENTER );
    PanelSkObjectsList objectsPanel = new PanelSkObjectsList( aParent, tsContext() );
    objectsPanel.setLayoutData( gd );
    // objectsPanel.setLayoutData( BorderLayout.WEST );
    // aParent.layout();
  }
}
