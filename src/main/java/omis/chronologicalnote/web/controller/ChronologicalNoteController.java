package omis.chronologicalnote.web.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

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

import omis.beans.factory.PropertyEditorFactory;
import omis.beans.factory.spring.CustomDateEditorFactory;
import omis.chronologicalnote.domain.ChronologicalNote;
import omis.chronologicalnote.domain.ChronologicalNoteCategory;
import omis.chronologicalnote.domain.ChronologicalNoteCategoryGroup;
import omis.chronologicalnote.domain.ChronologicalNoteCategoryGroupTemplate;
import omis.chronologicalnote.domain.ChronologicalNoteCategoryTemplate;
import omis.chronologicalnote.exception.ChronologicalNoteExistsException;
import omis.chronologicalnote.service.ChronologicalNoteService;
import omis.chronologicalnote.web.form.ChronologicalNoteCategoryItem;
import omis.chronologicalnote.web.form.ChronologicalNoteCategoryItemOperation;
import omis.chronologicalnote.web.form.ChronologicalNoteForm;
import omis.chronologicalnote.web.validator.ChronologicalNoteFormValidator;
import omis.offender.beans.factory.OffenderPropertyEditorFactory;
import omis.offender.domain.Offender;
import omis.offender.web.controller.delegate.OffenderSummaryModelDelegate;
import omis.util.DateManipulator;

/**
 * Chronological note controller.
 * 
 * @author Joel Norris
 * @author Stephen Abson
 * @author Sheronda Vaughn
 * @version 0.1.0 (February 6, 2018)
 * @since OMIS 3.0
 */
@Controller
@RequestMapping("/chronologicalNote")
@PreAuthorize("hasRole('USER')")
public class ChronologicalNoteController {
	
	/* Redirect URLs. */
	
	private static final String CHRONOLOGICAL_NOTE_LIST_REDIRECT_URL = 
			"redirect:list.html?offender=%d";
	
	/* View names. */
	
	private static final String EDIT_VIEW_NAME = "chronologicalNote/edit";
	private static final String CHRONOLOGICAL_NOTE_ACTION_MENU_VIEW_NAME 
		= "chronologicalNote/includes/chronologicalNoteActionMenu";
	private static final String BOOLEAN_VIEW_NAME = "common/json/booleanValue";
	private static final String NULL_VALUE_VIEW_NAME = "common/json/nullValue";
	private static final String CATEGORY_TEMPLATES_VIEW_NAME 
		= "chronologicalNote/json/chronologicalNoteCategoryTemplates";
	
	/* Model keys. */
	
	private static final String CHRONOLOGICAL_NOTE_FORM_MODEL_KEY 
		= "chronologicalNoteForm";
	private static final String OFFENDER_MODEL_KEY = "offender";
	private static final String CHRONOLOGICAL_NOTE_MODEL_KEY 
		= "chronologicalNote";
	private static final String BOOLEAN_VALUE_MODEL_KEY = "booleanValue";
	private static final String GROUP_TEMPLATES_MODEL_KEY = "groupTemplates";
	private static final String CATEGORY_TEMPLATE_MODEL_KEY 
		= "categoryTemplate";
	private static final String GROUPS_MODEL_KEY = "groups";
	private static final String GROUP_CATEGORY_MAP_MODEL_KEY 
		= "groupCategoryMap";
	
	/* Services. */
	
	@Autowired
	@Qualifier("chronologicalNoteService")
	private ChronologicalNoteService chronologicalNoteService;
	
	/* Validators. */
	
	@Autowired
	@Qualifier("chronologicalNoteFormValidator")
	private ChronologicalNoteFormValidator chronologicalNoteFormValidator;
	
	/* Property editors. */
	
	@Autowired
	@Qualifier("datePropertyEditorFactory")
	private CustomDateEditorFactory customDateEditorFactory;
	
	@Autowired
	@Qualifier("offenderPropertyEditorFactory")
	private OffenderPropertyEditorFactory offenderPropertyEditorFactory;
	
	@Autowired
	@Qualifier("chronologicalNotePropertyEditorFactory")
	private PropertyEditorFactory chronologicalNotePropertyEditorFactory;
	
	@Autowired
	@Qualifier("chronologicalNoteCategoryPropertyEditorFactory")
	private PropertyEditorFactory 
		chronologicalNoteCategoryPropertyEditorFactory;
	
	/* Helpers. */
	
	@Autowired
	@Qualifier("offenderSummaryModelDelegate")
	private OffenderSummaryModelDelegate offenderSummaryModelDelegate;
		
	/* Constructor. */
	
	/**
	 * Instantiates a default instance of chronological note controller.
	 */
	public ChronologicalNoteController() {
		//Default constructor.
	}
	
