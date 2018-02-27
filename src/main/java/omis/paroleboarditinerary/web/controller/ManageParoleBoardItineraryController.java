package omis.paroleboarditinerary.web.controller;

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
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import omis.beans.factory.PropertyEditorFactory;
import omis.beans.factory.spring.CustomDateEditorFactory;
import omis.datatype.DateRange;
import omis.exception.DuplicateEntityFoundException;
import omis.location.domain.Location;
import omis.paroleboarditinerary.domain.AttendeeRoleCategory;
import omis.paroleboarditinerary.domain.BoardAttendee;
import omis.paroleboarditinerary.domain.BoardMeetingSite;
import omis.paroleboarditinerary.domain.ParoleBoardItinerary;
import omis.paroleboarditinerary.domain.ParoleBoardItineraryNote;
import omis.paroleboarditinerary.service.ParoleBoardItineraryService;
import omis.paroleboarditinerary.web.form.BoardMeetingSiteItem;
import omis.paroleboarditinerary.web.form.BoardMeetingSiteItemOperation;
import omis.paroleboarditinerary.web.form.BoardMeetingSiteLocation;
import omis.paroleboarditinerary.web.form.ParoleBoardItineraryForm;
import omis.paroleboarditinerary.web.form.ParoleBoardItineraryNoteItem;
import omis.paroleboarditinerary.web.form.ParoleBoardItineraryNoteItemOperation;
import omis.paroleboarditinerary.web.validator.ParoleBoardItineraryFormValidator;
import omis.paroleboardlocation.domain.ParoleBoardLocation;
import omis.paroleboardmember.domain.ParoleBoardMember;
import omis.probationterm.exception.ProbationTermExistsException;
import omis.web.controller.delegate.BusinessExceptionHandlerDelegate;

/**
 * Controller for managing parole board itineraries.
 *
 * @author Josh Divine
 * @author Annie Wahl 
 * @version 0.1.1 (Jan 23, 2018)
 * @since OMIS 3.0
 */
@Controller
@PreAuthorize("hasRole('USER')")
@RequestMapping("/paroleBoardItinerary")
public class ManageParoleBoardItineraryController {
	
	/* View names. */
	
	private static final String VIEW_NAME = "paroleBoardItinerary/edit";

	/* Table Rows. */
	
	private static final String BOARD_MEETING_SITE_TABLE_ROW_VIEW_NAME = 
			"paroleBoardItinerary/includes/boardMeetingSiteTableRow";
	
	private static final String BOARD_ITINERARY_NOTE_TABLE_ROW_VIEW_NAME =
			"paroleBoardItinerary/includes/boardItineraryNoteTableRow";
	
	private static final String BOARD_MEMBER_OPTIONS_VIEW_NAME = 
			"paroleBoardItinerary/includes/boardMemberOptions";
	
	/* Action menus. */

	private static final String ACTION_MENU_VIEW_NAME = 
			"paroleBoardItinerary/includes/paroleBoardItineraryActionMenu";
	
	private static final String BOARD_MEETING_SITES_ACTION_MENU_VIEW_NAME = 
			"paroleBoardItinerary/includes/boardMeetingSitesActionMenu";
	
	private static final String BOARD_ITINERARY_NOTES_ACTION_MENU_VIEW_NAME = 
			"paroleBoardItinerary/includes/boardItineraryNotesActionMenu";
	
	/* Redirects. */
	
	private static final String REDIRECT_URL = 
			"redirect:/paroleBoardItinerary/list.html";
	
	/* Model keys. */
	
	private static final String PAROLE_BOARD_ITINERARY_FORM_MODEL_KEY = 
			"paroleBoardItineraryForm";
	
	private static final String PAROLE_BOARD_ITINERARY_MODEL_KEY = 
			"paroleBoardItinerary";
	
	private static final String BOARD_MEETING_SITE_INDEX_MODEL_KEY = 
			"boardMeetingSiteIndex";
	
