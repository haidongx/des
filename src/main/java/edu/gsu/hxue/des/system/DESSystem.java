package edu.gsu.hxue.des.system;

import edu.gsu.hxue.des.engines.DESEngine;
import edu.gsu.hxue.des.engines.DESEngineImp201202;
import edu.gsu.hxue.des.models.Model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Vector;

/**
 * The abstract class of a DES system.
 *
 * @author Haidong Xue
 */
public abstract class DESSystem implements Cloneable, Serializable {
    private static final long serialVersionUID = -320019136833061623L;
    private DESEngine engine;
    //cross reference data structure for models
    private Vector<Model> models; // models in a system
    private HashMap<Model, Integer> modelIndex; // used to speedup isContainingModel() and getIndex() methods

    private String name = ""; // not a id, only a simple description

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void printSimulationComponents() {
        System.out.println("Engine: " + engine.hashCode());
        System.out.println("Models: " + models.hashCode());
        System.out.println("modelIndex: " + modelIndex.hashCode());
    }

    public DESSystem(Object config) {
        models = new Vector<Model>();
        modelIndex = new HashMap<Model, Integer>();
        this.engine = new DESEngineImp201202(this);
        this.creatModelsAndCoupling(config);
    }

    protected abstract void creatModelsAndCoupling(Object config);

    /**
     * Let this system run for a given time
     */
    public void run(double time) throws Exception {
        engine.run(time);
    }

    /**
     * Add a model to this system
     */
    public void addModel(Model model) {
        models.add(model); //add the model to this system
        int index = models.size() - 1;
        model.setModelIndex(index);
        modelIndex.put(model, index);
        engine.includeModel(model); //let the engine include this model into simulation
    }

    /**
     * Return true if this system contains the model
     */
    public boolean isContainingModel(Model model) {
        return models.contains(model);
    }

    public boolean isContainingModelIndex(int index) {
        return (index < models.size()) && (index > 0);
    }

    /**
     * Return a deep copy of this system
     */
    public Object clone() {
        //Get a shallow copy of this object
        DESSystem c = null;
        try {
            c = (DESSystem) super.clone();  //its observrModels and MessageBuffer are empty now
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            System.err.println(super.getClass() + " does not implement Cloneable.");
            System.exit(1);
        }

        //Modify the shallow copy to a deep copy
        // for each model, create a semi-deep copy and modify it to a deep copy
        c.models = new Vector<Model>();
        c.modelIndex = new HashMap<Model, Integer>();
        for (int id = 0; id < models.size(); id++) {
            Model oldM = models.elementAt(id);
            Model semiDeepModelCopy = (Model) oldM.clone();
            semiDeepModelCopy.setSystem(c);  // after set the system, it is a deep copy
            c.models.add(semiDeepModelCopy);
            c.modelIndex.put(semiDeepModelCopy, c.models.size() - 1);
        }

        // for engine
        c.engine = (DESEngine) engine.clone();
        c.engine.setSystem(c);

        // return the deep copy of the current object DESSystem
        return c;
    }


    /**
     * Return the system simulation time.
     */
    public double getSystemTime() {
        return this.engine.getEngineTime();
    }


    /**
     * Bring a model to the top of the event queue.
     */
    public void bringModelToTop(Model model) {
        this.engine.bringModelToTop(model);
    }

    public Model getModel(int index) {
        return this.models.elementAt(index);
    }
}
