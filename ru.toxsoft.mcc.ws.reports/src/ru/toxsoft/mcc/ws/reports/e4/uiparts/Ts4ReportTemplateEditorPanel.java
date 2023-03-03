package ru.toxsoft.mcc.ws.reports.e4.uiparts;

import static org.toxsoft.core.tsgui.bricks.actions.ITsStdActionDefs.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.uskat.base.gui.utils.SkQueryProgressDialogUtils.*;
import static org.toxsoft.uskat.core.ISkHardConstants.*;
import static ru.toxsoft.mcc.ws.reports.IMccWsReportsConstants.*;
import static ru.toxsoft.mcc.ws.reports.e4.uiparts.ISkResources.*;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.toxsoft.core.jasperreports.gui.main.JasperReportViewer;
import org.toxsoft.core.tsgui.bricks.actions.ITsActionDef;
import org.toxsoft.core.tsgui.bricks.actions.TsActionDef;
import org.toxsoft.core.tsgui.bricks.ctx.ITsGuiContext;
import org.toxsoft.core.tsgui.bricks.ctx.impl.TsGuiContext;
import org.toxsoft.core.tsgui.bricks.tsnodes.*;
import org.toxsoft.core.tsgui.bricks.tstree.tmm.ITsTreeMaker;
import org.toxsoft.core.tsgui.bricks.tstree.tmm.TreeModeInfo;
import org.toxsoft.core.tsgui.dialogs.TsDialogUtils;
import org.toxsoft.core.tsgui.dialogs.datarec.ITsDialogInfo;
import org.toxsoft.core.tsgui.dialogs.datarec.TsDialogInfo;
import org.toxsoft.core.tsgui.graphics.icons.EIconSize;
import org.toxsoft.core.tsgui.graphics.icons.ITsStdIconIds;
import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.gui.M5GuiUtils;
import org.toxsoft.core.tsgui.m5.gui.mpc.IMultiPaneComponentConstants;
import org.toxsoft.core.tsgui.m5.gui.mpc.impl.MultiPaneComponentModown;
import org.toxsoft.core.tsgui.m5.gui.panels.IM5CollectionPanel;
import org.toxsoft.core.tsgui.m5.gui.panels.impl.M5CollectionPanelMpcModownWrapper;
import org.toxsoft.core.tsgui.m5.model.IM5ItemsProvider;
import org.toxsoft.core.tsgui.m5.model.IM5LifecycleManager;
import org.toxsoft.core.tsgui.m5.model.impl.M5BunchEdit;
import org.toxsoft.core.tsgui.panels.TsPanel;
import org.toxsoft.core.tsgui.panels.toolbar.ITsToolbar;
import org.toxsoft.core.tsgui.utils.layout.BorderLayout;
import org.toxsoft.core.tsgui.utils.layout.EBorderLayoutPlacement;
import org.toxsoft.core.tsgui.widgets.TsComposite;
import org.toxsoft.core.tslib.av.IAtomicValue;
import org.toxsoft.core.tslib.av.impl.AvUtils;
import org.toxsoft.core.tslib.av.opset.IOptionSetEdit;
import org.toxsoft.core.tslib.av.opset.impl.OptionSet;
import org.toxsoft.core.tslib.av.opset.impl.OptionSetUtils;
import org.toxsoft.core.tslib.bricks.time.*;
import org.toxsoft.core.tslib.bricks.time.impl.QueryInterval;
import org.toxsoft.core.tslib.bricks.time.impl.TimeInterval;
import org.toxsoft.core.tslib.coll.IList;
import org.toxsoft.core.tslib.coll.IListEdit;
import org.toxsoft.core.tslib.coll.primtypes.IStringMap;
import org.toxsoft.core.tslib.coll.primtypes.IStringMapEdit;
import org.toxsoft.core.tslib.coll.primtypes.impl.StringMap;
import org.toxsoft.core.tslib.utils.errors.TsNotAllEnumsUsedRtException;
import org.toxsoft.core.tslib.utils.errors.TsNullArgumentRtException;
import org.toxsoft.uskat.base.gui.conn.ISkConnectionSupplier;
import org.toxsoft.uskat.core.api.hqserv.*;
import org.toxsoft.uskat.core.api.users.ISkUser;
import org.toxsoft.uskat.core.connection.ISkConnection;

