package com.funo.park.client.po;

import java.util.ArrayList;
import java.util.List;

public class ParkDetailInfo extends BaseReqInfo {

	public List<ParkDetailData> parkDetailData = new ArrayList<ParkDetailData>();

	public static class ParkDetailData {

		protected String method;

		protected String parkId;

		public String getParkId() {
			return parkId;
		}

		public void setParkId(String parkId) {
			this.parkId = parkId;
		}

		public String getMethod() {
			return method;
		}

		public void setMethod(String method) {
			this.method = method;
		}

	}

	public List<ParkDetailData> getParkDetailData() {
		return parkDetailData;
	}

	public void setParkDetailData(List<ParkDetailData> ParkDetailData) {
		this.parkDetailData = ParkDetailData;
	}

}