package ru.toxsoft.mcc.ws.reports.e4.uiparts;

import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.dialogs.*;
import org.toxsoft.core.tsgui.mws.bases.*;

/**
 * Временная заглушка.
 *
 * @author hazard157
 */
public class UipartReportsFoo
    extends MwsAbstractPart {

  @Override
  protected void doInit( Composite aParent ) {

    Button b = new Button( aParent, SWT.PUSH );
    b.setText( getClass().getName() );
    b.addSelectionListener( new SelectionAdapter() {

      @Override
      public void widgetSelected( SelectionEvent e ) {
        TsDialogUtils.underDevelopment( getShell() );
      }

    } );

  }

}
