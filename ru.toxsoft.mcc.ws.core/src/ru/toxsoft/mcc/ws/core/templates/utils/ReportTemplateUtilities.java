package ru.toxsoft.mcc.ws.core.templates.utils;

import static org.toxsoft.core.tsgui.m5.IM5Constants.*;
import static org.toxsoft.core.tslib.av.EAtomicType.*;
import static org.toxsoft.uskat.core.api.hqserv.ISkHistoryQueryServiceConstants.*;

import java.text.*;
import java.util.*;

import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.chart.api.*;
import org.toxsoft.core.tsgui.chart.impl.*;
import org.toxsoft.core.tsgui.graphics.colors.*;
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
import org.toxsoft.core.tslib.gw.skid.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.uskat.base.gui.conn.*;
import org.toxsoft.uskat.core.*;
import org.toxsoft.uskat.core.api.hqserv.*;
import org.toxsoft.uskat.core.api.objserv.*;
import org.toxsoft.uskat.core.api.users.*;
import org.toxsoft.uskat.core.connection.*;
import org.toxsoft.uskat.core.impl.dto.*;

import ru.toxsoft.mcc.ws.core.chart_utils.*;
import ru.toxsoft.mcc.ws.core.chart_utils.dataset.*;
import ru.toxsoft.mcc.ws.core.templates.api.*;

/**
 * Утилитный класс для работы с шаблонами
 *
 * @author Max
 * @author dima
 */
public class ReportTemplateUtilities {

  // по умолчанию берем данные за последние 6 час
  static TimeInterval initValues =
      new TimeInterval( System.currentTimeMillis() - 6L * 60L * 60L * 1000L, System.currentTimeMillis() );

  private static boolean IS_SAME_TIME_IN_EACH_COLUMN = true;

  private static final String STR_TIME_COLUMN_VALUE_DEFAULT = "-";

  private static final String FMT_TIME_COLUMN_VALUE = "MM.dd HH:mm:ss";

  private static final String FMT_N_TIME_COLUMN = "Время: %s";

  private static final String STR_N_TIME_COLUMN = "Время";

  protected static ChartPanel popupChart;

  /**
   * Формат идентификатора параметра запроса данных.
   */
  static final String QUERY_PARAM_ID_FORMAT = "param%d"; //$NON-NLS-1$

  /**
   * Формат идентификатора столбца модели - в качестве параметра - номер столбца, начиная с 0 [int].
   */
  public static final String MODEL_COLUMN_ID_FORMAT = "column%d"; //$NON-NLS-1$

  /**
   * Формат идентификатора столбца времени модели - в качестве параметра - номер столбца, начиная с 0 [int].
   */
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
   * @param aReportTemplate ISkReportTemplate - report template params.
   * @return IM5Model - M5 Model according to report template params.
   */
  public static IM5Model<IStringMap<IAtomicValue>> createM5ModelForTemplate( ISkReportTemplate aReportTemplate ) {
    return new ReportM5Model( aReportTemplate, IS_SAME_TIME_IN_EACH_COLUMN );
  }

  /**
   * Creates M5 data provider according to report template params and report data.
   *
   * @param aReportTemplate ISkReportTemplate - report template params.
   * @param aReportData IList - report data.
   * @return IM5ItemsProvider - M5 data provider.
   */
  public static IM5ItemsProvider<IStringMap<IAtomicValue>> createM5ItemProviderForTemplate(
      ISkReportTemplate aReportTemplate, IList<ITimedList<?>> aReportData ) {
    return new ReportM5ItemProvider( aReportTemplate, aReportData, IS_SAME_TIME_IN_EACH_COLUMN );
  }

