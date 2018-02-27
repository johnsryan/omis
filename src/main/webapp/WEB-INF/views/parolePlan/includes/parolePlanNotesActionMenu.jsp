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
 - Version: 0.1.0 (Feb 13, 2018)
 - Since: OMIS 3.0
 --%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<fmt:bundle basename="omis.paroleplan.msgs.parolePlan">
	<ul>
		<li>
			<a id="createParolePlanNoteLink" class="createLink" href="${pageContext.request.contextPath}/parolePlan/addParolePlanNote.html?parolePlanNoteIndex=${parolePlanNoteIndex}"><span class="visibleLinkLabel"><fmt:message key="addParolePlanNoteLink"/></span></a>
		</li>
	</ul>
</fmt:bundle>