<%@taglib uri="http://jakarta.apache.org/tiles" prefix="tiles" %>
<html>
<head>
<title><tiles:getAsString name="titleString"/></title>
<link rel="stylesheet" type="text/css" href="css/styles.css"/>
</head>

<body>
<table width="100%"  border="0">
  <tr>
  	<%-- Begin Header --%>
    <td colspan="2"><tiles:insert attribute="header"/></td>
	<%-- End Header --%>
  </tr>
  <tr>
  	<%-- Begin Left Menu --%>
    <td width="2%"><%-- Empty, for now --%></td>
	<%-- End Left Menu --%>
	<%-- Begin Main Content --%>
    <td width="94%"><tiles:insert attribute="content"/></td>
	<%-- End Main Content --%>
  </tr>
  <tr>
  	<%-- Begin Footer --%>
    <td colspan="2"><%-- Empty, for now --%></td>
	<%-- End Footer --%>
  </tr>
</table>
</body>
</html>
