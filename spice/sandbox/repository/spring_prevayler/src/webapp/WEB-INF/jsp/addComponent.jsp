<%@ include file="/WEB-INF/jsp/includes.jsp" %>

<P>
<H2>Add Component:</H2>
<FORM method="POST">
  Name: <INPUT type="text" maxlength="30" size="30" name="name" value="<c:out value="${status.value}"/>" >
  <INPUT type = "submit" value="Add component"  />
</FORM>