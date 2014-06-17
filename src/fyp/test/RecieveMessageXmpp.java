package fyp.test;


import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.PacketCollector;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.filter.AndFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;

public class RecieveMessageXmpp {
    private XMPPConnection xmppConnection;

    public void connect(String server, int port, String s) throws Exception {
        xmppConnection = new XMPPConnection(new ConnectionConfiguration(server, port,s));
        xmppConnection.connect();
    }

    public void disconnect(){
        if(xmppConnection != null){
            xmppConnection.disconnect();
            //interrupt();
        }
    }

    public void login(String username, String password) throws Exception{
        connect("jabber.at", 5222, "jabber.at");
        xmppConnection.login(username, password);
    }

    public String run(){
    	String str;
        
    	//RecieveMessageXmpp gtd = new RecieveMessageXmpp();
            try {
				login("aamirshakeel", "mubbi");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            System.out.println("Login successful");
           str=listeningForMessages();
           
           return str;
        
		
    }

   

    public String listeningForMessages() {
    	String x=null;
        PacketFilter filter = new AndFilter(new PacketTypeFilter(Message.class));
        PacketCollector collector = xmppConnection.createPacketCollector(filter);
        while (true) {
            Packet packet = collector.nextResult();
            if (packet instanceof Message) {
                Message message = (Message) packet;
                if (message != null && message.getBody() != null)
                    System.out.println("Received message from "
                            + packet.getFrom() + " : "
                            + (message != null ? message.getBody() : "NULL"));
                x=message.getBody().toString();
                			return x;
            }
        }
    }
    

}