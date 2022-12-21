package ru.toxsoft.mcc.ws.mnemos.app.rt.chart.data_aliases.impl;

import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.model.*;
import org.toxsoft.core.tsgui.m5.model.impl.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.gw.skid.*;
import org.toxsoft.uskat.ggprefs.lib.*;

import ru.toxsoft.mcc.ws.mnemos.app.rt.chart.data_aliases.*;

/**
 * Модель используемая при редактировании списка data aliases
 *
 * @author dima
 */
public class GuiDataAliasesPrefsEditModel
    extends M5Model<Skid> {

  /**
   * Идентификатор модели.
   */
  public static final String MODEL_ID =
      "ru.toxsoft.mcc.ws.mnemos.app.rt.chart.data_aliases.impl.GuiDataAliasesPrefsEditModel"; //$NON-NLS-1$

  /**
   * Идентификатор модели списка data aliases
   */
  public static final String DATA_ALIASES_MODEL_ID = "data.aliases.list.model"; //$NON-NLS-1$

  private IGuiGwPrefsSection prefsSection;

  /**
   * Конструктор модели редактирования опций gui объекта
   *
   * @param aPrefsSection - редактируемый раздел
   * @param aObjSkid - идентификатор gui объекта
   */
  public GuiDataAliasesPrefsEditModel( IGuiGwPrefsSection aPrefsSection, Skid aObjSkid ) {
    super( MODEL_ID, Skid.class );

    setNameAndDescription( "Опции объекта", "Опции объекта для редактирования" ); //$NON-NLS-1$//$NON-NLS-2$

    prefsSection = aPrefsSection;

    IStridablesList<IDataDef> optDefs = prefsSection.listOptionDefs( aObjSkid );

    IListEdit<IM5FieldDef<?, ?>> fDefs = new ElemArrayList<>();

    // формируем поля - по одному на опцию
    for( IDataDef optDef : optDefs ) {
      fDefs.add( createFieldDef( optDef ) );
    }

    addFieldDefs( fDefs.toArray( new IM5FieldDef[0] ) );

  }

  private IM5FieldDef<?, ?> createFieldDef( IDataDef optDef ) {

    IM5MultiModownFieldDef<Skid, IDataNameAlias> fDef =
        new M5MultiModownFieldDef<>( DATA_ALIASES_MODEL_ID, MccDataAliasM5Model.MODEL_ID ) {

          @SuppressWarnings( "nls" )
          @Override
          protected void doInit() {
            setNameAndDescription( "data aliases", "data aliases" );
          }

          @Override
          protected IList<IDataNameAlias> doGetFieldValue( Skid aEntity ) {
            IOptionSet values = prefsSection.getOptions( aEntity );
            IDataNameAliasesList list = (IDataNameAliasesList)values.getValue( optDef.id() ).asValobj();
            return list.items();
          }

        };

    return fDef;
  }

}
