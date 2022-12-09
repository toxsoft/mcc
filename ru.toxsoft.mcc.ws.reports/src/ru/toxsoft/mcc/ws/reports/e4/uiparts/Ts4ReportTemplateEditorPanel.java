package ru.toxsoft.mcc.ws.reports.e4.uiparts;

import static org.toxsoft.core.tsgui.bricks.actions.ITsStdActionDefs.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.uskat.core.ISkHardConstants.*;
import static ru.toxsoft.mcc.ws.reports.e4.uiparts.IReportTemplateConstants.*;

import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.actions.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.ctx.impl.*;
import org.toxsoft.core.tsgui.bricks.tsnodes.*;
import org.toxsoft.core.tsgui.bricks.tstree.tmm.*;
import org.toxsoft.core.tsgui.graphics.icons.*;
import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.gui.mpc.*;
import org.toxsoft.core.tsgui.m5.gui.mpc.impl.*;
import org.toxsoft.core.tsgui.m5.gui.panels.*;
import org.toxsoft.core.tsgui.m5.gui.panels.impl.*;
import org.toxsoft.core.tsgui.m5.model.*;
import org.toxsoft.core.tsgui.panels.*;
import org.toxsoft.core.tsgui.panels.toolbar.*;
import org.toxsoft.core.tsgui.utils.layout.*;
import org.toxsoft.core.tsgui.widgets.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.bricks.time.*;
import org.toxsoft.core.tslib.bricks.time.impl.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.tslib.utils.logs.impl.*;
import org.toxsoft.sandbox.m5.table.lib.jasper.*;
import org.toxsoft.uskat.base.gui.conn.*;
import org.toxsoft.uskat.core.api.hqserv.*;
import org.toxsoft.uskat.core.api.users.*;
import org.toxsoft.uskat.core.connection.*;
import org.toxsoft.uskat.s5.legacy.*;

import ru.toxsoft.mcc.ws.core.templates.api.*;
import ru.toxsoft.mcc.ws.core.templates.gui.m5.*;
import ru.toxsoft.mcc.ws.core.templates.utils.*;

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
        case ACTID_FORM_REPORT: {
          // запросим у пользователя интервал времени
          TimeInterval retVal =
              IntervalSelectionDialogPanel.getParams( tsContext().get( Shell.class ), initValues, tsContext() );
          if( retVal != null ) {

            IStringMap<IDtoQueryParam> queryParams = ReportTemplateUtilities.formQueryParams( selTemplate );
            ISkConnectionSupplier connSupp = tsContext().get( ISkConnectionSupplier.class );

            ISkQueryProcessedData processData =
                connSupp.defConn().coreApi().hqService().createProcessedQuery( IOptionSet.NULL );

            processData.prepare( queryParams );
            processData.exec( new QueryInterval( EQueryIntervalType.OSOE, retVal.startTime(), retVal.endTime() ) );

            IM5Model<IStringMap<IAtomicValue>> resultModel =
                ReportTemplateUtilities.createM5ModelForTemplate( selTemplate );

            // IList<ITimedList<?>> reportData = ReportTemplateUtiles.createTestResult( queryParams );
            IList<ITimedList<?>> reportData = ReportTemplateUtilities.createResult( processData, queryParams );

            // mvk
            LoggerUtils.defaultLogger().info( "==== result query: ===" );
            for( String paramId : processData.listArgs().keys() ) {
              LoggerUtils.defaultLogger().info( "  pararm = %s, count = %d", paramId,
                  Integer.valueOf( processData.getArgData( paramId ).size() ) );
            }
            LoggerUtils.defaultLogger().info( "======================" );

            IM5ItemsProvider<IStringMap<IAtomicValue>> resultProvider =
                ReportTemplateUtilities.createM5ItemProviderForTemplate( selTemplate, reportData );

            if( reportV == null ) {
              reportV = new JasperReportViewer( rightBoard, tsContext() );
            }

            reportV.setJasperReportPrint( tsContext(), resultModel, resultProvider );
          }
          break;
        }
        default:
          throw new TsNotAllEnumsUsedRtException( aActionId );
      }
    }

  }

  final static String ACTID_FORM_REPORT = SK_ID + ".users.gui.RunReportForm";

  final static TsActionDef ACDEF_FORM_REPORT = TsActionDef.ofPush2( ACTID_FORM_REPORT, "Сформировать отчёт",
      "Загрузка данных из БД и формирование отчёта", ICON_RUN );

  static TimeInterval initValues =
      new TimeInterval( System.currentTimeMillis() - 24L * 60L * 60L * 1000L, System.currentTimeMillis() );

  final ISkConnection                   conn;
  IM5CollectionPanel<ISkReportTemplate> reportTemplatesPanel;

  private TsComposite                  rightBoard;
  /**
   * лист шаблона
   */
  final ITsNodeKind<ISkReportTemplate> NK_TEMPLATE_LEAF =
      new TsNodeKind<>( "LeafTemplate", ISkReportTemplate.class, false, ICON_TEMPLATE ); //$NON-NLS-1$

  /**
   * узел пользователя
   */
  final ITsNodeKind<ISkUser> NK_USER_NODE = new TsNodeKind<>( "NodeUser", ISkUser.class, true, ICON_USER ); //$NON-NLS-1$

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

    IM5Model<ISkReportTemplate> model = m5().getModel( ISkReportTemplate.CLASS_ID, ISkReportTemplate.class );

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

    ReportTemplatePaneComponentModown componentModown =
        new ReportTemplatePaneComponentModown( ctx, model, lm.itemsProvider(), lm );

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
