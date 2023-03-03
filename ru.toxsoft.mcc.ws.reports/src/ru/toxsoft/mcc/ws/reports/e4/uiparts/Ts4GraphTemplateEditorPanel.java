package ru.toxsoft.mcc.ws.reports.e4.uiparts;

import static org.toxsoft.core.tsgui.bricks.actions.ITsStdActionDefs.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.uskat.base.gui.utils.SkQueryProgressDialogUtils.*;
import static org.toxsoft.uskat.core.ISkHardConstants.*;
import static ru.toxsoft.mcc.ws.reports.IMccWsReportsConstants.*;
import static ru.toxsoft.mcc.ws.reports.e4.uiparts.ISkResources.*;

import java.text.SimpleDateFormat;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.toxsoft.core.tsgui.bricks.actions.ITsActionDef;
import org.toxsoft.core.tsgui.bricks.actions.TsActionDef;
import org.toxsoft.core.tsgui.bricks.ctx.ITsGuiContext;
import org.toxsoft.core.tsgui.bricks.ctx.impl.TsGuiContext;
import org.toxsoft.core.tsgui.bricks.tsnodes.*;
import org.toxsoft.core.tsgui.bricks.tstree.tmm.ITsTreeMaker;
import org.toxsoft.core.tsgui.bricks.tstree.tmm.TreeModeInfo;
import org.toxsoft.core.tsgui.chart.api.IG2DataSet;
import org.toxsoft.core.tsgui.dialogs.TsDialogUtils;
import org.toxsoft.core.tsgui.dialogs.datarec.ITsDialogInfo;
import org.toxsoft.core.tsgui.dialogs.datarec.TsDialogInfo;
import org.toxsoft.core.tsgui.graphics.icons.EIconSize;
import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.gui.M5GuiUtils;
import org.toxsoft.core.tsgui.m5.gui.mpc.IMultiPaneComponentConstants;
import org.toxsoft.core.tsgui.m5.gui.mpc.impl.MultiPaneComponentModown;
import org.toxsoft.core.tsgui.m5.gui.panels.IM5CollectionPanel;
import org.toxsoft.core.tsgui.m5.gui.panels.impl.M5CollectionPanelMpcModownWrapper;
import org.toxsoft.core.tsgui.m5.model.IM5LifecycleManager;
import org.toxsoft.core.tsgui.m5.model.impl.M5BunchEdit;
import org.toxsoft.core.tsgui.panels.TsPanel;
import org.toxsoft.core.tsgui.panels.toolbar.ITsToolbar;
import org.toxsoft.core.tsgui.utils.layout.BorderLayout;
import org.toxsoft.core.tsgui.utils.layout.EBorderLayoutPlacement;
import org.toxsoft.core.tslib.av.impl.AvUtils;
import org.toxsoft.core.tslib.av.opset.IOptionSetEdit;
import org.toxsoft.core.tslib.av.opset.impl.OptionSet;
import org.toxsoft.core.tslib.av.opset.impl.OptionSetUtils;
import org.toxsoft.core.tslib.bricks.time.*;
import org.toxsoft.core.tslib.bricks.time.impl.QueryInterval;
import org.toxsoft.core.tslib.bricks.time.impl.TimeInterval;
import org.toxsoft.core.tslib.coll.IList;
import org.toxsoft.core.tslib.coll.IListEdit;
import org.toxsoft.core.tslib.coll.impl.ElemArrayList;
import org.toxsoft.core.tslib.coll.primtypes.IStringMap;
import org.toxsoft.core.tslib.coll.primtypes.IStringMapEdit;
import org.toxsoft.core.tslib.coll.primtypes.impl.StringMap;
import org.toxsoft.core.tslib.gw.gwid.Gwid;
import org.toxsoft.core.tslib.utils.errors.TsNotAllEnumsUsedRtException;
import org.toxsoft.core.tslib.utils.errors.TsNullArgumentRtException;
import org.toxsoft.uskat.base.gui.conn.ISkConnectionSupplier;
import org.toxsoft.uskat.core.api.hqserv.*;
import org.toxsoft.uskat.core.api.users.ISkUser;
import org.toxsoft.uskat.core.connection.ISkConnection;