	private static final String BOARD_ITINERARY_NOTE_INDEX_MODEL_KEY = 
			"boardItineraryNoteIndex";
	
	private static final String ITEM_OPERATION_MODEL_KEY = "operation";
	
	private static final String BOARD_MEMBERS_MODEL_KEY = "boardMembers";

	private static final String BOARD_MEETING_SITES_MODEL_KEY = 
			"boardMeetingSites";

	private static final String PAROLE_BOARD_LOCATIONS_MODEL_KEY =
			"paroleBoardLocations";
	
	/* Message keys. */


	private static final String PAROLE_BOARD_ITINERARY_EXISTS_MESSAGE_KEY = 
			"paroleBoardItinerary.exists";
	
	/* Bundles */
	
	private static final String ERROR_BUNDLE_NAME = 
			"omis.paroleboarditinerary.msgs.form";
	
	/* Services. */
	
	@Autowired
	@Qualifier("paroleBoardItineraryService")
	private ParoleBoardItineraryService paroleBoardItineraryService;
	
	/* Property editor factories. */
	
	@Autowired
	@Qualifier("paroleBoardItineraryPropertyEditorFactory")
	private PropertyEditorFactory paroleBoardItineraryPropertyEditorFactory;
	
	@Autowired
	@Qualifier("datePropertyEditorFactory")
	private CustomDateEditorFactory customDateEditorFactory;
	
	@Autowired
	@Qualifier("paroleBoardMemberPropertyEditorFactory")
	private PropertyEditorFactory paroleBoardMemberPropertyEditorFactory;
	
	@Autowired
	@Qualifier("paroleBoardItineraryNotePropertyEditorFactory")
	private PropertyEditorFactory paroleBoardItineraryNotePropertyEditorFactory;

	@Autowired
	@Qualifier("boardMeetingSitePropertyEditorFactory")
	private PropertyEditorFactory boardMeetingSitePropertyEditorFactory;
	
	@Autowired
	@Qualifier("locationPropertyEditorFactory")
	private PropertyEditorFactory locationPropertyEditorFactory;
	
	@Autowired
	@Qualifier("paroleBoardLocationPropertyEditorFactory")
	private PropertyEditorFactory paroleBoardLocationPropertyEditorFactory;
	
	/* Validators. */

	@Autowired
	@Qualifier("paroleBoardItineraryFormValidator")
	private ParoleBoardItineraryFormValidator paroleBoardItineraryFormValidator;
	
	/* Helpers. */

	@Autowired
	@Qualifier("businessExceptionHandlerDelegate")
	private BusinessExceptionHandlerDelegate businessExceptionHandlerDelegate;

	/* Constructors. */
	
	/** Instantiates controller for parole board itineraries. */
	public ManageParoleBoardItineraryController() {
		// Default instantiation
	}
	
	/* URL invokable methods. */
	
	/**
	 * Shows screen to create a parole board itinerary.
	 * 
	 * @return screen to create parole board itinerary
	 */
	@PreAuthorize("hasRole('PAROLE_BOARD_ITINERARY_CREATE') "
			+ "or hasRole('ADMIN')")
	@RequestMapping(value = "/create.html", method = RequestMethod.GET)
	public ModelAndView create() {
		ParoleBoardItineraryForm paroleBoardItineraryForm = 
				new ParoleBoardItineraryForm();
		paroleBoardItineraryForm.setStartDate(new Date());
		ModelAndView mav = this.prepareMav(paroleBoardItineraryForm);
		return mav;
	}
	
