package ru.toxsoft.mcc.ws.mnemos.app;

import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.panels.*;
import org.toxsoft.core.tslib.gw.skid.*;
import org.toxsoft.uskat.core.api.objserv.*;
import org.toxsoft.uskat.core.connection.*;
import org.toxsoft.uskat.core.utils.*;

import ru.toxsoft.mcc.ws.mnemos.app.dialogs.*;

public class MccMainControlPanel
    extends TsPanel
    implements ISkConnected {

  private final ISkConnection skConn;

  public MccMainControlPanel( Composite aParent, ITsGuiContext aContext, ISkConnection aSkConn ) {
    super( aParent, aContext );

    skConn = aSkConn;

    GridLayout gl = new GridLayout( 1, false );
    setLayout( gl );

    GridData gd = new GridData();
    gd.widthHint = 80;

    Composite buttonBar = new Composite( this, SWT.NONE );
    gl = new GridLayout( 4, false );
    gl.marginHeight = 0;
    gl.verticalSpacing = 0;
    gl.marginTop = 0;
    gl.marginBottom = 0;
    buttonBar.setLayout( gl );
    // buttonBar.setBackground( colorManager().getColor( ETsColor.RED ) );

    CLabel l = new CLabel( buttonBar, SWT.NONE );
    l.setText( "Режим управления: " );

    Button btnArm = new Button( buttonBar, SWT.TOGGLE );
    btnArm.setText( "АРМ" );
    btnArm.setLayoutData( gd );

    Button btnPanel = new Button( buttonBar, SWT.TOGGLE );
    btnPanel.setText( "Панель" );
    btnPanel.setLayoutData( gd );

    Button btnAuto = new Button( buttonBar, SWT.TOGGLE );
    btnAuto.setText( "Автомат" );
    btnAuto.setLayoutData( gd );

    createLaunchPanel( this );
  }

  // ------------------------------------------------------------------------------------
  // ISkConnected
  //

  @Override
  public ISkConnection skConn() {
    return skConn;
  }

  // ------------------------------------------------------------------------------------
  // Implementation
  //

  private void createLaunchPanel( Composite aParent ) {
    Group stepHolder = new Group( this, SWT.NONE );
    stepHolder.setText( "Запуск" );
    stepHolder.setLayoutData( new GridData( SWT.FILL, SWT.TOP, true, false ) );
    stepHolder.setLayout( new GridLayout( 3, false ) );
    FontData fd = stepHolder.getFont().getFontData()[0];
    Font f = fontManager().getFont( fd.getName(), fd.getHeight(), SWT.BOLD );
    stepHolder.setFont( f );

    GridData gd = new GridData();
    gd.widthHint = 50;

    CLabel l = new CLabel( stepHolder, SWT.NONE );
    l.setText( "Шаг: " );

    l = new CLabel( stepHolder, SWT.CENTER | SWT.BORDER );
    l.setLayoutData( gd );

    l = new CLabel( stepHolder, SWT.CENTER | SWT.BORDER );
    l.setLayoutData( new GridData( SWT.FILL, SWT.TOP, true, false ) );

    Composite buttonsHolder = new Composite( stepHolder, SWT.NONE );
    GridLayout gl = new GridLayout( 3, false );
    gl.marginHeight = 0;
    gl.marginWidth = 0;
    buttonsHolder.setLayout( gl );
    buttonsHolder.setLayoutData( new GridData( SWT.FILL, SWT.TOP, true, false, 3, 1 ) );
    // buttonsHolder.setBackground( colorManager().getColor( ETsColor.RED ) );

    Button btnStart = new Button( buttonsHolder, SWT.PUSH );
    btnStart.setText( "Старт АВТ" );
    // btnStart.setLayoutData( new GridData( SWT.LEFT, SWT.TOP, true, false ) );

    Button btnStop = new Button( buttonsHolder, SWT.PUSH );
    btnStop.setText( "Стоп АВТ" );
    btnStop.setLayoutData( new GridData( SWT.LEFT, SWT.TOP, true, false ) );

    Button btnBlocks = new Button( buttonsHolder, SWT.PUSH );
    btnBlocks.setText( "Блокировки" );
    btnBlocks.setLayoutData( new GridData( SWT.RIGHT, SWT.TOP, true, false ) );
    btnBlocks.addSelectionListener( new SelectionAdapter() {

      @Override
      public void widgetSelected( SelectionEvent aE ) {
        ISkObject skObj = coreApi().objService().find( new Skid( "mcc.CtrlSystem", "n2CtrlSystem" ) );
        MccDialogContext ctx = new MccDialogContext( tsContext(), skObj );
        MccBlockingPanel.showDialog( ctx );
      }
    } );
  }

}
