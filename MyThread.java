import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;




public class MyThread extends Thread
{
    private Socket localThreadSocket;
    private Servlet servlet;
    private int request_number;
    
    MyThread(Servlet serv,  Socket sock, int request_number) {
        this.localThreadSocket = sock;
        this.servlet = serv;
        this.request_number = request_number;
    }
    
    @Override
    public void run() {
        try {
            
            this.servlet.processRequest(this.localThreadSocket);
            
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        finally{
            
            try {
                
                if(!Shutdown.flag){
                    File file = new File("./handler.txt");
                    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss dd/MM/yyyy");
                    LocalDateTime now = LocalDateTime.now();

                    if(!file.exists()){
                        file.createNewFile();
                        
                        BufferedWriter bw = new BufferedWriter(new FileWriter(file.getAbsolutePath()));
                        bw.write(dtf.format(now)+"\n");
                        bw.write(Thread.currentThread().getName()+" handling request n."+request_number+"\n");
                        bw.close();
                        
                    }
                    else
                    {
                        BufferedWriter out = new BufferedWriter(new FileWriter("./handler.txt",true));
                        out.write("--------------------------"+"\n"+dtf.format(now)+"\n");
                        out.write(Thread.currentThread().getName()+" handling request n."+request_number+"\n");
                        out.close();
                    }
                }
                else
                this.servlet.getServerSocket().close();    
            }
        catch (IOException e) {
            e.getMessage();
        }
        }
    }
}
