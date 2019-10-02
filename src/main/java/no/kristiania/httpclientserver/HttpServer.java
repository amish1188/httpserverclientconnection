package no.kristiania.httpclientserver;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class HttpServer {

    private ServerSocket serverSocket;
    private String fileLocation;

    public HttpServer(int port) throws IOException {
        serverSocket = new ServerSocket(port);
    }

    public static void main(String[] args) throws IOException {
        HttpServer httpServer = new HttpServer(8080);
        httpServer.setFileLocation("src/main/resources");
        httpServer.start();


    }



    void start() throws IOException {

        while(true){
        Socket socket = serverSocket.accept();


        String statusLine = readLine(socket);
        String line;
        String requestLine = null;
        while (!(line = readLine(socket)).isEmpty()) {

            System.out.println("Line: " + line);
        }
        System.out.println("Done");


        String requestTarget = statusLine.split(" ")[1];
        int questionPos = requestTarget.indexOf("?");
        String requestPath = questionPos == -1 ? requestTarget : requestTarget.substring(0, questionPos);
        if(!requestPath.equals("/echo")) {
            File file = new File(fileLocation + requestPath);
            socket.getOutputStream().write(("HTTP/1.1 200 OK\r\n" +
                    "Content-Type: text/html\r\n" +
                    "Content-Length: " + file.length() + "\r\n" +
                    "Connection: close\r\n"+
                    "\r\n").getBytes());
            new FileInputStream(file).transferTo(socket.getOutputStream());
            socket.getOutputStream().flush();
            return;
        }





        Map<String, String> queryParameters = parseQueryParameters(requestTarget);

        String status = queryParameters.getOrDefault("status", "200");
        String location = queryParameters.getOrDefault("Location", null);
        String body = queryParameters.getOrDefault("body", "Hello World");


        //if(queryParameters.containsKey("status")){
          //  status = queryParameters.get("status");
        //} else if (queryParameters.containsKey("Location")){
         //   location = queryParameters.get("Location");
        //}

        //else if (queryParameters.containsKey("body")){
          //  body = paramValue;
        //}

        socket.getOutputStream().write(("HTTP/1.1 " + status + " OK\r\n").getBytes());
        socket.getOutputStream().write("Content-Type: text/html; charset=utf-8\r\n".getBytes());
        socket.getOutputStream().write(("Content-Length: " + body.length() + "\r\n").getBytes());
        socket.getOutputStream().write("Connection: close\r\n".getBytes());
        if(location != null){
            socket.getOutputStream().write(("Location: " + location + "\r\n").getBytes());
        }
        socket.getOutputStream().write("\r\n".getBytes());
        socket.getOutputStream().write(body.getBytes());
        socket.getOutputStream().flush();
    } }

    private Map<String, String> parseQueryParameters(String requestTarget) {
        Map<String,String> queryParameters = new HashMap<>();
        int questionPos = requestTarget.indexOf('?');
        if (questionPos > 0) {
            String query = requestTarget.substring(questionPos + 1);
            for(String parameter : query.split("&")) {
                int equalsPos = parameter.indexOf('=');
                String paramName = parameter.substring(0, equalsPos);
                String paramValue = parameter.substring(equalsPos + 1);
                queryParameters.put(paramName, paramValue);
            }
        }
        return queryParameters;
    }

    static String readLine (Socket socket) throws IOException {
            int c;
            StringBuilder line = new StringBuilder();
            while ((c = socket.getInputStream().read()) != -1) {
                if (c == '\r') {
                    c = socket.getInputStream().read();

                    if (c != '\n') {
                        System.err.println("Unexpected character! " + ((char) c));
                    }
                    return line.toString();
                }

                line.append((char) c);
            }
            return line.toString();

        }






    public int getActualPort() {
        return serverSocket.getLocalPort();
    }

    public void setFileLocation(String fileLocation) {
        this.fileLocation = fileLocation;
    }
}


