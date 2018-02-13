/*
 * OMIS - Offender Management Information System
 * Copyright (C) 2011 - 2017 State of Montana
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package omis.substancetest.web.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import omis.beans.factory.PropertyEditorFactory;
import omis.beans.factory.spring.CustomDateEditorFactory;
import omis.content.RequestContentMapping;
import omis.content.RequestContentType;
import omis.exception.DuplicateEntityFoundException;
import omis.offender.beans.factory.OffenderPropertyEditorFactory;
import omis.offender.domain.Offender;
import omis.offender.report.OffenderReportService;
import omis.offender.web.controller.delegate.OffenderSummaryModelDelegate;
import omis.person.domain.Person;
import omis.substance.domain.Substance;
import omis.substance.domain.SubstanceLab;
import omis.substance.report.SubstanceSummaryService;
import omis.substance.report.SubstanceTestSummary;
import omis.substance.validator.SubstanceTestFormValidator;
import omis.substance.web.form.LabResultItem;
import omis.substance.web.form.ResultItem;
import omis.substance.web.form.SubstanceTestForm;
import omis.substancetest.domain.LabResult;
import omis.substancetest.domain.SampleCollectionMethod;
import omis.substancetest.domain.SampleRequest;
import omis.substancetest.domain.SubstanceTest;
import omis.substancetest.domain.SubstanceTestReason;
import omis.substancetest.domain.SubstanceTestResult;
import omis.substancetest.domain.SubstanceTestResultValue;
import omis.substancetest.domain.SubstanceTestSample;
import omis.substancetest.domain.TestKit;
import omis.substancetest.domain.TestKitParameter;
import omis.substancetest.service.SubstanceTestService;
import omis.web.controller.delegate.BusinessExceptionHandlerDelegate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 * Substance Test Controller.
 * 
 * @author Joel Norris
 * @author Josh Divine
 * @version 0.1.1 (Feb 12, 2018)
 * @since OMIS 3.0
 */
@Controller
@RequestMapping("/substanceTest")
@PreAuthorize("hasRole('USER')")
public class SubstanceTestController {
	
	/* Redirect URLs. */
	
	private static final String SUBSTANCE_LIST_REDIRECT_URL = 
			"redirect:list.html?offender=";
	
	/* View names. */
	
	private static final String SUBSTANCE_LIST_VIEW_NAME = "substanceTest/list";
	
	private static final String EDIT_SUBSTANCE_TEST_VIEW_NAME 
		= "substanceTest/edit";
	
	private static final String LAB_CONTENT_VIEW_NAME 
		= "substanceTest/includes/labResultsContent";
	
	private static final String SUBSTANCE_TEST_RESULT_ROW_VIEW_NAME 
		= "/substanceTest/includes/substanceTestResultTableRow";
	
	private static final String SUBSTANCE_TEST_ADMIT_VIEW_NAME 
		= "/substanceTest/includes/admitBeforeTest";
	
	private static final String LAB_TABLE_BODY_VIEW_NAME
		= "/substanceTest/includes/labResultTableBody";
	
	private static final String LAB_RESULT_ROW_VIEW_NAME 
		= "/substanceTest/includes/labResultTableRow";
	
	private static final String SUBSTANCE_TESTS_ACTION_MENU_VIEW_NAME
		= "/substanceTest/includes/substanceTestsActionMenu";
	
	private static final String SUBSTANCE_TEST_ACTION_MENU_VIEW_NAME
		= "/substanceTest/includes/substanceTestActionMenu";
	
	private static final String SUBSTANCE_TESTS_ROW_ACTION_MENU_VIEW_NAME
	 	= "/substanceTest/includes/substanceTestsRowActionMenu";
	
	private static final String SUBSTANCE_TESTS_RESULTS_ACTION_MENU_VIEW_NAME
		= "/substanceTest/includes/substanceTestsResultsActionMenu";
	
	private static final String JUSTIFICATION_CONTENT_VIEW_NAME
		= "/substanceTest/includes/justificationContent";
	
	private static final String 
	SUBSTANCE_TESTS_VERIFICATION_RESULTS_ACTION_MENU_VIEW_NAME
		= "/substanceTest/includes/substanceTestsVerificationResultsActionMenu";
	
	/* Model keys. */
	
	private static final String OFFENDER_SUMMARY_MODEL_KEY = "offenderSummary";
	
	private static final String OFFENDER_NUMBER_MODEL_KEY = "offenderNumber";
	
	private static final String OFFENDER_MODEL_KEY = "offender";
	
	private static final String SUMMARIES_MODEL_KEY = "summaries";
	
	private static final String CURRENT_SUBSTANCE_TEST_RESULT_INDEX_MODEL_KEY 
		= "currentSubstanceTestResultIndex";
	
	private static final String CURRENT_LAB_RESULT_INDEX_MODEL_KEY 
		= "currentLabResultIndex";
	
	private static final String SUBSTANCE_TEST_SAMPLE_MODEL_KEY 
		= "substanceTestSample";
	
