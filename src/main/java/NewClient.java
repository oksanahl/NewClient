import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.Vector;

import com.google.gson.Gson;

public class NewClient {

    public static int portNumIn = 5000;
    public static int portNumOut = 6000;

    public static String enteredCommand[] = {"",""};

    public static String localFileName;
    public static String fs533FileName;

    public static void main(String[] args) throws Exception {


        while (true) {

            System.out.println("Enter command to execute: put, get, remove, ls, locate, lshere or list, disconnect and grep");
            Scanner scanner = new Scanner(System.in);

            String commandType = scanner.nextLine();

            try {

                String ipAddress = InetAddress.getLocalHost().getHostAddress();
                TCPMessage tcpMessage = new TCPMessage ("client", commandType, ipAddress, ipAddress, 173);

                Socket socket = new Socket(ipAddress, portNumIn);

                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

                if (commandType.contains("put")) {

                    out.println(toJson(executePut(commandType)));
                    socket.close(); }

                else if (commandType.contains ("get")){

                    out.println(toJson(executeGet(commandType)));
                    socket.close();

                }

                else if (commandType.contains ("remove")){

                    out.println(toJson(executeRemove(commandType)));
                    socket.close();

                }

                else if (commandType.contains("locate")){

                    out.println(toJson(executeLocate(commandType)));
                    socket.close();
                }

                else {
                    out.println(toJson(tcpMessage));
                    socket.close();}

                try {


                    ServerSocket listener = new ServerSocket (portNumOut);
                    Socket s = null;
                    s = listener.accept();

                    Scanner in = new Scanner(s.getInputStream());
                    String response = in.nextLine();

                    TCPMessage receivedMessage = parseJason(response);
                    parseReceivedMessage(receivedMessage);

                   if (receivedMessage.commandType.contains("put")){

                        String confirmAsk = in.nextLine();
                        TCPMessage confirmMessage = parseJason(confirmAsk);
                        parseReceivedMessage(confirmMessage);
                        listener.close();

                        commandType = scanner.nextLine();

                        switch (commandType) {

                            case "yes":
                                out.println(toJson(tcpMessage));
                                System.out.println("Put command has been confirmed. File will be updated.");
                                socket.close();
                                break;

                            default:
                                System.out.println ("Put command has been aborted either no or an invalid response has been received");
                                socket.close();
                                break;

                        }
                    }

                   else {

                    listener.close();}
                } catch (Exception e) {
                    System.out.println("Exception: " + e.getMessage()); }

            } catch (Exception e) {
                System.out.println ("Exception: " + e.getMessage()); }
        }
    }

    public static String toJson (Object tcpMessage) {
        Gson json = new Gson();
        String jsonArray = json.toJson(tcpMessage);
        return (jsonArray);}

    public static TCPMessage parseJason (String message) {
        Gson gson = new Gson();
        TCPMessage parsedTCPMessage = gson.fromJson(message,TCPMessage.class );
        return parsedTCPMessage;
    }

    public static void parseReceivedMessage (TCPMessage receivedMessage) {

        Gson json = new Gson();
        String[] temp = json.fromJson(receivedMessage.dataList,String[].class);
        Vector<String> tempList = new Vector<String>();
        for(int i = 0; i < temp.length; i++)
        {
            tempList.add(temp[i]);
        }

        for (String val: tempList
        ) {
            System.out.println(val);
        }
    }

    public static TCPMessage executePut (String commandType) throws UnknownHostException {

        String ipAddress = InetAddress.getLocalHost().getHostAddress();
        enteredCommand = commandType.split("\\s+");
        commandType = enteredCommand[0];
        localFileName = enteredCommand[1];
        fs533FileName = enteredCommand[2];

        TCPMessage localMessage = new TCPMessage ("client", commandType, ipAddress, ipAddress, 173);
        localMessage.localFileName = localFileName;
        localMessage.fs533FileName = fs533FileName;
        return localMessage;
    }

    public static TCPMessage executeGet (String commandType) throws UnknownHostException {

        String ipAddress = InetAddress.getLocalHost().getHostAddress();
        enteredCommand = commandType.split("\\s+");
        commandType = enteredCommand[0];
        localFileName = enteredCommand[2];
        fs533FileName = enteredCommand[1];

        TCPMessage localMessage = new TCPMessage ("client", commandType, ipAddress, ipAddress, 173);
        localMessage.localFileName = localFileName;
        localMessage.fs533FileName = fs533FileName;
        return localMessage;
    }

    public static TCPMessage executeRemove (String commandType) throws UnknownHostException {

        String ipAddress = InetAddress.getLocalHost().getHostAddress();
        enteredCommand = commandType.split("\\s+");
        commandType = enteredCommand[0];
        fs533FileName = enteredCommand[1];

        TCPMessage localMessage = new TCPMessage ("client", commandType, ipAddress, ipAddress, 173);
        localMessage.fs533FileName = fs533FileName;
        return localMessage;
    }

    public static TCPMessage executeLocate (String commandType) throws UnknownHostException {

        String ipAddress = InetAddress.getLocalHost().getHostAddress();
        enteredCommand = commandType.split("\\s+");
        commandType = enteredCommand[0];
        fs533FileName = enteredCommand[1];

        TCPMessage localMessage = new TCPMessage ("client", commandType, ipAddress, ipAddress, 173);
        localMessage.fs533FileName = fs533FileName;
        return localMessage;
    }

    public static class TCPMessage {

        public String messageType;
        public String commandType;
        public String senderIP;
        public String destinationIP;
        public long sendTimestamp;

        public String dataList;
        public String localFileName;
        public String fs533FileName;
        public boolean fileSaveConfirm;

        public TCPMessage(String messageType, String commandType, String senderIP, String destinationIP,  long sendTimestamp)
        {
            this.commandType = commandType;
            this.messageType = messageType;
            this.senderIP = senderIP;
            this.sendTimestamp = sendTimestamp;
            this.destinationIP = destinationIP;

        }



    }
}


