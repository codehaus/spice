<%@ include file="../../includes.jsp" %>
<style type="text/css">
<!--
.style3 {font-family: Verdana, Arial, Helvetica, sans-serif; font-size: 10px; }
-->
</style>

<table width="70%"  border="0">
  <tr bgcolor="#666666">
    <th scope="col" class="style3"><div align="left">Name</div></th>
    <th scope="col" class="style3"><div align="left">Version</div></th>
    <th scope="col" class="style3"><div align="left">Description</div></th>
    <th scope="col" class="style3"><div align="left">Implementations Available</div></th>
  </tr>
  <c:forEach items="${components}" var="component">
  <c:url var="viewDetailsUrl" value="componentDetails.action" >
    <c:param name="id" value="${component.id}" />
  </c:url>
  <c:url var="downloadUrl" value="downloadComponent.action" >
    <c:param name="id" value="${component.id}" />
  </c:url>
  <tr>
    <td class="style3"><c:out value="${component.name}"/></br><a href="<c:out value="${viewDetailsUrl}"/>">more</a> <a href="<c:out value="${downloadUrl}"/>">download</a></td>
    <td class="style3"><c:out value="${component.version}"/></td>
    <td class="style3"><c:out value="${component.oneLineDescription}"/></td>
    <td><table width="100%"  border="0">
      <c:forEach var="impl" items="${component.implementations}">
      <c:url var="showImplementationUrl" value="showImplementation.action" >
        <c:param name="id" value="${component.id}" />
        <c:param name="implId" value="${impl.id}" />
      </c:url>
      <tr>
        <td class="style3"><c:out value="${impl.name}"/></br><a href="<c:out value="${showImplementationUrl}"/>">usage</a></td>
        <td class="style3"><c:out value="${impl.version}"/></td>
      </tr>
      </c:forEach>
    </table></td>
  </tr>
  </c:forEach>
</table>

<pre>
Few things here:

* Click on "more" to see more info about a particular interface
* Click on download to download the jar.  I can't get the name of the jar right until a bug in Tomcat is fixed.
* There is a list of available impls of this interface shown on the right.  Click on one of the usage links next to one of them.
* I care nothing about format, as you can see.
* I do need to make the screens fit into 800 x 600 though.
* content of this page is up for debate
</pre>
