<%@ include file="/WEB-INF/jsp/includes.jsp" %>

<table width="70%" border="0" cellspacing="0">
  <tr>
    <th scope="col" class="style3"><div align="left">Name</div></th>
    <th scope="col" class="style3"><div align="left">Version</div></th>
    <th scope="col" class="style3"><div align="left">Description</div></th>
  </tr>
  <c:forEach items="${components}" var="component">
  <c:url var="viewDetailsUrl" value="componentDetails.action" >
    <c:param name="id" value="${component.id}" />
  </c:url>
  <c:url var="downloadUrl" value="downloadComponent.action" >
    <c:param name="id" value="${component.id}" />
  </c:url>
  <tr>
    <td class="description_text"><c:out value="${component.name}"/></td>
    <td class="description_text"><c:out value="${component.version}"/></td>
    <td class="description_text"><c:out value="${component.oneLineDescription}"/></br>
        <a href="<c:out value="${viewDetailsUrl}"/>">more</a> <a href="<c:out value="${downloadUrl}"/>">download</a>
    </td>
  </tr>
  </c:forEach>
</table>
