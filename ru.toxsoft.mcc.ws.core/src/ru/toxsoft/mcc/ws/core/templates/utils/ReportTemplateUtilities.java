package ru.toxsoft.mcc.ws.core.templates.utils;

import static org.toxsoft.core.tsgui.m5.IM5Constants.*;
import static org.toxsoft.core.tslib.av.EAtomicType.*;
import static org.toxsoft.uskat.core.api.hqserv.ISkHistoryQueryServiceConstants.*;

import java.text.*;
import java.util.*;

import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.chart.api.*;
import org.toxsoft.core.tsgui.chart.impl.*;
import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.model.*;
import org.toxsoft.core.tsgui.m5.model.impl.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.av.temporal.*;
import org.toxsoft.core.tslib.bricks.filter.*;
import org.toxsoft.core.tslib.bricks.time.*;
import org.toxsoft.core.tslib.bricks.time.impl.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.core.tslib.gw.gwid.*;
import org.toxsoft.uskat.core.api.hqserv.*;
import org.toxsoft.uskat.core.impl.dto.*;

import ru.toxsoft.mcc.ws.core.templates.api.*;

/**
 * Утилитный класс для работы с шаблонами
 *
 * @author Max
 * @author dima
 */
public class ReportTemplateUtilities {

  static final String QUERY_PARAM_ID_FORMAT = "param%d";

  /**
   * Формат идентификатора столбца модели - в качестве параметра - номер столбца, начиная с 0 [int].
   */
  public static final String MODEL_COLUMN_ID_FORMAT = "column%d"; //$NON-NLS-1$

  public static final String MODEL_TIME_COLUMN_ID_FORMAT = "timeColumn%d"; //$NON-NLS-1$

  /**
   * Формирует параметры запроса на основе информации из шаблона отчёта
   *
   * @param aReportTemplate ISkReportTemplate - шаблон отчёта
   * @return IStringMap<IDtoQueryParam> - массив параметров отчёта (одна сущность на одно запрашиваемое данное) -
   *         идентификатор каждой сущности имеет формат paramN, где N - номер, начиная с нуля, совпадающий с порядком
   *         параметров в шаблоне (param0, param1, param2 ...)
   */
  public static IStringMap<IDtoQueryParam> formQueryParams( ISkReportTemplate aReportTemplate ) {
    return template2Query( aReportTemplate.listParams(),
        Integer.valueOf( (int)aReportTemplate.aggrStep().timeInMills() ) );
  }

  /**
   * Формирует параметры запроса на основе информации из шаблона графика
   *
   * @param aGraphTemplate { {@link ISkGraphTemplate } - шаблон графика
   * @return IStringMap<IDtoQueryParam> - массив параметров отчёта (одна сущность на одно запрашиваемое данное) -
   *         идентификатор каждой сущности имеет формат paramN, где N - номер, начиная с нуля, совпадающий с порядком
   *         параметров в шаблоне (param0, param1, param2 ...)
   */
  public static IStringMap<IDtoQueryParam> formQueryParams( ISkGraphTemplate aGraphTemplate ) {
    return template2Query( aGraphTemplate.listParams(),
        Integer.valueOf( (int)aGraphTemplate.aggrStep().timeInMills() ) );
  }

  /**
   * По списку параметров шаблона формирует карту параметров для запроса к сервису запросов
   *
   * @param aTemplateParams параметры шаблона
   * @param aAggrStep шаг агрегации
   * @return карта параметров запроса к одноименному сервису
   */
  private static IStringMap<IDtoQueryParam> template2Query( IList<? extends ISkTemplateParam> aTemplateParams,
      Integer aAggrStep ) {
    IStringMapEdit<IDtoQueryParam> result = new StringMap<>();

    for( int i = 0; i < aTemplateParams.size(); i++ ) {
      ISkTemplateParam param = aTemplateParams.get( i );

      Gwid gwid = param.gwid();
      ITsCombiFilterParams filter = ITsCombiFilterParams.ALL;

      EAggregationFunc aggrFunc = param.aggrFunc();
      String funcId = convertFunc( aggrFunc );

      IOptionSetEdit funcArgs = new OptionSet();
      // задаем интервал агрегации
      funcArgs.setInt( HQFUNC_ARG_AGGREGAION_INTERVAL, aAggrStep.intValue() );

      IDtoQueryParam qParam = DtoQueryParam.create( gwid, filter, funcId, funcArgs );

      result.put( String.format( QUERY_PARAM_ID_FORMAT, Integer.valueOf( i ) ), qParam );
    }

    return result;
  }

