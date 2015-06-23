package edu.gsu.hxue.des.models;

import edu.gsu.hxue.des.system.DESSystem;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * The abstract class of an atomic model in DES system.
 *
 * @author Haidong Xue
 */
public abstract class Model implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = -8589197982690832275L;

    //the simulation time of this model
    private double time;

    //Receiver-Message pair buffer
    private ArrayList<ReceiverIndexMessagePair> messageBuffer = new ArrayList<ReceiverIndexMessagePair>();

    //The DES system that the model belongs to
    protected DESSystem system;

    //Model index
    private int modelIndex;

    public DESSystem getSystem() {
        return system;
    }

    public void setModelIndex(int index) {
        this.modelIndex = index;
    }

    public Model(DESSystem system) {
        this.system = system;
        if (system != null) {
            /*
             * this model is added to the model list of the system
             * the model index will be set at the same time
             * a event is scheduled and added into the event queue
             */
            system.addModel(this);
        }
    }

    public int getModelIndex() {
        return modelIndex;
    }

    /**
     * Getter of current time (should be only used by engines)
     */
    public final double getTime() {
        return this.time;
    }

    /**
     * Setter of current time (should be only used by the engine)
     */
    public final void setTime(double time) {
        this.time = time;
    }

    /**
     * @return model outputs
     */
    public ArrayList<ReceiverIndexMessagePair> getOutput() {
        return this.messageBuffer;
    }

    /**
     * Clear message buffer
     */
    public void clearMessageBuffer() {
        this.messageBuffer.clear();
    }

    public void createNewMessageBuffer() {
        this.messageBuffer = new ArrayList<ReceiverIndexMessagePair>();
    }

    /**
     * Put a receiver-message pair into the message buffer
     *
     * @param receiverIndex   receiver's index
     * @param message  message
     */
    protected void sendMessage(int receiverIndex, Message message) {
        this.messageBuffer.add(new ReceiverIndexMessagePair(receiverIndex, message));
    }

    /**
     * Return a semi-deep copy of this model, i.e. observerModels and system are shallow copy
     */
    public Object clone() {
        try {
            //shallow copy
            Model c = (Model) super.clone();

            //deep copy messageBuffer
            c.messageBuffer = new ArrayList<ReceiverIndexMessagePair>(); //just put a new one here. it should be always empty
            if (!this.messageBuffer.isEmpty()) {
                System.out.println("message buffer: ");
                for (Model.ReceiverIndexMessagePair p : messageBuffer) {
                    System.out.println(p.receiverIndex + " " + p.message);
                }
                throw new Exception("When cloning a Model, it's messageBuffer is not empty");
            }

            return c;

        } catch (CloneNotSupportedException e) {
            System.err.println(super.getClass() + " does not implement Cloneable.");
            e.printStackTrace();
            System.exit(1);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
        return null;
    }

    public void setSystem(DESSystem system) {
        this.system = system;
    }

    /**
     * delta_int function
     */
    public abstract void internalTransit(double delta_time);

    /**
     * delta_ext function
     */
    public abstract void externalTransit(Message message) throws Exception;

    /**
     * lambda function
     */
    public abstract void generateOutput() throws Exception;

    /**
     * return the duration before the next self-transition
     */
    public abstract double getNextInternalTransitionTime();

    /**
     * The class of receiverIndex-message pair
     */
    public class ReceiverIndexMessagePair implements Serializable {
        private static final long serialVersionUID = -879624640149270692L;
        public int receiverIndex;
        public Message message;

        public ReceiverIndexMessagePair(int receiverIndex, Message message) {
            this.receiverIndex = receiverIndex;
            this.message = message;
        }
    }

}
