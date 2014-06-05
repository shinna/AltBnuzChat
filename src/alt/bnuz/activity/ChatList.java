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
    /* 非界面，做一个数据连接试试 */
    private TextView TV_UserInfoShow;
    private Intent loginIntent;
    private Bundle bundle;
    private XMPPAction App;
    /* 基于smack的联系人相关方法 */
    private String userName;
    private String JID;// 这个JID可能是服务器端判断用户的主键

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_chatlist);
	/* 实例化对象 */
	App = (XMPPAction) getApplication();
	TV_UserInfoShow = (TextView) findViewById(R.id.TV_UserInfoShow);
	/* 获取刚刚Intent传递过来的用户名 */
	loginIntent = this.getIntent();
	bundle = loginIntent.getExtras();
	/* 获取联系人列表 */
	UserInfoGetter ug = new UserInfoGetter();
	ug.execute();
    }

    /* 添加右上角的注销菜单 */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
	menu.add(Menu.NONE, 1, 0, "注销");
	return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
	switch (item.getItemId()) {
	/* case必须为常量 */
	case 1: {
	    // 先注销 再回到登陆界面
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
	    res = "联系人总数:" + fCount;
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

    /* 获取联系人状态 ,类型是一个Pair数组，键值对方式是 用户名|状态 */
    /* PS：我也不知道这个状态是属于签名，还是那种可以自定义的在线/隐身/离开 等状态 */
    public ArrayList<Pair<String, String>> getFriendList() {
	Roster roster = App.xmppConn.getRoster();
	ArrayList<Pair<String, String>> userStatus = new ArrayList<Pair<String, String>>();
	// 获取联系人列表的集合
	Collection<RosterEntry> rg = roster.getEntries();
	// 遍历联系人列表
	for (RosterEntry rosterEntry : rg) {
	    // 获取好友状态对象
	    Presence presence = roster.getPresence(rosterEntry.getUser());
	    String friendUserName = rosterEntry.getName();// 用户名
	    String friendUserStatus = presence.getStatus();// 状态
	    Pair<String, String> friendInfo = new Pair<String, String>(
		    friendUserName, friendUserStatus);
	    userStatus.add(friendInfo);
	}

	return userStatus;
    }

    /* Logout异步线程 */
    public class LogoutTask extends AsyncTask<Void, Void, String> {

	@Override
	protected String doInBackground(Void... p) {
	    App.logout();
	    String res = "Logout";
	    return res;
	}

	@Override
	protected void onPostExecute(String res) {
	    Toast.makeText(ChatList.this, "已经注销，返回登陆界面", Toast.LENGTH_LONG).show();
	}
    }
}
