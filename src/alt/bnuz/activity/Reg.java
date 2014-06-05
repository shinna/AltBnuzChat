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

	/*���ע�᷽��*/
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
    
    
    /*�����鷽��*/
    public boolean regCheck(){
	boolean res=true;
	if(ET_RegUserName.getText().toString().equals("")||ET_RegPassWord.getText().toString().equals("")||ET_RegEmail.getText().toString().equals("")){
	    res=false;
	    Toast.makeText(Reg.this, "���пո�û��?", Toast.LENGTH_LONG).show();
	}
	return res;
    }
    
    /*ʵ��ע����첽�߳�*/
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
		/*���ע��ɹ�*/
		Toast.makeText(Reg.this, "ע��ɹ������ص�½����", Toast.LENGTH_LONG).show();
		/*��ת������½����*/
		Intent toLogin=new Intent(Reg.this,Login.class);
		startActivity(toLogin);
	    }
	    else{
		/*ע��ʧ��*/
		Toast.makeText(Reg.this, "δ֪ԭ�� ע��ʧ��", Toast.LENGTH_LONG).show();
	    }
	}
    }
}