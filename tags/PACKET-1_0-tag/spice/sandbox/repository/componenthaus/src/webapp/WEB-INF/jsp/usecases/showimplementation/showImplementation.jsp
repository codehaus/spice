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
    <td class="style1 style3"><c:out value="${impl.name}"/></td>
    <td bgcolor="#666666" class="style1"><span class="style3"><strong>Authors</strong></span></td>
    <td class="style4"><c:out value="${impl.authorsCSV}"/></td>
    <td bgcolor="#666666" class="style1"><span class="style3"><strong>Plug Interfaces</strong></span></td>
  </tr>
  <tr>
    <td bgcolor="#666666" class="style1"><span class="style3"><strong>Full name</strong></span></td>
    <td class="style4"><c:out value="${impl.fullyQualifiedName}"/></td>
    <td bgcolor="#666666" class="style1"><span class="style3"><strong>Version</strong></span></td>
    <td class="style4"><c:out value="${impl.version}"/></td>
    <td class="style1"><table width="100%"  border="0">
      <c:forEach var="plug" items="${impl.plugs}">
      <c:url var="viewDetailsUrl" value="componentDetails.action" >
        <c:param name="id" value="${plug.id}" />
      </c:url>
      <tr>
        <td><span class="style3"><a href="<c:out value="${viewDetailsUrl}"/>"><c:out value="${plug.name}"/></a></span></td>
        <td><span class="style3"><c:out value="${plug.version}"/></span></td>
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
    <td bgcolor="#666666" class="style4"><strong>Description</strong></td>
    <td colspan="4" class="style4"><span class="style3"><pre><c:out value="${impl.fullDescription}"/></pre></span></td>
  </tr>
</table>

<pre>
Again, I need to get this to fit on 800 x 600.
There is a nice feature on this page, I think.  This page shows you an implementation
of an interface.  It is intended for those looking to use the impl.  As such, it
contains a list of the interfaces this impl depends on, which I have called plugs.  These
plugs are themselves components, so clicking on them will take you back out to an
interface specification page, which will in turn have impls available.  In other words,
there is glue available also.

What I'd like to be able to do is allow a component integrator pick an impl and then got a
consolidated list of the interfaces he must implement.  For each interface, he can pick from
a list of available impls.  Then the process recurses.  Alternatively he can say "I will implement
this myself".  In any case, when he has finished selecting interfaces and impls, he gets a
personalized jar file containing what he needs.

One thing that I am almost ready is to show what other generic dependencies the impl has,
like jar files etc.
</pre>
