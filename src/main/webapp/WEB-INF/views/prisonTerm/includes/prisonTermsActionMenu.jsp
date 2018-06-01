<!-- 
 - Author: Trevor Isles
 - Version: 0.1.0 (April 20, 2017)
 - Since: OMIS 3.0
 -->
 <%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<fmt:setBundle basename="omis.msgs.common" var="commonBundle" />
<fmt:bundle basename="omis.prisonterm.msgs.prisonTerm">
	<ul>
		<sec:authorize access="hasRole('PRISON_TERM_CREATE') or hasRole('ADMIN')">
			<c:if test="${not empty offender and empty prisonTerm}">
			<li>
			<a class="createLink" href="${pageContext.request.contextPath}/prisonTerm/create.html?offender=${offender.id}">
				<span class="visibleLinkLabel">
					<fmt:message key="createPrisonTermLink"/></span>
			</a>
			</li>
			</c:if>
		</sec:authorize>
		<sec:authorize access="hasRole('PRISON_TERM_LIST') or hasRole('ADMIN')">
			<c:if test="${not empty offender and empty prisonTerm}">
			<li>
				<a href="${pageContext.request.contextPath}/prisonTerm/prisonTermListingReport.html?offender=${offender.id}&reportFormat=PDF" class="newTab printLink"><fmt:message key="prisonTermListingReportLinkLabel"/></a>
			</li>
			</c:if>
		</sec:authorize>
		<sec:authorize access="hasRole('PRISON_TERM_EDIT') or hasRole('ADMIN')">
			<c:if test="${not empty prisonTerm}">
			<li>
			<a class="viewEditLink" href="${pageContext.request.contextPath}/prisonTerm/edit.html?prisonTerm=${prisonTerm.id}&offender=${offender.id}">
				<span class="visibleLinkLabel">
					<fmt:message key="viewEditPrisonTermLink"/>
				</span>
			</a>
			</li>
			</c:if>
		</sec:authorize>
		<sec:authorize access="hasRole('PRISON_TERM_REMOVE') or hasRole('ADMIN')">
			<c:if test="${not empty prisonTerm}">
			<li>
			<a class="removeLink" href="${pageContext.request.contextPath}/prisonTerm/remove.html?prisonTerm=${prisonTerm.id}&offender=${offender.id}">
				<span class="visibleLinkLabel">
					<fmt:message key="removePrisonTermLink"/>
				</span>
			</a>
			</li>
			</c:if>
		</sec:authorize>
		<sec:authorize access="hasRole('PRISON_TERM_LIST') or hasRole('ADMIN')">
			<c:if test="${not empty prisonTerm}">
			<li>
				<a href="${pageContext.request.contextPath}/prisonTerm/prisonTermDetailsReport.html?prisonTerm=${prisonTerm.id}&reportFormat=PDF" class="newTab printLink"><fmt:message key="prisonTermDetailsReportLinkLabel"/></a>
			</li>
			</c:if>
		</sec:authorize>
	</ul>
</fmt:bundle>