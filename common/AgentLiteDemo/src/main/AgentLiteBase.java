package main;

import com.huawei.iota.base.BaseService;
import com.huawei.iota.bind.BindService;
import com.huawei.iota.iodev.IotaDeviceInfo;
import com.huawei.iota.iodev.datatrans.DataTransService;
import com.huawei.iota.iodev.hub.HubService;
import com.huawei.iota.login.LoginService;

public class AgentLiteBase {
	
	//private static final Observable Observable = null;
	public static IotaDeviceInfo deviceInfo = null;

	public static void main1 (String[] args) {
		System.out.println(" =============   demo  begin ============== ");


		boolean res = false;
		//初始化AgentLite资源
		res = BaseService.init("./workdir", null);
		if (res == false) {
			System.out.println(" ============ init failed ============");
		}		
		
		//网关绑定
		AgentLiteBind agentLiteBind = AgentLiteBind.getInstance();
		BindService bindService = BindService.getInstance();
		bindService.registerObserver(agentLiteBind);
		agentLiteBind.bindAction();
		
		try {
			Thread.sleep(2000);
		} catch(InterruptedException e) {
			e.printStackTrace();
		}
		
		//网关登陆
		AgentLiteLogin agentLiteLogin = AgentLiteLogin.getInstance();
		LoginService loginService = LoginService.getInstance();
		loginService.registerObserver(agentLiteLogin);
		agentLiteLogin.loginAction();
		
		try {
			Thread.sleep(2000);
		} catch(InterruptedException e) {
			e.printStackTrace();
		}
		
		//sensor添加
		AgentLiteHub agentLiteHub = AgentLiteHub.getInstance();
		HubService hubService = HubService.getInstance();
		hubService.registerObserver(agentLiteHub);
		agentLiteHub.addSensor();
		
		try {
			Thread.sleep(2000);
		} catch(InterruptedException e) {
			e.printStackTrace();
		}
		
		//sensor数据上报
		AgentLiteDataTrans agentLiteDataTrans = AgentLiteDataTrans.getInstance();
		DataTransService dataTransService = DataTransService.getInstance();
		dataTransService.registerObserver(agentLiteDataTrans);
		agentLiteDataTrans.subdevDataReport();		
		
		try {
			Thread.sleep(3000);
		} catch(InterruptedException e) {
			e.printStackTrace();
		}
		
		//sensor状态更新
		agentLiteHub.updataDeviceStatus();
		
		try {
			Thread.sleep(3000);
		} catch(InterruptedException e) {
			e.printStackTrace();
		}
		//sensor删除
		agentLiteHub.rmvSensor();
		
		try {
			Thread.sleep(3000);
		} catch(InterruptedException e) {
			e.printStackTrace();
		}
		//网关登出
		agentLiteLogin.onDestroy();
		
		
		/*
		//释放AgentLite资源
		BaseService.destroy();
		*/
	}

	public static void main (String[] args) {
		System.out.println(" =============   demo  begin ============== ");
		
		boolean res = false;
		//初始化AgentLite资源
		res = BaseService.init("./workdir", null);
		if (res == false) {
			System.out.println("============ init failed ============");
		}		
		
		//网关绑定
		AgentLiteBind agentLiteBind = AgentLiteBind.getInstance();
		BindService bindService = BindService.getInstance();
		bindService.registerObserver(agentLiteBind);
		agentLiteBind.bindAction();
		
		try {
			Thread.sleep(2000);
		} catch(InterruptedException e) {
			e.printStackTrace();
		}
		

	}		
}
