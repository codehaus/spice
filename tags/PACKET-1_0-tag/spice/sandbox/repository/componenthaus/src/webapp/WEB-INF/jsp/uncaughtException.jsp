<%@ page import="jcontainer.exceptions.ActorException,
                 jcontainer.exceptions.SystemException"%>
<%@ include file="/WEB-INF/jsp/includes.jsp" %>

<H2>Internal error</H2>
<P>

<% 
try {
	// The Servlet spec guarantees this attribute will be available
	Exception exception = (Exception) request.getAttribute("javax.servlet.error.exception"); 

	if (exception != null) {
		if (exception instanceof ServletException) {
			// It's a ServletException: we should extract the root cause
			ServletException sex = (ServletException) exception;
			Throwable rootCause = sex.getRootCause();
			if (rootCause == null)
				rootCause = sex;
			out.println("** Root cause is: "+ rootCause.getMessage());
			rootCause.printStackTrace(new java.io.PrintWriter(out)); 
		} else if ( exception instanceof ActorException ) {
            out.println ("We think you did something wrong: " + exception.getMessage() + "</P>");
            out.println("This problem has been logged - please contact an administrator with this exception id");
        } else if ( exception instanceof SystemException ) {
            out.println ("We experienced a system failure: " + exception.getMessage()+ "</P>" );
            out.println("This problem has been logged - please contact an administrator with this exception id");
        }
		else {
			// It's not a ServletException, so we'll just show it
            out.println("Unknown system failure:</p>");
			exception.printStackTrace(new java.io.PrintWriter(out));
		}
	} 
	else  {
    out.println("No error information available");
	}

    out.println("</P>");
	// Display cookies
	out.println("\nCookies:\n");
	Cookie[] cookies = request.getCookies();
	if (cookies != null) {
    for (int i = 0; i < cookies.length; i++) {
      out.println(cookies[i].getName() + "=[" + cookies[i].getValue() + "]");
		}
	}
	    
} catch (Exception ex) { 
	ex.printStackTrace(new java.io.PrintWriter(out));
}
%>

<P>
<BR>
<A href="<c:url value="welcome.htm"/>">Home</A>


