package com.funo.park.client.po;

import java.util.Date;
import java.util.List;

public class ParkListReturnInfo extends BaseRspInfo {

	private List<ParkListReturnData> list;

	public List<ParkListReturnData> getList() {
		return list;
	}

	public void setList(List<ParkListReturnData> list) {
		this.list = list;
	}

	public static class ParkListReturnData implements Comparable<ParkListReturnData> {
		
		private String id;
		private String name;
		private String code;
		private String address;
		private String keywords;
		private Long totalNum;
		private Long freeNum;
		private Long usedNum;
		private String isOrder;
		private Long orderNum;
		private Long freeOrderNum;
		private Long orderedNum;
		private String notice;
		private Date updateTime;
		private String longitude;
		private String latitude;
		private String area;
		private String state;
		private String payType;
		private String merid;
		private String psType;
		private String extra;
		private String chargeSet;
		private Integer isSyschron;
		private Integer orderNo;
		private Integer parkType;
		private double distance;
		private String numStr;
		private String dis;
		// private Integer type;
		private Integer isqy;

		public Integer getIsqy() {
			return isqy;
		}

		public void setIsqy(Integer isqy) {
			this.isqy = isqy;
		}

		// public Integer getType() {
		// return type;
		// }
		//
		// public void setType(Integer type) {
		// this.type = type;
		// }

		public String getDis() {
			return dis;
		}

		public void setDis(String dis) {
			this.dis = dis;
		}

		public String getNumStr() {
			return numStr;
		}

		public void setNumStr(String numStr) {
			this.numStr = numStr;
		}

		public double getDistance() {
			return distance;
		}

		public void setDistance(double distance) {
			this.distance = distance;
		}

		@Override
		public String toString() {
			return this.name;
		}

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getCode() {
			return code;
		}

		public void setCode(String code) {
			this.code = code;
		}

		public String getAddress() {
			return address;
		}

		public void setAddress(String address) {
			this.address = address;
		}

		public String getKeywords() {
			return keywords;
		}

		public void setKeywords(String keywords) {
			this.keywords = keywords;
		}

		public Long getTotalNum() {
			return totalNum;
		}

		public void setTotalNum(Long totalNum) {
			this.totalNum = totalNum;
		}

		public Long getFreeNum() {
			return freeNum;
		}

		public void setFreeNum(Long freeNum) {
			this.freeNum = freeNum;
		}

		public Long getUsedNum() {
			return usedNum;
		}

		public void setUsedNum(Long usedNum) {
			this.usedNum = usedNum;
		}

		public String getIsOrder() {
			return isOrder;
		}

		public void setIsOrder(String isOrder) {
			this.isOrder = isOrder;
		}

		public Long getOrderNum() {
			return orderNum;
		}

		public void setOrderNum(Long orderNum) {
			this.orderNum = orderNum;
		}

		public Long getFreeOrderNum() {
			return freeOrderNum;
		}

		public void setFreeOrderNum(Long freeOrderNum) {
			this.freeOrderNum = freeOrderNum;
		}

		public Long getOrderedNum() {
			return orderedNum;
		}

		public void setOrderedNum(Long orderedNum) {
			this.orderedNum = orderedNum;
		}

		public String getNotice() {
			return notice;
		}

		public void setNotice(String notice) {
			this.notice = notice;
		}

		public Date getUpdateTime() {
			return updateTime;
		}

		public void setUpdateTime(Date updateTime) {
			this.updateTime = updateTime;
		}

		public String getLongitude() {
			return longitude;
		}

		public void setLongitude(String longitude) {
			this.longitude = longitude;
		}

		public String getLatitude() {
			return latitude;
		}

		public void setLatitude(String latitude) {
			this.latitude = latitude;
		}

		public String getArea() {
			return area;
		}

		public void setArea(String area) {
			this.area = area;
		}

		public String getState() {
			return state;
		}

		public void setState(String state) {
			this.state = state;
		}

		public String getPayType() {
			return payType;
		}

		public void setPayType(String payType) {
			this.payType = payType;
		}

		public String getMerid() {
			return merid;
		}

		public void setMerid(String merid) {
			this.merid = merid;
		}

		public String getPsType() {
			return psType;
		}

		public void setPsType(String psType) {
			this.psType = psType;
		}

		public String getExtra() {
			return extra;
		}

		public void setExtra(String extra) {
			this.extra = extra;
		}

		public String getChargeSet() {
			return chargeSet;
		}

		public void setChargeSet(String chargeSet) {
			this.chargeSet = chargeSet;
		}

		public Integer getIsSyschron() {
			return isSyschron;
		}

		public void setIsSyschron(Integer isSyschron) {
			this.isSyschron = isSyschron;
		}

		public Integer getOrderNo() {
			return orderNo;
		}

		public void setOrderNo(Integer orderNo) {
			this.orderNo = orderNo;
		}

		public Integer getParkType() {
			return parkType;
		}

		public void setParkType(Integer parkType) {
			this.parkType = parkType;
		}

		@Override
		public int compareTo(ParkListReturnData another) {
			// System.out.println("another.getDistance()---->" +
			// another.getDistance() + "-------" + this.distance);
			int result = (another.getDistance() < this.distance) ? 1
					: ((another.getDistance() == this.distance) ? 0 : -1);
			return result;
		}
		
	}
	
}
