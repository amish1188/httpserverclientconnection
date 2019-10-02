package no.kristiania.httpclientserver;

import java.io.IOException;
import java.net.Socket;

public class HttpClient {

    private String host;
    private String requestTarget;
    private int port;

    public HttpClient(String host, int port, String requestTarget) {
        this.host = host;
        this.requestTarget = requestTarget;
        this.port = port;
    }

    public static void main(String[] args) throws IOException {
        new HttpClient("urlecho.appspot.com", 80, "/echo?body=Hello%20world!").executeRequest();
    }

    // executing client request
    // returning httpclientresponse object
    public HttpClientResponse executeRequest() throws IOException {
        try (Socket socket = new Socket(host, port)) {

            socket.getOutputStream().write(("GET " + requestTarget + " HTTP/1.1\r\n").getBytes());
            socket.getOutputStream().write(("Host: " + host + "\r\n").getBytes());
            socket.getOutputStream().write("Connection: close\r\n".getBytes());
            socket.getOutputStream().write("\r\n".getBytes());
            socket.getOutputStream().flush();


            //initiation of new client response class where we are filling parameters of host and port
            HttpClientResponse httpClientResponse = new HttpClientResponse(socket);
            httpClientResponse.invoke();
            return httpClientResponse;

        }
    }


}
