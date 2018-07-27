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
 - Author: Josh Divine
 - Version: 0.1.0 (July 17, 2018)
 - Since: OMIS 3.0
 --%>
 <%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib uri="/WEB-INF/tld/omis.tld" prefix="omis" %>
<fmt:setBundle basename="omis.msgs.common" var="commonBundle" />
<fmt:bundle basename="omis.paroleboarditinerary.msgs.paroleBoardItinerary">
	<ul>
		<sec:authorize access="hasRole('BOPP_VIEW') or hasRole('ADMIN')">
			<c:if test="${not empty paroleBoardItinerary}">
				<li>
					<a class="viewEditLink" href="${pageContext.request.contextPath}/bopp/home.html">
						<span class="visibleLinkLabel">
							<fmt:message key="boppHomeLink"/>
						</span>
					</a>
				</li>
			</c:if>
		</sec:authorize>
		<sec:authorize access="hasRole('PAROLE_BOARD_ITINERARY_VIEW') or hasRole('ADMIN')">
			<c:if test="${not empty paroleBoardItinerary}">
				<li>
					<a class="viewEditLink" href="${pageContext.request.contextPath}/paroleBoardItinerary/edit.html?paroleBoardItinerary=${paroleBoardItinerary.id}">
						<span class="visibleLinkLabel">
							<fmt:message key="viewEditParoleBoardItineraryLink"/>
						</span>
					</a>
				</li>
			</c:if>
		</sec:authorize>
	</ul>
</fmt:bundle>