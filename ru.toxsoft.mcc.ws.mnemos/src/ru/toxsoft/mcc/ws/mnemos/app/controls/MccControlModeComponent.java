package ru.toxsoft.mcc.ws.mnemos.app.controls;

import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.bricks.strid.more.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.gw.gwid.*;
import org.toxsoft.uskat.core.api.objserv.*;

/**
 * Копонента, отображающая режим управления (АРМ, панель и т.д.)
 * <p>
 *
 * @author vs
 */
public class MccControlModeComponent
    extends AbstractMccControl {

  private Composite bkPanel = null;

  private CLabel modeLabel = null;

  private final IMapEdit<Gwid, String> modeNames = new ElemMap<>();

  MccControlModeComponent( ISkObject aSkObject, ITsGuiContext aTsContext, IdChain aConnId ) {
    super( aSkObject, "rtdAwpCtrl", aTsContext, aConnId ); //$NON-NLS-1$
    Gwid gwid;
    gwid = Gwid.createRtdata( skObject().classId(), skObject().strid(), "rtdAwpCtrl" ); //$NON-NLS-1$
    modeNames.put( gwid, dataInfo( gwid.propId() ).nmName() );
    gwid = Gwid.createRtdata( skObject().classId(), skObject().strid(), "rtdPanelCtrl" ); //$NON-NLS-1$
    modeNames.put( gwid, dataInfo( gwid.propId() ).nmName() );
    gwid = Gwid.createRtdata( skObject().classId(), skObject().strid(), "rtdLocalCtrl" ); //$NON-NLS-1$
    modeNames.put( gwid, dataInfo( gwid.propId() ).nmName() );
    gwid = Gwid.createRtdata( skObject().classId(), skObject().strid(), "rtdAutoCtrl" ); //$NON-NLS-1$
    modeNames.put( gwid, dataInfo( gwid.propId() ).nmName() );
  }

  // ------------------------------------------------------------------------------------
  // IRtDataConsumer
  //

  @Override
  public IGwidList listNeededGwids() {
    GwidList gl = new GwidList();
    for( Gwid gwid : modeNames.keys() ) {
      gl.add( gwid );
    }
    return gl;
  }

  @Override
  public void setValues( Gwid[] aGwids, IAtomicValue[] aValues, int aCount ) {
    String strMode = "None"; //$NON-NLS-1$
    for( int i = 0; i < aCount; i++ ) {
      if( aValues[i].isAssigned() && aValues[i].asBool() ) {
        strMode = modeNames.getByKey( aGwids[i] );
      }
    }
    modeLabel.setText( strMode );
  }

  // ------------------------------------------------------------------------------------
  // AbstractMccControl
  //

  @Override
  public Control createControl( Composite aParent, int aSwtStyle ) {
    bkPanel = new Composite( aParent, aSwtStyle );
    GridLayout gl = new GridLayout( 2, false );
    gl.marginLeft = 0;
    gl.marginTop = 0;
    gl.marginRight = 0;
    gl.marginBottom = 0;
    gl.verticalSpacing = 0;
    gl.horizontalSpacing = 0;
    bkPanel.setLayout( gl );

    CLabel l = new CLabel( bkPanel, SWT.CENTER );
    l.setText( "Режим yправления: " );

    modeLabel = new CLabel( bkPanel, SWT.BORDER );
    GridData gd = new GridData();
    gd.widthHint = 180;
    modeLabel.setLayoutData( gd );

    return bkPanel;
  }

  @Override
  public Control getControl() {
    return bkPanel;
  }

  @Override
  public void update() {

  }

}