	private static final String SUBSTANCE_TEST_FORM_MODEL_KEY = 
			"substanceTestForm";
	
	private static final String SUBSTANCE_TEST_MODEL_KEY = "substanceTest";
	
	private static final String SUBSTANCE_TEST_SAMPLE_REQUEST_MODLE_KEY
		= "substanceTestRequest";
	
	private static final String RESULTS_MODEL_KEY = "results";
	
	private static final String SUBSTANCES_MODEL_KEY = "substances";
	
	private static final String SUBSTANCE_TEST_RESULT_INDEX_MODEL_KEY
		= "substanceTestResultIndex";
	
	private static final String LAB_RESULT_INDEX_MODEL_KEY 
		= "labResultIndex";
	
	private static final String NEW_CURRENT_INDEX_MODEL_KEY =
			"newCurrentIndex";
	
	private static final String SUBSTANCE_LABS_MODEL_KEY = "substanceLabs";
	
	private static final String PRIVATE_LAB_MODEL_KEY = "privateLab";
	
	private static final String EDIT_FORM_MODEL_KEY = "substanceTestForm";
	
	/* Message keys. */
	
	/* Message bundles. */

	/* Services. */
	
	@Autowired
	@Qualifier("substanceTestService")
	SubstanceTestService substanceTestService;
	
	@Autowired
	@Qualifier("substanceSummaryService")
	SubstanceSummaryService substanceSummaryService;
	
	@Autowired
	@Qualifier("offenderReportService")
    private OffenderReportService offenderReportService;
	
	/* Property editor factories. */
	
	@Autowired
	@Qualifier("personPropertyEditorFactory")
	private PropertyEditorFactory personPropertyEditorFactory;
	
	@Autowired
	private CustomDateEditorFactory customDateEditorFactory;
	
	@Autowired
	@Qualifier("sampleCollectionMethodPropertyEditorFactory")
	private PropertyEditorFactory sampleCollectionMethodPropertyEditorFactory;
	
	@Autowired
	@Qualifier("substanceTestPropertyEditorFactory")
	private PropertyEditorFactory substanceTestPropertyEditorFactory;

	@Autowired
	@Qualifier("substanceTestReasonPropertyEditorFactory")
	private PropertyEditorFactory substanceTestReasonPropertyEditorFactory;
	
	@Autowired
	@Qualifier("substanceTestResultPropertyEditorFactory")
	private PropertyEditorFactory substanceTestResultPropertyEditorFactory;
	
	@Autowired
	@Qualifier("substanceTestSamplePropertyEditorFactory")
	private PropertyEditorFactory substanceTestSamplePropertyEditorFactory;
	
	@Autowired
	@Qualifier("testKitPropertyEditorFactory")
	private PropertyEditorFactory testKitPropertyEditorFactory;
	
	@Autowired
	@Qualifier("testKitParameterPropertyEditorFactory")
	private PropertyEditorFactory testKitParameterPropertyEditorFactory;
	
	@Autowired
	@Qualifier("offenderPropertyEditorFactory")
	private OffenderPropertyEditorFactory offenderPropertyEditorFactory;
	
	@Autowired
	@Qualifier("substanceTestResultValuePropertyEditorFactory")
	private PropertyEditorFactory substanceTestResultValuePropertyEditorFactory;
	
	@Autowired
	@Qualifier("substancePropertyEditorFactory")
	private PropertyEditorFactory substancePropertyEditorFactory;
	
	@Autowired
	@Qualifier("labResultPropertyEditorFactory")
	private PropertyEditorFactory labResultPropertyEditorFactory;
	
	@Autowired
	@Qualifier("sampleRequestPropertyEditorFactory")
	private PropertyEditorFactory
	sampleRequestPropertyEditorFactory;
	
	@Autowired
	@Qualifier("substanceLabPropertyEditorFactory")
	private PropertyEditorFactory substanceLabPropertyEditorFactory;
	
	/* Validators. */

	@Autowired
	@Qualifier("substanceTestFormValidator")
	private SubstanceTestFormValidator substanceTestFormValidator;
	
	/* Helpers. */
	
	@Autowired
	@Qualifier("businessExceptionHandlerDelegate")
	private BusinessExceptionHandlerDelegate businessExceptionHandlerDelegate;
	
	@Autowired
	@Qualifier("offenderSummaryModelDelegate")
	private OffenderSummaryModelDelegate offenderSummaryModelDelegate;
	
	/* Constructors. */
	
	/**
	 * Instantiates a default instance of substance test controller.
	 */
	public SubstanceTestController() {
		//Default constructor.
	}
	
	/* URL mapped methods. */
	
