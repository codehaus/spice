<%@ include file="/WEB-INF/jsp/includes.jsp" %>

<span class="description_text">
Type your search query below.  You can use AND, OR and NOT.  '+' indicates a required word.
</span>

<form action="searchComponents.action" method="post">
    <input type="text" name="query" value="<c:out value="${query}"/>"/><br>
    <input type="submit" value="Search">
</form>