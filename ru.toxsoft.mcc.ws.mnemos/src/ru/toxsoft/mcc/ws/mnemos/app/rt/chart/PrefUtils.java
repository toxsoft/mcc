package ru.toxsoft.mcc.ws.mnemos.app.rt.chart;

import static ru.toxsoft.mcc.ws.mnemos.app.rt.chart.ISkResources.*;

import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.uskat.core.*;
import org.toxsoft.uskat.core.connection.*;
import org.toxsoft.uskat.ggprefs.lib.*;
import org.toxsoft.uskat.ggprefs.lib.impl.*;

/**
 * Набор вспомогательных методов для работы с настройками панели RtCharts.
 * <p>
 *
 * @author dima
 */
public class PrefUtils {

  /**
   * ИД раздела настроек {@link IGuiGwPrefsSection} для работы с настройками панели RtCharts.
   */
  public static final String RTCHARTS_PREFS_SECTION_ID = "RtChartsSection"; //$NON-NLS-1$

  /**
   * Возвращает секцию настроек панели RtCharts.
   * <p>
   * Если секция в переданном контексте отсутствует, то создает её и помещает в контекст.
   *
   * @param aConn ISkConnection - соединение с сервером
   * @return IGuiGwPrefsSection - секция настроек редактора ГДП
   */
  public static IGuiGwPrefsSection section( ISkConnection aConn ) {
    return section( RTCHARTS_PREFS_SECTION_ID, aConn );
  }

  /**
   * Возвращает секцию настроек панели RtCharts.
   * <p>
   * Если секция в переданном контексте отсутствует, то создает её и помещает в контекст.
   *
   * @param aSectionId String - ИД раздела настроек
   * @param aConn ISkConnection - соединение с сервером
   * @return IGuiGwPrefsSection - секция настроек редактора ГДП
   */
  public static IGuiGwPrefsSection section( String aSectionId, ISkConnection aConn ) {
    ISkCoreApi coreApi = aConn.coreApi();
    if( !coreApi.services().hasKey( ISkGuiGwPrefsService.SERVICE_ID ) ) {
      coreApi.addService( SkGuiGwPrefsService.CREATOR );
    }
    ISkGuiGwPrefsService service = coreApi.getService( ISkGuiGwPrefsService.SERVICE_ID );
    if( !service.listSections().hasKey( aSectionId ) ) {
      createPrefsSection( aSectionId, aConn );
    }
    return service.getSection( aSectionId );
  }

  // ------------------------------------------------------------------------------------
  // Внутренняя реализация
  //

  private static IGuiGwPrefsSection createPrefsSection( String aSectionId, ISkConnection aConn ) {
    ISkGuiGwPrefsService prefServ = aConn.coreApi().getService( ISkGuiGwPrefsService.SERVICE_ID );
    if( !prefServ.listSections().hasKey( aSectionId ) ) {
      IDpuGuiGwPrefsSectionDef sd;
      String name = STR_N_RTCHARTS_PREFS_SECT;
      String descr = STR_D_RTCHARTS_PREFS_SECT;
      sd = new DpuGuiGwPrefsSectionDef( aSectionId, name, descr, IOptionSet.NULL );
      prefServ.defineSection( sd );
    }
    return prefServ.getSection( aSectionId );
  }

  /**
   * Запрет на создание экземпляров
   */
  private PrefUtils() {
    // nop
  }
}
