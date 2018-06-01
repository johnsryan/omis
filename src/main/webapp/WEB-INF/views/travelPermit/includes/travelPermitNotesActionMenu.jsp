<!-- 
 - Author: Yidong Li
 - Version: 0.1.0 (May 29, 2018)
 - Since: OMIS 3.0
 -->
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<fmt:bundle basename="omis.travelpermit.msgs.travelPermit">
	<ul>
		<li>
			<a class="createLink" href="${pageContext.request.contextPath}/travelPermit/addTravelPermitNoteItem.html" id="addTravelPermitNoteItemLink">
				<span class="visibleLinkLabel">
					<fmt:message key="createTravelPermitNotesLabel"/>
				</span>
			</a>
		</li>
	</ul>
	</body>
</fmt:bundle>