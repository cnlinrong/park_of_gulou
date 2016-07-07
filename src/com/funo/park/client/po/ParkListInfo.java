package com.funo.park.client.po;

import java.util.ArrayList;
import java.util.List;

public class ParkListInfo extends BaseReqInfo {

	public List<ParkListData> parkListData = new ArrayList<ParkListData>();

	public static class ParkListData {

		protected String method;
		protected String parkName;
		protected String area;
		protected String startRecord;
		protected String pageSize;

		public String getParkName() {
			return parkName;
		}

		public void setParkName(String parkName) {
			this.parkName = parkName;
		}

		public String getArea() {
			return area;
		}

		public void setArea(String area) {
			this.area = area;
		}

		public String getStartRecord() {
			return startRecord;
		}

		public void setStartRecord(String startRecord) {
			this.startRecord = startRecord;
		}

		public String getPageSize() {
			return pageSize;
		}

		public void setPageSize(String pageSize) {
			this.pageSize = pageSize;
		}

		public String getMethod() {
			return method;
		}

		public void setMethod(String method) {
			this.method = method;
		}

	}

	public List<ParkListData> getParkListData() {
		return parkListData;
	}

	public void setParkListData(List<ParkListData> parkListData) {
		this.parkListData = parkListData;
	}

}
