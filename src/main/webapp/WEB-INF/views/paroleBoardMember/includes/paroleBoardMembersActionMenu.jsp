<%-- 
 - Author: Josh Divine
 - Version: 0.1.0 (Nov 9, 2017)
 - Since: OMIS 3.0
 --%>
 <%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib uri="/WEB-INF/tld/omis.tld" prefix="omis" %>
<fmt:setBundle basename="omis.msgs.common" var="commonBundle" />
<fmt:bundle basename="omis.paroleboardmember.msgs.paroleBoardMember">
	<ul>
		<sec:authorize access="hasRole('PAROLE_BOARD_MEMBER_CREATE') or hasRole('ADMIN')">
			<c:if test="${empty paroleBoardMember}">
				<li>
					<a class="createLink" href="${pageContext.request.contextPath}/paroleBoardMember/create.html">
						<span class="visibleLinkLabel">
							<fmt:message key="createParoleBoardMemberLink"/></span>
					</a>
				</li>
			</c:if>
		</sec:authorize>
		<sec:authorize access="hasRole('PAROLE_BOARD_MEMBER_LIST') or hasRole('ADMIN')">
			<c:if test="${empty paroleBoardMember}">
				<li>
					<omis:reportPro reportPath="/BOPP/Board_of_Pardons_and_Parole_Staff_Listing" decorate="no" title="" className="newTab reportLink"><fmt:message key="boardMemberListingReportLinkLabel"/></omis:reportPro>					
				</li>
			</c:if>
		</sec:authorize>		
		<sec:authorize access="hasRole('PAROLE_BOARD_MEMBER_VIEW') or hasRole('ADMIN')">
			<c:if test="${not empty paroleBoardMember}">
				<li>
					<a class="viewEditLink" href="${pageContext.request.contextPath}/paroleBoardMember/edit.html?paroleBoardMember=${paroleBoardMember.id}">
						<span class="visibleLinkLabel">
							<fmt:message key="viewEditParoleBoardMemberLink"/>
						</span>
					</a>
				</li>
			</c:if>
		</sec:authorize>
		<sec:authorize access="hasRole('PAROLE_BOARD_MEMBER_REMOVE') or hasRole('ADMIN')">
			<c:if test="${not empty paroleBoardMember}">
				<li>
					<a class="removeLink" href="${pageContext.request.contextPath}/paroleBoardMember/remove.html?paroleBoardMember=${paroleBoardMember.id}">
						<span class="visibleLinkLabel">
							<fmt:message key="removeParoleBoardMemberLink"/>
						</span>
					</a>
				</li>
			</c:if>
		</sec:authorize>
		<sec:authorize access="hasRole('PAROLE_BOARD_MEMBER_LIST') or hasRole('ADMIN')">
			<c:if test="${not empty paroleBoardMember}">
				<li>
					<a href="${pageContext.request.contextPath}/paroleBoardMember/boardMemberDetailsReport.html?paroleBoardMember=${paroleBoardMember.id}&reportFormat=PDF" class="newTab printLink"><fmt:message key="boardMemberDetailsReportLinkLabel"/></a>
				</li>
			</c:if>
		</sec:authorize>		
	</ul>
</fmt:bundle>