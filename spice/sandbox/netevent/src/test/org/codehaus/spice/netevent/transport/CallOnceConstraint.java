package org.codehaus.spice.netevent.transport;

import org.jmock.Constraint;

/**
 * @author Peter Donald
 * @version $Revision: 1.2 $ $Date: 2004-05-17 06:21:39 $
 */
public class CallOnceConstraint
    implements Constraint
{
    private final Constraint _constraint;
    private boolean _invoked;

    public CallOnceConstraint( final Constraint constraint )
    {
        _constraint = constraint;
    }

    public boolean eval( final Object object )
    {
        if( _constraint.eval( object ) && !_invoked )
        {
            _invoked = true;
            return true;
        }
        else
        {
            return false;
        }
    }
}
