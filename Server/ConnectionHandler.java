package Server;

import Messages.Message;
import Messages.MessageHandler;

import javax.net.ssl.SSLSocket;
import java.io.*;
import java.math.BigInteger;

import static Utilities.Constants.*;

/**
 * Handles new SSL Connections to the server
 */
public class ConnectionHandler implements Runnable {

    private SSLSocket sslSocket;
    private BufferedReader in;
    private ObjectInputStream serverInputStream;
    private ObjectOutputStream serverOutputStream;
    private Server server;

    /**
     * Handles new SSL Connections to the server
     * @param socket
     * @param server
     */
    public ConnectionHandler(SSLSocket socket, Server server) {
        this.sslSocket = socket;
        this.server = server;
        try {
            in = new BufferedReader(new InputStreamReader(sslSocket.getInputStream()));
            serverOutputStream = new ObjectOutputStream(sslSocket.getOutputStream());
            serverInputStream = new ObjectInputStream(sslSocket.getInputStream());
        } catch (IOException e) {
            System.out.println("Error creating buffered reader...");
            e.printStackTrace();
        }
    }

    /**
     * Sends a message through a ssl socket
     */
    public void sendMessage(Message message) {
        try {
            serverOutputStream.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Analyses Responses
     * @param response
     */
    public Message analyseResponse(Message response) {

        String[] body = response.getBody().split(" ");

        System.out.println(response.getMessageType());

        switch (response.getMessageType()) {
            case SIGNIN:
                return server.loginUser(body[0], body[1]);
            case SIGNUP:
                return server.addUser(body[0],body[1]);
            case CREATE_CHAT:
                return server.createChat(response.getBody());
            case NEWNODE:
                Node n = server.predecessorLookUp(Integer.parseInt(body[0]));
                if(n.getNodeId() < Integer.parseInt(body[0])){
                    MessageHandler handler = new MessageHandler(new Message(PREDECESSOR, BigInteger.valueOf(server.getNodeId()),body[0],body[1],body[2]), body[1], body[2], server);
                    handler.sendMessage();
                    handler.closeSocket();
                }else{
                    MessageHandler handler = new MessageHandler(new Message(NEWNODE,BigInteger.valueOf(server.getNodeId()),body[0],body[1],body[2]), body[1], body[2], server);
                    handler.sendMessage();
                    handler.closeSocket();
                }
                try {
                    sslSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            default:
                break;
        }
        return null;
    }


    /**
     * Reads Messages
     */
    public void run() {
        Message message = null;
        try {
            message = (Message) serverInputStream.readObject();
            Message responseMessage = analyseResponse(message);
            sendMessage(responseMessage);
        } catch (IOException e) {
            System.out.println("Error reading message...");
        } catch (ClassNotFoundException e) {
            System.out.println("Error reading message...");
        }

    }
}