  /**
   * Creates M5 Model according to report template params.
   *
   * @param aReportTemplate
   * @return
   */
  public static IM5Model<IStringMap<IAtomicValue>> createM5ModelForTemplate( ISkReportTemplate aReportTemplate ) {
    return new ReportM5Model( aReportTemplate );
  }

  /**
   * Creates M5 Model according to report template params.
   *
   * @param aReportTemplate
   * @param aReportData
   * @return
   */
  public static IM5ItemsProvider<IStringMap<IAtomicValue>> createM5ItemProviderForTemplate(
      ISkReportTemplate aReportTemplate, IList<ITimedList<?>> aReportData ) {
    return new ReportM5ItemProvider( aReportTemplate, aReportData );
  }

  private static String convertFunc( EAggregationFunc aAggrFunc ) {
    switch( aAggrFunc ) {
      case AVERAGE:
        return HQFUNC_ID_AVERAGE;
      case MAX:
        return HQFUNC_ID_MAX;
      case MIN:
        return HQFUNC_ID_MIN;
      case COUNT:
        return HQFUNC_ID_COUNT;
      case SUM:
        return HQFUNC_ID_SUM;
      default:
        break;
    }
    return null;
  }

  /**
   * По шаблону графика и результату запроса к сервису отчетов создает список наборов данных для графической компоненты
   *
   * @param aGraphTemplate {@link ISkGraphTemplate} - шаблон графика
   * @param aReportData - результат запроса к сервису отчетов
   * @return - список наборов данных для графика
   */
  public static IList<IG2DataSet> createG2DataSetList( ISkGraphTemplate aGraphTemplate,
      IList<ITimedList<?>> aReportData ) {
    IListEdit<IG2DataSet> retVal = new ElemArrayList<>();
    IList<ISkGraphParam> graphParams = aGraphTemplate.listParams();
    // создаем нужные наборы данных
    for( int i = 0; i < graphParams.size(); i++ ) {
      ISkGraphParam graphParam = graphParams.get( i );
      String gdsId = graphDataSetId( graphParam );
      G2HistoryDataSet dataSet = new G2HistoryDataSet( gdsId );
      retVal.add( dataSet );
      // наполняем его данными
      ITimedList<?> timedList = aReportData.get( i );
      dataSet.setValues( convertList2List( timedList ) );
    }
    return retVal;
  }

  /**
   * Выдает id для набора данных одного графика. Использует gwid параметра и функцию агрегации.
   *
   * @param aGraphParam {@link ISkGraphParam } - описание одного параметра графика
   * @return строка id набора данных
   */
  public static String graphDataSetId( ISkGraphParam aGraphParam ) {
    return String.format( "%s_%s_%s_%s", aGraphParam.gwid().classId(), aGraphParam.gwid().strid(), //$NON-NLS-1$
        aGraphParam.gwid().propId(), aGraphParam.aggrFunc().id() );
  }

  /**
   * Из набора данных результата запроса готовит набор данных одного графика
   *
   * @param aTimedList ответ сервиса данных
   * @return набор данных одного графика
   */
  private static IList<ITemporalAtomicValue> convertList2List( ITimedList<?> aTimedList ) {
    IListEdit<ITemporalAtomicValue> retVal = new ElemArrayList<>();
    for( Object value : aTimedList ) {
      if( value instanceof TemporalAtomicValue tav ) {
        retVal.add( tav );
      }
    }
    return retVal;
  }