	/**
	 * Shows screen to edit a parole board itinerary.
	 * 
	 * @param paroleBoardItinerary parole board itinerary
	 * @return screen to edit a parole board itinerary
	 */
	@PreAuthorize("hasRole('PAROLE_BOARD_ITINERARY_VIEW') or hasRole('ADMIN')")
	@RequestMapping(value = "/edit.html", method = RequestMethod.GET)
	public ModelAndView edit(
			@RequestParam(value = "paroleBoardItinerary", required = true)
				final ParoleBoardItinerary paroleBoardItinerary) {
		ParoleBoardItineraryForm paroleBoardItineraryForm = 
				new ParoleBoardItineraryForm();
		paroleBoardItineraryForm.setStartDate(DateRange.getStartDate(
				paroleBoardItinerary.getDateRange()));
		paroleBoardItineraryForm.setEndDate(DateRange.getEndDate(
				paroleBoardItinerary.getDateRange()));
		paroleBoardItineraryForm.setParoleBoardLocation(
				paroleBoardItinerary.getParoleBoardLocation());
		List<BoardAttendee> attendees = this.paroleBoardItineraryService
				.findBoardAttendeesByBoardItinerary(paroleBoardItinerary);
		for (BoardAttendee attendee : attendees) {
			if (attendee.getNumber().equals(1L)) {
				paroleBoardItineraryForm.setBoardMember1(
						attendee.getBoardMember());
			}
			if (attendee.getNumber().equals(2L)) {
				paroleBoardItineraryForm.setBoardMember2(
						attendee.getBoardMember());
			}
			if (attendee.getNumber().equals(3L)) {
				paroleBoardItineraryForm.setBoardMember3(
						attendee.getBoardMember());
			}
		}
		paroleBoardItineraryForm.setBoardMemberAlternate(
				this.paroleBoardItineraryService
				.findBoardAlternateAttendeeByBoardItinerary(
						paroleBoardItinerary).getBoardMember());
		List<BoardMeetingSite> meetingSites = this.paroleBoardItineraryService
				.findBoardMeetingSitesByBoardItinerary(paroleBoardItinerary);
		List<BoardMeetingSiteItem> meetingSiteItems = 
				new ArrayList<BoardMeetingSiteItem>();
		for (BoardMeetingSite meetingSite : meetingSites) {
			BoardMeetingSiteItem meetingSiteItem = new BoardMeetingSiteItem();
			meetingSiteItem.setBoardMeetingSite(meetingSite);
			meetingSiteItem.setDate(meetingSite.getDate());
			meetingSiteItem.setLocation(meetingSite.getLocation());
			meetingSiteItem.setOrder(meetingSite.getOrder());
			meetingSiteItem.setOperation(BoardMeetingSiteItemOperation.EDIT);
			meetingSiteItems.add(meetingSiteItem);
		}
		paroleBoardItineraryForm.setBoardMeetingSiteItems(meetingSiteItems);
		List<ParoleBoardItineraryNote> itineraryNotes = this
				.paroleBoardItineraryService.findItineraryNotesByBoardItinerary(
						paroleBoardItinerary);
		List<ParoleBoardItineraryNoteItem> itineraryNoteItems = 
				new ArrayList<ParoleBoardItineraryNoteItem>();
		for (ParoleBoardItineraryNote itineraryNote : itineraryNotes) {
			ParoleBoardItineraryNoteItem itineraryNoteItem = 
					new ParoleBoardItineraryNoteItem();
			itineraryNoteItem.setParoleBoardItineraryNote(itineraryNote);
			itineraryNoteItem.setDate(itineraryNote.getDate());
			itineraryNoteItem.setValue(itineraryNote.getDescription());
			itineraryNoteItem.setOperation(
					ParoleBoardItineraryNoteItemOperation.EDIT);
			itineraryNoteItems.add(itineraryNoteItem);
		}
		paroleBoardItineraryForm.setBoardItineraryNoteItems(itineraryNoteItems);
		ModelAndView mav = this.prepareMav(paroleBoardItineraryForm);
		mav.addObject(PAROLE_BOARD_ITINERARY_MODEL_KEY, paroleBoardItinerary);
		return mav;
	}
	
