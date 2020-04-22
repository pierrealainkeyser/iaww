package fr.keyser.wonderfull.security;

public class ProviderPrincipal extends UserPrincipal {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8627899274087690536L;
	private final String provider;

	public ProviderPrincipal(String name, String uid, String provider) {
		super(name, uid);
		this.provider = provider;
	}

	public String getProvider() {
		return provider;
	}
}
