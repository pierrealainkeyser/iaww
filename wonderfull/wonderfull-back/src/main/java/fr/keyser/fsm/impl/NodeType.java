package fr.keyser.fsm.impl;

public class NodeType {

	public static final NodeType LEAF = new NodeType("LEAF");

	public static final NodeType CHOICE = new NodeType("CHOICE");

	public static final NodeType JOINPOINT = new NodeType("JOINPOINT");

	public static final NodeType TERMINAL = new NodeType("TERMINAL");

	public static final NodeType CONTAINER = new ContainerType("CONTAINER");

	public static final NodeType AUTO = new NodeType("AUTO");

	public static RegionType region(boolean parralel, int times) {
		return new RegionType(parralel ? "PARRALEL" : "REGION", times);
	}

	public static class ContainerType extends NodeType {

		private ContainerType(String name) {
			super(name);
		}

		@Override
		public boolean allowTransitions() {
			return true;
		}
	}

	public static class RegionType extends NodeType {

		private final int times;

		private RegionType(String name, int times) {
			super(name);
			this.times = times;
		}

		public int getTimes() {
			return times;
		}

		@Override
		public String toString() {
			if (times > 1)
				return super.toString() + " *" + times;
			return super.toString();
		}
	}

	private final String name;

	private NodeType(String name) {
		this.name = name;
	}

	public boolean allowTransitions() {
		return false;
	}

	@Override
	public String toString() {
		return getName();
	}

	public String getName() {
		return name;
	}

}
