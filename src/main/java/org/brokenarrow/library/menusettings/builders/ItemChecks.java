package org.brokenarrow.library.menusettings.builders;

public final class ItemChecks {
	private final boolean strict;
	private final boolean checkArmorSlots;
	private final boolean checkOffHand;
	private final boolean checkNameContains;
	private final boolean checkNameEquals;
	private final boolean checkNameIgnorecase;
	private final boolean checkLoreContains;
	private final boolean checkLoreEquals;
	private final boolean checkLoreIgnorecase;

	private ItemChecks(Builder builder) {
		this.strict = builder.strict;
		this.checkArmorSlots = builder.checkArmorSlots;
		this.checkOffHand = builder.checkOffHand;
		this.checkNameContains = builder.checkNameContains;
		this.checkNameEquals = builder.checkNameEquals;
		this.checkNameIgnorecase = builder.checkNameIgnorecase;
		this.checkLoreContains = builder.checkLoreContains;
		this.checkLoreEquals = builder.checkLoreEquals;
		this.checkLoreIgnorecase = builder.checkLoreIgnorecase;
	}

	public boolean isStrict() {
		return strict;
	}

	public boolean isCheckArmorSlots() {
		return checkArmorSlots;
	}

	public boolean isCheckOffHand() {
		return checkOffHand;
	}

	public boolean isCheckNameContains() {
		return checkNameContains;
	}

	public boolean isCheckNameEquals() {
		return checkNameEquals;
	}

	public boolean isCheckNameIgnorecase() {
		return checkNameIgnorecase;
	}

	public boolean isCheckLoreContains() {
		return checkLoreContains;
	}

	public boolean isCheckLoreEquals() {
		return checkLoreEquals;
	}

	public boolean isCheckLoreIgnorecase() {
		return checkLoreIgnorecase;
	}

	public static class Builder {
		private boolean strict;
		private boolean checkArmorSlots;
		private boolean checkOffHand;
		private boolean checkNameContains;
		private boolean checkNameIgnorecase;
		private boolean checkNameEquals;
		private boolean checkLoreContains;
		private boolean checkLoreEquals;
		private boolean checkLoreIgnorecase;

		public Builder setStrict(boolean strict) {
			this.strict = strict;
			return this;
		}

		public Builder setCheckArmorSlots(boolean checkArmorSlots) {
			this.checkArmorSlots = checkArmorSlots;
			return this;
		}

		public Builder setCheckOffHand(boolean checkOffHand) {
			this.checkOffHand = checkOffHand;
			return this;
		}

		public Builder setCheckNameContains(boolean checkNameContains) {
			this.checkNameContains = checkNameContains;
			return this;
		}

		public Builder setCheckNameEquals(boolean checkNameEquals) {
			this.checkNameEquals = checkNameEquals;
			return this;
		}

		public Builder setCheckNameIgnorecase(boolean checkNameIgnorecase) {
			this.checkNameIgnorecase = checkNameIgnorecase;
			return this;
		}

		public Builder setCheckLoreContains(boolean checkLoreContains) {
			this.checkLoreContains = checkLoreContains;
			return this;
		}

		public Builder setCheckLoreEquals(boolean checkLoreEquals) {
			this.checkLoreEquals = checkLoreEquals;
			return this;
		}

		public Builder setCheckLoreIgnorecase(boolean checkLoreIgnorecase) {
			this.checkLoreIgnorecase = checkLoreIgnorecase;
			return this;
		}

		public ItemChecks build() {
			return new ItemChecks(this);
		}
	}
}