	/**
	 * Listing screen to show all offender related substance material.
	 * 
	 * @param offender offender
	 * @return model and view
	 */
	@RequestContentMapping(nameKey = "substanceSummaryListScreenName",
			descriptionKey = "substanceSummaryListScreenDescription",
			messageBundle = "omis.substance.msgs.substance",
			screenType = RequestContentType.LISTING_SCREEN)
	@RequestMapping(value = "/list.html", method = RequestMethod.GET)
	@PreAuthorize("hasRole('SUBSTANCE_TEST_LIST') or hasRole('ADMIN')")
	public ModelAndView list(@RequestParam(value = "offender",
			required = true)final Offender offender) {
		ModelMap map = new ModelMap();
		List<SubstanceTestSummary> summaries = this.substanceSummaryService
				.summarizeSubstanceTestsByOffender(offender);
		map.addAttribute(OFFENDER_NUMBER_MODEL_KEY, 
				String.valueOf(offender.getOffenderNumber()));
		map.addAttribute(OFFENDER_MODEL_KEY, offender);
		this.offenderSummaryModelDelegate.add(map, offender);
		map.addAttribute(SUMMARIES_MODEL_KEY, summaries);
		return new ModelAndView(SUBSTANCE_LIST_VIEW_NAME, map);
	}
	
	/**
	 * Detail screen to present the substance test form for the purpose of
	 * creating a new substance test.
	 * 
	 * @param substanceTestSample substance test sample
	 * @return model and view
	 */
	@RequestContentMapping(nameKey = "substanceTestCreateScreenName",
			descriptionKey = "substanceTestCreateScreenDescription",
			messageBundle = "omis.substance.msgs.substance",
			screenType = RequestContentType.DETAIL_SCREEN)
	@RequestMapping(value = "/create.html", 
			method = RequestMethod.GET)
	@PreAuthorize("hasRole('SUBSTANCE_TEST_CREATE') or hasRole('ADMIN')")
	public ModelAndView create(@RequestParam(value = 
			"substanceTestSample", required = true)
				final SubstanceTestSample substanceTestSample) {
		ModelMap map = new ModelMap();
		SubstanceTestForm form = new SubstanceTestForm();
		List<TestKit> testKits = this.substanceTestService
				.findKitsBySampleCollectionMethod(
						substanceTestSample.getSampleCollectionMethod());
		int currentSubstanceTestResultIndex = 0;
		int currentLabResultIndex = 0;
		if (testKits != null && testKits.size() > 1) {
			throw new IllegalStateException(
					"More than 1 test kit found for " + substanceTestSample
					.getSampleCollectionMethod().getName() 
					+ " sample collection method");
		} else if(testKits != null && !testKits.isEmpty()) {
			List<TestKitParameter> parameters = this.substanceTestService
					.findParametersByTestKit(testKits.get(0));
				if (parameters.size() > 0) {
					List<ResultItem> resultItems = 
							new ArrayList<ResultItem>();
					int count = 0;
					for (TestKitParameter parameter : parameters) {
						ResultItem resultItem = new ResultItem();
						resultItem.setSubstance(parameter.getSubstance());
						resultItem.setProcess(true);
						resultItem.setDefaultResultValue(
								parameter.getDefaultResultValue());
						resultItems.add(count, resultItem);
						count++;
					}
					currentSubstanceTestResultIndex = parameters.size();
					form.setResultItems(resultItems);
				}
		}
		return this.prepareEditMav(map, substanceTestSample, form,
				currentSubstanceTestResultIndex, currentLabResultIndex);
	}
	
	/**
	 * Save a new substance test with values from the specified substance
	 * test form.
	 *  
	 * @param substanceTestSample substance test sample
	 * @param form substance test form
	 * @param result binding result
	 * @return model and view
	 * @throws DuplicateEntityFoundException thrown when a duplicate substance
	 * test, substance test result, or crime lab result is found.
	 */
	@RequestContentMapping(nameKey = "substanceTestSaveName",
			descriptionKey = "substanceTestSaveDescription",
			messageBundle = "omis.substance.msgs.substance",
			screenType = RequestContentType.FORM_SUBMIT)
	@RequestMapping(value = "/create.html", 
			method = RequestMethod.POST)
	@PreAuthorize("hasRole('SUBSTANCE_TEST_CREATE') or hasRole('ADMIN')")
	public ModelAndView save(@RequestParam(value = "substanceTestSample", 
			required = true) final SubstanceTestSample substanceTestSample, 
			final SubstanceTestForm form, final BindingResult result) 
		throws DuplicateEntityFoundException {
		this.substanceTestFormValidator.validate(form, result);
		if (result.hasErrors()) {
			ModelMap map = new ModelMap();
			int currentSubstanceTestResultIndex;
			if (form.getResultItems() == null) {
				currentSubstanceTestResultIndex = 0;
			} else {
				currentSubstanceTestResultIndex = form
						.getResultItems().size();
			}
			int currentLabResultIndex;
			if (form.getLabResultItems() == null) {
				currentLabResultIndex = 0;
			} else {
				currentLabResultIndex = form
						.getLabResultItems().size();
			}
			map.addAttribute(CURRENT_SUBSTANCE_TEST_RESULT_INDEX_MODEL_KEY, 
					currentSubstanceTestResultIndex);
			map.addAttribute(CURRENT_LAB_RESULT_INDEX_MODEL_KEY,
					currentLabResultIndex);
			map.addAttribute(OFFENDER_SUMMARY_MODEL_KEY, 
					this.offenderReportService.summarizeOffender(
							substanceTestSample.getOffender()));
			map.addAttribute(OFFENDER_MODEL_KEY,
					substanceTestSample.getOffender());
			map.addAttribute(SUBSTANCE_TEST_SAMPLE_MODEL_KEY, 
					substanceTestSample);
			map.addAttribute(BindingResult.MODEL_KEY_PREFIX
					+ SUBSTANCE_TEST_FORM_MODEL_KEY, result);
			return this.prepareEditMav(map, substanceTestSample, form,
					currentSubstanceTestResultIndex, 
					currentLabResultIndex);
		}
		SubstanceTest substanceTest = this.substanceTestService
				.create(substanceTestSample, 
				form.getResultDate(), form.getLabInvolved(),
				form.getLabResultDate(), form.getLabRequestDate(),
				form.getTestComment(), form.getLab(),
				form.getPrivateLabJustification(),
				form.getAuthorizingStaff());
		for (ResultItem formResult : form.getResultItems()) {
			if (formResult.getProcess() != null && formResult.getProcess()) {
				this.substanceTestService.addResult(substanceTest, 
						formResult.getSubstance(), 
						formResult.getSubstanceTestResultValue(), 
						formResult.getAdmit(), formResult.getAdmitPrior());
			}
		}
		for (LabResultItem clResult : form
				.getLabResultItems()) {
			if (clResult.getProcess() != null && clResult.getProcess()) {
				this.substanceTestService.addLabResult(substanceTest, 
						clResult.getSubstance(), 
						clResult.getSubstanceTestResultValue());
			}
		}
		return new ModelAndView(SUBSTANCE_LIST_REDIRECT_URL 
				+ substanceTestSample.getOffender().getId());
	}
	
