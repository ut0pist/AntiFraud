import crypt.crypt;
import handlers.authHandler;

import com.sun.net.httpserver.*;
import java.io.IOException;
import java.net.InetSocketAddress;


import java.util.HashMap;
import java.util.Map;



public class server {

    private static Map<String,crypt> cryptMap;
    public static void main(String[] args) throws Exception {
        init();
    }

    private static void init() throws IOException {
        serverInit();
        cryptInit();
    }

    private static void cryptInit() {
        cryptMap = new HashMap<String,crypt>();
    }

    private static void serverInit() throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
        server.createContext("/auth", new authHandler());
        // server.createContext("/keyGet", new getKeyHandler());
        // server.createContext("/transaction", new transactionHandler());
        server.setExecutor(null); // creates a default executor
        server.start();
    }
}
