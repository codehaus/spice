package org.realityforge.jmxtools;

class UserConfig
{
   private final String _username;
   private final String _password;

   UserConfig( final String username,
               final String password )
   {
      _username = username;
      _password = password;
   }

   String getUsername()
   {
      return _username;
   }

   String getPassword()
   {
      return _password;
   }
}
