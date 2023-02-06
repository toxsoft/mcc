package ru.toxsoft.mcc.ws.mnemos.app.dialogs;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

/**
 * Окно диалога настройки какого-либо объекта проекта МосКокс.
 * <p>
 *
 * @author vs
 */
public class MccDialogWindow {

  Shell wnd;

  public MccDialogWindow( Shell aParent, String aCaption ) {

    Shell[] shells = aParent.getDisplay().getShells();
    for( Shell child : shells ) {
      System.out.println( "shell: " + child.getText() );
      if( aCaption.equals( child.getText() ) ) {
        child.dispose();
        break;
        // child.setFocus();
        // return;
      }
    }

    // wnd = new Shell( aParent, SWT.ON_TOP | SWT.CLOSE | SWT.RESIZE );
    wnd = new Shell( aParent, SWT.CLOSE | SWT.RESIZE );
    wnd.setLayout( new FillLayout() );
    wnd.setText( aCaption );

    // wnd.open();
  }

  public void layout() {
    wnd.layout();
    wnd.pack();
    Rectangle dr = wnd.getDisplay().getClientArea();
    Point wSize = wnd.getSize();
    // wnd.setLocation( (dr.width - wSize.x) / 2, (dr.height - wSize.y) / 2 );
    wnd.setLocation( dr.width / 2 + wSize.x / 2, (dr.height - wSize.y) / 2 );
  }

  public Shell shell() {
    return wnd;
  }

  public void open() {
    layout();
    wnd.open();
  }

}
