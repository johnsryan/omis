package omis.financial.web.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.support.ByteArrayMultipartFileEditor;
import org.springframework.web.servlet.ModelAndView;

import omis.beans.factory.PropertyEditorFactory;
import omis.document.domain.Document;
import omis.document.domain.DocumentTag;
import omis.document.io.DocumentPersister;
import omis.document.io.DocumentRetriever;
import omis.document.io.impl.DocumentFilenameGenerator;
import omis.document.web.form.DocumentTagItem;
import omis.document.web.form.DocumentTagOperation;
import omis.exception.DuplicateEntityFoundException;
import omis.financial.domain.FinancialAsset;
import omis.financial.domain.FinancialAssetCategory;
import omis.financial.domain.FinancialDocumentAssociation;
import omis.financial.domain.FinancialLiability;
import omis.financial.domain.FinancialLiabilityCategory;
import omis.financial.domain.RecurringDeduction;
import omis.financial.domain.RecurringDeductionCategory;
import omis.financial.domain.RecurringDeductionFrequency;
import omis.financial.service.FinancialProfileService;
import omis.financial.web.form.FinancialAssetItem;
import omis.financial.web.form.FinancialDocumentItem;
import omis.financial.web.form.FinancialLiabilityItem;
import omis.financial.web.form.FinancialProfileForm;
import omis.financial.web.form.ItemOperation;
import omis.financial.web.form.RecurringDeductionItem;
import omis.financial.web.validator.FinancialProfileFormValidator;
import omis.io.FileRemover;
import omis.offender.beans.factory.OffenderPropertyEditorFactory;
import omis.offender.domain.Offender;
import omis.offender.report.OffenderReportService;
import omis.offender.report.OffenderSummary;
import omis.offender.web.controller.delegate.OffenderSummaryModelDelegate;
import omis.report.ReportFormat;
import omis.report.ReportRunner;
import omis.report.web.controller.delegate.ReportControllerDelegate;

/** Controller for employment.
 * @author Josh Divine
 * @author Yidong Li
 * @author Trevor Isles
 * @author Sierra Haynes
 * @versio 0.1.1 (November 22, 2016)
 * @since OMIS 3.0 */
@Controller
@RequestMapping("/financial")
@PreAuthorize("hasRole('USER')")
public class FinancialProfileController {

	/* Views. */
	private static final String EDIT_VIEW_NAME
		= "financial/edit";
	
	private static final String FINANCIAL_ASSET_TABLE_ROW_VIEW_NAME
		= "financial/asset/includes/editTableRow";
	
	private static final String FINANCIAL_LIABILITY_TABLE_ROW_VIEW_NAME
		= "financial/liability/includes/editTableRow";
	
	private static final String FINANCIAL_ASSETS_ACTION_MENU_VIEW_NAME
	 	= "financial/includes/financialAssetsActionMenu";
	
	private static final String FINANCIAL_LIABILITIES_ACTION_MENU_VIEW_NAME
 		= "financial/includes/financialLiabilitiesActionMenu";
	
	private static final String ACTION_MENU_VIEW_NAME
		= "financial/includes/financialProfileActionMenu";
	
	private static final String RECURRING_DEDUCTION_ACTION_MENU_VIEW_NAME
		= "financial/includes/recurringDeductionActionMenu";
	
	private static final String RECURRING_DEDUCTION_TABLE_ROW_VIEW_NAME
		= "financial/deduction/includes/editRecurringDeductionTableRow";
	
	private static final String FINANCIAL_DOCUMENT_TABLE_ROW_VIEW_NAME
		= "financial/financialDocumentAssociation/includes/"
				+ "editFinancialDocumentAssociationTableRow";
	
	private static final String DOCUMENT_TAG_ITEM_CONTENT_VIEW_NAME 
		= "financial/financialDocumentAssociation/includes/"
				+ "financialDocumentAssociationTagItemContent";
	
	private static final String FINANCIAL_DOCUMENT_ITEM_CONTENT_VIEW_NAME 
		= "financial/financialDocumentAssociation/includes/"
				+ "financialDocumentItemContent";
	
	/* Model Keys. */
	private static final String OFFENDER_MODEL_KEY = "offender";
	private static final String OFFENDER_SUMMARY_MODEL_KEY = "offenderSummary";
	private static final String TOTAL_ASSETS_MODEL_KEY = "totalAssets";
	private static final String TOTAL_LIABILITIES_MODEL_KEY 
		= "totalLiabilities";
	private static final String TOTAL_EQUITY_MODEL_KEY = "totalEquity";
	private static final String FINANCIAL_ASSET_CATEGORIES_MODEL_KEY
		= "financialAssetCategories";
	private static final String FINANCIAL_ASSET_ITEM_INDEX_MODEL_KEY
		= "financialAssetItemIndex";
	private static final String FINANCIAL_ASSET_ITEM_MODEL_KEY
		= "financialAssetItem";
	private static final String FINANCIAL_LIABILITY_CATEGORIES_MODEL_KEY
		= "financialLiabilityCategories";
	private static final String FINANCIAL_LIABILITY_ITEM_INDEX_MODEL_KEY
		= "financialLiabilityItemIndex";
	private static final String FINANCIAL_LIABILITY_ITEM_MODEL_KEY
		= "financialLiabilityItem";
	private static final String FINANCIAL_PROFILE_FORM_MODEL_KEY
	 	= "financialProfileForm";
	private static final String RECURRING_DEDUCTION_CATEGORIES_MODEL_KEY
		= "recurringDeductionCategories";
	private static final String RECURRING_DEDUCTION_FREQUENCIES_MODEL_KEY
		= "recurringDeductionFrequencies";
	private static final String RECURRING_DEDUCTION_ITEM_INDEX_MODEL_KEY
		= "recurringDeductionItemIndex";
	private static final String RECURRING_DEDUCTION_ITEM_MODEL_KEY
		= "recurringDeductionItem";
	private static final String FINANCIAL_DOCUMENT_ITEM_INDEX_MODEL_KEY
		= "financialDocumentAssociationItemIndex";
	private static final String FINANCIAL_DOCUMENT_ITEM_MODEL_KEY
		= "financialDocumentAssociationItem";
	private static final String DOCUMENT_TAG_ITEM_INDEXES_MODEL_KEY 
		= "documentTagItemIndexes";
	private static final String DOCUMENT_TAG_ITEM_MODEL_KEY = "documentTagItem";
	private static final String DOCUMENT_TAG_ITEM_INDEX_MODEL_KEY 
		= "documentTagItemIndex";
	
