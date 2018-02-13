<?xml version="1.0" encoding="UTF-8"?>
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
 - Modules screen.
 -
 - Author: Stephen Abson
 --%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html 
     PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
     "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<fmt:bundle basename="omis.offender.msgs.modules">
<head>
<title>
	<fmt:message key="modulesHeader"/>
	<jsp:include page="/WEB-INF/views/offender/includes/offenderNameSummary.jsp"/>
</title>
<jsp:include page="/WEB-INF/views/common/includes/headerOffenderListResources.jsp"/>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/offender/style/module.css"/>
</head>
<body>
<jsp:include page="includes/offenderHeader.jsp"/>
  <ul id="moduleLinks" class="taskLinks">
	<sec:authorize access="hasRole('ADA_ACCOMMODATION_LIST') or hasRole('ADMIN')">
	    <li class="moduleLink">
	   		<a href="${pageContext.request.contextPath}/adaAccommodation/list.html?offender=${offenderSummary.id}">
				<fmt:message key="accommodationLink"/>
			</a>
		</li>
	</sec:authorize> 
	<sec:authorize access="hasRole('ALERT_LIST') or hasRole('ADMIN')">
     	<li class="moduleLink">
  			<a href="${pageContext.request.contextPath}/alert/list.html?offender=${offenderSummary.id}">
   				<fmt:message key="alertLink"/></a>
	   	</li>
  	</sec:authorize>
  	<sec:authorize access="hasRole('APP_DEV')">
		<li class="moduleLink">
			<a href="${pageContext.request.contextPath}/bedPlacement/list.html?offender=${offenderSummary.id}">
				<fmt:message key="bedPlacementLink"/>
			</a>
		</li>
    </sec:authorize>
    <sec:authorize access="hasRole('CAUTION_LIST') or hasRole('ADMIN')">
     	<li class="moduleLink">
    		<a href="${pageContext.request.contextPath}/caution/list.html?offender=${offenderSummary.id}">
    			<fmt:message key="cautionLink"/></a>
	    </li>
    </sec:authorize>
    <sec:authorize access="hasRole('CHRONOLOGICAL_NOTE_LIST') or hasRole('ADMIN')">
     	<li class="moduleLink">
    		<a href="${pageContext.request.contextPath}/chronologicalNote/list.html?offender=${offenderSummary.id}">
    			<fmt:message key="chronologicalNoteLink"/></a>
	    </li>
    </sec:authorize>
    <sec:authorize access="hasRole('OFFENDER_CONTACT_LIST') or hasRole('ADMIN')">
     	<li class="moduleLink">
    		<a href="${pageContext.request.contextPath}/offenderContact/edit.html?offender=${offenderSummary.id}">
    			<fmt:message key="offenderContactLink"/></a>
	    </li>
    </sec:authorize>
    <sec:authorize access="hasRole('COMMIT_STATUS_LIST') or hasRole('ADMIN')">
     	<li class="moduleLink">
    		<a href="${pageContext.request.contextPath}/commitStatus/list.html?offender=${offenderSummary.id}">
    			<fmt:message key="commitStatusLink"/></a>
	    </li>
    </sec:authorize>
    <sec:authorize access="hasRole('CORRECTIONAL_STATUS_TERM_LIST') or hasRole('ADMIN')">
     	<li class="moduleLink">
    		<a href="${pageContext.request.contextPath}/supervision/correctionalStatusTerm/list.html?offender=${offenderSummary.id}">
    			<fmt:message key="correctionalStatusTermsLink"/></a>
	    </li>
    </sec:authorize>
    <sec:authorize access="hasRole('COURT_CASE_LIST') or hasRole('ADMIN')">
		<li class="moduleLink">
			<a href="${pageContext.request.contextPath}/courtCase/list.html?defendant=${offenderSummary.id}">
				<fmt:message key="courtCaseLink"/>
			</a>
		</li>
    </sec:authorize>
    <sec:authorize access="hasRole('COURT_CASE_CONDITION_LIST') or hasRole('ADMIN')">
		<li class="moduleLink">
			<a href="${pageContext.request.contextPath}/courtCaseCondition/list.html?offender=${offenderSummary.id}">
				<fmt:message key="courtCaseConditionLink"/>
			</a>
		</li>
    </sec:authorize>
    <sec:authorize access="hasRole('OFFENSE_TERM_LIST') or hasRole('ADMIN')">
    	<li class="moduleLink">
			<a href="${pageContext.request.contextPath}/offenseTerm/list.html?person=${offenderSummary.id}">
				<fmt:message key="offenseTermLink"/>
			</a>
		</li>
    </sec:authorize>
	<sec:authorize access="(hasRole('ADMIN') or hasRole('CRIMINAL_ASSOCIATION_LIST')) and hasRole('APP_DEV')">
    	<li class="moduleLink">
  			<a href="${pageContext.request.contextPath}/criminalAssociation/list.html?offender=${offenderSummary.id}">
   				<fmt:message key="criminalAssociatesLink"/></a>
	   	</li>
  	</sec:authorize>
  	<sec:authorize access="hasRole('OFFENSE_TERM_LIST') or hasRole('ADMIN')">
    	<li class="moduleLink">
			<a href="${pageContext.request.contextPath}/offenseTerm/listCurrentOffenses.html?person=${offenderSummary.id}">
				<fmt:message key="currentOffenseLink"/>
			</a>
		</li>
    </sec:authorize>
    <sec:authorize access="hasRole('CUSTODY_REVIEW_LIST') or hasRole('ADMIN')">
		<li class="moduleLink">
			<a href="${pageContext.request.contextPath}/custody/list.html?offender=${offenderSummary.id}">
				<fmt:message key="custodyReviewLink"/>
			</a>
		</li>
    </sec:authorize>
    <sec:authorize access="hasRole('OFFENDER_DEMOGRAPHICS_EDIT') or hasRole('ADMIN')">
  		<li class="moduleLink">
  		  <a href="${pageContext.request.contextPath}/offender/demographics/edit.html?offender=${offenderSummary.id}">
  		  	<fmt:message key="offenderDemographicsLink"/></a>
  		</li>
  	</sec:authorize>
    <sec:authorize access="(hasRole('DETAINER_NOTIFICATION_LIST') or hasRole('ADMIN')) and hasRole('APP_DEV')">
		<li class="moduleLink">
			<a href="${pageContext.request.contextPath}/detainerNotification/list.html?offender=${offenderSummary.id}">
				<fmt:message key="detainerNotificationLink"/>
			</a>
		</li>
    </sec:authorize>
    <sec:authorize access="hasRole('DNA_MODULE') or hasRole('ADMIN')">
		<li class="moduleLink">
			<a href="${pageContext.request.contextPath}/dna/list.html?offender=${offenderSummary.id}">
				<fmt:message key="dnaLink"/>
			</a>
		</li>
    </sec:authorize>
    <sec:authorize access="(hasRole('DOCUMENT_MODULE') or hasRole('ADMIN')) and hasRole('APP_DEV')">
    	<li class="moduleLink">
    		<a href="${pageContext.request.contextPath}/documents/profile.html?offender=${offenderSummary.id}">
    			<fmt:message key="documentLink"/>
    		</a>
    	</li>
    </sec:authorize>
    <sec:authorize access="hasRole('TRACKED_DOCUMENT_LIST') or hasRole('ADMIN')">
    	<li class="moduleLink">
    		<a href="${pageContext.request.contextPath}/trackedDocumentReport/list.html?offender=${offenderSummary.id}">
    			<fmt:message key="documentTrackingLink"/>
    		</a>
    	</li>
    </sec:authorize>
    <sec:authorize access="hasRole('ADMIN') or hasRole('EDUCATION_TERM_LIST')">
    	<li class="moduleLink">
    		<a href="${pageContext.request.contextPath}/education/list.html?offender=${offenderSummary.id}">
    			<fmt:message key="educationLink"/></a>
    	</li>
    </sec:authorize>
    <sec:authorize access="hasRole('EMPLOYMENT_TERM_LIST') or hasRole('ADMIN')">
    	<li class="moduleLink">
    		<a href="${pageContext.request.contextPath}/employment/list.html?offender=${offenderSummary.id}">
    			<fmt:message key="employmentLink"/></a>
    	</li>
    </sec:authorize>
    <sec:authorize access="(hasRole('ADMIN') or hasRole('FAMILY_MODULE')) and hasRole('APP_DEV')">
    	<li class="moduleLink">
    		<a href="${pageContext.request.contextPath}/family/list.html?offender=${offenderSummary.id}">
    			<fmt:message key="familyLink"/>
    		</a>
    	</li>
    </sec:authorize>
	<sec:authorize access="hasRole('FINANCIAL_PROFILE_VIEW') or hasRole('ADMIN')">
    	<li class="moduleLink">
    		<a href="${pageContext.request.contextPath}/financial/edit.html?offender=${offenderSummary.id}">
    			<fmt:message key="financialLink"/>
    		</a>
    	</li>
    </sec:authorize>
    <sec:authorize access="hasRole('OFFENDER_FLAG_VIEW') or hasRole('ADMIN')">
    	<li class="moduleLink">
    		<a href="${pageContext.request.contextPath}/offenderFlag/edit.html?offender=${offenderSummary.id}">
    			<fmt:message key="offenderFlagLink"/>
    		</a>
    	</li>
    </sec:authorize>
    <sec:authorize access="hasRole('ADMIN') or hasRole('GRIEVANCE_LIST')">
    	<li class="moduleLink">
    		<a href="${pageContext.request.contextPath}/grievance/listByOffender.html?offender=${offenderSummary.id}">
    			<fmt:message key="grievanceLink"/></a>
    	</li>
    </sec:authorize>
    <sec:authorize access="hasRole('ADMIN') or hasRole('HEARING_LIST')">
    	<li class="moduleLink">
    		<a href="${pageContext.request.contextPath}/hearing/list.html?offender=${offenderSummary.id}">
    			<fmt:message key="hearingsLink"/>
    		</a>
    	</li>
    </sec:authorize>
    <sec:authorize access="hasRole('IDENTIFICATION_NUMBER_LIST') or hasRole('ADMIN')">
    	<li class="moduleLink">
    		<a href="${pageContext.request.contextPath}/identificationNumber/list.html?offender=${offenderSummary.id}">
    			<fmt:message key="identificationNumberLink"/></a>
    	</li>
    </sec:authorize>
    <sec:authorize access="hasRole('OFFENDER_VIEW') or hasRole('ADMIN')">
    	<li class="moduleLink">
    		<a href="${pageContext.request.contextPath}/offender/personalDetails/edit.html?offender=${offenderSummary.id}">
    			<fmt:message key="personLink"/></a>
    	</li>
    </sec:authorize>
    <sec:authorize access="hasRole('LOCATION_TERM_LIST') or hasRole('ADMIN')">
     	<li class="moduleLink">
    		<a href="${pageContext.request.contextPath}/locationTerm/list.html?offender=${offenderSummary.id}">
    			<fmt:message key="locationTermsLink"/></a>
	    </li>
    </sec:authorize>
    <sec:authorize access="hasRole('MEDICAL_REVIEW_LIST') or hasRole('ADMIN')">
    	<li class="moduleLink">
    		<a href="${pageContext.request.contextPath}/medicalReview/list.html?offender=${offenderSummary.id}">
    			<fmt:message key="medicalReviewLink"/>
    		</a>
    	</li>
    </sec:authorize>
    <sec:authorize access="hasRole('MENTAL_HEALTH_REVIEW_LIST') or hasRole('ADMIN')">
    	<li class="moduleLink">
    		<a href="${pageContext.request.contextPath}/mentalHealthReview/list.html?offender=${offenderSummary.id}">
    			<fmt:message key="mentalHealthReviewLink"/></a>
    	</li>
    </sec:authorize>
    <sec:authorize access="hasRole('MILITARY_LIST') or hasRole('ADMIN')">
    	<li class="moduleLink">
    		<a href="${pageContext.request.contextPath}/military/list.html?offender=${offenderSummary.id}">
    			<fmt:message key="militaryLink"/>
    		</a>
    	</li>
    </sec:authorize>
        <sec:authorize access="hasRole('CITATION_LIST') or hasRole('ADMIN')">
     	<li class="moduleLink">
    		<a href="${pageContext.request.contextPath}/citation/list.html?offender=${offenderSummary.id}">
    			<fmt:message key="citationLink"/></a>
	    </li>
    </sec:authorize>
    <sec:authorize access="(hasRole('NEED_MODULE') or hasRole('ADMIN')) and hasRole('APP_DEV')">
    	<li class="moduleLink">
    		<a href="${pageContext.request.contextPath}/need/casePlanObjective/list.html?offender=${offenderSummary.id}">
    			<fmt:message key="needsLink"/>
    		</a>
    	</li>
    </sec:authorize>
    <sec:authorize access="hasRole('PAROLE_BOARD_CONDITION_LIST') or hasRole('ADMIN')">
    	<li class="moduleLink">
    		<a href="${pageContext.request.contextPath}/paroleBoardCondition/list.html?offender=${offenderSummary.id}">
    			<fmt:message key="paroleBoardConditionLink"/>
    		</a>
    	</li>
    </sec:authorize>
    <sec:authorize access="hasRole('PAROLE_ELIGIBILITY_LIST') or hasRole('ADMIN')">
    	<li class="moduleLink">
    		<a href="${pageContext.request.contextPath}/paroleEligibility/list.html?offender=${offenderSummary.id}">
    			<fmt:message key="paroleEligibilityLink"/>
    		</a>
    	</li>
    </sec:authorize>
    <sec:authorize access="hasRole('PAROLE_REVIEW_LIST') or hasRole('ADMIN')">
    	<li class="moduleLink">
    		<a href="${pageContext.request.contextPath}/paroleReview/list.html?offender=${offenderSummary.id}">
    			<fmt:message key="paroleReviewLink"/></a>
    	</li>
    </sec:authorize>
    <sec:authorize access="hasRole('OFFENDER_PHOTO_LIST') or hasRole('ADMIN')">
		<li class="moduleLink">
			<a href="${pageContext.request.contextPath}/offenderPhoto/list.html?offender=${offenderSummary.id}">
				<fmt:message key="offenderPhotosLink"/>
			</a>
		</li>
    </sec:authorize>
    <sec:authorize access="hasRole('PLACEMENT_PROFILE_VIEW') or hasRole('ADMIN')">
		<li class="moduleLink">
			<a href="${pageContext.request.contextPath}/placement/home.html?offender=${offenderSummary.id}">
				<fmt:message key="placementHomeLink"/>
			</a>
		</li>
    </sec:authorize>
    <sec:authorize access="hasRole('PLACEMENT_TERM_LIST') or hasRole('ADMIN')">
		<li class="moduleLink">
			<a href="${pageContext.request.contextPath}/supervision/placementTerm/list.html?offender=${offenderSummary.id}">
				<fmt:message key="placementTermsLink"/>
			</a>
		</li>
    </sec:authorize>
	<sec:authorize access="hasRole('OFFENDER_PROFILE_VIEW') or hasRole('ADMIN')">
		<li  class="moduleLink"><a href="${pageContext.request.contextPath}/offender/profile.html?offender=${offenderSummary.id}">
 			<fmt:message key="profileLink"/></a>
 		</li>
	</sec:authorize>
	<sec:authorize access="hasRole('PRISON_TERM_LIST') or hasRole('ADMIN')">
		<li class="moduleLink">
			<a href="${pageContext.request.contextPath}/prisonTerm/list.html?offender=${offenderSummary.id}">
				<fmt:message key="prisonTermLink"/>
			</a>
		</li>
	</sec:authorize>
	<sec:authorize access="hasRole('PROBATION_TERM_LIST') or hasRole('ADMIN')">
		<li class="moduleLink">
			<a href="${pageContext.request.contextPath}/probationTerm/list.html?offender=${offenderSummary.id}">
				<fmt:message key="probationTermLink"/>
			</a>
		</li>
	</sec:authorize>
	<sec:authorize access="hasRole('PROGRAM_PLACEMENT_LIST') or hasRole('ADMIN')">
		<li class="moduleLink">
			<a href="${pageContext.request.contextPath}/program/list.html?offender=${offenderSummary.id}">
				<fmt:message key="programPlacementLink"/>
			</a>
		</li>
	</sec:authorize>
    <sec:authorize access="(hasRole('ADMIN') or hasRole('RELATIONSHIPS_LIST')) and hasRole('APP_DEV')">
    	<li class="moduleLink">
    		<a href="${pageContext.request.contextPath}/offenderRelationship/list.html?offender=${offenderSummary.id}">
    			<fmt:message key="relationshipLink"/>
    		</a>
    	</li>
    </sec:authorize>
    <sec:authorize access="hasRole('RELIGIOUS_PREFERENCE_LIST') or hasRole('ADMIN')">
    	<li class="moduleLink">
    		<a href="${pageContext.request.contextPath}/religion/religiousPreference/list.html?offender=${offenderSummary.id}">
    			<fmt:message key="religiousPreferenceLink"/>
    		</a>
    	</li>
    </sec:authorize>
    <sec:authorize access="(hasRole('RESIDENCE_MODULE') or hasRole('ADMIN') and hasRole('APP_DEV')) and hasRole('APP_DEV')">
     	<li class="moduleLink">
  			<a href="${pageContext.request.contextPath}/residence/list.html?offender=${offenderSummary.id}">
   				<fmt:message key="residenceLink"/></a>
	   	</li>
	</sec:authorize>
	<sec:authorize access="hasRole('ADMIN') or hasRole('PHYSICAL_FEATURE_LIST')">
		<li class="moduleLink">
			<a href="${pageContext.request.contextPath}/physicalFeature/list.html?offender=${offenderSummary.id}">
				<fmt:message key="physicalFeatureLink"/>
			</a>
		</li>
    </sec:authorize>
	<sec:authorize access="hasRole('STG_LIST') or hasRole('ADMIN')">
    	<li class="moduleLink">
    		<a href="${pageContext.request.contextPath}/stg/list.html?offender=${offenderSummary.id}">
    			<fmt:message key="stgLink"/>
    		</a>
    	</li>
    </sec:authorize>
    <sec:authorize access="(hasRole('SEPARATION_NEED_MODULE') or hasRole('ADMIN')) and hasRole('APP_DEV')">
		<li class="moduleLink">
			<a href="${pageContext.request.contextPath}/separationNeed/list.html?offender=${offenderSummary.id}">
				<fmt:message key="separationNeedLink"/>
			</a>
		</li>
    </sec:authorize>
    <sec:authorize access="hasRole('SPECIAL_NEED_LIST') or hasRole('ADMIN')">
		<li class="moduleLink">
			<a href="${pageContext.request.contextPath}/specialNeed/list.html?offender=${offenderSummary.id}">
				<fmt:message key="specialManagementDesignationsLink"/>
			</a>
		</li>
    </sec:authorize>
    <sec:authorize access="hasRole('SUBSTANCE_TEST_LIST') or hasRole('ADMIN')">
		<li class="moduleLink">
			<a href="${pageContext.request.contextPath}/substanceTest/list.html?offender=${offenderSummary.id}">
				<fmt:message key="substanceTestLink"/>
			</a>
		</li>
    </sec:authorize>
    <sec:authorize access="hasRole('SUPERVISORY_ORGANIZATION_TERM_LIST') or hasRole('ADMIN')">
     	<li class="moduleLink">
    		<a href="${pageContext.request.contextPath}/supervision/supervisoryOrganizationTerm/list.html?offender=${offenderSummary.id}">
    			<fmt:message key="supervisoryOrganizationTermsLink"/></a>
	    </li>
    </sec:authorize>
    <sec:authorize access="(hasRole('SUPERVISION_FEE_MODULE') or hasRole('ADMIN') and hasRole('APP_DEV')) and hasRole('APP_DEV')">
     	<li class="moduleLink">
  			<a href="${pageContext.request.contextPath}/supervisionFee/list.html?offender=${offenderSummary.id}">
   				<fmt:message key="supervisionFeeLink"/></a>
	   	</li>
	</sec:authorize>
    <sec:authorize access="hasRole('TIER_DESIGNATION_LIST') or hasRole('ADMIN')">
    	<li class="moduleLink">
    		<a href="${pageContext.request.contextPath}/tierDesignation/list.html?offender=${offenderSummary.id}">
    			<fmt:message key="tierDesignationLink"/>
    		</a>
    	</li>
    </sec:authorize>
    <sec:authorize access="hasRole('USER_ACCOUNT_LIST') or hasRole('ADMIN')">
    	<li class="moduleLink">
    		<a href="${pageContext.request.contextPath}/user/admin/userAccount/list.html?user=${offenderSummary.id}">
    			<fmt:message key="userAccountLink"/></a>
    	</li>
    </sec:authorize>
    <sec:authorize access="hasRole('VEHICLE_LIST') or hasRole('ADMIN')">
    	<li class="moduleLink">
    		<a href="${pageContext.request.contextPath}/vehicle/list.html?offender=${offenderSummary.id}">
    			<fmt:message key="vehicleAssociationLink"/></a>
    	</li>
    </sec:authorize>
    <sec:authorize access="hasRole('VICTIM_ASSOCIATION_LIST') or hasRole('ADMIN')">
    	<li class="moduleLink">
    		<a href="${pageContext.request.contextPath}/victim/association/listByOffender.html?offender=${offenderSummary.id}">
    			<fmt:message key="victimAssociationLink"/>
    		</a>
    	</li>
    </sec:authorize>
    <sec:authorize access="hasRole('ADMIN') or hasRole('VIOLATION_EVENT_LIST')">
    	<li class="moduleLink">
    		<a href="${pageContext.request.contextPath}/violationEvent/list.html?offender=${offenderSummary.id}">
    			<fmt:message key="violationEventLink"/>
    		</a>
    	</li>
    </sec:authorize>
    <sec:authorize access="hasRole('APP_DEV')">
    	<li class="moduleLink">
    		<a href="${pageContext.request.contextPath}/visitation/list.html?offender=${offenderSummary.id}">
    			<fmt:message key="visitationLink"/>
    		</a>
    	</li>
    </sec:authorize>
    <sec:authorize access="hasRole('ADMIN') or hasRole('WARRANT_LIST')">
    	<li class="moduleLink">
    		<a href="${pageContext.request.contextPath}/warrant/list.html?offender=${offenderSummary.id}">
    			<fmt:message key="warrantsLink"/>
    		</a>
    	</li>
    </sec:authorize>
    <sec:authorize access="hasRole('ADMIN') or hasRole('WORK_ASSIGNMENT_LIST')">
    	<li class="moduleLink">
    		<a href="${pageContext.request.contextPath}/workAssignment/list.html?offender=${offenderSummary.id}">
    			<fmt:message key="workAssignmentLink"/>
    		</a>
    	</li>
    </sec:authorize>
    <sec:authorize access="hasRole('ADMIN') or hasRole('WORK_RESTRICTION_LIST')">
    	<li class="moduleLink">
    		<a href="${pageContext.request.contextPath}/workRestriction/list.html?offender=${offenderSummary.id}">
    			<fmt:message key="workRestrictionLink"/>
    		</a>
    	</li>
    </sec:authorize>
    </ul>
</body>
</fmt:bundle>
</html>