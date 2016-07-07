package com.funo.park.client.po;

import java.util.ArrayList;
import java.util.List;

public class ParkDetailReturnInfo extends BaseRspInfo {

	public List<ParkDetailReturnData> ParkDetailReturnData = new ArrayList<ParkDetailReturnData>();

	public static class ParkDetailReturnData {

		protected String parkName;

		protected String parkId;

		protected String totalSeat;

		protected String freeSeat;

		protected String orderFreeSeat;

		protected String parkType;

		protected String chargeSet;

		protected String address;

		protected String notice;

		public String getParkName() {
			return parkName;
		}

		public void setParkName(String parkName) {
			this.parkName = parkName;
		}

		public String getParkId() {
			return parkId;
		}

		public void setParkId(String parkId) {
			this.parkId = parkId;
		}

		public String getTotalSeat() {
			return totalSeat;
		}

		public void setTotalSeat(String totalSeat) {
			this.totalSeat = totalSeat;
		}

		public String getFreeSeat() {
			return freeSeat;
		}

		public void setFreeSeat(String freeSeat) {
			this.freeSeat = freeSeat;
		}

		public String getOrderFreeSeat() {
			return orderFreeSeat;
		}

		public void setOrderFreeSeat(String orderFreeSeat) {
			this.orderFreeSeat = orderFreeSeat;
		}

		public String getParkType() {
			return parkType;
		}

		public void setParkType(String parkType) {
			this.parkType = parkType;
		}

		public String getChargeSet() {
			return chargeSet;
		}

		public void setChargeSet(String chargeSet) {
			this.chargeSet = chargeSet;
		}

		public String getAddress() {
			return address;
		}

		public void setAddress(String address) {
			this.address = address;
		}

		public String getNotice() {
			return notice;
		}

		public void setNotice(String notice) {
			this.notice = notice;
		}

	}

	public List<ParkDetailReturnData> getParkDetailReturnData() {
		return ParkDetailReturnData;
	}

	public void setParkDetailReturnData(List<ParkDetailReturnData> ParkDetailReturnData) {
		this.ParkDetailReturnData = ParkDetailReturnData;
	}

}
