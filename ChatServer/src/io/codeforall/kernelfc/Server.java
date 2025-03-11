package io.codeforall.kernelfc;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {

    static ArrayList <ClientHandler> clientHandlers = new ArrayList<>();

    public static void main(String[] args) {
        // STEP1: Get parameters from command line arguments
        //ArrayList <ClientHandler> clientHandlers = new ArrayList<>();
        ServerSocket serverSocket = null;
        Socket clientSocket = null;
        int portNumber = 8080;






        try {
            serverSocket = new ServerSocket(portNumber);
            clientSocket = serverSocket.accept();
            System.out.println("A new chat has begun. Waiting for clients message!");
            ClientHandler aux = new ClientHandler(clientSocket);
            ExecutorService cachedpool = Executors.newCachedThreadPool();

            clientHandlers.add(0, aux);
            cachedpool.submit(aux);

            while(true){
                clientSocket = serverSocket.accept();
                ClientHandler aux2 = new ClientHandler(clientSocket);
                clientHandlers.add(0, aux2);
                cachedpool.submit(aux2);
                //Thread.sleep(500);
            }



            /*serverSocket.close();
            clientSocket.close();
            cachedpool.shutdown();
            System.out.println("Chat has been closed");*/

        } catch (IOException e) {
            throw new RuntimeException(e);
        }


// STEP2: Bind to local port and block while waiting for client connections


// STEP4: Read from/write to the stream
    }

    static class ClientHandler implements Runnable{

        String name;
        Socket clientSocket;
        PrintWriter out;
        BufferedReader in;

        public ClientHandler(Socket clientSocket) {
            this.clientSocket = clientSocket;
            try {
                this.out = new PrintWriter(clientSocket.getOutputStream(), true);
                this.in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                setName();
            } catch (IOException e) {
                System.out.println(e.getMessage());;
            }

        }
        public void setName (){
            try {
                String name = in.readLine();
                this.name = name;
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
            System.out.println(name + " entered the chat");
        }

        public PrintWriter getOut() {
            return out;
        }

        public String getName() {
            return name;
        }

        public void read () {
            String message = "ola menina quero cuidar de ti";

            while (message != null) {

                try {
                    message = in.readLine();
                    String[] commands = message.split("\\s+");
                    if(message.equals("/quit")) break;
                    if(message.equals("/list")) writeList();
                    if(commands[0].equals("/whisper")) {
                        whisperMessage(commands);
                    }
                    if(message != null && !message.equals("/quit") && !message.equals("/list") && !(commands[0].equals("/whisper"))){
                        write(message);
                        System.out.println(name + " said: " + message);
                    }


                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }
            write("rage quitted");

            try {
                in.close();
                out.close();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }


        }

        public void writeList(){
            String names = "";
            for(int i=0; i<clientHandlers.size(); i++){
                names = addName(names, clientHandlers.get(i).getName());
            }
            out.println(names);
        }

        public String addName(String allNames, String name){
            return (allNames + " " + name);
        }

        public void write (String message){
            for(int i = 0; i<clientHandlers.size();i++){
                if(clientHandlers.get(i).getName() != name){
                    clientHandlers.get(i).getOut().println(name + ": " + message);
                }
            }
        }

        public void whisperMessage (String[] commands){
            if(commands.length > 2){
                String name = commands[1];
                String message = "";
                for(int i = 2;i<commands.length;i++){
                    message = addName(message, commands[i]);
                }


                for(int i = 0; i<clientHandlers.size();i++){
                    if(clientHandlers.get(i).getName().equals(name)){
                        clientHandlers.get(i).getOut().println(this.name + " whispered : " + message);
                    }
                }
            } else out.println("whisper command badly used");
        }


        @Override
        public void run() {
            read();
        }
    }
}
