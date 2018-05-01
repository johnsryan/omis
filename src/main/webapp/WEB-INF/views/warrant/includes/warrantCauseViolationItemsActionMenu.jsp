<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<fmt:setBundle basename="omis.msgs.common" var="commonBundle" />
<fmt:bundle basename="omis.warrant.msgs.warrant">
	<ul>
		<li>
			<a id="createViolationToWitItemLink" class="createLink" href="${pageContext.request.contextPath}/warrant/createViolationToWitItem.html?offender=${offender.id}&&violationToWitItemIndex=${violationToWitItemIndex}"><fmt:message key="createViolationToWitLinkLabel"/></a>
		</li>
<%-- 		<sec:authorize access="hasRole('WARRANT_CREATE') or hasRole('WARRANT_EDIT') or hasRole('ADMIN')"> --%>
<%-- 			<c:forEach items="${courtCases}" var="courtCase" varStatus="i"> --%>
<!-- 				<li class="createWarrantCauseViolationItemLink"> -->
<%-- 					<a id="createWarrantCauseViolationItemLink${i.index}" class="createLink" href="${pageContext.request.contextPath}/warrant/createWarrantCauseViolationItem.html?warrantCauseViolationItemIndex=${warrantCauseViolationItemIndex}&courtCase=${courtCase.id}"> --%>
<!-- 						<span class="visibleLinkLabel"> -->
<%-- 							<fmt:message key="addConditionsLink"> --%>
<%-- 								<fmt:param value="${courtCase.docket.value}" /> --%>
<%-- 							</fmt:message> --%>
<!-- 						</span> -->
<%-- 						<input type="hidden" value="${courtCase.id}" /> --%>
<!-- 					</a> -->
<!-- 				</li> -->
<%-- 			</c:forEach> --%>
<%-- 		</sec:authorize> --%>
	</ul>
</fmt:bundle>