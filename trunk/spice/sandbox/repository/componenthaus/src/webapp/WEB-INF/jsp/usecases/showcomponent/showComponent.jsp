<%@ include file="/WEB-INF/jsp/includes.jsp" %>
<table width="70%"  border="0">
    <tr>
        <td class="field_name" valign="top"><b>Name</b></td>
        <td class="description_text"><c:out value="${component.name}"/></td>
    </tr>
    <tr>
        <td class="field_name" valign="top"><b>Version</b></td>
        <td class="description_text"><c:out value="${component.version}"/></td>
    </tr>
    <tr>
        <td class="field_name" valign="top"><b>Description</b></td>
        <td class="description_text"><c:out value="${component.fullDescription}"/></td>
    </tr>
    <tr>
        <td class="field_name" valign="top"><b>Interface</b></td>
        <td class="description_text"><pre><c:out value="${component.serviceInterface}"/></pre></td>
    </tr>
</table>
