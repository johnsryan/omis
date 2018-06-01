<!-- 
 - Author: Joel Norris
 - Version: 0.1.0 (May 05, 2015)
 - Since: OMIS 3.0
 -->
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib uri="/WEB-INF/tld/omis.tld" prefix="omis" %>
<fmt:setBundle basename="omis.msgs.common" var="commonBundle" />
<fmt:bundle basename="omis.specialneed.msgs.specialNeed">
	<ul>
		<sec:authorize access="hasRole('SPECIAL_NEED_CREATE') or hasRole('ADMIN')">
		<c:forEach items="${classifications}" var="classification" varStatus="status">
			<li>			
				<a class="createLink" href="${pageContext.request.contextPath}/specialNeed/create.html?offender=${offender.id}&amp;specialNeedClassification=${classification.id}">
				<span class="visibleLinkLabel">
				<fmt:message key="newSpecialNeedLabel">
					<fmt:param>
						${classification.name}
					</fmt:param>
				</fmt:message>
				</span>
				</a>					
			</li>
		</c:forEach>
		</sec:authorize>
		<sec:authorize access="hasRole('SPECIAL_NEED_LIST') or hasRole('ADMIN')">
			<c:if test="${not empty offender}">
			<li>
				<a href="${pageContext.request.contextPath}/specialNeed/specialNeedListingReport.html?offender=${offender.id}&reportFormat=PDF" class="newTab printLink"><fmt:message key="specialNeedListingReportLinkLabel"/></a>
			</li>
			</c:if>
		</sec:authorize>
	</ul>
</fmt:bundle>