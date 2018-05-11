<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib uri="/WEB-INF/tld/omis.tld" prefix="omis" %>
<fmt:setBundle basename="omis.msgs.common" var="commonBundle"/>
<fmt:bundle basename="omis.warrant.msgs.warrant">
	<ul>
		<sec:authorize access="hasRole('WARRANT_VIEW') or hasRole('ADMIN')">
			<li>
				<a class="viewEditLink" href="${pageContext.request.contextPath}/warrant/edit.html?warrant=${warrant.id}"><span class="visibleLinkLabel"><fmt:message key="viewEditLink" bundle="${commonBundle}"/></span></a>
			</li>
		</sec:authorize>
		<c:if test="${empty warrantRelease}">
			<sec:authorize access="hasRole('WARRANT_VIEW') or hasRole('ADMIN')">
				<li>
					<a class="viewEditLink" href="${pageContext.request.contextPath}/warrant/cancel.html?warrant=${warrant.id}"><span class="visibleLinkLabel"><fmt:message key="cancelLink" /></span></a>
				</li>
			</sec:authorize>
		</c:if>
		<c:if test="${empty warrantCancellation and not empty warrantArrest}">
			<sec:authorize access="hasRole('WARRANT_VIEW') or hasRole('ADMIN')">
				<li>
					<a class="viewEditLink" href="${pageContext.request.contextPath}/warrant/release.html?warrant=${warrant.id}"><span class="visibleLinkLabel"><fmt:message key="releaseLink" /></span></a>
				</li>
			</sec:authorize>
		</c:if>
		<sec:authorize access="hasRole('WARRANT_VIEW') or hasRole('ADMIN')">
			<c:if test="${not empty warrantCancellation or not empty warrantArrest}">
			<li>
			<omis:reportPro reportPath="/Compliance/Warrants/Authorization_to_Cancel_Warrant_Pick_Up_Hold&WARRANT_ID=${warrant.id}" decorate="no" title="" className="newTab reportLink"><fmt:message key="AuthCancelWarrantReportLinkLabel"/></omis:reportPro>
			</li>
		    </c:if>		
			<c:if test="${not empty warrant}">
			<li>
				<a href="${pageContext.request.contextPath}/warrant/authPickUpHoldReport.html?offender=${warrant.id}&reportFormat=DOCX" class="reportLink"><fmt:message key="authPickUpHoldReportLinkLabel"/></a>
			</li>
		    </c:if>
		    <c:if test="${not empty warrantCancellation or not empty warrantArrest}">
			<li>
				<a href="${pageContext.request.contextPath}/warrant/warrantCancellationDetailsReport.html?offender=${warrant.id}&reportFormat=PDF" class="newTab reportLink"><fmt:message key="warrantCancellationDetailsReportLinkLabel"/></a>
			</li>
		    </c:if>			    
			<c:if test="${not empty warrant}">
			<li>
				<a href="${pageContext.request.contextPath}/warrant/warrantDetailsReport.html?offender=${warrant.id}&reportFormat=PDF" class="newTab reportLink"><fmt:message key="warrantDetailsReportLinkLabel"/></a>
			</li>
		    </c:if>	
			<c:if test="${not empty warrant}">
			<li>
				<a href="${pageContext.request.contextPath}/warrant/warrantToArrestReport.html?offender=${warrant.id}&reportFormat=DOCX" class="reportLink"><fmt:message key="warrantToArrestReportLinkLabel"/></a>
			</li>
		    </c:if>	
			<c:if test="${not empty warrant}">
			<li>
				<a href="${pageContext.request.contextPath}/warrant/warrantToArrestIscReport.html?offender=${warrant.id}&reportFormat=DOCX" class="reportLink"><fmt:message key="warrantToArrestIscReportLinkLabel"/></a>
			</li>
		    </c:if>			    		    	    
		</sec:authorize>
	</ul>
</fmt:bundle>