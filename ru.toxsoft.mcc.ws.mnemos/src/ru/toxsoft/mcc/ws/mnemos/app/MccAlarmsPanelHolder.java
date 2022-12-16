package ru.toxsoft.mcc.ws.mnemos.app;

import org.eclipse.swt.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.panels.*;
import org.toxsoft.uskat.core.connection.*;
import org.toxsoft.uskat.core.utils.*;

import ru.toxsoft.mcc.ws.mnemos.app.rt.alarm.*;

/**
 * Компонента для размещения панели алармов в проекте МосКокс.
 * <p>
 *
 * @author vs
 */
public class MccAlarmsPanelHolder
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
  public MccAlarmsPanelHolder( Composite aParent, ISkConnection aSkConn, ITsGuiContext aContext ) {
    super( aParent, aContext );
    skConnection = aSkConn;

    // Макс - здесь должна быть твоя инициализация

    GridLayout gl = new GridLayout( 1, false );
    setLayout( gl );
    GridData gd = new GridData( SWT.FILL, SWT.FILL, true, true );
    Ts4AlarmPanel alarmsPanel = new Ts4AlarmPanel( this, aSkConn, aContext );
    alarmsPanel.setLayoutData( gd );
  }

  // ------------------------------------------------------------------------------------
  // ISkConnected
  //

  @Override
  public ISkConnection skConn() {
    return skConnection;
  }

}
