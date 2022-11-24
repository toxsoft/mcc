package ru.toxsoft.mcc.ws.mnemos.app.controls;

import org.eclipse.swt.custom.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.bricks.strid.more.*;
import org.toxsoft.uskat.core.api.objserv.*;

/**
 * Однострочное нередактируемое текстовое поле для отображения значения РВ-данного для проекта МосКокс.
 * <p>
 *
 * @author vs
 */
public class MccRtLabel
    extends AbstractMccSingleDataControl {

  CLabel label = null;

  public MccRtLabel( ISkObject aSkObject, String aDataId, ITsGuiContext aTsContext, IdChain aConnId ) {
    super( aSkObject, aDataId, aTsContext, aConnId );
  }

  @Override
  public Control createControl( Composite aParent, int aSwtStyle ) {
    label = new CLabel( aParent, aSwtStyle );
    return label;
  }

  @Override
  public Control getControl() {
    return label;
  }

  @Override
  public void update() {
    label.setText( AvUtils.printAv( formatString( valueType ), value ) );
  }

}
