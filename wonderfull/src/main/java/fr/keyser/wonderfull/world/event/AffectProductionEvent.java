package fr.keyser.wonderfull.world.event;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

import fr.keyser.wonderfull.world.BuildedCard;
import fr.keyser.wonderfull.world.DraftedCard;
import fr.keyser.wonderfull.world.InProductionCard;
import fr.keyser.wonderfull.world.Token;

@JsonTypeName(AffectProductionEvent.TYPE)
public class AffectProductionEvent implements EmpireEvent {

	public static final String TYPE = "affect";

	private final int index;

	private final InProductionCard inProduction;

	private final BuildedCard builded;

	private final Map<Integer, Token> affected;

	private final DraftedCard recycled;

	@JsonCreator
	public AffectProductionEvent(@JsonProperty("index") int index,
			@JsonProperty("inProduction") InProductionCard inProduction, @JsonProperty("builded") BuildedCard builded,
			@JsonProperty("affected") Map<Integer, Token> affected, @JsonProperty("recycled") DraftedCard recycled) {
		this.index = index;
		this.inProduction = inProduction;
		this.builded = builded;
		this.affected = affected;
		this.recycled = recycled;
	}

	public int getIndex() {
		return index;
	}

	public InProductionCard getInProduction() {
		return inProduction;
	}

	public BuildedCard getBuilded() {
		return builded;
	}

	public Map<Integer, Token> getAffected() {
		return affected;
	}

	public DraftedCard getRecycled() {
		return recycled;
	}

}
