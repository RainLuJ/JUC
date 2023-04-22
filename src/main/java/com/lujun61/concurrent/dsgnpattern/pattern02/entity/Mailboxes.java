package com.lujun61.concurrent.dsgnpattern.pattern02.entity;

import com.lujun61.concurrent.dsgnpattern.pattern02.GuardedObject;

import java.util.Hashtable;
import java.util.Map;
import java.util.Set;

public class Mailboxes {
    private static int id = 1;
    private static Map<Integer, GuardedObject> boxes = new Hashtable<>();

    public static synchronized int generateId() {
        return id++;
    }

    // 用户会进行投信
    public static GuardedObject createGuardedObject() {
        GuardedObject guardedObject = new GuardedObject(generateId());
        boxes.put(guardedObject.getId(), guardedObject);
        return guardedObject;
    }

    // 派件员会派发信
    public static GuardedObject getGuardedObject(int id) {
        return boxes.remove(id);
    }

    public static Set<Integer> getIds() {
        return boxes.keySet();
    }

}
