package main;

import java.util.Observable;
import java.util.Observer;
import java.util.Random;

import com.huawei.iota.iodev.IotaDeviceInfo;
import com.huawei.iota.iodev.datatrans.DataTransService;
import com.huawei.iota.iodev.hub.HubService;
import com.huawei.iota.login.LoginConfig;
import com.huawei.iota.login.LoginService;
import com.huawei.iota.util.IotaMessage;
import com.huawei.iota.util.MyObserver;

import bean.GatewayInfo;

public class AgentLiteLogin implements MyObserver {
	
	private static IotaDeviceInfo deviceInfo = null;
	private static AgentLiteLogin instance = new AgentLiteLogin();
	
    public static AgentLiteLogin getInstance() {
        return instance;
    }
	
//    public void addObserver(Observable o) {
//    	o.addObserver(this);
//    }
//	
//	public void update(Observable o, Object arg) {
//		System.out.println("LoginManager收到通知:" + ((LoginService)o).getIotaMessage());
//		IotaMessage iotaMsg = ((LoginService)o).getIotaMessage();
//		int mMsgType = iotaMsg.getMsgType();
//		switch(mMsgType) {
//			case 1:
//				loginResultAction(iotaMsg);
//				break;
//			case 2:
//				logoutResultAction(iotaMsg);
//				break;
//			default:
//				break;
//		}
//	}
	
	public void loginAction() {
		System.out.println(" =============   start  login ============== ");
        configLoginPara();
        LoginService.login();		
	}
	
	public void onDestroy() {
		System.out.println(" =============   start  onDestroy ============== ");
		LoginService.logout();
	}
	
	private static void configLoginPara() {
        LoginConfig.setConfig(LoginConfig.LOGIN_CONFIG_DEVICEID,  GatewayInfo.getDeviceID());
        LoginConfig.setConfig(LoginConfig.LOGIN_CONFIG_APPID, GatewayInfo.getAppID());
        LoginConfig.setConfig(LoginConfig.LOGIN_CONFIG_SECRET, GatewayInfo.getSecret());
        LoginConfig.setConfig(LoginConfig.LOGIN_CONFIG_IOCM_ADDR, GatewayInfo.getLvsAddress());
        //LoginConfig.setConfig(LoginConfig.LOGIN_CONFIG_IOCM_ADDR, GatewayInfo.getHaAddress());
        LoginConfig.setConfig(LoginConfig.LOGIN_CONFIG_IOCM_PORT, "8943");
        //LoginConfig.setConfig(LoginConfig.LOGIN_CONFIG_MQTT_ADDR, GatewayInfo.getHaAddress());
        LoginConfig.setConfig(LoginConfig.LOGIN_CONFIG_MQTT_ADDR, GatewayInfo.getLvsAddress());
        LoginConfig.setConfig(LoginConfig.LOGIN_CONFIG_MQTT_PORT, "8883");	
	}
	
	// 收到登陆通知后执行
	private void loginResultAction(IotaMessage iotaMsg) {
		System.out.println(" ============= login Success ============== ");	
		
		//添加sonser
//		AgentLiteHub agentLiteHub = AgentLiteHub.getInstance();
//		agentLiteHub.addObserver(HubService.getInstance());
//		System.out.println(" ========= Observer is ========" + agentLiteHub);
//		System.out.println(" ========= HubService is ========" + HubService.getInstance());
//		agentLiteHub.addSensor();
		
		//网关数据上报
//		AgentLiteDataTrans agentLiteDataTrans = AgentLiteDataTrans.getInstance();
//		agentLiteDataTrans.addObserver(DataTransService.getInstance());
//		agentLiteDataTrans.dataReportAction("gateway data report test.");
	}
	
	
	// 收到登出通知后执行
	private void logoutResultAction(IotaMessage iotaMsg) {
		   int reason = iotaMsg.getUint(LoginService.LOGIN_IE_REASON, -1);
           System.out.println("reason is : " + reason);
           switch (reason) {
               case LoginService.LOGIN_REASON_DEVICE_NOEXIST:
               case LoginService.LOGIN_REASON_DEVICE_REMOVED:
                   gotoBind();
                   break;
               default:
                   break;
           }
	}
	
//    private void gatewayDataReport(String properties) {
//        System.out.println(" ============= gatewayDataReport! ============== ");
//        int cookie;
//        String deviceId = GatewayInfo.getDeviceID();
//
//        Random random = new Random();
//        cookie = random.nextInt(65535);
//        System.out.println("cookie = " + cookie);
//        DataTransService.dataReport(cookie, null, deviceId, null, properties);
//    }
	
	
	private void gotoBind() {
		
	}

	@Override
	public void update(IotaMessage arg0) {
		// TODO Auto-generated method stub
		System.out.println("LoginManager收到通知:" + arg0);
		int mMsgType = arg0.getMsgType();
		switch(mMsgType) {
			case 1:
				loginResultAction(arg0);
				break;
			case 2:
				logoutResultAction(arg0);
				break;
			default:
				break;
		}
	}
}