	/* Redirects. */
	
	private static final String EDIT_REDIRECT
		= "redirect:/financial/edit.html?offender=%d";
	
	/* Property editor. */
	@Autowired
	@Qualifier("offenderPropertyEditorFactory")
	private OffenderPropertyEditorFactory offenderPropertyEditorFactory;
	
	@Autowired
	@Qualifier("financialAssetPropertyEditorFactory")
	private PropertyEditorFactory financialAssetPropertyEditorFactory;
	
	@Autowired
	@Qualifier("financialAssetCategoryPropertyEditorFactory")
	private PropertyEditorFactory financialAssetCategoryPropertyEditorFactory;
	
	@Autowired
	@Qualifier("financialLiabilityPropertyEditorFactory")
	private PropertyEditorFactory financialLiabilityPropertyEditorFactory;

	@Autowired
	@Qualifier("financialLiabilityCategoryPropertyEditorFactory")
	private PropertyEditorFactory financialLiabilityCategoryPropertyEditorFactory;
	
	@Autowired
	@Qualifier("recurringDeductionPropertyEditorFactory")
	private PropertyEditorFactory recurringDeductionPropertyEditorFactory;

	@Autowired
	@Qualifier("recurringDeductionCategoryPropertyEditorFactory")
	private PropertyEditorFactory recurringDeductionCategoryPropertyEditorFactory;
	
	@Autowired
	@Qualifier("documentPropertyEditorFactory")
	private PropertyEditorFactory documentPropertyEditorFactory;
	
	@Autowired
	@Qualifier("documentTagPropertyEditorFactory")
	private PropertyEditorFactory documentTagPropertyEditorFactory;
	
	@Autowired
	@Qualifier("financialDocumentAssociationPropertyEditorFactory")
	private PropertyEditorFactory 
		financialDocumentAssociationPropertyEditorFactory;
	
	/* Services. */
	@Autowired
	@Qualifier("financialProfileService")
	private FinancialProfileService financialProfileService; 
	
	@Autowired
	@Qualifier("offenderReportService")
	private OffenderReportService offenderReportService;
	
	/* Delegate */
	@Autowired
	@Qualifier("offenderSummaryModelDelegate")
	private OffenderSummaryModelDelegate offenderSummaryModelDelegate;
	
	@Autowired
	@Qualifier("reportControllerDelegate")
	private ReportControllerDelegate reportControllerDelegate;
	
	/* Helpers */
	@Autowired
	@Qualifier("documentFilenameGenerator")
	private DocumentFilenameGenerator documentFilenameGenerator;
	
	@Autowired
	@Qualifier("financialDocumentPersister")
	private DocumentPersister financialDocumentPersister;
	
	@Autowired
	@Qualifier("financialDocumentRetriever")
	private DocumentRetriever financialDocumentRetriever;
	
	@Autowired
	@Qualifier("financialDocumentRemover")
	private FileRemover financialDocumentRemover;

	/* Validator */
	@Autowired
	@Qualifier("financialProfileFormValidator")
	private FinancialProfileFormValidator financialProfileFormValidator;
	
	/* Report names. */
	
	private static final String FINANCIAL_PROFILE_DETAILS_REPORT_NAME 
		= "/CaseManagement/FinancialProfile/Financial_Profile";
	
	private static final String OFFENDER_FINANCIAL_SUMMARY_REPORT_NAME 
	= "/CaseManagement/FinancialProfile/Offender_Financial_Summary";	

	/* Report parameter names. */
	
	private static final String FINANCIAL_PROFILE_DETAILS_ID_REPORT_PARAM_NAME 
		= "DOC_ID";

	/* Report runners. */
	
	@Autowired
	@Qualifier("reportRunner")
	private ReportRunner reportRunner;
	
	/* Constructor. */
	/** Instantiates a default financial profile controller. */
	public FinancialProfileController() {
		// Default instantiation
	}
	
/* Screens */
	
	/**
	 * Returns a model and view to create a financial asset
	 * @param itemIndex - item index
	 * @return ModelAndView
	 */
	@RequestMapping(value = "/createAsset.html", method = RequestMethod.GET)
	@PreAuthorize("hasRole('FINANCIAL_ASSET_CREATE') or "
			+ "hasRole('ADMIN')")
	public ModelAndView createAsset(@RequestParam(
			value = "itemIndex", required = true)
		final Integer itemIndex){
		FinancialAssetItem financialAssetItem = new FinancialAssetItem();
		financialAssetItem.setOperation(ItemOperation.CREATE);
		List<FinancialAssetCategory> categories = this.financialProfileService
				.findAssetCategories();
		ModelAndView mav 
			= new ModelAndView(FINANCIAL_ASSET_TABLE_ROW_VIEW_NAME);
		mav.addObject(FINANCIAL_ASSET_CATEGORIES_MODEL_KEY, categories);
		mav.addObject(FINANCIAL_ASSET_ITEM_INDEX_MODEL_KEY, itemIndex);
		mav.addObject(FINANCIAL_ASSET_ITEM_MODEL_KEY, financialAssetItem);
		return mav;
	}
	
