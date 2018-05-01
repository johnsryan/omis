<%--
  - Search results for offender relationships
  -
  - Author: Sheronda Vaughn
  --%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<fmt:setBundle var="offenderRelationshipBundle" basename="omis.offenderrelationship.msgs.offenderRelationship"/>
	<ul>
		<li>
			<c:choose>
				<c:when test="${relationshipExist}">
					<a class="viewEditLink" href="${pageContext.request.contextPath}/offenderRelationship/update/edit.html?relationship=${relationship.id}" title="<fmt:message key='editOffenderRelationshipWithNewRelationLink' bundle='${offenderRelationshipBundle}'/>"><span class="visibleLinkLabel"><fmt:message key='editOffenderRelationshipWithNewRelationLink' bundle='${offenderRelationshipBundle}'/></span></a>
				</c:when>
				<c:otherwise>
					<a class="createLink" href="${pageContext.request.contextPath}/offenderRelationship/create.html?relation=${relation.id}&amp;offender=${offender.id}" title="<fmt:message key='createOffenderRelationshipLink' bundle='${offenderRelationshipBundle}'/>"><span class="invisibleLinkLabel"><fmt:message key='createOffenderRelationshipLink' bundle='${offenderRelationshipBundle}'/></span></a>
				</c:otherwise>
			</c:choose>
		</li>	
	</ul>