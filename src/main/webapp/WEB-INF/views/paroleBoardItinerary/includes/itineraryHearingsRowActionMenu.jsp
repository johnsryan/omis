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
		<sec:authorize access="hasRole('PAROLE_ELIGIBILITY_VIEW') or hasRole('ADMIN')">
			<c:if test="${not empty eligibility}">
				<li>
					<a class="viewEditLink" href="${pageContext.request.contextPath}/paroleEligibility/edit.html?eligibility=${eligibility.id}">
						<span class="visibleLinkLabel">
							<fmt:message key="viewParoleEligibilityEditLink"/>
						</span>
					</a>
				</li>
			</c:if>
		</sec:authorize>
		<sec:authorize access="hasRole('HEARING_ANALYSIS_VIEW') or hasRole('ADMIN')">
			<c:if test="${not empty eligibility}">
				<li>
					<a class="viewEditLink" href="${pageContext.request.contextPath}/hearingAnalysis/edit.html?eligibility=${eligibility.id}">
						<span class="visibleLinkLabel">
							<fmt:message key="viewHearingAnalysisLink"/>
						</span>
					</a>
				</li>
			</c:if>
		</sec:authorize>
		<sec:authorize access="hasRole('HEARING_ANALYSIS_VIEW') or hasRole('ADMIN')">
			<c:if test="${not empty hearingAnalysis}">
				<li>
					<a class="viewEditLink" href="${pageContext.request.contextPath}/hearingAnalysis/home.html?hearingAnalysis=${hearingAnalysis.id}">
						<span class="visibleLinkLabel">
							<fmt:message key="workHearingAnalysisLink"/>
						</span>
					</a>
				</li>
			</c:if>
		</sec:authorize>	
		<sec:authorize access="hasRole('HEARING_ANALYSIS_EDIT') or hasRole('ADMIN')">
			<c:choose>
				<c:when test="${not empty boardHearing}">
					<li>
						<a class="viewEditLink" href="${pageContext.request.contextPath}/boardHearing/edit.html?boardHearing=${boardHearing.id}">
							<span class="visibleLinkLabel">
								<fmt:message key="editBoardHearingLink"/>
							</span>
						</a>
					</li>
				</c:when>
				<c:otherwise>
					<c:if test="${not empty eligibility}">
						<li>
							<a class="viewEditLink" href="${pageContext.request.contextPath}/boardHearing/create.html?paroleEligibility=${eligibility.id}">
								<span class="visibleLinkLabel">
									<fmt:message key="editBoardHearingLink"/>
								</span>
							</a>
						</li>
					</c:if>
				</c:otherwise>
			</c:choose>
		</sec:authorize>
		<sec:authorize access="hasRole('HEARING_PARTICIPANT_LIST') or hasRole('ADMIN')">
			<c:if test="${not empty boardHearing}">
				<li>
					<a class="viewEditLink" href="${pageContext.request.contextPath}/hearingParticipant/list.html?boardHearing=${boardHearing.id}">
						<span class="visibleLinkLabel">
							<fmt:message key="hearingParticipantsLink"/>
						</span>
					</a>
				</li>
			</c:if>
		</sec:authorize>
		<sec:authorize access="hasRole('BOARD_HEARING_DECISION_VIEW') or hasRole('ADMIN')">
			<c:if test="${not empty boardHearing}">
				<li>
					<a class="viewEditLink" href="${pageContext.request.contextPath}/boardHearingDecision/edit.html?boardHearing=${boardHearing.id}&redirectUrl=redirect:/paroleBoardItinerary/listHearings.html?paroleBoardItinerary=${paroleBoardItinerary.id}">
						<span class="visibleLinkLabel">
							<fmt:message key="adjudicateHearingLink"/>
						</span>
					</a>
				</li>
			</c:if>
		</sec:authorize>
	</ul>
</fmt:bundle>