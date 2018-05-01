<%--
 - Action menu for screen that supports multiple pages (tabs, etc).
 -
 - Author: Ryan Johns
 - Author: Stephen Abson
 - Version: 0.1.0 (Sep 3, 2015)
 - Since: OMIS3.0 --%>
 <%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
 <%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
 <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
 <fmt:setBundle basename="omis.msgs.general" var="generalBundle"/>
 <fmt:setBundle basename="omis.offender.msgs.offender" var="offenderBundle"/>
 <fmt:bundle basename="omis.msgs.index">
 <ul>
 	<li>
		<span class="productName"><fmt:message key="productName" bundle="${generalBundle}"/></span>
		<span class="productVersion"><fmt:message key="productVersion" bundle="${generalBundle}"/></span>
	</li>
 	<li id="productName">
		<a id="aboutLink" class="infoLink iconLink newTab" href="${pageContext.request.contextPath}/about.html" title="<fmt:message key='aboutLink'/>"><span><fmt:message key="aboutLink"/></span></a>
	</li>
    <sec:authorize access="hasRole('ADMIN')">
    <li>
    	<a id="adminIndexLink" class="adminIndexLink iconLink newTab" href="${pageContext.request.contextPath}/admin/index.html" title="<fmt:message key='appAdminLink'/>"><span><fmt:message key="appAdminLink"/></span></a>
    </li>
    </sec:authorize>
    <sec:authorize access="hasRole('ADMIN') or hasRole('USER_ADMIN_INDEX_VIEW')">
    <li>
    	<a id="userAdminIndexLink" class="userAdminLink iconLink newTab" href="${pageContext.request.contextPath}/user/admin/index.html" title="<fmt:message key='userAdminIndexLink'/>"><span><fmt:message key="userAdminIndexLink"/></span></a>
    </li>
    </sec:authorize>
	<sec:authorize access="hasRole('OFFENDER_CREATE') or hasRole('ADMIN')">
		<li>
			<a id="createOffenderLink" class="newTab createLink" href="${pageContext.request.contextPath}/offender/create.html">
				<fmt:message key="createOffenderHeader" bundle="${offenderBundle}"/>
			</a>
		</li>
	</sec:authorize>
    <c:choose>
	    <c:when test="${not empty userPreference}">
	    	<c:set var="userPreferenceUrl" value="${pageContext.request.contextPath}/userPreference/edit.html?userPreference=${userPreference.id}"/>
	    </c:when>
	    <c:otherwise>
	    	<c:set var="userPreferenceUrl" value="${pageContext.request.contextPath}/userPreference/create.html?userAccount=${userAccount.id}"/>
	    </c:otherwise>
    </c:choose>
    <li>
    	<span class="userDetails">${userAccount.user.name.lastName}, ${userAccount.user.name.firstName} (<a href="${pageContext.request.contextPath}/j_spring_security_logout"><fmt:message key="logoutLink"/></a>) (<a class="newTab" href="${userPreferenceUrl}"><fmt:message key="userPreferencesLink"/></a>)</span>
    </li>
 </ul>
 </fmt:bundle>