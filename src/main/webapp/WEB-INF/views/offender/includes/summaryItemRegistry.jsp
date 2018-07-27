<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<fmt:bundle basename="omis.offender.msgs.offenderHeader">
<div class="item scrollItem" id="${summaryItemRegistryTitle}_contentPane">
	<div class="itemsContainer">
		<c:forEach var="summaryItem" items="${summaryItemRegistry.items}" varStatus="i">
			<c:if test="${summaryItem.enabled}">
				<jsp:include page="${summaryItem.includedPageName}"/>
			</c:if>
		</c:forEach>
	</div>
<span class="itemTitle"><fmt:message key="${summaryItemRegistryTitle}Label"/></span>
</div>
</fmt:bundle>