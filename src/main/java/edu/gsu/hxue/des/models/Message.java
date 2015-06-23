package edu.gsu.hxue.des.models;

import java.io.Serializable;

/**
 * The base class of a message among models.
 *
 * @author Haidong Xue
 */
public class Message implements Cloneable, Serializable {
    private static final long serialVersionUID = 3705398379235377339L;

    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }
}
