package ru.toxsoft.mcc.ws.mnemos.app;

import org.eclipse.swt.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.panels.*;
import org.toxsoft.uskat.base.gui.conn.*;
import org.toxsoft.uskat.core.connection.*;

import ru.toxsoft.mcc.ws.mnemos.app.rt.*;

/**
 * Панель отображения мнемосхемы.
 * <p>
 *
 * @author vs
 */
public class MccSchemeRightPanel
    extends TsPanel {

  private final MccGraphicsHolderPanel graphicsHolder;

  /**
   * Конструктор.
   *
   * @param aParent Composite - родительская компонента
   * @param aDataProvider IRtDataProvider - поставщик РВ-данных
   * @param aContext ITsGuiContext - контекст панели
   */
  public MccSchemeRightPanel( Composite aParent, IRtDataProvider aDataProvider, ITsGuiContext aContext ) {
    super( aParent, aContext );
    GridLayout gl = new GridLayout( 1, false );
    setLayout( gl );

    ISkConnection skConn = aContext.get( ISkConnectionSupplier.class ).defConn();

    MccMainControlPanel ctrlPanel = new MccMainControlPanel( this, aDataProvider, skConn, aContext );
    ctrlPanel.setLayoutData( new GridData( SWT.FILL, SWT.TOP, true, false ) );

    graphicsHolder = new MccGraphicsHolderPanel( this, skConn, aContext );
    graphicsHolder.setLayoutData( new GridData( SWT.FILL, SWT.FILL, true, true ) );
  }

}
