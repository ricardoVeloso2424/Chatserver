package io.codeforall.kernelfc;

import java.io.*;
import java.net.*;


public class Client {
    public static void main(String[] args) throws IOException {


        // STEP1: Get the host and the port from the command-line
        String hostName = "localhost";
        int portNumber = 8080;

        // STEP2: Open a client socket, blocking while connecting to the server


        try {
            Socket clientSocket = new Socket(hostName, portNumber);
            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            String message = "hello";
            while (message != null){
            message = in.readLine();
            out.println(message);
            }

            in.close();
            out.close();
            clientSocket.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        // STEP4: Read from/write to the stream

// STEP5: Close the streams

// STEP6: Close the sockets
    }
}
