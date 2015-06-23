package edu.gsu.hxue.des.engines;

import edu.gsu.hxue.des.models.Event;
import edu.gsu.hxue.des.models.Message;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.PriorityQueue;

/**
 * An implementation of {@code EventPriorityQueue}.
 *
 * @author Haidong Xue
 */
public class EventPriorityQueueImp201202 implements EventPriorityQueue, Cloneable {
    private static final long serialVersionUID = -524723428595572251L;

    private PriorityQueue<Event> priorityQueue; // the priority queue of events

    /*
     * Note: priorityQueue.contains(Event) should be marked "deprecated" since the complexity is very likely to be O(n).
     * Moreover, if I use priorityQueue.contains(Event) to check if there is a event with the given sender, the overridden Event.equals() is strange since e1.equals(e2) is true only if they have the same sender
     *
     * To solve this problem, I build a sender-event index using HashMap. To search an event with a given sender is then O(1).
     *
     */
    private HashMap<Integer, Event> sender_event_hashMap;

    public EventPriorityQueueImp201202() {
        this.priorityQueue = new PriorityQueue<Event>();
        this.sender_event_hashMap = new HashMap<Integer, Event>();
    }

    private Event getEventWithSenderIndex(int senderIndex) {
        return this.sender_event_hashMap.get(senderIndex); // null will be returned if the sender is not in this hash map
    }

    @Override
    /**
     * Insert an event to the event priority queue.
     */
    public void insert(Event e) {
        //In the priority queue, if there is a event with the same sender as the one of e, remove the existing event
        Event existingEvent = this.getEventWithSenderIndex(e.getSenderIndex());  //O(1)
        if (existingEvent != null) {
            priorityQueue.remove(existingEvent); // O(log(n))
            sender_event_hashMap.remove(e.getSenderIndex()); // O(1)
        }

        //Insert this event
        priorityQueue.add(e);  //O(log(n)
        sender_event_hashMap.put(e.getSenderIndex(), e);
    }

    @Override
    public Event popTop() {
        Event top = this.priorityQueue.poll(); // O(1) for peek, O(log(n)) for remove
        sender_event_hashMap.remove(top.getSenderIndex()); // O(1)
        return top;
    }

    @Override
    public boolean isEmpty() {
        return this.priorityQueue.isEmpty();
    }

    @Override
    public int getSize() {
        return this.priorityQueue.size();
    }

    @Override
    public Event peekTop() {
        return this.priorityQueue.peek();
    }

    @Override
    public Object clone() {
        EventPriorityQueueImp201202 c = null;

        // Create a shallow copy
        try {
            c = (EventPriorityQueueImp201202) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            System.exit(1);
        }

        // Modify the shallow copy to get a deep copy
        c.sender_event_hashMap = new HashMap<Integer, Event>();
        c.priorityQueue = new PriorityQueue<Event>();

        for (Entry<Integer, Event> sender_event : this.sender_event_hashMap.entrySet()) {
            //the sender index
            int newSenderIndex = sender_event.getKey();

            //the scheduled time
            double newTime = sender_event.getValue().getScheduledTime();

            //the message
            Message newMessage = null;
            if (sender_event.getValue().getMessage() != null)
                newMessage = (Message) sender_event.getValue().getMessage().clone();

            // inset the deep copy of event
            Event newE = new Event(newSenderIndex, newTime, newMessage);
            c.insert(newE);
        }

        return c;
    }


}
