package ru.toxsoft.mcc.ws.mnemos.app.rt;

import org.toxsoft.core.tslib.bricks.strid.coll.*;

/**
 * Поставщик данных реального времени.
 * <p>
 *
 * @author vs
 */
public interface IRtDataProvider {

  /**
   * Начинает мониторинг РВ данных и поставку их зарегистрированным потребителям.
   */
  void start();

  // /**
  // * Останавливает мониторинг РВ данных и поставку их зарегистрированным потребителям.
  // */
  // void stop();

  /**
   * Добавляет "потребителя" РВ-данных.
   *
   * @param aConsumer IRtDataConsumer - "потребитель" РВ-данных
   */
  void addDataConsumer( IRtDataConsumer aConsumer );

  /**
   * Удаляет из списка зарегистрированных "потребителя" РВ-данных.
   *
   * @param aConsumerId String - ИД "потребителя" РВ-данных
   * @return IRtDataConsumer - "потребитель" или null, в случае его отстутствия
   */
  IRtDataConsumer removeDataConsumer( String aConsumerId );

  /**
   * Возвращает список зарегистрированных "потребителей".<br>
   *
   * @return IStridablesList&lt;IRtDataConsumer> - список зарегистрированных "потребителей"
   */
  IStridablesList<IRtDataConsumer> listConsumers();

  /**
   * Освобождает ресурсы
   */
  void dispose();
}
