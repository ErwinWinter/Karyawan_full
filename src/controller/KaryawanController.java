package controller;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import model.Karyawan;
import repository.KaryawanRepository;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class KaryawanController implements HttpHandler {
    
    private Gson gson = new Gson();

    @Override
    // handle endpoint
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        // String query = exchange.getRequestURI().getQuery();
        // String parts[] = query.split("/");

        // pilihan method endpoint
        // if (method.equals("POST")) {
        //     handlePost(exchange);
        // } else if (method.equals("GET")) {
        //     if (query == null) {
        //         handleGet(exchange);
        //     } else {
        //         // ! handleGetById tidak bisa digunakan 
        //         // * coba untuk membuat parts dan menghubungkan nya dengan handleGetById
        //         handleGetById(exchange);
        //     }
        // } else if (method.equals("PUT")) {
        //     handlePut(exchange);
        // } else if (method.equals("DELETE")) {
        //     handleDelete(exchange);
        // }
        switch(method) {
            case "GET" -> handleGet(exchange);
            case "POST" -> handlePost(exchange);
            case "PUT" -> handlePut(exchange);
            case "DELETE" -> handleDelete(exchange);
            // default -> sendJson(exchange, "Mehtod Not Found", 400);
        }
    }

    // handle post endpoint dan database
    private void handlePost(HttpExchange exchange) throws IOException {
        // input value ke endpoint POST di postman
        InputStream body = exchange.getRequestBody();
        String json = new String(body.readAllBytes(), StandardCharsets.UTF_8);

        // passing value dari input POST ke database
        Karyawan k = gson.fromJson(json, Karyawan.class);
        KaryawanRepository.insert(k);   

        // logic response di postman
        String resp = "{\"status\":\"success\"}";
        exchange.sendResponseHeaders(200, resp.length());
        OutputStream os = exchange.getResponseBody();
        os.write(resp.getBytes());
        os.close();
    }

    // handle get postman dan database
    private void handleGet(HttpExchange exchange) throws IOException {
        List<Karyawan> list = KaryawanRepository.getAll();
        String resp = gson.toJson(list);

        exchange.sendResponseHeaders(200, resp.length());
        OutputStream os = exchange.getResponseBody();
        os.write(resp.getBytes());
        os.close();
    }

    // handle handle getById
    // private void handleGetById(HttpExchange exchange) throws IOException {
    //     String query = exchange.getRequestURI().getQuery();
    //     int id = Integer.parseInt(query.split("=")[1]);

    //     Karyawan k = KaryawanRepository.cariById(id);

    //     String resp = gson.toJson(k);
    //     sendJson(exchange, resp);
    // }

    // handle PUT
    private void handlePut(HttpExchange exchange) throws IOException {
        InputStream body = exchange.getRequestBody();
        String json = new String(body.readAllBytes(), StandardCharsets.UTF_8);

        Karyawan k = gson.fromJson(json, Karyawan.class);
        KaryawanRepository.update(k);

        sendJson(exchange, "{\"status\":\"updated\"}");
    }

    // handle DELETE
    //! buat logic untuk me nonaktifkan safe update di database di KaryawanRepository
    private void handleDelete(HttpExchange exchange) throws IOException {
        String query = exchange.getRequestURI().getQuery();
        int id = Integer.parseInt(query.split("=")[1]);

        KaryawanRepository.hapusKaryawan(id);

        sendJson(exchange, "{\"status\":\"deleted\"}");
    }

    // method helper
    private void sendJson(HttpExchange exchange, String resp) throws IOException {
        exchange.sendResponseHeaders(200, resp.length());
        OutputStream os = exchange.getResponseBody();
        os.write(resp.getBytes());
        os.close();
    }
}
