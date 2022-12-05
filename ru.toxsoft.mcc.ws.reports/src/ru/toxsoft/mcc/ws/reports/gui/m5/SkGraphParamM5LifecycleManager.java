package ru.toxsoft.mcc.ws.reports.gui.m5;

import org.toxsoft.core.tsgui.chart.api.*;
import org.toxsoft.core.tsgui.graphics.colors.*;
import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.model.*;
import org.toxsoft.core.tsgui.m5.model.impl.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.helpers.*;
import org.toxsoft.core.tslib.gw.gwid.*;
import org.toxsoft.uskat.core.connection.*;

import ru.toxsoft.mcc.ws.reports.lib.*;
import ru.toxsoft.mcc.ws.reports.lib.impl.*;

/**
 * @author dima
 */
class SkGraphParamM5LifecycleManager
    extends M5LifecycleManager<ISkGraphParam, ISkConnection> {

  public SkGraphParamM5LifecycleManager( IM5Model<ISkGraphParam> aModel, ISkConnection aMaster ) {
    super( aModel, true, true, true, false, aMaster );
  }

  /**
   * Subclass may perform validation before instance creation.
   * <p>
   * In base class returns {@link ValidationResult#SUCCESS}, there is no need to call superclass method when overriding.
   *
   * @param aValues {@link IM5Bunch} - field values, never is <code>null</code>
   * @return {@link ValidationResult} - the validation result
   */
  @Override
  protected ValidationResult doBeforeCreate( IM5Bunch<ISkGraphParam> aValues ) {
    return ValidationResult.SUCCESS;
  }

  /**
   * If creation is supported subclass must create the entity instance.
   * <p>
   * In base class throws an exception, never call superclass method when overriding.
   *
   * @param aValues {@link IM5Bunch} - field values, never is <code>null</code>
   * @return &lt;ISkGraphParam&gt; - created instance
   */
  @Override
  protected ISkGraphParam doCreate( IM5Bunch<ISkGraphParam> aValues ) {
    return bunch2SkGraphParam( aValues );
  }

  private static ISkGraphParam bunch2SkGraphParam( IM5Bunch<ISkGraphParam> aValues ) {
    Gwid gwid = aValues.getAsAv( SkGraphParamM5Model.FID_GWID ).asValobj();
    String title = aValues.getAsAv( SkGraphParamM5Model.FID_TITLE ).asString();
    String descr = aValues.getAsAv( SkGraphParamM5Model.FID_DESCR ).asString();
    String unitId = aValues.getAsAv( SkGraphParamM5Model.FID_UNIT_ID ).asString();
    String unitName = aValues.getAsAv( SkGraphParamM5Model.FID_UNIT_NAME ).asString();
    EAggregationFunc func = aValues.getAsAv( SkGraphParamM5Model.FID_AGGR_FUNC ).asValobj();
    EDisplayFormat format = aValues.getAsAv( SkGraphParamM5Model.FID_DISPL_FORMAT ).asValobj();
    ETsColor color = aValues.getAsAv( SkGraphParamM5Model.FID_COLOR ).asValobj();
    int lineWidth = aValues.getAsAv( SkGraphParamM5Model.FID_LINE_WIDTH ).asInt();
    boolean isLadder = aValues.getAsAv( SkGraphParamM5Model.FID_IS_LADDER ).asBool();

    return new SkGraphParam( gwid, title, descr, unitId, unitName, func, format, color, lineWidth, isLadder );
  }

  /**
   * Subclass may perform validation before existing editing.
   * <p>
   * In base class returns {@link ValidationResult#SUCCESS}, there is no need to call superclass method when overriding.
   *
   * @param aValues {@link IM5Bunch} - field values, never is <code>null</code>
   * @return {@link ValidationResult} - the validation result
   */
  @Override
  protected ValidationResult doBeforeEdit( IM5Bunch<ISkGraphParam> aValues ) {
    return ValidationResult.SUCCESS;
  }

  /**
   * If editing is supported subclass must edit the existing entity.
   * <p>
   * In base class throws an exception, never call superclass method when overriding.
   * <p>
   * The old values may be found in the {@link IM5Bunch#originalEntity()} which obviously is not <code>null</code>.
   *
   * @param aValues {@link IM5Bunch} - field values, never is <code>null</code>
   * @return &lt;ISkGraphParam&gt; - created instance
   */
  @Override
  protected ISkGraphParam doEdit( IM5Bunch<ISkGraphParam> aValues ) {
    return bunch2SkGraphParam( aValues );
  }

  /**
   * Subclass may perform validation before remove existing entity.
   * <p>
   * In base class returns {@link ValidationResult#SUCCESS}, there is no need to call superclass method when overriding.
   *
   * @param aEntity &lt;ISkGraphParam&gt; - the entity to be removed, never is <code>null</code>
   * @return {@link ValidationResult} - the validation result
   */
  @Override
  protected ValidationResult doBeforeRemove( ISkGraphParam aEntity ) {
    return ValidationResult.SUCCESS;
  }

  /**
   * If removing is supported subclass must remove the existing entity.
   * <p>
   * In base class throws an exception, never call superclass method when overriding.
   *
   * @param aEntity &lt;ISkGraphParam&gt; - the entity to be removed, never is <code>null</code>
   */
  @Override
  protected void doRemove( ISkGraphParam aEntity ) {
    // TODO
  }

  /**
   * If enumeration is supported subclass must return list of entities.
   * <p>
   * In base class returns {@link IList#EMPTY}, there is no need to call superclass method when overriding.
   *
   * @return {@link IList}&lt;ISkGraphParam&gt; - list of entities in the scope of maetr object
   */
  @Override
  protected IList<ISkGraphParam> doListEntities() {
    return IList.EMPTY;
  }

  /**
   * If enumeration is supported subclass may allow items reordering.
   * <p>
   * In base class returns <code>null</code>, there is no need to call superclass method when overriding.
   * <p>
   * This method is called every time when user asks for {@link IM5ItemsProvider#reorderer()}.
   *
   * @return {@link IListReorderer}&lt;ISkGraphParam&gt; - optional {@link IM5ItemsProvider#listItems()} reordering
   *         means
   */
  @Override
  protected IListReorderer<ISkGraphParam> doGetItemsReorderer() {
    return null;
  }

}
