package ru.toxsoft.mcc.ws.journals.e4.uiparts.engine;

import static org.toxsoft.core.tsgui.m5.IM5Constants.*;
import static org.toxsoft.core.tslib.av.EAtomicType.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static ru.toxsoft.mcc.ws.journals.e4.uiparts.engine.IMmResources.*;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.gui.panels.*;
import org.toxsoft.core.tsgui.m5.gui.panels.impl.*;
import org.toxsoft.core.tsgui.m5.model.impl.*;
import org.toxsoft.core.tsgui.valed.api.*;
import org.toxsoft.core.tsgui.valed.controls.av.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.uskat.core.api.evserv.*;
import org.toxsoft.uskat.core.api.objserv.*;
import org.toxsoft.uskat.core.api.sysdescr.*;
import org.toxsoft.uskat.core.api.sysdescr.dto.*;
import org.toxsoft.uskat.core.connection.*;

import ru.toxsoft.mcc.ws.journals.e4.uiparts.devel.*;

/**
 * Модель отображения событий
 * <ul>
 * <li>1. Создать свой экземпляр: M5DefaultItemsProvider ip = new M5DefaultItemsProvider();</li>
 * <li>2. При создании панели передать поставщик: panel = model.panelCreator().createCollViewerPanel( ctx, ip );</li>
 * <li>3. По своему усмотрению редактировать список элементов - изменения отразяться в таблице. <br>
 * Например, так IList<T> newItems = s5... получить новые элементы ip.items().addAll( newItems );<br>
 * новые элементы добавятся в отображенный список</li>
 * </ul>
 *
 * @author dima
 */
