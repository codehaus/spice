package org.realityforge.packet.event.impl.collections;

import java.nio.BufferUnderflowException;

/**
 * Simple Buffer interface.
 * 
 * @author Peter Donald
 * @version $Revision: 1.1 $ $Date: 2003-12-05 02:14:24 $
 */
public interface Buffer
{
    /**
     * Adds the object to the buffer.
     *
     * @param object the object to add
     * @return true
     */
    boolean add( Object object );

    /**
     * Adds the objects to the buffer.
     *
     * @param objects the objects to add
     * @return true
     */
    boolean addAll( Object[] objects );

    /**
     * Returns next object.
     * 
     * @return the next object
     * @throws BufferUnderflowException if the buffer is empty
     */
    Object peek();

    /**
     * Returns next object and removes from the buffer.
     * 
     * @return the next object
     * @throws BufferUnderflowException if the buffer is empty
     */
    Object pop();

    /**
     * Return the number of objects that are stored in buffer.
     *
     * @return the number of objects that are stored in buffer.
     */
    int size();
}