	/**
	 * Returns a model and view to create a financial liability
	 * @param itemIndex - item index
	 * @return ModelAndView
	 */
	@RequestMapping(value = "/createLiability.html", method = RequestMethod.GET)
	@PreAuthorize("hasRole('FINANCIAL_LIABILITY_CREATE') or "
			+ "hasRole('ADMIN')")
	public ModelAndView createLiability(@RequestParam(
			value = "itemIndex", required = true)
		final Integer itemIndex){
		FinancialLiabilityItem financialLiabilityItem 
			= new FinancialLiabilityItem();
		financialLiabilityItem.setOperation(ItemOperation.CREATE);
		List<FinancialLiabilityCategory> categories 
			= this.financialProfileService.findLiabilityCategories();
		ModelAndView mav 
			= new ModelAndView(FINANCIAL_LIABILITY_TABLE_ROW_VIEW_NAME);
		mav.addObject(FINANCIAL_LIABILITY_CATEGORIES_MODEL_KEY, categories);
		mav.addObject(FINANCIAL_LIABILITY_ITEM_INDEX_MODEL_KEY, itemIndex);
		mav.addObject(FINANCIAL_LIABILITY_ITEM_MODEL_KEY, 
				financialLiabilityItem);
		return mav;
	}
	
	/**
	 * Returns a model and view to create a recurring deduction
	 * @param itemIndex - item index
	 * @return ModelAndView
	 */
	@RequestMapping(value = "/createRecurringDeduction.html", 
		method = RequestMethod.GET)
	@PreAuthorize("hasRole('RECURRING_DEDUCTION_CREATE') or "
			+ "hasRole('ADMIN')")
	public ModelAndView createRecurringDeduction(@RequestParam(
			value = "itemIndex", required = true)
		final Integer itemIndex){
		RecurringDeductionItem recurringDeductionItem 
			= new RecurringDeductionItem();
		recurringDeductionItem.setOperation(ItemOperation.CREATE);
		List<RecurringDeductionCategory> categories 
			= this.financialProfileService.findRecurringDeductionCategories();
		ModelAndView mav 
			= new ModelAndView(RECURRING_DEDUCTION_TABLE_ROW_VIEW_NAME);
		mav.addObject(RECURRING_DEDUCTION_CATEGORIES_MODEL_KEY, categories);
		mav.addObject(RECURRING_DEDUCTION_ITEM_INDEX_MODEL_KEY, itemIndex);
		mav.addObject(RECURRING_DEDUCTION_ITEM_MODEL_KEY, recurringDeductionItem);
		mav.addObject(RECURRING_DEDUCTION_FREQUENCIES_MODEL_KEY,
			RecurringDeductionFrequency.values());
		return mav;
	}
	
	/**
	 * Returns a model and view to create a financial document association
	 * @param itemIndex - item index
	 * @return ModelAndView
	 */
	@RequestMapping(value = "/createFinancialDocumentAssociation.html",
			method = RequestMethod.GET)
	@PreAuthorize("hasRole('FINANCIAL_ASSOCIABLE_DOCUMENT_CREATE') or "
			+ "hasRole('ADMIN')")
	public ModelAndView createFinancialDocument(@RequestParam(
			value = "financialDocumentAssociationItemIndex", required = false)
		final Integer itemIndex){
		FinancialDocumentItem financialDocumentItem
			= new FinancialDocumentItem();
		financialDocumentItem.setOperation(ItemOperation.CREATE);
		ModelAndView mav = new ModelAndView(
				FINANCIAL_DOCUMENT_TABLE_ROW_VIEW_NAME);
		mav.addObject(FINANCIAL_DOCUMENT_ITEM_INDEX_MODEL_KEY, itemIndex);
		mav.addObject(FINANCIAL_DOCUMENT_ITEM_MODEL_KEY, financialDocumentItem);
		return mav;
	}
	
