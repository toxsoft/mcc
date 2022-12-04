package ru.toxsoft.mcc.ws.mnemos.app;

import org.eclipse.swt.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.panels.*;
import org.toxsoft.uskat.core.*;
import org.toxsoft.uskat.core.connection.*;
import org.toxsoft.uskat.core.utils.*;

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

    // FIXME Дима, замени на свою инициализацию (для работы с сервером см. интерфейсы ISkConnected и ITsGuiContextable)
    ISkCoreApi coreApi = coreApi();

    GridLayout gl = new GridLayout( 1, false );
    setLayout( gl );

    GridData gd = new GridData( SWT.FILL, SWT.FILL, true, true );
    Text t1 = new Text( this, SWT.BORDER );
    t1.setText( "Грaфик 1" );
    t1.setLayoutData( gd );

    Text t2 = new Text( this, SWT.BORDER );
    t2.setText( "Грaфик 1" );
    t2.setLayoutData( gd );

    Text t3 = new Text( this, SWT.BORDER );
    t3.setText( "Грaфик 1" );
    t3.setLayoutData( gd );

  }

  // ------------------------------------------------------------------------------------
  // ISkConnected
  //

  @Override
  public ISkConnection skConn() {
    return skConnection;
  }

}
