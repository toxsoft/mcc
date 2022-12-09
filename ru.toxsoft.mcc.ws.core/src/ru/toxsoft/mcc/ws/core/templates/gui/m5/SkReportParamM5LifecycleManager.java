package ru.toxsoft.mcc.ws.core.templates.gui.m5;

import org.toxsoft.core.tsgui.chart.api.*;
import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.model.*;
import org.toxsoft.core.tsgui.m5.model.impl.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.helpers.*;
import org.toxsoft.core.tslib.gw.gwid.*;
import org.toxsoft.uskat.core.connection.*;

import ru.toxsoft.mcc.ws.core.templates.api.*;
import ru.toxsoft.mcc.ws.core.templates.api.impl.*;

/**
 * @author dima
 */
class SkReportParamM5LifecycleManager
    extends M5LifecycleManager<ISkReportParam, ISkConnection> {

  public SkReportParamM5LifecycleManager( IM5Model<ISkReportParam> aModel, ISkConnection aMaster ) {
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
  protected ValidationResult doBeforeCreate( IM5Bunch<ISkReportParam> aValues ) {
    return ValidationResult.SUCCESS;
  }

  /**
   * If creation is supported subclass must create the entity instance.
   * <p>
   * In base class throws an exception, never call superclass method when overriding.
   *
   * @param aValues {@link IM5Bunch} - field values, never is <code>null</code>
   * @return &lt;ISkReportParam&gt; - created instance
   */
  @Override
  protected ISkReportParam doCreate( IM5Bunch<ISkReportParam> aValues ) {
    Gwid gwid = aValues.getAsAv( SkReportParamM5Model.FID_GWID ).asValobj();
    String title = aValues.getAsAv( SkReportParamM5Model.FID_TITLE ).asString();
    String descr = aValues.getAsAv( SkReportParamM5Model.FID_DESCR ).asString();
    EAggregationFunc func = aValues.getAsAv( SkReportParamM5Model.FID_AGGR_FUNC ).asValobj();
    EDisplayFormat format = aValues.getAsAv( SkReportParamM5Model.FID_DISPL_FORMAT ).asValobj();

    return new SkReportParam( gwid, title, descr, func, format );
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
  protected ValidationResult doBeforeEdit( IM5Bunch<ISkReportParam> aValues ) {
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
   * @return &lt;ISkReportParam&gt; - created instance
   */
  @Override
  protected ISkReportParam doEdit( IM5Bunch<ISkReportParam> aValues ) {
    Gwid gwid = aValues.getAsAv( SkReportParamM5Model.FID_GWID ).asValobj();
    String title = aValues.getAsAv( SkReportParamM5Model.FID_TITLE ).asString();
    String descr = aValues.getAsAv( SkReportParamM5Model.FID_DESCR ).asString();
    EAggregationFunc func = aValues.getAsAv( SkReportParamM5Model.FID_AGGR_FUNC ).asValobj();
    EDisplayFormat format = aValues.getAsAv( SkReportParamM5Model.FID_DISPL_FORMAT ).asValobj();

    return new SkReportParam( gwid, title, descr, func, format );
  }

  /**
   * Subclass may perform validation before remove existing entity.
   * <p>
   * In base class returns {@link ValidationResult#SUCCESS}, there is no need to call superclass method when overriding.
   *
   * @param aEntity &lt;ISkReportParam&gt; - the entity to be removed, never is <code>null</code>
   * @return {@link ValidationResult} - the validation result
   */
  @Override
  protected ValidationResult doBeforeRemove( ISkReportParam aEntity ) {
    return ValidationResult.SUCCESS;
  }

  /**
   * If removing is supported subclass must remove the existing entity.
   * <p>
   * In base class throws an exception, never call superclass method when overriding.
   *
   * @param aEntity &lt;ISkReportParam&gt; - the entity to be removed, never is <code>null</code>
   */
  @Override
  protected void doRemove( ISkReportParam aEntity ) {
    // TODO
  }

  /**
   * If enumeration is supported subclass must return list of entities.
   * <p>
   * In base class returns {@link IList#EMPTY}, there is no need to call superclass method when overriding.
   *
   * @return {@link IList}&lt;ISkReportParam&gt; - list of entities in the scope of maetr object
   */
  @Override
  protected IList<ISkReportParam> doListEntities() {
    return IList.EMPTY;
  }

  /**
   * If enumeration is supported subclass may allow items reordering.
   * <p>
   * In base class returns <code>null</code>, there is no need to call superclass method when overriding.
   * <p>
   * This method is called every time when user asks for {@link IM5ItemsProvider#reorderer()}.
   *
   * @return {@link IListReorderer}&lt;ISkReportParam&gt; - optional {@link IM5ItemsProvider#listItems()} reordering
   *         means
   */
  @Override
  protected IListReorderer<ISkReportParam> doGetItemsReorderer() {
    return null;
  }

}
