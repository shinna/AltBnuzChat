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
	/* �ڴ˳�ʼ��XMPPConnection */
	InitNetWorkThread inwt=new InitNetWorkThread();
	inwt.execute();
	}

    /*ִ����������첽�߳�*/
    public class InitNetWorkThread extends AsyncTask<Void,Void,Void>{

	@Override
	protected Void doInBackground(Void... params) {
	    init();
	    return null;
	}
	
    }
    
    /* XMPPConnection�ȵ�Ĭ�ϳ�ʼ������ */
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

    /*��ꑷ���*/
    public int login(String UserName, String PassWord) {
	int status = -1;
	try {
	    XMPPConn.login(UserName, PassWord);
	    status = 1;// �����½�ɹ����޸�Ϊ1
	} catch (Exception e) {
	    Log.d("RegTAG", e.toString());
	}
	return status;
    }

    /* ע�᷽����Ŀǰ���Email��û�õ� */
    public int regist(String UserName, String PassWord, String Email) {
	int status = -1;
	try {
	    accountMngr = XMPPConn.getAccountManager();
	    accountMngr.createAccount(UserName, PassWord);
	    status = 1;// ���ע��ɹ����޸�Ϊ1

	} catch (Exception e) {
	    Log.d("RegTAG", e.toString());
	}
	return status;
    }

}