  /**
   * Standart M5 model of a report, fields have the same meaning as parametors of a report template
   *
   * @author max
   */
  static class ReportM5Model
      extends M5Model<IStringMap<IAtomicValue>> {

    /**
     * Model Id.
     */
    public static final String MODEL_ID_FORMAT = "org.toxsoft.uskat.reptempl.utils.ReportM5Model.%s"; //$NON-NLS-1$

    private ISkReportTemplate reportTemplate;

    /**
     * Конструктор модели по шаблону отчёта.
     *
     * @param aReportTemplate ISkReportTemplate - report template.
     */
    public ReportM5Model( ISkReportTemplate aReportTemplate ) {
      super( generateId( aReportTemplate ), (Class)IStringMap.class );

      reportTemplate = aReportTemplate;

      IListEdit<IM5FieldDef<IStringMap<IAtomicValue>, ?>> fDefs = new ElemArrayList<>();

      IList<ISkReportParam> reportParams = aReportTemplate.listParams();

      for( int i = 0; i < reportParams.size(); i++ ) {
        ISkReportParam param = reportParams.get( i );

        String fieldName = param.title();
        String fieldDescr = param.description();
        EDisplayFormat displayFormat = param.displayFormat();

        String timeId = String.format( MODEL_TIME_COLUMN_ID_FORMAT, Integer.valueOf( i ) );

        M5AttributeFieldDef<IStringMap<IAtomicValue>> jointModelTimeField =
            createFieldDef( timeId, "time_" + fieldName, "time_" + fieldDescr, displayFormat );
        fDefs.add( jointModelTimeField );

        String id = String.format( MODEL_COLUMN_ID_FORMAT, Integer.valueOf( i ) );

        M5AttributeFieldDef<IStringMap<IAtomicValue>> jointModelField =
            createFieldDef( id, fieldName, fieldDescr, displayFormat );
        fDefs.add( jointModelField );
      }

      addFieldDefs( fDefs );
    }

    /**
     * Returns report template.
     *
     * @return ISkReportTemplate - report template.
     */
    public ISkReportTemplate getReportTemplate() {
      return reportTemplate;
    }

    public static String generateId( ISkReportTemplate aReportTemplate ) {
      return String.format( MODEL_ID_FORMAT, aReportTemplate.id() );
    }

    /**
     * TODO
     *
     * @param aId
     * @param aNmName
     * @param aDescr
     * @param aDisplayFormat
     * @return
     */
    private static M5AttributeFieldDef<IStringMap<IAtomicValue>> createFieldDef( String aId, String aNmName,
        String aDescr, EDisplayFormat aDisplayFormat ) {

      M5AttributeFieldDef<IStringMap<IAtomicValue>> fDef = new M5AttributeFieldDef<>( aId, STRING ) {

        @Override
        protected IAtomicValue doGetFieldValue( IStringMap<IAtomicValue> aEntity ) {
          return aEntity.findByKey( id() );
        }

      };

      fDef.setNameAndDescription( aNmName, aDescr );
      fDef.setDefaultValue( IAtomicValue.NULL );
      fDef.setFlags( M5FF_COLUMN | M5FF_READ_ONLY );
      return fDef;
    }
  }

