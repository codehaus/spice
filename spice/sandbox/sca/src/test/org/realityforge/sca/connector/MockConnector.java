package org.realityforge.sca.connector;

class MockConnector
   extends Connector
{
   private final long _lastTxTime;
   private final long _lastRxTime;
   private long _lastPingTime;
   private int _pingCount;

   MockConnector( final long lastTxTime,
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

   public boolean ping()
   {
      _lastPingTime = System.currentTimeMillis();
      _pingCount++;
      return true;
   }

   int getPingCount()
   {
      return _pingCount;
   }
}
