package org.realityforge.connector;

class MockConnector
   extends Connector
{
   private final long _lastTxTime;
   private final long _lastRxTime;
   private final long _lastPingTime;

   public MockConnector( final long lastTxTime,
                         final long lastRxTime,
                         final long lastPingTime )
   {
      _lastTxTime = lastTxTime;
      _lastRxTime = lastRxTime;
      _lastPingTime = lastPingTime;
   }

   public long getLastPingTime()
   {
      return _lastPingTime;
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
