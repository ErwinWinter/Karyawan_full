import com.sun.net.httpserver.HttpServer;
import controller.KaryawanController;

import java.net.InetSocketAddress;

public class MainServer {
    public static void main(String[] args) {
        try {
            HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);

            server.createContext("/karyawanFull", new KaryawanController());

            server.setExecutor(null);
            server.start();

            System.out.println("Server running on http://localhost:8000");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
