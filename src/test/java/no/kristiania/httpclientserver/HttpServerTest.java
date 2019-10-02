package no.kristiania.httpclientserver;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

class HttpServerTest {



    @Test
    void shouldReturnErrorCode() throws IOException {
        int port = startServer();
        HttpClient client = new HttpClient("localhost", port,"/echo?status=401");
        HttpClientResponse response = client.executeRequest();
        assertEquals(401, response.getStatus());
    }

    @Test
    void shouldReturnRequestHeader() throws IOException {
        int port = startServer();
        HttpClient client = new HttpClient("localhost",port,"/echo?Location=http://example.com");
        HttpClientResponse response = client.executeRequest();
        assertEquals(200, response.getStatus());
        assertEquals("http://example.com",response.getHeader("Location"));
    }

    @Test
    void shouldReturn302() throws IOException {
        int port = startServer();
        HttpClient client = new HttpClient("localhost",port,"/echo?status=302&Location=http://example.com");
        HttpClientResponse response = client.executeRequest();
        assertEquals(302, response.getStatus());
        assertEquals("http://example.com",response.getHeader("Location"));
    }

    @Test
    void shouldReturnRequestedBody() throws IOException {
        int port = startServer();
        HttpClient client = new HttpClient("localhost",port,"/echo?body=HelloWorld!");
        HttpClientResponse response = client.executeRequest();
        assertEquals("HelloWorld!",response.getBody());
    }

    /* @Test
    void shouldReturnFilesFromDisk() throws IOException {
        HttpServer server = new HttpServer(0);
        server.start();
        Files.writeString(Paths.get("src/main/resources/sample.txt"),"Hello everyone");
        server.setFileLocation("src/main/resources");
        HttpClient client = new HttpClient("localhost", server.getActualPort(), "/index.html");
        HttpClientResponse response = client.executeRequest();
        assertEquals("Hello everyone",response.getBody());

    } */

    private int startServer() throws IOException {
        HttpServer httpServer = new HttpServer(0);

        new Thread(() -> {
            try {
                httpServer.start();
            } catch (IOException e){
                e.printStackTrace();
            }
        }).start();
        return httpServer.getActualPort();
    }

}