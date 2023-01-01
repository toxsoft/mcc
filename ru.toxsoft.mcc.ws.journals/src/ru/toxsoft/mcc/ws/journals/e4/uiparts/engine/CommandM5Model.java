package ru.toxsoft.mcc.ws.journals.e4.uiparts.engine;

import static org.toxsoft.core.tsgui.m5.IM5Constants.*;
import static org.toxsoft.core.tslib.av.EAtomicType.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static ru.toxsoft.mcc.ws.journals.e4.uiparts.engine.IMmResources.*;

import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.model.impl.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.uskat.core.api.cmdserv.*;
import org.toxsoft.uskat.core.api.objserv.*;
import org.toxsoft.uskat.core.api.sysdescr.*;
import org.toxsoft.uskat.core.api.sysdescr.dto.*;
import org.toxsoft.uskat.core.connection.*;

/**
 * Модель отображения команд.
 *
 * @author max, dima
 */
public class CommandM5Model
    extends M5Model<IDtoCompletedCommand> {

  private static final String VIS_NAME_FORMAT = "%s <%s> [%s]"; //$NON-NLS-1$

  private static final String AUTHOR_FORMAT = "%s [%s]"; //$NON-NLS-1$

  private static final String EXECUTER_FORMAT = "%s [%s]"; //$NON-NLS-1$

  /**
   * Идентификатор модели.
   */
  public static final String MODEL_ID = "ts.sk.journal.ISkCommand"; //$NON-NLS-1$

  /**
   * Model for print.
   */
  public static final String PRINT_MODEL_ID = MODEL_ID + ".print"; //$NON-NLS-1$

  /**
   * Идентификатор поля {@link #VIS_NAME}.
   */
  public static final String FID_VIS_NAME = "VisName"; //$NON-NLS-1$

  /**
   * Идентификатор поля {@link #TIME}.
   */
  private static final String FID_TIME = "ts.Time"; //$NON-NLS-1$

  /**
   * Идентификатор поля {@link #AUTHOR}.
   */
  private static final String FID_AUTHOR = "ts.Author"; //$NON-NLS-1$

  /**
   * Идентификатор поля {@link #EXECUTER}.
   */
  private static final String FID_EXECUTER = "ts.Executer"; //$NON-NLS-1$

  /**
   * Наименование команды.
   */
  public final M5AttributeFieldDef<IDtoCompletedCommand> VIS_NAME = new M5AttributeFieldDef<>( FID_VIS_NAME, STRING ) {

    @Override
    protected void doInit() {
      setNameAndDescription( VIS_NAME_COL_NAME, VIS_NAME_COL_DESCR );
      setDefaultValue( IAtomicValue.NULL );
      setFlags( M5FF_COLUMN | M5FF_READ_ONLY );

    }

    @Override
    protected IAtomicValue doGetFieldValue( IDtoCompletedCommand aEntity ) {
      // Получаем объект команды
      ISkObject skObject = conn.coreApi().objService().find( aEntity.cmd().cmdGwid().skid() );
      // Получаем его класс
      ISkClassInfo skClass = conn.coreApi().sysdescr().findClassInfo( skObject.classId() );
      // Описание команды
      IDtoCmdInfo cmdInfo = skClass.cmds().list().findByKey( aEntity.cmd().cmdGwid().propId() );

      return avStr(
          String.format( VIS_NAME_FORMAT, cmdInfo.nmName(), cmdInfo.description(), aEntity.cmd().cmdGwid().strid() ) );
    }
  };

  /**
   * Время команды
   */
  public final M5AttributeFieldDef<IDtoCompletedCommand> TIME = new M5AttributeFieldDef<>( FID_TIME, TIMESTAMP ) {

    @Override
    protected void doInit() {
      setNameAndDescription( TIME_COL_NAME, TIME_COL_DESCR );
      setDefaultValue( IAtomicValue.NULL );
      setFlags( M5FF_COLUMN | M5FF_READ_ONLY );
    }

    @Override
    protected IAtomicValue doGetFieldValue( IDtoCompletedCommand aEntity ) {
      return avTimestamp( aEntity.timestamp() );
    }
  };

  /**
   * Автор команды
   */
  public final M5AttributeFieldDef<IDtoCompletedCommand> AUTHOR = new M5AttributeFieldDef<>( FID_AUTHOR, STRING ) {

    @Override
    protected void doInit() {
      setNameAndDescription( AUTHOR_COL_NAME, AUTHOR_COL_DESCR );
      setDefaultValue( IAtomicValue.NULL );
      setFlags( M5FF_COLUMN | M5FF_READ_ONLY );
    }

    @Override
    protected IAtomicValue doGetFieldValue( IDtoCompletedCommand aEntity ) {
      ISkObject srcObj = conn.coreApi().objService().find( aEntity.cmd().authorSkid() );

      return avStr( String.format( AUTHOR_FORMAT, srcObj.readableName(), srcObj.strid() ) );
    }
  };

  /**
   * Исполнитель команды
   */
  public final M5AttributeFieldDef<IDtoCompletedCommand> EXECUTER = new M5AttributeFieldDef<>( FID_EXECUTER, STRING ) {

    @Override
    protected void doInit() {
      setNameAndDescription( CMD_EXEC_COL_NAME, CMD_EXEC_COL_DESCR );
      setDefaultValue( IAtomicValue.NULL );
      setFlags( M5FF_COLUMN | M5FF_READ_ONLY );
    }

    @Override
    protected IAtomicValue doGetFieldValue( IDtoCompletedCommand aEntity ) {
      ISkObject srcObj = conn.coreApi().objService().find( aEntity.cmd().cmdGwid().skid() );

      return avStr( String.format( EXECUTER_FORMAT, srcObj.readableName(), srcObj.strid() ) );
    }
  };

  ISkConnection conn;

  /**
   * Конструктор.
   *
   * @param aConn - соединение с сервером.
   * @param aForPrint - attribute signs the model for prints (if true).
   */
  public CommandM5Model( ISkConnection aConn, boolean aForPrint ) {
    super( aForPrint ? PRINT_MODEL_ID : MODEL_ID, IDtoCompletedCommand.class );
    conn = aConn;
    setNameAndDescription( CMDS_LIST_TABLE_NAME, CMDS_LIST_TABLE_DESCR );
    IListEdit<IM5FieldDef<IDtoCompletedCommand, ?>> fDefs = new ElemArrayList<>();
    fDefs.add( VIS_NAME );
    fDefs.add( TIME );
    fDefs.add( AUTHOR );
    fDefs.add( EXECUTER );

    addFieldDefs( fDefs );
  }
}
