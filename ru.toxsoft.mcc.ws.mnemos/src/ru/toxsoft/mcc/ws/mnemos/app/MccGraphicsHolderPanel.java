package ru.toxsoft.mcc.ws.mnemos.app;

import org.eclipse.swt.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.panels.*;
import org.toxsoft.uskat.core.connection.*;
import org.toxsoft.uskat.core.utils.*;

import ru.toxsoft.mcc.ws.mnemos.app.rt.chart.*;

/**
 * Панель для размещения графиков в проекте МосКокс.
 * <p>
 *
 * @author vs
 */
public class MccGraphicsHolderPanel
    extends TsPanel
    implements ISkConnected {

  private final ISkConnection skConnection;

  /**
   * Конструктор.<br>
   *
   * @param aParent Composite - родительская панель
   * @param aSkConn ISkConnection - соединение с сервером
   * @param aContext ITsGuiContext - соответствующий контекст
   */
  public MccGraphicsHolderPanel( Composite aParent, ISkConnection aSkConn, ITsGuiContext aContext ) {
    super( aParent, aContext );
    skConnection = aSkConn;

    GridLayout gl = new GridLayout( 1, false );
    gl.marginWidth = 0;
    gl.marginHeight = 0;
    setLayout( gl );

    GridData gd = new GridData( SWT.FILL, SWT.FILL, true, true );
    ChartsTabPanel topChartPanel = new ChartsTabPanel( this, aSkConn, aContext, true );
    topChartPanel.setLayoutData( gd );
    ChartsTabPanel bottomChartPanel = new ChartsTabPanel( this, aSkConn, aContext, false );
    bottomChartPanel.setLayoutData( gd );

  }

  // ------------------------------------------------------------------------------------
  // ISkConnected
  //

  @Override
  public ISkConnection skConn() {
    return skConnection;
  }

}
