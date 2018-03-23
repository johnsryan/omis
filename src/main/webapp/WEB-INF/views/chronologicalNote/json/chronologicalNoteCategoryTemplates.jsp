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
  - Template text for chronological note group.
  -
  - Author: Stephen Abson
  --%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
{
	<c:choose>
		<c:when test="${not empty groupTemplates}">
			groupTemplates: [
				<c:forEach var="groupTemplate" items="${groupTemplates}" varStatus="status">
					 '<c:out value="${groupTemplate.text}"/>'<c:if test="${not status.last}">,</c:if>
				</c:forEach>
			],
		</c:when>
		<c:otherwise>categoryTemplates: null,</c:otherwise>
	</c:choose>
	categoryTemplate: <c:choose><c:when test="${not empty categoryTemplate}">'${categoryTemplate.text}'</c:when><c:otherwise>null</c:otherwise></c:choose>
}