  static class ReportM5ItemProvider
      extends M5DefaultItemsProvider<IStringMap<IAtomicValue>> {

    ISkReportTemplate    reportTemplate;
    IList<ITimedList<?>> reportData;

    /**
     * Конструктор по объединённой модели и массиву поставщиков, упорядоченных в соответствии с моделями.
     *
     * @param aJointModels JointM5Model - объединённая модель.
     * @param aProviders IM5ItemsProvider<?>[] - массив поставщиков, упорядоченных в соответствии с моделями.
     * @param aExcludedFields - исключаемые из модели столбцы
     */
    public ReportM5ItemProvider( ISkReportTemplate aReportTemplate, IList<ITimedList<?>> aReportData ) {
      super();
      reportTemplate = aReportTemplate;
      reportData = aReportData;

      formItems();
    }

    private void formItems() {
      items().clear();

      IList<ISkReportParam> reportParams = reportTemplate.listParams();

      for( int j = 0; j < reportData.first().size(); j++ ) {

        IStringMapEdit<IAtomicValue> rowValues = new StringMap<>();
        for( int i = 0; i < reportParams.size(); i++ ) {

          ISkReportParam param = reportParams.get( i );

          EDisplayFormat displayFormat = param.displayFormat();

          ITimedList<?> timedList = reportData.get( i );

          Object val = timedList.get( j );

          String strVal = convertValue( val, displayFormat );

          String strTime = convertTime( val );

          String timeId = String.format( MODEL_TIME_COLUMN_ID_FORMAT, Integer.valueOf( i ) );

          rowValues.put( timeId, AvUtils.avStr( strTime ) );

          String id = String.format( MODEL_COLUMN_ID_FORMAT, Integer.valueOf( i ) );

          rowValues.put( id, AvUtils.avStr( strVal ) );
        }

        items().add( rowValues );
      }
    }
  }

  /**
   * Implementaiotn of Single data argument.
   *
   * @author max
   */
  static class DtoQueryParamImpl2
      implements IDtoQueryParam {

    private Gwid dataGwid;

    private ITsCombiFilterParams filter;

    private String funcId;

    private IOptionSetEdit funcArgs;

    public DtoQueryParamImpl2( Gwid aGwid, ITsCombiFilterParams aFilter, String aFuncId, IOptionSetEdit aFuncArgs ) {
      dataGwid = aGwid;
      filter = aFilter;
      funcId = aFuncId;
      funcArgs = aFuncArgs;
    }

    @Override
    public Gwid dataGwid() {
      return dataGwid;
    }

    @Override
    public ITsCombiFilterParams filterParams() {
      return filter;
    }

    @Override
    public String funcId() {
      return funcId;
    }

    @Override
    public IOptionSet funcArgs() {
      return funcArgs;
    }

  }

  public static String convertValue( Object aVal, EDisplayFormat aDisplayFormat ) {
    if( aVal instanceof TemporalAtomicValue ) {

      IAtomicValue value = ((TemporalAtomicValue)aVal).value();

      if( value != null && value.isAssigned() && value.atomicType() == EAtomicType.FLOATING ) {
        return String.format( aDisplayFormat.format(), Double.valueOf( value.asDouble() ) );
      }

      if( value != null ) {
        return value.asString();
      }
    }
    return aVal.toString();
  }

  public static String convertTime( Object aVal ) {
    if( aVal instanceof TemporalAtomicValue ) {

      DateFormat formatter = new SimpleDateFormat( "MM.dd HH:mm:ss" );

      return formatter.format( new Date( ((TemporalAtomicValue)aVal).timestamp() ) );
    }
    return "withoutTime";
  }

  public static IList<ITimedList<?>> createResult( ISkQueryProcessedData aProcessData,
      IStringMap<IDtoQueryParam> aQueryParams ) {
    IListEdit<ITimedList<?>> result = new ElemArrayList<>();

    for( String paramKey : aQueryParams.keys() ) {
      while( !aProcessData.isArgDataReady( paramKey ) ) {
        try {
          Display.getCurrent().readAndDispatch();
          Thread.sleep( 10L );
        }
        catch( InterruptedException ex ) {

        }
      }
      ITimedList<?> data = aProcessData.getArgData( paramKey );
      result.add( data );
    }
    return result;

  }

  public static IList<ITimedList<?>> createTestResult( IStringMap<IDtoQueryParam> aQueryParams ) {
    IListEdit<ITimedList<?>> result = new ElemArrayList<>();

    long curTime = System.currentTimeMillis();

    for( IDtoQueryParam p : aQueryParams.values() ) {
      ITimedListEdit<TemporalAtomicValue> timedList = new TimedList<>();
      result.add( timedList );
      for( int i = 2000; i > 0; i-- ) {

        TemporalAtomicValue val1 = new TemporalAtomicValue( curTime - i * 1000L, AvUtils.avInt( i ) );
        timedList.add( val1 );
      }

    }
    return result;
  }
}