import ru.toxsoft.mcc.ws.core.chart_utils.ChartPanel;
import ru.toxsoft.mcc.ws.core.chart_utils.dataset.G2SelfUploadHistoryDataSetNew;
import ru.toxsoft.mcc.ws.core.chart_utils.dataset.IDataSetParam;
import ru.toxsoft.mcc.ws.core.templates.api.*;
import ru.toxsoft.mcc.ws.core.templates.gui.m5.SkGraphTemplateM5LifecycleManager;
import ru.toxsoft.mcc.ws.core.templates.utils.IntervalSelectionDialogPanel;
import ru.toxsoft.mcc.ws.core.templates.utils.ReportTemplateUtilities;

/**
 * Панель редактора шаблонов графиков ts4.<br>
 *
 * @author dima
 */
public class Ts4GraphTemplateEditorPanel
    extends TsPanel {

  final static String ACTID_FORM_GRAPH = SK_ID + ".users.gui.RunGraphForm"; //$NON-NLS-1$

  final static TsActionDef ACDEF_FORM_GRAPH =
      TsActionDef.ofPush2( ACTID_FORM_GRAPH, STR_N_GENERATE_CHART, STR_D_GENERATE_CHART, ICONID_RUN );

  final ISkConnection                  conn;
  IM5CollectionPanel<ISkGraphTemplate> graphTemplatesPanel;

  private CTabFolder tabFolder;

  // по умолчанию берем данные за последние 6 час
  static TimeInterval initValues =
      new TimeInterval( System.currentTimeMillis() - 6L * 60L * 60L * 1000L, System.currentTimeMillis() );

  SimpleDateFormat                    sdf              = new SimpleDateFormat( "dd.MM.YY HH:mm:ss" ); //$NON-NLS-1$
  /**
   * лист отчета
   */
  final ITsNodeKind<ISkGraphTemplate> NK_TEMPLATE_LEAF =
      new TsNodeKind<>( "LeafTemplate", ISkGraphTemplate.class, false, ICONID_GRAPH_TEMPLATE );       //$NON-NLS-1$

  /**
   * узел пользователя
   */
  final ITsNodeKind<ISkUser> NK_USER_NODE = new TsNodeKind<>( "NodeUser", ISkUser.class, true, ICONID_USER ); //$NON-NLS-1$

  protected ChartPanel        chartPanel;
  protected static ChartPanel popupChart;

  static final String TMID_GROUP_BY_USER = "GroupByUser"; //$NON-NLS-1$

  /**
   * Создатель дерева пользователи-отчеты
   *
   * @author dima
   */
  private class User2GraphTemplatesTreeMaker
      implements ITsTreeMaker<ISkGraphTemplate> {

    private final ISkGraphTemplateService service;

    User2GraphTemplatesTreeMaker( ISkGraphTemplateService aService ) {
      service = aService;
    }

    @Override
    public boolean isItemNode( ITsNode aNode ) {
      return aNode.kind() == NK_TEMPLATE_LEAF;
    }

    private IStringMapEdit<DefaultTsNode<ISkUser>> makeUser2TemplatesTypesMap( ITsNode aRootNode ) {
      IStringMapEdit<DefaultTsNode<ISkUser>> retVal = new StringMap<>();
      // получаем все шаблоны системы и строим узлы дерева
      IList<ISkGraphTemplate> templates = service.listGraphTemplates();
      for( ISkGraphTemplate template : templates ) {
        if( !retVal.hasKey( template.author().id() ) ) {
          DefaultTsNode<ISkUser> authorNode = new DefaultTsNode<>( NK_USER_NODE, aRootNode, template.author() );
          // присвоим нормальное имя
          authorNode.setName( template.author().attrs().getStr( IM5Constants.FID_NAME ) );
          retVal.put( template.author().id(), authorNode );
        }
      }
      return retVal;
    }

    @SuppressWarnings( { "unchecked", "rawtypes" } )
    @Override
    public IList<ITsNode> makeRoots( ITsNode aRootNode, IList<ISkGraphTemplate> aTemplates ) {
      IStringMapEdit<DefaultTsNode<ISkUser>> roots = makeUser2TemplatesTypesMap( aRootNode );
      for( ISkGraphTemplate template : aTemplates ) {
        DefaultTsNode<ISkUser> userNode = roots.findByKey( template.author().id() );
        DefaultTsNode<ISkGraphTemplate> templateLeaf = new DefaultTsNode<>( NK_TEMPLATE_LEAF, userNode, template );
        // присвоим нормальное имя
        templateLeaf.setName( template.attrs().getStr( IM5Constants.FID_NAME ) );
        userNode.addNode( templateLeaf );
      }
      return (IList)roots.values();
    }

  }

  /**
   * Конструктор панели.
   * <p>
   * Конструктор просто запоминает ссылку на контекст, без создания копии.
   *
   * @param aParent {@link Composite} - родительская панель
   * @param aContext {@link ITsGuiContext} - контекст панели
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  public Ts4GraphTemplateEditorPanel( Composite aParent, ITsGuiContext aContext ) {
    super( aParent, aContext );
    this.setLayout( new BorderLayout() );
    ISkConnectionSupplier connSup = aContext.get( ISkConnectionSupplier.class );
    conn = connSup.defConn();

    IM5Domain m5 = conn.scope().get( IM5Domain.class );
    IM5Model<ISkGraphTemplate> model = m5.getModel( ISkGraphTemplate.CLASS_ID, ISkGraphTemplate.class );

    IM5LifecycleManager<ISkGraphTemplate> lm = new SkGraphTemplateM5LifecycleManager( model, conn );
    ITsGuiContext ctx = new TsGuiContext( aContext );
    ctx.params().addAll( aContext.params() );
    IMultiPaneComponentConstants.OPDEF_IS_DETAILS_PANE.setValue( ctx.params(), AvUtils.AV_TRUE );
    IMultiPaneComponentConstants.OPDEF_DETAILS_PANE_PLACE.setValue( ctx.params(),
        avValobj( EBorderLayoutPlacement.SOUTH ) );
    IMultiPaneComponentConstants.OPDEF_IS_SUPPORTS_TREE.setValue( ctx.params(), AvUtils.AV_TRUE );
    IMultiPaneComponentConstants.OPDEF_IS_ACTIONS_CRUD.setValue( ctx.params(), AvUtils.AV_TRUE );
    // добавляем в панель фильтр
    IMultiPaneComponentConstants.OPDEF_IS_FILTER_PANE.setValue( ctx.params(), AvUtils.AV_TRUE );

    SashForm sf = new SashForm( aParent, SWT.HORIZONTAL );
    MultiPaneComponentModown<ISkGraphTemplate> componentModown =
        new MultiPaneComponentModown<>( ctx, model, lm.itemsProvider(), lm ) {

          @Override
          protected ITsToolbar doCreateToolbar( @SuppressWarnings( "hiding" ) ITsGuiContext aContext, String aName,
              EIconSize aIconSize, IListEdit<ITsActionDef> aActs ) {
            aActs.add( ACDEF_SEPARATOR );
            aActs.add( Ts4ReportTemplateEditorPanel.ACDEF_COPY_TEMPLATE );
            aActs.add( ACDEF_SEPARATOR );
            aActs.add( ACDEF_FORM_GRAPH );

            ITsToolbar toolbar =

                super.doCreateToolbar( aContext, aName, aIconSize, aActs );

            toolbar.addListener( aActionId -> {
              // TODO Auto-generated method stub

            } );

            return toolbar;
          }

          @Override
          protected void doProcessAction( String aActionId ) {
            ISkGraphTemplate selTemplate = selectedItem();

            switch( aActionId ) {
              case Ts4ReportTemplateEditorPanel.ACTID_COPY_TEMPLATE:
                copyTemplate( selTemplate );
                break;

              case ACTID_FORM_GRAPH:
                formGraph( aContext, selTemplate );
                break;
              default:
                throw new TsNotAllEnumsUsedRtException( aActionId );
            }
          }

          private void copyTemplate( ISkGraphTemplate aSelTemplate ) {
            IM5Bunch<ISkGraphTemplate> originalBunch = model.valuesOf( aSelTemplate );
            IM5BunchEdit<ISkGraphTemplate> copyBunch = new M5BunchEdit<>( model );
            for( IM5FieldDef<ISkGraphTemplate, ?> fd : originalBunch.model().fieldDefs() ) {
              copyBunch.set( fd.id(), originalBunch.get( fd ) );
            }
            ITsDialogInfo cdi = TsDialogInfo.forCreateEntity( tsContext() );
            ISkGraphTemplate copyTemplate =
                M5GuiUtils.askCreate( tsContext(), model, copyBunch, cdi, model.getLifecycleManager( null ) );
            if( copyTemplate != null ) {
              // создали копию, обновим список
              refresh();
            }
          }

          private void formGraph( ITsGuiContext aContext, ISkGraphTemplate aSelTemplate ) {
            Shell shell = aContext.get( Shell.class );
            // запросим у пользователя интервал времени
            TimeInterval retVal = IntervalSelectionDialogPanel.getParams( shell, initValues, aContext );
            if( retVal == null ) {
              return;
            }
            // запомним выбранный интервал
            initValues = new TimeInterval( retVal.startTime(), retVal.endTime() );
            // формируем запрос к одноименному сервису
            IStringMap<IDtoQueryParam> queryParams = ReportTemplateUtilities.formQueryParams( aSelTemplate );
            ISkConnectionSupplier connSupp = tsContext().get( ISkConnectionSupplier.class );

            // Максимальное время выполнения запроса (мсек)
            long timeout = aSelTemplate.maxExecutionTime();
            // Параметры запроса
            IOptionSetEdit options = new OptionSet( OptionSetUtils.createOpSet( //
                ISkHistoryQueryServiceConstants.OP_SK_MAX_EXECUTION_TIME, AvUtils.avInt( timeout ) //
            ) );
            // Формирование запроса
            ISkQueryProcessedData query = connSupp.defConn().coreApi().hqService().createProcessedQuery( options );
            try {
              // Подготовка запроса
              query.prepare( queryParams );
              // Настройка обработки результатов запроса
              query.genericChangeEventer().addListener( aSource -> {
                ISkQueryProcessedData q = (ISkQueryProcessedData)aSource;
                if( q.state() == ESkQueryState.READY ) {
                  IList<ITimedList<?>> requestAnswer = ReportTemplateUtilities.createResult( query, queryParams );
                  IList<IG2DataSet> graphData =
                      createG2SelfUploDataSetList( aSelTemplate, requestAnswer, connSupp.defConn() );
                  for( IG2DataSet ds : graphData ) {
                    if( ds instanceof G2SelfUploadHistoryDataSetNew ) {
                      ((G2SelfUploadHistoryDataSetNew)ds).addListener( aSource1 -> chartPanel.refresh() );
                    }
                  }
                  // создаем новую закладку
                  CTabItem tabItem = new CTabItem( tabFolder, SWT.CLOSE );
                  tabItem.setText( aSelTemplate.nmName() );
                  chartPanel = new ChartPanel( tabFolder, tsContext() );

                  tabItem.setControl( chartPanel );
                  tabFolder.setSelection( tabItem );
                  chartPanel.setReportAnswer( graphData, aSelTemplate, true );
                  chartPanel.requestLayout();
                }
                if( q.state() == ESkQueryState.FAILED ) {
                  String stateMessage = q.stateMessage();
                  TsDialogUtils.error( getShell(), ERR_QUERY_FAILED, stateMessage );
                }
              } );
              // Интервал запроса
              IQueryInterval interval =
                  new QueryInterval( EQueryIntervalType.OSOE, retVal.startTime(), retVal.endTime() );
              // Выполение запроса в прогресс-диалоге
              execQueryByProgressDialog( shell, STR_EXEC_QUERY_FOR_GRAPH, query, interval, timeout );
            }
            finally {
              query.close();
            }
          }
        };

    User2GraphTemplatesTreeMaker treeMaker =
        new User2GraphTemplatesTreeMaker( conn.coreApi().getService( ISkGraphTemplateService.SERVICE_ID ) );

    componentModown.tree().setTreeMaker( treeMaker );

    componentModown.treeModeManager().addTreeMode( new TreeModeInfo<>( TMID_GROUP_BY_USER, ISkResources.STR_N_BY_USERS,
        ISkResources.STR_D_BY_USERS, null, treeMaker ) );
    componentModown.treeModeManager().setCurrentMode( TMID_GROUP_BY_USER );

    componentModown.addTsSelectionListener( ( aSource, aSelectedItem ) -> {
      componentModown.toolbar().setActionEnabled( ACTID_FORM_GRAPH, aSelectedItem != null );
    } );

    graphTemplatesPanel = new M5CollectionPanelMpcModownWrapper<>( componentModown, false );

    graphTemplatesPanel.createControl( sf );

    tabFolder = new CTabFolder( sf, SWT.BORDER );
    tabFolder.setLayout( new BorderLayout() );

    sf.setWeights( 300, 500 );

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
