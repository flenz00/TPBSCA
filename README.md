# TPBSCA
My implementation of a Thread Pool Based Servlet Container with Annotations for the Software Platforms project

Requires Java 8 to work properly

- Compile java files
```bash
javac -cp javax/javax.servlet-api-3.1.0.jar:. *.java
```
Remember to replace ':' with ';' if you're using Windows

- Create the jar 
```bash
jar -cfm tpbsca.jar Manifest.txt .
```
- Run the jar
```bash
java -jar tpbsca.jar
```
