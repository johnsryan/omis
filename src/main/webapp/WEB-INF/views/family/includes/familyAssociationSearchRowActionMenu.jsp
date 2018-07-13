<%--
  - Search results for family associations
  -
  - Author: Sheronda Vaughn
  --%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<fmt:setBundle var="familyBundle" basename="omis.family.msgs.family"/>
	<ul>
		<li>
			<c:choose>
				<c:when test="${familyAssociationExists}">
					<c:choose>
						<c:when test="${isOffender}">
							<a id="offenderProfileLink" class="offenderProfileLink newTab" href="/OMIS3/offender/profile.html?offender=${familyMember.id}" title="Goto Offender Profile"><fmt:message key="offenderProfileLinkLabel" bundle="${familyBundle}"/></a>
						</c:when>						
						<c:otherwise>
							<a class="viewEditLink" href="${pageContext.request.contextPath}/family/edit.html?familyAssociation=${familyAssociation.id}&amp;offender=${offender.id}" title="<fmt:message key='editRelationLink' bundle='${familyBundle}'/>"><span class="visibleLinkLabel"><fmt:message key='editRelationLink' bundle='${familyBundle}'/></span></a>
						</c:otherwise>
					</c:choose>
				</c:when>	
				<c:otherwise>
					<a class="createLink" href="${pageContext.request.contextPath}/family/create.html?familyMember=${familyMember.id}&amp;offender=${offender.id}" ><span class="invisibleLinkLabel"><fmt:message key="createFamilyAssociationLink" bundle="${familyBundle}"/></span></a>
				</c:otherwise>	
			</c:choose>
		</li>
	</ul>