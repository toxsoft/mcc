package ru.toxsoft.mcc.ws.journals.e4.uiparts.engine;

import java.text.*;
import java.util.*;

import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * TODO - в том или ином виде нужно перенести в tsgui (Max) Декоратор класса {@link DateTime}.
 * <p>
 * В связи с тем, что под Линуксом стандартный widget DateTime не поддерживает региональные установки формата даты и
 * времени (дату всегда выводит mm/dd/yyyy), возникла необходимость найти какой-нибудь обходной путь. Данный класс
 * просто раполагает поверх текстовой части стандартного {@link DateTime} собственную компоненту, которая способна
 * отображать дату и время в указанном формате.<br>
 * Форматы даты и времени должны передаваться в конструкторе. По умолчанию используются следующие форматы:
 * <ul>
 * <li>Для даты - "dd/MM/yyyy"</li>
 * <li>Для времени - "hh:mm"</li>
 * </ul>
 * <br>
 * Если операционная система не Линукс, то ничего не делает.<br>
 * Перед созданием объекта данного класса рекомендуется вызвать статический метод
 * {@link DateTimePickerDecorator#isApplicable()} и создавать объект в случае если указанный метод вернет <b>true</b>
 *
 * @author vs
 */
public class DateTimePickerDecorator {

  /**
   * Имя системного свойства для названия операционной системы
   */
  private static final String OS_NAME_PROPERTY = "os.name"; //$NON-NLS-1$
  /**
   * Имя операционной системы Linux
   */
  private static final String NAME_LINUX       = "linux";   //$NON-NLS-1$

  DateTime timeControl = null;
  DateTime dateControl = null;

  String timeFormatStr = "hh:mm";      //$NON-NLS-1$
  String dateFormatStr = "dd/MM/yyyy"; //$NON-NLS-1$

  SimpleDateFormat dateFormat;

  CLabel   dateLabel;
  Calendar calendar;

  DateTime dtPicker;

  /**
   * Конструктор.
   *
   * @param aDtPicker DateTimePicker - декорируемый объект
   */
  public DateTimePickerDecorator( DateTime aDtPicker ) {
    TsNullArgumentRtException.checkNull( aDtPicker );
    if( isApplicable() ) {
      dtPicker = aDtPicker;
      doInit( aDtPicker );
    }
  }

  /**
   * Конструктор.
   *
   * @param aDtPicker DateTimePicker - декорируемый объект
   * @param aDateFormat String - форматная строка для отображения даты (м.б. null или пустая)
   * @param aTimeFormat String - форматная строка для отображения времени (м.б. null или пустая)
   */
  public DateTimePickerDecorator( DateTime aDtPicker, String aDateFormat, String aTimeFormat ) {
    TsNullArgumentRtException.checkNull( aDtPicker );
    if( isApplicable() ) {
      dateFormatStr = aDateFormat;
      timeFormatStr = aTimeFormat;
      doInit( aDtPicker );
    }
  }

  /**
   * Обновляет надпись.
   * <p>
   * Этот метод необходимо вызывать после программной установки даты временни.
   */
  public void refresh() {
    if( dateLabel != null ) {
      Calendar cal = Calendar.getInstance();
      cal.set( Calendar.YEAR, dtPicker.getYear() );
      cal.set( Calendar.MONTH, dtPicker.getMonth() );
      cal.set( Calendar.DAY_OF_MONTH, dtPicker.getDay() );
      cal.set( Calendar.HOUR_OF_DAY, dtPicker.getHours() );
      cal.set( Calendar.MINUTE, dtPicker.getMinutes() );
      cal.set( Calendar.SECOND, dtPicker.getSeconds() );
      long start = cal.getTimeInMillis();
      dateLabel.setText( dateFormat.format( Long.valueOf( start ) ) );
    }
  }

  /**
   * Возвращает признак того имеет ли смысл применять данный декоратор.
   *
   * @return <b>true</b> - применение данного декоратора имеет смысл<br>
   *         <b>false</b> - применение декоратора бессмысленно - он ничего делать не будет
   */
  public static boolean isApplicable() {
    String osName = System.getProperty( OS_NAME_PROPERTY ).toLowerCase();
    return osName.equals( NAME_LINUX );
  }

  // ------------------------------------------------------------------------------------
  // Внутренняя реализация
  //

  void doInit( DateTime aDtPicker ) {
    calendar = Calendar.getInstance();

    Control[] controls = aDtPicker.getChildren();
    if( controls.length < 1 || controls.length > 2 ) {
      // ожидается что у родителя либо один либо два ребенка класса DateTime
      throw new TsInternalErrorRtException( "It needs one or two DateTime subControls" ); //$NON-NLS-1$
    }
    for( int i = 0; i < controls.length; i++ ) {
      if( controls[i] instanceof DateTime dt ) {
        if( (dt.getStyle() & SWT.TIME) != 0 ) {
          // это контрль для отображения времени

        }
        if( (dt.getStyle() & SWT.DATE) != 0 ) {
          // это контрль для отображения даты
          dateFormat = new SimpleDateFormat( dateFormatStr );
          Control[] dtControls = dt.getChildren();
          for( int j = 0; j < dtControls.length; j++ ) {
            Control control = dtControls[j];
            if( control instanceof Text textControl ) {
              dateLabel = new CLabel( (DateTime)controls[i], SWT.BORDER );
              dateLabel.setBackground( textControl.getBackground() );
              dateLabel.setForeground( textControl.getForeground() );
              dateLabel.setFont( textControl.getFont() );
              textControl.addControlListener( new ControlListener() {

                @Override
                public void controlResized( ControlEvent aE ) {
                  dateLabel.setSize( textControl.getSize() );
                }

                @Override
                public void controlMoved( ControlEvent aE ) {
                  dateLabel.setLocation( textControl.getLocation() );
                }
              } );

              dt.addSelectionListener( new SelectionAdapter() {

                @Override
                public void widgetSelected( SelectionEvent aE ) {
                  updateDate( (DateTime)aE.getSource() );
                }

              } );
            }
          }
          updateDate( dt );
        }
      }
      else {
        throw new TsInternalErrorRtException( "Class of the contol is not DateTime" ); //$NON-NLS-1$
      }
    }
  }

  void updateDate( DateTime aDateTime ) {
    if( dateLabel != null ) {
      int day = aDateTime.getDay();
      int month = aDateTime.getMonth();
      int year = aDateTime.getYear();
      calendar.set( year, month, day );
      dateLabel.setText( dateFormat.format( calendar.getTime() ) );
    }
  }
}
