/*

============================================================================
                  The Apache Software License, Version 1.1
============================================================================

Copyright (C) 2002,2003 The Apache Software Foundation. All rights reserved.

Redistribution and use in source and binary forms, with or without modifica-
tion, are permitted provided that the following conditions are met:

1. Redistributions of  source code must  retain the above copyright  notice,
   this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright notice,
   this list of conditions and the following disclaimer in the documentation
   and/or other materials provided with the distribution.

3. The end-user documentation included with the redistribution, if any, must
   include  the following  acknowledgment:  "This product includes  software
   developed  by the  Apache Software Foundation  (http://www.apache.org/)."
   Alternately, this  acknowledgment may  appear in the software itself,  if
   and wherever such third-party acknowledgments normally appear.

4. The names "Jakarta", "Avalon", "Excalibur" and "Apache Software Foundation"
   must not be used to endorse or promote products derived from this  software
   without  prior written permission. For written permission, please contact
   apache@apache.org.

5. Products  derived from this software may not  be called "Apache", nor may
   "Apache" appear  in their name,  without prior written permission  of the
   Apache Software Foundation.

THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,
INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
FITNESS  FOR A PARTICULAR  PURPOSE ARE  DISCLAIMED.  IN NO  EVENT SHALL  THE
APACHE SOFTWARE  FOUNDATION  OR ITS CONTRIBUTORS  BE LIABLE FOR  ANY DIRECT,
INDIRECT, INCIDENTAL, SPECIAL,  EXEMPLARY, OR CONSEQUENTIAL  DAMAGES (INCLU-
DING, BUT NOT LIMITED TO, PROCUREMENT  OF SUBSTITUTE GOODS OR SERVICES; LOSS
OF USE, DATA, OR  PROFITS; OR BUSINESS  INTERRUPTION)  HOWEVER CAUSED AND ON
ANY  THEORY OF LIABILITY,  WHETHER  IN CONTRACT,  STRICT LIABILITY,  OR TORT
(INCLUDING  NEGLIGENCE OR  OTHERWISE) ARISING IN  ANY WAY OUT OF THE  USE OF
THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

This software  consists of voluntary contributions made  by many individuals
on  behalf of the Apache Software  Foundation. For more  information on the
Apache Software Foundation, please see <http://www.apache.org/>.

*/

/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.spice.jervlet.impl.pico;

import org.picocontainer.MutablePicoContainer;
import org.codehaus.spice.jervlet.Instantiator;

/**
 * Instantiator for Pico style servlet/filter components. Note, these
 * slightly break the servlet specification by unsing a non empty
 * constructor.
 *
 * @author Paul Hammant
 * @author Johan Sjoberg
 */
public class PicoInstantiator implements Instantiator
{
    /** The parent container */
    private final MutablePicoContainer m_parentContainer;

   /**
    * Create a new instance.
    * 
    * @param mutablePicoContainer The parent container
    */
   public PicoInstantiator( MutablePicoContainer mutablePicoContainer )
   {
       m_parentContainer = mutablePicoContainer;
   }

   /**
    * Instantiate a Pico servlet or filter class.
    *
    * @param clazz The servlet or filter class to instantiate
    * @return the instatiated class
    * @throws InstantiationException If the class couldn't be instantiated
    * @throws IllegalAccessException If access to the class was restricted
    */
    public Object instantiate( Class clazz )
        throws InstantiationException, IllegalAccessException
    {
        MutablePicoContainer picoContainer =
          m_parentContainer.makeChildContainer();
        picoContainer.registerComponentImplementation( clazz );
        Object object = picoContainer.getComponentInstanceOfType( clazz );
        if( null == object )
        {
            throw new InstantiationException( "Couldn't instantiate class ["
              + clazz.getName() + "]. Picocontainer returned null." );
        }
        return object;
    }
}
