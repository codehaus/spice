<%@ include file="../../includes.jsp" %>

<span class="description_text">
<c:choose>
    <c:when test='${totalMatches == 0}'>
    No matches found.
    </c:when>
    <c:otherwise>
        Results <c:out value="${beginIndex}"/> to <c:out value="${endIndex}"/> of <c:out value="${totalMatches}"/></br>

        <table border="0">
            <c:forEach items="${results}" var="result">
                <c:url var="viewDetailsUrl" value="componentDetails.action" >
                    <c:param name="id" value="${result.id}" />
                </c:url>
                <tr>
                    <td><a href="<c:out value="${viewDetailsUrl}"/>" class="description_text"><c:out value="${result.name}"/></a></td>
                    <td class="description_text"><c:out value="${result.oneLineDescription}"/></td>
                </tr>
            </c:forEach>
        </table>
    </c:otherwise>
</c:choose>
</span>