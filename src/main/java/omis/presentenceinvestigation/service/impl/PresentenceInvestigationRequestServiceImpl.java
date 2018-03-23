package omis.presentenceinvestigation.service.impl;

import java.util.Date;
import java.util.List;

import omis.court.domain.Court;
import omis.court.service.delegate.CourtDelegate;
import omis.docket.domain.Docket;
import omis.docket.exception.DocketExistsException;
import omis.docket.service.delegate.DocketDelegate;
import omis.exception.DuplicateEntityFoundException;
import omis.offender.service.delegate.OffenderDelegate;
import omis.person.domain.Person;
import omis.person.domain.Suffix;
import omis.person.service.delegate.PersonDelegate;
import omis.person.service.delegate.SuffixDelegate;
import omis.presentenceinvestigation.domain.PresentenceInvestigationCategory;
import omis.presentenceinvestigation.domain.PresentenceInvestigationRequest;
import omis.presentenceinvestigation.domain.PresentenceInvestigationRequestNote;
import omis.presentenceinvestigation.service.PresentenceInvestigationRequestService;
import omis.presentenceinvestigation.service.delegate.PresentenceInvestigationCategoryDelegate;
import omis.presentenceinvestigation.service.delegate.PresentenceInvestigationRequestDelegate;
import omis.presentenceinvestigation.service.delegate.PresentenceInvestigationRequestNoteDelegate;
import omis.user.domain.UserAccount;
import omis.user.service.delegate.UserAccountDelegate;

/** Implementation of presentence investigation request service.
 * @author Ryan Johns
 * @author Annie Jacques
 * @version 0.1.1 (Jun 23, 2017)
 * @since OMIS 3.0 */
