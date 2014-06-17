package ebtihaj.soundRecorder;

import java.io.IOException;
import java.io.File;

import javax.sound.sampled.DataLine;
import javax.sound.sampled.TargetDataLine;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.AudioFileFormat;

public class SimpleAudioRecorder
extends Thread
{
    private TargetDataLine      m_line;
    private AudioFileFormat.Type    m_targetType;
    private AudioInputStream    m_audioInputStream;
    private File            m_outputFile;



    public SimpleAudioRecorder(TargetDataLine line,
                     AudioFileFormat.Type targetType,
                     File file)
    {
        m_line = line;
        m_audioInputStream = new AudioInputStream(line);
        m_targetType = targetType;
        m_outputFile = file;
    }
    public void start()
    {
        
			try {
			if(!m_line.isOpen())
				m_line.open();
				m_line.start();
				if(!this.isAlive())
					super.start();		
			
			} catch (LineUnavailableException e) {
				e.printStackTrace();
			}
    }
 public void stopRecording()
    {
        m_line.stop();
        m_line.close();
    }
 public void run()
    {
            try
            {
            	
            	while(true){
            		if(m_line.isOpen())
		                AudioSystem.write(
		                    m_audioInputStream,
		                    m_targetType,
		                    m_outputFile);
	                Thread.sleep(500);
            	}
                
            }
            catch (IOException e)
            {
                e.printStackTrace();
            } catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    }
 public static SimpleAudioRecorder getInstance(){
    	return getInstance("default.wav");
    }
  public static SimpleAudioRecorder getInstance(String fileName)
    {
        File    outputFile = new File(fileName);
        AudioFormat audioFormat = new AudioFormat(
            AudioFormat.Encoding.PCM_SIGNED,
            5000.0F, 16, 2, 4, 1000.0F, false);
        DataLine.Info   info = new DataLine.Info(TargetDataLine.class, audioFormat);
        TargetDataLine  targetDataLine = null;
        try
        {
            targetDataLine = (TargetDataLine) AudioSystem.getLine(info);
            targetDataLine.open(audioFormat);
        }
        catch (LineUnavailableException e)
        {
            out("unable to get a recording line");
            e.printStackTrace();
            System.exit(1);
        }
        AudioFileFormat.Type    targetType = AudioFileFormat.Type.WAVE;
        SimpleAudioRecorder recorder = new SimpleAudioRecorder(
            targetDataLine,
            targetType,
            outputFile);        
        return recorder;
    }
   private static void out(String strMessage)
    {
        System.out.println(strMessage);
    }
}

