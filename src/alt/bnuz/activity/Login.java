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
	/*实例化对象*/
	ET_UserName=(EditText)findViewById(R.id.ET_UserName);
	ET_PassWord=(EditText)findViewById(R.id.ET_PassWord);
	BT_Login=(Button)findViewById(R.id.BT_Login);
	BT_Reg=(Button)findViewById(R.id.BT_Reg);
	App=(XMPPAction)getApplication();
	/*添加登陆按钮的Listener*/
	BT_Login.setOnClickListener(new OnClickListener(){
	    @Override
	    public void onClick(View v) {
		if(loginCheck()==true){
		    LoginTask loginTask=new LoginTask();
		    loginTask.execute();
		}
	    }
	});
	
	/*添加注册按钮的Listener*/
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
    
    
    /*检查是否有空的输入栏*/
    public boolean loginCheck(){
	boolean res=true;
	if(ET_UserName.getText().toString().equals("")&&ET_PassWord.getText().toString().equals("")){
	    res=false;
	    Toast.makeText(Login.this, "用户名或者密码没输入!", Toast.LENGTH_SHORT).show();
	}
	return res;
    }
    
    /*实现登陆检测的异步线程*/
    public class LoginTask extends AsyncTask<Void,Void,Integer>{

	@Override
	protected Integer doInBackground(Void... p) {
	    /*实现登陆操作*/
	    String userName=ET_UserName.getText().toString();
	    String passWord=ET_PassWord.getText().toString();
	    int loginRes=App.login(userName, passWord);
	    return loginRes;
	}
	
	
	@Override
	protected void onPostExecute(Integer loginRes) {
	    if(loginRes==1){
		/*如果登陆成功，跳转Activity*/
		Toast.makeText(Login.this, "登陆成功!~", Toast.LENGTH_LONG).show();
	    
	    }
	    else{
		/*登陆失败，弹出提示框*/
		Toast.makeText(Login.this, "登陆失败，请检查用户名或密码", Toast.LENGTH_LONG).show();
	    }
	}
    }
}	
