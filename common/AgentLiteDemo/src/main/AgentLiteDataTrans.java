package main;

import java.util.Random;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.huawei.iota.iodev.IodevService;
import com.huawei.iota.iodev.datatrans.DataTransService;
import com.huawei.iota.util.IotaMessage;
import com.huawei.iota.util.MyObserver;

import bean.GatewayInfo;

public class AgentLiteDataTrans implements MyObserver {
	
	private static AgentLiteDataTrans instance = new AgentLiteDataTrans();
	
    public static AgentLiteDataTrans getInstance() {
        return instance;
    }
	
//    public void addObserver(Observable o) {
//    	o.addObserver(this);
//    }
    
	
//	public void update(Observable o, Object arg) {
//		System.out.println("AgentLiteDataTrans收到通知:" + ((DataTransService)o).getIotaMessage());
//		IotaMessage iotaMsg = ((DataTransService)o).getIotaMessage();
//		int mMsgType = iotaMsg.getMsgType();
//		switch(mMsgType) {
//		//数据上报应答
//		case IodevService.IODEV_MSG_DATA_REPORT_RSP:
//			getDataReportAnswer(iotaMsg);
//			break;
//		//被动接收命令
//		case IodevService.IODEV_MSG_RECEIVE_CMD:
//			getCmdReceive(iotaMsg);
//			break;
//		//MQTT消息推送
//		case IodevService.IODEV_MSG_MQTT_PUB_RSP:
//			//logoutResultAction(iotaMsg);
//			break;			
//		default:
//			break;
//	}
//}

	
	
	public void dataReportAction(String properties) {
		System.out.println(" =============   start  WGdataReport ============== ");
        int cookie;
        String deviceId = GatewayInfo.getDeviceID();

        Random random = new Random();
        cookie = random.nextInt(65535);
        System.out.println(" cookie:" + cookie + "deviceId :" + deviceId);
        DataTransService.dataReport(cookie, null, deviceId, null, properties);
	}
	
    public void subdevDataReport() {
        System.out.println(" =============   subdevDataReport! ============== ");
        int cookie;
        Random random = new Random();
        cookie = random.nextInt(65535);

        String deviceId = GatewayInfo.getSensorId();

        Gson iotGson = new Gson();
        JsonObject data = new JsonObject();
        data.addProperty("batteryLevel", "3");
        DataTransService.dataReport(cookie, null, deviceId, null, iotGson.toJson(data));
    }
	
    private void getDataReportAnswer(IotaMessage iotaMsg) {
		String deviceId = iotaMsg.getString(DataTransService.DATATRANS_IE_DEVICEID);
		int retcode = iotaMsg.getUint(DataTransService.DATATRANS_IE_RESULT, 0);
		System.out.println("deviceId:" + deviceId + "data report, ret = " + retcode);
        if (deviceId.equals(GatewayInfo.getDeviceID())) {
        	System.out.println("report gateway data success, cookie = " + iotaMsg.getUint(DataTransService.DATATRANS_IE_COOKIE, 0));
        }
        if (deviceId.equals(GatewayInfo.getSensorId())) {
        	System.out.println(" report sensor data success ");
        }
    } 
    
    private void getCmdReceive(IotaMessage iotaMsg) {
    	System.out.println("=========receive iotCMD ============");
        String deviceId = iotaMsg.getString(DataTransService.DATATRANS_IE_DEVICEID);
        String requestId = iotaMsg.getString(DataTransService.DATATRANS_IE_REQUSTID);
        String serviceId = iotaMsg.getString(DataTransService.DATATRANS_IE_SERVICEID);
        String method = iotaMsg.getString(DataTransService.DATATRANS_IE_METHOD);
        String cmd = iotaMsg.getString(DataTransService.DATATRANS_IE_CMDCONTENT);
        if (method.equals("REMOVE_GATEWAY")) {
            //rmvGateway(context);
        }

        System.out.println ("Receive cmd :"
                + "\ndeviceId  = " + deviceId
                + "\nrequestId = " + requestId
                + "\nserviceId = " + serviceId
                + "\nmethod    = " + method
                + "\ncmd       = " + cmd);
    }
    
	@Override
	public void update(IotaMessage arg0) {
		// TODO Auto-generated method stub
		System.out.println("AgentLiteDataTrans收到通知:" + arg0);
		int mMsgType = arg0.getMsgType();
		switch(mMsgType) {
		//数据上报应答
		case IodevService.IODEV_MSG_DATA_REPORT_RSP:
			getDataReportAnswer(arg0);
			break;
		//被动接收命令
		case IodevService.IODEV_MSG_RECEIVE_CMD:
			getCmdReceive(arg0);
			break;
		//MQTT消息推送
		case IodevService.IODEV_MSG_MQTT_PUB_RSP:
			//logoutResultAction(iotaMsg);
			break;			
		default:
			break;
		}
	}
}
