package com.lujun61.concurrent.dsgnpattern.pattern02.entity;

import com.lujun61.concurrent.dsgnpattern.pattern02.GuardedObject;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class Postman extends Thread {

    private int id;
    private String mail;

    public Postman(int id, String mail) {
        this.id = id;
        this.mail = mail;
    }

    @Override
    public void run() {
        GuardedObject guardedObject = Mailboxes.getGuardedObject(id);
        log.info("送信的 id: {}, 内容: {}", id, mail);
        guardedObject.complete(mail);
    }
}