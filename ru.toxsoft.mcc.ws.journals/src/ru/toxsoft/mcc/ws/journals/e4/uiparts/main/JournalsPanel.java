package ru.toxsoft.mcc.ws.journals.e4.uiparts.main;

import static ru.toxsoft.mcc.ws.journals.e4.uiparts.main.IMmResources.*;

import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.ctx.impl.*;
import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.panels.*;
import org.toxsoft.core.tsgui.utils.layout.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.uskat.base.gui.conn.*;
import org.toxsoft.uskat.core.connection.*;

import ru.toxsoft.mcc.ws.journals.e4.uiparts.engine.*;
import ru.toxsoft.mcc.ws.mnemos.app.rt.alarm.*;

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

    setLayout( new BorderLayout() );

    TabFolder paramsFolder = new TabFolder( this, SWT.NONE );

    IM5Domain m5 = eclipseContext().get( IM5Domain.class );

    try {
      ITsGuiContext eventContext = new TsGuiContext( aContext );
      ISkConnection connection = eventContext.get( ISkConnectionSupplier.class ).defConn();
      if( !m5.models().hasKey( EventM5Model.MODEL_ID ) ) {

        m5.addModel( new EventM5Model( connection, eventContext ) );
      }
      createEventsTable( paramsFolder, eventContext );
    }
    catch( TsException ex ) {
      ex.printStackTrace();
    }

    try {
      ITsGuiContext cmdContext = new TsGuiContext( aContext );
      ISkConnection connection = cmdContext.get( ISkConnectionSupplier.class ).defConn();
      if( !m5.models().hasKey( CommandM5Model.MODEL_ID ) ) {
        m5.addModel( new CommandM5Model( connection ) );
      }
      createCommandsTable( paramsFolder, cmdContext );
    }
    catch( TsException ex ) {
      ex.printStackTrace();
    }

    try {
      ITsGuiContext alarmContext = new TsGuiContext( aContext );

      if( !m5.models().hasKey( SkAlarmM5Model.MODEL_ID ) ) {
        m5.addModel( new SkAlarmM5Model() );
      }
      createAlarmsTable( paramsFolder, alarmContext );
    }
    catch( TsException ex ) {
      ex.printStackTrace();
    }
  }

  private static void createEventsTable( TabFolder aParent, ITsGuiContext aContext )
      throws TsException {

    TabItem item = new TabItem( aParent, SWT.NONE );
    item.setText( EVENTS_STR );

    item.setControl( new EventsJournalPanel( aParent, aContext ) );
  }

  private static void createCommandsTable( TabFolder aParent, ITsGuiContext aContext )
      throws TsException {

    TabItem item = new TabItem( aParent, SWT.NONE );
    item.setText( CMDS_STR );

    item.setControl( new CommandsJournalPanel( aParent, aContext ) );
  }

  private static void createAlarmsTable( TabFolder aParent, ITsGuiContext aContext )
      throws TsException {

    TabItem item = new TabItem( aParent, SWT.NONE );
    item.setText( "Тревоги" );

    item.setControl( new AlarmsJournalPanel( aParent, aContext ) );
  }
}