	/**
	 * Detail screen to to present the substance test form for the purpose
	 * of editing the specified substance test.
	 * 
	 * @param substanceTest substance test
	 * @return model and view
	 */
	@RequestContentMapping(nameKey = "substanceTestEditScreenName",
			descriptionKey = "substanceTestEditScreenDescription",
			messageBundle = "omis.substance.msgs.substance",
			screenType = RequestContentType.DETAIL_SCREEN)
	@RequestMapping(value = "/edit.html", 
			method = RequestMethod.GET)
	@PreAuthorize("hasRole('SUBSTANCE_TEST_EDIT') or hasRole('ADMIN')")
	public ModelAndView edit(@RequestParam(value = "substanceTest", 
			required = true) final SubstanceTest substanceTest) {
		ModelMap map = new ModelMap();
		SubstanceTestForm form = new SubstanceTestForm();
		form.setResultDate(substanceTest.getResultDate());
		form.setTestComment(substanceTest.getTestComment());
		form.setLabInvolved(substanceTest.getLabInvolved());
		form.setLabResultDate(substanceTest.getLabResultDate());
		form.setLabRequestDate(substanceTest.getLabRequestDate());
		form.setLab(substanceTest.getLab());
		form.setAuthorizingStaff(substanceTest.getAuthorizingStaff());
		form.setPrivateLabJustification(
				substanceTest.getPrivateLabJustification());
		int currentLabResultIndex = 0; 
		int currentSubstanceTestResultIndex = 0; 
		List<SubstanceTestResult> substanceTestResults = this
				.substanceTestService.findResultsBySubstanceTest(substanceTest);
		List<ResultItem> resultItems = new ArrayList<ResultItem>();
		if (substanceTestResults.size() != 0) {
			currentSubstanceTestResultIndex = substanceTestResults.size();
			int count = 0;
			for (SubstanceTestResult result : substanceTestResults) {
				ResultItem resultItem = new ResultItem();
				resultItem.setSubstance(result.getSubstance());
				resultItem.setProcess(true);
				resultItem.setSubstanceTestResultValue(result
						.getValue());
				resultItem.setAdmit(result.getSubstanceUseAdmission());
				resultItem.setAdmitPrior(result.getAdmissionPriorToTest());
				resultItems.add(count, resultItem);
				count++;
			}
			form.setResultItems(resultItems);
		}
		List<LabResult> labResults = this
				.substanceTestService.findLabResultsBySubstanceTest(
						substanceTest);
		List<LabResultItem> labResultItems = 
				new ArrayList<LabResultItem>();
		if (labResults.size() != 0) {
			currentLabResultIndex = labResults.size();
			int count = 0;
			for (LabResult clResult : labResults) {
				LabResultItem labResultItem = 
						new LabResultItem();
				labResultItem.setProcess(true);
				labResultItem.setSubstance(clResult.getSubstance());
				labResultItem.setSubstanceTestResultValue(clResult
						.getValue());
				labResultItems.add(count, labResultItem);
				count++;
			}
			form.setLabResultItems(labResultItems);
		}
		map.addAttribute(SUBSTANCE_TEST_MODEL_KEY, substanceTest);
		return this.prepareEditMav(map, substanceTest.getSubstanceTestSample(), 
				form, currentSubstanceTestResultIndex, 
				currentLabResultIndex);
	}
	
