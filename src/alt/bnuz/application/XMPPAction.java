package alt.bnuz.application;

import org.jivesoftware.smack.AccountManager;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

public class XMPPAction extends Application{
    /*声明基于Android Application的全局且单例变量*/
    
    //声明服务器地址和端口
    public String serverDomain="aquariuslt.com";
    public int serverPort=5222;
    
    //连接服务器的参数 opConnConf:OpenfireConnectionConfiguration
    public ConnectionConfiguration opConnConf;
    //XMPP连接 xmppConn:XMPPConnection
    public XMPPConnection xmppConn;
    //XMPP账号管理
    public AccountManager acountManager;
    
    //负责端口初始化+连接服务器的方法  为了方便，网络操作直接用异步线程类搞XMPPAction 下的所有异步线程后缀 都为Action
    public class InitAction extends AsyncTask<Void,Integer,Void>{
	
	@Override
	protected Void doInBackground(Void... p) {
	    opConnConf=new ConnectionConfiguration(serverDomain,serverPort);
	    xmppConn=new XMPPConnection(opConnConf);
	    try {
		xmppConn.connect();
	    } catch (XMPPException e) {
		Log.d("AppInit",e.toString());
	    }
	    return null;
	}
    }
    
    //负责用户登录的方法
    public int login(String userName,String passWord){
	int status = -1;
	try {
	    xmppConn.login(userName, passWord);
	    status = 1;// 如果登陆成功，修改为1
	} catch (Exception e) {
	    Log.d("RegTAG", e.toString());
	}
	return status;
    }
    
    /* 注册方法，目前这个Email是没用的 */
    public int regist(String UserName, String PassWord, String Email) {
	int status = -1;
	try {
	    acountManager = xmppConn.getAccountManager();
	    acountManager.createAccount(UserName, PassWord);
	    status = 1;// 如果注册成功，修改为1

	} catch (Exception e) {
	    Log.d("RegTAG", e.toString());
	}
	return status;
    }
    
    /* 注销方法*/
    public void logout(){
	try{
	    xmppConn.disconnect();
	}catch(Exception e){
	    Log.d("LogoutTAG",e.toString());
	}
    }
    
    
    /*========下面是Application类的重写方法========*/
    @Override
    public void onCreate() {
	//初始化连接
        InitAction initAction=new InitAction();
        initAction.execute();
        Log.d("AppInit","已经初始化连接");
    }

    
}
