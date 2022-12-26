package ru.toxsoft.mcc.ws.journals.e4.uiparts.main;

import static ru.toxsoft.mcc.ws.journals.e4.uiparts.main.IMmResources.*;

import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.panels.*;
import org.toxsoft.core.tsgui.utils.layout.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.uskat.base.gui.conn.*;
import org.toxsoft.uskat.core.connection.*;

import ru.toxsoft.mcc.ws.journals.e4.uiparts.engine.*;

/**
 * Панель журналов (событий, команд и т.д.)
 *
 * @author max
 */
public class JournalsPanel
    extends TsPanel {

  /**
   * @param aParent родительский компонент
   * @param aContext контекст
   */
  public JournalsPanel( Composite aParent, ITsGuiContext aContext ) {
    super( aParent, aContext );

    initFromContext();
    setLayout( new BorderLayout() );

    TabFolder paramsFolder = new TabFolder( this, SWT.NONE );

    try {
      createEventsTable( paramsFolder );
    }
    catch( TsException ex ) {
      ex.printStackTrace();
    }

    // try {
    // createCommandsTable( paramsFolder );
    // }
    // catch( TsException ex ) {
    // ex.printStackTrace();
    // }
  }

  private void initFromContext() {
    IM5Domain m5 = eclipseContext().get( IM5Domain.class );

    ISkConnection connection = tsContext().get( ISkConnectionSupplier.class ).defConn();

    if( !m5.models().hasKey( EventM5Model.MODEL_ID ) ) {

      m5.addModel( new EventM5Model( connection, tsContext() ) );
    }

    if( !m5.models().hasKey( CommandM5Model.MODEL_ID ) ) {
      m5.addModel( new CommandM5Model( connection ) );
    }
  }

  private void createEventsTable( TabFolder aParent )
      throws TsException {

    TabItem item = new TabItem( aParent, SWT.NONE );
    item.setText( EVENTS_STR );

    item.setControl( new EventsJournalPanel( aParent, tsContext() ) );
  }

  private void createCommandsTable( TabFolder aParent )
      throws TsException {

    TabItem item = new TabItem( aParent, SWT.NONE );
    item.setText( CMDS_STR );

    item.setControl( new CommandsJournalPanel( aParent, tsContext() ) );
  }
}
