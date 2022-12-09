package ru.toxsoft.mcc.ws.mnemos.e4.uiparts;

import java.awt.*;

import org.eclipse.jface.dialogs.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.widgets.Composite;
import org.toxsoft.core.tsgui.utils.layout.*;
import org.toxsoft.core.tsgui.utils.layout.BorderLayout;
import org.toxsoft.core.tsgui.widgets.*;
import org.toxsoft.core.tslib.utils.logs.impl.*;
import org.toxsoft.uskat.base.gui.conn.*;
import org.toxsoft.uskat.base.gui.e4.uiparts.*;
import org.toxsoft.uskat.core.api.objserv.*;
import org.toxsoft.uskat.core.connection.*;

import ru.toxsoft.mcc.ws.mnemos.app.*;
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
  protected void init( final Composite aParent ) {

    aParent.setLayout( new BorderLayout() );

    // Composite schemeComp = new Composite( aParent, SWT.NONE );
    // schemeComp.setLayoutData( BorderLayout.CENTER );
    //
    // ImageDescriptor imd;
    // imd = AbstractUIPlugin.imageDescriptorFromPlugin( Activator.PLUGIN_ID, "icons/mcc-main.png" );
    // Image imgScheme = imd.createImage();
    //
    // schemeComp.addPaintListener( aEvent -> aEvent.gc.drawImage( imgScheme, 0, 0 ) );

    MccSchemePanel schemeComp = new MccSchemePanel( aParent, tsContext() );
    schemeComp.addAI( 145, 122, "n2AI_TP9" ); //$NON-NLS-1$
    schemeComp.addAI( 373, 119, "n2AI_TP8" ); //$NON-NLS-1$
    schemeComp.addAI( 464, 120, "n2AI_TP7" ); //$NON-NLS-1$
    schemeComp.addAI( 644, 121, "n2AI_TP6" ); //$NON-NLS-1$
    schemeComp.addAI( 466, 236, "n2AI_TP5" ); //$NON-NLS-1$
    schemeComp.addAI( 645, 235, "n2AI_TP4" ); //$NON-NLS-1$
    schemeComp.addAI( 737, 145, "n2AI_TP3" ); //$NON-NLS-1$
    schemeComp.addAI( 737, 229, "n2AI_TP2" ); //$NON-NLS-1$
    schemeComp.addAI( 917, 274, "n2AI_TP1" ); //$NON-NLS-1$

    schemeComp.addAI( 146, 210, "n2AI_ST8" ); //$NON-NLS-1$
    schemeComp.addAI( 374, 212, "n2AI_ST7" ); //$NON-NLS-1$
    schemeComp.addAI( 521, 121, "n2AI_ST6" ); //$NON-NLS-1$
    schemeComp.addAI( 587, 120, "n2AI_ST5" ); //$NON-NLS-1$
    schemeComp.addAI( 522, 235, "n2AI_ST4" ); //$NON-NLS-1$
    schemeComp.addAI( 590, 236, "n2AI_ST3" ); //$NON-NLS-1$
    schemeComp.addAI( 793, 120, "n2AI_ST2" ); //$NON-NLS-1$
    schemeComp.addAI( 915, 121, "n2AI_ST1" ); //$NON-NLS-1$

    schemeComp.addAI( 479, 638, "n2AI_TM2" ); //$NON-NLS-1$
    schemeComp.addAI( 484, 772, "n2AI_TM1" ); //$NON-NLS-1$

    schemeComp.addAI( 487, 377, "n2AI_P1" ); //$NON-NLS-1$
    schemeComp.addAI( 568, 377, "n2AI_P2" ); //$NON-NLS-1$
    schemeComp.addAI( 143, 555, "n2AI_P3" ); //$NON-NLS-1$
    schemeComp.addAI( 411, 770, "n2AI_P5" ); //$NON-NLS-1$
    schemeComp.addAI( 439, 467, "n2AI_P7" ); //$NON-NLS-1$
    schemeComp.addAI( 1176, 514, "n2AI_P9" ); //$NON-NLS-1$
    schemeComp.addAI( 1254, 211, "n2AI_P14" ); //$NON-NLS-1$
    schemeComp.addAI( 1029, 346, "n2AI_P62" ); //$NON-NLS-1$
    schemeComp.addAI( 1154, 347, "n2AI_P61" ); //$NON-NLS-1$

    schemeComp.addAI( 1322, 308, "n2AI_DDZ" ); //$NON-NLS-1$

    schemeComp.addAI( 867, 183, "n2AI_T15" ); //$NON-NLS-1$
    schemeComp.addAI( 1178, 594, "n2AI_T14" ); //$NON-NLS-1$
    schemeComp.addAI( 1414, 237, "n2AI_T13" ); //$NON-NLS-1$
    schemeComp.addAI( 423, 293, "n2AI_T9" ); //$NON-NLS-1$
    schemeComp.addAI( 369, 294, "n2AI_T8" ); //$NON-NLS-1$
    schemeComp.addAI( 146, 274, "n2AI_T7" ); //$NON-NLS-1$
    schemeComp.addAI( 115, 378, "n2AI_T3" ); //$NON-NLS-1$

    MccSchemeRightPanel rightComp = new MccSchemeRightPanel( aParent, tsContext() );
    rightComp.setLayoutData( BorderLayout.EAST );
    rightComp.setData( AWTLayout.KEY_PREFERRED_SIZE, new Dimension( 440, -1 ) );

    schemeComp.rtStart();

    schemeComp.addMouseListener( new MouseListener() {

      @Override
      public void mouseUp( MouseEvent aE ) {
        // TODO Auto-generated method stub

      }

      @Override
      public void mouseDown( MouseEvent aE ) {
        if( aE.button == 3 ) {
          InputDialog dlg = new InputDialog( getShell(), "Создать контроль для AI", "Имя объекта:", "", null );
          if( dlg.open() == org.eclipse.jface.window.Window.OK ) {
            String objStrid = "n2AI_" + dlg.getValue(); //$NON-NLS-1$
            // System.out.println( "x = " + aE.x + "; y = " + aE.y + "; Strid: " + objStrid );
            String str = String.format( "schemeComp.addAI(%s, %s, \"%s\" );", aE.x, aE.y, objStrid );
            System.out.println( str );
          }
        }
      }

      @Override
      public void mouseDoubleClick( MouseEvent aE ) {
        ISkObject skObj = PanelSelectAnalogInput.selectAnalogInput( tsContext() );
        if( skObj != null ) {
          PanelAnalogInput.showDialog( new MccDialogContext( tsContext(), skObj ) );
        }
      }
    } );
  }
}
