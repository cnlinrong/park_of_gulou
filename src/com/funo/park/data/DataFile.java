package com.funo.park.data;

import java.util.ArrayList;
import java.util.List;

import com.funo.park.mode.BicycleStation;
import com.funo.park.mode.HelpBean;
import com.funo.park.mode.TempPoint;

import android.content.ContentValues;

public class DataFile {

	// 百度经纬度
	public static List<BicycleStation> getBicycleStationList() {
		List<BicycleStation> list = new ArrayList<BicycleStation>();
		BicycleStation bicycleStation = new BicycleStation();
		bicycleStation.setStationName("软件园站");
		bicycleStation.setStationAddr("软件大道软件园正门东侧");
		// bicycleStation.setLat("26.119067");
		// bicycleStation.setLon("119.276862");
		bicycleStation.setLat("26.11270413");
		bicycleStation.setLon("119.27042842499998");

		bicycleStation.setStationNum("20");
		list.add(bicycleStation);

		bicycleStation = new BicycleStation();
		bicycleStation.setStationName("丞相坊站");
		bicycleStation.setStationAddr("丞相路五凤兰庭三期门口北侧");
		bicycleStation.setLat("26.114493");
		bicycleStation.setLon("119.278838");
		bicycleStation.setStationNum("20");
		list.add(bicycleStation);

		bicycleStation = new BicycleStation();
		bicycleStation.setStationName("五凤兰庭站");
		bicycleStation.setStationAddr("丞相路凤宜家园门口西侧");
		bicycleStation.setLat("26.112579");
		bicycleStation.setLon("119.277832");
		bicycleStation.setStationNum("40");
		list.add(bicycleStation);

		bicycleStation = new BicycleStation();
		bicycleStation.setStationName("白龙路站");
		bicycleStation.setStationAddr("白龙路省军区第三干休所对面");
		bicycleStation.setLat("26.108118");
		bicycleStation.setLon("119.282611");
		bicycleStation.setStationNum("40");
		list.add(bicycleStation);

		bicycleStation = new BicycleStation();
		bicycleStation.setStationName("大儒世家站");
		bicycleStation.setStationAddr("梅亭路大儒世家公交站旁");
		bicycleStation.setLat("26.09428");
		bicycleStation.setLon("119.263746");
		bicycleStation.setStationNum("20");
		list.add(bicycleStation);

		bicycleStation = new BicycleStation();
		bicycleStation.setStationName("运盛美之国二期站");
		bicycleStation.setStationAddr("梅峰路运盛美之国二期门口东侧");
		bicycleStation.setLat("26.10096");
		bicycleStation.setLon("119.274966");
		bicycleStation.setStationNum("20");
		list.add(bicycleStation);

		bicycleStation = new BicycleStation();
		bicycleStation.setStationName("梅峰路站");
		bicycleStation.setStationAddr("梅峰路与梅峰支路交叉口西北侧");
		bicycleStation.setLat("26.102408");
		bicycleStation.setLon("119.280109");
		bicycleStation.setStationNum("21");
		list.add(bicycleStation);

		bicycleStation = new BicycleStation();
		bicycleStation.setStationName("左海公园西门站");
		bicycleStation.setStationAddr("二环路左海西门西侧");
		// bicycleStation.setLat("26.103616");
		// bicycleStation.setLon("119.287888");

		bicycleStation.setLat("26.097292049999996");
		bicycleStation.setLon("119.28143097499998");

		bicycleStation.setStationNum("40");
		list.add(bicycleStation);

		bicycleStation = new BicycleStation();
		bicycleStation.setStationName("左海公园北门站");
		bicycleStation.setStationAddr("铜盘路左海北门西侧");
		bicycleStation.setLat("26.107461");
		bicycleStation.setLon("119.292613");
		bicycleStation.setStationNum("40");
		list.add(bicycleStation);

		bicycleStation = new BicycleStation();
		bicycleStation.setStationName("北环西路站");
		bicycleStation.setStationAddr("北二环路中机中泰别克4S店前");
		bicycleStation.setLat("26.109099");
		bicycleStation.setLon("119.291922");
		bicycleStation.setStationNum("20");
		list.add(bicycleStation);

		bicycleStation = new BicycleStation();
		bicycleStation.setStationName("屏西小区站");
		bicycleStation.setStationAddr("屏西小区大门内100米");
		bicycleStation.setLat("26.114769");
		bicycleStation.setLon("119.292465");
		bicycleStation.setStationNum("24");
		list.add(bicycleStation);

		bicycleStation = new BicycleStation();
		bicycleStation.setStationName("五凤社区服务中心站（待开通）");
		bicycleStation.setStationAddr("天泉路天元花园西北侧进小路100米");
		bicycleStation.setLat("26.119635");
		bicycleStation.setLon("119.297316");
		bicycleStation.setStationNum("20");
		list.add(bicycleStation);

		bicycleStation = new BicycleStation();
		bicycleStation.setStationName("市直湖前站");
		bicycleStation.setStationAddr("江厝路与永辉超市湖前店路口");
		bicycleStation.setLat("26.12028");
		bicycleStation.setLon("119.302683");
		bicycleStation.setStationNum("30");
		list.add(bicycleStation);

		bicycleStation = new BicycleStation();
		bicycleStation.setStationName("屏东中学屏北分校站");
		bicycleStation.setStationAddr("琴亭路屏东中学屏北分校右侧路段");
		bicycleStation.setLat("26.124002");
		bicycleStation.setLon("119.308325");
		bicycleStation.setStationNum("20");
		list.add(bicycleStation);

		bicycleStation = new BicycleStation();
		bicycleStation.setStationName("省体育中心站");
		bicycleStation.setStationAddr("五四北路省体育中心公交站旁");
		bicycleStation.setLat("26.116638");
		bicycleStation.setLon("119.312241");
		bicycleStation.setStationNum("30");
		list.add(bicycleStation);

		bicycleStation = new BicycleStation();
		bicycleStation.setStationName("国棉永辉站");
		bicycleStation.setStationAddr("二环路国棉永辉东侧");
		bicycleStation.setLat("26.115016");
		bicycleStation.setLon("119.304624");
		bicycleStation.setStationNum("20");
		list.add(bicycleStation);

		bicycleStation = new BicycleStation();
		bicycleStation.setStationName("华屏路站");
		bicycleStation.setStationAddr("华屏路龙峰农副产品综合市场正对面");
		bicycleStation.setLat("26.111399");
		bicycleStation.setLon("119.307418");
		bicycleStation.setStationNum("30");
		list.add(bicycleStation);

		bicycleStation = new BicycleStation();
		bicycleStation.setStationName("屏山站");
		bicycleStation.setStationAddr("鼓屏路北段农副产品展示中心旁");
		bicycleStation.setLat("26.105332");
		bicycleStation.setLon("119.303043");
		bicycleStation.setStationNum("20");
		list.add(bicycleStation);

		bicycleStation = new BicycleStation();
		bicycleStation.setStationName("西湖公园站");
		bicycleStation.setStationAddr("西湖公园湖滨路中段（省实验幼儿园旁）");
		bicycleStation.setLat("26.098859");
		bicycleStation.setLon("119.299414");
		bicycleStation.setStationNum("30");
		list.add(bicycleStation);

		bicycleStation = new BicycleStation();
		bicycleStation.setStationName("鼓西路孙老营巷站");
		bicycleStation.setStationAddr("鼓西路孙老营巷路口东南侧");
		bicycleStation.setLat("26.094236");
		bicycleStation.setLon("119.297815");
		bicycleStation.setStationNum("20");
		list.add(bicycleStation);

		bicycleStation = new BicycleStation();
		bicycleStation.setStationName("通湖路站");
		bicycleStation.setStationAddr("通湖路西湖影剧院南80米");
		bicycleStation.setLat("26.095339");
		bicycleStation.setLon("119.296934");
		bicycleStation.setStationNum("20");
		list.add(bicycleStation);

		bicycleStation = new BicycleStation();
		bicycleStation.setStationName("湖滨新城小区站");
		bicycleStation.setStationAddr("北梦山路湖滨新城小区门口东侧");
		bicycleStation.setLat("26.096815");
		bicycleStation.setLon("119.291239");
		bicycleStation.setStationNum("15");
		list.add(bicycleStation);

		bicycleStation = new BicycleStation();
		bicycleStation.setStationName("西洪路站");
		bicycleStation.setStationAddr("西洪路816花园门口西侧");
		bicycleStation.setLat("26.088922");
		bicycleStation.setLon("119.28434");
		bicycleStation.setStationNum("20");
		list.add(bicycleStation);

		bicycleStation = new BicycleStation();
		bicycleStation.setStationName("茶园山小学站");
		bicycleStation.setStationAddr("杨桥中路茶园山小学正对面");
		bicycleStation.setLat("26.086545");
		bicycleStation.setLon("119.280612");
		bicycleStation.setStationNum("30");
		list.add(bicycleStation);

		bicycleStation = new BicycleStation();
		bicycleStation.setStationName("福大东门站");
		bicycleStation.setStationAddr("工业路福大东门北侧");
		bicycleStation.setLat("26.081037");
		bicycleStation.setLon("119.282696");
		bicycleStation.setStationNum("20");
		list.add(bicycleStation);

		bicycleStation = new BicycleStation();
		bicycleStation.setStationName("金牛山公园站");
		bicycleStation.setStationAddr("金牛山公园正门西侧");
		bicycleStation.setLat("26.082489");
		bicycleStation.setLon("119.267721");
		bicycleStation.setStationNum("40");
		list.add(bicycleStation);

		bicycleStation = new BicycleStation();
		bicycleStation.setStationName("洪山桥头站");
		bicycleStation.setStationAddr("洪山桥头公交站旁");
		bicycleStation.setLat("26.084209");
		bicycleStation.setLon("119.261864");
		bicycleStation.setStationNum("30");
		list.add(bicycleStation);

		bicycleStation = new BicycleStation();
		bicycleStation.setStationName("西河游泳场站");
		bicycleStation.setStationAddr("西河游泳场内");
		bicycleStation.setLat("26.080218");
		bicycleStation.setLon("119.266194");
		bicycleStation.setStationNum("20");
		list.add(bicycleStation);

		bicycleStation = new BicycleStation();
		bicycleStation.setStationName("北江滨融侨锦江站");
		bicycleStation.setStationAddr("北江滨辅道与凤湖路交叉路口");
		bicycleStation.setLat("26.078011");
		bicycleStation.setLon("119.272015");
		bicycleStation.setStationNum("20");
		list.add(bicycleStation);

		bicycleStation = new BicycleStation();
		bicycleStation.setStationName("融侨锦江站");
		bicycleStation.setStationAddr("凤湖路与乌山西路交叉西南侧口");
		bicycleStation.setLat("26.073962");
		bicycleStation.setLon("119.278528");
		bicycleStation.setStationNum("20");
		list.add(bicycleStation);

		bicycleStation = new BicycleStation();
		bicycleStation.setStationName("美景良辰站");
		bicycleStation.setStationAddr("乌山西路美景良辰门口西侧");
		bicycleStation.setLat("26.077427");
		bicycleStation.setLon("119.289577");
		bicycleStation.setStationNum("20");
		list.add(bicycleStation);

		bicycleStation = new BicycleStation();
		bicycleStation.setStationName("省委党校站");
		bicycleStation.setStationAddr("西二环中路省委党校西大门");
		bicycleStation.setLat("26.081321");
		bicycleStation.setLon("119.291904");
		bicycleStation.setStationNum("20");
		list.add(bicycleStation);

		bicycleStation = new BicycleStation();
		bicycleStation.setStationName("杨桥永辉站");
		bicycleStation.setStationAddr("杨桥路杨桥永辉正门西侧");
		bicycleStation.setLat("26.088918");
		bicycleStation.setLon("119.290651");
		bicycleStation.setStationNum("30");
		list.add(bicycleStation);

		bicycleStation = new BicycleStation();
		bicycleStation.setStationName("光禄坊站");
		bicycleStation.setStationAddr("光禄坊公园正对面");
		bicycleStation.setLat("26.085722");
		bicycleStation.setLon("119.301394");
		bicycleStation.setStationNum("15");
		list.add(bicycleStation);

		bicycleStation = new BicycleStation();
		bicycleStation.setStationName("杨桥路双抛桥站");
		bicycleStation.setStationAddr("杨桥路双抛桥斜对面");
		bicycleStation.setLat("26.091952");
		bicycleStation.setLon("119.301224");
		bicycleStation.setStationNum("20");
		list.add(bicycleStation);

		bicycleStation = new BicycleStation();
		bicycleStation.setStationName("东街口万霞广场站");
		bicycleStation.setStationAddr("八一七北路万霞广场前");
		bicycleStation.setLat("26.094637");
		bicycleStation.setLon("119.305262");
		bicycleStation.setStationNum("40");
		list.add(bicycleStation);

		bicycleStation = new BicycleStation();
		bicycleStation.setStationName("鼓东路站");
		bicycleStation.setStationAddr("鼓东路与井大路交叉路口西北侧");
		bicycleStation.setLat("26.095757");
		bicycleStation.setLon("119.307072");
		bicycleStation.setStationNum("20");
		list.add(bicycleStation);

		bicycleStation = new BicycleStation();
		bicycleStation.setStationName("中山路口站");
		bicycleStation.setStationAddr("省财政厅、省移动大厦旁（中山大厦门口）");
		bicycleStation.setLat("26.098831");
		bicycleStation.setLon("119.307656");
		bicycleStation.setStationNum("20");
		list.add(bicycleStation);

		bicycleStation = new BicycleStation();
		bicycleStation.setStationName("五四路中段路");
		bicycleStation.setStationAddr("恒力城旁（外贸中心酒店斜对面）");
		bicycleStation.setLat("26.099013");
		bicycleStation.setLon("119.313005");
		bicycleStation.setStationNum("20");
		list.add(bicycleStation);

		bicycleStation = new BicycleStation();
		bicycleStation.setStationName("信和广场站");
		bicycleStation.setStationAddr("五四路信和广场南侧交通银行前");
		bicycleStation.setLat("26.102631");
		bicycleStation.setLon("119.313023");
		bicycleStation.setStationNum("20");
		list.add(bicycleStation);

		bicycleStation = new BicycleStation();
		bicycleStation.setStationName("华林四桥站");
		bicycleStation.setStationAddr("华林路台湾饭店对面");
		bicycleStation.setLat("26.109655");
		bicycleStation.setLon("119.318094");
		bicycleStation.setStationNum("30");
		list.add(bicycleStation);

		bicycleStation = new BicycleStation();
		bicycleStation.setStationName("中医学院站");
		bicycleStation.setStationAddr("五四北路中医学院门口北侧");
		bicycleStation.setLat("26.111415");
		bicycleStation.setLon("119.312399");
		bicycleStation.setStationNum("20");
		list.add(bicycleStation);

		bicycleStation = new BicycleStation();
		bicycleStation.setStationName("公益路站");
		bicycleStation.setStationAddr("公益路1号北侧");
		bicycleStation.setLat("26.114375");
		bicycleStation.setLon("119.316513");
		bicycleStation.setStationNum("30");
		list.add(bicycleStation);

		bicycleStation = new BicycleStation();
		bicycleStation.setStationName("温泉公园站");
		bicycleStation.setStationAddr("温泉公园路温泉公园北门东侧");
		bicycleStation.setLat("26.105677");
		bicycleStation.setLon("119.319172");
		bicycleStation.setStationNum("45");
		list.add(bicycleStation);

		bicycleStation = new BicycleStation();
		bicycleStation.setStationName("省图书馆站");
		bicycleStation.setStationAddr("湖东路省图书馆正门东侧");
		bicycleStation.setLat("26.100246");
		bicycleStation.setLon("119.319527");
		bicycleStation.setStationNum("20");
		list.add(bicycleStation);

		bicycleStation = new BicycleStation();
		bicycleStation.setStationName("温泉支路站");
		bicycleStation.setStationAddr("温泉支路温泉宾馆斜对面");
		bicycleStation.setLat("26.096823");
		bicycleStation.setLon("119.320362");
		bicycleStation.setStationNum("25");
		list.add(bicycleStation);

		bicycleStation = new BicycleStation();
		bicycleStation.setStationName("蒙古营站");
		bicycleStation.setStationAddr("五一北路五金批发公司前");
		bicycleStation.setLat("26.089888");
		bicycleStation.setLon("119.31543");
		bicycleStation.setStationNum("20");
		list.add(bicycleStation);

		bicycleStation = new BicycleStation();
		bicycleStation.setStationName("鼓二小站");
		bicycleStation.setStationAddr("东泰路鼓二小斜对面");
		bicycleStation.setLat("26.090512");
		bicycleStation.setLon("119.309268");
		bicycleStation.setStationNum("20");
		list.add(bicycleStation);

		bicycleStation = new BicycleStation();
		bicycleStation.setStationName("鼓楼区政府大院站");
		bicycleStation.setStationAddr("津泰路98号鼓楼区委大院3号楼前");
		bicycleStation.setLat("26.088083");
		bicycleStation.setLon("119.310499");
		bicycleStation.setStationNum("30");
		list.add(bicycleStation);

		bicycleStation = new BicycleStation();
		bicycleStation.setStationName("津泰路军门社区站（待建）");
		bicycleStation.setStationAddr("南营巷内中段");
		bicycleStation.setLat("26.087417");
		bicycleStation.setLon("119.307768");
		bicycleStation.setStationNum("20");
		list.add(bicycleStation);

		bicycleStation = new BicycleStation();
		bicycleStation.setStationName("乌石山北坡站");
		bicycleStation.setStationAddr("澳门路尽头乌石山北坡公园旁");
		bicycleStation.setLat("26.084319");
		bicycleStation.setLon("119.304529");
		bicycleStation.setStationNum("20");
		list.add(bicycleStation);

		bicycleStation = new BicycleStation();
		bicycleStation.setStationName("福中路西路口站");
		bicycleStation.setStationAddr("福中路西路口（安泰街道）");
		bicycleStation.setLat("26.078238");
		bicycleStation.setLon("119.308401");
		bicycleStation.setStationNum("30");
		list.add(bicycleStation);

		bicycleStation = new BicycleStation();
		bicycleStation.setStationName("冠亚广场站");
		bicycleStation.setStationAddr("乌山路冠亚广场大洋晶典西侧");
		bicycleStation.setLat("26.082655");
		bicycleStation.setLon("119.308262");
		bicycleStation.setStationNum("20");
		list.add(bicycleStation);

		bicycleStation = new BicycleStation();
		bicycleStation.setStationName("五一广场站");
		bicycleStation.setStationAddr("古田路福建省科技馆北侧");
		bicycleStation.setLat("26.082282");
		bicycleStation.setLon("119.314923");
		bicycleStation.setStationNum("40");
		list.add(bicycleStation);

		bicycleStation = new BicycleStation();
		bicycleStation.setStationName("省新闻出版局站");
		bicycleStation.setStationAddr("东水路省新闻出版社局门口南侧");
		bicycleStation.setLat("26.089839");
		bicycleStation.setLon("119.321647");
		bicycleStation.setStationNum("20");
		list.add(bicycleStation);

		bicycleStation = new BicycleStation();
		bicycleStation.setStationName("古田路站");
		bicycleStation.setStationAddr("古田路光大银行前");
		bicycleStation.setLat("26.082286");
		bicycleStation.setLon("119.321849");
		bicycleStation.setStationNum("20");
		list.add(bicycleStation);

		bicycleStation = new BicycleStation();
		bicycleStation.setStationName("六一环岛站");
		bicycleStation.setStationAddr("古田支路路口劳动大厦旁");
		bicycleStation.setLat("26.082238");
		bicycleStation.setLon("119.325559");
		bicycleStation.setStationNum("20");
		list.add(bicycleStation);

		bicycleStation = new BicycleStation();
		bicycleStation.setStationName("古乐路省六建站");
		bicycleStation.setStationAddr("古乐路省六建后门东侧");
		bicycleStation.setLat("26.078051");
		bicycleStation.setLon("119.323636");
		bicycleStation.setStationNum("20");
		list.add(bicycleStation);

		bicycleStation = new BicycleStation();
		bicycleStation.setStationName("冠茂都会小区站");
		bicycleStation.setStationAddr("六一路冠茂都会小区前");
		bicycleStation.setLat("26.076859");
		bicycleStation.setLon("119.326349");
		bicycleStation.setStationNum("20");
		list.add(bicycleStation);
		return list;
	}

