<%@ include file="../../includes.jsp" %>
<style type="text/css">
<!--
.style1 {
	font-family: Verdana, Arial, Helvetica, sans-serif;
	font-size: 10px;
}
.style2 {
	font-size: 10px;
	font-weight: bold;
}
.style3 {font-size: 10px}
.style4 {font-family: Verdana, Arial, Helvetica, sans-serif; font-size: 10px; }
-->
</style>
<table width="70%"  border="0">
  <tr>
    <td bgcolor="#666666" class="style1"><span class="style2">Name</span></td>
    <td class="style1 style3"><c:out value="${component.name}"/></td>
    <td bgcolor="#666666" class="style1"><span class="style3"><strong>Authors</strong></span></td>
    <td class="style4"><c:out value="${component.authorsCSV}"/></td>
    <td bgcolor="#666666" class="style1"><span class="style3"><strong>Implementations Available</strong></span></td>
  </tr>
  <tr>
    <td bgcolor="#666666" class="style1"><span class="style3"><strong>Full name</strong></span></td>
    <td class="style4"><c:out value="${component.fullyQualifiedName}"/></td>
    <td bgcolor="#666666" class="style1"><span class="style3"><strong>Version</strong></span></td>
    <td class="style4"><c:out value="${component.version}"/></td>
    <td class="style1"><table width="100%"  border="0">
      <c:forEach var="impl" items="${component.implementations}">
      <c:url var="showImplementationUrl" value="showImplementation.action" >
        <c:param name="id" value="${component.id}" />
        <c:param name="implId" value="${impl.id}" />
      </c:url>
      <tr>
        <td><span class="style3"><c:out value="${impl.name}"/></br><a href="<c:out value="${showImplementationUrl}"/>">usage</a></span></td>
        <td><span class="style3"><c:out value="${impl.version}"/></span></td>
      </tr>
      </c:forEach>
    </table></td>
  </tr>
  <tr>
    <td bgcolor="#666666" class="style3"><span class="style4"><strong>Dependency Strategy</strong></span></td>
    <td class="style4"><a href="/todo.html">Type 3 IoC</a></td>
    <td bgcolor="#666666" class="style2"><span class="style4"><span class="style3"><strong>Language</strong></span></span></td>
    <td class="style4">Java</td>
    <td class="style1">&nbsp;</td>
  </tr>
  <tr>
    <td bgcolor="#666666" class="style4"><strong>Interface</strong></td>
    <td colspan="4" class="style4"><span class="style3"><pre><c:out value="${component.serviceInterface}"/></pre></span></td>
  </tr>
</table>

<pre>
Many things to add here, like links to many of the maven reports, javadoc, testsuite, coverage etc.
Again, there is a link to the implementations of this interface.
</pre>
