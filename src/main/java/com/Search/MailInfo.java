package com.Search;

public class MailInfo {
    private String messageId;
    private String date;
    private String from;
    private String[] to;
    private String subject;
    private String mimeVersion;
    private String contentType;
    private String contentTransferEncoding;
    private String xFrom;
    private String xTo;
    private String xCc;
    private String xBcc;
    private String xFolder;
    private String xOrigin;
    private String xFileName;

    MailInfo(String fileName){
        ResultProcessing.getFileInfo(fileName);
    }
}
