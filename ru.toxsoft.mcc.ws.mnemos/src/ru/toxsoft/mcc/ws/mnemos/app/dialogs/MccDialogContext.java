package ru.toxsoft.mcc.ws.mnemos.app.dialogs;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.uskat.core.api.objserv.*;

public class MccDialogContext
    implements ITsGuiContextable {

  private final ITsGuiContext tsContext;
  private final ISkObject     skObject;

  public MccDialogContext( ITsGuiContext aTsContext, ISkObject aSkObject ) {
    tsContext = aTsContext;
    skObject = aSkObject;
  }

  // ------------------------------------------------------------------------------------
  // ITsGuiContextable
  //

  @Override
  public ITsGuiContext tsContext() {
    return tsContext;
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Возвращает серверный объект, с которым работает диалог
   *
   * @return ISkObject - серверный объект, с которым работает диалог
   */
  public ISkObject skObject() {
    return skObject;
  }

}
