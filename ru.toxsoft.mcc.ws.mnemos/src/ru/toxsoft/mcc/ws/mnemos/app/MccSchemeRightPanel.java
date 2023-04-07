package ru.toxsoft.mcc.ws.mnemos.app;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.toxsoft.core.tsgui.bricks.ctx.ITsGuiContext;
import org.toxsoft.core.tsgui.panels.TsPanel;
import org.toxsoft.uskat.core.connection.ISkConnection;
import org.toxsoft.uskat.core.gui.conn.ISkConnectionSupplier;

import ru.toxsoft.mcc.ws.mnemos.app.rt.IRtDataProvider;
import ru.toxsoft.mcc.ws.mnemos.app.rt.MccRtDataProvider;

/**
 * Панель отображения мнемосхемы.
 * <p>
 *
 * @author vs
 */
public class MccSchemeRightPanel
    extends TsPanel {

  private final MccGraphicsHolderPanel graphicsHolder;

  IRtDataProvider dataProvider;

  /**
   * Конструктор.
   *
   * @param aParent Composite - родительская компонента
   * @param aContext ITsGuiContext - контекст панели
   */
  public MccSchemeRightPanel( Composite aParent, ITsGuiContext aContext ) {
    super( aParent, aContext );
    GridLayout gl = new GridLayout( 1, false );
    gl.marginWidth = 0;
    gl.marginHeight = 0;
    setLayout( gl );

    ISkConnection skConn = aContext.get( ISkConnectionSupplier.class ).defConn();
    dataProvider = new MccRtDataProvider( skConn, aContext );

    addDisposeListener( aE -> {
      dataProvider.dispose();
    } );

    MccMainControlPanel ctrlPanel = new MccMainControlPanel( this, skConn, aContext );
    ctrlPanel.setLayoutData( new GridData( SWT.FILL, SWT.TOP, true, false ) );

    graphicsHolder = new MccGraphicsHolderPanel( this, skConn, aContext );
    graphicsHolder.setLayoutData( new GridData( SWT.FILL, SWT.FILL, true, true ) );
    // dima 21.12.22 велено отключить алармы
    // alarmsHolder = new MccAlarmsPanelHolder( this, skConn, aContext );
    // GridData gd = new GridData( SWT.FILL, SWT.FILL, true, true );
    // gd.heightHint = 150;
    // alarmsHolder.setLayoutData( gd );

    dataProvider.addDataConsumer( ctrlPanel );
  }

  public void rtStart() {
    dataProvider.start();
  }
}
