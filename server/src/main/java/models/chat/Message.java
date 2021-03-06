package models.chat;

import controllers.ChatController;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.SimpleTimeZone;

public class Message {
    private final int id;

    private int senderId;
    private boolean isHiddenToSender;
    private String text;
    private boolean seen;
    private String timeSent;

    public Message(String text) {
        this.text = text;
        senderId = 0;
        setDefaults();
        this.id = ChatController.getChatController().getNextMessageId();
    }

    public Message(String text, int senderId) {
        this.text = text;
        this.senderId = senderId;
        setDefaults();
        this.id = ChatController.getChatController().getNextMessageId();
    }

    // for gson
    public Message() {
        this.text = "default";
        senderId = 0;
        this.id = 0;
    }

    private void setDefaults() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        timeSent = dtf.format(now);
    }


    public int getSenderId() {
        return senderId;
    }

    public void setSenderId(int senderId) {
        this.senderId = senderId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isSeen() {
        return seen;
    }

    public void setSeen(boolean seen) {
        this.seen = seen;
    }

    public String getTimeSentAsHHMM() {
        Date date = getDateSent();
        SimpleDateFormat dtf = new SimpleDateFormat("hh:mm");
        String result = dtf.format(date);
        return result;
    }

    public Date getDateSent() {
        Date date = null;
        try {
            date = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").parse(timeSent);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public boolean isHiddenToSender() {
        return isHiddenToSender;
    }

    public void setHiddenToSender(boolean hiddenToSender) {
        isHiddenToSender = hiddenToSender;
    }

    public String getTimeSent() {
        return timeSent;
    }

    public void setTimeSent(String timeSent) {
        this.timeSent = timeSent;
    }

    public int getId() {
        return id;
    }
}