	/**
	 * Displays offenders financial profile.
	 * 
	 * @param offender offender
	 * @return view to display the list of employment records
	 */
	@RequestMapping(value="/edit.html", method = RequestMethod.GET)
	@PreAuthorize("hasRole('FINANCIAL_PROFILE_VIEW') or hasRole('ADMIN')")
	public ModelAndView edit(@RequestParam(value = "offender", required = true) 
		final Offender offender) {
		List<FinancialAsset> financialAssets = this.financialProfileService
				.findAssets(offender);
		List<FinancialAssetItem> financialAssetItems 
			= new ArrayList<FinancialAssetItem>();
		for (FinancialAsset financialAsset : financialAssets) {
			FinancialAssetItem financialAssetItem = new FinancialAssetItem();
			financialAssetItem.setAmount(financialAsset.getAmount()
					.toPlainString());
			financialAssetItem.setCategory(financialAsset.getCategory());
			financialAssetItem.setDescription(financialAsset.getDescription());
			financialAssetItem.setOperation(ItemOperation.UPDATE);
			financialAssetItem.setReportedDate(financialAsset
					.getReportedDate());
			financialAssetItem.setFinancialAsset(financialAsset);
			financialAssetItems.add(financialAssetItem);
		}
		List<FinancialLiability> financialLiabilities 
			= this.financialProfileService.findLiabilities(offender);
		List<FinancialLiabilityItem> financialLiabilityItems 
			= new ArrayList<FinancialLiabilityItem>();
		for (FinancialLiability financialLiability : financialLiabilities) {
			FinancialLiabilityItem financialLiabilityItem 
				= new FinancialLiabilityItem();
			financialLiabilityItem.setAmount(financialLiability.getAmount()
					.toPlainString());
			financialLiabilityItem.setCategory(financialLiability
					.getCategory());
			financialLiabilityItem.setDescription(financialLiability
					.getDescription());
			financialLiabilityItem.setOperation(ItemOperation.UPDATE);
			financialLiabilityItem.setReportedDate(financialLiability
					.getReportedDate());
			financialLiabilityItem.setFinancialLiability(financialLiability);
			financialLiabilityItems.add(financialLiabilityItem);
		}
		List<RecurringDeduction> recurringDeductions 
			= this.financialProfileService.findRecurringDeductions(offender);
		List<RecurringDeductionItem> recurringDeductionItems 
			= new ArrayList<RecurringDeductionItem>();
		for (RecurringDeduction recurringDeduction : recurringDeductions) {
			RecurringDeductionItem recurringDeductionItem 
				= new RecurringDeductionItem();
			recurringDeductionItem.setAmount(recurringDeduction.getAmount()
					.toPlainString());
			recurringDeductionItem.setCategory(recurringDeduction.getCategory());
			recurringDeductionItem.setDescription(recurringDeduction
					.getDescription());
			recurringDeductionItem.setOperation(ItemOperation.UPDATE);
			recurringDeductionItem.setReportedDate(recurringDeduction
					.getReportedDate());
			recurringDeductionItem.setRecurringDeduction(recurringDeduction);
			recurringDeductionItem.setFrequency(recurringDeduction.getFrequency());
			recurringDeductionItems.add(recurringDeductionItem);
		}
		List<FinancialDocumentAssociation> financialDocuments
			= this.financialProfileService.
				findFinancialDocumentAssociationsByOffender(offender);
		List<FinancialDocumentItem> financialDocumentItems
			= new ArrayList<FinancialDocumentItem>();
		for (FinancialDocumentAssociation financialDocument 
				: financialDocuments) {
			FinancialDocumentItem financialDocumentItem 
				= new FinancialDocumentItem();
			financialDocumentItem.setOperation(ItemOperation.UPDATE);
			financialDocumentItem.setDocument(financialDocument.getDocument());
			financialDocumentItem.setDate(financialDocument.getDocument()
					.getDate());
			financialDocumentItem.setTitle(financialDocument.getDocument()
					.getTitle());
			financialDocumentItem.setFileExtension(financialDocument
					.getDocument().getFileExtension());
			financialDocumentItem.setData(this.financialDocumentRetriever
					.retrieve(financialDocument.getDocument()));
			financialDocumentItem.setFinancialDocumentAssociation(financialDocument);
			List<DocumentTag> documentTags = this.financialProfileService
						.findDocumentTagsByDocument(financialDocument
						.getDocument());
			List<DocumentTagItem> documentTagItems 
					= new ArrayList<DocumentTagItem>();
			for (DocumentTag tag : documentTags) {
				DocumentTagItem tagItem = new DocumentTagItem();
				tagItem.setDocumentTagOperation(DocumentTagOperation.UPDATE);
				tagItem.setDocumentTag(tag);
				tagItem.setName(tag.getName());
				documentTagItems.add(tagItem);
			}
			financialDocumentItem.setDocumentTagItems(documentTagItems);
			financialDocumentItems.add(financialDocumentItem);
		}
		FinancialProfileForm financialProfileForm = new FinancialProfileForm();
		financialProfileForm.setFinancialAssetItems(financialAssetItems);
		financialProfileForm.setFinancialLiabilityItems(financialLiabilityItems);
		financialProfileForm.setRecurringDeductionItems(recurringDeductionItems);
		financialProfileForm.setFinancialDocumentAssociationItems(financialDocumentItems);
		return this.prepareEditMav(financialProfileForm, offender);
	}
	
	/**
	 * Updates financial profile.
	 * 
	 * @param victimNote victim note to update
	 * @param victimNoteForm form for victim note
	 * @param result result
	 * @return redirect to screen to list notes for victim of note
	 * @throws DuplicateEntityFoundException if victim note exists
	 */
	@PreAuthorize("hasRole('ADMIN') or hasRole('FINANCIAL_PROFILE_EDIT')")
	@RequestMapping(value = "/edit.html", method = RequestMethod.POST)
	public ModelAndView update(
			@RequestParam(value = "offender", required = true)
			final Offender offender,
			final FinancialProfileForm financialProfileForm,
			final BindingResult result) throws DuplicateEntityFoundException {
		this.financialProfileFormValidator.validate(financialProfileForm, 
				result);
		if (result.hasErrors()) {
			return this.prepareRedisplayMav(financialProfileForm, result, 
					offender);
		}
		processAssetChanges(financialProfileForm.getFinancialAssetItems(), 
				offender);
		processLiabilityChanges(financialProfileForm
				.getFinancialLiabilityItems(), offender);
		processRecurringDeductionChanges(financialProfileForm
				.getRecurringDeductionItems(), offender);
		processFinancialDocumentAssociationChanges(financialProfileForm
				.getFinancialDocumentAssociationItems(), offender);
		
		return new ModelAndView(String.format(EDIT_REDIRECT, offender.getId()));
	}
	
	/* Action menus. */
	
	/**
	 * Shows action menu for financial profile.
	 * 
	 * @param offender offender
	 * @return action menu for financial profile
	 */
	@RequestMapping(value = "/financialProfileActionMenu.html", 
			method = RequestMethod.GET)
	public ModelAndView showActionMenu(
			@RequestParam(value = "offender", required = false)
				final Offender offender) {
		ModelAndView mav = new ModelAndView(ACTION_MENU_VIEW_NAME);
		mav.addObject(OFFENDER_MODEL_KEY, offender);
		return mav;
	}
	
