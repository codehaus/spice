<%@ include file="../../includes.jsp" %>

 <c:forEach items="${results}" var="result">
  <c:url var="viewDetailsUrl" value="componentDetails.action" >
    <c:param name="id" value="${result}" />
  </c:url>
  Component id <c:out value="${result}"/> <a href="<c:out value="${viewDetailsUrl}"/>">here</a>.<br>
</c:forEach>