	/**
	 * Saves a parole board itinerary.
	 * 
	 * @param paroleBoardItineraryForm parole board itinerary form
	 * @param bindingResult binding result
	 * @return redirect to parole board itinerary listing screen
	 * @throws DuplicateEntityFoundException if duplicate entity exists 
	 */
	@PreAuthorize("hasRole('PAROLE_BOARD_ITINERARY_CREATE') "
			+ "or hasRole('ADMIN')")
	@RequestMapping(value = "/create.html", method = RequestMethod.POST)
	public ModelAndView save(
			final ParoleBoardItineraryForm paroleBoardItineraryForm,
			final BindingResult bindingResult) 
					throws DuplicateEntityFoundException {
		this.paroleBoardItineraryFormValidator.validate(
				paroleBoardItineraryForm, bindingResult);
		if (bindingResult.hasErrors()) {
			ModelAndView mav = this.prepareRedisplay(paroleBoardItineraryForm, 
					bindingResult);
			return mav;
		}
		ParoleBoardItinerary boardItinerary = this.paroleBoardItineraryService
				.create(paroleBoardItineraryForm.getParoleBoardLocation(), 
				paroleBoardItineraryForm.getStartDate(),
				paroleBoardItineraryForm.getEndDate());

		this.paroleBoardItineraryService.createAttendee(boardItinerary, 
				paroleBoardItineraryForm.getBoardMember1(), 1L, 
				AttendeeRoleCategory.PRIMARY);
		this.paroleBoardItineraryService.createAttendee(boardItinerary, 
				paroleBoardItineraryForm.getBoardMember2(), 2L, 
				AttendeeRoleCategory.PRIMARY);
		this.paroleBoardItineraryService.createAttendee(boardItinerary, 
				paroleBoardItineraryForm.getBoardMember3(), 3L, 
				AttendeeRoleCategory.PRIMARY);
		this.paroleBoardItineraryService.createAttendee(boardItinerary, 
				paroleBoardItineraryForm.getBoardMemberAlternate(), 1L, 
				AttendeeRoleCategory.ALTERNATE);
		
		processBoardMeetingSites(boardItinerary, 
				paroleBoardItineraryForm.getBoardMeetingSiteItems());
		
		processParoleBoardItineraryNoteItems(boardItinerary, 
				paroleBoardItineraryForm.getBoardItineraryNoteItems());
		
		return new ModelAndView(REDIRECT_URL);
	}
	
