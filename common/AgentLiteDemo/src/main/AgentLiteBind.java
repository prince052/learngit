package main;

import com.huawei.iota.base.BaseService;
import com.huawei.iota.bind.BindConfig;
import com.huawei.iota.bind.BindService;
import com.huawei.iota.iodev.IotaDeviceInfo;
import com.huawei.iota.login.LoginService;
import com.huawei.iota.util.IotaMessage;
import com.huawei.iota.util.MyObserver;

import bean.GatewayInfo;

public class AgentLiteBind implements MyObserver {
	

//	设备注册成功, 请保存好您的"设备标识码"与"PSK", 通过"设备标识码"与"PSK"进行南向注册, 获取平台"动态密钥"。
//
//	设备ID：
//	78b8647c-b1f3-48fa-87a3-a220384d8fa3
//	设备标识码：
//	huawei_gateway1
//	PSK：
//	23afb1e7b9f1dad57b3bce2a9b5e2919
	
	public static IotaDeviceInfo deviceInfo = null;
	
	private static AgentLiteBind instance = new AgentLiteBind();
	
    public static AgentLiteBind getInstance() {
        return instance;
    }
	
//    public void addObserver(Observable o) {
//    	o.addObserver(this);
//    }
//	
//	public void update(Observable o, Object arg) {
//		System.out.println("BindManager收到绑定通知:" + ((BindService)o).getIotaMessage());
//		IotaMessage iotaMsg = ((BindService)o).getIotaMessage();
//		int status = iotaMsg.getUint(BindService.BIND_IE_RESULT, -1);
//		System.out.println("status is :" + status);
//		switch (status) {
//			case 0:
//                saveBindParaAndGotoLogin(iotaMsg);
//                break;
//            default:
//            	System.out.println(" =============  绑定失败   ============== ");
//                bindAction();
//                break;
//		}
//	}
	
	public void bindAction() {
		System.out.println(" =============   start  bind ============== ");
		String nodeId = "huawei_gateway1";
		String manufactrueId = "Huawei";
		String deviceType = "Gateway";
		String model = "AgentLite01";
		String protocolType = "HuaweiM2M";
		deviceInfo = new IotaDeviceInfo(nodeId, manufactrueId, deviceType, model, protocolType);
		
		//绑定配置
		configBindPara();
		
		//发起绑定请求
		BindService.bind(nodeId, deviceInfo);
	}
	
	
	//绑定配置
	private static void configBindPara() {
		boolean res = false;
//		String server_ip = "117.78.47.187";
//		String server_ip = "";
		res = BindConfig.setConfig(BindConfig.BIND_CONFIG_ADDR, GatewayInfo.getLvsAddress());
		res = BindConfig.setConfig(BindConfig.BIND_CONFIG_PORT, "8943");
		BaseService.setConfig(8,18,"0");
		if (false == res) {
			System.out.println(" ============ set BindConfig failed ============");
		}
		System.out.println("startBind platformIP =" + GatewayInfo.getLvsAddress() + ":" + "8943");		
	}
	
    //保存绑定响应消息携带的参数
    private void saveBindParaAndGotoLogin(IotaMessage iotaMsg) {
        System.out.println(" ============ saveBindParaAndGotoLogin ============");
        String appId = iotaMsg.getString(BindService.BIND_IE_APPID);
        String deviceId = iotaMsg.getString(BindService.BIND_IE_DEVICEID);
        String secret = iotaMsg.getString(BindService.BIND_IE_DEVICESECRET);
//        String haAddress = iotaMsg.getString(BindService.BIND_IE_HA_ADDR);
//        String lvsAddress = iotaMsg.getString(BindService.BIND_IE_LVS_ADDR);
        String haAddress = null, lvsAddress = null;
        
        saveGatewayInfo(appId, deviceId, secret, haAddress, lvsAddress);
        //saveSharedPreferences(appId, deviceId, secret, haAddress, lvsAddress);
        //addSensorForTest();
        
		//网关设备登陆
//		AgentLiteLogin agentLiteLogin = AgentLiteLogin.getInstance();
//		agentLiteLogin.registerObserver(LoginService.getInstance());
//		agentLiteLogin.loginAction();
		
		AgentLiteLogin agentLiteLogin = AgentLiteLogin.getInstance();
		LoginService loginService = LoginService.getInstance();
		loginService.registerObserver(agentLiteLogin);
		agentLiteLogin.loginAction();
		
    }
    
    private void saveGatewayInfo(String appId, String deviceId, String secret, String haAddress, String lvsAddress) {
        GatewayInfo.setAppID(appId);
        GatewayInfo.setDeviceID(deviceId);
        GatewayInfo.setSecret(secret);
        GatewayInfo.setHaAddress(haAddress);
        if(lvsAddress!=null) {
            GatewayInfo.setLvsAddress(lvsAddress);
   	
        }
    }

	@Override
	public void update(IotaMessage arg0) {
		// TODO Auto-generated method stub
		System.out.println("BindManager收到绑定通知:" + arg0);
		int status = arg0.getUint(BindService.BIND_IE_RESULT, -1);
		System.out.println("status is :" + status);
		switch (status) {
			case 0:
                saveBindParaAndGotoLogin(arg0);
                break;
            default:
            	System.out.println(" =============  绑定失败   ============== ");
                bindAction();
                break;
		}
	}
    

}
