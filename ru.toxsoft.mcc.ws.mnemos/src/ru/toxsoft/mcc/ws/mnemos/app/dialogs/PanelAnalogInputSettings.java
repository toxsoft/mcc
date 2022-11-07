package ru.toxsoft.mcc.ws.mnemos.app.dialogs;

import static ru.toxsoft.tsgui.dialogs.CommonDialogBase.*;

import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.dialogs.datarec.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.utils.errors.*;

import ru.toxsoft.s5.common.ugwi.*;
import ru.toxsoft.tsgui.dialogs.*;
import ru.toxsoft.vj.ws.rtscreens.ds.IRtDataGate;
import ru.toxsoft.vj.ws.rtscreens.rt.screen.controls.swt.RtBooleanLabel;

/**
 * Диалог параметров "Аналогового входа".
 * <p>
 * Питание и отклонение stateWord<br>
 * бит - 1 calibrationError Текст ... ошибка калибровки (отклонение свыше 5%)<br>
 * бит - 2 calibrationWarning Текст '... предупреждение калибровки (отклонение < 5%)
 * <p>
 * <b>Вопросы:</b><br>
 * Откуда брать значения для "Значение сигнала в пределах измерения"?<br>
 * Откуда брать значения для "Блокировка"?<br>
 * Откуда брать значения для установки "Время фильтрации"?<br>
 *
 * @author vs
 */
