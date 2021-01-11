package com.app.xq.dingding;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Android-小强
 * @email: 15075818555@163.com
 * @data: on 2021/1/8 16:43
 */
class CountMailbox {

    private int count;
    private ArrayList<String> emailAddress;
    private String content;
    private String fromAddress;
    private String fromAddressPass;
    private String title;
    private List<File> files;

    public String getFromAddressPass() {
        return fromAddressPass == null ? "" : fromAddressPass;
    }

    public void setFromAddressPass(String fromAddressPass) {
        this.fromAddressPass = fromAddressPass;
    }

    public String getFromAddress() {
        return fromAddress == null ? "" : fromAddress;
    }

    public void setFromAddress(String fromAddress) {
        this.fromAddress = fromAddress;
    }

    public ArrayList<String> getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(ArrayList<String> emailAddress) {
        this.emailAddress = emailAddress;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getContent() {
        return content == null ? "" : content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title == null ? "" : title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<File> getFiles() {
        return files;
    }

    public void setFiles(List<File> files) {
        this.files = files;
    }
}
