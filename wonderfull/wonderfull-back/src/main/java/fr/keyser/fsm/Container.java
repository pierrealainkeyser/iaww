package fr.keyser.fsm;

public interface Container<T> extends NodeContainer<T> {

	default Choice<T> choice(String choice) {
		return choice(choice, 0);
	}

	Choice<T> choice(String choice, int priority);

	default Region<T> region(String region) {
		return region(region, 1);
	}

	Region<T> region(String region, int times);

	Terminal<T> terminal(String terminal);

}
