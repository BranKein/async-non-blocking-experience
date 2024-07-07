package me.yeonhyuk.threadpoolqueue;

import java.util.LinkedList;

public class AsyncQueue extends LinkedList<GeneratedResult> {


    public synchronized boolean add(GeneratedResult result) {
        return super.add(result);
    }

    public synchronized void asyncAdd(GeneratedResult result) {
        super.add(result);
    }

    public synchronized GeneratedResult asyncPoll() {
        return super.poll();
    }
}