	/**
	 * Update the specified substance test with values from the specified
	 * substance test form.
	 * 
	 * @param substanceTest substance test
	 * @param form substance test form
	 * @param result binding result
	 * @return model and view
	 * @throws DuplicateEntityFoundException thrown when a duplicate substance
	 * test, substance test result, or crime lab result is found.
	 */
	@RequestContentMapping(nameKey = "substanceTestUpdateName",
			descriptionKey = "substanceTestupdateDescription",
			messageBundle = "omis.substance.msgs.substance",
			screenType = RequestContentType.FORM_SUBMIT)
	@RequestMapping(value = "/edit.html", 
			method = RequestMethod.POST)
	@PreAuthorize("hasRole('SUBSTANCE_TEST_EDIT') or hasRole('ADMIN')")
	public ModelAndView update(@RequestParam(value = "substanceTest", 
			required = true) final SubstanceTest substanceTest, 
			final SubstanceTestForm form, final BindingResult result)
		throws DuplicateEntityFoundException {
		this.substanceTestFormValidator.validate(form, result);
		if (result.hasErrors()) {
			ModelMap map = new ModelMap();
			int currentSubstanceTestResultIndex;
			if (form.getResultItems() == null) {
				currentSubstanceTestResultIndex = 0;
			} else {
				currentSubstanceTestResultIndex = form
						.getResultItems().size();
			}
			int currentLabResultIndex;
			if (form.getLabResultItems() == null) {
				currentLabResultIndex = 0;
			} else {
				currentLabResultIndex = form
						.getLabResultItems().size();
			}
			map.addAttribute(CURRENT_SUBSTANCE_TEST_RESULT_INDEX_MODEL_KEY, 
					currentSubstanceTestResultIndex);
			map.addAttribute(CURRENT_LAB_RESULT_INDEX_MODEL_KEY,
					currentLabResultIndex);
			map.addAttribute(BindingResult.MODEL_KEY_PREFIX
					+ EDIT_FORM_MODEL_KEY, result);
			return this.prepareEditMav(map, 
					substanceTest.getSubstanceTestSample(), form, 
					currentSubstanceTestResultIndex, currentLabResultIndex);
		}
		this.substanceTestService.update(substanceTest, 
				substanceTest.getSubstanceTestSample(), form.getResultDate(), 
				form.getLabInvolved(), form.getLabResultDate(), 
				form.getLabRequestDate(), form.getTestComment(), form.getLab(),
				form.getPrivateLabJustification(), form.getAuthorizingStaff());
		Set<SubstanceTestResult> persistedResults = new 
				HashSet<SubstanceTestResult>();
		for (ResultItem formResult : form.getResultItems()) {
			if (formResult.getProcess() != null && formResult.getProcess()) {
				SubstanceTestResult newResult = this.substanceTestService
						.findResultBySubstanceAndTest(substanceTest,
							formResult.getSubstance());
				if (newResult == null) {
					this.substanceTestService.addResult(substanceTest, 
							formResult.getSubstance(), 
							formResult.getSubstanceTestResultValue(), 
							formResult.getAdmit(), formResult.getAdmitPrior());
				} else {
					this.substanceTestService.updateResult(newResult, 
							formResult.getSubstance(), 
							formResult.getSubstanceTestResultValue(), 
							formResult.getAdmit(), formResult.getAdmitPrior());
				}
				persistedResults.add(newResult);
			}
		}
		this.substanceTestService.removeResultsBySubstanceTestExcluding(
				substanceTest, persistedResults.toArray(
						new SubstanceTestResult[] {}));
		Set<LabResult> persistedCLResults = new HashSet<LabResult>();
		for (LabResultItem clResult : form.getLabResultItems()) {
			if (clResult.getProcess() != null && clResult.getProcess()) {
				LabResult newCLResult = this.substanceTestService
						.findLabResultBySubstanceAndTest(substanceTest,
								clResult.getSubstance());
				if (newCLResult == null) {
					this.substanceTestService.addLabResult(substanceTest,
							clResult.getSubstance(),
							clResult.getSubstanceTestResultValue());
				} else {
					this.substanceTestService.updateLabResult(newCLResult,
							clResult.getSubstance(),
							clResult.getSubstanceTestResultValue());
				}
				persistedCLResults.add(newCLResult);
			}
		}
		if (persistedCLResults.isEmpty()) {
			this.substanceTestService.removeLabResultsBySubstanceTest(
					substanceTest);
		} else {
			this.substanceTestService
				.removeLabResultBySubstanceTestExcluding(substanceTest, 
						persistedCLResults.toArray(new LabResult[] {}));
		}
		return new ModelAndView(SUBSTANCE_LIST_REDIRECT_URL
				+ substanceTest.getSubstanceTestSample().getOffender()
				.getId());
	}
	
