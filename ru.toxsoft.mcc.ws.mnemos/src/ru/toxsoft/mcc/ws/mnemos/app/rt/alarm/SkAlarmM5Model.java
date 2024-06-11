package ru.toxsoft.mcc.ws.mnemos.app.rt.alarm;

import static org.toxsoft.core.tsgui.m5.IM5Constants.*;
import static org.toxsoft.core.tsgui.valed.api.IValedControlConstants.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.toxsoft.core.tsgui.m5.model.impl.M5AttributeFieldDef;
import org.toxsoft.core.tsgui.m5.model.impl.M5Model;
import org.toxsoft.core.tsgui.valed.controls.av.ValedAvStringText;
import org.toxsoft.core.tslib.av.EAtomicType;
import org.toxsoft.core.tslib.av.IAtomicValue;
import org.toxsoft.skf.legacy.alarms.lib.ISkAlarm;
import org.toxsoft.uskat.core.api.objserv.ISkObject;
import org.toxsoft.uskat.core.connection.ISkConnection;
import org.toxsoft.uskat.core.gui.conn.ISkConnectionSupplier;

/**
 * M5-model of {@link ISkAlarm}.
 *
 * @author max
 */
public class SkAlarmM5Model
    extends M5Model<ISkAlarm> {

  /**
   * формат для отображения метки времени
   */
  private static final String     timestampFormatString = "dd.MM.yy HH:mm:ss.SSS ";                     //$NON-NLS-1$
  private static final DateFormat timestampFormat       = new SimpleDateFormat( timestampFormatString );

  private static final String STR_N_PARAM_AUTHOR = "Автор";
  private static final String STR_D_PARAM_AUTHOR = "Автор тревоги";

  private static final String STR_N_PARAM_MESSAGE = "Сообщение";
  private static final String STR_D_PARAM_MESSAGE = "Сообщение тревоги";

  private static final String STR_N_PARAM_TIME = "Время";
  private static final String STR_D_PARAM_TIME = "Время тревоги";

  private static final String STR_N_ALARM = "Тревоги";
  private static final String STR_D_ALARM = "Список тревог";

  /**
   * Model id
   */
  public static final String MODEL_ID = "sk.Alarm"; //$NON-NLS-1$

  /**
   * Print Model id
   */
  public static final String PRINT_MODEL_ID = MODEL_ID + ".print"; //$NON-NLS-1$

  /**
   * Author of alarm
   */
  public static final String FID_AUTHOR = "author"; //$NON-NLS-1$

  /**
   * Message of alarm
   */
  public static final String FID_MESSAGE = "message"; //$NON-NLS-1$

  /**
   * Time of alarm
   */
  public static final String FID_TIME = "time"; //$NON-NLS-1$

  /**
   * Attribute {@link ISkAlarm#authorId() } author of alarm
   */
  public M5AttributeFieldDef<ISkAlarm> AUTHOR = new M5AttributeFieldDef<>( FID_AUTHOR, EAtomicType.STRING, //
      TSID_NAME, STR_N_PARAM_AUTHOR, //
      TSID_DESCRIPTION, STR_D_PARAM_AUTHOR, //
      OPID_EDITOR_FACTORY_NAME, ValedAvStringText.FACTORY_NAME //
  ) {

    @Override
    protected void doInit() {
      setFlags( M5FF_COLUMN );
    }

    protected IAtomicValue doGetFieldValue( ISkAlarm aEntity ) {
      // dima 17.01.23
      // return avStr( aEntity.authorId().strid() );
      ISkConnectionSupplier connSup = tsContext().get( ISkConnectionSupplier.class );
      ISkConnection conn = connSup.defConn();
      ISkObject author = conn.coreApi().objService().find( aEntity.authorId() );
      return avStr( author.nmName() );
    }

  };

  /**
   * Attribute {@link ISkAlarm#message() } message of alarm
   */
  public M5AttributeFieldDef<ISkAlarm> MESSAGE = new M5AttributeFieldDef<>( FID_MESSAGE, EAtomicType.STRING, //
      TSID_NAME, STR_N_PARAM_MESSAGE, //
      TSID_DESCRIPTION, STR_D_PARAM_MESSAGE, //
      OPID_EDITOR_FACTORY_NAME, ValedAvStringText.FACTORY_NAME //
  ) {

    @Override
    protected void doInit() {
      setFlags( M5FF_COLUMN );
    }

    protected IAtomicValue doGetFieldValue( ISkAlarm aEntity ) {
      return avStr( aEntity.message() );
    }

  };

  /**
   * Attribute {@link ISkAlarm#timestamp() } time of alarm
   */
  public M5AttributeFieldDef<ISkAlarm> TIME = new M5AttributeFieldDef<>( FID_TIME, EAtomicType.STRING, //
      TSID_NAME, STR_N_PARAM_TIME, //
      TSID_DESCRIPTION, STR_D_PARAM_TIME, //
      OPID_EDITOR_FACTORY_NAME, ValedAvStringText.FACTORY_NAME //
  ) {

    @Override
    protected void doInit() {
      setFlags( M5FF_COLUMN );
    }

    protected IAtomicValue doGetFieldValue( ISkAlarm aEntity ) {
      return avStr( timestampFormat.format( new Date( aEntity.timestamp() ) ) );
    }

  };

  /**
   * Constructor
   *
   * @param aForPrint - attribute signs the model for prints (if true).
   */
  public SkAlarmM5Model( boolean aForPrint ) {
    super( aForPrint ? PRINT_MODEL_ID : MODEL_ID, ISkAlarm.class );
    setNameAndDescription( STR_N_ALARM, STR_D_ALARM );
    // add fields
    addFieldDefs( TIME, AUTHOR, MESSAGE );
  }

}
