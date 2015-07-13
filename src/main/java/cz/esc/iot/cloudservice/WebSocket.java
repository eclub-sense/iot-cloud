package cz.esc.iot.cloudservice;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PipedOutputStream;
import java.io.PrintStream;
import java.io.RandomAccessFile;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.filechooser.FileSystemView;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.WebSocketAdapter;

public class WebSocket extends WebSocketAdapter
{
    @Override
    public void onWebSocketConnect(Session sess)
    {
        super.onWebSocketConnect(sess);
        System.out.println("Socket Connected: " + sess);
    }
    
    @Override
    public void onWebSocketBinary(byte[] payload, int offset, int len)
    {
        super.onWebSocketBinary(payload, offset, len);
        System.out.print("Received binary message: ");
        for (int i = 0; i < len; i++) {
        	System.out.printf("%X", payload[i]);
        }
        System.out.print('\n');
    }
    
    @Override
    public void onWebSocketText(String message)
    {
        super.onWebSocketText(message);
        System.out.println("Received TEXT message: " + message);
        messageTunnel(message);
    }
    
    @Override
    public void onWebSocketClose(int statusCode, String reason)
    {
        super.onWebSocketClose(statusCode,reason);
        System.out.println("Socket Closed: [" + statusCode + "] " + reason);
    }
    
    @Override
    public void onWebSocketError(Throwable cause)
    {
        super.onWebSocketError(cause);
        cause.printStackTrace(System.err);
    }
    
    private void messageTunnel(String msg) {
    	try {
    	    BufferedWriter pipe = new BufferedWriter(new FileWriter("pipe.fifo"));
    	    pipe.write(msg);
    	    pipe.close();
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    }
}
