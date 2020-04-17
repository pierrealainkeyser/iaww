package fr.keyser.wonderfull.security;

public class ProviderPrincipal extends UserPrincipal {

	private final String provider;

	public ProviderPrincipal(String name, String uid, String provider) {
		super(name, uid);
		this.provider = provider;
	}

	public String getProvider() {
		return provider;
	}
}