	/**
	 * Updates a parole board itinerary.
	 * 
	 * @param paroleBoardItinerary parole board itinerary
	 * @param paroleBoardItineraryForm parole board itinerary form
	 * @param bindingResult binding result
	 * @return redirect to parole board itinerary listing screen
	 * @throws DuplicateEntityFoundException if duplicate entity exists 
	 */
	@PreAuthorize("hasRole('PAROLE_BOARD_ITINERARY_EDIT') or hasRole('ADMIN')")
	@RequestMapping(value = "/edit.html", method = RequestMethod.POST)
	public ModelAndView update(
			@RequestParam(value = "paroleBoardItinerary", required = true)
				final ParoleBoardItinerary paroleBoardItinerary,
			final ParoleBoardItineraryForm paroleBoardItineraryForm,
			final BindingResult bindingResult) 
					throws DuplicateEntityFoundException {
		this.paroleBoardItineraryFormValidator.validate(
				paroleBoardItineraryForm, bindingResult);
		if (bindingResult.hasErrors()) {
			ModelAndView mav = this.prepareRedisplay(paroleBoardItineraryForm, 
					bindingResult);
			return mav;
		}
		
		ParoleBoardItinerary boardItinerary = this.paroleBoardItineraryService
				.update(paroleBoardItinerary,
				paroleBoardItineraryForm.getParoleBoardLocation(),
				paroleBoardItineraryForm.getStartDate(), 
				paroleBoardItineraryForm.getEndDate());
		
		List<BoardAttendee> attendees = this.paroleBoardItineraryService
				.findBoardAttendeesByBoardItinerary(boardItinerary);
		BoardAttendee attendee1 = null, attendee2 = null, attendee3 = null;
		for (BoardAttendee attendee : attendees) {
			if (attendee.getNumber().equals(1L)) {
				attendee1 = attendee;
			}
			if (attendee.getNumber().equals(2L)) {
				attendee2 = attendee;
			}
			if (attendee.getNumber().equals(3L)) {
				attendee3 = attendee;
			}
		}
		BoardAttendee alternateAttendee = this.paroleBoardItineraryService
				.findBoardAlternateAttendeeByBoardItinerary(boardItinerary);
		this.paroleBoardItineraryService.updateAttendee(attendee1, 
				paroleBoardItineraryForm.getBoardMember1(), 
				AttendeeRoleCategory.PRIMARY);
		this.paroleBoardItineraryService.updateAttendee(attendee2, 
				paroleBoardItineraryForm.getBoardMember2(), 
				AttendeeRoleCategory.PRIMARY);
		this.paroleBoardItineraryService.updateAttendee(attendee3, 
				paroleBoardItineraryForm.getBoardMember3(), 
				AttendeeRoleCategory.PRIMARY);
		this.paroleBoardItineraryService.updateAttendee(alternateAttendee, 
				paroleBoardItineraryForm.getBoardMemberAlternate(), 
				AttendeeRoleCategory.ALTERNATE);
		
		processBoardMeetingSites(boardItinerary, 
				paroleBoardItineraryForm.getBoardMeetingSiteItems());
		
		processParoleBoardItineraryNoteItems(boardItinerary,
				paroleBoardItineraryForm.getBoardItineraryNoteItems());
		
		return new ModelAndView(REDIRECT_URL);
	}
	
	/**
	 * Removes a parole board itinerary.
	 * 
	 * @param paroleBoardItinerary parole board itinerary
	 * @return redirect to parole board itinerary listing screen
	 */
	@PreAuthorize("hasRole('PAROLE_BOARD_ITINERARY_REMOVE') "
			+ "or hasRole('ADMIN')")
	@RequestMapping(value = "/remove.html", method = RequestMethod.GET)
	public ModelAndView remove(
			@RequestParam(value = "paroleBoardItinerary", required = true)
				final ParoleBoardItinerary paroleBoardItinerary) {
		this.paroleBoardItineraryService.remove(paroleBoardItinerary);
		return new ModelAndView(REDIRECT_URL); 
	}
	
	/* Invokable AJAX calls. */
	
	/**
	 * Displays table row of board meeting site inputs.
	 * 
	 * @param boardMeetingSiteIndex index of board meeting site for which to 
	 * display row of inputs
	 * @param boardMeetingSiteLocation location type that will populate the 
	 * locations drop down
	 * @return table row of board meeting site inputs
	 */
	@RequestMapping(value = "/addBoardMeetingSite.html", 
			method = RequestMethod.GET)
	public ModelAndView addBoardMeetingSite(
			@RequestParam(value = "boardMeetingSiteIndex", required = true)
				final int boardMeetingSiteIndex,
			@RequestParam(value = "boardMeetingSiteLocation", required = true)
				final BoardMeetingSiteLocation boardMeetingSiteLocation) {
		ModelAndView mav = new ModelAndView(
				BOARD_MEETING_SITE_TABLE_ROW_VIEW_NAME);
		mav.addObject(BOARD_MEETING_SITE_INDEX_MODEL_KEY, 
				boardMeetingSiteIndex);
		mav.addObject(ITEM_OPERATION_MODEL_KEY, 
				BoardMeetingSiteItemOperation.CREATE);
		if (BoardMeetingSiteLocation.SECURE_FACILITY.equals(
				boardMeetingSiteLocation)) {
			mav.addObject(BOARD_MEETING_SITES_MODEL_KEY, 
					this.paroleBoardItineraryService.findFacilityLocations());
		} else if (BoardMeetingSiteLocation.JAIL.equals(
				boardMeetingSiteLocation)) {
			mav.addObject(BOARD_MEETING_SITES_MODEL_KEY, 
					this.paroleBoardItineraryService.findJailLocations());
		} else if (BoardMeetingSiteLocation.PRERELEASE.equals(
				boardMeetingSiteLocation)) {
			mav.addObject(BOARD_MEETING_SITES_MODEL_KEY, 
					this.paroleBoardItineraryService.findPrereleaseLocations());
		} else if (BoardMeetingSiteLocation.COMMUNITY_SUPERVISION_OFFICE.equals(
				boardMeetingSiteLocation)) {
			mav.addObject(BOARD_MEETING_SITES_MODEL_KEY, 
					this.paroleBoardItineraryService
					.findCommunitySupervisionOfficeLocations());
		} else if (BoardMeetingSiteLocation.TREATMENT_AND_SANCTION_CENTER
				.equals(boardMeetingSiteLocation)) {
			mav.addObject(BOARD_MEETING_SITES_MODEL_KEY, 
					this.paroleBoardItineraryService
					.findTreatmentAndSactionCenterLocations());
		}
		return mav;
	}
	
