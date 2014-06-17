package upload;
import java.io.*;
import it.sauronsoftware.ftp4j.FTPClient;
import it.sauronsoftware.ftp4j.FTPDataTransferListener;
public class uploadTry {

	/*********  work only for Dedicated IP ***********/
    static final String FTP_HOST= "31.170.160.103";
     
    /*********  FTP USERNAME ***********/
    static final String FTP_USER = "a2543237";
     
    /*********  FTP PASSWORD ***********/
    static final String FTP_PASS  ="khan0545960062";
	public static void main(String[] args) {
		
		 File f = new File("/users/Ebtihaj Khan/workspace/FYP/default.wav");
		 uploadFile(f);
	}
 public static void uploadFile(File fileName){
         
         
         FTPClient client = new FTPClient();
         
        try {
        	
            try {
        	client.connect(FTP_HOST,21);
            }catch(Exception e){
            	System.out.println(e);
            	
            }
            client.login(FTP_USER, FTP_PASS);
            client.setType(FTPClient.TYPE_AUTO);
            
            //client.createDirectory("Zahid");
            client.changeDirectory("/Zahid/");
             String s = client.serverStatus().toString();
             
            client.upload(fileName, new MyTransferListener());
             
        } catch (Exception e) {
        	System.out.println(e);
            e.printStackTrace();
            try {
                client.disconnect(true);    
            } catch (Exception e2) {
            	System.out.println(e2);
                e2.printStackTrace();
            }}}}
//ftp://androidcheck.netai.net/Zahid/Badtameez.mp3
