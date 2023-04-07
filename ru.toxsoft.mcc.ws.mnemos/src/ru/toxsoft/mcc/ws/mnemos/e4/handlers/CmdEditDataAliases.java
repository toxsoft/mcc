package ru.toxsoft.mcc.ws.mnemos.e4.handlers;

import static ru.toxsoft.mcc.ws.mnemos.e4.handlers.ISkResources.*;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.Execute;
import org.toxsoft.core.tsgui.bricks.ctx.ITsGuiContext;
import org.toxsoft.core.tsgui.bricks.ctx.impl.TsGuiContext;
import org.toxsoft.core.tsgui.dialogs.datarec.ITsDialogInfo;
import org.toxsoft.core.tsgui.dialogs.datarec.TsDialogInfo;
import org.toxsoft.core.tsgui.m5.IM5Domain;
import org.toxsoft.core.tsgui.m5.gui.M5GuiUtils;
import org.toxsoft.core.tslib.av.metainfo.IDataDef;
import org.toxsoft.core.tslib.bricks.strid.coll.IStridablesListEdit;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.StridablesList;
import org.toxsoft.core.tslib.coll.IList;
import org.toxsoft.core.tslib.gw.gwid.Gwid;
import org.toxsoft.core.tslib.gw.skid.Skid;
import org.toxsoft.skf.ggprefs.lib.IGuiGwPrefsSection;
import org.toxsoft.skf.ggprefs.lib.ISkGuiGwPrefsService;
import org.toxsoft.skf.ggprefs.lib.impl.SkGuiGwPrefsService;
import org.toxsoft.uskat.core.ISkCoreApi;
import org.toxsoft.uskat.core.connection.ISkConnection;
import org.toxsoft.uskat.core.gui.conn.ISkConnectionSupplier;
import org.toxsoft.uskat.s5.legacy.ISkSystem;

import ru.toxsoft.mcc.ws.mnemos.app.rt.chart.PrefUtils;
import ru.toxsoft.mcc.ws.mnemos.app.rt.chart.data_aliases.MccSystemOptions;
import ru.toxsoft.mcc.ws.mnemos.app.rt.chart.data_aliases.impl.GuiDataAliasesPrefsEditLifecycleManager;
import ru.toxsoft.mcc.ws.mnemos.app.rt.chart.data_aliases.impl.GuiDataAliasesPrefsEditModel;

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
