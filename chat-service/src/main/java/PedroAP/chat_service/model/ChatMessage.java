package PedroAP.chat_service.model;

public class ChatMessage {
    private String from;
    private String text;

    public ChatMessage() {
    }

    public ChatMessage(String from, String text) {
        this.from = from;
        this.text = text;
    }

    // Getters y setters

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
