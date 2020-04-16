package fr.keyser.fsm;

import java.util.Optional;
import java.util.function.Function;

import org.springframework.expression.Expression;
import org.springframework.expression.spel.support.StandardEvaluationContext;

public class SpringELChoice<T> implements ChoicePredicate<T> {

	private final Expression expression;

	public SpringELChoice(Expression expression) {
		this.expression = expression;
	}

	@Override
	public boolean test(Instance<T> instance, Transition t) {
		Optional<T> value = instance.opt(Function.identity());

		Optional<T> parentValue = Optional.ofNullable(instance.getParent()).flatMap(i -> i.opt(Function.identity()));

		return value.map(v -> testValue(v, parentValue, t)).orElse(false);

	}

	private boolean testValue(T value, Optional<T> parentValue, Transition t) {
		StandardEvaluationContext ctx = new StandardEvaluationContext(value);
		ctx.setVariable("event", t.getEvent());
		parentValue.ifPresent(v -> ctx.setVariable("parent", v));

		return expression.getValue(ctx, Boolean.class);
	}

	@Override
	public String toString() {
		return expression.getExpressionString();
	}

}
