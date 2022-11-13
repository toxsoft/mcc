package ru.toxsoft.mcc.ws.mnemos.app;

import org.eclipse.jface.resource.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.ui.plugin.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.ctx.impl.*;
import org.toxsoft.core.tsgui.panels.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.av.opset.*;

import ru.toxsoft.mcc.ws.mnemos.*;
import ru.toxsoft.mcc.ws.mnemos.app.valed.*;

/**
 * Панель отображения мнемосхемы.
 * <p>
 *
 * @author vs
 */
public class MccSchemePanel
    extends TsPanel {

  /**
   * Конструктор.
   *
   * @param aParent Composite - родительская компонента
   * @param aContext ITsGuiContext - контекст панели
   */
  public MccSchemePanel( Composite aParent, ITsGuiContext aContext ) {
    super( aParent, aContext );
    setLayout( null );

    ImageDescriptor imd;
    imd = AbstractUIPlugin.imageDescriptorFromPlugin( Activator.PLUGIN_ID, "icons/mcc-main.png" ); //$NON-NLS-1$
    Image imgScheme = imd.createImage();

    addPaintListener( aEvent -> aEvent.gc.drawImage( imgScheme, 0, 0 ) );
  }

  public void addAI( int aX, int aY, String aObjStrid ) {
    ITsGuiContext ctx = new TsGuiContext( tsContext() );
    IOptionSetEdit params = ctx.params();

    ValedMccAnalogInput.OPDEF_CLASS_ID.setValue( params, AvUtils.avStr( "mcc.AnalogInput" ) ); //$NON-NLS-1$
    ValedMccAnalogInput.OPDEF_OBJ_STRID.setValue( params, AvUtils.avStr( aObjStrid ) );
    ValedMccAnalogInput.OPDEF_DATA_ID.setValue( params, AvUtils.avStr( "currentValue" ) ); //$NON-NLS-1$

    ValedMccAnalogInput valedAI = new ValedMccAnalogInput( ctx );
    CLabel l = (CLabel)valedAI.createControl( this );
    l.setLocation( aX, aY );
    l.setSize( 50, 22 );
  }
}
