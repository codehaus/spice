<%@ include file="../../includes.jsp" %>
<span class="description_text">
<c:forEach var="page" items="${pages}">
    <c:choose>
        <c:when test='${page.id == currentPage.id}'>
            <c:out value="${page.id}"/>
        </c:when>
        <c:otherwise>
            <c:url var="url" value="searchComponents.action" >
                <c:param name="query" value="${query}" />
                <c:param name="beginIndex" value="${beginIndex}" />
                <c:param name="endIndex" value="${endIndex}" />
            </c:url>
            <a href="<c:out value="${url}"/>"><c:out value="${page.id}"/></a>
        </c:otherwise>
    </c:choose>
</c:forEach>
</span>