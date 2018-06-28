<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib uri="/WEB-INF/tld/omis.tld" prefix="omis" %>
<fmt:setBundle basename="omis.msgs.common" var="commonBundle"/>
<fmt:bundle basename="omis.warrant.msgs.warrant">
	<ul>
		<sec:authorize access="hasRole('WARRANT_VIEW') or hasRole('ADMIN')">
			<li>
				<a class="viewEditLink" href="${pageContext.request.contextPath}/warrant/edit.html?warrant=${warrant.id}"><span class="visibleLinkLabel"><fmt:message key="viewEditWarrantLink"/></span></a>
			</li>
		</sec:authorize>
		<c:if test="${empty warrantRelease and empty warrantArrest}">
			<sec:authorize access="hasRole('WARRANT_VIEW') or hasRole('ADMIN')">
				<li>
					<c:choose>
						<c:when test="${empty warrantCancellation}">
							<sec:authorize access="hasRole('WARRANT_CANCELLATION_CREATE') or hasRole('ADMIN')">
								<li>				
									<a class="createLink" href="${pageContext.request.contextPath}/warrant/createWarrantCancellation.html?warrant=${warrant.id}"><span class="visibleLinkLabel"><fmt:message key="cancelLink" /></span></a>
								</li>
							</sec:authorize>
						</c:when>
						<c:otherwise>
							<sec:authorize access="hasRole('WARRANT_CANCELLATION_VIEW') or hasRole('ADMIN')">
								<li>
									<a class="viewEditLink" href="${pageContext.request.contextPath}/warrant/editWarrantCancellation.html?warrantCancellation=${warrantCancellation.id}"><span class="visibleLinkLabel"><fmt:message key="viewEditCancelLink" /></span></a>
								</li>
							</sec:authorize>
						</c:otherwise>
					</c:choose>
				</li>
			</sec:authorize>
		</c:if>
		<c:if test="${empty warrantCancellation and not empty warrantArrest}">
			<c:choose>
				<c:when test="${empty warrantRelease}">
					<sec:authorize access="hasRole('WARRANT_RELEASE_CREATE') or hasRole('ADMIN')">
						<li>
							<a class="createLink" href="${pageContext.request.contextPath}/warrant/createWarrantRelease.html?warrant=${warrant.id}"><span class="visibleLinkLabel"><fmt:message key="releaseLink" /></span></a>
						</li>
					</sec:authorize>
				</c:when>
				<c:otherwise>
					<sec:authorize access="hasRole('WARRANT_RELEASE_VIEW') or hasRole('ADMIN')">
						<li>
							<a class="viewEditLink" href="${pageContext.request.contextPath}/warrant/editWarrantRelease.html?warrantRelease=${warrantRelease.id}"><span class="visibleLinkLabel"><fmt:message key="viewEditReleaseLink" /></span></a>
						</li>
					</sec:authorize>
				</c:otherwise>
			</c:choose>
		</c:if>
		<sec:authorize access="hasRole('WARRANT_REMOVE') or hasRole('ADMIN')">
			<li>
				<a class="removeLink" href="${pageContext.request.contextPath}/warrant/remove.html?warrant=${warrant.id}"><span class="visibleLinkLabel"><fmt:message key="removeWarrantLinkLabel"/></span></a>
			</li>
		</sec:authorize>
		<sec:authorize access="hasRole('WARRANT_VIEW') or hasRole('ADMIN')">
			<c:if test="${not empty warrantCancellation and warrant.warrantReason ne 'ARREST_INTERSTATE_COMPACT_OFFENDER'}">
			<li>
			<a href="${pageContext.request.contextPath}/warrant/authCancelWarrantReport.html?offender=${warrant.id}&reportFormat=DOCX" class="msWordReportLink"><fmt:message key="AuthCancelWarrantReportLinkLabel"/></a>
			</li>
		    </c:if>
			<c:if test="${not empty warrantCancellation and warrant.warrantReason eq 'ARREST_INTERSTATE_COMPACT_OFFENDER'}">
			<li>
			<a href="${pageContext.request.contextPath}/warrant/authCancelISCWarrantReport.html?offender=${warrant.id}&reportFormat=DOCX" class="msWordReportLink"><fmt:message key="AuthCancelISCWarrantReportLinkLabel"/></a>
			</li>
		    </c:if>		    		
			<c:if test="${not empty warrant and warrant.warrantReason eq 'AUTHORIZATION_TO_PICKUP_AND_HOLD_PROBATIONER'}">
			<li>
				<a href="${pageContext.request.contextPath}/warrant/authPickUpHoldReport.html?warrant=${warrant.id}&reportFormat=DOCX" class="msWordReportLink"><fmt:message key="authPickUpHoldReportLinkLabel"/></a>
			</li>
		    </c:if>
			<c:if test="${not empty warrantArrest}">
			<li>
				<a href="${pageContext.request.contextPath}/warrant/authReleaseOffenderReport.html?offender=${warrant.id}&reportFormat=DOCX" class="msWordReportLink"><fmt:message key="authReleaseOffenderReportLinkLabel"/></a>
			</li>
		    </c:if>		    	
			<c:if test="${not empty warrant and (warrant.warrantReason eq 'ARREST_PAROLEE' or warrant.warrantReason eq 'ARREST_CONDITIONAL_RELEASE_OFFENDER' or warrant.warrantReason eq 'ARREST_PAROLE_ABSCONDER' or warrant.warrantReason eq 'ARREST_CONDITIONAL_RELEASE_ABSCONDER')}">
			<li>
				<a href="${pageContext.request.contextPath}/warrant/warrantToArrestReport.html?offender=${warrant.id}&reportFormat=DOCX" class="msWordReportLink"><fmt:message key="warrantToArrestReportLinkLabel"/></a>
			</li>
		    </c:if>	
			<c:if test="${not empty warrant and warrant.warrantReason eq 'ARREST_INTERSTATE_COMPACT_OFFENDER'}">
			<li>
				<a href="${pageContext.request.contextPath}/warrant/warrantToArrestIscReport.html?offender=${warrant.id}&reportFormat=DOCX" class="msWordReportLink"><fmt:message key="warrantToArrestIscReportLinkLabel"/></a>
			</li>
		    </c:if>			    		    	    
		    <c:if test="${not empty warrantCancellation or not empty warrantArrest}">
			<li>
				<a href="${pageContext.request.contextPath}/warrant/warrantCancellationDetailsReport.html?offender=${warrant.id}&reportFormat=PDF" class="newTab reportLink"><fmt:message key="warrantCancellationDetailsReportLinkLabel"/></a>
			</li>
		    </c:if>			    
			<c:if test="${not empty warrant}">
			<li>
				<a href="${pageContext.request.contextPath}/warrant/warrantDetailsReport.html?offender=${warrant.id}&reportFormat=PDF" class="newTab printLink"><fmt:message key="warrantDetailsReportLinkLabel"/></a>
			</li>
		    </c:if>
	</sec:authorize>
	</ul>
</fmt:bundle>