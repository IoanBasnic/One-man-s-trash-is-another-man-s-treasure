package OMTIAMT.serviceServer.Server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Date;

@SpringBootApplication
@RestController
public class ServiceServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServiceServerApplication.class, args);

		int port = 6868;
		try (ServerSocket serverSocket = new ServerSocket(port)) {

			System.out.println("This server is listening on port " + port);

			while (true) {
				Socket socket = serverSocket.accept();
				System.out.println("Middleware server connected!");
				OutputStream output = socket.getOutputStream();
				PrintWriter writer = new PrintWriter(output, true);
				writer.println(new Date().toString());
			}

		} catch (IOException ex) {
			System.out.println("Server exception: " + ex.getMessage());
			ex.printStackTrace();
		}
	}

//	@Bean
//	public RestTemplate restTemplate(){
//		return new RestTemplate();
//	}

}
