package com.example.androidlabs;

public class Message {

    //Android Studio hint: to create getter and setter, put mouse on variable and click "alt+insert"
    protected String text;
    protected String type;
    protected long id;
    //type 0 is send, type 1 is receive

    /**Constructor:*/
    public Message(String text, String type, long id)
    {
        this.text = text;
        this.type = type;
        this.id = id;
    }

    @Override
    public String toString(){
        return this.text;
    }

    public String getText() {
        return this.text;
    }

    public String getType() {
        return this.type;
    }

    public long getId() {
        return this.id;
    }

    public void setText(String text){ this.text = text; }

    public void setType(String type){ this.type = type; }

    public void setId(String type){ this.id = id; }




}
