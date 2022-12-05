package ru.toxsoft.mcc.ws.reports.e4.uiparts;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.ctx.impl.*;
import org.toxsoft.core.tsgui.utils.layout.*;
import org.toxsoft.core.tsgui.widgets.*;
import org.toxsoft.uskat.base.gui.e4.uiparts.*;

/**
 * Вью работы c шаблонами графиков: список, создание, правка, удаление.
 * <p>
 *
 * @author dima
 */
public class UipartGraphTemplates
    extends SkMwsAbstractPart {

  Ts4GraphTemplateEditorPanel panel;

  @Override
  protected void doCreateContent( TsComposite aParent ) {
    ITsGuiContext ctx = new TsGuiContext( getWindowContext() );
    panel = new Ts4GraphTemplateEditorPanel( aParent, ctx );
    panel.setLayoutData( BorderLayout.CENTER );
  }

}
