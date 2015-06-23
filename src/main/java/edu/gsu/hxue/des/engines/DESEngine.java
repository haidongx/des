package edu.gsu.hxue.des.engines;

import edu.gsu.hxue.des.models.Model;
import edu.gsu.hxue.des.system.DESSystem;

import java.io.Serializable;

/**
 * This class defines the function of a DES engine.
 *
 * @author Haidong Xue
 */
public interface DESEngine extends Serializable {
    /**
     * Run simulation for certain time.
     *
     * @throws Exception
     */
    void run(double time) throws Exception;

    /**
     * Add model to be simulated by this engine.
     */
    void includeModel(Model model);

    /**
     * Return the simulation time on engine.
     */
    double getEngineTime();

    Object clone();

    void bringModelToTop(Model model);

    void setSystem(DESSystem system);

}
