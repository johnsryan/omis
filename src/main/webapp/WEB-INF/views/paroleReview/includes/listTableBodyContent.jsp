<%--
 - OMIS - Offender Management Information System
 - Copyright (C) 2011 - 2017 State of Montana
 -
 - This program is free software: you can redistribute it and/or modify
 - it under the terms of the GNU General Public License as published by
 - the Free Software Foundation, either version 3 of the License, or
 - (at your option) any later version.
 -
 - This program is distributed in the hope that it will be useful,
 - but WITHOUT ANY WARRANTY; without even the implied warranty of
 - MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 - GNU General Public License for more details.
 -
 - You should have received a copy of the GNU General Public License
 - along with this program.  If not, see <http://www.gnu.org/licenses/>.
--%>
<%--
 - Table body content of parole reviews.
 -
 - Author: Josh Divine
 - Version: 0.1.0 (Jan 29, 2018)
 - Since: OMIS 3.0
 --%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<fmt:setBundle basename="omis.msgs.common" var="commonBundle"/>
<fmt:bundle basename="omis.parolereview.msgs.paroleReview">
<c:forEach var="paroleReview" items="${paroleReviewSummaries}" varStatus="status">
	<tr>
		<td>
			<a class="actionMenuItem" id="rowActionMenuLink${status.index}" href="${pageContext.request.contextPath}/paroleReview/paroleReviewsActionMenu.html?paroleReview=${paroleReview.id}"></a>
		</td>
		<td><fmt:formatDate value="${paroleReview.date}" pattern="MM/dd/yyyy"/></td>
		<td><c:out value="${paroleReview.endorsementName}"/></td>
		<td>
			<c:out value="${paroleReview.staffMemberLastName}, ${paroleReview.staffMemberFirstName}"/>
			<c:if test="${not empty paroleReview.staffMemberMiddleName}">
				<c:out value=" ${paroleReview.staffMemberMiddleName}"/>
			</c:if>
			<c:if test="${not empty paroleReview.staffTitle}">
				<c:out value=" ${paroleReview.staffTitle}"/>
			</c:if>
			<c:out value=" (${paroleReview.staffCategoryName})"/>
		</td>
	</tr>
</c:forEach>
</fmt:bundle>