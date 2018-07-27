package omis.userpreference.domain.impl;

import omis.media.domain.Photo;
import omis.user.domain.UserAccount;
import omis.userpreference.domain.ColorValue;
import omis.userpreference.domain.DisplayTheme;
import omis.userpreference.domain.UserPreference;

/**
 * User preference implementation.
 * 
 * @author Joel Norris
 * @version 0.1.0 (Nov 19, 2015)
 * @since OMIS 3.0
 */
public class UserPreferenceImpl implements UserPreference {

	private static final long serialVersionUID = 1L;

	private Long id;
	private ColorValue foregroundColorValue;
	private ColorValue backgroundColorValue;
	private ColorValue accentColorValue;
	private Boolean whiteBackground;
	private Boolean shadows;
	private Short borderRadius;
	private Photo backgroundPhoto;
	private DisplayTheme displayTheme;
	private UserAccount userAccount;
	
	/**
	 * Instantiates a default instance of user preference.
	 */
	public UserPreferenceImpl() {
		//Default constructor.
	}

	/** {@inheritDoc} */
	@Override
	public Long getId() {
		return id;
	}

	/** {@inheritDoc} */
	@Override
	public void setId(final Long id) {
		this.id = id;
	}

	/** {@inheritDoc} */
	@Override
	public ColorValue getForegroundColorValue() {
		return this.foregroundColorValue;
	}

	/** {@inheritDoc} */
	@Override
	public void setForegroundColorValue(final ColorValue foregroundColorValue) {
		this.foregroundColorValue = foregroundColorValue;
	}

	/** {@inheritDoc} */
	@Override
	public ColorValue getBackgroundColorValue() {
		return this.backgroundColorValue;
	}

	/** {@inheritDoc} */
	@Override
	public void setBackgroundColorValue(final ColorValue backgroundColorValue) {
		this.backgroundColorValue = backgroundColorValue;
	}

	/** {@inheritDoc} */
	@Override
	public ColorValue getAccentColorValue() {
		return accentColorValue;
	}

	/** {@inheritDoc} */
	@Override
	public void setAccentColorValue(final ColorValue accentColorValue) {
		this.accentColorValue = accentColorValue;
	}

	/** {@inheritDoc} */
	@Override
	public Boolean getWhiteBackground() {
		return this.whiteBackground;
	}

	/** {@inheritDoc} */
	@Override
	public void setWhiteBackground(final Boolean whiteBackground) {
		this.whiteBackground = whiteBackground;
	}

	/** {@inheritDoc} */
	@Override
	public UserAccount getUserAccount() {
		return this.userAccount;
	}

	/** {@inheritDoc} */
	@Override
	public void setUserAccount(final UserAccount userAccount) {
		this.userAccount = userAccount;
	}
	
	/** {@inheritDoc} */
	@Override
	public Boolean getShadows() {
		return this.shadows;
	}

	/** {@inheritDoc} */
	@Override
	public void setShadows(final Boolean shadows) {
		this.shadows = shadows;
	}

	/** {@inheritDoc} */
	@Override
	public Short getBorderRadius() {
		return this.borderRadius;
	}
	
	/** {@inheritDoc} */
	@Override
	public void setBorderRadius(final Short borderRadius) {
		this.borderRadius = borderRadius;
	}

	/** {@inheritDoc} */
	@Override
	public Photo getBackgroundPhoto() {
		return this.backgroundPhoto;
	}

	/** {@inheritDoc} */
	@Override
	public void setBackgroundPhoto(final Photo backgroundPhoto) {
		this.backgroundPhoto = backgroundPhoto;
	}

	/** {@inheritDoc} */
	@Override
	public DisplayTheme getDisplayTheme() {
		return this.displayTheme;
	}

	/** {@inheritDoc} */
	@Override
	public void setDisplayTheme(final DisplayTheme displayTheme) {
		this.displayTheme = displayTheme;
	}

	/** {@inheritDoc} */
	@Override
	public boolean equals(final Object o) {
		if (this == o) {
			return true;
		}	
		if (!(o instanceof UserPreference)) {
			return false;
		}
		
		UserPreference that = (UserPreference) o;
		
		if (this.getUserAccount() == null) {
			throw new IllegalStateException("UserAccount required.");
		}
		if (!this.getUserAccount().equals(that.getUserAccount())) {
			return false;
		}
		return true;
	}
	
	/** {@inheritDoc} */
	@Override
	public int hashCode() {
		if (this.getUserAccount() == null) {
			throw new IllegalStateException("UserAccount required.");
		}
		
		int hashCode = 14;
		
		hashCode = 29 * hashCode + this.getUserAccount().hashCode();
		
		return hashCode;
	}
}