	/* URL Mapped methods. */
	
	/**
	 * Returns model and view for creating a new chronological note.
	 * 
	 * @param offender offender
	 * @return model and view for creating chronological note
	 */
	@RequestMapping(value = "create.html",
			method = RequestMethod.GET)
	@PreAuthorize("hasRole('ADMIN') or hasRole('CHRONOLOGICAL_NOTE_CREATE')")
	public ModelAndView create(@RequestParam(value = "offender", 
		required = true)final Offender offender) {
		ModelMap map = new ModelMap();
		ChronologicalNoteForm form = new ChronologicalNoteForm();		
		form.setItems(this.createCategoryItems(null));
		return this.prepareEditMav(map, form, offender, null);
	}
	
	/**
	 * Creates a chronological note for the specified offender with valued 
	 * from the specified chronological note form, and supplies a model and view
	 * for the chronological note list screen redirect.
	 * 
	 * @param offender offender
	 * @param form chronological note form
	 * @param result binding result
	 * @return model and view for chronological note list screen redirect
	 * @throws ChronologicalNoteExistsException Thrown when a duplicate 
	 * chronological note is found.
	 */
	@RequestMapping(value = "create.html",
			method = RequestMethod.POST)
	@PreAuthorize("hasRole('ADMIN') or hasRole('CHRONOLOGICAL_NOTE_CREATE')")
	public ModelAndView save(@RequestParam(value = "offender", required = true)
			final Offender offender, final ChronologicalNoteForm form, 
			final BindingResult result)
		throws ChronologicalNoteExistsException {
		this.chronologicalNoteFormValidator.validate(form, result);
		if (result.hasErrors()) {
			ModelMap map = new ModelMap();
			map.addAttribute(BindingResult.MODEL_KEY_PREFIX
					+ CHRONOLOGICAL_NOTE_FORM_MODEL_KEY, result);
			return this.prepareEditMav(map, form, offender, null); 
		}
		
		ChronologicalNote note = this.chronologicalNoteService.create(
				DateManipulator.getDateAtTimeOfDay(form.getDate(), 
						form.getDateTime()), offender, form.getTitle(), 
				form.getNarrative());
		this.processCategoryItems(form.getItems(), note);
		return new ModelAndView(String.format(
				CHRONOLOGICAL_NOTE_LIST_REDIRECT_URL, offender.getId()));
	}
	
	/**
	 * Returns a model and view for editing the specified chronological note.
	 * 
	 * @param note chronological note
	 * @return model and view for editing chronological note
	 */
	@RequestMapping(value = "edit.html",
			method = RequestMethod.GET)
	@PreAuthorize("hasRole('ADMIN') or hasRole('CHRONOLOGICAL_NOTE_VIEW')")
	public ModelAndView edit(@RequestParam(value = "chronologicalNote", 
		required = true) final ChronologicalNote note) {
		ModelMap map = new ModelMap();
		ChronologicalNoteForm form = new ChronologicalNoteForm();
		form.setTitle(note.getTitle());
		form.setDate(note.getDate());
		form.setDateTime(note.getDate());
		form.setNarrative(note.getNarrative());
		form.setItems(this.createCategoryItems(this.chronologicalNoteService
				.findAssociatedCategories(note)));
		map.addAttribute(CHRONOLOGICAL_NOTE_MODEL_KEY, note);
		return this.prepareEditMav(map, form, note.getOffender(), 
				this.chronologicalNoteService.findAssociatedCategories(note));
	}
	
	/**
	 * Updates the specified chronological note with valued from the specified 
	 * chronological note form, and supplies a model and view for the 
	 * chronological note list screen redirect.
	 * 
	 * @param note chronological note
	 * @param form chronological note form
	 * @param result binding result
	 * @return model and view redirect to chronological note list screen
	 * @throws ChronologicalNoteExistsException Thrown when a duplicate 
	 * chronological note is found.
	 */
	@RequestMapping(value = "edit.html",
			method = RequestMethod.POST)
	@PreAuthorize("#note.creationSignature.userAccount.username "
			+ "== authentication.name"
			+ " and (hasRole('ADMIN') or hasRole('CHRONOLOGICAL_NOTE_EDIT'))")
	public ModelAndView update(@RequestParam(value = "chronologicalNote", 
		required = true)
		final ChronologicalNote note, final ChronologicalNoteForm form, 
		final BindingResult result)
		throws ChronologicalNoteExistsException {
		this.chronologicalNoteFormValidator.validate(form, result);
		if (result.hasErrors()) {
			ModelMap map = new ModelMap();
			map.addAttribute(BindingResult.MODEL_KEY_PREFIX
					+ CHRONOLOGICAL_NOTE_FORM_MODEL_KEY, result);
			map.addAttribute(CHRONOLOGICAL_NOTE_MODEL_KEY, note);
			return this.prepareEditMav(map, form, note.getOffender(), 
					this.chronologicalNoteService.findAssociatedCategories(
							note)); 
		}
		this.chronologicalNoteService.update(note, 
				DateManipulator.getDateAtTimeOfDay(form.getDate(), 
						form.getDateTime()), form.getTitle(), 
				form.getNarrative());
		this.processCategoryItems(form.getItems(), note);
		return new ModelAndView(String.format(
				CHRONOLOGICAL_NOTE_LIST_REDIRECT_URL, note.getOffender()
				.getId()));
	}
	
