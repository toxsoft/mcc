package ru.toxsoft.mcc.ws.mnemos.app.dialogs;

import static ru.toxsoft.mcc.ws.mnemos.app.dialogs.IVjResources.*;

import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.dialogs.datarec.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.gw.gwid.*;
import org.toxsoft.uskat.core.api.objserv.*;

import ru.toxsoft.mcc.ws.mnemos.app.widgets.*;

/**
 * Панель свойств аналогового сигнала.
 * <p>
 *
 * @author vs
 */
public class PanelIrreversibleEngine
    extends AbstractMccRtPanel {

  private final ISkObject skObject;

  protected PanelIrreversibleEngine( Composite aParent, TsDialog<Object, MccDialogContext> aOwnerDialog ) {
    super( aParent, aOwnerDialog );
    skObject = environ().skObject();
    init();
    // contentPanel().rtStart();
  }

  void init() {

    GridLayout layout = new GridLayout( 1, false );
    // contentPanel().setLayout( layout );

    Composite buttonBar = new Composite( this, SWT.NONE );
    buttonBar.setLayout( new GridLayout( 2, false ) );

    GridData gd = new GridData();
    gd.widthHint = 100;
    Gwid cmdGwid = Gwid.createCmd( skObject.classId(), skObject.strid(), "cmdConfirmation" ); //$NON-NLS-1$
    OptionSet args = new OptionSet();
    args.setValue( "value", AvUtils.AV_TRUE );
    CmdPushButton btn = new CmdPushButton( buttonBar, STR_CONFIRMATION, cmdGwid, args, tsContext() );
    btn.button().setLayoutData( gd );

    Button btnSettins = new Button( buttonBar, SWT.PUSH );
    btnSettins.setText( STR_SETTINGS );
    btnSettins.setToolTipText( "Вызвать диалог настроек нереверсивного двигателя" );
    btnSettins.setLayoutData( gd );
    btnSettins.addSelectionListener( new SelectionAdapter() {

      @Override
      public void widgetSelected( SelectionEvent e ) {
        // PanelAnalogInputSettings.showDialog( environ() );
      }
    } );

  }

  /**
   * Показывает диалог Аналогового сигнала.
   *
   * @param aContext MccDialogContext - контекст диалога
   */
  public static void showDialog( MccDialogContext aContext ) {
    IDialogPanelCreator<Object, MccDialogContext> creator = PanelIrreversibleEngine::new;
    ITsGuiContext ctx = aContext.tsContext();
    Shell shell = ctx.get( Shell.class ).getShell();
    int flags = ITsDialogConstants.DF_NO_APPROVE;
    ITsDialogInfo dlgInfo = new TsDialogInfo( ctx, shell, aContext.skObject().readableName(), DLG_SETTINGS_MSG, flags );
    TsDialog<Object, MccDialogContext> d = new TsDialog<>( dlgInfo, null, aContext, creator );
    d.execData();
  }

}
