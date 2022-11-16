package ru.toxsoft.mcc.ws.mnemos.app;

import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.ctx.impl.*;
import org.toxsoft.core.tsgui.panels.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.uskat.base.gui.glib.*;

import ru.toxsoft.mcc.ws.mnemos.app.valed.*;

/**
 * Панель отображения мнемосхемы.
 * <p>
 *
 * @author vs
 */
public class MccSchemeRightPanel
    extends TsPanel {

  private RtValedsPanel rtPanel;

  /**
   * Конструктор.
   *
   * @param aParent Composite - родительская компонента
   * @param aContext ITsGuiContext - контекст панели
   */
  public MccSchemeRightPanel( Composite aParent, ITsGuiContext aContext ) {
    super( aParent, aContext );
    setLayout( new RowLayout( SWT.VERTICAL ) );

    CLabel labelTitle = new CLabel( this, SWT.CENTER );
    Font f = fontManager().getFont( "Arial", 24, SWT.BOLD ); //$NON-NLS-1$
    labelTitle.setFont( f );
    labelTitle.setText( "Нагнетатель №" );

    rtPanel = new RtValedsPanel( this, aContext );
    rtPanel.setLayout( null );

    // ImageDescriptor imd;
    // imd = AbstractUIPlugin.imageDescriptorFromPlugin( Activator.PLUGIN_ID, "icons/mcc-main.png" ); //$NON-NLS-1$
    // Image imgScheme = imd.createImage();
    //
    // rtPanel.addPaintListener( aEvent -> aEvent.gc.drawImage( imgScheme, 0, 0 ) );
  }

  public void addAI( int aX, int aY, String aObjStrid ) {
    ITsGuiContext ctx = new TsGuiContext( tsContext() );
    IOptionSetEdit params = ctx.params();

    ValedMccAnalogInput.OPDEF_CLASS_ID.setValue( params, AvUtils.avStr( "mcc.AnalogInput" ) ); //$NON-NLS-1$
    ValedMccAnalogInput.OPDEF_OBJ_STRID.setValue( params, AvUtils.avStr( aObjStrid ) );
    ValedMccAnalogInput.OPDEF_DATA_ID.setValue( params, AvUtils.avStr( "rtdCurrentValue" ) ); //$NON-NLS-1$

    ValedMccAnalogInput valedAI = new ValedMccAnalogInput( ctx );
    CLabel l = (CLabel)valedAI.createControl( rtPanel );
    l.setLocation( aX, aY );
    l.setSize( 50, 22 );

    rtPanel.defineRtData( valedAI.dataGwid(), valedAI );
  }

  public void rtStart() {
    rtPanel.rtStart();
  }

}