public class EventM5Model
    extends M5Model<SkEvent> {

  /**
   * Формат наименования источника события.
   */
  private static final String SOURCE_FORMAT = "%s [%s]"; //$NON-NLS-1$

  /**
   * Формат наименования события.
   */
  private static final String VIS_NAME_FORMAT = "%s"; //$NON-NLS-1$

  /**
   * Идентификатор модели.
   */
  public static final String MODEL_ID = "ts.sk.journal.SkEvent"; //$NON-NLS-1$

  /**
   * Идентификатор печатной модели.
   */
  public static final String PRINT_MODEL_ID = "ts.sk.journal.SkEvent.print"; //$NON-NLS-1$

  /**
   * Идентификатор поля {@link #VIS_NAME}.
   */
  public static final String FID_VIS_NAME = "VisName"; //$NON-NLS-1$

  /**
   * Идентификатор поля {@link #TIME}.
   */
  public static final String FID_TIME = "ts.Time"; //$NON-NLS-1$

  /**
   * Идентификатор поля {@link #SOURCE}.
   */
  public static final String FID_SOURCE = "ts.Source"; //$NON-NLS-1$

  /**
   * Идентификатор поля {@link #DESCRIPTION}.
   */
  public static final String FID_DESCRIPTION = "ts.Description"; //$NON-NLS-1$

  /**
   * Идентификатор поля {@link #SHORT_DESCRIPTION}.
   */
  public static final String FID_SHORT_DESCRIPTION = "ts.ShortDescription"; //$NON-NLS-1$

  /**
   * Наименование события
   */
  public final M5AttributeFieldDef<SkEvent> VIS_NAME = new M5AttributeFieldDef<>( FID_VIS_NAME, STRING ) {

    @Override
    protected void doInit() {
      setNameAndDescription( EVENT_NAME_COL_NAME, EVENT_NAME_COL_DESCR );
      setDefaultValue( IAtomicValue.NULL );
      setFlags( M5FF_COLUMN | M5FF_READ_ONLY );
    }

    @Override
    protected IAtomicValue doGetFieldValue( SkEvent aEntity ) {
      // Получаем объект события
      ISkObject skObject = conn.coreApi().objService().find( aEntity.eventGwid().skid() );
      // Получаем его класс
      ISkClassInfo skClass = conn.coreApi().sysdescr().findClassInfo( skObject.classId() );
      // Описание события
      IDtoEventInfo evInfo = skClass.events().list().findByKey( aEntity.eventGwid().propId() );

      return AvUtils.avStr( String.format( VIS_NAME_FORMAT, evInfo.nmName() ) );
    }
  };

  /**
   * Время события
   */
  public final M5AttributeFieldDef<SkEvent> TIME = new M5AttributeFieldDef<>( FID_TIME, TIMESTAMP ) {

    @Override
    protected void doInit() {
      setNameAndDescription( EVENT_TIME_COL_NAME, EVENT_TIME_COL_DESCR );
      setDefaultValue( IAtomicValue.NULL );
      setFlags( M5FF_COLUMN | M5FF_READ_ONLY );
    }

    @Override
    protected IAtomicValue doGetFieldValue( SkEvent aEntity ) {
      return avTimestamp( aEntity.timestamp() );
    }
  };

  /**
   * Короткое описание события - однострочное
   */
  public final M5AttributeFieldDef<SkEvent> SHORT_DESCRIPTION =
      new M5AttributeFieldDef<>( FID_SHORT_DESCRIPTION, STRING ) {

        @Override
        protected void doInit() {
          setNameAndDescription( DESCRIPTION_STR, EV_DESCRIPTION );
          setDefaultValue( IAtomicValue.NULL );
          setFlags( M5FF_COLUMN | M5FF_READ_ONLY );
        }

        @Override
        protected IAtomicValue doGetFieldValue( SkEvent aEntity ) {
          ISkObject srcObj = conn.coreApi().objService().find( aEntity.eventGwid().skid() );
          IMwsModJournalEventFormatter formatter =
              formatterRegistry.find( srcObj.classId(), aEntity.eventGwid().propId() );

          return formatter == null ? VIS_NAME.getFieldValue( aEntity )
              : avStr( formatter.formatShortText( aEntity, context ) );
        }
      };

  /**
   * Описание события - возможно многострочное - в окне детального отображения
   */
  public final M5AttributeFieldDef<SkEvent> DESCRIPTION = new M5AttributeFieldDef<>( FID_DESCRIPTION, STRING ) {

    @Override
    protected void doInit() {
      setNameAndDescription( DESCRIPTION_STR, EV_DESCRIPTION );
      setDefaultValue( IAtomicValue.NULL );
      setFlags( M5FF_DETAIL | M5FF_READ_ONLY );

      setValedEditor( ValedAvStringText.FACTORY.factoryName() );
    }

    @Override
    protected IAtomicValue doGetFieldValue( SkEvent aEntity ) {
      ISkObject srcObj = conn.coreApi().objService().find( aEntity.eventGwid().skid() );
      IMwsModJournalEventFormatter formatter = formatterRegistry.find( srcObj.classId(), aEntity.eventGwid().propId() );

      return formatter == null ? VIS_NAME.getFieldValue( aEntity )
          : avStr( formatter.formatLongText( aEntity, context ) );
    }
  };

  /**
   * Источник события
   */
  public final M5AttributeFieldDef<SkEvent> SOURCE = new M5AttributeFieldDef<>( FID_SOURCE, STRING ) {

    @Override
    protected void doInit() {
      setNameAndDescription( EVENT_SRC_COL_NAME, EVENT_SRC_COL_DESCR );
      setDefaultValue( IAtomicValue.NULL );
      setFlags( M5FF_COLUMN | M5FF_READ_ONLY );
    }

    @Override
    protected IAtomicValue doGetFieldValue( SkEvent aEntity ) {
      ISkObject srcObj = conn.coreApi().objService().find( aEntity.eventGwid().skid() );

      return avStr( String.format( SOURCE_FORMAT, srcObj.readableName(), srcObj.strid() ) );
    }
  };

  /**
   * Соединение с Sk сервером
   */
  ISkConnection conn;

  ITsGuiContext context;

  /**
   * Регистр форматов отображения событий
   */
  IMwsModJournalEventFormattersRegistry formatterRegistry;

  /**
   * Конструктор.
   *
   * @param aConn - соединение с сервером.
   * @param aModelContext - контекст
   */
  public EventM5Model( ISkConnection aConn, ITsGuiContext aModelContext ) {
    this( aConn, aModelContext, false );
  }

  /**
   * Конструктор.
   *
   * @param aConn - соединение с сервером.
   * @param aModelContext - контекст
   * @param aForPrint -признак того, что это печатная версия модели.
   */
  @SuppressWarnings( { "rawtypes", "unchecked" } )
  public EventM5Model( ISkConnection aConn, ITsGuiContext aModelContext, boolean aForPrint ) {
    super( aForPrint ? PRINT_MODEL_ID : MODEL_ID, SkEvent.class );
    conn = aConn;
    context = aModelContext;

    formatterRegistry = context.get( IMwsModJournalEventFormattersRegistry.class );
    setNameAndDescription( EVENTS_LIST_TABLE_NAME, EVENTS_LIST_TABLE_DESCR );
    IListEdit<IM5FieldDef<SkEvent, ?>> fDefs = new ElemArrayList<>();
    fDefs.add( VIS_NAME );
    fDefs.add( TIME );
    fDefs.add( SOURCE );
    if( !aForPrint ) {
      fDefs.add( SHORT_DESCRIPTION );
    }
    fDefs.add( DESCRIPTION );

    addFieldDefs( fDefs );

    setPanelCreator( new M5DefaultPanelCreator<SkEvent>() {

      @Override
      protected IM5EntityPanel<SkEvent> doCreateEntityDetailsPanel( ITsGuiContext aContext ) {

        return new M5DefaultEntityDetailsPanel<>( aContext, model() ) {

          @Override
          protected IValedControl doCreateEditor( IValedControlFactory aFactory, IM5FieldDef<SkEvent, ?> aFieldDef,
              ITsGuiContext bContext ) {
            // ValedAvStringText.IS_MULTI_LINE.setValue( bContext.params(), AvUtils.avBool( true ) );
            return super.doCreateEditor( aFactory, aFieldDef, bContext );
          }
        };
      }
    } );
  }
}
