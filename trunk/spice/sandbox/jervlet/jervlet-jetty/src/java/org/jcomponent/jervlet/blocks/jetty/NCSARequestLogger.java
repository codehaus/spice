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
package org.jcomponent.jervlet.blocks.jetty;

import java.util.Locale;
import java.util.TimeZone;
import org.apache.avalon.framework.activity.Initializable;
import org.apache.avalon.framework.logger.AbstractLogEnabled;
import org.mortbay.http.HttpRequest;
import org.mortbay.http.HttpResponse;
import org.mortbay.util.DateCache;

/**
 * Logs request information in NCSA (== apache httpd) format. The
 * contents of log() method was copied with permission from
 * org.mortbay.http.NCSARequestLog.log.
 *
 * @phoenix:block
 * @phoenix:service name="org.jcomponent.jervlet.blocks.jetty.RequestLogger"
 */
public class NCSARequestLogger extends AbstractLogEnabled
    implements Initializable, RequestLogger
{
    /**
     * Timestamp format used by NCSA log is fixed. So it is not
     * configurable.
     */
    private final static String m_logDateFormat = "dd/MMM/yyyy:HH:mm:ss ZZZ";
    private DateCache m_dateCache;

    /**
     * Logs information about a single HTTP transaction after the
     * fact.
     *
     * @param request that was processed
     * @param response that was returned
     * @param responseLength number of octets sent in response
     */
    public void log( HttpRequest request, HttpResponse response, int responseLength )
    {
        if( getLogger().isInfoEnabled() )
        {
            StringBuffer buf = new StringBuffer( 160 );

            buf.append( request.getRemoteAddr() );
            buf.append( " - " );

            String user = request.getAuthUser();
            buf.append( ( user == null ) ? "-" : user );
            buf.append( " [" );
            buf.append( m_dateCache.format( request.getTimeStamp() ) );
            buf.append( "] \"" );
            buf.append( request.getMethod() );
            buf.append( ' ' );
            buf.append( request.getURI() );
            buf.append( ' ' );
            buf.append( request.getVersion() );
            buf.append( "\" " );

            int status = response.getStatus();
            buf.append( (char)( '0' + ( ( status / 100 ) % 10 ) ) );
            buf.append( (char)( '0' + ( ( status / 10 ) % 10 ) ) );
            buf.append( (char)( '0' + ( status % 10 ) ) );

            if( responseLength >= 0 )
            {
                buf.append( ' ' );

                if( responseLength > 99999 )
                {
                    buf.append( Integer.toString( responseLength ) );
                }
                else
                {
                    if( responseLength > 9999 )
                        buf.append( (char)( '0' + ( ( responseLength / 10000 ) % 10 ) ) );
                    if( responseLength > 999 )
                        buf.append( (char)( '0' + ( ( responseLength / 1000 ) % 10 ) ) );
                    if( responseLength > 99 )
                        buf.append( (char)( '0' + ( ( responseLength / 100 ) % 10 ) ) );
                    if( responseLength > 9 )
                        buf.append( (char)( '0' + ( ( responseLength / 10 ) % 10 ) ) );
                    buf.append( (char)( '0' + ( responseLength % 10 ) ) );
                }
                buf.append( ' ' );
            }
            else
            {
                buf.append( " - " );
            }

            getLogger().info( buf.toString() );
        }
    }

    public void initialize() throws Exception
    {
        m_dateCache = new DateCache( m_logDateFormat, Locale.US );
        m_dateCache.setTimeZoneID( TimeZone.getDefault().getID() );
    }
}
