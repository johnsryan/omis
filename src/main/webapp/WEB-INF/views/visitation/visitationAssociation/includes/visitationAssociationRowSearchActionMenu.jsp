<%--
  - Search results for family associations
  -
  - Author: Sheronda Vaughn
  --%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<fmt:bundle basename="omis.visitation.msgs.visitation">
	<ul>
		<li>
			<c:choose>			
					<c:when test="${visitationAssociationExists}">
						<c:choose>
							<c:when test="${isOffender}">
								<li><a id="offenderProfileLink" class="offenderProfileLink newTab" href="/OMIS3/offender/profile.html?offender=${visitor.id}" title="Goto Offender Profile"><fmt:message key="offenderProfileLinkLabel"/></a></li>
							</c:when>
							<c:otherwise>
								<a class="viewEditLink" href="${pageContext.request.contextPath}/visitation/edit.html?visitationAssociation=${visitationAssociation.id}"><fmt:message key="viewEditRelationshipLabel"/></a>
							</c:otherwise>
						</c:choose>
					</c:when>
					<c:otherwise>
						<a class="createLink" href="${pageContext.request.contextPath}/visitation/create.html?visitor=${visitor.id}&amp;offender=${offender.id}" ><span class="invisibleLinkLabel"><fmt:message key="createVisitationAssociationLink"/></span></a>
					</c:otherwise>
			</c:choose>
		</li>
	</ul>
</fmt:bundle>