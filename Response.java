import java.util.Collection;
import javax.servlet.http.Cookie;
import javax.servlet.ServletOutputStream;
import java.util.Locale;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.io.FileInputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.OutputStream;
import javax.servlet.http.HttpServletResponse;



public class Response implements HttpServletResponse
{
    Request request;
    OutputStream output;
    PrintWriter writer;
    
    public Response( OutputStream output) throws IOException {
        this.output = output;
        this.writer = this.getWriter();
    }
    
    public void setRequest( Request request) {
        this.request = request;
    }
    
    public void sendStaticResource() throws IOException {
        if (this.request.getUri().equals(Servlet.getShutdownCommand())) {
             String successMessage = "HTTP/1.1 200 OK\r\nContent-Type: text/html\r\nContent-Length: 25\r\n\r\n<h1>Shutting Down...</h1>";
            this.writer.print(successMessage);
            this.writer.flush();
        }
        else {
            FileInputStream fis = null;
            String filePath = "./staticcontentrepository" + this.request.getUri();
            try {
                 File file = new File(filePath);
                fis = new FileInputStream(file);
                 Scanner in = new Scanner(file);
                 StringBuilder sBuilder = new StringBuilder(50);
                while (in.hasNextLine()) {
                    sBuilder.append(in.nextLine());
                    sBuilder.append("\n");
                }
                in.close();
                 String successMessage2 = "HTTP/1.1 200 OK\r\nContent-Type: text/html\r\nContent-Length: " + sBuilder.length() + "\r\n" + "\r\n";
                this.writer.print(successMessage2);
                this.writer.flush();
                this.writer.print(sBuilder);
                this.writer.flush();
            }
            catch (FileNotFoundException e) {
                 int totalLength = filePath.length() + 30;
                 String errorMessage = "HTTP/1.1 404 File Not Found\r\nContent-Type: text/html\r\nContent-Length: " + totalLength + "\r\n" + "\r\n" + "<h1>File Not Found</h1>" + "<p>" + filePath + "</p>";
                this.writer.print(errorMessage);
                this.writer.flush();
                return;
            }
            finally {
                if (fis != null) {
                    fis.close();
                }
            }
            if (fis != null) {
                fis.close();
            }
        }
    }
    
    public void flushBuffer() throws IOException {
    }
    
    public int getBufferSize() {
        return 0;
    }
    
    public String getCharacterEncoding() {
        return null;
    }
    
    public Locale getLocale() {
        return null;
    }
    
    public ServletOutputStream getOutputStream() throws IOException {
        return null;
    }
    
    public PrintWriter getWriter() throws IOException {
        return this.writer = new PrintWriter(this.output, true);
    }
    
    public boolean isCommitted() {
        return false;
    }
    
    public void reset() {
    }
    
    public void resetBuffer() {
    }
    
    public void setBufferSize( int size) {
    }
    
    public void setContentLength( int length) {
    }
    
    public void setContentType( String type) {
    }
    
    public void setLocale( Locale locale) {
    }
    
    public String getContentType() {
        return null;
    }
    
    public void setCharacterEncoding( String arg0) {
    }
    
    public void setContentLengthLong( long arg0) {
    }
    
    public void addCookie( Cookie cookie) {
    }
    
    public boolean containsHeader( String name) {
        return false;
    }
    
    public String encodeURL( String url) {
        return null;
    }
    
    public String encodeRedirectURL( String url) {
        return null;
    }
    
    public String encodeUrl( String url) {
        return null;
    }
    
    public String encodeRedirectUrl( String url) {
        return null;
    }
    
    public void sendError( int sc,  String msg) throws IOException {
    }
    
    public void sendError( int sc) throws IOException {
    }
    
    public void sendRedirect( String location) throws IOException {
    }
    
    public void setDateHeader( String name,  long date) {
    }
    
    public void addDateHeader( String name,  long date) {
    }
    
    public void setHeader( String name,  String value) {
    }
    
    public void addHeader( String name,  String value) {
    }
    
    public void setIntHeader( String name,  int value) {
    }
    
    public void addIntHeader( String name,  int value) {
    }
    
    public void setStatus( int sc) {
    }
    
    public void setStatus( int sc,  String sm) {
    }
    
    public int getStatus() {
        return 0;
    }
    
    public String getHeader( String name) {
        return null;
    }
    
    public Collection<String> getHeaders( String name) {
        return null;
    }
    
    public Collection<String> getHeaderNames() {
        return null;
    }
}