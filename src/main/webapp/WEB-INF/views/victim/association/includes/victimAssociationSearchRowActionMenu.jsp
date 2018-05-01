<%--
  - Search results for family associations
  -
  - Author: Sheronda Vaughn
  --%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<fmt:bundle basename="omis.victim.msgs.victim">
	<ul>
		<li>
			<c:choose>
				<c:when test="${victimAssociationExists}">						
					<c:choose>
						<c:when test="${offenderYesNo}">
							<li><a id="offenderProfileLink" class="offenderProfileLink newTab" href="/OMIS3/offender/profile.html?offender=${victim.id}" title="Goto Offender Profile"><fmt:message key="offenderProfileLinkLabel"/></a></li>
						</c:when>
						<c:otherwise>
							<a class="viewEditLink" href="${pageContext.request.contextPath}/victim/association/edit.html?victimAssociation=${victimAssociation.id}"><fmt:message key="viewEditRelationshipLabel"/></a>
						</c:otherwise>
					</c:choose>	
				</c:when>
				<c:otherwise>
					<a class=" createLink" href="${pageContext.request.contextPath}/victim/association/create.html?victim=${victim.id}&amp;offender=${offender.id}" title="<fmt:message key='createVictimAssociationLink'/>"><span class="invisibleLinkLabel"><fmt:message key='createVictimAssociationLink'/></span></a>
				</c:otherwise>
			</c:choose>
		</li>
	</ul>
</fmt:bundle>