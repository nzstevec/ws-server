package com.clogic.wsserver;

public class MessageDto {
    private String from;
    private String text;

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

    @Override
    public String toString() {
        return "MessageDto{" +
                "from='" + from + '\'' +
                ", text='" + text + '\'' +
                '}';
    }
}
