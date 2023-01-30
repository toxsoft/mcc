package ru.toxsoft.mcc.ws.mnemos.app.rt.alarm;

import java.io.*;
import java.net.*;

import javax.sound.sampled.*;

import org.eclipse.core.runtime.*;
import org.osgi.framework.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.logs.impl.*;

import ru.toxsoft.mcc.ws.mnemos.*;

/**
 * Проигрыватель звуковых файлов.
 * <p>
 *
 * @author vs
 */
public class SoundPlayer
    implements ICloseable {

  private File file = null;
  private Clip clip = null;

  boolean stopped = false;

  AudioInputStream inputStream = null;

  /**
   * Конструктор.
   *
   * @param aFilePath String - путь к файлу (ресурсу)
   */
  public SoundPlayer( String aFilePath ) {
    try {
      String pluginId = Activator.PLUGIN_ID;
      Bundle bundle = Platform.getBundle( pluginId );
      URL url = FileLocator.find( bundle, new Path( aFilePath ), null );
      url = FileLocator.toFileURL( url );
      file = URIUtil.toFile( URIUtil.toURI( url ) );
      inputStream = AudioSystem.getAudioInputStream( file );
      init();
    }
    catch( Throwable ex ) {
      LoggerUtils.errorLogger().error( ex );
    }
    finally {
      try {
        if( inputStream != null ) {
          inputStream.close();
        }
      }
      catch( Throwable ex ) {
        LoggerUtils.errorLogger().error( ex );
      }
    }
  }

  /**
   * Начинает проигрывание звукового файла
   */
  public void start() {
    stopped = false;
    clip.setFramePosition( 0 );
    clip.start();
  }

  /**
   * Завершает проигрывание звукового файла
   */
  public void stop() {
    stopped = true;
    clip.stop();
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса {@link ICloseable}
  //

  @Override
  public void close() {
    stopped = true;
    try {
      inputStream.close();
    }
    catch( Throwable ex ) {
      LoggerUtils.errorLogger().error( ex );
    }
    clip.close();
  }

  // ------------------------------------------------------------------------------------
  // Внутренняя реализация
  //

  private void init() {
    try {
      clip = (Clip)AudioSystem.getLine( new Line.Info( Clip.class ) );
      clip.addLineListener( event -> {
        if( event.getType() == LineEvent.Type.STOP ) {
          if( !stopped ) {
            try {
              clip.setFramePosition( 0 );
              clip.start();
            }
            catch( Throwable ex ) {
              LoggerUtils.errorLogger().error( ex );
            }
          }
        }
      } );
      clip.open( inputStream );
    }
    catch( Throwable ex ) {
      LoggerUtils.errorLogger().error( ex );
    }
  }

}
