<%@ include file="/WEB-INF/jsp/includes.jsp" %>

<P>
<H2>Components:</H2>
<TABLE border="true">
  <TH>Name</TH><TH>Version</TH><TH>Interface</TH>
  <c:forEach var="component" items="${components}">
    <TR>
      <TD><c:out value="${component.name}"/></TD>
      <TD><c:out value="${component.version}"/></TD>
      <TD><PRE><TEXTAREA ROWS="20" COLS="80" NAME="WHOCARES" READONLY><c:out value="${component.theInterface}"/></TEXTAREA></PRE></TD>
    </TR>
  </c:forEach>
</TABLE>
<P>
<BR>
<A href="<c:url value="welcome.htm"/>">Home</A>