	/**
	 * Returns financial assets action menu.
	 * 
	 * @param offender offender
	 * @return financial assets action menu
	 */
	@RequestMapping(value="/financialAssetsActionMenu.html",
			method = RequestMethod.GET)
	public ModelAndView showFinancialAssetsActionMenu(
			@RequestParam(value = "offender", required = false)
				final Offender offender) {
		ModelAndView mav 
			= new ModelAndView(FINANCIAL_ASSETS_ACTION_MENU_VIEW_NAME);
		mav.addObject(OFFENDER_MODEL_KEY, offender);
		return mav;
	}
	
	/**
	 * Returns financial liabilities action menu.
	 * 
	 * @param offender offender
	 * @return financial liabilities action menu
	 */
	@RequestMapping(value="/financialLiabilitiesActionMenu.html",
			method = RequestMethod.GET)
	public ModelAndView showFinancialLiabilitiesActionMenu(
			@RequestParam(value = "offender", required = false)
				final Offender offender) {
		ModelAndView mav 
			= new ModelAndView(FINANCIAL_LIABILITIES_ACTION_MENU_VIEW_NAME);
		mav.addObject(OFFENDER_MODEL_KEY, offender);
		return mav;
	}
	
	/**
	 * Returns recurring deduction action menu.
	 * 
	 * @param offender offender
	 * @return financial assets action menu
	 */
	@RequestMapping(value="/recurringDeductionActionMenu.html",
			method = RequestMethod.GET)
	public ModelAndView showRecurringDeductionActionMenu(
			@RequestParam(value = "offender", required = false)
				final Offender offender) {
		ModelAndView mav 
			= new ModelAndView(RECURRING_DEDUCTION_ACTION_MENU_VIEW_NAME);
		mav.addObject(OFFENDER_MODEL_KEY, offender);
		return mav;
	}
	
	/* Reports. */
	
	/**
	 * Returns the report for the specified offender.
	 * 
	 * @param offender offender
	 * @param reportFormat report format
	 * @return response entity with report
	 */
	@RequestMapping(value = "/financialProfileReport.html",
			method = RequestMethod.GET)
	@PreAuthorize("hasRole('FINANCIAL_PROFILE_VIEW') or hasRole('ADMIN')")
	public ResponseEntity<byte []> reportFinancialProfile(@RequestParam(
			value = "offender", required = true)
			final Offender offender,
			@RequestParam(value = "reportFormat", required = true)
			final ReportFormat reportFormat) {
		Map<String, String> reportParamMap = new HashMap<String, String>();
		reportParamMap.put(FINANCIAL_PROFILE_DETAILS_ID_REPORT_PARAM_NAME,
				Long.toString(offender.getOffenderNumber()));
		byte[] doc = this.reportRunner.runReport(
				FINANCIAL_PROFILE_DETAILS_REPORT_NAME,
				reportParamMap, reportFormat);
		return this.reportControllerDelegate.constructReportResponseEntity(
				doc, reportFormat);
	}
	/**
	 * Returns the financial summary report for the specified offender.
	 * 
	 * @param offender offender
	 * @param reportFormat report format
	 * @return response entity with report
	 */
	@RequestMapping(value = "/offenderFinancialSummaryReport.html",
			method = RequestMethod.GET)
	@PreAuthorize("hasRole('FINANCIAL_PROFILE_VIEW') or hasRole('ADMIN')")
	public ResponseEntity<byte []> reportOffenderFinancialSummary(@RequestParam(
			value = "offender", required = true)
			final Offender offender,
			@RequestParam(value = "reportFormat", required = true)
			final ReportFormat reportFormat) {
		Map<String, String> reportParamMap = new HashMap<String, String>();
		reportParamMap.put(FINANCIAL_PROFILE_DETAILS_ID_REPORT_PARAM_NAME,
				Long.toString(offender.getOffenderNumber()));
		byte[] doc = this.reportRunner.runReport(
				OFFENDER_FINANCIAL_SUMMARY_REPORT_NAME,
				reportParamMap, reportFormat);
		return this.reportControllerDelegate.constructReportResponseEntity(
				doc, reportFormat);
	}
		
	/* Helper methods. */
	
	/*
	 * Prepares model and view to redisplay screen to edit the financial profile
	 * @param financialProfileForm financial profile form
	 * @param result result
	 * @param offender offender
	 * @return model and view to be redisplayed
	 */
	private ModelAndView prepareRedisplayMav(
			final FinancialProfileForm financialProfileForm,
			final BindingResult result, final Offender offender) {
		ModelAndView mav = this.prepareEditMav(
				financialProfileForm, offender);
		mav.addObject(BindingResult.MODEL_KEY_PREFIX
				+ FINANCIAL_PROFILE_FORM_MODEL_KEY, result);
		return mav;
	}
	