  /**
   * Перевод значение enum в id понятный сервису запросов
   *
   * @param aAggrFunc enum
   * @return String
   */
  public static String convertFunc( EAggregationFunc aAggrFunc ) {
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
  public static IList<ITemporalAtomicValue> convertList2List( ITimedList<?> aTimedList ) {
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
     * @param aIsSameTimeInEachColumn boolean - признак того, что время всех элементов в одной строке отчёта - одно
     *          (один столбец времени).
     */
    public ReportM5Model( ISkReportTemplate aReportTemplate, boolean aIsSameTimeInEachColumn ) {
      super( generateId( aReportTemplate ), (Class)IStringMap.class );

      reportTemplate = aReportTemplate;

      IListEdit<IM5FieldDef<IStringMap<IAtomicValue>, ?>> fDefs = new ElemArrayList<>();

      IList<ISkReportParam> reportParams = aReportTemplate.listParams();

      if( aIsSameTimeInEachColumn ) {
        String timeId = String.format( MODEL_TIME_COLUMN_ID_FORMAT, Integer.valueOf( 0 ) );

        M5AttributeFieldDef<IStringMap<IAtomicValue>> timeField =
            createFieldDef( timeId, STR_N_TIME_COLUMN, STR_N_TIME_COLUMN, EDisplayFormat.AS_INTEGER );
        fDefs.add( timeField );
      }

      for( int i = 0; i < reportParams.size(); i++ ) {
        ISkReportParam param = reportParams.get( i );

        String fieldName = param.title();
        String fieldDescr = param.description();
        EDisplayFormat displayFormat = param.displayFormat();

        if( !aIsSameTimeInEachColumn ) {
          String timeId = String.format( MODEL_TIME_COLUMN_ID_FORMAT, Integer.valueOf( i ) );
          M5AttributeFieldDef<IStringMap<IAtomicValue>> jointModelTimeField =
              createFieldDef( timeId, String.format( FMT_N_TIME_COLUMN, fieldName ),
                  String.format( FMT_N_TIME_COLUMN, fieldDescr ), displayFormat );
          fDefs.add( jointModelTimeField );
        }

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

    private ISkReportTemplate    reportTemplate;
    private IList<ITimedList<?>> reportData;
    private boolean              isSameTimeInEachColumn;

    /**
     * Конструктор поставщика данных по шаблону и результату запроса.
     *
     * @param aReportTemplate ISkReportTemplate - шаблон запроса.
     * @param aReportData IList - результат запроса.
     * @param aIsSameTimeInEachColumn boolean - признак того, что время всех элементов в одной строке отчёта - одно
     *          (один столбец времени).
     */
    public ReportM5ItemProvider( ISkReportTemplate aReportTemplate, IList<ITimedList<?>> aReportData,
        boolean aIsSameTimeInEachColumn ) {
      super();
      reportTemplate = aReportTemplate;
      reportData = aReportData;
      isSameTimeInEachColumn = aIsSameTimeInEachColumn;

      formItems();
    }

    private void formItems() {
      items().clear();

      IList<ISkReportParam> reportParams = reportTemplate.listParams();

      if( reportParams.size() == 0 ) {
        return;
      }

      // количество строк - максимальное количество элементов в истории одного из параметров
      int rowCount = 0;
      int longestColumnIndex = 0;
      for( int i = 0; i < reportParams.size(); i++ ) {
        ITimedList<?> timedList = reportData.get( i );
        if( timedList.size() > rowCount ) {
          rowCount = timedList.size();
          longestColumnIndex = i;
        }
      }

      // набор со временем самого длинного столбца
      IStringListEdit timeColumnValue = new StringArrayList();
      if( isSameTimeInEachColumn ) {
        ITimedList<?> longestTimedList = reportData.get( longestColumnIndex );
        for( int j = 0; j < rowCount; j++ ) {
          Object val = longestTimedList.get( j );
          String strTime = convertTime( val );
          timeColumnValue.add( strTime );
        }
      }

      for( int j = 0; j < rowCount; j++ ) {
        IStringMapEdit<IAtomicValue> rowValues = new StringMap<>();
        if( isSameTimeInEachColumn ) {
          String timeId = String.format( MODEL_TIME_COLUMN_ID_FORMAT, Integer.valueOf( 0 ) );

          rowValues.put( timeId, AvUtils.avStr( timeColumnValue.get( j ) ) );
        }

        for( int i = 0; i < reportParams.size(); i++ ) {

          ISkReportParam param = reportParams.get( i );

          EDisplayFormat displayFormat = param.displayFormat();

          ITimedList<?> timedList = reportData.get( i );

          Object val = j < timedList.size() ? timedList.get( j ) : TsLibUtils.EMPTY_STRING;

          if( !isSameTimeInEachColumn ) {
            String strTime = convertTime( val );
            String timeId = String.format( MODEL_TIME_COLUMN_ID_FORMAT, Integer.valueOf( i ) );
            rowValues.put( timeId, AvUtils.avStr( strTime ) );
          }

          String strVal = convertValue( val, displayFormat );
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

      DateFormat formatter = new SimpleDateFormat( FMT_TIME_COLUMN_VALUE );

      return formatter.format( new Date( ((TemporalAtomicValue)aVal).timestamp() ) );
    }
    return STR_TIME_COLUMN_VALUE_DEFAULT;
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

  /**
   * Создает Chart панель для графика одного параметра
   *
   * @param aContext контекст
   * @param aParent родительский компонент
   * @param aParamGwid параметр отображаемый на графике
   * @param aTitle название параметра
   * @param aDescription описание параметра
   * @return панель для графика
   */
  public static ChartPanel popupChart( ITsGuiContext aContext, Composite aParent, Gwid aParamGwid, String aTitle,
      String aDescription ) {
    ISkGraphTemplate selTemplate = createTemplate( aParamGwid, aTitle, aDescription );
    // формируем запрос к одноименному сервису
    IStringMap<IDtoQueryParam> queryParams = ReportTemplateUtilities.formQueryParams( selTemplate );
    ISkConnectionSupplier connSupp = aContext.get( ISkConnectionSupplier.class );

    ISkQueryProcessedData processData =
        connSupp.defConn().coreApi().hqService().createProcessedQuery( IOptionSet.NULL );

    processData.prepare( queryParams );

    processData.exec( new QueryInterval( EQueryIntervalType.OSOE, initValues.startTime(), initValues.endTime() ) );

    // асинхронное получение данных
    processData.genericChangeEventer().addListener( aSource -> {
      ISkQueryProcessedData q = (ISkQueryProcessedData)aSource;
      if( q.state() == ESkQueryState.READY ) {
        IList<ITimedList<?>> requestAnswer = createResult( processData, queryParams );
        IList<IG2DataSet> graphData = createG2SelfUploDataSetList( selTemplate, requestAnswer, connSupp.defConn() );
        for( IG2DataSet ds : graphData ) {
          if( ds instanceof G2SelfUploadHistoryDataSetNew ) {
            ((G2SelfUploadHistoryDataSetNew)ds).addListener( aSource1 -> popupChart.refresh() );
          }
        }
        popupChart.setReportAnswer( graphData, selTemplate );
        popupChart.requestLayout();
      }
    } );

    // создаем новую панель
    popupChart = new ChartPanel( aParent, aContext );
    return popupChart;
  }

  private static ISkGraphTemplate createTemplate( Gwid aParamGwid, String aTitle, String aDescription ) {
    ISkGraphTemplate retVal = new ISkGraphTemplate() {

      /**
       * @return { @link ETimeUnit} - time step of aggregation
       */
      @Override
      public ETimeUnit aggrStep() {
        return ETimeUnit.MIN01;
      }

      @Override
      public String nmName() {
        return aTitle;
      }

      @Override
      public String description() {
        return aDescription;
      }

      @Override
      public String strid() {
        return Skid.NONE.strid();
      }

      @Override
      public Skid skid() {
        return Skid.NONE;
      }

      @Override
      public IMappedSkids rivets() {
        return null;
      }

      @Override
      public String readableName() {
        return aTitle;
      }

      @Override
      public String id() {
        return Skid.NONE.strid();
      }

      @Override
      public Skid getSingleLinkSkid( String aLinkId ) {
        return Skid.NONE;
      }

      @Override
      public <T extends ISkObject> T getSingleLinkObj( String aLinkId ) {
        return null;
      }

      @Override
      public ISkidList getRivetRevSkids( String aClassId, String aRivetId ) {
        return ISkidList.EMPTY;
      }

      @Override
      public <T extends ISkObject> IList<T> getRivetRevObjs( String aClassId, String aRivetId ) {
        return IList.EMPTY;
      }

      @Override
      public ISkidList getLinkSkids( String aLinkId ) {
        return null;
      }

      @Override
      public ISkidList getLinkRevSkids( String aClassId, String aLinkId ) {
        return ISkidList.EMPTY;
      }

      @Override
      public <T extends ISkObject> IList<T> getLinkRevObjs( String aClassId, String aLinkId ) {
        return IList.EMPTY;
      }

      @Override
      public <T extends ISkObject> IList<T> getLinkObjs( String aLinkId ) {
        return IList.EMPTY;
      }

      @Override
      public String getClob( String aClobId, String aDefaultValue ) {
        return null;
      }

      @Override
      public ISkCoreApi coreApi() {
        return null;
      }

      @Override
      public String classId() {
        return Skid.NONE.classId();
      }

      @Override
      public IOptionSet attrs() {
        return IOptionSet.NULL;
      }

      @Override
      public IList<ISkGraphParam> listParams() {
        return new ElemArrayList<>( new ISkGraphParam() {

          @Override
          public Gwid gwid() {
            return aParamGwid;
          }

          @Override
          public String title() {
            return aTitle;
          }

          @Override
          public String description() {
            return aDescription;
          }

          @Override
          public EAggregationFunc aggrFunc() {
            return EAggregationFunc.AVERAGE;
          }

          @Override
          public EDisplayFormat displayFormat() {
            return EDisplayFormat.TWO_DIGIT;
          }

          @Override
          public ETsColor color() {
            return ETsColor.BLACK;
          }

          @Override
          public int lineWidth() {
            return 2;
          }

          @Override
          public String unitId() {
            return "Y";
          }

          @Override
          public String unitName() {
            return "";
          }

          @Override
          public boolean isLadder() {
            return false;
          }

          @Override
          public IStringList setPoints() {
            return IStringList.EMPTY;
          }

        } );
      }

      @Override
      public ISkUser author() {
        return null;
      }
    };
    return retVal;
  }

  /**
   * По шаблону графика и результату запроса к сервису отчетов создает список наборов данных для графической компоненты
   *
   * @param aGraphTemplate {@link ISkGraphTemplate} - шаблон графика
   * @param aReportData - результат запроса к сервису отчетов
   * @param aConnection - соединение с сервером
   * @return - список наборов данных для графика
   */
  public static IList<IG2DataSet> createG2SelfUploDataSetList( ISkGraphTemplate aGraphTemplate,
      IList<ITimedList<?>> aReportData, ISkConnection aConnection ) {
    IListEdit<IG2DataSet> retVal = new ElemArrayList<>();
    IList<ISkGraphParam> graphParams = aGraphTemplate.listParams();
    // создаем нужные наборы данных
    for( int i = 0; i < graphParams.size(); i++ ) {
      ISkGraphParam graphParam = graphParams.get( i );
      String gdsId = ReportTemplateUtilities.graphDataSetId( graphParam );

      G2SelfUploadHistoryDataSetNew dataSet =
          new G2SelfUploadHistoryDataSetNew( aConnection, gdsId, new IDataSetParam() {

            @Override
            public Gwid gwid() {
              return graphParam.gwid();
            }

            @Override
            public String aggrFuncId() {
              return ReportTemplateUtilities.convertFunc( graphParam.aggrFunc() );
            }

            @Override
            public int aggrStep() {
              return (int)aGraphTemplate.aggrStep().timeInMills();
            }

          } );
      retVal.add( dataSet );
      // наполняем его данными
      ITimedList<?> timedList = aReportData.get( i );
      dataSet.setValues( ReportTemplateUtilities.convertList2List( timedList ) );
    }
    return retVal;
  }

}
