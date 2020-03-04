package com.example.booking.apiSendEmail;

import com.example.booking.constant.Constant;

public class SendGmail {
    private static final String GMAIL_ADMIN = "nhannguyen98bn@gmail.com";
    private static final String PASSWORD_GMAIL = "nhanbn98";

    public void send(String msg, String gmail) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    GMailSender mailSender = new GMailSender(GMAIL_ADMIN, PASSWORD_GMAIL);
                    mailSender.sendMail(Constant.SUBJECT_SEND, msg,
                            GMAIL_ADMIN, gmail);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
