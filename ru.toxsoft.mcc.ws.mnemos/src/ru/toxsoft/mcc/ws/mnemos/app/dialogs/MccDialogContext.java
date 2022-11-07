package ru.toxsoft.mcc.ws.mnemos.app.dialogs;

import org.toxsoft.core.tsgui.bricks.ctx.*;

public class MccDialogContext
    implements ITsGuiContextable {

  private final ITsGuiContext tsContext;

  public MccDialogContext( ITsGuiContext aTsContext ) {
    tsContext = aTsContext;
  }

  // ------------------------------------------------------------------------------------
  // ITsGuiContextable
  //

  @Override
  public ITsGuiContext tsContext() {
    return tsContext;
  }

  // ITsGuiContext
}
