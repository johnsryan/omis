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
 - Author: Stephen Abson
 - Author: Ryan Johns
 - Author: Sheronda Vaughn
 - Author: Josh Divine
 - Version: 0.1.2 (Apr 17, 2018)
 - Since: OMIS 3.0
 --%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<fmt:setBundle basename="omis.offender.msgs.offender" var="offenderBundle"/>
<fmt:bundle basename="omis.msgs.homeLinks">
		<h1 class="foregroundUltraLight">
			<a class="actionMenuItem" id="searchActionMenu" href="${pageContext.request.contextPath}/searchActionMenu.html"></a>
			<span class="contentHeading"><fmt:message key="searchHeader"/></span>
		</h1>
		<ul class="content foregroundUltraLight">		
			<li>
				<a href="${pageContext.request.contextPath}/offender/advancedOffenderSearch.html">
					<span class="banner">
						<img src="${pageContext.request.contextPath}/resources/common/images/offenderSearch.png" height="90" width="90"/>
						<span class="label"><fmt:message key="offenderSearchLabel"/></span>
					</span>
				</a>
			</li>
			<li>
				<a href="${pageContext.request.contextPath}/staffSearch/staffSearch.html">
					<span class="banner">
						<img src="${pageContext.request.contextPath}/resources/common/images/staffSearch.png" height="90" width="90"/>
						<span class="label"><fmt:message key="staffSearchLabel"/></span>
					</span>										
				</a>				
			</li>
			<sec:authorize access="(hasRole('ADMIN') and hasRole('APP_DEV'))">
			<li>
				<a href="${pageContext.request.contextPath}/employer/employerSearch.html">
					<span class="banner">
						<img src="${pageContext.request.contextPath}/resources/common/images/employerSearch.png" height="90" width="90"/>
						<span class="label"><fmt:message key="employerSearchLabel"/></span>
					</span>
				</a>
			</li>
			</sec:authorize>
			<sec:authorize access="(hasRole('ADMIN') and hasRole('APP_DEV'))">
			<li>
				<a href="${pageContext.request.contextPath}/health/provider/providerSearch.html">
					<span class="banner">
						<img src="${pageContext.request.contextPath}/resources/common/images/serviceProviderSearch.png" height="90" width="90"/>
						<span class="label"><fmt:message key="serviceProviderSearchLabel"/></span>
					</span>
				</a>
			</li>
			</sec:authorize>
			<sec:authorize access="(hasRole('ADMIN') and hasRole('APP_DEV'))">
			<li>
				<a href="${pageContext.request.contextPath}/residence/residenceSearch.html">
					<span class="banner">
						<img src="${pageContext.request.contextPath}/resources/common/images/addressSearch.png" height="90" width="90"/>
						<span class="label"><fmt:message key="addressPhoneSearchLabel"/></span>
					</span>					
				</a>
			</li>
			</sec:authorize>			
		</ul>
		<h1 class="foregroundUltraLight">
			<a class="actionMenuItem" id="caseLoadActionMenu" href="${pageContext.request.contextPath}/caseLoadActionMenu.html"></a>
			<span class="contentHeading"><fmt:message key="caseLoadHeader"/></span>
		</h1>
		<ul class="content foregroundUltraLight">
			<sec:authorize access="(hasRole('ADMIN') and hasRole('APP_DEV'))">
			<li>
				<a href="${pageContext.request.contextPath}/caseload/list.html">								
					<span class="banner">
						<img src="${pageContext.request.contextPath}/resources/common/images/myCaseloads.png" height="90" width="90"/>
						<span class="label"><fmt:message key="myCaseloadsLabel"/></span>
					</span>
				</a>
			</li>			
			</sec:authorize>
			<%-- <sec:authorize access="(hasRole('ADMIN') and hasRole('APP_DEV'))">
			<li>
				<a href="#">
					<span class="banner">
						<img src="${pageContext.request.contextPath}/resources/common/images/caseloadAnalytics.png" height="90" width="90"/>
						<span class="label"><fmt:message key="caseloadAnalyticsHealth"/></span>
					</span>
				</a>
			</li>
			</sec:authorize> --%>
			<%-- <sec:authorize access="(hasRole('ADMIN') and hasRole('APP_DEV'))">
			<li>
				<a href="#">
					<span class="banner">
						<img src="${pageContext.request.contextPath}/resources/common/images/caseloadAdministration.png" height="90" width="90"/>
						<span class="label"><fmt:message key="caseloadAdministration"/></span>
					</span>
				</a>
			</li>
			</sec:authorize> --%>
			<%-- <sec:authorize access="(hasRole('ADMIN') and hasRole('APP_DEV'))">
			
					<span class="banner">
						<img src="${pageContext.request.contextPath}/resources/common/images/administrativeCaseloads.png" height="90" width="90"/>
						<span class="label"><fmt:message key="administrativeCaseloads"/></span>
					</span>
			</sec:authorize> --%>
			<%-- <sec:authorize access="(hasRole('ADMIN') and hasRole('APP_DEV'))">
			<li>
				<a href="${pageContext.request.contextPath}/caseload/list.html">	
			
				<a href="${pageContext.request.contextPath}/caseLoad/administrative/create.html">
				<a href="${pageContext.request.contextPath}/caseLoad/supervisory/create.html">
					<span class="banner">
						<img src="${pageContext.request.contextPath}/resources/common/images/supervisoryCaseloads.png" height="90" width="90"/>
						<span class="label">Supervisory Caseloads</span>
					</span>
				</a>
			</li>
			</sec:authorize> --%>
		</ul>
		<h1 class="foregroundUltraLight">
			<a class="actionMenuItem" id="workCentersActionMenu" href="${pageContext.request.contextPath}/workCentersActionMenu.html"></a>
			<span class="contentHeading"><fmt:message key="workCentersHeader"/></span>
		</h1>
		<ul class="content foregroundUltraLight">
			<li>
				<a href="${pageContext.request.contextPath}/health/referral/referralCenter.html">
					<span class="banner">
						<img src="${pageContext.request.contextPath}/resources/common/images/medicalCenter.png" height="90" width="90"/>
						<span class="label"><fmt:message key="facilityHealthReferralCenter"/></span>
					</span>
				</a>
			</li>
			<li>
				<a href="${pageContext.request.contextPath}/bopp/home.html">
					<span class="banner">
						<!-- <img src="${pageContext.request.contextPath}/resources/common/images/??.png" height="90" width="90"/> -->
						<span class="label"><fmt:message key="boardOfPardonsAndParole"/></span>
					</span>
				</a>
			</li>
			<li>
				<a href="${pageContext.request.contextPath}/presentenceInvestigation/request/list.html">
					<span class="banner">
						<!-- <img src="${pageContext.request.contextPath}/resources/common/images/??.png" height="90" width="90"/>-->
						<span class="label"><fmt:message key="presentenceInvestigations"/></span>
					</span>
				</a>
			</li>
			<sec:authorize access="(hasRole('ADMIN') or hasRole('VISIT_LIST'))">
			<li>
				<a href="${pageContext.request.contextPath}/visitation/facility/list.html">
					<span class="banner">
						<img src="${pageContext.request.contextPath}/resources/common/images/visitationCenter.png" height="90" width="90"/>
						<span class="label"><fmt:message key="facilityVisitationCenter"/></span>
					</span>
				</a>
			</li>
			</sec:authorize>
			<sec:authorize access="(hasRole('INCIDENT_REPORT_LIST') or hasRole('ADMIN'))">
			<li>
				<a href="${pageContext.request.contextPath}/incident/statement/list.html">
					<span class="banner">
						<img src="${pageContext.request.contextPath}/resources/common/images/incidentManagementSystem.png" height="90" width="90"/>
						<span class="label"><fmt:message key="correctionsIMS"/></span>
						
					</span>
				</a>
			</li>	
			</sec:authorize>	
			<sec:authorize access="(hasRole('ADMIN') and hasRole('APP_DEV'))">	
			<li>
				<a href="#">
					<span class="banner">
						<img src="${pageContext.request.contextPath}/resources/common/images/legalRecordsCenter.png" height="90" width="90"/>
						<span class="label"><fmt:message key="offenderLegalCenter"/></span>
					</span>
				</a>
			</li>
			</sec:authorize>
			<sec:authorize access="(hasRole('ADMIN') and hasRole('APP_DEV'))">
			<li>
				<a href="${pageContext.request.contextPath}/placement/home.html">
					<span class="banner">
						<img src="${pageContext.request.contextPath}/resources/common/images/offenderPlacementCenter.png" height="90" width="90"/>
						<span class="label"><fmt:message key="offenderPlacementCenter"/></span>
					</span>
				</a>
			</li>
			</sec:authorize>			
		</ul>
</fmt:bundle>