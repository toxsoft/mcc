package ru.toxsoft.mcc.ws.mnemos.app.rt.alarm;

import org.eclipse.jface.resource.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.ui.plugin.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.panels.*;
import org.toxsoft.uskat.core.connection.*;
import org.toxsoft.uskat.core.utils.*;

import ru.toxsoft.mcc.ws.mnemos.*;

/**
 * "Дурацкий" пример панели тревог.
 *
 * @author vs
 */
public class FooAlarmsPanel
    extends TsPanel
    implements ISkConnected {

  private final Image image;

  private final ISkConnection skConn;

  /**
   * Конструктор.
   *
   * @param aParent Composite - родительская компонента
   * @param aSkConn ISkConnection - соединение с сервером
   * @param aContext ITsGuiContext - соответствующий контекст
   */
  public FooAlarmsPanel( Composite aParent, ISkConnection aSkConn, ITsGuiContext aContext ) {
    super( aParent, aContext );

    skConn = aSkConn;

    ImageDescriptor imd = AbstractUIPlugin.imageDescriptorFromPlugin( Activator.PLUGIN_ID, "icons/cry.png" ); //$NON-NLS-1$
    image = imd.createImage();

    addPaintListener( aE -> {
      ImageData imageData = image.getImageData();
      aE.gc.drawImage( image, 0, 0, imageData.width, imageData.height, 0, 0, getSize().x, getSize().y );
    } );

    addDisposeListener( aE -> {
      if( image != null ) {
        image.dispose();
      }
    } );

  }

  // ------------------------------------------------------------------------------------
  // ISkConnected
  //

  @Override
  public ISkConnection skConn() {
    return skConn;
  }
}