	/*
	 * Prepares the model and view to display the screen to edit the financial 
	 * profile
	 * @param financialProfileForm financial profile form
	 * @param offender offender
	 * @return model and view to be displayed
	 */
	private ModelAndView prepareEditMav(
			final FinancialProfileForm financialProfileForm,
			final Offender offender) {
		ModelAndView mav = new ModelAndView(EDIT_VIEW_NAME);
		mav.addObject(FINANCIAL_ASSET_CATEGORIES_MODEL_KEY,
				this.financialProfileService
					.findAssetCategories());
		mav.addObject(FINANCIAL_LIABILITY_CATEGORIES_MODEL_KEY,
				this.financialProfileService
					.findLiabilityCategories());
		
		this.offenderSummaryModelDelegate.add(mav.getModelMap(), offender);
		BigDecimal totalAssets = this.financialProfileService
				.calculateAssets(offender);
		mav.addObject(TOTAL_ASSETS_MODEL_KEY, totalAssets);
		BigDecimal totalLiabilities = this.financialProfileService
				.calculateLiabilities(offender);
		mav.addObject(TOTAL_LIABILITIES_MODEL_KEY, totalLiabilities);
		BigDecimal totalEquity = this.financialProfileService
				.calculateEquity(offender);
		mav.addObject(TOTAL_EQUITY_MODEL_KEY, totalEquity);
		mav.addObject(OFFENDER_MODEL_KEY, offender);
		Integer assetItemIndex = 0;
		if (financialProfileForm.getFinancialAssetItems() != null) {
			assetItemIndex = financialProfileForm.getFinancialAssetItems()
					.size();
		}
		mav.addObject(FINANCIAL_ASSET_ITEM_INDEX_MODEL_KEY, assetItemIndex);
		Integer liabilityItemIndex = 0;
		if (financialProfileForm.getFinancialLiabilityItems() != null) {
			liabilityItemIndex = financialProfileForm
					.getFinancialLiabilityItems().size();
		}
		mav.addObject(FINANCIAL_LIABILITY_ITEM_INDEX_MODEL_KEY, 
				liabilityItemIndex);
		Integer recurringDeductionItemIndex = 0;
		if (financialProfileForm.getRecurringDeductionItems() != null) {
			recurringDeductionItemIndex = financialProfileForm
					.getRecurringDeductionItems().size();
		}
		mav.addObject(RECURRING_DEDUCTION_ITEM_INDEX_MODEL_KEY, 
				recurringDeductionItemIndex);
		Integer financialDocumentItemIndex = 0;
		if (financialProfileForm.getFinancialDocumentAssociationItems() != null) {
			financialDocumentItemIndex = financialProfileForm
					.getFinancialDocumentAssociationItems().size();
		}
		mav.addObject(FINANCIAL_DOCUMENT_ITEM_INDEX_MODEL_KEY, 
				financialDocumentItemIndex);
		
		if(financialProfileForm.getFinancialDocumentAssociationItems() != null){
			List<Integer> tagIndexes = new ArrayList<Integer>();
			for(int i = 0;
				i < financialProfileForm.getFinancialDocumentAssociationItems()
					.size(); i++){
				if(financialProfileForm.getFinancialDocumentAssociationItems()
						.get(i) != null){
					tagIndexes.add(i,
						(financialProfileForm.getFinancialDocumentAssociationItems()
						.get(i).getDocumentTagItems() != null ?
						financialProfileForm.getFinancialDocumentAssociationItems()
						.get(i).getDocumentTagItems().size() : 0));
				}
				else{
					tagIndexes.add(i, 0);
				}
			}
			mav.addObject(DOCUMENT_TAG_ITEM_INDEXES_MODEL_KEY, tagIndexes);
		}
		
		OffenderSummary offenderSummary = this.offenderReportService
			.summarizeOffender(offender);
		mav.addObject(OFFENDER_SUMMARY_MODEL_KEY, offenderSummary);
		
		mav.addObject(RECURRING_DEDUCTION_CATEGORIES_MODEL_KEY, 
			this.financialProfileService.findRecurringDeductionCategories());
		mav.addObject(RECURRING_DEDUCTION_FREQUENCIES_MODEL_KEY,
				RecurringDeductionFrequency.values());
		mav.addObject(FINANCIAL_PROFILE_FORM_MODEL_KEY, financialProfileForm);
		return mav;
	}
	
	/*
	 * Processes Asset items 
	 * @param items list financial asset items to be processed
	 * @param offender offender
	 * @throws DuplicateEntityFoundException
	 */
	private void processAssetChanges(final List<FinancialAssetItem> items,
			final Offender offender) throws DuplicateEntityFoundException {
		if (items != null) {
			for (FinancialAssetItem assetItem : items) {
				if (ItemOperation.CREATE.equals(assetItem.getOperation())) {
					String assetAmount = assetItem.getAmount().replace("$", "")
							.replace(",", "");
					FinancialAsset asset = this.financialProfileService
							.createAsset(offender, assetItem.getCategory(), 
									assetItem.getReportedDate(), 
									assetItem.getDescription(), 
									new BigDecimal(assetAmount));
					System.out.println(asset.getId());
				} else if (ItemOperation.UPDATE.equals(assetItem
						.getOperation())) {
					String assetAmount = assetItem.getAmount().replace("$", "")
							.replace(",", "");
					this.financialProfileService.updateAsset(
							assetItem.getFinancialAsset(), offender, 
							assetItem.getCategory(), 
							assetItem.getReportedDate(), 
							assetItem.getDescription(), 
							new BigDecimal(assetAmount));
				} else if (ItemOperation.REMOVE.equals(assetItem
						.getOperation())) {
					this.financialProfileService.removeAsset(
							assetItem.getFinancialAsset());
				}
			}
		}
	}
	
