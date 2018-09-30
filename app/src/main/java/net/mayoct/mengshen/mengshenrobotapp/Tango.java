package net.mayoct.mengshen.mengshenrobotapp;

public class Tango {
    private String hanzi;
    private String pinyin;
    private String tones;

    public Tango(String hanzi, String pinyin, String tones) {
        this.hanzi = hanzi;
        this.pinyin = pinyin;
        this.tones = tones;
    }

    public void setHanzi(String hanzi) {
        this.hanzi = hanzi;
    }
    public String getHanzi() {
        return this.hanzi;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }
    public String getPinyin() {
        return this.pinyin;
    }

    public void setTones(String tones) {
        this.tones = tones;
    }
    public String getTones() {
        return this.tones;
    }
}
