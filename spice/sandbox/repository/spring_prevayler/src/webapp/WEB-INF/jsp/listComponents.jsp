<%@ include file="/WEB-INF/jsp/includes.jsp" %>

<P>
<H2>Components:</H2>
<TABLE border="true">
  <TH>Name</TH>
  <c:forEach var="component" items="${components}">
    <TR>
      <TD><c:out value="${component.name}"/></TD>
    </TR>
  </c:forEach>
</TABLE>
<P>
<BR>
<A href="<c:url value="welcome.htm"/>">Home</A>
