package com.lujun61.concurrent.dsgnpattern.pattern02;

import com.lujun61.concurrent.dsgnpattern.pattern02.entity.Mailboxes;
import com.lujun61.concurrent.dsgnpattern.pattern02.entity.People;
import com.lujun61.concurrent.dsgnpattern.pattern02.entity.Postman;
import com.lujun61.concurrent.util.JucUtils;


public class GuardedSuspensionTest {

    public static void main(String[] args) {

        for (int i = 0; i < 3; i++) {
            new People().start();
        }

        JucUtils.sleep(1000);

        for(Integer id : Mailboxes.getIds()) {
            new Postman(id, "内容 " + id).start();
        }
    }

}
