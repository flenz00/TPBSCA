import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Scanner;
import java.lang.annotation.Annotation;

import javax.lang.model.util.ElementScanner6;
import javax.servlet.http.HttpServlet;
import java.net.URLClassLoader;
import java.net.URL;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.File;
import java.io.FileInputStream;



public class ManagementConsole extends Thread
{
    private Servlet serv;
    
    ManagementConsole(Servlet serv) {
        this.serv = serv;
    }
    
    String firstWord( String command) {
        if (command.contains(" ")) {
             int index = command.indexOf(" ");
            return command.substring(0, index);
        }
        return command;
    }
    
    String secondWord( String command) {
        if (command.contains(" ")) {
             int index = command.indexOf(" ");
            return command.substring(index + 1, command.length());
        }
        return null;
    }
    
    void executeUnload(String servletInternalName) {
        if (!ServletHashTable.contains(servletInternalName)) {
            System.out.println("Servlet " + servletInternalName + " not in the servlet repository");
        }
        else {
            ServletHashTable.remove(servletInternalName);
            System.out.println("Servlet " + servletInternalName + " removed");
        }
    }
    
    void executeLoad(String servletInternalName) {
        if (ServletHashTable.contains(servletInternalName)) {
            System.out.println("Servlet " + servletInternalName + " already in the servlet repository");
        }
        else {
            String servletClassName = null;
             String servletRepository = new String("./servletrepository");
             String servletDir = new String(String.valueOf(servletRepository) + "/" + servletInternalName);
             File f = new File(servletDir);
            if (!f.exists() || !f.isDirectory()) {
                System.out.println("Directory " + servletDir + " does not exists");
                return;
            }
            try {
                 String metadataFile = String.valueOf(servletDir) + File.separator + "metadata.txt";
                 BufferedReader reader = new BufferedReader(new FileReader(metadataFile));
                for (String line = reader.readLine(); line != null; line = reader.readLine()) {
                    if (line.contains("=")) {
                         int index = line.indexOf("=");
                        servletClassName = line.substring(index + 1, line.length());
                    }
                }
                reader.close();
            }
            catch (FileNotFoundException fe) {
                System.out.println("File not found");
                return;
            }
            catch (IOException e) {
                System.out.println("Error");
                System.out.println(e.getMessage());
                return;
            }
            URLClassLoader loader = null;
            try {
                 URL[] urls = { new URL("file:" + servletDir + File.separator + "class" + File.separator) };
                loader = new URLClassLoader(urls);
            }
            catch (IOException e2) {
                System.out.println("Error");
                System.out.println(e2.getMessage());
                return;
            }
            Class myClass = null;
            try {
                myClass = loader.loadClass(servletClassName);
            }
            catch (ClassNotFoundException e3) {
                System.out.println("Class not found:" + servletClassName);
                return;
            }
            HttpServlet servlet = null;
            try {
                servlet = (HttpServlet)myClass.newInstance();
            }
            catch (Exception e4) {
                System.out.println("Error");
                System.out.println(e4.getMessage());
                return;
            }
            ServletHashTable.put(servletInternalName, servlet);
            System.out.println("Servlet " + servletInternalName + " added");
            try {
                loader.close();
            }
            catch (IOException e5) {
                System.out.println("Error");
                System.out.println(e5.getMessage());
            }
        }
    }
    