	public static List<HelpBean> getHelpBeans() {
		List<HelpBean> list = new ArrayList<HelpBean>();
		HelpBean hb = new HelpBean();
		hb.setTitle("公共自行车借还车操作指南");
		hb.setContent(
				"借车者按规定程序办理IC卡，即可在福州市公共自行车任何管理服务站点（任一服务站点）的感应器上进行刷卡操作，借用公共自行车。\n借车：\n将具有借车功能的IC卡放在感应器的刷卡区刷卡，此时感应器界面上绿灯亮起并语音提示“请取出”，表示锁孔已打开，借车者应及时将车取出，则完成借车。若3分钟内未取车，锁止器将自动重新上锁，并且此卡被置为已借车状态，而无法再次借车，需与客服人员联系处理。\n还车：\n将所借的自行车推入锁孔，当绿灯亮时，语音提示“请刷卡”，及时将借车的IC卡放在感应区刷卡，语音提示“还车成功”。但3分钟内未刷卡，卡片依旧为借车状态，而无法再次借车，需与客服人员联系处理。");
		list.add(hb);

		hb = new HelpBean();
		hb.setTitle("公共自行车IC卡申办须知");
		hb.setContent(
				"IC卡的申领\n1、IC卡申领需凭本人有效身份证件及复印件一张至公司指定地点或服务点办理申领。\n2、签订《租用服务协议》，一次性交纳信用保证金300元，IC卡工本费20元，卡内充值启用资金不低于50元。\n3、取卡并开通自行车租用业务。\nIC卡充值\n本卡可反复充值使用，除开卡时启用资金至少充值50元外，后续充值金额无限制。");
		list.add(hb);

		hb = new HelpBean();
		hb.setTitle("福州市公共自行车换卡须知");
		hb.setContent(
				"换卡办理须知\n换卡对象：卡失效或卡异常等（非人为原因造成卡无法使用的）。客户前往各服务窗口办理换卡登记业务，24小时后办理手续（须预先核实系统借还车数据）。24小时后，客户到各窗口出示本人办卡时的有效身份证件办理手续。填写《福州市公共自行车运营管理有限公司个人IC卡补卡（换卡）申请表》。免收IC卡工本费。");
		list.add(hb);

		hb = new HelpBean();
		hb.setTitle("福州市公共自行车补卡须知");
		hb.setContent(
				"补卡办理须知\n补卡对象：卡遗失、卡损坏（因持卡人弯折、烘烤、打孔、浸水等原因造成的）。客户前往各服务窗口办理补卡登记业务，24小时后办理手续（须预先核实系统借还车数据）。24小时后，客户到各窗口提供本人办卡时的有效身份证件及复印件办理手续。填写《福州市公共自行车运营管理有限公司个人IC卡补卡（换卡）申请表》。缴纳IC卡工本费20元。");
		list.add(hb);

		hb = new HelpBean();
		hb.setTitle("福州市公共自行车退卡须知");
		hb.setContent(
				"退卡办理须知\n客户前往各服务窗口办理退卡登记业务，24小时后办理手续（须预先核实系统借还车数据）。24小时后，客户到各窗口提供本人办卡时的有效身份证件及复印件办理手续。填写《福州市公共自行车运营管理有限公司个人IC卡销户(退卡)申请表》。退回信用保证金300元与IC卡电子钱包剩余资金，完成退卡手续。办理IC卡时缴纳的20元工本费不予以退还。");
		list.add(hb);

		hb = new HelpBean();
		hb.setTitle("公共自行车借车收费标准 ");
		hb.setContent(
				"1小时以内：免费；1-2小时以内：1元；2-3小时以内：3元；3-4小时以内：6元；4-5小时以内：9元；5-6小时以内：12元；6-7小时以内：15元；7-8小时以内：18元；8-9小时以内：21元；9-10小时以内：24元；10-11小时以内：27元；11-24小时以内：30元。");
		list.add(hb);

		return list;
	}