	/*
	 * Processes Liability items
	 * @param items list of financial liability items to process
	 * @param offender offender
	 * @throws DuplicateEntityFoundException
	 */
	private void processLiabilityChanges(
			final List<FinancialLiabilityItem> items,
			final Offender offender) throws DuplicateEntityFoundException {
		if (items != null) {
			for (FinancialLiabilityItem liabilityItem : items) {
				if (ItemOperation.CREATE.equals(liabilityItem.getOperation())) {
					String liabilityAmount = liabilityItem.getAmount()
							.replace("$", "").replace(",", "");
					this.financialProfileService.createLiability(offender, 
							liabilityItem.getCategory(), 
							liabilityItem.getReportedDate(), 
							liabilityItem.getDescription(), 
							new BigDecimal(liabilityAmount));
				} else if (ItemOperation.UPDATE.equals(liabilityItem
						.getOperation())) {
					String liabilityAmount = liabilityItem.getAmount()
							.replace("$", "").replace(",", "");
					this.financialProfileService.updateLiability(
							liabilityItem.getFinancialLiability(), offender, 
							liabilityItem.getCategory(), 
							liabilityItem.getReportedDate(), 
							liabilityItem.getDescription(), 
							new BigDecimal(liabilityAmount));
				} else if (ItemOperation.REMOVE.equals(liabilityItem
						.getOperation())) {
					this.financialProfileService.removeLiability(
							liabilityItem.getFinancialLiability());
				}
			}
		}
	}
	
	/*
	 * Processes recurring deduction items
	 * @param items list of recurring deduction items to process
	 * @param offender offender
	 * @throws DuplicateEntityFoundException
	 */
	private void processRecurringDeductionChanges(
			final List<RecurringDeductionItem> items,
			final Offender offender) throws DuplicateEntityFoundException {
		if (items != null) {
			for (RecurringDeductionItem recurringDeductionItem : items) {
				if (ItemOperation.CREATE.equals(
					recurringDeductionItem.getOperation())) {
					String recurringDeductionAmount = recurringDeductionItem
						.getAmount().replace("$", "").replace(",", "");
					this.financialProfileService.createRecurringDeduction(
						offender, recurringDeductionItem.getCategory(), 
						recurringDeductionItem.getReportedDate(), 
						recurringDeductionItem.getDescription(), 
						new BigDecimal(recurringDeductionAmount), 
						recurringDeductionItem.getFrequency());
				} else if (ItemOperation.UPDATE.equals(recurringDeductionItem
						.getOperation())) {
					String recurringDeductionAmount = recurringDeductionItem
						.getAmount().replace("$", "").replace(",", "");
					this.financialProfileService.updateRecurringDeduction(
						recurringDeductionItem.getRecurringDeduction(), 
						recurringDeductionItem.getCategory(), 
						recurringDeductionItem.getReportedDate(), 
						recurringDeductionItem.getDescription(), 
						new BigDecimal(recurringDeductionAmount), 
						recurringDeductionItem.getFrequency());
				} else if (ItemOperation.REMOVE.equals(recurringDeductionItem
						.getOperation())) {
					this.financialProfileService.removeRecurringDeduction(
						recurringDeductionItem.getRecurringDeduction());
				}
			}
		}
	}
	
	/**
	 * Processes financial document association items
	 * @param items list of financial document association items to process
	 * @param offender offender
	 * @throws DuplicateEntityFoundException
	 */
	private void processFinancialDocumentAssociationChanges(
			final List<FinancialDocumentItem> items, final Offender offender)
					throws DuplicateEntityFoundException {
		if (items != null) {
			for (FinancialDocumentItem financialDocumentItem : items) {
				if (ItemOperation.CREATE.equals(
						financialDocumentItem.getOperation())) {
					final String fileExtension = financialDocumentItem
							.getFileExtension();
					this.documentFilenameGenerator.setExtension(fileExtension);
					final String filename = this.documentFilenameGenerator
							.generate();
					Document document = this.financialProfileService
							.createDocument(financialDocumentItem.getDate(),
									filename, fileExtension,
									financialDocumentItem.getTitle());
					this.financialDocumentPersister.persist(document, 
							financialDocumentItem.getData());
					this.financialProfileService.createFinancialDocument(
							offender, document);
					processDocumentTags(
							financialDocumentItem.getDocumentTagItems(), 
							document);
				}
				
				if (ItemOperation.UPDATE.equals(
						financialDocumentItem.getOperation())) {
					Document document = this.financialProfileService
							.updateDocument(financialDocumentItem.getDocument(), 
									financialDocumentItem.getDate(), 
									financialDocumentItem.getTitle());
					this.financialProfileService.updateFinancialDocument(
							financialDocumentItem
							.getFinancialDocumentAssociation(), document);
					if(financialDocumentItem.getDocumentTagItems() != null) {
						processDocumentTags(
								financialDocumentItem.getDocumentTagItems(), 
								document);
					}
				}
				
				if (ItemOperation.REMOVE.equals(financialDocumentItem
						.getOperation())) {
					processDocumentTags(financialDocumentItem
							.getDocumentTagItems(), null);
					this.financialProfileService.removeFinancialDocument(
							financialDocumentItem
							.getFinancialDocumentAssociation());
					this.financialDocumentRemover.remove(
							financialDocumentItem.getDocument().getFilename());
					this.financialProfileService
							.removeDocument(financialDocumentItem.getDocument());
					}
				}
			}
		}
			
	/**
	 * Processes a list of documentTag items for creation, updating, or removal
	 * @param documentTagItems - List of DocumentTagItems
	 * @param document - Document for which the documentTags are being processed
	 * for
	 * @throws DuplicateEntityFoundException - When a document tag already exists
	 * with given name and document
	 */
	private void processDocumentTags(
			final List<DocumentTagItem> documentTagItems, 
			final Document document) throws DuplicateEntityFoundException {
		if(documentTagItems != null){
			Iterator<DocumentTagItem> documentTagIterator = 
					documentTagItems.iterator();
			while (documentTagIterator.hasNext()) {
				final DocumentTagItem documentTagItem = documentTagIterator.next();
				final DocumentTagOperation documentTagOperation = 
						documentTagItem.getDocumentTagOperation();
				if (documentTagOperation == DocumentTagOperation.UPDATE) {
					this.financialProfileService.updateDocumentTag(
							documentTagItem.getDocumentTag(), 
							documentTagItem.getName());
				} else if (documentTagOperation == DocumentTagOperation.CREATE) {
					this.financialProfileService.createDocumentTag(
							document, documentTagItem.getName());
				} else if (documentTagOperation == DocumentTagOperation.REMOVE) {
					this.financialProfileService.removeDocumentTag(
							documentTagItem.getDocumentTag());
				}
			}
		}
	}
	
