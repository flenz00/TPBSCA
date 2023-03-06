import java.io.OutputStream;
import java.io.InputStream;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.io.IOException;
import java.net.InetAddress;
import java.util.concurrent.Executors;
import java.net.ServerSocket;

@SuppressWarnings({"deprecation","removal"})
public class Servlet
{
    public static  String WEB_ROOT = System.getProperty("user.dir");
    private static  String SHUTDOWN_COMMAND = "/SHUTDOWN";
    private boolean shutdown;
    private ServerSocket serverSocket;
    
    Servlet() {
        this.shutdown = false;
    }
    
    public void await() {
         ExecutorService pool = Executors.newFixedThreadPool(2);
        this.serverSocket = null;
         int port = 7654;
        int request_number = 0;
        
        try {
            
            this.serverSocket = new ServerSocket(port, 1, InetAddress.getByName("127.0.0.1"));
        }
        catch (IOException e) {
            e.getMessage();
            System.exit(1);
        }
        while (!this.shutdown) {
            Socket socket = null;
            
        try {
                socket = this.serverSocket.accept();
                if (Shutdown.flag) {
                    pool.shutdownNow();
                    return;
                }
                 MyThread T = new MyThread(this, socket,request_number);
                pool.execute(T);
            }
            catch (Exception ex) {}
            request_number++;
        }
        pool.shutdownNow();
    }
    
    public void processRequest( Socket socket) throws IOException {
         InputStream input = socket.getInputStream();
         OutputStream output = socket.getOutputStream();
         Request request = new Request(input);
        request.parse();
         Response response = new Response(output);
        response.setRequest(request);
        if (request.getUri() != null) {
            
            if (request.getUri().equals("/SHUTDOWN")) {
                    MyStaticResourceProcessor processor = new MyStaticResourceProcessor();
                    processor.process(request, response);
                    this.shutdown = true;
                    try {
                        this.serverSocket.close();
                        System.exit(1);
                    }
                    catch (Exception e) {
                        System.out.println(e.getMessage());
                        return;
                    }
                }
                if (request.getUri().startsWith("/servlet")) {
                    MyServletProcessor processor2 = new MyServletProcessor();
                    processor2.process(request, response);
                    
                }
                else {
                    MyStaticResourceProcessor processor = new MyStaticResourceProcessor();
                    processor.process(request, response);
                }
            
            socket.close();
        }
    }
    
    public static String getWebRoot() {
        return ".";
    }
    
    public static String getShutdownCommand() {
        return "/SHUTDOWN";
    }
    
    public boolean isShutdown() {
        return this.shutdown;
    }
    
    public ServerSocket getServerSocket() {
        return this.serverSocket;
    }
    
    public void setShutdown( boolean shutdown) {
        this.shutdown = shutdown;
    }
    
    public static void main( String[] args) {
        ServletHashTable.initTable();
        try {
            Servlet myServlet = new Servlet();
            ManagementConsole managementConsole = new ManagementConsole(myServlet);
            managementConsole.start();
            myServlet.await();
            System.out.println("Exiting...");
            managementConsole.stop();
            System.out.println("Closing management console..");
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
