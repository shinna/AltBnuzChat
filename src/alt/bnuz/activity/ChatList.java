package alt.bnuz.activity;

import java.util.ArrayList;
import java.util.Collection;

import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.packet.Presence;

import alt.bnuz.activity.Login.LoginTask;
import alt.bnuz.application.XMPPAction;
import alt.bnuz.chat.R;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

@SuppressWarnings("unused")
public class ChatList extends Activity {
    /* �ǽ��棬��һ�������������� */
    private TextView TV_UserInfoShow;
    private Intent loginIntent;
    private Bundle bundle;
    private XMPPAction App;
    /* ����smack����ϵ����ط��� */
    private String userName;
    private String JID;// ���JID�����Ƿ��������ж��û�������

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_chatlist);
	/* ʵ�������� */
	App = (XMPPAction) getApplication();
	TV_UserInfoShow = (TextView) findViewById(R.id.TV_UserInfoShow);
	/* ��ȡ�ո�Intent���ݹ������û��� */
	loginIntent = this.getIntent();
	bundle = loginIntent.getExtras();
	/* ��ȡ��ϵ���б� */
	UserInfoGetter ug = new UserInfoGetter();
	ug.execute();
    }

    /* ������Ͻǵ�ע���˵� */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
	menu.add(Menu.NONE, 1, 0, "ע��");
	return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
	switch (item.getItemId()) {
	/* case����Ϊ���� */
	case 1: {
	    // ��ע�� �ٻص���½����
	    LogoutTask logoutTask = new LogoutTask();
	    logoutTask.execute();
	    Intent backToLogin = new Intent(ChatList.this, Login.class);
	    startActivity(backToLogin);
	    break;
	}
	}
	return super.onOptionsItemSelected(item);

    }

    public class UserInfoGetter extends AsyncTask<Void, Void, String> {

	@Override
	protected String doInBackground(Void... p) {
	    String res = "";
	    ArrayList<Pair<String, String>> fList = getFriendList();
	    int fCount = fList.size();
	    res = "��ϵ������:" + fCount;
	    for (Pair<String, String> f : fList) {
		res += "\n" + f.first;
	    }
	    return res;
	}

	@Override
	protected void onPostExecute(String res) {
	    TV_UserInfoShow.setText(res);
	}
    }

    /* ��ȡ��ϵ��״̬ ,������һ��Pair���飬��ֵ�Է�ʽ�� �û���|״̬ */
    /* PS����Ҳ��֪�����״̬������ǩ�����������ֿ����Զ��������/����/�뿪 ��״̬ */
    public ArrayList<Pair<String, String>> getFriendList() {
	Roster roster = App.xmppConn.getRoster();
	ArrayList<Pair<String, String>> userStatus = new ArrayList<Pair<String, String>>();
	// ��ȡ��ϵ���б�ļ���
	Collection<RosterEntry> rg = roster.getEntries();
	// ������ϵ���б�
	for (RosterEntry rosterEntry : rg) {
	    // ��ȡ����״̬����
	    Presence presence = roster.getPresence(rosterEntry.getUser());
	    String friendUserName = rosterEntry.getName();// �û���
	    String friendUserStatus = presence.getStatus();// ״̬
	    Pair<String, String> friendInfo = new Pair<String, String>(
		    friendUserName, friendUserStatus);
	    userStatus.add(friendInfo);
	}

	return userStatus;
    }

    /* Logout�첽�߳� */
    public class LogoutTask extends AsyncTask<Void, Void, String> {

	@Override
	protected String doInBackground(Void... p) {
	    App.logout();
	    String res = "Logout";
	    return res;
	}

	@Override
	protected void onPostExecute(String res) {
	    Toast.makeText(ChatList.this, "�Ѿ�ע�������ص�½����", Toast.LENGTH_LONG).show();
	}
    }
}