import ru.toxsoft.mcc.ws.core.templates.api.ISkReportTemplate;
import ru.toxsoft.mcc.ws.core.templates.api.ISkReportTemplateService;
import ru.toxsoft.mcc.ws.core.templates.gui.m5.SkReportTemplateM5LifecycleManager;
import ru.toxsoft.mcc.ws.core.templates.utils.IntervalSelectionDialogPanel;
import ru.toxsoft.mcc.ws.core.templates.utils.ReportTemplateUtilities;

/**
 * Панель редактора шаблонов отчетов ts4.<br>
 *
 * @author dima
 */
public class Ts4ReportTemplateEditorPanel
    extends TsPanel {

  private static class ReportTemplatePaneComponentModown
      extends MultiPaneComponentModown<ISkReportTemplate> {

    private JasperReportViewer reportV;
    private TsComposite        rightBoard;

    ReportTemplatePaneComponentModown( ITsGuiContext aContext, IM5Model<ISkReportTemplate> aModel,
        IM5ItemsProvider<ISkReportTemplate> aItemsProvider, IM5LifecycleManager<ISkReportTemplate> aLifecycleManager ) {
      super( aContext, aModel, aItemsProvider, aLifecycleManager );

    }

    protected void setReportParentWidget( TsComposite aRightBoard ) {
      rightBoard = aRightBoard;
    }

    @Override
    protected ITsToolbar doCreateToolbar( ITsGuiContext aContext, String aName, EIconSize aIconSize,
        IListEdit<ITsActionDef> aActs ) {
      aActs.add( ACDEF_SEPARATOR );
      aActs.add( ACDEF_COPY_TEMPLATE );
      aActs.add( ACDEF_SEPARATOR );
      aActs.add( ACDEF_FORM_REPORT );

      ITsToolbar toolbar =

          super.doCreateToolbar( aContext, aName, aIconSize, aActs );

      toolbar.addListener( aActionId -> {
        // TODO Auto-generated method stub

      } );

      return toolbar;
    }

    @Override
    protected void doProcessAction( String aActionId ) {
      ISkReportTemplate selTemplate = selectedItem();

      switch( aActionId ) {
        case ACTID_COPY_TEMPLATE:
          copyTemplate( selTemplate );
          break;
        case ACTID_FORM_REPORT:
          formReport( selTemplate );
          break;

        default:
          throw new TsNotAllEnumsUsedRtException( aActionId );
      }
    }

    private void copyTemplate( ISkReportTemplate aSelTemplate ) {
      ISkConnectionSupplier connSup = tsContext().get( ISkConnectionSupplier.class );
      ISkConnection conn = connSup.defConn();

      IM5Domain m5 = conn.scope().get( IM5Domain.class );
      IM5Model<ISkReportTemplate> model = m5.getModel( ISkReportTemplate.CLASS_ID, ISkReportTemplate.class );
      IM5Bunch<ISkReportTemplate> originalBunch = model.valuesOf( aSelTemplate );
      IM5BunchEdit<ISkReportTemplate> copyBunch = new M5BunchEdit<>( model );
      for( IM5FieldDef<ISkReportTemplate, ?> fd : originalBunch.model().fieldDefs() ) {
        copyBunch.set( fd.id(), originalBunch.get( fd ) );
      }
      ITsDialogInfo cdi = TsDialogInfo.forCreateEntity( tsContext() );
      ISkReportTemplate copyTemplate =
          M5GuiUtils.askCreate( tsContext(), model, copyBunch, cdi, model.getLifecycleManager( null ) );
      if( copyTemplate != null ) {
        // создали копию, обновим список
        refresh();
      }
    }

    private void formReport( ISkReportTemplate aSelTemplate ) {
      Shell shell = tsContext().get( Shell.class );
      // запросим у пользователя интервал времени
      TimeInterval retVal = IntervalSelectionDialogPanel.getParams( shell, initValues, tsContext() );
      if( retVal == null ) {
        return;
      }
      // запомним выбранный интервал
      initValues = new TimeInterval( retVal.startTime(), retVal.endTime() );

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
        IM5Model<IStringMap<IAtomicValue>> resultModel =
            ReportTemplateUtilities.createM5ModelForTemplate( aSelTemplate );
        query.genericChangeEventer().addListener( aSource -> {
          ISkQueryProcessedData q = (ISkQueryProcessedData)aSource;
          if( q.state() == ESkQueryState.READY ) {
            IList<ITimedList<?>> reportData = ReportTemplateUtilities.createResult( query, queryParams );
            IM5ItemsProvider<IStringMap<IAtomicValue>> resultProvider =
                ReportTemplateUtilities.createM5ItemProviderForTemplate( aSelTemplate, reportData );
            if( reportV == null ) {
              reportV = new JasperReportViewer( rightBoard, tsContext() );
            }
            reportV.setJasperReportPrint( tsContext(), resultModel, resultProvider );
          }
          if( q.state() == ESkQueryState.FAILED ) {
            String stateMessage = q.stateMessage();
            TsDialogUtils.error( getShell(), ERR_QUERY_FAILED, stateMessage );
          }
        } );
        // Интервал запроса
        IQueryInterval interval = new QueryInterval( EQueryIntervalType.OSOE, retVal.startTime(), retVal.endTime() );
        // Выполение запроса в прогресс-диалоге
        execQueryByProgressDialog( shell, STR_EXEC_QUERY_REPORT, query, interval, timeout );
      }
      finally {
        query.close();
      }
    }

  }

  final static String ACTID_FORM_REPORT = SK_ID + ".users.gui.RunReportForm"; //$NON-NLS-1$

  final static TsActionDef ACDEF_FORM_REPORT =
      TsActionDef.ofPush2( ACTID_FORM_REPORT, STR_N_GENERATE_REPORT, STR_D_GENERATE_REPORT, ICONID_RUN );

  final static String ACTID_COPY_TEMPLATE = SK_ID + ".users.gui.CopyTemplate"; //$NON-NLS-1$

  final static TsActionDef ACDEF_COPY_TEMPLATE = TsActionDef.ofPush2( ACTID_COPY_TEMPLATE, STR_N_COPY_TEMPLATE,
      STR_D_COPY_TEMPLATE, ITsStdIconIds.ICONID_EDIT_COPY );
  // по умолчанию берем данные за последний час
  static TimeInterval initValues =
      new TimeInterval( System.currentTimeMillis() - 60L * 60L * 1000L, System.currentTimeMillis() );

  final ISkConnection                   conn;
  IM5CollectionPanel<ISkReportTemplate> reportTemplatesPanel;

  private TsComposite                  rightBoard;
  /**
   * лист шаблона
   */
  final ITsNodeKind<ISkReportTemplate> NK_TEMPLATE_LEAF =
      new TsNodeKind<>( "LeafTemplate", ISkReportTemplate.class, false, ICONID_REPORT_TEMPLATE ); //$NON-NLS-1$

  /**
   * узел пользователя
   */
  final ITsNodeKind<ISkUser> NK_USER_NODE = new TsNodeKind<>( "NodeUser", ISkUser.class, true, ICONID_USER ); //$NON-NLS-1$

  /**
   * панель отображения дерева шаблонов
   */
  private ReportTemplatePaneComponentModown componentModown;

  static final String TMIID_GROUP_BY_USER = "GroupByUser"; //$NON-NLS-1$

  /**
   * Создатель дерева пользователи-отчеты
   *
   * @author dima
   */
  private class User2ReportTemplatesTreeMaker
      implements ITsTreeMaker<ISkReportTemplate> {

    private final ISkReportTemplateService service;

    User2ReportTemplatesTreeMaker( ISkReportTemplateService aService ) {
      service = aService;
    }

    @Override
    public boolean isItemNode( ITsNode aNode ) {
      return aNode.kind() == NK_TEMPLATE_LEAF;
    }

    private IStringMapEdit<DefaultTsNode<ISkUser>> makeUser2TemplatesTypesMap( ITsNode aRootNode ) {
      IStringMapEdit<DefaultTsNode<ISkUser>> retVal = new StringMap<>();
      // получаем все шаблоны системы и строим узлы дерева
      IList<ISkReportTemplate> templates = service.listReportTemplates();
      for( ISkReportTemplate template : templates ) {
        if( !retVal.hasKey( template.author().id() ) ) {
          DefaultTsNode<ISkUser> authorNode = new DefaultTsNode<>( NK_USER_NODE, aRootNode, template.author() );
          // присвоим красивую иконку и нормальное имя
          authorNode.setName( template.author().attrs().getStr( IM5Constants.FID_NAME ) );
          retVal.put( template.author().id(), authorNode );
        }
      }
      return retVal;
    }

    @SuppressWarnings( { "unchecked", "rawtypes" } )
    @Override
    public IList<ITsNode> makeRoots( ITsNode aRootNode, IList<ISkReportTemplate> aTemplates ) {
      IStringMapEdit<DefaultTsNode<ISkUser>> roots = makeUser2TemplatesTypesMap( aRootNode );
      for( ISkReportTemplate template : aTemplates ) {
        DefaultTsNode<ISkUser> userNode = roots.findByKey( template.author().id() );
        DefaultTsNode<ISkReportTemplate> templateLeaf = new DefaultTsNode<>( NK_TEMPLATE_LEAF, userNode, template );
        // присвоим красивую иконку и нормальное имя
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
  public Ts4ReportTemplateEditorPanel( Composite aParent, ITsGuiContext aContext ) {
    super( aParent, aContext );
    this.setLayout( new BorderLayout() );
    ISkConnectionSupplier connSup = aContext.get( ISkConnectionSupplier.class );
    conn = connSup.defConn();

    IM5Domain m5 = conn.scope().get( IM5Domain.class );
    IM5Model<ISkReportTemplate> model = m5.getModel( ISkReportTemplate.CLASS_ID, ISkReportTemplate.class );

    IM5LifecycleManager<ISkReportTemplate> lm = new SkReportTemplateM5LifecycleManager( model, conn );
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
    // dima 03.11.22 вынес в отдельный класс

    componentModown = new ReportTemplatePaneComponentModown( ctx, model, lm.itemsProvider(), lm );

    // дерево пользователи -> их шаблоны
    User2ReportTemplatesTreeMaker treeMaker =
        new User2ReportTemplatesTreeMaker( conn.coreApi().getService( ISkReportTemplateService.SERVICE_ID ) );

    componentModown.tree().setTreeMaker( treeMaker );

    componentModown.treeModeManager().addTreeMode( new TreeModeInfo<>( TMIID_GROUP_BY_USER, ISkResources.STR_N_BY_USERS,
        ISkResources.STR_D_BY_USERS, null, treeMaker ) );
    componentModown.treeModeManager().setCurrentMode( TMIID_GROUP_BY_USER );

    reportTemplatesPanel = new M5CollectionPanelMpcModownWrapper<>( componentModown, false );

    reportTemplatesPanel.createControl( sf );

    // TODO
    rightBoard = new TsComposite( sf );
    rightBoard.setLayout( new BorderLayout() );
    componentModown.setReportParentWidget( rightBoard );

    sf.setWeights( 300, 500 );

  }
}
