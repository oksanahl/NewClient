public class TCPMessage {

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