	/**
	 * Remove note.
	 * @param note note
	 * @return to list screen
	 */
	@RequestMapping(value = "/remove.html", method = RequestMethod.GET)
	@PreAuthorize("hasRole('ADMIN') or hasRole('CHRONOLOGICAL_NOTE_REMOVE')")
	public ModelAndView remove(@RequestParam(value = "chronologicalNote",
		required = true) final ChronologicalNote note) {
		for (ChronologicalNoteCategory category
				: this.chronologicalNoteService.findAssociatedCategories(
						note)) {
			this.chronologicalNoteService.dissociateCategory(note, category);
		}
		this.chronologicalNoteService.remove(note);
		return new ModelAndView(String.format(
			CHRONOLOGICAL_NOTE_LIST_REDIRECT_URL, note.getOffender().getId()));
	}
	
	/**
	 * Return model and view of chronological note action menu.
	 *
	 * @param offender offender
	 * @return new model and view
	 */
	@RequestMapping(value = "/chronologicalNoteActionMenu.html", 
			method = RequestMethod.GET)
	public ModelAndView displayChronologicalNoteActionMenu(@RequestParam(
			value = "offender", required = true)
			final Offender offender) {
		ModelMap map = new ModelMap();
		map.addAttribute(OFFENDER_MODEL_KEY, offender);
		return new ModelAndView(CHRONOLOGICAL_NOTE_ACTION_MENU_VIEW_NAME, map);
	}
	
	/**
	 * Reserved for extending session.
	 * 
	 * @return boolean
	 */
	@RequestMapping(value = "/extendSession.html", method = RequestMethod.GET)
	public ModelAndView extendSession() {
		ModelMap map = new ModelMap();
		map.addAttribute(BOOLEAN_VALUE_MODEL_KEY, true);
		return new ModelAndView(BOOLEAN_VIEW_NAME, map);
	}
	
	/**
	 * Returns templates for category or null.
	 * 
	 * @param category category
	 * @return templates for category or null
	 */
	@RequestMapping(value = "/findTemplates.json", method = RequestMethod.GET)
	public ModelAndView findTemplates(
			@RequestParam(value = "category", required = true)
				final ChronologicalNoteCategory category) {
		ChronologicalNoteCategoryTemplate categoryTemplate
			= this.chronologicalNoteService.findCategoryTemplate(category);
		List<ChronologicalNoteCategoryGroupTemplate> groupTemplates
			= this.chronologicalNoteService.findTemplatesByGroup(
					category.getGroup());
		ModelAndView mav;
		if (categoryTemplate != null || !groupTemplates.isEmpty()) {
			mav = new ModelAndView(CATEGORY_TEMPLATES_VIEW_NAME);
			if (categoryTemplate != null) {
				mav.addObject(CATEGORY_TEMPLATE_MODEL_KEY, categoryTemplate);
			}
			if (!groupTemplates.isEmpty()) {
				mav.addObject(GROUP_TEMPLATES_MODEL_KEY, groupTemplates);
			}
		} else {
			mav = new ModelAndView(NULL_VALUE_VIEW_NAME);
		}
		return mav;
	}
	
	/* Helper methods. */
	
	/*
	 * Prepares a model and view for editing or creating a chronological note.
	 * 
	 * @param map model map
	 * @param form chronological note form
	 * @param offender offender
	 * @return model and view
	 */
	private ModelAndView prepareEditMav(final ModelMap map, 
			final ChronologicalNoteForm form,
			final Offender offender, final List<ChronologicalNoteCategory> 
				categories) {
		map.addAttribute(CHRONOLOGICAL_NOTE_FORM_MODEL_KEY, form);
		map.addAttribute(OFFENDER_MODEL_KEY, offender);
		map.addAttribute(GROUPS_MODEL_KEY, this.chronologicalNoteService
				.findGroups());
		this.offenderSummaryModelDelegate.add(map, offender);
		map.addAttribute(GROUP_CATEGORY_MAP_MODEL_KEY, 
				this.createCategoryItemsMap(categories, form.getItems()));
		return new ModelAndView(EDIT_VIEW_NAME, map);
	}
	
