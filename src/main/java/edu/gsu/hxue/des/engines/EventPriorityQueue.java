package edu.gsu.hxue.des.engines;

import edu.gsu.hxue.des.models.Event;

import java.io.Serializable;

/**
 * This interface of event priority queues.
 *
 * @author Haidong Xue
 */
public interface EventPriorityQueue extends Serializable {
    /**
     * Insert an event. It erase the event with the same sender in the queue.
     */
    void insert(Event e);

    /**
     * Pop the top event.
     */
    Event popTop();

    boolean isEmpty();

    int getSize();

    Event peekTop();

    Object clone();
}
