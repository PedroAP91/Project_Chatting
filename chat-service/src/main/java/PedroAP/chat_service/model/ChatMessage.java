package PedroAP.chat_service.model;

public class ChatMessage {
    private String from; // o "nick"
    private String text;

    // Constructor, getters y setters (puedes usar Lombok)
    public ChatMessage() {}

    public ChatMessage(String from, String text) {
        this.from = from;
        this.text = text;
    }

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
