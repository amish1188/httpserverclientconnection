package no.kristiania.httpclientserver;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

class HttpClientResponse {
    private Socket socket;
    public String statusLine;
    private Map<String, String> headers = new HashMap<>(); // headers represent answer from th server
    private String body;

    public HttpClientResponse(Socket socket) {
        this.socket = socket;
    }


    //
    public void invoke() throws IOException {
        this.statusLine = readLine(socket);
        System.out.print(this.statusLine);
        String headerline;
        //as long as there is value that is not empty we print out headerline
        while(!(headerline = readLine(socket)).isEmpty()) {
            // returns from which class the code is taken?
            System.out.println(getClass().getSimpleName()  + ": " + headerline);
            // colon : Position in header
            int colonPos = headerline.indexOf(':');
            // pushing into map array value from index 0 to the value of colon index; trim is cutting out the end from position +1 after colon
            this.headers.put(headerline.substring(0,colonPos), headerline.substring(colonPos+1).trim());
        }

        // if header contains content-length
        if(headers.containsKey("Content-Length")){
            StringBuilder body = new StringBuilder();
            // we build body of content length
            for (int i = 0; i< getContentLength(); i++){
                // socket contains our answer from server and build body
                body.append((char)socket.getInputStream().read());
            }
            this.body = body.toString();
        }
    }

    // method returning output stream from both client and server
    static String readLine(Socket socket) throws IOException {
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


    // returns the status code
    public int getStatus() {
        return Integer.parseInt(statusLine.split(" ")[1]);
    }

    // returns header name
    public String getHeader(String headerName) {
        return headers.get(headerName);
    }

    public int getContentLength() {
        return Integer.parseInt(getHeader("Content-Length"));
    }

    public String getBody() {
        return body;
    }
}
