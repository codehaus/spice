package org.realityforge.connector;

class MockConnector
   extends Connector
{
   private final long _lastTxTime;
   private final long _lastRxTime;

   public MockConnector( final long lastTxTime,
                         final long lastRxTime )
   {
      _lastTxTime = lastTxTime;
      _lastRxTime = lastRxTime;
   }

   public long getLastTxTime()
   {
      return _lastTxTime;
   }

   public long getLastRxTime()
   {
      return _lastRxTime;
   }
}
