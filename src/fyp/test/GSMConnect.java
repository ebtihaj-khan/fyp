package fyp.test;
 
import java.io.*;
import upload.*;
import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.util.*;
import javax.comm.*;
import javax.swing.*;
import org.jivesoftware.smack.ConnectionConfiguration;
//import org.jivesoftware.smack.PacketCollector;
import org.jivesoftware.smack.XMPPConnection;
//import org.jivesoftware.smack.filter.AndFilter;
//import org.jivesoftware.smack.filter.PacketFilter;
//import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.Message;
//import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Presence;

import ebtihaj.soundRecorder.Recorder;

public class GSMConnect implements SerialPortEventListener, 
CommPortOwnershipListener {
  static String _comPort = "COM7";
  private String comPort = "COM7"; // This COM Port must be connect with GSM Modem or your mobile phone
  private String messageString = "";
  private CommPortIdentifier portId = null;
  private Enumeration portList;
  private InputStream inputStream = null;
  private OutputStream outputStream = null;
  private SerialPort serialPort;
  String eol = System.getProperty("line.separator");


  public static void main(String args[]) {
	  
	  GSMConnect gsm = new GSMConnect(_comPort); 
	  fyp.test.RecieveMessageXmpp RMxmpp= new fyp.test.RecieveMessageXmpp();
	  if (gsm.init()) {
        try {     	  
		       gsm.connect();
		      gsm.receivecall();
		      
		   // gsm.receiveMessageGSM();
		      // gsm.serialPort.close();
		       //gsm.delMessage();
//		       { String str=RMxmpp.run();
//		       gsm.sendMessage("+923329941575",str);}
		       //gsm.dial("+923329941575");
		      }
           catch (Exception e) 
           {
              e.printStackTrace();
            }
	  		} 
     else {
        System.out.println("Can't init this card");
     }
  }
  public GSMConnect(String comm) 
  {
	  this.comPort = comm;
	  }
  public boolean init() {
     portList = CommPortIdentifier.getPortIdentifiers();
     while (portList.hasMoreElements()) {
    	 			portId = (CommPortIdentifier) portList.nextElement();
    	 				if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
    	 					if (portId.getName().equals(comPort)) {
    	 				return true;
    	 								}
    	 							}
     						}
     return false;
  }
  public void dial(String phoneNumber) {
     try {
     //dial to this phone number
        messageString = "ATD" + phoneNumber + ";\n\r";
        outputStream.write(messageString.getBytes());
     } 
        catch (IOException e) {
           e.printStackTrace();
        }
  }
  public void send(String cmd) {
     try {
        outputStream.write(cmd.getBytes());
     } 
        catch (IOException e) {
           e.printStackTrace();
        }
  }
  public void connect() throws NullPointerException {
     if (portId != null) {
        try {
           portId.addPortOwnershipListener(this);
           serialPort = (SerialPort) portId.open("HSDPA", 2000);
        } 
           catch (PortInUseException e) {
              e.printStackTrace();
           }

        try {
           inputStream = serialPort.getInputStream();
           outputStream = serialPort.getOutputStream();
        } 
           catch (IOException e) {
              e.printStackTrace();
           }

        try {
        /** These are the events we want to know about*/
           serialPort.addEventListener(this);
           serialPort.notifyOnDataAvailable(true);
        } 
           catch (TooManyListenersException e) {
              e.printStackTrace();
           }

     //Register to home network of sim card

        send("ATZ\r\n");

     } 
     else {
        throw new NullPointerException("COM Port not found!!");
     }
  }
  public void serialEvent(javax.comm.SerialPortEvent serialPortEvent) {
     switch (serialPortEvent.getEventType()) {
        case SerialPortEvent.BI:
        case SerialPortEvent.OE:
        case SerialPortEvent.FE:
        case SerialPortEvent.PE:
        case SerialPortEvent.CD:
        case SerialPortEvent.CTS:
        case SerialPortEvent.DSR:
        case SerialPortEvent.RI:
        case SerialPortEvent.OUTPUT_BUFFER_EMPTY:
        case SerialPortEvent.DATA_AVAILABLE:

           byte[] readBuffer = new byte[2048];
           try {
           // 0) 
              while (inputStream.available()> 0) 
              {
                 int numBytes = inputStream.read(readBuffer);
              }
           //print response message
              System.out.print(new String(readBuffer));
           } 
              catch (IOException e) {
              }
           break;
     }
  }
  public void ownershipChange(int type) {
     switch (type) {
        case CommPortOwnershipListener.PORT_UNOWNED:
           System.out.println(portId.getName() + ": PORT_UNOWNED");
           break;
        case CommPortOwnershipListener.PORT_OWNED:
           System.out.println(portId.getName() + ": PORT_OWNED");
           break;
        case CommPortOwnershipListener.PORT_OWNERSHIP_REQUESTED:
        	System.out.println(portId.getName() + ": PORT_INUSED");
           break;
     }

  }
 
  public void delMessage() throws InterruptedException {
	    send("AT+CMGD=1,4");
	    Thread.sleep(2000);
	   }
  public void receiveMessageGSM() throws InterruptedException {
	  send("AT+CMGF=1\r\n");
	  Thread.sleep(2000);	 
	  send("AT+CMGL=\"REC UNREAD\"" + "\r");
	  String line = "";
    String fileName = "temp.txt";
      String temp=null;
      int i=0;
	 if(SerialPortEvent.DATA_AVAILABLE==1)
	  {
		  BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
          
          
          try {
			while( (line = reader.readLine()) != null){
			  try {
				  temp+=line;
				  line=line.replace("REC UNREAD","").replace("null+CMGL: ","").replace(",","").replace("OK","")
				  .replace("\"", eol).replace("+CMGL:", "");
				  GSMConnect gsmReuse = new GSMConnect(_comPort);
			      gsmReuse.sendxmpp(line);
				  
				  }
			      
				  
			  catch(Exception ex) {
			      System.out.println("Error writing to file '"+ fileName + "'");
			  } 			  
			  
	      } 		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
	  
	  	
	  return;
  }
  public  void sendxmpp(String temp)
  {
      ConnectionConfiguration connConfig = new ConnectionConfiguration("jabber.at", 5222, "jabber.at");
         XMPPConnection connection = new XMPPConnection(connConfig);

          try {
              connection.connect();
              System.out.println("Connected to " + connection.getHost());
          } catch (XMPPException ex) {
              System.out.println("Failed to connect to " + connection.getHost());
              System.exit(1);
          }
          try {
              connection.login("aamirshakeel", "mubbi");
              System.out.println("Logged in as " + connection.getUser());

              Presence presence = new Presence(Presence.Type.available);
              connection.sendPacket(presence);

          } catch (XMPPException ex) {
              System.out.println("Failed to log in as " + connection.getUser());
              System.exit(1);
          }

          ChatManager chatmanager = connection.getChatManager();
          Chat newChat = chatmanager.createChat("ebtihaj@jabber.at", new MessageListener() {
          public void processMessage(Chat chat, Message message)
          {
              System.out.println("Received message: " + message);
          }
      });

      try {
      	
          newChat.sendMessage(temp);
          System.out.println("Message Sent...");
      }
      catch (XMPPException e) {
          System.out.println("Error Delivering block");
      }
  }
  public void sendMessage(String phoneNumber, String message) throws InterruptedException {
	    char qu=34;
	    char cz=26;
	    send("AT+CMGF=1\r\n");
	    Thread.sleep(2000);
	    send("AT+CMGS=" + qu + phoneNumber + qu + ",145\r\n");
	    send(message + cz + "\r");
	  }
  private void receivecall() throws AWTException, IOException {
	  int i=1;
	 
			Robot robot = new Robot();      
       Recorder rec=new Recorder();
	String output=null;
	  send("Cring\r");
      if(SerialPortEvent.DATA_AVAILABLE==1)
	  {
		  BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
		  
			 try {
      
			while( (output=reader.readLine()) != null){
			  try {
				  System.out.println(output);
				  if(output.contains("NO DIALTONE"))
						  {rec.recorder();
						uploadTry upload= new uploadTry();
						upload.main(null);
						  }
				  }
			  catch(Exception ex) {
			      System.out.println("Error writing to file ''");
			  }
			  if(i==1)
			  {
				  robot.mouseMove(1215, 690);
					// robot.mousePress(InputEvent.BUTTON1_MASK);
				      // robot.mouseRelease(InputEvent.BUTTON1_MASK);
			rec.recorder();i++;
			  }
			} 		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
	}}