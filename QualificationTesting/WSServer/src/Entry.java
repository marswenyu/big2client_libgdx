import com.game.server.example.ChatServer;
import javaConsole.JavaConsole;
import org.java_websocket.WebSocket;
import org.java_websocket.WebSocketImpl;
import org.java_websocket.handshake.ClientHandshake;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.UnknownHostException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by matt1201 on 2016/12/20.
 */
public class Entry extends ChatServer {
    private static JavaConsole m_console = new JavaConsole();
    private static AtomicBoolean m_running = new AtomicBoolean(true);

    static {
        m_console.setBackground(Color.BLACK);
        m_console.setForeground(Color.WHITE);
        m_console.setFont(new Font ("Ariel", Font.BOLD, 12));
        m_console.getFrame().setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        //Customize the console frame
        m_console.setTitle("Game Server");
        m_console.addWindowEventCatcher(new WindowListener() {
            @Override
            public void windowClosed(WindowEvent e) {}

            @Override
            public void windowOpened(WindowEvent e) {}

            @Override
            public void windowClosing(WindowEvent e) {
                m_running.set(false);
                System.exit(0);
            }

            @Override
            public void windowIconified(WindowEvent e) {}

            @Override
            public void windowDeiconified(WindowEvent e) {}

            @Override
            public void windowActivated(WindowEvent e) {}

            @Override
            public void windowDeactivated(WindowEvent e) {}
        });
    }

    private Thread updateProcess = null;
    private AtomicInteger ccu = new AtomicInteger(0);

    public static void main( String[] args ) throws InterruptedException , IOException {
        WebSocketImpl.DEBUG = true;
        int port = 8887; // 843 flash policy port
        try {
            port = Integer.parseInt( args[ 0 ] );
        } catch ( Exception ex ) {
        }
        Entry s = new Entry( port );
        s.start();
        System.out.println( "ChatServer started on port: " + s.getPort() );

        BufferedReader sysin = new BufferedReader( new InputStreamReader( System.in ) );
        while ( true ) {
            String in = sysin.readLine();
            s.sendToAll( in );
            if( in.equals( "exit" ) ) {
                s.stop();
                break;
            } else if( in.equals( "restart" ) ) {
                s.stop();
                s.start();
                break;
            }
        }
    }

    public Entry(int port) throws UnknownHostException {
        super(port);

        updateProcess = new Thread(new Runnable() {
            @Override
            public void run() {
                while (m_running.get()) {
                    update();

                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        updateProcess.start();
    }

    private void update(){
        String output = String.format("CCU : %d", ccu.get());
        m_console.setText(output);
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        super.onOpen(conn, handshake);

        ccu.addAndGet(1);
    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        super.onMessage(conn, message);
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        super.onClose(conn, code, reason, remote);

        ccu.addAndGet(-1);
    }
}
