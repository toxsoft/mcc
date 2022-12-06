package ru.toxsoft.mcc.ws.core.templates.utils;

import static ru.toxsoft.mcc.ws.core.templates.utils.ITsResources.*;

import java.util.*;

import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.dialogs.datarec.*;
import org.toxsoft.core.tslib.bricks.time.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Диалог для выбора интервала времени
 *
 * @author Dima
 */
public class IntervalSelectionDialogPanel
    extends AbstractTsDialogPanel<TimeInterval, ITsGuiContext> {

  // ------------------------------------------------------------------------------------
  // Создание панели
  //

  /**
   * Конcтруктор для использования панели в диалоге.
   *
   * @param aParent Composite - родительская компонента
   * @param aOwnerDialog {@link TsDialog} - родительский диалог
   */
  public IntervalSelectionDialogPanel( Composite aParent, TsDialog<TimeInterval, ITsGuiContext> aOwnerDialog ) {
    super( aParent, aOwnerDialog );
    init( this );
  }

  // ------------------------------------------------------------------------------------
  // Реализация методов {@link AbstractDataRecordPanel}
  //

  @Override
  protected void doSetDataRecord( TimeInterval aParams ) {
    Calendar cal = Calendar.getInstance();
    cal.setTimeInMillis( aParams.startTime() );
    int year = cal.get( Calendar.YEAR );
    int month = cal.get( Calendar.MONTH );
    int day = cal.get( Calendar.DAY_OF_MONTH );
    int hours = cal.get( Calendar.HOUR_OF_DAY );
    int minutes = cal.get( Calendar.MINUTE );
    int seconds = cal.get( Calendar.SECOND );
    startCalendar.setDate( year, month, day );
    startTime.setTime( hours, minutes, seconds );

    cal.setTimeInMillis( aParams.endTime() );
    year = cal.get( Calendar.YEAR );
    month = cal.get( Calendar.MONTH );
    day = cal.get( Calendar.DAY_OF_MONTH );
    hours = cal.get( Calendar.HOUR_OF_DAY );
    minutes = cal.get( Calendar.MINUTE );
    seconds = cal.get( Calendar.SECOND );
    endCalendar.setDate( year, month, day );
    endTime.setTime( hours, minutes, seconds );
  }

  @Override
  protected TimeInterval doGetDataRecord() {
    Calendar cal = Calendar.getInstance();
    cal.set( Calendar.YEAR, startCalendar.getYear() );
    cal.set( Calendar.MONTH, startCalendar.getMonth() );
    cal.set( Calendar.DAY_OF_MONTH, startCalendar.getDay() );
    cal.set( Calendar.HOUR_OF_DAY, startTime.getHours() );
    cal.set( Calendar.MINUTE, startTime.getMinutes() );
    cal.set( Calendar.SECOND, startTime.getSeconds() );
    long start = cal.getTimeInMillis();

    cal.set( Calendar.YEAR, endCalendar.getYear() );
    cal.set( Calendar.MONTH, endCalendar.getMonth() );
    cal.set( Calendar.DAY_OF_MONTH, endCalendar.getDay() );
    cal.set( Calendar.HOUR_OF_DAY, endTime.getHours() );
    cal.set( Calendar.MINUTE, endTime.getMinutes() );
    cal.set( Calendar.SECOND, endTime.getSeconds() );
    long end = cal.getTimeInMillis();

    TimeInterval retVal = new TimeInterval( start, end );
    return retVal;
  }

  static IDialogPanelCreator<TimeInterval, ITsGuiContext> creator = IntervalSelectionDialogPanel::new;

  // --------------------------------------------------------------------------
  // Открытое API
  //

  /**
   * Выводит диалог выбора диапазон времени запроса
   * <p>
   * Если пользователь отказался от выбора, возвращает null.
   *
   * @param aShell {@link Shell} - родительское окно
   * @param aInitParams - начальное значение параметров
   * @param aContext - контекст приложения
   * @return {@link TimeInterval} - выбранные параметры или null, если пользователь отказался от выбора
   */
  public static TimeInterval getParams( Shell aShell, TimeInterval aInitParams, ITsGuiContext aContext ) {

    TsNullArgumentRtException.checkNull( aContext );
    ITsDialogInfo dlgInfo = new TsDialogInfo( aContext, DLG_TITLE_PERIOD_SEL, DLG_HEADER_PERIOD_SEL );
    TsDialog<TimeInterval, ITsGuiContext> d = new TsDialog<>( dlgInfo, aInitParams, aContext, creator );
    return d.execData();

  }

  // ------------------------------------------------------------------------------------
  // Внутренняя реализация
  //
  static String startLabelText = START_STR;
  static String endLabelText   = END_STR;
  // Контроль ввода времени начала
  private DateTime startCalendar;
  private DateTime startTime;
  // Контроль ввода времени окончания
  private DateTime endCalendar;
  private DateTime endTime;

  private void init( Composite aParent ) {
    setLayout( new GridLayout( 2, false ) );

    Group gridParamsGroup = new Group( aParent, SWT.NONE );
    gridParamsGroup.setLayoutData( new GridData( SWT.FILL, SWT.FILL, true, false, 2, 1 ) );
    gridParamsGroup.setLayout( new GridLayout( 3, false ) );

    CLabel l = new CLabel( gridParamsGroup, SWT.CENTER );
    l.setText( startLabelText );
    startCalendar = new DateTime( gridParamsGroup, SWT.DROP_DOWN );
    startTime = new DateTime( gridParamsGroup, SWT.TIME );

    GridData gd = new GridData( GridData.FILL_BOTH );
    gd.verticalAlignment = GridData.FILL;
    gd.grabExcessHorizontalSpace = true;
    startTime.setLayoutData( gd );

    l = new CLabel( gridParamsGroup, SWT.CENTER );
    l.setText( endLabelText );
    endCalendar = new DateTime( gridParamsGroup, SWT.DROP_DOWN );
    endTime = new DateTime( gridParamsGroup, SWT.TIME );

    gd.verticalAlignment = GridData.FILL;
    gd.grabExcessHorizontalSpace = true;
    endTime.setLayoutData( gd );
  }
}
