package alt.bnuz.activity;

import alt.bnuz.application.XMPPAction;
import alt.bnuz.chat.R;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Reg extends Activity{
    private EditText ET_RegUserName,ET_RegPassWord,ET_RegEmail;
    private Button BT_Reg_do;
    private XMPPAction App;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_reg);
	ET_RegUserName=(EditText)findViewById(R.id.ET_RegUserName);
	ET_RegPassWord=(EditText)findViewById(R.id.ET_RegPassWord);
	ET_RegEmail=(EditText)findViewById(R.id.ET_RegEmail);
	BT_Reg_do=(Button)findViewById(R.id.BT_Reg_do);
	App=(XMPPAction)getApplication();

	/*添加注册方法*/
	BT_Reg_do.setOnClickListener(new OnClickListener(){
	    @Override
	    public void onClick(View v) {
		if(regCheck()==true){
		    RegTask regTask=new RegTask();
		    regTask.execute();
		}
	    }
	});
    }
    
    
    /*输入检查方法*/
    public boolean regCheck(){
	boolean res=true;
	if(ET_RegUserName.getText().toString().equals("")||ET_RegPassWord.getText().toString().equals("")||ET_RegEmail.getText().toString().equals("")){
	    res=false;
	    Toast.makeText(Reg.this, "还有空格没填?", Toast.LENGTH_LONG).show();
	}
	return res;
    }
    
    /*实现注册的异步线程*/
    public class RegTask extends AsyncTask<Void,Void,Integer>{

	@Override
	protected Integer doInBackground(Void... p) {
	    String RUserName=ET_RegUserName.getText().toString();
	    String RPassWord=ET_RegPassWord.getText().toString();
	    String REmail=ET_RegEmail.getText().toString();
	    Integer regRes=App.regist(RUserName, RPassWord, REmail);
	    return regRes;
	}

	@Override
	protected void onPostExecute(Integer regRes){
	    if(regRes==1){
		/*如果注册成功*/
		Toast.makeText(Reg.this, "注册成功，返回登陆界面", Toast.LENGTH_LONG).show();
		/*跳转回来登陆界面*/
		Intent toLogin=new Intent(Reg.this,Login.class);
		startActivity(toLogin);
	    }
	    else{
		/*注册失败*/
		Toast.makeText(Reg.this, "未知原因 注册失败", Toast.LENGTH_LONG).show();
	    }
	}
    }
}