<%@ include file="/WEB-INF/jsp/includes.jsp" %>

<P>
<H2>Add Component:</H2>
<FORM method="POST" action="registerComponent.htm">
  Category: <select name="category">
  <c:forEach var="category" items="${categories}">
    <option><c:out value="${category.name}"/>
  </c:forEach>
  </select>
  <br>
  Version: <INPUT type="text" maxlength="6" size="6" name="version" value="<c:out value="${status.value}"/>" ><br>
  Interface (paste the src with the javadoc): <TEXTAREA rows="20" cols="80" name="theInterface" value="<c:out value="${status.value}"/>" ></TEXTAREA><br>
  <INPUT type = "submit" value="Add component"  />
</FORM>