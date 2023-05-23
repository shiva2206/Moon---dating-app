package NotificationPack;

public class NotificationSender {
    private Data data;
    private String to;

    public Data getData() {
        return data;
    }

    public NotificationSender() {
    }

    public String getTo() {
        return to;
    }

    public NotificationSender(Data data, String to) {
        this.data = data;
        this.to = to;
    }
}
