<%@ include file="/WEB-INF/jsp/includes.jsp" %>

<P>
<H2>Add Component:</H2>
<FORM method="POST">
  Category Name: <INPUT type="text" maxlength="30" size="30" name="categoryName" value="<c:out value="${status.value}"/>" ><br>
  <INPUT type = "submit" value="Add category"  />
</FORM>