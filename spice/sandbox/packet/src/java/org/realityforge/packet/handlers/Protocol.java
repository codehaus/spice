/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.packet.handlers;

/**
 * Set of constants used in network protocol.
 * 
 * @author Peter Donald
 * @version $Revision: 1.9 $ $Date: 2004-02-18 02:33:38 $
 */
public class Protocol
{
   /**
    * Byte array containing magic number sent at start of stream.
    */
   public static final byte[] MAGIC = new byte[]
   {
      'm', 'a', 'g', 'i', 'c', '0', '1'
   };

   /**
    * The sizeof header that client sends to server.
    */
   public static final int SIZEOF_GREETING = MAGIC.length +
                                             TypeIOUtil.SIZEOF_LONG /* session */ +
                                             TypeIOUtil.SIZEOF_SHORT /* auth */;

   /**
    * The Max-message header is the "connect" message to client. The "data"
    * message is the largest size but it does secondary checking before reading
    * from payload.
    */
   public static final int MAX_MESSAGE_HEADER = TypeIOUtil.SIZEOF_BYTE +
                                                TypeIOUtil.SIZEOF_LONG +
                                                TypeIOUtil.SIZEOF_SHORT;

   /**
    * Max difference between successive sequence numbers to test for wrap
    * around.
    */
   public static final short MAX_DIFF = Short.MAX_VALUE / 2;

   /**
    * Return true if seq1 is less than or equal seq2 accounting for wraparound.
    *
    * @param seq1 a sequence
    * @param seq2 a sequence
    * @return true if seq1 is less than or equal seq2
    */
   public static boolean isLessThanOrEqual( final short seq1,
                                            final short seq2 )
   {
      return seq1 <= seq2 || seq1 - MAX_DIFF > seq2;
   }

   /**
    * Return true if seq1 is next in sequence after seq2 accounting for
    * wrapping.
    *
    * @param seq1 the first sequence
    * @param seq2 the seconds sequence
    * @return true if seq1 is next in sequence after seq2 accounting for
    *         wrapping.
    */
   public static final boolean isNextInSequence( final short seq1,
                                                 final short seq2 )
   {
      return (short)(seq2 + 1) == seq1;
   }
}
