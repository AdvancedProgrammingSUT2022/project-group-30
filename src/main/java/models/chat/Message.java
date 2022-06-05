package models.chat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.SimpleTimeZone;

public class Message {
    private int senderId;
    private String text;
    private boolean seen;
    private String timeSent;

    public Message(String text) {
        this.text = text;
        senderId = 0;
        setDefaults();
    }

    public Message(String text, int id) {
        this.text = text;
        senderId = id;
        setDefaults();
    }

    public Message() {
        this.text = "default";
        senderId = 0;
        setDefaults();
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
}