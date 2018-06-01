<!-- 
 - Author: Sheronda Vaughn
 - Version: 0.1.0 (May 05, 2016)
 - Since: OMIS 3.0
 -->
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<fmt:setBundle basename="omis.offenderphoto.msgs.offenderPhoto" var="offenderPhotoBundle"/>
<fmt:setBundle basename="omis.msgs.common" var="commonBundle"/>
	<ul id="testing">
		<sec:authorize access="hasRole('OFFENDER_PHOTO_VIEW') or hasRole('ADMIN')">	
			<li>
				<a class="viewEditLink" href="${pageContext.request.contextPath}/offenderPhoto/edit.html?association=${association.id}" title="<fmt:message key='viewEditLink' bundle='${commonBundle}'/>"><fmt:message key="viewEditLink" bundle="${commonBundle}"/></a>
			</li>
		</sec:authorize>
		<sec:authorize access="hasRole('OFFENDER_PHOTO_REMOVE') or hasRole('ADMIN')">
			<li>	
				<a class="removeLink" href="${pageContext.request.contextPath}/offenderPhoto/remove.html?association=${association.id}" title="<fmt:message key='removeLink' bundle='${commonBundle}'/>"><span class="visibleLinkLabel"><fmt:message key="removeLink" bundle="${commonBundle}"/></span></a>
			</li>
		</sec:authorize>
		<c:if test="${not association.profile}">
			<sec:authorize access="hasRole('OFFENDER_PHOTO_EDIT') or hasRole('ADMIN')">
				<li>
					<a class="linkLink" href="${pageContext.request.contextPath}/offenderPhoto/setProfilePhoto.html?association=${association.id}"><span class="visibleLinkLabel"><fmt:message key="setAsProfilePhotoLink" bundle="${offenderPhotoBundle}"/></span></a>
				</li>
			</sec:authorize>
		</c:if>
		<sec:authorize access="hasRole('OFFENDER_PHOTO_VIEW') or hasRole('ADMIN')">
			<c:if test="${not empty association}">
			<li>
				<a href="${pageContext.request.contextPath}/offenderPhoto/offenderPhotosDetailsReport.html?association=${association.id}&reportFormat=PDF" class="newTab printLink"><fmt:message key="offenderPhotosDetailsReportLinkLabel" bundle="${offenderPhotoBundle}"/></a>
			</li>
			</c:if>
		</sec:authorize>
	</ul>