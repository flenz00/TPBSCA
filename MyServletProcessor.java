import javax.servlet.http.HttpServlet;
import javax.servlet.ServletResponse;
import javax.servlet.ServletRequest;

public class MyServletProcessor
{
    public void process( Request request,  Response response) {
         String uri = request.getUri();
         String servletName = uri.substring(uri.lastIndexOf("/") + 1);
        if (ServletHashTable.contains(servletName)) {
             HttpServlet servlet = ServletHashTable.get(servletName);
            try {
                servlet.service((ServletRequest)request, (ServletResponse)response);
            }
            catch (Exception e) {
                System.out.println(e.getMessage());
            }
            catch (Throwable e2) {
                System.out.println(e2.getMessage());
            }
        }
    }
}

