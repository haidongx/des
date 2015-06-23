package edu.gsu.hxue.des.engines;

import edu.gsu.hxue.des.models.Event;
import edu.gsu.hxue.des.models.Model;
import edu.gsu.hxue.des.system.DESSystem;

import java.util.ArrayList;


/**
 * An implementation of {@code DESEngine}.
 *
 * @author Haidong Xue
 */
public class DESEngineImp201202 implements DESEngine, Cloneable {
    private static final long serialVersionUID = -2542708096433276837L;
    private EventPriorityQueue eventQueue; // the event priority queue of this engine
    private double engineTime; //the current simulation time of this engine\
    private DESSystem system;

    public DESEngineImp201202(DESSystem system) {
        this.engineTime = 0;
        this.eventQueue = new EventPriorityQueueImp201202();
        this.system = system;
    }

    @Override
    public void includeModel(Model model) {
        int modelIndex = model.getModelIndex();
        eventQueue.insert(new Event(modelIndex, this.engineTime, null));
    }

    @Override
    public void run(double simulationLength) throws Exception {
        double timer = 0; //timer of this run

        //let all the models run in a discrete event manner
        while (!eventQueue.isEmpty()) {
            //let each model run
            timer += eventQueue.peekTop().getScheduledTime() - this.engineTime;
            if (timer > simulationLength) {
                this.engineTime = eventQueue.peekTop().getScheduledTime() - (timer - simulationLength);
                break;
            }

            Event top = eventQueue.popTop();

            this.engineTime = top.getScheduledTime();
            letModelHandleEvent(top.getSenderIndex(), top); //let the model handle this event
        }
    }

    private void letModelHandleEvent(int modelIndex, Event e) throws Exception {

        //let the model internal-transit
        Model m = system.getModel(modelIndex);

        double duration = this.engineTime - m.getTime();
        m.setTime(this.engineTime);
        m.internalTransit(duration);


        //let the model external-transit
        if (e.getSenderIndex() != modelIndex) {
            m.externalTransit(e.getMessage());
        }

        //let the model generate output and let the affected models handle them
        m.createNewMessageBuffer();
        m.generateOutput();
        ArrayList<Model.ReceiverIndexMessagePair> model_message_pairs = m.getOutput();

        for (Model.ReceiverIndexMessagePair mmp : model_message_pairs) {
            this.letModelHandleEvent(mmp.receiverIndex, new Event(modelIndex, this.engineTime, mmp.message));
        }
        m.clearMessageBuffer();

        //schedule next self-event for this model, if next time is not infinity
        double nextTime = m.getNextInternalTransitionTime();
        if (nextTime != Double.POSITIVE_INFINITY) {
            Event newE = new Event(modelIndex, nextTime + this.engineTime, null);
            this.eventQueue.insert(newE);
        }

    }

    @Override
    public double getEngineTime() {
        return this.engineTime;
    }


    @Override
    public Object clone() {
        //Create a shallow copy
        DESEngineImp201202 c = null;
        try {
            c = (DESEngineImp201202) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            System.err.println(super.getClass() + " does not implement Cloneable.");
            System.exit(1);
        }

        //Modify the shallow copy to get a deep copy
        c.eventQueue = (EventPriorityQueue) (eventQueue.clone());

        return c;
    }

    @Override
    public void setSystem(DESSystem system) {
        this.system = system;
    }


    @Override
    public void bringModelToTop(Model model) {
        int modelIndex = model.getModelIndex();
        eventQueue.insert(new Event(modelIndex, this.engineTime, null));
    }


}
