package org.codehaus.spice.netevent.transport;

import org.jmock.Constraint;

/**
 * @author Peter Donald
 * @version $Revision: 1.1 $ $Date: 2004-01-12 04:12:19 $
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