	/**
	 * Displays table row of parole board itinerary note inputs.
	 * 
	 * @param boardItineraryNoteIndex index of parole board itinerary note for 
	 * which to display row of inputs
	 * @return table row of parole board itinerary note inputs
	 */
	@RequestMapping(value = "/addBoardItineraryNote.html", 
			method = RequestMethod.GET)
	public ModelAndView addBoardItineraryNote(
			@RequestParam(value = "boardItineraryNoteIndex", required = true)
				final int boardItineraryNoteIndex) {
		ModelAndView mav = new ModelAndView(
				BOARD_ITINERARY_NOTE_TABLE_ROW_VIEW_NAME);
		mav.addObject(BOARD_ITINERARY_NOTE_INDEX_MODEL_KEY, 
				boardItineraryNoteIndex);
		mav.addObject(ITEM_OPERATION_MODEL_KEY, 
				ParoleBoardItineraryNoteItemOperation.CREATE);
		return mav;
	}
	
	/**
	 * Displays board member options for the specified date.
	 * 
	 * @param date date
	 * @return board member options
	 */
	@RequestMapping(value = "/findBoardMembersOnDate.html", 
			method = RequestMethod.GET)
	public ModelAndView findBoardMembersOnDate(
			@RequestParam(value = "date", required = true) final Date date) {
		ModelAndView mav = new ModelAndView(
				BOARD_MEMBER_OPTIONS_VIEW_NAME);
		mav.addObject(BOARD_MEMBERS_MODEL_KEY, 
				this.paroleBoardItineraryService.findBoardMembersByDate(date));
		return mav;
	}
	
	/* Action menus. */
	
	/**
	 * Displays action menu for screen to create/edit a parole board itinerary.
	 * 
	 * @return action menu for screen to create/edit a parole board itinerary
	 */
	@RequestMapping(value = "/paroleBoardItineraryActionMenu.html", 
			method = RequestMethod.GET)
	public ModelAndView showActionMenu() {
		ModelAndView mav = new ModelAndView(ACTION_MENU_VIEW_NAME);
		return mav;
	}
	
	/** 
	 * Board meeting sites action menu.
	 * 
	 * @param boardMeetingSiteIndex board meeting site index.
	 * @return action menu. 
	 */
	@RequestMapping(value = "/boardMeetingSitesActionMenu.html", 
		method = RequestMethod.GET)
	public ModelAndView showBoardMeetingSitesActionMenu(
			@RequestParam("boardMeetingSiteIndex") 
				final Long boardMeetingSiteIndex) {
		final ModelMap modelMap = new ModelMap();
		modelMap.put(BOARD_MEETING_SITE_INDEX_MODEL_KEY, boardMeetingSiteIndex);
		return new ModelAndView(BOARD_MEETING_SITES_ACTION_MENU_VIEW_NAME, 
				modelMap);
	}
	
