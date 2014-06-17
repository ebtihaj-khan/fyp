package upload;
import fyp.test.GSMConnect;
import it.sauronsoftware.ftp4j.FTPDataTransferListener;

public class MyTransferListener implements FTPDataTransferListener {
	 static String _comPort = "COM7";
	@Override
	public void aborted() {
		System.out.println("aborted");
		
	}

	@Override
	public void completed() {
		// TODO Auto-generated method stub
		System.out.println("uploaded");
		 GSMConnect gsmReuse = new GSMConnect(_comPort);
	      gsmReuse.sendxmpp("ftp://androidcheck.netai.net/Zahid/default.wav");
	}

	@Override
	public void failed() {
		System.out.println("failed");
		
	}

	@Override
	public void started() {
		System.out.println("started");
		
	}

	@Override
	public void transferred(int arg0) {
		// TODO Auto-generated method stub
		
	}
	 
      
        }
