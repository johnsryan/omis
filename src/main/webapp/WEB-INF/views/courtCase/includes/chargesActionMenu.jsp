<!-- 
 - Author: Josh Divine
 - Version: 0.1.0 (Aug 15, 2017)
 - Since: OMIS 3.0
 -->
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<fmt:bundle basename="omis.courtcase.msgs.courtCase">
	<ul>
		<%-- Left as place holder for future refactor 
		<sec:authorize access="hasRole('CHARGE_CREATE') or hasRole('ADMIN')">
			<c:if test="${not empty defendant}">
				<li>
					<a class="createLink" href="${pageContext.request.contextPath}/courtCase/createCharge.html?defendant=${defendant.id}">
						<span class="visibleLinkLabel">
							<fmt:message key="createChargeLink"/>
						</span>
					</a>
				</li>
			</c:if>
		</sec:authorize> --%>
		<sec:authorize access="hasRole('CHARGE_LIST') or hasRole('ADMIN')">
			<c:if test="${not empty defendant}">
				<li>
					<a href="${pageContext.request.contextPath}/courtCase/chargeListingReport.html?defendant=${defendant.id}&reportFormat=PDF" class="newTab printLink"><fmt:message key="chargeListingReportLinkLabel"/></a>
				</li>
			</c:if>
		</sec:authorize>
		<sec:authorize access="hasRole('CHARGE_LIST') or hasRole('ADMIN')">
			<c:if test="${not empty charge}">
				<li>
					<a class="viewEditLink" href="${pageContext.request.contextPath}/courtCase/editCharge.html?charge=${charge.id}">
						<fmt:message key="viewEditChargeLink"/>
					</a>
				</li>
			</c:if>
		</sec:authorize>	
		<sec:authorize access="hasRole('CHARGE_REMOVE') or hasRole('ADMIN')">
			<c:if test="${not empty charge}">
				<li>
					<a class="removeLink" href="${pageContext.request.contextPath}/courtCase/removeCharge.html?charge=${charge.id}">
						<fmt:message key="removeChargeLink"/>
					</a>
				</li>
			</c:if>
		</sec:authorize>
		<sec:authorize access="hasRole('CHARGE_LIST') or hasRole('ADMIN')">
			<c:if test="${not empty charge}">
				<li>
					<a href="${pageContext.request.contextPath}/courtCase/chargeDetailsReport.html?charge=${charge.id}&reportFormat=PDF" class="newTab printLink"><fmt:message key="chargeDetailsReportLinkLabel"/></a>
				</li>
			</c:if>
	</sec:authorize>
	</ul>
</fmt:bundle>