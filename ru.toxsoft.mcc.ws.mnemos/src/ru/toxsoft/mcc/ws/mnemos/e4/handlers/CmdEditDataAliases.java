package ru.toxsoft.mcc.ws.mnemos.e4.handlers;

import static ru.toxsoft.mcc.ws.mnemos.e4.handlers.ISkResources.*;

import org.eclipse.e4.core.contexts.*;
import org.eclipse.e4.core.di.annotations.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.ctx.impl.*;
import org.toxsoft.core.tsgui.dialogs.datarec.*;
import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.gui.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.gw.gwid.*;
import org.toxsoft.core.tslib.gw.skid.*;
import org.toxsoft.uskat.base.gui.conn.*;
import org.toxsoft.uskat.core.*;
import org.toxsoft.uskat.core.connection.*;
import org.toxsoft.uskat.ggprefs.lib.*;
import org.toxsoft.uskat.ggprefs.lib.impl.*;
import org.toxsoft.uskat.s5.legacy.*;

import ru.toxsoft.mcc.ws.mnemos.app.rt.chart.*;
import ru.toxsoft.mcc.ws.mnemos.app.rt.chart.data_aliases.*;
import ru.toxsoft.mcc.ws.mnemos.app.rt.chart.data_aliases.impl.*;

/**
 * Испонитель команды "Правки названии параметров".
 *
 * @author hazard157
 * @author dima
 */
public class CmdEditDataAliases {

  private IGuiGwPrefsSection prefSection;

  private ISkConnection conn;

  private final Skid systemSkid = new Skid( ISkSystem.CLASS_ID, ISkSystem.THIS_SYSTEM );
  private final Gwid systemGwid = Gwid.createObj( ISkSystem.CLASS_ID, ISkSystem.THIS_SYSTEM );

  /**
   * ИД раздела настроек {@link IGuiGwPrefsSection} для работы с настройками мнемосхемы.
   */
  public static final String MNEMOS_PREFS_SECTION_ID = "MccMnemosSection"; //$NON-NLS-1$

  @Execute
  void exec( IEclipseContext aAppContext ) {
    ISkConnectionSupplier connSup = aAppContext.get( ISkConnectionSupplier.class );
    conn = connSup.defConn();
    ITsGuiContext ctx = new TsGuiContext( aAppContext );

    initMnemosPrefs();

    // подготовка и вызов диалога редактирования
    ITsDialogInfo dialogInfo = new TsDialogInfo( ctx, DLC_C_DATA_ALIESES_EDIT, DLC_T_DATA_ALIESES_EDIT );

    GuiDataAliasesPrefsEditModel model = new GuiDataAliasesPrefsEditModel( prefSection, systemSkid );

    IM5Domain d = conn.scope().get( IM5Domain.class );
    d.initTemporaryModel( model );

    GuiDataAliasesPrefsEditLifecycleManager manager = new GuiDataAliasesPrefsEditLifecycleManager( model, prefSection );
    M5GuiUtils.askEdit( ctx, model, systemSkid, dialogInfo, manager );
  }

  private void initMnemosPrefs() {
    ISkCoreApi coreApi = conn.coreApi();

    if( !coreApi.services().hasKey( ISkGuiGwPrefsService.SERVICE_ID ) ) {
      coreApi.addService( SkGuiGwPrefsService.CREATOR );
    }
    prefSection = PrefUtils.section( MNEMOS_PREFS_SECTION_ID, conn );

    // Задание опций
    IStridablesListEdit<IDataDef> panelPrefs = new StridablesList<>();
    panelPrefs.addAll( MccSystemOptions.allOptions() );

    IList<IDataDef> currOpDefs = prefSection.listOptionDefs( systemSkid );
    IStridablesListEdit<IDataDef> newPrefDefs = new StridablesList<>();
    // перебираем все устанавливаемые опции и добавляем только новые
    for( IDataDef addingOptDef : MccSystemOptions.allOptions() ) {
      if( !PrefUtils.hasOptionDef( currOpDefs, addingOptDef ) ) {
        newPrefDefs.add( addingOptDef );
      }
    }
    prefSection.bindOptions( systemGwid, newPrefDefs );
  }

}
