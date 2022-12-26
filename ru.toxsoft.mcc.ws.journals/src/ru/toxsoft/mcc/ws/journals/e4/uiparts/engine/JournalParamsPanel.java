package ru.toxsoft.mcc.ws.journals.e4.uiparts.engine;

import static ru.toxsoft.mcc.ws.journals.e4.uiparts.JournalsLibUtils.*;
import static ru.toxsoft.mcc.ws.journals.e4.uiparts.engine.IMmResources.*;

import org.eclipse.e4.core.contexts.*;
import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.utils.swt.*;
import org.toxsoft.core.tsgui.widgets.*;
import org.toxsoft.core.tslib.bricks.events.change.*;
import org.toxsoft.core.tslib.bricks.time.*;
import org.toxsoft.core.tslib.bricks.time.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Реализация панели тул-бара {@link IJournalParamsPanel}.
 *
 * @author goga, dima
 */
public class JournalParamsPanel
    implements IJournalParamsPanel {

  /*
   * Картинка для действия "Фильтр запроса"
   */
  // static Image querySelectedImage

  // = AbstractUIPlugin.imageDescriptorFromPlugin( Activator.PLUGIN_ID, ICON_FILTER ).createImage();
  /*
   * Картинка для действия "Экспорт в Excel" page_excel.png
   */
  // static Image export2XlsImage =
  // AbstractUIPlugin.imageDescriptorFromPlugin( Activator.PLUGIN_ID, ICON_EXCEL ).createImage();
  /*
   * Картинка для действия "Запросить данные"
   */
  // static Image queryRunImage =
  // AbstractUIPlugin.imageDescriptorFromPlugin( Activator.PLUGIN_ID, ICON_RUN_QUERY ).createImage();
  /*
   * Картинка для действия "Печать данных"
   */
  // static Image printImage = AbstractUIPlugin.imageDescriptorFromPlugin( Activator.PLUGIN_ID, ICON_PRINT
  // ).createImage();

  final GenericChangeEventer         genericChangeListenersHolder;
  TsComposite                        backplane = null;
  IEclipseContext                    appContext;
  ITsGuiContext                      context;
  /**
   * Только выбранные пользователем
   */
  IConcerningEventsParams            selectedParams;
  DateTime                           sTime     = null;
  DateTime                           eTime     = null;
  /**
   * Текущее действие пользователя
   */
  IJournalParamsPanel.ECurrentAction currAction;
  /**
   * Путь к файлу для экспорта в Excel
   */
  protected String                   xlsFileName;

  protected String searchString;

  /**
   * Пустой конструктор.
   *
   * @param aContext ITsGuiContext - контекст.
   */
  public JournalParamsPanel( ITsGuiContext aContext ) {
    genericChangeListenersHolder = new GenericChangeEventer( this );
    selectedParams = new ConcerningEventsParams();
    context = aContext;
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса ILazyControl
  //

  @SuppressWarnings( "unused" )
  @Override
  public Control createControl( Composite aParent ) {
    TsIllegalStateRtException.checkNoNull( backplane );
    backplane = new TsComposite( aParent );

    backplane.setLayout( new GridLayout( 10, false ) );
    // название панели
    Label l = new Label( backplane, SWT.CENTER );
    l.setText( STR_L_JPP_NAME );
    sTime = new DateTime( backplane, SWT.BORDER | SWT.DATE | SWT.TIME | SWT.CALENDAR | SWT.DROP_DOWN );
    if( DateTimePickerDecorator.isApplicable() ) {
      new DateTimePickerDecorator( sTime );
    }

    // По умолчанию запрос за сутки
    // sTime.setTimeInMillis( System.currentTimeMillis() - 24 * 60 * 60 * 1000L );
    // просто разделитель
    l = new Label( backplane, SWT.CENTER );
    l.setText( "  -  " ); //$NON-NLS-1$

    eTime = new DateTime( backplane, SWT.BORDER | SWT.DATE | SWT.TIME | SWT.CALENDAR | SWT.DROP_DOWN );
    if( DateTimePickerDecorator.isApplicable() ) {
      new DateTimePickerDecorator( eTime );
    }
    // кнопка запуска запроса
    Button runQuery = new Button( backplane, SWT.PUSH );
    // runQuery.setImage( queryRunImage );
    runQuery.setText( "Run" );
    runQuery.addSelectionListener( new SelectionListenerAdapter() {

      @Override
      public void widgetSelected( SelectionEvent aE ) {
        currAction = ECurrentAction.QUERY_ALL;
        genericChangeListenersHolder.fireChangeEvent();
      }

    } );

    // кнопка фильтрованного запроса
    Button b = new Button( backplane, SWT.PUSH );
    // b.setImage( querySelectedImage );
    b.setText( "Filter" );
    b.addSelectionListener( new SelectionListenerAdapter() {

      @Override
      public void widgetSelected( SelectionEvent aE ) {
        IConcerningEventsParams retVal =
            DialogConcerningEventsParams.edit( (ConcerningEventsParams)selectedParams, context );
        if( retVal != null ) {
          selectedParams = retVal;
          currAction = ECurrentAction.QUERY_SELECTED;
          genericChangeListenersHolder.fireChangeEvent();
        }
      }

    } );

    // просто разделитель
    l = new Label( backplane, SWT.CENTER );
    l.setText( "  |  " ); //$NON-NLS-1$

    // кнопка печати
    Button printButton = new Button( backplane, SWT.PUSH );
    // printButton.setImage( printImage );
    printButton.setText( "Print" );
    printButton.addSelectionListener( new SelectionListenerAdapter() {

      @Override
      public void widgetSelected( SelectionEvent aE ) {
        currAction = ECurrentAction.PRINT;
        genericChangeListenersHolder.fireChangeEvent();
      }

    } );

    // кнопка экспорта Excel
    // Button exportExcel = new Button( backplane, SWT.PUSH );
    // exportExcel.setImage( export2XlsImage );
    // exportExcel.addSelectionListener( new SelectionListenerAdapter() {
    //
    // @Override
    // public void widgetSelected( SelectionEvent aE ) {
    // // Запрашиваем путь для файла вывода
    // Shell parentShell = appContext.get( Shell.class );
    // FileDialog fileDialog = new FileDialog( parentShell, SWT.SAVE );
    // fileDialog.setText( DLG_T_EXPORT_FILE );
    // xlsFileName = fileDialog.open();
    // if( xlsFileName != null ) {
    // currAction = ECurrentAction.EXPORT_EXCEL;
    // genericChangeListenersHolder.fireChangeEvent();
    // }
    // }
    //
    // } );
    //
    // // просто разделитель
    // l = new Label( backplane, SWT.CENTER );
    // l.setText( " | " ); //$NON-NLS-1$
    //
    // // поле для поиска по тексту
    // Text searchText = new Text( backplane, SWT.BORDER );
    //
    // // кнопка поиска по тексту
    // Button search = new Button( backplane, SWT.PUSH );
    // search.setText( STR_SEARCH_BTN_NAME );
    // search.addSelectionListener( new SelectionListenerAdapter() {
    //
    // @Override
    // public void widgetSelected( SelectionEvent aE ) {
    // // отправляем событие
    // if( searchText.getText().length() > 0 ) {
    // searchString = searchText.getText();
    // currAction = ECurrentAction.SEARCH_IN_LIST;
    // genericChangeListenersHolder.fireChangeEvent();
    // }
    // }
    //
    // } );

    return backplane;
  }

  @Override
  public Control getControl() {
    return backplane;
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса IGenericChangeEventProducer
  //

  // @Override
  // public void addGenericChangeListener( IGenericChangeListener aListener ) {
  // genericChangeListenersHolder.addGenericChangeListener( aListener );
  // }

  // @Override
  // public void removeGenericChangeListener( IGenericChangeListener aListener ) {
  // genericChangeListenersHolder.removeGenericChangeListener( aListener );
  // }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса IJournalParamsPanel
  //

  @Override
  public ITimeInterval interval() {
    return new TimeInterval( getTimeInMiles( sTime ), getTimeInMiles( eTime ) );
  }

  @Override
  public IConcerningEventsParams selectedParams() {
    return selectedParams;
  }

  @Override
  public void setAppContext( IEclipseContext aAppContext ) {
    appContext = aAppContext;
  }

  @Override
  public ECurrentAction currentAction() {
    return currAction;
  }

  @Override
  public String xlsFileName() {
    return xlsFileName;
  }

  @Override
  public String searchString() {
    return searchString;
  }

  @Override
  public void setContext( ITsGuiContext aContext ) {
    context = aContext;
  }

  @Override
  public void addListener( IGenericChangeListener aListener ) {
    genericChangeListenersHolder.addListener( aListener );
  }

  @Override
  public void removeListener( IGenericChangeListener aListener ) {
    genericChangeListenersHolder.removeListener( aListener );
  }

  @Override
  public void muteListener( IGenericChangeListener aListener ) {
    genericChangeListenersHolder.muteListener( aListener );
  }

  @Override
  public void unmuteListener( IGenericChangeListener aListener ) {
    genericChangeListenersHolder.unmuteListener( aListener );

  }

  @Override
  public boolean isListenerMuted( IGenericChangeListener aListener ) {
    return genericChangeListenersHolder.isListenerMuted( aListener );
  }

  @Override
  public void pauseFiring() {
    genericChangeListenersHolder.pauseFiring();
  }

  @Override
  public void resumeFiring( boolean aFireDelayed ) {
    genericChangeListenersHolder.resumeFiring( aFireDelayed );
  }

  @Override
  public boolean isFiringPaused() {
    return genericChangeListenersHolder.isFiringPaused();
  }

  @Override
  public boolean isPendingEvents() {
    return genericChangeListenersHolder.isPendingEvents();
  }

  @Override
  public void resetPendingEvents() {
    genericChangeListenersHolder.resetPendingEvents();
  }

}
