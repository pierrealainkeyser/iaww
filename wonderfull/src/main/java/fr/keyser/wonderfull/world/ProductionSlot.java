package fr.keyser.wonderfull.world;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ProductionSlot {

	private final Token type;

	private final ProductionStatus status;

	public ProductionSlot(Token type) {
		this(type, ProductionStatus.EMPTY);
	}

	@JsonCreator
	public ProductionSlot(@JsonProperty("type") Token type, @JsonProperty("status") ProductionStatus status) {
		this.type = type;
		this.status = status;
	}

	public ProductionSlot updateStatus(ProductionStatus status) {
		return new ProductionSlot(type, status);
	}

	public boolean empty() {
		return !ok();
	}

	public boolean ok() {
		return status.ok();
	}

	public Token getType() {
		return type;
	}

	public ProductionStatus getStatus() {
		return status;
	}

	@Override
	public int hashCode() {
		return Objects.hash(status, type);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ProductionSlot other = (ProductionSlot) obj;
		return status == other.status && type == other.type;
	}

}
