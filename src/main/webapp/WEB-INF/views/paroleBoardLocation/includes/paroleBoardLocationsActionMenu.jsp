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
 - Version: 0.1.0 (Feb 21, 2018)
 - Since: OMIS 3.0
 --%>
 <%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib uri="/WEB-INF/tld/omis.tld" prefix="omis" %>
<fmt:setBundle basename="omis.msgs.common" var="commonBundle" />
<fmt:bundle basename="omis.paroleboardlocation.msgs.paroleBoardLocation">
	<ul>
		<sec:authorize access="hasRole('PAROLE_BOARD_LOCATION_CREATE') or hasRole('ADMIN')">
			<c:if test="${empty paroleBoardLocation}">
				<li>
					<a class="createLink" href="${pageContext.request.contextPath}/paroleBoardLocation/create.html">
						<span class="visibleLinkLabel">
							<fmt:message key="createParoleBoardLocationLink"/></span>
					</a>
				</li>
			</c:if>
		</sec:authorize>
		<sec:authorize access="hasRole('PAROLE_BOARD_LOCATION_LIST') or hasRole('ADMIN')">
			<c:if test="${empty paroleBoardLocation}">
				<li>
					<omis:reportPro reportPath="/BOPP/Parole_Board_Location_Listing" decorate="no" title="" className="newTab reportLink"><fmt:message key="boardLocationListingReportLinkLabel"/></omis:reportPro>					
				</li>
			</c:if>
		</sec:authorize>		
		<sec:authorize access="hasRole('PAROLE_BOARD_LOCATION_VIEW') or hasRole('ADMIN')">
			<c:if test="${not empty paroleBoardLocation}">
				<li>
					<a class="viewEditLink" href="${pageContext.request.contextPath}/paroleBoardLocation/edit.html?paroleBoardLocation=${paroleBoardLocation.id}">
						<span class="visibleLinkLabel">
							<fmt:message key="viewEditParoleBoardLocationLink"/>
						</span>
					</a>
				</li>
			</c:if>
		</sec:authorize>
		<sec:authorize access="hasRole('PAROLE_BOARD_LOCATION_REMOVE') or hasRole('ADMIN')">
			<c:if test="${not empty paroleBoardLocation}">
				<li>
					<a class="removeLink" href="${pageContext.request.contextPath}/paroleBoardLocation/remove.html?paroleBoardLocation=${paroleBoardLocation.id}">
						<span class="visibleLinkLabel">
							<fmt:message key="removeParoleBoardLocationLink"/>
						</span>
					</a>
				</li>
			</c:if>
		</sec:authorize>
		<sec:authorize access="hasRole('PAROLE_BOARD_LOCATION_LIST') or hasRole('ADMIN')">
			<c:if test="${not empty paroleBoardLocation}">
				<li>
					<a href="${pageContext.request.contextPath}/paroleBoardLocation/boardLocationDetailsReport.html?paroleBoardLocation=${paroleBoardLocation.id}&reportFormat=PDF" class="newTab printLink"><fmt:message key="boardLocationDetailsReportLinkLabel"/></a>
				</li>
			</c:if>
		</sec:authorize>		
	</ul>
</fmt:bundle>