	/**
	 * Remove the specified substance test from the offenders
	 * substance test list screen.
	 * 
	 * @param substanceTest substance test 
	 * @return model and view
	 */
	@RequestContentMapping(nameKey = "substanceTestRemoveName",
			descriptionKey = "substanceTestRemoveDescription",
			messageBundle = "omis.substance.msgs.substance",
			screenType = RequestContentType.LISTING_SCREEN)
	@RequestMapping(value = "/remove.html", method = RequestMethod.GET)
	@PreAuthorize("hasRole('SUBSTANCE_TEST_REMOVE') or hasRole('ADMIN')")
	public String removeSubstanceTest(@RequestParam(
			value = "substanceTest", required = true)
			final SubstanceTest substanceTest) {
		Offender offender = substanceTest.getSubstanceTestSample()
				.getOffender();
		List<SubstanceTestResult> substanceTestResults = this
				.substanceTestService.findResultsBySubstanceTest(substanceTest);
		for (SubstanceTestResult substanceTestResult : substanceTestResults) {
			this.substanceTestService.removeResult(substanceTestResult);
		}
		List<LabResult> labResults = this.substanceTestService
				.findLabResultsBySubstanceTest(substanceTest);
		for (LabResult labResult : labResults) {
			this.substanceTestService.removeLabResult(labResult);
		}
		this.substanceTestService.remove(substanceTest);
		return SUBSTANCE_LIST_REDIRECT_URL + offender.getId();
	}
	
	/**
	 * Returns model and view to show crime lab content for a substance test
	 * form.
	 * 
	 * @return model and view for crime lab content
	 */
	@RequestMapping(value = "/labContent.html", method = RequestMethod.GET)
	public ModelAndView showLabContent() {
		ModelMap map = new ModelMap();
		map.addAttribute(SUBSTANCE_LABS_MODEL_KEY, this.substanceTestService
				.findSubstanceLabs());
		return new ModelAndView(LAB_CONTENT_VIEW_NAME, map);
	}
	
	@RequestMapping(value = "/displayJustification.html",
			method = RequestMethod.GET)
	public ModelAndView displayJustification(
			@RequestParam(value = "lab", required = false)
			final SubstanceLab lab) {
		ModelMap map = new ModelMap();
		if (lab != null) {
			map.addAttribute(PRIVATE_LAB_MODEL_KEY, lab.getPrivateLab());
		}
		return new ModelAndView(JUSTIFICATION_CONTENT_VIEW_NAME, map);
	}
	
	/**
	 * Ajax request that adds a crime lab result to the substance test form.
	 * 
	 * @param labResultIndex crime lab result index
	 * @return model and view
	 */
	@RequestMapping(value = "/addLabResult.html", 
			method = RequestMethod.GET)
	public ModelAndView addLabResult(@RequestParam(
			value = "labResultIndex", required = true)
			final int labResultIndex) {
		ModelMap map = new ModelMap();
		map.addAttribute(LAB_RESULT_INDEX_MODEL_KEY, labResultIndex);
		this.prepareResultRowMap(map);
		return new ModelAndView(LAB_RESULT_ROW_VIEW_NAME, map);
	}
	
	/**
	 * Ajax request that adds all of the local results substances into new
	 * crime lab result rows.
	 * 
	 * @param form substance test form
	 * @param positiveOnly positive only boolean
	 * @return model and view
	 */
	@RequestMapping(value = "/verifyLocalResults.html", 
			method = RequestMethod.GET)
	public ModelAndView verifyLocalResults(final SubstanceTestForm form, 
			@RequestParam(value = "positiveOnly", required = true)
			final boolean positiveOnly) {
		ModelMap map = new ModelMap();
		List<Substance> substances = new ArrayList<Substance>();
		for (LabResultItem resultItem : form.getLabResultItems()) {
			substances.add(resultItem.getSubstance());
		}
		for (ResultItem item : form.getResultItems()) {
			if (item.getProcess() != null && item.getProcess()) {
				if (positiveOnly) {
					if (item.getSubstanceTestResultValue() != null
							&& "Positive".equals(item
								.getSubstanceTestResultValue().getName())) {
						if (!substances.contains(item.getSubstance())) {
							LabResultItem newLRItem = 
									new LabResultItem();
							newLRItem.setSubstance(item.getSubstance());
							newLRItem.setProcess(true);
							form.getLabResultItems().add(newLRItem);
						}
					}
				} else {
					if (!substances.contains(item.getSubstance())) {
						LabResultItem newLRItem = 
								new LabResultItem();
						newLRItem.setSubstance(item.getSubstance());
						newLRItem.setProcess(true);
						form.getLabResultItems().add(newLRItem);
					}
				}
			}
		}
		int currentLabResultIndex;
		if (form.getLabResultItems() == null) {
			currentLabResultIndex = 0;
		} else {
			currentLabResultIndex = form
					.getLabResultItems().size();
		}
		map.addAttribute(SUBSTANCE_TEST_FORM_MODEL_KEY, form);
		map.addAttribute(CURRENT_LAB_RESULT_INDEX_MODEL_KEY, 
				currentLabResultIndex);
		map.addAttribute(NEW_CURRENT_INDEX_MODEL_KEY, true);
		this.prepareResultRowMap(map);
		return new ModelAndView(LAB_TABLE_BODY_VIEW_NAME, map);
	}
	