	public static List<ContentValues> getDataBaseDatas() {
		List<ContentValues> list = new ArrayList<ContentValues>();
		ContentValues cv = null;
		for (int i = 0; i < getBicycleStationList().size(); i++) {
			cv = new ContentValues();
			cv.put("name", getBicycleStationList().get(i).getStationName());
			cv.put("addr", getBicycleStationList().get(i).getStationAddr());
			cv.put("lat", getBicycleStationList().get(i).getLat());
			cv.put("lon", getBicycleStationList().get(i).getLon());
			cv.put("collectStatus", "0");
			cv.put("collectTime", "");
			list.add(cv);
		}
		return list;
	}

	// 临时经纬度
	public static List<TempPoint> getTempPoints() {
		// 软件园站
		List<TempPoint> list = new ArrayList<TempPoint>();
		TempPoint tempPoint = new TempPoint();
		tempPoint.setLat(26.122270507279);
		tempPoint.setLon(119.28805217393);
		list.add(tempPoint);
		// 丞相坊站
		tempPoint = new TempPoint();
		tempPoint.setLat(26.117696967189);
		tempPoint.setLon(119.29003436563);
		list.add(tempPoint);

		// 五凤兰庭站
		tempPoint = new TempPoint();
		tempPoint.setLat(26.115786878454);
		tempPoint.setLon(119.28902815819);
		list.add(tempPoint);
		// 白龙路站
		tempPoint = new TempPoint();
		tempPoint.setLat(26.111311791639);
		tempPoint.setLon(119.29382153868);
		list.add(tempPoint);
		// 大儒世家站
		tempPoint = new TempPoint();
		tempPoint.setLat(26.097433311932);
		tempPoint.setLon(119.2749614506);
		list.add(tempPoint);
		// 运盛美之国二期站
		tempPoint = new TempPoint();
		tempPoint.setLat(26.104178389716);
		tempPoint.setLon(119.28616948936);
		list.add(tempPoint);

		// 梅峰路站
		tempPoint = new TempPoint();
		tempPoint.setLat(26.10561852385);
		tempPoint.setLon(119.29131866552);
		list.add(tempPoint);

		// 左海公园西门站
		tempPoint = new TempPoint();
		tempPoint.setLat(26.106770816958);
		tempPoint.setLon(119.2991201281);
		list.add(tempPoint);

		// 左海公园北门站
		tempPoint = new TempPoint();
		tempPoint.setLat(26.110556288076);
		tempPoint.setLon(119.30386192719);
		list.add(tempPoint);

		// 北环西路站
		tempPoint = new TempPoint();
		tempPoint.setLat(26.112201782309);
		tempPoint.setLon(119.30316609734);
		list.add(tempPoint);

		// 屏西小区站
		tempPoint = new TempPoint();
		tempPoint.setLat(26.117859774827);
		tempPoint.setLon(119.30370681025);
		list.add(tempPoint);

		// 五凤社区服务中心站（待开通）
		tempPoint = new TempPoint();
		tempPoint.setLat(26.122651405356);
		tempPoint.setLon(119.30857884656);
		list.add(tempPoint);

		// 市直湖前站
		tempPoint = new TempPoint();
		tempPoint.setLat(26.123207793647);
		tempPoint.setLon(119.31397557386);
		list.add(tempPoint);

		// 屏东中学屏北分校站
		tempPoint = new TempPoint();
		tempPoint.setLat(26.126830921251);
		tempPoint.setLon(119.31964900512);
		list.add(tempPoint);

		// 省体育中心站
		tempPoint = new TempPoint();
		tempPoint.setLat(26.119409004217);
		tempPoint.setLon(119.32359165385);
		list.add(tempPoint);

		// 国棉永辉站
		tempPoint = new TempPoint();
		tempPoint.setLat(26.117914642005);
		tempPoint.setLon(119.31593130425);
		list.add(tempPoint);

		// 华屏路站
		tempPoint = new TempPoint();
		tempPoint.setLat(26.114253502537);
		tempPoint.setLon(119.31874460905);
		list.add(tempPoint);

		// 屏山站
		tempPoint = new TempPoint();
		tempPoint.setLat(26.108266077301);
		tempPoint.setLon(119.31434948785);
		list.add(tempPoint);

		// 西湖公园站
		tempPoint = new TempPoint();
		tempPoint.setLat(26.10185947946);
		tempPoint.setLon(119.31070598582);
		list.add(tempPoint);

		// 鼓西路孙老营巷站
		tempPoint = new TempPoint();
		tempPoint.setLat(26.097266030471);
		tempPoint.setLon(119.30910242041);
		list.add(tempPoint);

		// 通湖路站
		tempPoint = new TempPoint();
		tempPoint.setLat(26.098382049508);
		tempPoint.setLon(119.30821564775);
		list.add(tempPoint);

		// 湖滨新城小区站
		tempPoint = new TempPoint();
		tempPoint.setLat(26.0999371702);
		tempPoint.setLon(119.30249139145);
		list.add(tempPoint);

		// 西洪路站
		tempPoint = new TempPoint();
		tempPoint.setLat(26.092120893202);
		tempPoint.setLon(119.29557215496);
		list.add(tempPoint);

		// 茶园山小学站
		tempPoint = new TempPoint();
		tempPoint.setLat(26.089767336614);
		tempPoint.setLon(119.2918360835);
		list.add(tempPoint);

		// 福大东门站
		tempPoint = new TempPoint();
		tempPoint.setLat(26.084252953788);
		tempPoint.setLon(119.29392767247);
		list.add(tempPoint);

		// 金牛山公园站
		tempPoint = new TempPoint();
		tempPoint.setLat(26.085689771969);
		tempPoint.setLon(119.27893845752);
		list.add(tempPoint);

		// 洪山桥头站
		tempPoint = new TempPoint();
		tempPoint.setLat(26.087348501021);
		tempPoint.setLon(119.27309006758);
		list.add(tempPoint);

		// 西河游泳场站
		tempPoint = new TempPoint();
		tempPoint.setLat(26.083407461272);
		tempPoint.setLon(119.27741400933);
		list.add(tempPoint);

		// 北江滨融侨锦江站
		tempPoint = new TempPoint();
		tempPoint.setLat(26.081240547821);
		tempPoint.setLon(119.28323214473);
		list.add(tempPoint);

		// 融侨锦江站
		tempPoint = new TempPoint();
		tempPoint.setLat(26.077200128986);
		tempPoint.setLon(119.28975139685);
		list.add(tempPoint);

		// 美景良辰站
		tempPoint = new TempPoint();
		tempPoint.setLat(26.080584803263);
		tempPoint.setLon(119.30083341238);
		list.add(tempPoint);

		// 省委党校站
		tempPoint = new TempPoint();
		tempPoint.setLat(26.08444739471);
		tempPoint.setLon(119.30316978505);
		list.add(tempPoint);

		// 杨桥永辉站
		tempPoint = new TempPoint();
		tempPoint.setLat(26.092054259372);
		tempPoint.setLon(119.30190706032);
		list.add(tempPoint);

		// 光禄坊站
		tempPoint = new TempPoint();
		tempPoint.setLat(26.08870066749);
		tempPoint.setLon(119.31270712385);
		list.add(tempPoint);

		// 杨桥路双抛桥站
		tempPoint = new TempPoint();
		tempPoint.setLat(26.094928348483);
		tempPoint.setLon(119.31253224359);
		list.add(tempPoint);

		// 东街口万霞广场站
		tempPoint = new TempPoint();
		tempPoint.setLat(26.097542711692);
		tempPoint.setLon(119.31659117548);
		list.add(tempPoint);

		// 鼓东路站
		tempPoint = new TempPoint();
		tempPoint.setLat(26.098630787027);
		tempPoint.setLon(119.3184109664);
		list.add(tempPoint);

		// 中山路口站
		tempPoint = new TempPoint();
		tempPoint.setLat(26.101692420657);
		tempPoint.setLon(119.31899574913);
		list.add(tempPoint);

		// 五四路中段路
		tempPoint = new TempPoint();
		tempPoint.setLat(26.10178741946);
		tempPoint.setLon(119.32437527263);
		list.add(tempPoint);

		// 信和广场站
		tempPoint = new TempPoint();
		tempPoint.setLat(26.105401756505);
		tempPoint.setLon(119.32438985293);
		list.add(tempPoint);

		// 华林四桥站
		tempPoint = new TempPoint();
		tempPoint.setLat(26.11234806071);
		tempPoint.setLon(119.32948168269);
		list.add(tempPoint);

		// 中医学院站
		tempPoint = new TempPoint();
		tempPoint.setLat(26.114188050542);
		tempPoint.setLon(119.32375433015);
		list.add(tempPoint);

		// 公益路站
		tempPoint = new TempPoint();
		tempPoint.setLat(26.117084804031);
		tempPoint.setLon(119.32788878757);
		list.add(tempPoint);

		// 温泉公园站
		tempPoint = new TempPoint();
		tempPoint.setLat(26.10835999285);
		tempPoint.setLon(119.3305689013);
		list.add(tempPoint);

		// 省图书馆站
		tempPoint = new TempPoint();
		tempPoint.setLat(26.102929323961);
		tempPoint.setLon(119.33093085618);
		list.add(tempPoint);

		// 温泉支路站
		tempPoint = new TempPoint();
		tempPoint.setLat(26.099499807642);
		tempPoint.setLon(119.33177301135);
		list.add(tempPoint);

		// 蒙古营站
		tempPoint = new TempPoint();
		tempPoint.setLat(26.092634172936);
		tempPoint.setLon(119.32682134876);
		list.add(tempPoint);

		// 鼓二小站
		tempPoint = new TempPoint();
		tempPoint.setLat(26.093353475737);
		tempPoint.setLon(119.32062393021);
		list.add(tempPoint);

		// 鼓楼区政府大院站
		tempPoint = new TempPoint();
		tempPoint.setLat(26.090906342775);
		tempPoint.setLon(119.32186387015);
		list.add(tempPoint);

		// 津泰路军门社区站（待建）
		tempPoint = new TempPoint();
		tempPoint.setLat(26.090286283054);
		tempPoint.setLon(119.31911738264);
		list.add(tempPoint);

		// 乌石山北坡站
		tempPoint = new TempPoint();
		tempPoint.setLat(26.087245704387);
		tempPoint.setLon(119.31586118689);
		list.add(tempPoint);

		// 福中路西路口站
		tempPoint = new TempPoint();
		tempPoint.setLat(26.081103719813);
		tempPoint.setLon(119.31975789173);
		list.add(tempPoint);

		// 冠亚广场站
		tempPoint = new TempPoint();
		tempPoint.setLat(26.085519703793);
		tempPoint.setLon(119.31961702101);
		list.add(tempPoint);

		// 五一广场站
		tempPoint = new TempPoint();
		tempPoint.setLat(26.085041487622);
		tempPoint.setLon(119.32631604602);
		list.add(tempPoint);

		// 省新闻出版局站
		tempPoint = new TempPoint();
		tempPoint.setLat(26.092507888987);
		tempPoint.setLon(119.33306964239);
		list.add(tempPoint);

		// 古田路站
		tempPoint = new TempPoint();
		tempPoint.setLat(26.0849590294);
		tempPoint.setLon(119.33327681077);
		list.add(tempPoint);

		// 六一环岛站
		tempPoint = new TempPoint();
		tempPoint.setLat(26.084878835337);
		tempPoint.setLon(119.33700257986);
		list.add(tempPoint);

		// 古乐路省六建站
		tempPoint = new TempPoint();
		tempPoint.setLat(26.080710294419);
		tempPoint.setLon(119.33507289897);
		list.add(tempPoint);

		// 冠茂都会小区站
		tempPoint = new TempPoint();
		tempPoint.setLat(26.079498565323);
		tempPoint.setLon(119.3377969102);
		list.add(tempPoint);

		return list;
	}

