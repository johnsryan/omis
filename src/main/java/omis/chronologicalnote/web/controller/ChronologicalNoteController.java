package omis.chronologicalnote.web.controller;

import java.util.ArrayList;
import java.util.Date;
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
import omis.chronologicalnote.exception.ChronologicalNoteExistsException;
import omis.chronologicalnote.service.ChronologicalNoteService;
import omis.chronologicalnote.web.form.ChronologicalNoteCategoryItem;
import omis.chronologicalnote.web.form.ChronologicalNoteCategoryItemOperation;
import omis.chronologicalnote.web.form.ChronologicalNoteForm;
import omis.chronologicalnote.web.validator.ChronologicalNoteFormValidator;
import omis.offender.beans.factory.OffenderPropertyEditorFactory;
import omis.offender.domain.Offender;
import omis.offender.web.controller.delegate.OffenderSummaryModelDelegate;

/**
 * Chronological note controller.
 * 
 * @author Joel Norris
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
	
	/* Model keys. */
	
	private static final String CHRONOLOGICAL_NOTE_FORM_MODEL_KEY = "chronologicalNoteForm";
	private static final String OFFENDER_MODEL_KEY = "offender";
	private static final String CHRONOLOGICAL_NOTE_MODEL_KEY = "chronologicalNote";
	
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
	private CustomDateEditorFactory customDateEditorFactory;
	
	@Autowired
	@Qualifier("offenderPropertyEditorFactory")
	private OffenderPropertyEditorFactory offenderPropertyEditorFactory;
	
	@Autowired
	@Qualifier("chronologicalNotePropertyEditorFactory")
	private PropertyEditorFactory chronologicalNotePropertyEditorFactory;
	
	@Autowired
	@Qualifier("chronologicalNoteCategoryPropertyEditorFactory")
	private PropertyEditorFactory chronologicalNoteCategoryPropertyEditorFactory;
	
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
	public ModelAndView create(@RequestParam(value = "offender", required = true)final Offender offender) {
		ModelMap map = new ModelMap();
		ChronologicalNoteForm form = new ChronologicalNoteForm();
		form.setItems(this.createCategoryItems(null));
		return this.prepareEditMav(map, form, offender);
	}
	
	/**
	 * Creates a chronological note for the specified offender with valued from the specified chronological
	 * note form, and supplies a model and view for the chronological note list screen redirect.
	 * 
	 * @param offender offender
	 * @param form chronological note form
	 * @param result binding result
	 * @return model and view for chronological note list screen redirect
	 * @throws ChronologicalNoteExistsException Thrown when a duplicate chronological note is found.
	 */
	@RequestMapping(value = "create.html",
			method = RequestMethod.POST)
	@PreAuthorize("hasRole('ADMIN') or hasRole('CHRONOLOGICAL_NOTE_CREATE')")
	public ModelAndView save(@RequestParam(value = "offender", required = true)
			final Offender offender, final ChronologicalNoteForm form, final BindingResult result)
		throws ChronologicalNoteExistsException {
		this.chronologicalNoteFormValidator.validate(form, result);
		if(result.hasErrors()) {
			ModelMap map = new ModelMap();
			map.addAttribute(BindingResult.MODEL_KEY_PREFIX
					+ CHRONOLOGICAL_NOTE_FORM_MODEL_KEY, result);
			return this.prepareEditMav(map, form, offender); 
		}
		ChronologicalNote note = this.chronologicalNoteService.create(
				form.getDate(), offender, form.getNarrative());
		this.processCategoryItems(form.getItems(), note);
		return new ModelAndView(String.format(CHRONOLOGICAL_NOTE_LIST_REDIRECT_URL, offender.getId()));
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
	public ModelAndView edit(@RequestParam(value = "chronologicalNote", required = true)
			final ChronologicalNote note) {
		ModelMap map = new ModelMap();
		ChronologicalNoteForm form = new ChronologicalNoteForm();
		form.setDate(note.getDate());
		form.setNarrative(note.getNarrative());
		form.setItems(this.createCategoryItems(this.chronologicalNoteService.findAssociatedCategories(note)));
		map.addAttribute(CHRONOLOGICAL_NOTE_MODEL_KEY, note);
		return this.prepareEditMav(map, form, note.getOffender());
	}
	
	/**
	 * Updates the specified chronological note with valued from the specified chronological note form,
	 * and supplies a model and view for the chronological note list screen redirect.
	 * 
	 * @param note chronological note
	 * @param form chronological note form
	 * @param result binding result
	 * @return model and view redirect to chronological note list screen
	 * @throws ChronologicalNoteExistsException Thrown when a duplicate chronological note is found.
	 */
	@RequestMapping(value = "edit.html",
			method = RequestMethod.POST)
	@PreAuthorize("#note.creationSignature.userAccount.username == authentication.name"
			+ " and (hasRole('ADMIN') or hasRole('CHRONOLOGICAL_NOTE_EDIT'))")
	public ModelAndView update(@RequestParam(value = "chronologicalNote", required = true)
	final ChronologicalNote note, final ChronologicalNoteForm form, final BindingResult result)
		throws ChronologicalNoteExistsException {
		this.chronologicalNoteFormValidator.validate(form, result);
		if(result.hasErrors()) {
			ModelMap map = new ModelMap();
			map.addAttribute(BindingResult.MODEL_KEY_PREFIX
					+ CHRONOLOGICAL_NOTE_FORM_MODEL_KEY, result);
			return this.prepareEditMav(map, form, note.getOffender()); 
		}
		this.chronologicalNoteService.update(note, form.getDate(), form.getNarrative());
		this.processCategoryItems(form.getItems(), note);
		return new ModelAndView(String.format(CHRONOLOGICAL_NOTE_LIST_REDIRECT_URL, note.getOffender().getId()));
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
	private ModelAndView prepareEditMav(final ModelMap map, final ChronologicalNoteForm form
			, final Offender offender) {
		map.addAttribute(CHRONOLOGICAL_NOTE_FORM_MODEL_KEY, form);
		map.addAttribute(OFFENDER_MODEL_KEY, offender);
		this.offenderSummaryModelDelegate.add(map, offender);
		return new ModelAndView(EDIT_VIEW_NAME, map);
	}
	
	/*
	 * Processes chronological note category items according to their operation.
	 * 
	 * @param items chronological note category items
	 * @param note chronological note
	 */
	private void processCategoryItems(List<ChronologicalNoteCategoryItem> items, ChronologicalNote note) {
		for(ChronologicalNoteCategoryItem item : items) {
			if (item.getOperation() != null) {
				if (ChronologicalNoteCategoryItemOperation.ASSOCIATE.equals(item.getOperation())) {
					this.chronologicalNoteService.associateCategory(note, item.getCategory());
				} else if (ChronologicalNoteCategoryItemOperation.DISSOCIATE.equals(item.getOperation())) {
					this.chronologicalNoteService.dissociateCategory(note, item.getCategory());
				} else {
					throw new UnsupportedOperationException(
							"Unsupported chronological note category item operation");
				}
			}
		}
	}
	
	/*
	 * Creates chronological note category items for all chronological note categories, and sets whether associated applies
	 * to the specified associated categories.
	 *  
	 * @param associatedCategories associated chronological note categories
	 * @return list of chronological note category items
	 */
	private List<ChronologicalNoteCategoryItem> createCategoryItems(
			final List<ChronologicalNoteCategory> associatedCategories) {
		List<ChronologicalNoteCategoryItem> items = new ArrayList<ChronologicalNoteCategoryItem>();
		List<ChronologicalNoteCategory> categories = this.chronologicalNoteService.findCategories();
		for(ChronologicalNoteCategory category : categories) {
			Boolean associated = false;
			if (associatedCategories != null && associatedCategories.contains(category)) {
				associated = true;
			}	
			items.add(new ChronologicalNoteCategoryItem(category, null, category.getName(), associated));
		}
		return items;
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
				Date.class,
				this.customDateEditorFactory
				.createCustomDateOnlyEditor(true));
		binder.registerCustomEditor(
				ChronologicalNote.class,
				this.chronologicalNotePropertyEditorFactory
				.createPropertyEditor());
		binder.registerCustomEditor(ChronologicalNoteCategory.class, this.chronologicalNoteCategoryPropertyEditorFactory.createPropertyEditor());
	}
}