public class PresentenceInvestigationRequestServiceImpl 
	implements PresentenceInvestigationRequestService {
	
	private final PresentenceInvestigationRequestDelegate 
		presentenceInvestigationRequestDelegate;
	
	private final PresentenceInvestigationRequestNoteDelegate
		presentenceInvestigationRequestNoteDelegate;
	
	private final PresentenceInvestigationCategoryDelegate
		presentenceInvestigationCategoryDelegate;
	
	private final PersonDelegate personDelegate;
	
	private final DocketDelegate docketDelegate;
	
	private final SuffixDelegate suffixDelegate;
	
	private final CourtDelegate courtDelegate;
	
	private final OffenderDelegate offenderDelegate;
	
	private final UserAccountDelegate userAccountDelegate;
	
	/** Constructor.
	 * @param presentenceInvestigationRequestDelegate - presentence 
	 * investigation request delegate. */
	public PresentenceInvestigationRequestServiceImpl(
			final PresentenceInvestigationRequestDelegate 
				presentenceInvestigationRequestDelegate,
			final PresentenceInvestigationRequestNoteDelegate
				presentenceInvestigationRequestNoteDelegate,
			final PresentenceInvestigationCategoryDelegate
				presentenceInvestigationCategoryDelegate,
			final PersonDelegate personDelegate,
			final DocketDelegate docketDelegate,
			final SuffixDelegate suffixDelegate,
			final CourtDelegate courtDelegate,
			final OffenderDelegate offenderDelegate,
			final UserAccountDelegate userAccountDelegate) {
		this.presentenceInvestigationRequestDelegate 
			= presentenceInvestigationRequestDelegate;
		this.presentenceInvestigationRequestNoteDelegate =
				presentenceInvestigationRequestNoteDelegate;
		this.presentenceInvestigationCategoryDelegate =
				presentenceInvestigationCategoryDelegate;
		this.personDelegate = personDelegate;
		this.docketDelegate = docketDelegate;
		this.suffixDelegate = suffixDelegate;
		this.courtDelegate = courtDelegate;
		this.offenderDelegate = offenderDelegate;
		this.userAccountDelegate = userAccountDelegate;
	}
	
	
	/** {@inheritDoc} */
	@Override
	public PresentenceInvestigationRequest create(
			final UserAccount assignedUser, final Date requestDate, 
			final Date expectedCompletionDate, final Docket docket,
			final Date completionDate, final Date sentenceDate,
			final PresentenceInvestigationCategory category, final Date submissionDate)
					throws DuplicateEntityFoundException {
		return this.presentenceInvestigationRequestDelegate.create(
				assignedUser, requestDate, expectedCompletionDate, 
				docket, completionDate, sentenceDate, category, submissionDate);
	}

	/** {@inheritDoc} */
	@Override
	public PresentenceInvestigationRequest update(
			final PresentenceInvestigationRequest 
				presentenceInvestigationRequest,
			final UserAccount assignedUser, final Date requestDate, 
			final Date completionDate, final Date expectedCompletionDate,
			final Docket docket, final Date sentenceDate,
			final PresentenceInvestigationCategory category, final Date submissionDate) 
					throws DuplicateEntityFoundException {
		return this.presentenceInvestigationRequestDelegate.update(
				presentenceInvestigationRequest, assignedUser, requestDate,
				completionDate, expectedCompletionDate, docket, sentenceDate,
				category, submissionDate);
	}

	
	/** {@inheritDoc} */
	@Override
	public void remove(
			final PresentenceInvestigationRequest 
				presentenceInvestigationRequest) {
		this.presentenceInvestigationRequestDelegate.remove(
				presentenceInvestigationRequest);
	}
	
	/**{@inheritDoc} */
	@Override
	public Person createPerson(final String lastName, final String firstName,
			final String middleName, final String suffix)
					throws DuplicateEntityFoundException {
		return this.personDelegate.create(lastName, firstName, middleName, suffix);
	}


	/**{@inheritDoc} */
	@Override
	public Person updatePerson(final Person person, final String lastName,
			final String firstName, final String middleName, final String suffix)
					throws DuplicateEntityFoundException {
		return this.personDelegate.update(person, lastName, firstName,
				middleName, suffix);
	}


	/**{@inheritDoc} */
	@Override
	public void removePerson(final Person person) {
		this.personDelegate.remove(person);
	}


	/**{@inheritDoc} */
	@Override
	public Docket createDocket(final Person person, final Court court,
			final String value) throws DocketExistsException {
		return this.docketDelegate.create(person, court, value);
	}


	/**{@inheritDoc} */
	@Override
	public Docket updateDocket(final Docket docket, final Court court,
			final String value) throws DocketExistsException {
		return this.docketDelegate.update(docket, court, value);
	}


	/**{@inheritDoc} */
	@Override
	public void removeDocket(final Docket docket) {
		this.docketDelegate.remove(docket);
	}


	/**{@inheritDoc} */
	@Override
	public PresentenceInvestigationRequestNote
		createPresentenceInvestigationRequestNote(
			final PresentenceInvestigationRequest presentenceInvestigationRequest,
			final String description, final Date date)
					throws DuplicateEntityFoundException {
		return this.presentenceInvestigationRequestNoteDelegate.create(
				presentenceInvestigationRequest, description, date);
	}


	/**{@inheritDoc} */
	@Override
	public PresentenceInvestigationRequestNote
		updatePresentenceInvestigationRequestNote(
			final PresentenceInvestigationRequestNote
				presentenceInvestigationRequestNote, final String description,
			final Date date)
					throws DuplicateEntityFoundException {
		return this.presentenceInvestigationRequestNoteDelegate.update(
				presentenceInvestigationRequestNote, description, date);
	}


	/**{@inheritDoc} */
	@Override
	public void removePresentenceInvestigationRequestNote(
			final PresentenceInvestigationRequestNote
				presentenceInvestigationRequestNote) {
		this.presentenceInvestigationRequestNoteDelegate.remove(
				presentenceInvestigationRequestNote);
	}


	/**{@inheritDoc} */
	@Override
	public List<PresentenceInvestigationRequestNote>
		findPresentenceInvestigationRequestNotesByPresentenceInvestigationRequest(
			final PresentenceInvestigationRequest presentenceInvestigationRequest) {
		return this.presentenceInvestigationRequestNoteDelegate
				.findByPresentenceInvestigationRequest(
						presentenceInvestigationRequest);
	}


	/**{@inheritDoc} */
	@Override
	public List<Suffix> findSuffixes() {
		return this.suffixDelegate.findAll();
	}


	/**{@inheritDoc} */
	@Override
	public List<Court> findCourts() {
		return this.courtDelegate.findAllCourts();
	}


	/**{@inheritDoc} */
	@Override
	public Boolean isOffender(final Person person) {
		return this.offenderDelegate.isOffender(person);
	}
	
	/** {@inheritDoc} */
	@Override
	public UserAccount findUserAccountByUsername(final String username) {
		return this.userAccountDelegate.findByUsername(username);
	}


	/**{@inheritDoc} */
	@Override
	public List<PresentenceInvestigationCategory>
			findAllPresentenceInvestigationCategories() {
		return this.presentenceInvestigationCategoryDelegate.findAll();
	}

}
