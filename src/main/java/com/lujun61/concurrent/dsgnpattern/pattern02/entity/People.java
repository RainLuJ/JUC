package com.lujun61.concurrent.dsgnpattern.pattern02.entity;

import com.lujun61.concurrent.dsgnpattern.pattern02.GuardedObject;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class People extends Thread {

    @Override
    public void run() {
        GuardedObject guardedObject = Mailboxes.createGuardedObject();
        log.info(" id为<<<{}>>>的居民等待收信中……", guardedObject.getId());

        Object o = guardedObject.get(5000);
        log.info("收到信的 id: {}, 内容: {}", guardedObject.getId(), o);
    }

}
