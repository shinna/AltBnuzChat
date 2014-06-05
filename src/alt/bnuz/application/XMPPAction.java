package alt.bnuz.application;

import org.jivesoftware.smack.AccountManager;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

public class XMPPAction extends Application{
    /*��������Android Application��ȫ���ҵ�������*/
    
    //������������ַ�Ͷ˿�
    public String serverDomain="aquariuslt.com";
    public int serverPort=5222;
    
    //���ӷ������Ĳ��� opConnConf:OpenfireConnectionConfiguration
    public ConnectionConfiguration opConnConf;
    //XMPP���� xmppConn:XMPPConnection
    public XMPPConnection xmppConn;
    //XMPP�˺Ź���
    public AccountManager acountManager;
    
    //����˿ڳ�ʼ��+���ӷ������ķ���  Ϊ�˷��㣬�������ֱ�����첽�߳����XMPPAction �µ������첽�̺߳�׺ ��ΪAction
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
    
    //�����û���¼�ķ���
    public int login(String userName,String passWord){
	int status = -1;
	try {
	    xmppConn.login(userName, passWord);
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
	    acountManager = xmppConn.getAccountManager();
	    acountManager.createAccount(UserName, PassWord);
	    status = 1;// ���ע��ɹ����޸�Ϊ1

	} catch (Exception e) {
	    Log.d("RegTAG", e.toString());
	}
	return status;
    }
    
    /* ע������*/
    public void logout(){
	try{
	    xmppConn.disconnect();
	}catch(Exception e){
	    Log.d("LogoutTAG",e.toString());
	}
    }
    
    
    /*========������Application�����д����========*/
    @Override
    public void onCreate() {
	//��ʼ������
        InitAction initAction=new InitAction();
        initAction.execute();
        Log.d("AppInit","�Ѿ���ʼ������");
    }

    
}