public class PanelAnalogInputSettings
    extends AbstractMccRtPanel {

  // CmdText imitationValue;

  /**
   * Imitation group
   */
  CmdBoolButton  btnImitation;
  RtBooleanLabel labelImitation;

  /**
   * Blocking group
   */
  CmdBoolButton btnBlock;

  /**
   * Time group
   */
  CmdText txtTestTime;
  CmdText txtContactTime;

  /**
   * Коснтруктор для использования панели в диалоге.
   *
   * @param aParent Composite - родительская компонента
   * @param aOwnerDialog {@link CommonDialogBase} - родительский диалог
   */
  public PanelAnalogInputSettings( Composite aParent, CommonDialogBase<Object, VjPanelContext> aOwnerDialog ) {
    super( aParent, aOwnerDialog, true );
    init();
  }

  static IDialogPanelCreator<Object, VjPanelContext> creator = ( aParent, aOwnerDialog ) -> new PanelAnalogInputSettings( aParent, aOwnerDialog );

  void init() {

    classId = context().bsObject().classId();
    objStrid = context().bsObject().strid();

    GridLayout layout = new GridLayout( 1, false );
    setLayout( layout );

    createErrorsGroup();

    createImitationGroup();

    createBlokingGroup();

    createFiltrationGroup();

    createScaleGroup();

    createIndicationGroup();

  }

  // ------------------------------------------------------------------------------------
  // Реализация методов AbstractRtDialogPanel
  //

  @Override
  void doCreatePins( IRtDataGate<IAtomicValue> aDataGate ) {
    // nop
    // aDataGate.sendAll();
  }

  // @Override
  // protected void onTimerTick() {
  // super.onTimerTick();
  // }

  // ------------------------------------------------------------------------------------
  // Методы вызова диалога
  //

  /**
   * Выводит диалог XXX
   * <p>
   *
   * @param aContext - контекст панели не null
   * @param aCaption - заголовок окна
   * @param aTitle - описание фукнциональности диалога
   * @return null
   */
  public static Object showDialog( MccDialogContext aContext, String aCaption, String aTitle ) {
    TsNullArgumentRtException.checkNull( aContext );
    Shell shell = aContext.getShell();
    CommonDialogBase<Object, MccDialogContext> d = new CommonDialogBase<>( shell, null, aContext, aCaption,
        "Установите требуемые параметры", DF_NO_APPROVE, creator );
    return d.execData();
  }

  // ------------------------------------------------------------------------------------
  // Внутренняя реализация
  //

  void createErrorsGroup() {
    Group group = createGroup( this, "Питание и отклонение", 1 );

    String classId = context().bsObject().classId();
    String objStrid = context().bsObject().strid();

    String ugwiString;
    ugwiString = UgwiUtils.createConcreteUgwiString( EGreenWorldEntity.GW_DATA, classId, objStrid, "stateWord", null );

    RtBitValueLabel bvl;
    bvl = new RtBitValueLabel( group, "noPower", "Напряжение питания", 1 << 3, imgGreenLamp, imgGrayLamp );
    addBitValueLabel( new Ugwi( ugwiString ), bvl );

    bvl = new RtBitValueLabel( group, "error", "Значение сигнала вышло за пределы измерения свыше 5%", 1 << 1,
        imgRedLamp, imgGrayLamp );
    addBitValueLabel( new Ugwi( ugwiString ), bvl );

    bvl = new RtBitValueLabel( group, "warn", "Значение сигнала вышло за пределы измерения до 5%", 1 << 2, imgRedLamp,
        imgGrayLamp );
    addBitValueLabel( new Ugwi( ugwiString ), bvl );

    bvl = new RtBitValueLabel( group, "norm", "Значение сигнала в пределах измерения", 1 << 0, imgGreenLamp,
        imgGrayLamp );
    addBitValueLabel( new Ugwi( ugwiString ), bvl );

    // ugwiString = UgwiUtils.createConcreteUgwiString( EGreenWorldEntity.GW_DATA, classId, objStrid, "chnlUPwr", null
    // );
    // RtBooleanLabel rbl = new RtBooleanLabel( "norm", new Ugwi( ugwiString ), imgGreenLamp, imgGrayLamp );
    // rbl.createControl( group, "Значение сигнала в пределах измерения" );
    // addBooleanLabel( rbl );

  }

  void createImitationGroup() {
    Group group = createGroup( this, "Имитация", 2 );

    IUgwi cmdUgwi = createCommandUgwi( "imitation" );
    IUgwi dataUgwi = createDataUgwi( "imitation" );
    String text = "Разрешение имитации";
    btnImitation =
        new CmdBoolButton( "imitation" + objStrid, group, text, serverApi, cmdUgwi, dataUgwi, SWT.CHECK, false );
    addBoolValueButton( btnImitation );

    // RtBitValueLabel rbl;
    // rbl = new RtBitValueLabel( group, "imitationTurnedOn", "Имитация включена", 1 << 4, imgGreenLamp, imgGrayLamp );
    // addBitValueLabel( createDataUgwi( "stateWord" ), rbl );

    RtBooleanLabel rbl;
    rbl = createBooleanLabel( "imitationTurnedOn", "imitation", imgGreenLamp, imgGrayLamp );
    rbl.createControl( group, "Имитация включена" );

    CLabel l = new CLabel( group, SWT.CENTER );
    l.setText( "Значение имитации: " );

    createFloatingEditor( group, "imitationValue", "imitationValue", "%.1f", 60 );
  }

  void createBlokingGroup() {
    Group group = createGroup( this, "Блокировка", 1 );

    IUgwi cmdUgwi = createCommandUgwi( "enblAlarmTreat" ); // FIXME "enblAlarmTreat" - такой команды нет
    IUgwi dataUgwi = createDataUgwi( "enblAlarmTreat" );
    String text = "Разрешение работы (отключение блокировки)";
    btnBlock = new CmdBoolButton( "btnBlock" + objStrid, group, text, serverApi, cmdUgwi, dataUgwi, SWT.CHECK, false );
    addBoolValueButton( btnBlock );
  }

  void createFiltrationGroup() {
    Group group = createGroup( this, "Фильтрация", 2 );

    String ugwiStr;

    CLabel l = new CLabel( group, SWT.CENTER );
    l.setText( "Время фильтрации (мсек)" );
    createIntegerEditor( group, "filterConst", "filterConst", 60 ); // FIXME нет такой команды filterConst

    l = new CLabel( group, SWT.CENTER );
    l.setText( "Входной сигнал с модуля" );
    // ugwiStr =
    // UgwiUtils.createConcreteUgwiString( EGreenWorldEntity.GW_DATA, classId, objStrid, "channelAddress", null );
    // RtTextLabel rtl = new RtTextLabel( "inputValue", new Ugwi( ugwiStr ), "%d" );
    // rtl.createControl( group, SWT.BORDER, "90" );
    createNumericLabel( group, SWT.CENTER | SWT.BORDER, "channelAddress", "%d", 60 );

    l = new CLabel( group, SWT.CENTER );
    l.setText( "Входное значение отфильтрованное" );
    // rtl = new RtTextLabel( "physicalValue", new Ugwi( ugwiStr ), "%.2f" );
    // rtl.createControl( group, SWT.BORDER, "4.14" );
    createNumericLabel( group, SWT.CENTER | SWT.BORDER, "physicalValue", "%.2f", 60 );
  }

  void createScaleGroup() {
    Group group = createGroup( this, "Масштабирование", 2 );

    CLabel l;
    int fieldWidth = 60;
    String formatStr = "%.2f"; //$NON-NLS-1$

    l = new CLabel( group, SWT.CENTER );
    l.setText( "Значение входного сигнала X0" );
    createFloatingEditor( group, "x0", "x0", formatStr, fieldWidth ); //$NON-NLS-1$//$NON-NLS-2$

    l = new CLabel( group, SWT.CENTER );
    l.setText( "Значение входного сигнала X1" );
    createFloatingEditor( group, "x1", "x1", formatStr, fieldWidth ); //$NON-NLS-1$//$NON-NLS-2$

    l = new CLabel( group, SWT.CENTER );
    l.setText( "Значение входного сигнала Y0" );
    createFloatingEditor( group, "y0", "y0", formatStr, fieldWidth ); //$NON-NLS-1$//$NON-NLS-2$

    l = new CLabel( group, SWT.CENTER );
    l.setText( "Значение входного сигнала Y1" );
    createFloatingEditor( group, "y1", "y1", formatStr, fieldWidth ); //$NON-NLS-1$//$NON-NLS-2$

  }

  void createIndicationGroup() {
    Group group = createGroup( this, "Индикация/Генерация", 2 );

    CLabel l;

    l = new CLabel( group, SWT.CENTER );
    l.setText( "Время срабатывания (мсек):" );
    createIntegerEditor( group, "eventTime", "eventTime", 60 );
  }

}