	// 纠偏后的经纬度
	public static List<BicycleStation> getModifyBicycleStationList() {
		List<BicycleStation> bStationList = new ArrayList<BicycleStation>();
		List<BicycleStation> bicycleStationList = new ArrayList<BicycleStation>();
		List<TempPoint> tempPointList = new ArrayList<TempPoint>();
		bicycleStationList = getBicycleStationList();
		tempPointList = getTempPoints();
		BicycleStation bicycleStation = new BicycleStation();
		TempPoint tempPoint = new TempPoint();
		double lat = 0.0;
		double lon = 0.0;
		for (int i = 0; i < bicycleStationList.size(); i++) {
			bicycleStation = bicycleStationList.get(i);
			tempPoint = tempPointList.get(i);
			lat = 2 * Double.valueOf(bicycleStation.getLat()) - tempPoint.getLat();
			System.out.println("lat_" + i + ":" + lat);
			lon = 2 * Double.valueOf(bicycleStation.getLon()) - tempPoint.getLon();
			System.out.println("lon_" + i + ":" + lon);
			bicycleStation.setLat(String.valueOf(lat));
			bicycleStation.setLon(String.valueOf(lon));
			bStationList.add(bicycleStation);
		}
		return bStationList;
	}

	// 谷歌经纬度
	public static List<BicycleStation> getGoogleBicycleStationList() {
		List<BicycleStation> list = new ArrayList<BicycleStation>();
		BicycleStation bicycleStation = new BicycleStation();
		bicycleStation.setStationName("软件园站");
		bicycleStation.setStationAddr("软件大道软件园正门东侧");
		bicycleStation.setLat("26.11270413");
		bicycleStation.setLon("119.27042842499998");
		bicycleStation.setStationNum("20");
		list.add(bicycleStation);

		bicycleStation = new BicycleStation();
		bicycleStation.setStationName("丞相坊站");
		bicycleStation.setStationAddr("丞相路五凤兰庭三期门口北侧");
		bicycleStation.setLat("26.10813013");
		bicycleStation.setLon("119.27240442499999");
		bicycleStation.setStationNum("20");
		list.add(bicycleStation);

		bicycleStation = new BicycleStation();
		bicycleStation.setStationName("五凤兰庭站");
		bicycleStation.setStationAddr("丞相路凤宜家园门口西侧");
		bicycleStation.setLat("26.10622363");
		bicycleStation.setLon("119.27139842500003");
		bicycleStation.setStationNum("40");
		list.add(bicycleStation);

		bicycleStation = new BicycleStation();
		bicycleStation.setStationName("白龙路站");
		bicycleStation.setStationAddr("白龙路省军区第三干休所对面");
		bicycleStation.setLat("26.10179405");
		bicycleStation.setLon("119.276153975");
		bicycleStation.setStationNum("40");
		list.add(bicycleStation);

		bicycleStation = new BicycleStation();
		bicycleStation.setStationName("大儒世家站");
		bicycleStation.setStationAddr("梅亭路大儒世家公交站旁");
		bicycleStation.setLat("26.087968335000003");
		bicycleStation.setLon("119.25728864999996");
		bicycleStation.setStationNum("20");
		list.add(bicycleStation);

		bicycleStation = new BicycleStation();
		bicycleStation.setStationName("运盛美之国二期站");
		bicycleStation.setStationAddr("梅峰路运盛美之国二期门口东侧");
		bicycleStation.setLat("26.0946036175");
		bicycleStation.setLon("119.268523975");
		bicycleStation.setStationNum("20");
		list.add(bicycleStation);

		bicycleStation = new BicycleStation();
		bicycleStation.setStationName("梅峰路站");
		bicycleStation.setStationAddr("梅峰路与梅峰支路交叉口西北侧");
		bicycleStation.setLat("26.0960853");
		bicycleStation.setLon("119.27364647499996");
		bicycleStation.setStationNum("21");
		list.add(bicycleStation);

		bicycleStation = new BicycleStation();
		bicycleStation.setStationName("左海公园西门站");
		bicycleStation.setStationAddr("二环路左海西门西侧");
		bicycleStation.setLat("26.097292049999996");
		bicycleStation.setLon("119.28143097499998");

		bicycleStation.setStationNum("40");
		list.add(bicycleStation);

		bicycleStation = new BicycleStation();
		bicycleStation.setStationName("左海公园北门站");
		bicycleStation.setStationAddr("铜盘路左海北门西侧");
		bicycleStation.setLat("26.1012570675");
		bicycleStation.setLon("119.28612275");
		bicycleStation.setStationNum("40");
		list.add(bicycleStation);

		bicycleStation = new BicycleStation();
		bicycleStation.setStationName("北环西路站");
		bicycleStation.setStationAddr("北二环路中机中泰别克4S店前");
		bicycleStation.setLat("26.1028950675");
		bicycleStation.setLon("119.28543174999998");
		bicycleStation.setStationNum("20");
		list.add(bicycleStation);

		bicycleStation = new BicycleStation();
		bicycleStation.setStationName("屏西小区站");
		bicycleStation.setStationAddr("屏西小区大门内100米");
		bicycleStation.setLat("26.10856759");
		bicycleStation.setLon("119.28598320000003");
		bicycleStation.setStationNum("24");
		list.add(bicycleStation);

		bicycleStation = new BicycleStation();
		bicycleStation.setStationName("五凤社区服务中心站（待开通）");
		bicycleStation.setStationAddr("天泉路天元花园西北侧进小路100米");
		bicycleStation.setLat("26.11343359");
		bicycleStation.setLon("119.2908342");
		bicycleStation.setStationNum("20");
		list.add(bicycleStation);

		bicycleStation = new BicycleStation();
		bicycleStation.setStationName("市直湖前站");
		bicycleStation.setStationAddr("江厝路与永辉超市湖前店路口");
		bicycleStation.setLat("26.1142563");
		bicycleStation.setLon("119.29616607499997");
		bicycleStation.setStationNum("30");
		list.add(bicycleStation);

		bicycleStation = new BicycleStation();
		bicycleStation.setStationName("屏东中学屏北分校站");
		bicycleStation.setStationAddr("琴亭路屏东中学屏北分校右侧路段");
		bicycleStation.setLat("26.11797105");
		bicycleStation.setLon("119.30180932500002");
		bicycleStation.setStationNum("20");
		list.add(bicycleStation);

		bicycleStation = new BicycleStation();
		bicycleStation.setStationName("省体育中心站");
		bicycleStation.setStationAddr("五四北路省体育中心公交站旁");
		bicycleStation.setLat("26.11077654");
		bicycleStation.setLon("119.30567852499996");
		bicycleStation.setStationNum("30");
		list.add(bicycleStation);

		bicycleStation = new BicycleStation();
		bicycleStation.setStationName("国棉永辉站");
		bicycleStation.setStationAddr("二环路国棉永辉东侧");
		bicycleStation.setLat("26.108984489999997");
		bicycleStation.setLon("119.2981016");
		bicycleStation.setStationNum("20");
		list.add(bicycleStation);

		bicycleStation = new BicycleStation();
		bicycleStation.setStationName("华屏路站");
		bicycleStation.setStationAddr("华屏路龙峰农副产品综合市场正对面");
		bicycleStation.setLat("26.105372239999997");
		bicycleStation.setLon("119.30089559999999");
		bicycleStation.setStationNum("30");
		list.add(bicycleStation);

		bicycleStation = new BicycleStation();
		bicycleStation.setStationName("屏山站");
		bicycleStation.setStationAddr("鼓屏路北段农副产品展示中心旁");
		bicycleStation.setLat("26.099296715");
		bicycleStation.setLon("119.29651222500001");
		bicycleStation.setStationNum("20");
		list.add(bicycleStation);

		bicycleStation = new BicycleStation();
		bicycleStation.setStationName("西湖公园站");
		bicycleStation.setStationAddr("西湖公园湖滨路中段（省实验幼儿园旁）");
		bicycleStation.setLat("26.09265362");
		bicycleStation.setLon("119.29291495000001");
		bicycleStation.setStationNum("30");
		list.add(bicycleStation);

		bicycleStation = new BicycleStation();
		bicycleStation.setStationName("鼓西路孙老营巷站");
		bicycleStation.setStationAddr("鼓西路孙老营巷路口东南侧");
		bicycleStation.setLat("26.088030619999998");
		bicycleStation.setLon("119.29131595000001");
		bicycleStation.setStationNum("20");
		list.add(bicycleStation);

		bicycleStation = new BicycleStation();
		bicycleStation.setStationName("通湖路站");
		bicycleStation.setStationAddr("通湖路西湖影剧院南80米");
		bicycleStation.setLat("26.08913362");
		bicycleStation.setLon("119.29043494999996");
		bicycleStation.setStationNum("20");
		list.add(bicycleStation);

		bicycleStation = new BicycleStation();
		bicycleStation.setStationName("湖滨新城小区站");
		bicycleStation.setStationAddr("北梦山路湖滨新城小区门口东侧");
		bicycleStation.setLat("26.09060137");
		bicycleStation.setLon("119.28473195000003");
		bicycleStation.setStationNum("15");
		list.add(bicycleStation);

		bicycleStation = new BicycleStation();
		bicycleStation.setStationName("西洪路站");
		bicycleStation.setStationAddr("西洪路816花园门口西侧");
		bicycleStation.setLat("26.082594190000002");
		bicycleStation.setLon("119.27786732499999");
		bicycleStation.setStationNum("20");
		list.add(bicycleStation);

		bicycleStation = new BicycleStation();
		bicycleStation.setStationName("茶园山小学站");
		bicycleStation.setStationAddr("杨桥中路茶园山小学正对面");
		bicycleStation.setLat("26.08021119");
		bicycleStation.setLon("119.27413382500003");
		bicycleStation.setStationNum("30");
		list.add(bicycleStation);

		bicycleStation = new BicycleStation();
		bicycleStation.setStationName("福大东门站");
		bicycleStation.setStationAddr("工业路福大东门北侧");
		bicycleStation.setLat("26.07471519");
		bicycleStation.setLon("119.276221825");
		bicycleStation.setStationNum("20");
		list.add(bicycleStation);

		bicycleStation = new BicycleStation();
		bicycleStation.setStationName("金牛山公园站");
		bicycleStation.setStationAddr("金牛山公园正门西侧");
		bicycleStation.setLat("26.076183182499996");
		bicycleStation.setLon("119.26125685");
		bicycleStation.setStationNum("40");
		list.add(bicycleStation);

		bicycleStation = new BicycleStation();
		bicycleStation.setStationName("洪山桥头站");
		bicycleStation.setStationAddr("洪山桥头公交站旁");
		bicycleStation.setLat("26.077896182499998");
		bicycleStation.setLon("119.25539985");
		bicycleStation.setStationNum("30");
		list.add(bicycleStation);

		bicycleStation = new BicycleStation();
		bicycleStation.setStationName("西河游泳场站");
		bicycleStation.setStationAddr("西河游泳场内");
		bicycleStation.setLat("26.073912182499995");
		bicycleStation.setLon("119.25972984999998");
		bicycleStation.setStationNum("20");
		list.add(bicycleStation);

		bicycleStation = new BicycleStation();
		bicycleStation.setStationName("北江滨融侨锦江站");
		bicycleStation.setStationAddr("北江滨辅道与凤湖路交叉路口");
		bicycleStation.setLat("26.071643192499998");
		bicycleStation.setLon("119.26555300000001");
		bicycleStation.setStationNum("20");
		list.add(bicycleStation);

		bicycleStation = new BicycleStation();
		bicycleStation.setStationName("融侨锦江站");
		bicycleStation.setStationAddr("凤湖路与乌山西路交叉西南侧口");
		bicycleStation.setLat("26.0675941925");
		bicycleStation.setLon("119.272066");
		bicycleStation.setStationNum("20");
		list.add(bicycleStation);

		bicycleStation = new BicycleStation();
		bicycleStation.setStationName("美景良辰站");
		bicycleStation.setStationAddr("乌山西路美景良辰门口西侧");
		bicycleStation.setLat("26.071097785");
		bicycleStation.setLon("119.28310002500001");
		bicycleStation.setStationNum("20");
		list.add(bicycleStation);

		bicycleStation = new BicycleStation();
		bicycleStation.setStationName("省委党校站");
		bicycleStation.setStationAddr("西二环中路省委党校西大门");
		bicycleStation.setLat("26.0751205625");
		bicycleStation.setLon("119.28539820000003");
		bicycleStation.setStationNum("20");
		list.add(bicycleStation);

		bicycleStation = new BicycleStation();
		bicycleStation.setStationName("杨桥永辉站");
		bicycleStation.setStationAddr("杨桥路杨桥永辉正门西侧");
		bicycleStation.setLat("26.0827018125");
		bicycleStation.setLon("119.28413720000003");
		bicycleStation.setStationNum("30");
		list.add(bicycleStation);

		bicycleStation = new BicycleStation();
		bicycleStation.setStationName("光禄坊站");
		bicycleStation.setStationAddr("光禄坊公园正对面");
		bicycleStation.setLat("26.0796842025");
		bicycleStation.setLon("119.29484765000001");
		bicycleStation.setStationNum("15");
		list.add(bicycleStation);

		bicycleStation = new BicycleStation();
		bicycleStation.setStationName("杨桥路双抛桥站");
		bicycleStation.setStationAddr("杨桥路双抛桥斜对面");
		bicycleStation.setLat("26.0859105125");
		bicycleStation.setLon("119.29467090000003");
		bicycleStation.setStationNum("20");
		list.add(bicycleStation);

		bicycleStation = new BicycleStation();
		bicycleStation.setStationName("东街口万霞广场站");
		bicycleStation.setStationAddr("八一七北路万霞广场前");
		bicycleStation.setLat("26.088600262499998");
		bicycleStation.setLon("119.29872239999997");
		bicycleStation.setStationNum("40");
		list.add(bicycleStation);

		bicycleStation = new BicycleStation();
		bicycleStation.setStationName("鼓东路站");
		bicycleStation.setStationAddr("鼓东路与井大路交叉路口西北侧");
		bicycleStation.setLat("26.0897202625");
		bicycleStation.setLon("119.30053240000001");
		bicycleStation.setStationNum("20");
		list.add(bicycleStation);

		bicycleStation = new BicycleStation();
		bicycleStation.setStationName("中山路口站");
		bicycleStation.setStationAddr("省财政厅、省移动大厦旁（中山大厦门口）");
		bicycleStation.setLat("26.0927942625");
		bicycleStation.setLon("119.30111640000001");
		bicycleStation.setStationNum("20");
		list.add(bicycleStation);

		bicycleStation = new BicycleStation();
		bicycleStation.setStationName("五四路中段路");
		bicycleStation.setStationAddr("恒力城旁（外贸中心酒店斜对面）");
		bicycleStation.setLat("26.0931478875");
		bicycleStation.setLon("119.30642539999996");
		bicycleStation.setStationNum("20");
		list.add(bicycleStation);

		bicycleStation = new BicycleStation();
		bicycleStation.setStationName("信和广场站");
		bicycleStation.setStationAddr("五四路信和广场南侧交通银行前");
		bicycleStation.setLat("26.0967745175");
		bicycleStation.setLon("119.30645222499998");
		bicycleStation.setStationNum("20");
		list.add(bicycleStation);

		bicycleStation = new BicycleStation();
		bicycleStation.setStationName("华林四桥站");
		bicycleStation.setStationAddr("华林路台湾饭店对面");
		bicycleStation.setLat("26.1037912675");
		bicycleStation.setLon("119.31152322499997");
		bicycleStation.setStationNum("30");
		list.add(bicycleStation);

		bicycleStation = new BicycleStation();
		bicycleStation.setStationName("中医学院站");
		bicycleStation.setStationAddr("五四北路中医学院门口北侧");
		bicycleStation.setLat("26.10555979");
		bicycleStation.setLon("119.30583652500002");
		bicycleStation.setStationNum("20");
		list.add(bicycleStation);

		bicycleStation = new BicycleStation();
		bicycleStation.setStationName("公益路站");
		bicycleStation.setStationAddr("公益路1号北侧");
		bicycleStation.setLat("26.10851354");
		bicycleStation.setLon("119.30995052499997");
		bicycleStation.setStationNum("30");
		list.add(bicycleStation);

		bicycleStation = new BicycleStation();
		bicycleStation.setStationName("温泉公园站");
		bicycleStation.setStationAddr("温泉公园路温泉公园北门东侧");
		bicycleStation.setLat("26.0998132675");
		bicycleStation.setLon("119.31260122499998");
		bicycleStation.setStationNum("45");
		list.add(bicycleStation);

		bicycleStation = new BicycleStation();
		bicycleStation.setStationName("省图书馆站");
		bicycleStation.setStationAddr("湖东路省图书馆正门东侧");
		bicycleStation.setLat("26.094389517499998");
		bicycleStation.setLon("119.312956225");
		bicycleStation.setStationNum("20");
		list.add(bicycleStation);

		bicycleStation = new BicycleStation();
		bicycleStation.setStationName("温泉支路站");
		bicycleStation.setStationAddr("温泉支路温泉宾馆斜对面");
		bicycleStation.setLat("26.0910669125");
		bicycleStation.setLon("119.31373535");
		bicycleStation.setStationNum("25");
		list.add(bicycleStation);

		bicycleStation = new BicycleStation();
		bicycleStation.setStationName("蒙古营站");
		bicycleStation.setStationAddr("五一北路五金批发公司前");
		bicycleStation.setLat("26.084020579999997");
		bicycleStation.setLon("119.30884359999999");
		bicycleStation.setStationNum("20");
		list.add(bicycleStation);

		bicycleStation = new BicycleStation();
		bicycleStation.setStationName("鼓二小站");
		bicycleStation.setStationAddr("东泰路鼓二小斜对面");
		bicycleStation.setLat("26.0844825125");
		bicycleStation.setLon("119.30272839999998");
		bicycleStation.setStationNum("20");
		list.add(bicycleStation);

		bicycleStation = new BicycleStation();
		bicycleStation.setStationName("鼓楼区政府大院站");
		bicycleStation.setStationAddr("津泰路98号鼓楼区委大院3号楼前");
		bicycleStation.setLat("26.08220108");
		bicycleStation.setLon("119.30389660000003");
		bicycleStation.setStationNum("30");
		list.add(bicycleStation);

		bicycleStation = new BicycleStation();
		bicycleStation.setStationName("津泰路军门社区站（待建）");
		bicycleStation.setStationAddr("南营巷内中段");
		bicycleStation.setLat("26.0813792025");
		bicycleStation.setLon("119.30122165");
		bicycleStation.setStationNum("20");
		list.add(bicycleStation);

		bicycleStation = new BicycleStation();
		bicycleStation.setStationName("乌石山北坡站");
		bicycleStation.setStationAddr("澳门路尽头乌石山北坡公园旁");
		bicycleStation.setLat("26.0782812025");
		bicycleStation.setLon("119.29798265");
		bicycleStation.setStationNum("20");
		list.add(bicycleStation);

		bicycleStation = new BicycleStation();
		bicycleStation.setStationName("福中路西路口站");
		bicycleStation.setStationAddr("福中路西路口（安泰街道）");
		bicycleStation.setLat("26.07220004");
		bicycleStation.setLon("119.30185037500001");
		bicycleStation.setStationNum("30");
		list.add(bicycleStation);

		bicycleStation = new BicycleStation();
		bicycleStation.setStationName("冠亚广场站");
		bicycleStation.setStationAddr("乌山路冠亚广场大洋晶典西侧");
		bicycleStation.setLat("26.0766244525");
		bicycleStation.setLon("119.30171415000001");
		bicycleStation.setStationNum("20");
		list.add(bicycleStation);

		bicycleStation = new BicycleStation();
		bicycleStation.setStationName("五一广场站");
		bicycleStation.setStationAddr("古田路福建省科技馆北侧");
		bicycleStation.setLat("26.07642183");
		bicycleStation.setLon("119.30833385");
		bicycleStation.setStationNum("40");
		list.add(bicycleStation);

		bicycleStation = new BicycleStation();
		bicycleStation.setStationName("省新闻出版局站");
		bicycleStation.setStationAddr("东水路省新闻出版社局门口南侧");
		bicycleStation.setLat("26.0840976075");
		bicycleStation.setLon("119.31502957499998");
		bicycleStation.setStationNum("20");
		list.add(bicycleStation);

		bicycleStation = new BicycleStation();
		bicycleStation.setStationName("古田路站");
		bicycleStation.setStationAddr("古田路光大银行前");
		bicycleStation.setLat("26.076551607499997");
		bicycleStation.setLon("119.31523032500001");
		bicycleStation.setStationNum("20");
		list.add(bicycleStation);

		bicycleStation = new BicycleStation();
		bicycleStation.setStationName("六一环岛站");
		bicycleStation.setStationAddr("古田支路路口劳动大厦旁");
		bicycleStation.setLat("26.076503607499997");
		bicycleStation.setLon("119.31894032499997");
		bicycleStation.setStationNum("20");
		list.add(bicycleStation);

		bicycleStation = new BicycleStation();
		bicycleStation.setStationName("古乐路省六建站");
		bicycleStation.setStationAddr("古乐路省六建后门东侧");
		bicycleStation.setLat("26.072309194999995");
		bicycleStation.setLon("119.31701464999997");
		bicycleStation.setStationNum("20");
		list.add(bicycleStation);

		bicycleStation = new BicycleStation();
		bicycleStation.setStationName("冠茂都会小区站");
		bicycleStation.setStationAddr("六一路冠茂都会小区前");
		bicycleStation.setLat("26.071117194999996");
		bicycleStation.setLon("119.31972765");
		bicycleStation.setStationNum("20");
		list.add(bicycleStation);
		return list;
	}

}
