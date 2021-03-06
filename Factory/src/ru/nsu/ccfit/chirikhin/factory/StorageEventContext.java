package ru.nsu.ccfit.chirikhin.factory;

import org.apache.log4j.Logger;
import ru.nsu.ccfit.chirikhin.blockingqueue.BlockingQueue;

public class StorageEventContext {
    private final static Logger logger = Logger.getLogger(StorageEventContext.class.getName());
    private final StorageEvent storageEvent;
    private final int currentFullness;
    private final int maxSize;

    public StorageEventContext(StorageEvent storageEvent, int currentFullness, int maxSize) {
        if (null == storageEvent) {
            throw new NullPointerException("Null reference");
        }

        if (currentFullness < 0 || maxSize <= 0) {
            throw new IllegalArgumentException("Invalid sizes");
        }

        this.storageEvent = storageEvent;
        this.currentFullness = currentFullness;
        this.maxSize = maxSize;
    }

    public StorageEventContext(BlockingQueue.QueueEventContext queueEventContext) {
        this.currentFullness = queueEventContext.getSize();
        this.maxSize = queueEventContext.getMaxSize();

        if (BlockingQueue.QueueEvent.PUT == queueEventContext.getQueueEvent()) {
            this.storageEvent = StorageEvent.PUT;
        } else {
            this.storageEvent = StorageEvent.GET;
        }
    }


    public StorageEvent getStorageEvent() {
        return storageEvent;
    }

    public int getCurrentFullness() {
        return currentFullness;
    }

    public int getMaxSize() {
        return maxSize;
    }
}
