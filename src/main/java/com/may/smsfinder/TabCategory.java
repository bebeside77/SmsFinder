package com.may.smsfinder;

public enum TabCategory {
	NELO(0, "NELO"),
	CI(1, "Hudson"),
	BDS(2, "BDS");

	private int tabOrder;
	private String bodyPattern;
	
	private TabCategory(int tabOrder, String bodyPattern) {
		this.tabOrder = tabOrder;
		this.bodyPattern = bodyPattern;
	}

	public int getTabOrder() {
		return tabOrder;
	}

	public String getBodyPattern() {
		return bodyPattern;
	}

	public static TabCategory findTabCategory(int tabOrder) {
		for (TabCategory each : values()) {
			if (each.getTabOrder() == tabOrder) {
				return each;
			}
		}
		return null;
	}
}
