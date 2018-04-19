<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<fmt:setBundle basename="omis.msgs.common" var="commonBundle" />
<fmt:bundle basename="omis.warrant.msgs.warrant">
	<ul>
		<sec:authorize access="hasRole('WARRANT_CREATE') or hasRole('ADMIN')">
			<c:forEach items="${warrantReasonCategories}" var="category">
				<li>
					<a class="createLink" href="${pageContext.request.contextPath}/warrant/create.html?offender=${offender.id}&warrantReasonCategory=${category}">
						<span class="visibleLinkLabel">
							<fmt:message key="createLink.${category}" />
						</span>
					</a>
				</li>
			</c:forEach>
		</sec:authorize>
	</ul>
</fmt:bundle>