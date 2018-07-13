<%-- 
 - Author: Josh Divine
 - Version: 0.1.0 (Nov 30, 2017)
 - Since: OMIS 3.0
 --%>
 <%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib uri="/WEB-INF/tld/omis.tld" prefix="omis" %>
<fmt:setBundle basename="omis.msgs.common" var="commonBundle" />
<fmt:bundle basename="omis.paroleboarditinerary.msgs.paroleBoardItinerary">
	<ul>
		<sec:authorize access="hasRole('PAROLE_BOARD_ITINERARY_CREATE') or hasRole('ADMIN')">
			<c:if test="${empty paroleBoardItinerary}">
				<li>
					<a class="createLink" href="${pageContext.request.contextPath}/paroleBoardItinerary/create.html">
						<span class="visibleLinkLabel">
							<fmt:message key="createParoleBoardItineraryLink"/></span>
					</a>
				</li>
			</c:if>
		</sec:authorize>
		<sec:authorize access="hasRole('PAROLE_BOARD_ITINERARY_LIST') or hasRole('ADMIN')">
			<c:if test="${empty paroleBoardItinerary}">
				<li>
			      <omis:reportPro reportPath="/BOPP/Parole_Board_Itinerary_Listing" decorate="no" title="" className="newTab reportLink"><fmt:message key="boardItineraryListingReportLinkLabel"/></omis:reportPro>					
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
		<sec:authorize access="hasRole('BOARD_HEARING_CREATE') or hasRole('ADMIN')">
			<c:if test="${not empty paroleBoardItinerary}">
				<li>
					<a class="viewEditLink" href="${pageContext.request.contextPath}/boardHearing/schedule/edit.html?paroleBoardItinerary=${paroleBoardItinerary.id}">
						<span class="visibleLinkLabel">
							<fmt:message key="scheduleOffendersLink"/>
						</span>
					</a>
				</li>
			</c:if>
		</sec:authorize>
		
		<sec:authorize access="hasRole('PAROLE_BOARD_ITINERARY_REMOVE') or hasRole('ADMIN')">
			<c:if test="${not empty paroleBoardItinerary}">
				<li>
					<a class="removeLink" href="${pageContext.request.contextPath}/paroleBoardItinerary/remove.html?paroleBoardItinerary=${paroleBoardItinerary.id}">
						<span class="visibleLinkLabel">
							<fmt:message key="removeParoleBoardItineraryLink"/>
						</span>
					</a>
				</li>
			</c:if>
		</sec:authorize>
		<sec:authorize access="hasRole('PAROLE_BOARD_ITINERARY_LIST') or hasRole('ADMIN')">
			<c:if test="${not empty paroleBoardItinerary}">
				<li>
					<a href="${pageContext.request.contextPath}/paroleBoardItinerary/boardItineraryDetailsReport.html?paroleBoardItinerary=${paroleBoardItinerary.id}&reportFormat=PDF" class="newTab printLink"><fmt:message key="boardItineraryDetailsReportLinkLabel"/></a>
				</li>
			</c:if>
		</sec:authorize>		
	</ul>
</fmt:bundle>