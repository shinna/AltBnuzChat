package alt.bnuz.application;

import org.jivesoftware.smack.AccountManager;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

public class XMPPAction extends Application {

    private String domain = "aquariuslt.com";
    private int port = 5222;
    private ConnectionConfiguration connConfig;
    private XMPPConnection XMPPConn;
    private AccountManager accountMngr;

    public ConnectionConfiguration getConnConfig() {
        return connConfig;
    }

    public void setConnConfig(ConnectionConfiguration connConfig) {
        this.connConfig = connConfig;
    }

    public XMPPConnection getXMPPConn() {
        return XMPPConn;
    }

    public void setXMPPConn(XMPPConnection xMPPConn) {
        XMPPConn = xMPPConn;
    }

    public AccountManager getAccountMngr() {
        return accountMngr;
    }

    public void setAccountMngr(AccountManager accountMngr) {
        this.accountMngr = accountMngr;
    }

    public void onCreate() {
	super.onCreate();
	/* 在此初始化XMPPConnection */
	InitNetWorkThread inwt=new InitNetWorkThread();
	inwt.execute();
	}

    /*执行网络操作异步线程*/
    public class InitNetWorkThread extends AsyncTask<Void,Void,Void>{

	@Override
	protected Void doInBackground(Void... params) {
	    init();
	    return null;
	}
	
    }
    
    /* XMPPConnection等的默认初始化方法 */
    public void init() {
	connConfig = new ConnectionConfiguration(domain, port);
	connConfig.setSASLAuthenticationEnabled(false);
	XMPPConn = new XMPPConnection(connConfig);
	try {
	    XMPPConn.connect();
	} catch (XMPPException e) {
	    Log.d("XMPPConnectionDebug",e.toString());
	}
    }

    /*登陸方法*/
    public int login(String UserName, String PassWord) {
	int status = -1;
	try {
	    XMPPConn.login(UserName, PassWord);
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
	    accountMngr = XMPPConn.getAccountManager();
	    accountMngr.createAccount(UserName, PassWord);
	    status = 1;// 如果注册成功，修改为1

	} catch (Exception e) {
	    Log.d("RegTAG", e.toString());
	}
	return status;
    }

}