	/*
	 * Processes chronological note category items according to their operation.
	 * 
	 * @param items chronological note category items
	 * @param note chronological note
	 */
	private void processCategoryItems(
			final List<ChronologicalNoteCategoryItem> items,
			final ChronologicalNote note) {
		for (ChronologicalNoteCategoryItem item : items) {
			if (item.getOperation() != null) {
				if (ChronologicalNoteCategoryItemOperation.ASSOCIATE.equals(
						item.getOperation())) {
					this.chronologicalNoteService.associateCategory(
							note, item.getCategory());	
				} else if (ChronologicalNoteCategoryItemOperation.DISSOCIATE
						.equals(item.getOperation())) {
					this.chronologicalNoteService.dissociateCategory(
							note, item.getCategory());
				} else {
					throw new UnsupportedOperationException(
							"Unsupported chronological note category "
							+ "item operation");
				}				
			}			
		}
	}
	
	/*
	 * Creates chronological note category items for all chronological 
	 * note categories, and sets whether associated applies
	 * to the specified associated categories.
	 *  
	 * @param associatedCategories associated chronological note categories
	 * @return list of chronological note category items
	 */
	private List<ChronologicalNoteCategoryItem> createCategoryItems(
			final List<ChronologicalNoteCategory> associatedCategories) {
		List<ChronologicalNoteCategoryItem> items 
			= new ArrayList<ChronologicalNoteCategoryItem>();
		List<ChronologicalNoteCategory> categories 
			= this.chronologicalNoteService.findCategories();
		for (ChronologicalNoteCategory category : categories) {
			Boolean associated = false;
			if (associatedCategories != null && associatedCategories.contains(
					category)) {
				associated = true;
			}	
			items.add(new ChronologicalNoteCategoryItem(category, null, 
					category.getName(), associated));
		}
		return items;
	}
	
	/*
	 * Creates chronological note category items for all chronological note 
	 * categories, sets whether associated applies to the specified associated 
	 * categories, and assigns them to a map according to group.
	 * 
	 * @param associatedCategories associated chronological note categories
	 * @return map of chronological note category items by chronological note 
	 * category group
	 */
	private HashMap<String, List<ChronologicalNoteCategoryItem>> 
		createCategoryItemsMap(
			final List<ChronologicalNoteCategory> associatedCategories,
			final List<ChronologicalNoteCategoryItem> formItems) {
		HashMap<String, List<ChronologicalNoteCategoryItem>> itemMap
			= new HashMap<String, List<ChronologicalNoteCategoryItem>>();
		List<ChronologicalNoteCategoryGroup> groups 
			= this.chronologicalNoteService.findCategoryGroups();
		if (formItems != null) {
			for (ChronologicalNoteCategoryGroup group : groups) {
				List<ChronologicalNoteCategoryItem> items 
					= new ArrayList<ChronologicalNoteCategoryItem>();
				for (ChronologicalNoteCategoryItem item : formItems) {
					if (group == item.getCategory().getGroup()) {
						items.add(item);
					}
				}
				itemMap.put(group.getName(), items);
			}
		} else {
			for (ChronologicalNoteCategoryGroup group : groups) {
				List<ChronologicalNoteCategoryItem> items 
					= new ArrayList<ChronologicalNoteCategoryItem>();
				for (ChronologicalNoteCategory category 
						: this.chronologicalNoteService.findCategoriesByGroup(
							group)) {
					Boolean associated = false;
					if (associatedCategories != null 
							&& associatedCategories.contains(category)) {
						associated = true;
					}	
					items.add(new ChronologicalNoteCategoryItem(
							category, null, category.getName(), associated));
				}
				itemMap.put(group.getName(), items);
			}
		}
		return itemMap;
	}
	
	/**
	 * Sets up and registers property editors.
	 * 
	 * @param binder web binder
	 */
	@InitBinder
	protected void initBinder(final WebDataBinder binder) {
		binder.registerCustomEditor(
				Offender.class,
				this.offenderPropertyEditorFactory
				.createOffenderPropertyEditor());
		binder.registerCustomEditor(
				Date.class, "date",
				this.customDateEditorFactory
				.createCustomDateOnlyEditor(true));
		binder.registerCustomEditor(
				Date.class, "dateTime",
				this.customDateEditorFactory
				.createCustomTimeOnlyEditor(true));
		binder.registerCustomEditor(
				ChronologicalNote.class,
				this.chronologicalNotePropertyEditorFactory
				.createPropertyEditor());
		binder.registerCustomEditor(
				ChronologicalNoteCategory.class,
				this.chronologicalNoteCategoryPropertyEditorFactory
				.createPropertyEditor());
	}
}