/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.metaclass;

/**
 * Exception if unable to locate MetaClass data
 * for a particular class.
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.1 $ $Date: 2003-08-15 06:53:33 $
 */
public class MetaClassException
   extends Exception
{
   /**
    * The throwable that caused this exception.
    */
   private final Throwable m_cause;

   /**
    * Create an exception with specified message.
    *
    * @param message the message
    */
   public MetaClassException( final String message )
   {
      this( message, null );
   }

   /**
    * Create an exception with specified message and cause.
    *
    * @param message the message
    * @param cause the exception that caused this exception
    */
   public MetaClassException( final String message,
                                     final Throwable cause )
   {
      super( message );
      m_cause = cause;
   }

   /**
    * Return the exception that caused this exception (if any).
    *
    * @return the exception that caused this exception (if any).
    */
   public Throwable getCause()
   {
      return m_cause;
   }
}