	/**
	 * Shows the optional control for the current substance test for substance
	 * use admission related information.
	 * 
	 * @param substanceTestResultIndex substance test result index
	 * @return model and view
	 */
	@RequestMapping(value = "/admitToUse.html", method = RequestMethod.GET)
	public ModelAndView admitToUse(@RequestParam(
			value = "substanceTestResultIndex", required = true)
			final int substanceTestResultIndex) {
		ModelMap map = new ModelMap();
		map.addAttribute(SUBSTANCE_TEST_RESULT_INDEX_MODEL_KEY, 
				substanceTestResultIndex);
		return new ModelAndView(SUBSTANCE_TEST_ADMIT_VIEW_NAME, map);
	}
	
	/**
	 * Ajax request that adds a substance test result to the substance 
	 * test form.
	 * 
	 * @param substanceTestResultIndex substance test result index
	 * @return model and view
	 */
	@RequestContentMapping(nameKey = "addSubstanceName",
			descriptionKey = "addSubstanceDescription",
			messageBundle = "omis.msgs.substancetest.substancetest",
			screenType = RequestContentType.AJAX_REQUEST)
	@RequestMapping(value = "/addSubstanceTestResult.html", 
			method = RequestMethod.GET)
	public ModelAndView addSubstanceTestResult(@RequestParam(
			value = "substanceTestResultIndex", required = true)
			final int substanceTestResultIndex) {
		ModelMap map = new ModelMap();
		map.addAttribute(SUBSTANCE_TEST_RESULT_INDEX_MODEL_KEY, 
				substanceTestResultIndex);
		this.prepareResultRowMap(map);
		return new ModelAndView(
				SUBSTANCE_TEST_RESULT_ROW_VIEW_NAME, map);
	}
	
	/**
	 * Shows the substance tests action menu.
	 * 
	 * @param offender offender
	 * @return model and view for substance tests action menu
	 */
	@RequestMapping(value = "/substanceTestsActionMenu.html",
			method = RequestMethod.GET)
	public ModelAndView showSubstanceTestsActionMenu(
			@RequestParam(value = "offender", required = true)
			final Offender offender) {
		ModelMap map = new ModelMap();
		map.addAttribute(OFFENDER_MODEL_KEY, offender);
		return new ModelAndView(SUBSTANCE_TESTS_ACTION_MENU_VIEW_NAME, map);
	}
	
	/**
	 * Shows the substance test action menu.
	 * 
	 * @param offender offender
	 * @return substance test action menu
	 */
	@RequestMapping(value = "/substanceTestActionMenu.html",
			method = RequestMethod.GET)
	public ModelAndView showSubstanceTestActionMenu(
			@RequestParam(value = "offender", required = true)
			final Offender offender) {
		ModelMap map = new ModelMap();
		map.addAttribute(OFFENDER_MODEL_KEY, offender);
		return new ModelAndView(SUBSTANCE_TEST_ACTION_MENU_VIEW_NAME, map);
	}
	
	/**
	 * Shows the substance tests row action menu.
	 * 
	 * @param sample substance test sample
	 * @param request substance test sample request
	 * @param substanceTest substance test
	 * @return model and view for substance tests row action menu
	 */
	@RequestMapping(value = "/substanceTestsRowActionMenu.html",
			method = RequestMethod.GET)
	public ModelAndView showSubstanceTestsRowActionMenu(
			@RequestParam(value = "offender", required = false)
				final Offender offender,
			@RequestParam(value = "sample", required = false)
				final SubstanceTestSample sample,
			@RequestParam(value = "request", required = false)
				final SampleRequest request,
			@RequestParam(value = "substanceTest", required = false)
				final SubstanceTest substanceTest) {
		ModelMap map = new ModelMap();
		map.addAttribute(SUBSTANCE_TEST_SAMPLE_MODEL_KEY, sample);
		map.addAttribute(SUBSTANCE_TEST_SAMPLE_REQUEST_MODLE_KEY, request);
		map.addAttribute(SUBSTANCE_TEST_MODEL_KEY, substanceTest);
		map.addAttribute(OFFENDER_MODEL_KEY, offender);
		return new ModelAndView(SUBSTANCE_TESTS_ROW_ACTION_MENU_VIEW_NAME, map);
	};
	
	/**
	 * Shows the substance test results action menu.
	 * 
	 * @return model and view for substance test results action menu
	 */
	@RequestMapping(value = "/substanceTestResultsActionMenu.html",
			method = RequestMethod.GET)
	public ModelAndView showSubstanceTestsResultsActionMenu(
			@RequestParam(value = "currentSubstanceTestResultIndex",
			required = true) final Integer index) {
		ModelMap map = new ModelMap();
		map.addAttribute(CURRENT_SUBSTANCE_TEST_RESULT_INDEX_MODEL_KEY, index);
		return new ModelAndView(SUBSTANCE_TESTS_RESULTS_ACTION_MENU_VIEW_NAME,
				map);
	};
	
