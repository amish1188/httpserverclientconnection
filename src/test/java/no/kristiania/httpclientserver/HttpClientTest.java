package no.kristiania.httpclientserver;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HttpClientTest {


    @Test
    void athShouldWork(){
        assertEquals(4,2+2);
    }

    @Test
    void shouldReadStatusCode() throws IOException {
        HttpClient client = new HttpClient("urlecho.appspot.com", 80,"/echo");
        HttpClientResponse response = client.executeRequest();
        assertEquals(200, response.getStatus());
    }

    @Test
    void shouldReadFailureStatusCode() throws IOException{
        HttpClient client = new HttpClient("urlecho.appspot.com", 80,"/echo?status=401");
        HttpClientResponse response = client.executeRequest();
        assertEquals(401,response.getStatus());
    }

    @Test
    void shouldReadContentLength() throws IOException{
        HttpClient client = new HttpClient("urlecho.appspot.com", 80,"/echo?body=12345678");
        HttpClientResponse response = client.executeRequest();
        assertEquals(8,response.getContentLength());
    }

    @Test
    void shouldReadBody() throws IOException{
        HttpClient client = new HttpClient("urlecho.appspot.com", 80,"/echo?body=HelloWorld!");
        HttpClientResponse response = client.executeRequest();
        assertEquals("HelloWorld!",response.getBody());
    }





}