	/** 
	 * Parole board itinerary note action menu.
	 * 
	 * @param boardItineraryNoteIndex parole board itinerary note index.
	 * @return action menu. 
	 */
	@RequestMapping(value = "/boardItineraryNotesActionMenu.html", 
		method = RequestMethod.GET)
	public ModelAndView showBoardItineraryNotesActionMenu(
			@RequestParam("boardItineraryNoteIndex") 
				final Long boardItineraryNoteIndex) {
		final ModelMap modelMap = new ModelMap();
		modelMap.put(BOARD_ITINERARY_NOTE_INDEX_MODEL_KEY, 
				boardItineraryNoteIndex);
		return new ModelAndView(BOARD_ITINERARY_NOTES_ACTION_MENU_VIEW_NAME, 
				modelMap);
	}
	
	/* Exception handler methods. */
	
	/**
	 * Handles duplicate entity exists exceptions.
	 * 
	 * @param exception duplicate entity exists exception
	 * @return model and view for displaying exception explanation
	 */
	@ExceptionHandler(DuplicateEntityFoundException.class)
	public ModelAndView handleParoleBoardItineraryExistsException(
			final ProbationTermExistsException exception) {
		return this.businessExceptionHandlerDelegate.prepareModelAndView(
				PAROLE_BOARD_ITINERARY_EXISTS_MESSAGE_KEY, ERROR_BUNDLE_NAME, 
				exception);
	}
	
	/* Helper methods. */
	
	// Prepares model and view
	private ModelAndView prepareMav(
			final ParoleBoardItineraryForm paroleBoardItineraryForm) {
		ModelAndView mav = new ModelAndView(VIEW_NAME);
		mav.addObject(PAROLE_BOARD_ITINERARY_FORM_MODEL_KEY, 
				paroleBoardItineraryForm);
		List<Location> locations = this.paroleBoardItineraryService
				.findAllBoardItineraryLocations();
		List<ParoleBoardLocation> paroleBoardLocations =
				this.paroleBoardItineraryService.findParoleBoardLocations();
		mav.addObject(PAROLE_BOARD_LOCATIONS_MODEL_KEY, paroleBoardLocations);
		mav.addObject(BOARD_MEETING_SITES_MODEL_KEY, locations);
		mav.addObject(BOARD_MEMBERS_MODEL_KEY, this.paroleBoardItineraryService
				.findBoardMembersByDate(
						paroleBoardItineraryForm.getStartDate()));
		int boardMeetingSiteIndex = 0;
		if (paroleBoardItineraryForm.getBoardMeetingSiteItems() != null) {
			boardMeetingSiteIndex = paroleBoardItineraryForm
					.getBoardMeetingSiteItems().size();
		}
		mav.addObject(BOARD_MEETING_SITE_INDEX_MODEL_KEY,
				boardMeetingSiteIndex);
		int boardItineraryNoteIndex = 0;
		if (paroleBoardItineraryForm.getBoardItineraryNoteItems() != null) {
			boardItineraryNoteIndex = paroleBoardItineraryForm
					.getBoardItineraryNoteItems().size();
		}
		mav.addObject(BOARD_ITINERARY_NOTE_INDEX_MODEL_KEY, 
				boardItineraryNoteIndex);
		return mav;
	}
	
	// Prepares redisplay model and view
	private ModelAndView prepareRedisplay(
			final ParoleBoardItineraryForm paroleBoardItineraryForm,
			final BindingResult bindingResult) {
		ModelAndView mav = this.prepareMav(paroleBoardItineraryForm);
		mav.addObject(BindingResult.MODEL_KEY_PREFIX
					+ PAROLE_BOARD_ITINERARY_FORM_MODEL_KEY, bindingResult);
		return mav;
	}
	
