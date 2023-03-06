import java.io.IOException;

public class MyStaticResourceProcessor
{
    public void process( Request request,  Response response) {
        try {
            response.sendStaticResource();
        }
        catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}