package fr.keyser.fsm;

import java.util.function.BiConsumer;

public interface ActionNode<T> {

	default ActionNode<T> callbackEntry(BiConsumer<Instance<T>, EventMsg> callback) {
		return entry((i, event) -> {
			callback.accept(i, event);
			return i;
		});
	}

	default ActionNode<T> updateEntry(PayloadActionFunction<T> entry) {
		return entry(entry.asAction());
	}

	ActionNode<T> entry(ActionFunction<T> entry);

	default ActionNode<T> callbackExit(BiConsumer<Instance<T>, EventMsg> callback) {
		return exit((i, event) -> {
			callback.accept(i, event);
			return i;
		});
	}

	default ActionNode<T> updateExit(PayloadActionFunction<T> exit) {
		return exit(exit.asAction());
	}

	ActionNode<T> exit(ActionFunction<T> exit);

}