	// Process board meeting sites
	private void processBoardMeetingSites(
			final ParoleBoardItinerary boardItinerary,
			final List<BoardMeetingSiteItem> boardMeetingSiteItems) 
					throws DuplicateEntityFoundException {
		if (boardMeetingSiteItems != null) {
			for (BoardMeetingSiteItem siteItem : boardMeetingSiteItems) {
				if (BoardMeetingSiteItemOperation.CREATE.equals(
						siteItem.getOperation())) {
					this.paroleBoardItineraryService.createBoardMeetingSite(
							boardItinerary, siteItem.getLocation(), 
							siteItem.getDate(), siteItem.getOrder());
				} else if (BoardMeetingSiteItemOperation.EDIT.equals(
						siteItem.getOperation())) {
					this.paroleBoardItineraryService.updateBoardMeetingSite(
							siteItem.getBoardMeetingSite(), 
							siteItem.getLocation(), siteItem.getDate(), 
							siteItem.getOrder());
				} else if (BoardMeetingSiteItemOperation.REMOVE.equals(
						siteItem.getOperation())) {
					this.paroleBoardItineraryService.removeBoardMeetingSite(
							siteItem.getBoardMeetingSite());
				}
			}
		}
	}
	
	// Process parole board itinerary notes
	private void processParoleBoardItineraryNoteItems(
			final ParoleBoardItinerary boardItinerary,
			final List<ParoleBoardItineraryNoteItem> boardItineraryNoteItems) 
					throws DuplicateEntityFoundException {
		if (boardItineraryNoteItems != null) {
			for (ParoleBoardItineraryNoteItem noteItem
					: boardItineraryNoteItems) {
				if (ParoleBoardItineraryNoteItemOperation.CREATE.equals(
						noteItem.getOperation())) {
					this.paroleBoardItineraryService.createBoardIteneraryNote(
							boardItinerary, noteItem.getValue(), 
							noteItem.getDate());
				} else if (ParoleBoardItineraryNoteItemOperation.EDIT.equals(
						noteItem.getOperation())) {
					this.paroleBoardItineraryService.updateBoardItineraryNote(
							noteItem.getParoleBoardItineraryNote(), 
							noteItem.getValue(), noteItem.getDate());
				} else if (ParoleBoardItineraryNoteItemOperation.REMOVE.equals(
						noteItem.getOperation())) {
					this.paroleBoardItineraryService.removeBoardItineraryNote(
							noteItem.getParoleBoardItineraryNote());
				}
			}
		}
	}
	
	/* Init binders. */
	
	/**
	 * Registers custom editors.
	 * 
	 * @param binder web binder
	 */
	@InitBinder
	protected void registerCustomEditors(final WebDataBinder binder) {
		binder.registerCustomEditor(ParoleBoardItinerary.class,
				this.paroleBoardItineraryPropertyEditorFactory
				.createPropertyEditor());
		binder.registerCustomEditor(ParoleBoardLocation.class,
				this.paroleBoardLocationPropertyEditorFactory
				.createPropertyEditor());
		binder.registerCustomEditor(Date.class,
				this.customDateEditorFactory.createCustomDateOnlyEditor(true));
		binder.registerCustomEditor(ParoleBoardMember.class,
				this.paroleBoardMemberPropertyEditorFactory
				.createPropertyEditor());
		binder.registerCustomEditor(ParoleBoardItineraryNote.class,
				this.paroleBoardItineraryNotePropertyEditorFactory
				.createPropertyEditor());
		binder.registerCustomEditor(BoardMeetingSite.class,
				this.boardMeetingSitePropertyEditorFactory
				.createPropertyEditor());
		binder.registerCustomEditor(Location.class,
				this.locationPropertyEditorFactory.createPropertyEditor());
	}
}
