<%@ include file="/WEB-INF/jsp/includes.jsp" %>

<P>
<H2>Find Component:</H2>
<FORM method="POST" action="findComponentResults.htm">
  Category: <select name="categoryName">
  <c:forEach var="category" items="${categories}">
    <option><c:out value="${category.name}"/>
  </c:forEach>
  </select>
  <br>
  Component Name Regexp: <INPUT type="text" maxlength="6" size="6" name="componentNameRegexp" value="<c:out value="${status.value}"/>" ><br>
  <INPUT type = "submit" value="Search"/>
</FORM>