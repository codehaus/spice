<%@ include file="../../includes.jsp" %>
<span class="description_text">
<c:forEach var="page" items="${pages}">
    <c:choose>
        <c:when test='${page.pageIndex == currentPage.pageIndex}'>
            <c:out value="${page.pageIndex}"/>
        </c:when>
        <c:otherwise>
            <c:url var="url" value="searchComponents.action" >
                <c:param name="query" value="${query}" />
                <c:param name="beginIndex" value="${page.hitBeginIndex}" />
                <c:param name="endIndex" value="${page.hitEndIndex}" />
            </c:url>
            <a href="<c:out value="${url}"/>"><c:out value="${page.pageIndex}"/></a>
        </c:otherwise>
    </c:choose>
</c:forEach>
</span>