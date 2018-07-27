package omis.userpreference.service.delegate;

import omis.instance.factory.InstanceFactory;
import omis.media.domain.Photo;
import omis.user.domain.UserAccount;
import omis.userpreference.dao.UserPreferenceDao;
import omis.userpreference.domain.ColorValue;
import omis.userpreference.domain.DisplayTheme;
import omis.userpreference.domain.UserPreference;

/**
 * User preference delegate.
 * 
 * @author Joel Norris
 * @version 0.1.1 (July, 16 2018)
 * @since OMIS 3.0
 */
public class UserPreferenceDelegate {

	/* Data access objects. */
	
	private UserPreferenceDao userPreferenceDao;
	
	/* Instance factories. */
	
	private InstanceFactory<UserPreference> userPreferenceInstanceFactory;
	
	/* Constructors. */
	
	/**
	 * Instantiates a user preference delegate with the specified data
	 * access object and instance factory.
	 * 
	 * @param userPreferenceDao user preference data access object
	 * @param userPreferenceInstanceFactory user preference instance factory
	 */
	public UserPreferenceDelegate(final UserPreferenceDao userPreferenceDao,
		final InstanceFactory<UserPreference> userPreferenceInstanceFactory) {
		this.userPreferenceDao = userPreferenceDao;
		this.userPreferenceInstanceFactory = userPreferenceInstanceFactory;
	}
	
	/* Method implementations. */
	
	/**
	 * Creates a new user preference for the specified user account.
	 * 
	 * @param foregroundColorValue foreground color value
	 * @param backgroundColorValue background color value
	 * @param accentColorValue accent color value
	 * @param whiteBackground white background
	 * @param userAccount user account
	 * @return user preference
	 */
	@Deprecated
	public UserPreference create(final ColorValue foregroundColorValue,
			final ColorValue backgroundColorValue,
			final ColorValue accentColorValue,
			final Boolean whiteBackground, final UserAccount userAccount) {
		UserPreference userPreference
			= userPreferenceInstanceFactory.createInstance();
		this.populateUserPreference(userPreference, foregroundColorValue,
				backgroundColorValue, accentColorValue, whiteBackground);
		userPreference.setUserAccount(userAccount);
		return this.userPreferenceDao.makePersistent(userPreference);
	}

	/**
	 * Creates a new user preference.
	 * 
	 * @param foregroundColorValue foreground color value
	 * @param backgroundColorValue background color value
	 * @param accentColorValue accent color value
	 * @param whiteBackground white background
	 * @param shadows shadows
	 * @param borderRadius border radius
	 * @param userAccount user account
	 * @return user preference
	 */
	public UserPreference create(final ColorValue foregroundColorValue,
			final ColorValue backgroundColorValue, final ColorValue accentColorValue,
			final Boolean whiteBackground, final Boolean shadows, final Short borderRadius,
			final DisplayTheme displayTheme, final Photo backgroundPhoto, 
			final UserAccount userAccount) {
		UserPreference userPreference = userPreferenceInstanceFactory.createInstance();
		userPreference.setUserAccount(userAccount);
		this.populateUserPreference(userPreference, foregroundColorValue, backgroundColorValue,
				accentColorValue, whiteBackground,
				borderRadius, shadows, displayTheme,
				backgroundPhoto);
		return this.userPreferenceDao.makePersistent(userPreference);
	}
	
	/**
	 * Updates the specified user preference.
	 * 
	 * @param foregroundColorValue foreground color value
	 * @param backgroundColorValue background color value
	 * @param accentColorValue accent color value
	 * @param whiteBackground white background
	 * @return updated user preference
	 */
	@Deprecated
	public UserPreference update(final UserPreference userPreference,
			final ColorValue foregroundColorValue,
			final ColorValue backgroundColorValue,
			final ColorValue accentColorValue,
			final Boolean whiteBackground) {
		this.populateUserPreference(userPreference, foregroundColorValue,
				backgroundColorValue, accentColorValue, whiteBackground);
		return this.userPreferenceDao.makePersistent(userPreference);
	}
	
	/**
	 * Updates the specified user preference.
	 * 
	 * @param userPreference user preference
	 * @param foregroundColorValue foreground color value
	 * @param backgroundColorValue backgounrd color value
	 * @param accentColorValue accent color value
	 * @param whiteBackground white background
	 * @param shadows shadows 
	 * @param borderRadius border radius
	 * @return updated user preference
	 */
	public UserPreference update(final UserPreference userPreference,
			final ColorValue foregroundColorValue,
			final ColorValue backgroundColorValue,
			final ColorValue accentColorValue,
			final Boolean whiteBackground, final Boolean shadows, final Short borderRadius,
			final DisplayTheme displayTheme, final Photo backgroundPhoto) {
		this.populateUserPreference(userPreference, foregroundColorValue,
				backgroundColorValue, accentColorValue,
				whiteBackground, borderRadius, shadows,
				displayTheme, backgroundPhoto);
		return this.userPreferenceDao.makePersistent(userPreference);
	}
	
	/**
	 * Returns the user preference for the specified user account.
	 * 
	 * @param userAccount user account
	 * @return user preference
	 */
	public UserPreference findByUserAccount(final UserAccount userAccount) {
		return this.userPreferenceDao.findByAccount(userAccount);
	}
	
	/**
	 * Removes the specified user preference.
	 * 
	 * @param userPreference user preference
	 */
	public void remove(final UserPreference userPreference) {
		this.userPreferenceDao.makeTransient(userPreference);
	}
	
	/* Helper methods. */
	
	/*
	 * Populates the specified user preference.
	 * 
	 * @param userPreference user preference
	 * @param foregroundColorValue foreground color value
	 * @param backgroundColorValue background color value
	 * @param accentColorValue accent color value
	 * @param whiteBackground white background
	 * @return populated user preference
	 */
	@Deprecated
	private UserPreference populateUserPreference(
			final UserPreference userPreference,
			final ColorValue foregroundColorValue,
			final ColorValue backgroundColorValue,
			final ColorValue accentColorValue, final Boolean whiteBackground) {
		userPreference.setForegroundColorValue(foregroundColorValue);
		userPreference.setBackgroundColorValue(backgroundColorValue);
		userPreference.setAccentColorValue(accentColorValue);
		userPreference.setWhiteBackground(whiteBackground);
		return userPreference;
	}
	
	/*
	 * Populates the specified user preference.
	 * 
	 * @param userPreference user preference
	 * @param foregroundColorValue foreground color value
	 * @param backgroundColorValue background color value
	 * @param accentColorValue accent color value
	 * @param whiteBackground white background
	 * @param borderRadius border radius
	 * @param shadows shadows
	 * @param displayTheme display theme
	 * @param backgroundPhoto background photo
	 * @return populated user preference
	 */
	private UserPreference populateUserPreference(
			final UserPreference userPreference,
			final ColorValue foregroundColorValue,
			final ColorValue backgroundColorValue,
			final ColorValue accentColorValue, final Boolean whiteBackground,
			final Short borderRadius, Boolean shadows,
			final DisplayTheme displayTheme, final Photo backgroundPhoto) {
		userPreference.setForegroundColorValue(foregroundColorValue);
		userPreference.setBackgroundColorValue(backgroundColorValue);
		userPreference.setAccentColorValue(accentColorValue);
		userPreference.setWhiteBackground(whiteBackground);
		userPreference.setBorderRadius(borderRadius);
		userPreference.setShadows(shadows);
		userPreference.setDisplayTheme(displayTheme);
		userPreference.setBackgroundPhoto(backgroundPhoto);
		return userPreference;
	}
}