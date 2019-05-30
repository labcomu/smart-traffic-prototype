package com.smarttrafficprototype.trafficmanager;

public enum Classification {

	COMPLETE {
		@Override
		public String toString() {
			return "Complete";
		}
	},
	INCOMPLETE {
		@Override
		public String toString() {
			return "Incomplete";
		}
	},
	FAILED {
		@Override
		public String toString() {
			return "Failed";
		}
	};
	
}