	/** Retrieves document file.
	 * @param document - document
	 * @param httpServletResponse - HTTP Servlet response. */
	@RequestMapping(value = "financialDocumentAssociation/retrieveFile.html", 
			method = RequestMethod.GET)
	@PreAuthorize("(hasRole('DOCUMENT_VIEW') and hasRole('USER')) "
			+ "or hasRole('ADMIN')")
	public void retrieveFile(
			@RequestParam(value = "document", required = true) 
			final Document document, 
			final HttpServletResponse httpServletResponse) {
		final byte[] bytes = this.financialDocumentRetriever.retrieve(document);
		httpServletResponse.setContentType("application/octet-stream");
		httpServletResponse.setHeader("Content-Disposition", 
				"attachment; filename=\"" + document.getFilename() + "\"");
		try {
			OutputStream outputStream = httpServletResponse.getOutputStream();
			outputStream.write(bytes);
			outputStream.flush();
			outputStream.close();
		} catch (IOException ioException) {
			throw new RuntimeException("Error writing file to disk.");
		}
	}
	
	/**
	 * Displays a DocumentTag item
	 * @param financialDocumentItemIndex - Integer
	 * @param documentTagItemIndex - Integer
	 * @return ModelAndView - DocumentTag item content
	 */
	@RequestMapping(value = "createDocumentTagItem.html",
			method = RequestMethod.GET)
	public ModelAndView displayDocumentTagItemRow(@RequestParam(
			value = "financialDocumentAssociationItemIndex",
				required = true) final Integer financialDocumentItemIndex,
			@RequestParam(value = "documentTagItemIndex", required = true)
					final Integer documentTagItemIndex) {
		ModelMap map = new ModelMap();
		DocumentTagItem tagItem = new DocumentTagItem();
		tagItem.setDocumentTagOperation(DocumentTagOperation.CREATE);
		map.addAttribute(FINANCIAL_DOCUMENT_ITEM_INDEX_MODEL_KEY,
			financialDocumentItemIndex);
		map.addAttribute(DOCUMENT_TAG_ITEM_MODEL_KEY, tagItem);
		map.addAttribute(DOCUMENT_TAG_ITEM_INDEX_MODEL_KEY,	
				documentTagItemIndex);
		return new ModelAndView(DOCUMENT_TAG_ITEM_CONTENT_VIEW_NAME, map);
	}
	
	/**
	 * Displays a FinancialDocumentAssociation item
	 * @param financialDocumentAssociationItemIndex - Integer
	 * @return ModelAndView - FinancialDocumentAssociation item  content
	 */
	@RequestMapping(value =
			"createFinancialDocumentAssociationItem.html",
			method = RequestMethod.GET)
	public ModelAndView displayFinancialDocumentAssociationItem(
			@RequestParam(
			value = "financialDocumentAssociationItemIndex",
			required = true)
			final Integer financialDocumentAssociationItemIndex){
		ModelMap map = new ModelMap();
		FinancialDocumentItem associableDocumentItem 
			= new FinancialDocumentItem();
		associableDocumentItem.setOperation(ItemOperation.CREATE);
		map.addAttribute(FINANCIAL_DOCUMENT_ITEM_MODEL_KEY,
				associableDocumentItem);
		map.addAttribute(FINANCIAL_DOCUMENT_ITEM_INDEX_MODEL_KEY,
			financialDocumentAssociationItemIndex);
		return new ModelAndView(FINANCIAL_DOCUMENT_ITEM_CONTENT_VIEW_NAME, map);
	}
	
	/* InitBinder */
	
	/**
	 * Sets up and registers property editors.
	 * 
	 * @param binder web binder
	 */
	@InitBinder
	protected void initBinder(final WebDataBinder binder) {
		binder.registerCustomEditor(Offender.class, 
				this.offenderPropertyEditorFactory
					.createOffenderPropertyEditor());
		binder.registerCustomEditor(FinancialAsset.class, 
				this.financialAssetPropertyEditorFactory
					.createPropertyEditor());
		binder.registerCustomEditor(FinancialAssetCategory.class, 
				this.financialAssetCategoryPropertyEditorFactory
					.createPropertyEditor());
		binder.registerCustomEditor(FinancialLiability.class, 
				this.financialLiabilityPropertyEditorFactory
					.createPropertyEditor());
		binder.registerCustomEditor(FinancialLiabilityCategory.class, 
				this.financialLiabilityCategoryPropertyEditorFactory
					.createPropertyEditor());
		binder.registerCustomEditor(RecurringDeductionCategory.class, 
				this.recurringDeductionCategoryPropertyEditorFactory
					.createPropertyEditor());
		binder.registerCustomEditor(RecurringDeduction.class, 
				this.recurringDeductionPropertyEditorFactory
					.createPropertyEditor());
		binder.registerCustomEditor(FinancialDocumentAssociation.class, 
				this.financialDocumentAssociationPropertyEditorFactory
				.createPropertyEditor());
		binder.registerCustomEditor(Document.class, 
				this.documentPropertyEditorFactory.createPropertyEditor());
		binder.registerCustomEditor(DocumentTag.class, 
				this.documentTagPropertyEditorFactory.createPropertyEditor());
		binder.registerCustomEditor(byte[].class,
				new ByteArrayMultipartFileEditor());
	}
}
