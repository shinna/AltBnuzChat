package alt.bnuz.activity;


import alt.bnuz.application.XMPPAction;
import alt.bnuz.chat.R;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends Activity {

    private EditText ET_UserName,ET_PassWord;
    private Button BT_Login,BT_Reg;
    private XMPPAction App;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_login);
	/*ʵ��������*/
	ET_UserName=(EditText)findViewById(R.id.ET_UserName);
	ET_PassWord=(EditText)findViewById(R.id.ET_PassWord);
	BT_Login=(Button)findViewById(R.id.BT_Login);
	BT_Reg=(Button)findViewById(R.id.BT_Reg);
	App=(XMPPAction)getApplication();
	/*��ӵ�½��ť��Listener*/
	BT_Login.setOnClickListener(new OnClickListener(){
	    @Override
	    public void onClick(View v) {
		if(loginCheck()==true){
		    LoginTask loginTask=new LoginTask();
		    loginTask.execute();
		}
	    }
	});
	
	/*���ע�ᰴť��Listener*/
	BT_Reg.setOnClickListener(new OnClickListener(){
	    @Override  
	    public void onClick(View v) {
		Intent intent =new Intent(Login.this,Reg.class );
		startActivity(intent);
	    }
	});
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
	getMenuInflater().inflate(R.menu.login, menu);
	return true;
    }
    
    
    /*����Ƿ��пյ�������*/
    public boolean loginCheck(){
	boolean res=true;
	if(ET_UserName.getText().toString().equals("")&&ET_PassWord.getText().toString().equals("")){
	    res=false;
	    Toast.makeText(Login.this, "�û�����������û����!", Toast.LENGTH_SHORT).show();
	}
	return res;
    }
    
    /*ʵ�ֵ�½�����첽�߳�*/
    public class LoginTask extends AsyncTask<Void,Void,Integer>{

	@Override
	protected Integer doInBackground(Void... p) {
	    /*ʵ�ֵ�½����*/
	    String userName=ET_UserName.getText().toString();
	    String passWord=ET_PassWord.getText().toString();
	    int loginRes=App.login(userName, passWord);
	    return loginRes;
	}
	
	
	@Override
	protected void onPostExecute(Integer loginRes) {
	    if(loginRes==1){
		/*�����½�ɹ�����תActivity*/
		Toast.makeText(Login.this, "��½�ɹ�!~", Toast.LENGTH_LONG).show();
	    
	    }
	    else{
		/*��½ʧ�ܣ�������ʾ��*/
		Toast.makeText(Login.this, "��½ʧ�ܣ������û���������", Toast.LENGTH_LONG).show();
	    }
	}
    }
}	