    void executeLoadWithAnnotations(String servletInternalName) {
        if (ServletHashTable.contains(servletInternalName)) {
            System.out.println("Servlet " + servletInternalName + " already in the servlet repository");
        }
        else {
             String annotatedClassDir = new String("./servletrepository/class");
             File folder = new File(annotatedClassDir);
             File[] listOfFiles = folder.listFiles();
            Class myClass = null;
            boolean found = false;
            for (int i = 0; i < listOfFiles.length; ++i) {
                if (listOfFiles[i].getName().endsWith(".class")) {
                    URLClassLoader loader = null;
                    
                    try {
                         URL[] urls = { new URL("file:" + annotatedClassDir + File.separator) };
                        loader = new URLClassLoader(urls);
                    }
                    catch (IOException e) {
                        System.out.println("Error");
                        e.getMessage();
                        return;
                    }
                    try {
                        
                        myClass = loader.loadClass(listOfFiles[i].getName().replace(".class", ""));
                    }
                    catch (ClassNotFoundException e2) {
                        System.out.println("Class not found:" + servletInternalName);
                        e2.getMessage();
                        return;
                    }
                     Annotation[] annotationList = myClass.getAnnotations();
                    MyAnnotation a = null;
                    if (annotationList.length != 0) {
                        for (int j = 0; j < annotationList.length; ++j) {
                            try {
                                annotationList[j].annotationType();
                                a = (MyAnnotation)annotationList[j];
                            }
                            catch (Exception e4) {
                                continue;
                            }
                            if (a.name().equals("URLServletName") && a.value().equals(servletInternalName)) {
                                found = true;
                                break;
                            }
                        }
                        if (found) {
                            break;
                        }
                    }
                }
            }
            if (!found) {
                System.out.println("Servlet " + servletInternalName + " not found");
                return;
            }
            HttpServlet servlet = null;
            try {
                servlet = (HttpServlet)myClass.newInstance();
            }
            catch (Exception e3) {
                
                System.out.println(e3.getMessage());
                return;
            }
            ServletHashTable.put(servletInternalName, servlet);
            System.out.println("Servlet " + servletInternalName + " added");
        }
    }
    
    void executeList() {
        
        System.out.println("--- Running Servlets ---");
        Iterator<String> it = ServletHashTable.ht.keySet().iterator();
        while (it.hasNext()) {
            System.out.println("    " + it.next());
        }
    }

    void monitorThread(){
        File file = new File("./handler.txt");
        
            try {
                if(!file.exists())
                throw new FileNotFoundException();
                
                Scanner in = new Scanner(file);
                
                while(in.hasNextLine()){
                    System.out.print(in.nextLine()+"\n");
                }
                in.close();
                
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    
    void executeCommand(String command) {
        if (this.firstWord(command).equals("load")) {
            if (this.secondWord(command) == null) {
                System.out.println("Wrong command!");
                return;
            }
            this.executeLoad(this.secondWord(command));
        }
        else if (this.firstWord(command).equals("remove")) {
            if (this.secondWord(command) == null) {
                System.out.println("Wrong command!");
                return;
            }
            this.executeUnload(this.secondWord(command));
        }
        else if (this.firstWord(command).contentEquals("ls")) {
            if (this.secondWord(command) != null) {
                System.out.println("Wrong command!");
                return;
            }
            this.executeList();
        }
        else if (this.firstWord(command).contentEquals("load-with-annotations")) {
            if (this.secondWord(command) == null) {
                System.out.println("Wrong command!");
                return;
            }
            this.executeLoadWithAnnotations(this.secondWord(command));
        }
        else if(this.firstWord(command).contentEquals("mt")){
            this.monitorThread();
        }
        else{
            if(this.firstWord(command).contentEquals("quit"))
                System.out.println("");
            else{
            System.out.println("Unknown command "+command);
		    System.out.println("Commands supported: load <servlet>, remove <servlet>, load-with-annotations <servlet>, ls, mt, quit");
            }
        }
        
    }
    
    @Override
    public void run() {
        Thread.currentThread().setName("ManagementConsole");
        String command = null;
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("Command: ");
        try {
                command = bufferedReader.readLine();
                
            }
            catch (IOException e) {
                System.out.println("Error. Try Again.");
                System.out.println(e.getMessage());
                System.exit(1);
            }
            finally {
                this.executeCommand(command);
        }
            
        
        System.out.println();
        while (!command.equals("quit") && !Shutdown.flag) {
            System.out.print("Command: ");
            try {
                    command = bufferedReader.readLine();
                    
                
                }
                catch (IOException e) {
                    System.out.println("Error. Try Again.");
                    System.out.println(e.getMessage());
                    System.exit(1);
                }
                finally {
                    this.executeCommand(command);
                }
                
            
            System.out.println();
        }
        Shutdown.flag = true;
        this.serv.setShutdown(true);
        try {
            bufferedReader.close();
            this.serv.getServerSocket().close();
        }
        catch (Exception e2) {
            System.out.println(e2.getMessage());
        }
    }
}