	@RequestMapping(value = "/labResultsActionMenu.html",
			method = RequestMethod.GET)
	public ModelAndView showLabResultsActionMenu(
			@RequestParam(value = "currentLabResultIndex",
			required = true) final Integer index) {
		ModelMap map = new ModelMap();
		map.addAttribute(CURRENT_LAB_RESULT_INDEX_MODEL_KEY, index);
		return new ModelAndView(
				SUBSTANCE_TESTS_VERIFICATION_RESULTS_ACTION_MENU_VIEW_NAME, 
				map);
	};
	
	/* Helper methods. */
	
	/*
	 * Prepares a model map with closed text options needed when displaying a
	 * result row on a substance test form.
	 * 
	 * @param map model map
	 * @return result row model map
	 */
	private ModelMap prepareResultRowMap(final ModelMap map) {
		map.addAttribute(RESULTS_MODEL_KEY, this
				.substanceTestService.findResultValues());
		map.addAttribute(SUBSTANCES_MODEL_KEY, this.substanceTestService
				.findSubstances());
		return map;
	}
	
	/*
	 * Prepares a model and view with information needed in the substance test
	 * sample form when creating a new sample.
	 */
	private ModelAndView prepareEditMav(
			final ModelMap map, final SubstanceTestSample substanceTestSample,
			final SubstanceTestForm form,
			final int currentSubstanceTestResultIndex,
			final int currentLabResultIndex) {
		this.offenderSummaryModelDelegate.add(map,
				substanceTestSample.getOffender());
		map.addAttribute(SUBSTANCE_TEST_SAMPLE_MODEL_KEY, substanceTestSample);
		map.addAttribute(OFFENDER_MODEL_KEY, substanceTestSample.getOffender());
		map.addAttribute(CURRENT_SUBSTANCE_TEST_RESULT_INDEX_MODEL_KEY, 
				currentSubstanceTestResultIndex);
		map.addAttribute(CURRENT_LAB_RESULT_INDEX_MODEL_KEY,
				currentLabResultIndex);
		map.addAttribute(SUBSTANCE_TEST_FORM_MODEL_KEY, form);
		map.addAttribute(SUBSTANCES_MODEL_KEY, this.substanceTestService
				.findSubstances());
		map.addAttribute(RESULTS_MODEL_KEY, this
				.substanceTestService.findResultValues());
		map.addAttribute(SUBSTANCE_LABS_MODEL_KEY, this.substanceTestService
				.findSubstanceLabs());
		return new ModelAndView(EDIT_SUBSTANCE_TEST_VIEW_NAME, map);
	}
	
	/**
	 * Sets up and registers property editors.
	 * 
	 * @param binder binder
	 */
	@InitBinder
	protected void initBinder(final WebDataBinder binder) {
		binder.registerCustomEditor(
				SampleCollectionMethod.class,
				this.sampleCollectionMethodPropertyEditorFactory
				.createPropertyEditor());
		binder.registerCustomEditor(
				SubstanceTest.class,
				this.substanceTestPropertyEditorFactory
				.createPropertyEditor());
		binder.registerCustomEditor(
				SubstanceTestReason.class,
				this.substanceTestReasonPropertyEditorFactory
				.createPropertyEditor());
		binder.registerCustomEditor(
				SubstanceTestResult.class,
				this.substanceTestResultPropertyEditorFactory
				.createPropertyEditor());
		binder.registerCustomEditor(
				SubstanceTestSample.class,
				this.substanceTestSamplePropertyEditorFactory
				.createPropertyEditor());
		binder.registerCustomEditor(
				TestKit.class,
				this.testKitPropertyEditorFactory
				.createPropertyEditor());
		binder.registerCustomEditor(
				TestKitParameter.class,
				this.testKitParameterPropertyEditorFactory
				.createPropertyEditor());
		binder.registerCustomEditor(
				Offender.class,
				this.offenderPropertyEditorFactory
				.createOffenderPropertyEditor());
		binder.registerCustomEditor(
				Date.class,
				this.customDateEditorFactory
				.createCustomDateOnlyEditor(true));
		binder.registerCustomEditor(
				SubstanceTestResult.class,
				this.substanceTestResultPropertyEditorFactory
				.createPropertyEditor());
		binder.registerCustomEditor(
				LabResult.class,
				this.labResultPropertyEditorFactory
				.createPropertyEditor());
		binder.registerCustomEditor(
				Substance.class,
				this.substancePropertyEditorFactory
				.createPropertyEditor());
		binder.registerCustomEditor(
				SubstanceTestResultValue.class,
				this.substanceTestResultValuePropertyEditorFactory
				.createPropertyEditor());
		binder.registerCustomEditor(
				SampleRequest.class,
				this.sampleRequestPropertyEditorFactory
				.createPropertyEditor());
		binder.registerCustomEditor(
				SubstanceLab.class,
				this.substanceLabPropertyEditorFactory
				.createPropertyEditor());
		binder.registerCustomEditor(
				Person.class,
				this.personPropertyEditorFactory
				.createPropertyEditor());
	}
}