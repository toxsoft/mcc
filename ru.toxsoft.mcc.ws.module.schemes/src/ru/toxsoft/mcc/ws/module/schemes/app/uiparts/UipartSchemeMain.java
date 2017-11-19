package ru.toxsoft.mcc.ws.module.schemes.app.uiparts;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

import ru.toxsoft.s5.client.e4.AbstractE4S5Uipart;
import ru.toxsoft.tsgui.dialogs.TsDialogUtils;

/**
 * Вью главной мнемосхемы.
 *
 * @author goga
 */
public class UipartSchemeMain
    extends AbstractE4S5Uipart {

  @Override
  protected void doDoInit( Composite aParent ) {

    Button b = new Button( aParent, SWT.PUSH );
    b.setText( "MAIN SCHEME - Главная мнемосхема" ); //$NON-NLS-1$
    b.addSelectionListener( new SelectionAdapter() {

      @Override
      public void widgetSelected( SelectionEvent e ) {
        TsDialogUtils.underDevelopment( getShell() );
      }

    } );

  }

}
