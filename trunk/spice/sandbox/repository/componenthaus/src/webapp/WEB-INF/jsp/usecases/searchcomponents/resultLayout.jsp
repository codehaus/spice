<%@taglib uri="http://jakarta.apache.org/tiles" prefix="tiles" %>
<table width="100%"  border="0">

  <%-- Search form here --%>
  <tr>
    <td><tiles:insert attribute="searchForm"/></td>
  </tr>

  <%-- Results here --%>
  <tr>
    <td><tiles:insert attribute="results"/></td>
  </tr>

  <%-- Pagination here --%>
  <tr>
    <td><tiles:insert attribute="pagination"/></td>
  </tr>

</table>
