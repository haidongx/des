package edu.gsu.hxue.des.models;

import java.io.Serializable;

/**
 * Discrete event.
 *
 * @author  Haidong Xue
 */
public class Event implements Comparable<Event>, Serializable {
    private static final long serialVersionUID = 7698886728517020349L;

    public Event(int senderIndex, double scheduledTime, Message message) {
        this.senderIndex = senderIndex;
        this.scheduledTime = scheduledTime;
        this.message = message;
    }

    private int senderIndex; // sender
    private double scheduledTime; //time
    private Message message; //message

    /**
     * @return the sender of this event
     */
    public int getSenderIndex() {
        return this.senderIndex;
    }

    /**
     * @return the time of this event
     */
    public double getScheduledTime() {
        return this.scheduledTime;
    }

    /**
     * @return the message of this event
     */
    public Message getMessage() {
        return this.message;
    }


    /**
     * "Note: this compareTo() imposes orderings that may be not inconsistent with equals."
     */
    @Override
    public int compareTo(Event anotherEvent) {
        return Double.compare(this.scheduledTime, anotherEvent.scheduledTime);
    }

    public String toString() {
        return this.senderIndex + "\t" + String.valueOf(this.scheduledTime);
    }


}
