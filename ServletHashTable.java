import javax.servlet.http.HttpServlet;
import java.util.Hashtable;

public class ServletHashTable
{
    static Hashtable<String, HttpServlet> ht;
    
    ServletHashTable() {
    }
    
    static void initTable() {
        ServletHashTable.ht = new Hashtable<String, HttpServlet>();
    }
    
    static void put( String s,  HttpServlet h) {
        ServletHashTable.ht.put(s, h);
    }
    
    static boolean contains( String s) {
        return ServletHashTable.ht.containsKey(s);
    }
    
    static HttpServlet get( String s) {
        return ServletHashTable.ht.get(s);
    }
    
    static void remove( String s) {
        ServletHashTable.ht.remove(s);
    }
}
