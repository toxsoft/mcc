package ru.toxsoft.mcc.ws.mnemos.app.rtb;

import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.graphics.*;
import org.toxsoft.core.tsgui.panels.vecboard.*;
import org.toxsoft.core.tsgui.panels.vecboard.impl.*;
import org.toxsoft.core.tsgui.widgets.*;
import org.toxsoft.core.tslib.utils.*;

public class TestPanel {

  private final IVecBoard vecBoard = new VecBoard();

  static class CLabelWrapper
      extends AbstractSwtLazyWrapper<CLabel> {

    private final int swtStyle;
    private String    text = TsLibUtils.EMPTY_STRING;

    CLabelWrapper( String aText, int aSwtStyle ) {
      swtStyle = aSwtStyle;
      text = aText;
    }

    @Override
    protected CLabel doCreateControl( Composite aParent ) {
      CLabel l = new CLabel( aParent, swtStyle );
      l.setText( text );
      return l;
    }

  }

  public Composite createControl( Composite aParent ) {
    // VecColumnLayout l = new VecColumnLayout( 3, false );
    VecColumnLayout l = VecColumnLayout.createNoTrims( 3, false );

    l.addControl( new CLabelWrapper( "cell 1:1", SWT.BORDER ),
        new VecColumnLayoutData( EHorAlignment.RIGHT, EVerAlignment.BOTTOM ) );
    l.addControl( new CLabelWrapper( "cell 1:2", SWT.BORDER ), IVecColumnLayoutData.DEFAULT );
    l.addControl( new CLabelWrapper( "cell 1:3", SWT.BORDER ), IVecColumnLayoutData.DEFAULT );
    l.addControl( new CLabelWrapper( "cell 2:1", SWT.BORDER ), IVecColumnLayoutData.DEFAULT );
    l.addControl( new CLabelWrapper( "cell 2:2", SWT.BORDER ), IVecColumnLayoutData.DEFAULT );
    l.addControl( new CLabelWrapper( "cell 2:3", SWT.BORDER ), IVecColumnLayoutData.DEFAULT );
    l.addControl( new CLabelWrapper( "cell 3:1", SWT.BORDER ), IVecColumnLayoutData.DEFAULT );

    vecBoard.setLayout( l );
    return vecBoard.createControl( aParent );
  }

}
