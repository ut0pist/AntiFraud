package handlers;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;


import com.sun.net.httpserver.*;


public class authHandler implements HttpHandler {

    private Connection connection;
    @Override
    public void handle(HttpExchange t) throws IOException {
        try {
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/hakathon?serverTimezone=Europe/Moscow&useSSL=false", "root", "root");
		} catch (SQLException e) {
            printError(e, t);
            return;
        }
        Map<String, String> queryParams = queryToMap(getRequestString(t));
        if(queryParams.get("login") != null && queryParams.get("passwordHash") != null){
            try {
                PreparedStatement query = connection.prepareStatement("select password_hash, user_id from client_info where login = ?");
                query.setString(1, queryParams.get("login"));
                ResultSet resultSet = query.executeQuery();
                resultSet.next();
                query.close();
                if(!resultSet.getString("passwordHash").equals(queryParams.get("passwordHash"))){
                    sendFile(t, "site/login.html");
                }else{
                    MessageDigest md = MessageDigest.getInstance("MD5");
                    md.update(StandardCharsets.UTF_8.encode( resultSet.getString("user_token")));
                    String newToken = String.format("%032x", new BigInteger(1, md.digest()));
                    query = connection.prepareStatement("UPDATE client_info SET user_token=? where login = ?");
                    query.setString(1, newToken);
                    query.setString(2, queryParams.get("login"));
                    query.executeUpdate();
                    t.getResponseHeaders().add("Set-Cookie", "user_token=newToken");
                    t.getResponseHeaders().add("Location", "https://localhost:8000");//Поменять адрес
                    t.sendResponseHeaders(302, -1);
                    t.close();
                }
            } catch (SQLException e) {
                printError(e, t);
                return;
            } catch (NoSuchAlgorithmException e) {
                printError(e, t);
				e.printStackTrace();
			}
        }else{
            sendFile(t, "site/login.html");
        }
        try{
            connection.commit();
            connection.close();
        }catch(SQLException e){
            System.out.println(e);
        }
    }

    private void printError(Exception e, HttpExchange t) throws IOException{
        t.getResponseHeaders().set("Content-Type", "text/html; charset=UTF-8");
        t.sendResponseHeaders(503, 0);
        String response = "<html><body>Простите. У нас технические шоколадки :( </body><html>";
        OutputStream os = t.getResponseBody();
        os.write(response.getBytes());
        os.close();
        System.out.println(e);
    }

    public void sendFile(HttpExchange t, String path) throws IOException {
        String res = "";
            try(FileReader reader = new FileReader(path)){
                int c;
                while((c = reader.read())>0){
                    res = res + (char)c;
                } 
            }
            catch(IOException ex){
                System.out.println(ex.getMessage());
            }
            t.sendResponseHeaders(200, res.length());
            OutputStream os = t.getResponseBody();
            os.write(res.getBytes());
            os.close();
    }
    public String getRequestString(HttpExchange t){
        StringBuilder textBuilder = new StringBuilder();
        try (Reader reader = new BufferedReader(new InputStreamReader
            (t.getRequestBody(), Charset.forName(StandardCharsets.UTF_8.name())))) {
            int c = 0;
            while ((c = reader.read()) != -1) {
                textBuilder.append((char) c);
            }
        }catch(IOException e){
            return null;
        }
        return textBuilder.toString();
    }
    public Map<String, String> queryToMap(String query){
        Map<String, String> result = new HashMap<String, String>();
        for (String param : query.split("&")) {
            String pair[] = param.split("=");
            if (pair.length>1) {
                result.put(pair[0], pair[1]);
            }else{
                result.put(pair[0], "");
            }
        }
        return result;
    }
}
