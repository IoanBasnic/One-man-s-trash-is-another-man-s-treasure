package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;

@CrossOrigin(origins = "localhost:8080")
@SpringBootApplication
public class Main {

	public static void main(String[] args) {
		SpringApplication.run(Main.class, args);

		String hostname = "localhost";
		int port = 8080;

//
//		try (Socket socket = new Socket(hostname, port)) {
//
//			InputStream input = socket.getInputStream();
//			BufferedReader reader = new BufferedReader(new InputStreamReader(input));
//
//			String time = reader.readLine();
//
//			System.out.println(time);
//
//
//		} catch (UnknownHostException ex) {
//
//			System.out.println("Server not found: " + ex.getMessage());
//
//		} catch (IOException ex) {
//
//			System.out.println("I/O error: " + ex.getMessage());
//		}
	}

}
