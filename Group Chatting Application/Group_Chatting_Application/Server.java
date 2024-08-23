// package Group_Chatting_Application;
// import java.io.*;
// // import java.net.ServerSocket;
// // import java.net.Socket;
// import java.net.*;
// import java.util.*;

// public class Server implements Runnable {
//     Socket socket;

//     public static Vector client = new Vector();

//     public  Server(Socket socket) {
//          try{
//             this.socket = socket;
//          }
//          catch(Exception e){
//             e.printStackTrace();
//          }
//     }

//     public void run() {
//         try{
//             BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//             BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

//             Vector<BufferedWriter> client = new Vector<>();
            
//             client.add(writer);

//             while(true){
//                 String data = reader.readLine().trim();
//                 System.out.println("Recieved"+ data);

//                 for(int i =0;i<client.size();i++){
//                       try{
//                          BufferedWriter bw = (BufferedWriter) client.get(i); 
//                          bw.write(data);
//                          bw.write("\r\n");
//                          bw.flush();
//                       }
//                       catch(Exception e){
//                         e.printStackTrace();
//                       }
//                 }
//          }
//         }
//          catch(Exception e){
//             e.printStackTrace();
//          }
//     }
//     public static void main(String[] args) throws IOException {
//         ServerSocket s = new ServerSocket(2003);
//         while(true) {
//            Socket socket = s.accept();
//            Server server = new Server(socket);
//            Thread thread = new Thread(server);
//            thread.start();
//            s.close();   
//         }
//     }
// }
package Group_Chatting_Application;

import java.io.*;
import java.net.*;
import java.util.*;

public class Server implements Runnable {
    private Socket socket;
    private static Vector<BufferedWriter> clients = new Vector<>();

    public Server(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

            clients.add(writer);

            while (true) {
                String data = reader.readLine();
                System.out.println("Received: " + data);

                for (BufferedWriter client : clients) {
                    try {
                        client.write(data + "\r\n");
                        client.flush();
                    } catch (IOException e) {
                        // If there's an exception sending the message to a client,
                        // remove that client from the vector
                        clients.remove(client);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(2003);
        System.out.println("Server started...");
        while (true) {
            Socket socket = serverSocket.accept();
            Server server = new Server(socket);
            Thread thread = new Thread(server);
            thread.start();
        }
    }
}
