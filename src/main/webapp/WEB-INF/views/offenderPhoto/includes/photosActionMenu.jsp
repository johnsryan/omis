<!-- 
 - Author: Sheronda Vaughn
 - Version: 0.1.0 (May 05, 2016)
 - Since: OMIS 3.0
 -->
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<fmt:setBundle basename="omis.msgs.common" var="commonBundle" />
<fmt:setBundle basename="omis.offenderphoto.msgs.offenderPhoto" var="offenderPhotoBundle"/>
	<ul>
			<sec:authorize access="hasRole('OFFENDER_PHOTO_CREATE') or hasRole('ADMIN')">
				<li>
					<a class="createLink" href="${pageContext.request.contextPath}/offenderPhoto/create.html?offender=${offender.id}&amp;profile=true">
						<fmt:message key="createProfilePhotoLink" bundle="${offenderPhotoBundle}"/></a>
				</li>
			</sec:authorize>
			<sec:authorize access="hasRole('OFFENDER_PHOTO_CREATE') or hasRole('ADMIN')">
				<li>
					<a class="createLink" href="${pageContext.request.contextPath}/offenderPhoto/create.html?offender=${offender.id}&amp;profile=false">
						<fmt:message key="createOffenderPhotoLink" bundle="${offenderPhotoBundle}"/></a>
				</li>
			</sec:authorize>
			<sec:authorize access="hasRole('OFFENDER_PHOTO_VIEW') or hasRole('ADMIN')">
			<c:if test="${not empty offender}">
			<li>
				<a href="${pageContext.request.contextPath}/offenderPhoto/offenderPhotosListingReport.html?offender=${offender.id}&reportFormat=PDF" class="newTab printLink"><fmt:message key="offenderPhotosListingReportLinkLabel" bundle="${offenderPhotoBundle}"/></a>
			</li>
			</c:if>
		</sec:authorize>
	</ul>