package other;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.RosterGroup;
import org.jivesoftware.smack.RosterListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.provider.PrivacyProvider;
import org.jivesoftware.smack.provider.ProviderManager;
import org.jivesoftware.smackx.GroupChatInvitation;
import org.jivesoftware.smackx.PrivateDataManager;
import org.jivesoftware.smackx.bytestreams.socks5.provider.BytestreamsProvider;
import org.jivesoftware.smackx.packet.ChatStateExtension;
import org.jivesoftware.smackx.packet.LastActivity;
import org.jivesoftware.smackx.packet.OfflineMessageInfo;
import org.jivesoftware.smackx.packet.OfflineMessageRequest;
import org.jivesoftware.smackx.packet.SharedGroupsInfo;
import org.jivesoftware.smackx.provider.AdHocCommandDataProvider;
import org.jivesoftware.smackx.provider.DataFormProvider;
import org.jivesoftware.smackx.provider.DelayInformationProvider;
import org.jivesoftware.smackx.provider.DiscoverInfoProvider;
import org.jivesoftware.smackx.provider.DiscoverItemsProvider;
import org.jivesoftware.smackx.provider.MUCAdminProvider;
import org.jivesoftware.smackx.provider.MUCOwnerProvider;
import org.jivesoftware.smackx.provider.MUCUserProvider;
import org.jivesoftware.smackx.provider.MessageEventProvider;
import org.jivesoftware.smackx.provider.MultipleAddressesProvider;
import org.jivesoftware.smackx.provider.RosterExchangeProvider;
import org.jivesoftware.smackx.provider.StreamInitiationProvider;
import org.jivesoftware.smackx.provider.VCardProvider;
import org.jivesoftware.smackx.provider.XHTMLExtensionProvider;
import org.jivesoftware.smackx.search.UserSearch;

import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.util.Log;

/**
 * ����openfire��������<br/>
 * ��������Openfire
 * 
 * @author Michael.Zhang <br/>
 *         Time 2013-3-22 ����3:32:36
 * 
 */
@SuppressWarnings("unused")
public class ClientConServer {

        
	private Context context;

        boolean isOnline = false;// ��־��������ʾ�û��Ƿ����ߣ���ʼΪ������

        private Handler isConnectServerhandler;// Handler����Ҫ����������������

        public static XMPPConnection connection; // ����XMPPconnection���󣬽���login�����н��г�ʼ��

        /**
         * ���췽��
         * 
         * @param _context
         */
        public ClientConServer(Context _context) {
                this.context = _context;
        }

        /**
         * ���캯�������ڳ�ʼ��������UI���߳�֮��Ľ���Handler
         * 
         * @param _handler
         */
        public ClientConServer(Handler _handler) {
                this.isConnectServerhandler = _handler;
        }

        public ClientConServer() {
        }

        /**
         * ���췽�������ڳ�ʼ��connection,�˷�����Ҫ����ע��ʱ�ĳ�ʼ��
         * 
         * @param _serverIp
         * @param _serverPort
         * @throws XMPPException
         */
        public ClientConServer(String _serverIp, int _serverPort)
                        throws XMPPException {
                connection = getConnection(_serverIp, _serverPort);
                connection.connect();
        }

        /**
         * ��ȡ����
         * 
         * @param _serverIp
         *            ������IP��ַ
         * @param _serverPort
         *            �������˿�
         * @return
         */
        public XMPPConnection getConnection(String _serverIp, int _serverPort) {
                //��ʼ������
                this.configure(ProviderManager.getInstance());
                
                ConnectionConfiguration config = new ConnectionConfiguration(_serverIp,
                                _serverPort);

                /* �Ƿ����õ���ģʽ */
                // config.setDebuggerEnabled(true);
                // config.setReconnectionAllowed(true);
                config.setSecurityMode(ConnectionConfiguration.SecurityMode.enabled);
                /* �Ƿ����ð�ȫ��֤ */
                config.setSASLAuthenticationEnabled(false);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                        config.setTruststoreType("AndroidCAStore");
                        config.setTruststorePassword(null);
                        config.setTruststorePath(null);
                } else {

                        String path = System.getProperty("javax.net.ssl.trustStore");
                        if (path == null)
                                path = System.getProperty("java.home") + File.separator + "etc"
                                                + File.separator + "security" + File.separator
                                                + "cacerts.bks";
                        config.setTruststorePath(path);
                        config.setTruststoreType("BKS");
                }

                /* ����Connection���� */
                XMPPConnection connection = new XMPPConnection(config);
                return connection;
        }

        /**
         * ��½
         * 
         * @param _username
         *            �û���
         * @param _password
         *            ����
         * @param _serverIp
         *            ������IP��ַ
         * @param _serverPort
         *            �������˿ں�
         * @return
         */
        public boolean login(String _username, String _password, String _serverIp,
                        int _serverPort) {

                // ��ʼ��connection����
                connection = getConnection(_serverIp, _serverPort);

                try {
                        connection.connect();
                        connection.login(_username, _password);

                        return true;
                } catch (XMPPException e) {
                        e.printStackTrace();
                        return false;

                }

        }

        /**
         * �˳���½�����Ͽ��������������
         * 
         * @return �˳����
         */
        public boolean logoff() {

                if (connection.isConnected()) {
                        connection.disconnect();
                        return true;
                } else {
                        return false;// �û�û�е�¼������Ͽ�
                }

        }

        /**
         * �õ������б�
         * 
         * @return �����б�
         */
        public Map<String, List<Object>> getUserList() {

                Roster roster = connection.getRoster();
                Collection<RosterGroup> entriesGroup = roster.getGroups();

                Map<String, List<Object>> map = new HashMap<String, List<Object>>();
                List<Object> listGroup = new ArrayList<Object>();
                List<Object> listGroupMember = new ArrayList<Object>();
                for (RosterGroup group : entriesGroup) {
                        Collection<RosterEntry> entries = group.getEntries();
                        listGroup.add(group.getName());
                        List<Object> groupMemb = new ArrayList<Object>();
                        for (RosterEntry entry : entries) {
                                groupMemb.add(entry.getName());
                        }
                        listGroupMember.add(groupMemb);
                }

                map.put("groupName", listGroup);
                map.put("groupMember", listGroupMember);

                return map;
        }

        /**
         * ��ѯĳ�û��Ƿ�����
         * 
         * @param _username
         * @return �û����ߣ�����true�� �����ߣ�����false
         */
        public boolean isSomeOneOnline(String _username) {
                Roster roster = connection.getRoster();

                String userJID = this.getUserJIDByName(_username);
                roster.addRosterListener(new RosterListener() {

                        @Override
                        public void presenceChanged(Presence presence) {
                                isOnline = presence.isAvailable();
                        }

                        @Override
                        public void entriesUpdated(Collection<String> arg0) {

                        }

                        @Override
                        public void entriesDeleted(Collection<String> arg0) {

                        }

                        @Override
                        public void entriesAdded(Collection<String> arg0) {

                        }
                });
                Presence presence = roster.getPresence(userJID);
                isOnline = presence.isAvailable();
                return isOnline;
        }

        /**
         * �õ��û�����״̬���������¼��֣�<br/>
         * 1 �� available ��Available (the default) ����<br/>
         * 2�� away Away. �뿪<br/>
         * 3�� chat Chat����������<br/>
         * 4�� dnd ��Do not disturb���������<br/>
         * 5�� xa �� Away for an extended period of time.��ʱ�뿪<br/>
         * 
         * @param _username
         *            �û���
         * @return <b> available </b> ���� <b> away</b> �뿪 <b> chat </b> �������� <b> dnd
         *         </b> ������� <b> xa </b>��ʱ�뿪
         */
        public Presence.Mode getMode(String _username) {
                Roster roster = connection.getRoster();
                String userJID = this.getUserJIDByName(_username);
                Presence presence = roster.getPresence(userJID);

                return presence.getMode();
        }

        /**
         * �����û�״̬
         * 
         * @param _mode
         *            ״̬����
         */
        public void setMode(Presence.Mode _mode) {
                Presence present = new Presence(Presence.Type.available, null, 1, _mode);
                connection.sendPacket(present);
        }

        /**
         * �����û�����ѯ�û���JID��Ϣ����b@michael-pc
         * 
         * @param _useremail
         *            �û�����
         * @return �����û���JID
         */
        public String getUserJIDByEmail(String _useremail) {
                Roster roster = connection.getRoster();
                RosterEntry rosterentry = roster.getEntry(_useremail);
                String userJID = rosterentry.getUser();
                return userJID;
        }

        /**
         * �����û�������û���JID <br/>
         * �����{@link #getUserEmail(String)} {@link #getUserJIDByEmail(String)}
         * 
         * @param _username
         *            �û���
         * @return ������ڣ������û�JID����������ڣ�����null
         */
        public String getUserJIDByName(String _username) {
                String useremail = getUserEmail(_username);
                String userJID = null;
                if (useremail != null) {
                        // �û�����
                        userJID = getUserJIDByEmail(useremail);
                }
                return userJID;
        }

        /**
         * �����û�������û�������
         * 
         * @param _username
         *            �û���
         * @return ��������û��������û����䣬����������û�������null
         */
        public String getUserEmail(String _username) {
                Roster roster = connection.getRoster();
                Collection<RosterEntry> rosterentrys = roster.getEntries();

                String useremail = null;
                for (RosterEntry rosterentry : rosterentrys) {
                        if (_username.equals(rosterentry.getName())) {
                                useremail = rosterentry.getUser();
                                break;
                        }
                }

                return useremail;

        }

        public void listeningConnectToServer() {
                connection.addConnectionListener(new ConnectionListener() {

                        @Override
                        public void reconnectionSuccessful() {
                                // TODO Auto-generated method stub
                                android.os.Message msg = android.os.Message.obtain();
                                msg.obj = true;
                                isConnectServerhandler.sendMessage(msg);
                        }

                        @Override
                        public void reconnectionFailed(Exception arg0) {
                                // TODO Auto-generated method stub

                        }

                        @Override
                        public void reconnectingIn(int arg0) {
                                // TODO Auto-generated method stub

                        }

                        @Override
                        public void connectionClosedOnError(Exception e) {
                                // TODO Auto-generated method stub
                                android.os.Message msg = android.os.Message.obtain();
                                msg.obj = false;
                                isConnectServerhandler.sendMessage(msg);

                        }

                        @Override
                        public void connectionClosed() {
                                // TODO Auto-generated method stub

                        }
                });
        }

        /**
         * ԭ��the smack.providers file, usually in /META-INF folder in normal versions of smack, can't be loaded in Android because its jar packaging. So all the providers must be initialized by hand
         * <br/>
         * �ο���ַ��http://stackoverflow.com/questions/5910219/problem-using-usersearch-in-xmpp-with-asmack
         * @param pm
         */
        public void configure(ProviderManager pm) {

                // Private Data Storage
                pm.addIQProvider("query", "jabber:iq:private",
                                new PrivateDataManager.PrivateDataIQProvider());

                // Time
                try {
                        pm.addIQProvider("query", "jabber:iq:time",
                                        Class.forName("org.jivesoftware.smackx.packet.Time"));
                } catch (ClassNotFoundException e) {
                        Log.w("TestClient",
                                        "Can't load class for org.jivesoftware.smackx.packet.Time");
                }

                // Roster Exchange
                pm.addExtensionProvider("x", "jabber:x:roster",
                                new RosterExchangeProvider());

                // Message Events
                pm.addExtensionProvider("x", "jabber:x:event",
                                new MessageEventProvider());

                // Chat State
                pm.addExtensionProvider("active",
                                "http://jabber.org/protocol/chatstates",
                                new ChatStateExtension.Provider());
                pm.addExtensionProvider("composing",
                                "http://jabber.org/protocol/chatstates",
                                new ChatStateExtension.Provider());
                pm.addExtensionProvider("paused",
                                "http://jabber.org/protocol/chatstates",
                                new ChatStateExtension.Provider());
                pm.addExtensionProvider("inactive",
                                "http://jabber.org/protocol/chatstates",
                                new ChatStateExtension.Provider());
                pm.addExtensionProvider("gone",
                                "http://jabber.org/protocol/chatstates",
                                new ChatStateExtension.Provider());

                // XHTML
                pm.addExtensionProvider("html", "http://jabber.org/protocol/xhtml-im",
                                new XHTMLExtensionProvider());

                // Group Chat Invitations
                pm.addExtensionProvider("x", "jabber:x:conference",
                                new GroupChatInvitation.Provider());

                // Service Discovery # Items
                pm.addIQProvider("query", "http://jabber.org/protocol/disco#items",
                                new DiscoverItemsProvider());

                // Service Discovery # Info
                pm.addIQProvider("query", "http://jabber.org/protocol/disco#info",
                                new DiscoverInfoProvider());

                // Data Forms
                pm.addExtensionProvider("x", "jabber:x:data", new DataFormProvider());

                // MUC User
                pm.addExtensionProvider("x", "http://jabber.org/protocol/muc#user",
                                new MUCUserProvider());

                // MUC Admin
                pm.addIQProvider("query", "http://jabber.org/protocol/muc#admin",
                                new MUCAdminProvider());

                // MUC Owner
                pm.addIQProvider("query", "http://jabber.org/protocol/muc#owner",
                                new MUCOwnerProvider());

                // Delayed Delivery
                pm.addExtensionProvider("x", "jabber:x:delay",
                                new DelayInformationProvider());

                // Version
                try {
                        pm.addIQProvider("query", "jabber:iq:version",
                                        Class.forName("org.jivesoftware.smackx.packet.Version"));
                } catch (ClassNotFoundException e) {
                        // Not sure what's happening here.
                }

                // VCard
                pm.addIQProvider("vCard", "vcard-temp", new VCardProvider());

                // Offline Message Requests
                pm.addIQProvider("offline", "http://jabber.org/protocol/offline",
                                new OfflineMessageRequest.Provider());

                // Offline Message Indicator
                pm.addExtensionProvider("offline",
                                "http://jabber.org/protocol/offline",
                                new OfflineMessageInfo.Provider());

                // Last Activity
                pm.addIQProvider("query", "jabber:iq:last", new LastActivity.Provider());

                // User Search
                pm.addIQProvider("query", "jabber:iq:search", new UserSearch.Provider());

                // SharedGroupsInfo
                pm.addIQProvider("sharedgroup",
                                "http://www.jivesoftware.org/protocol/sharedgroup",
                                new SharedGroupsInfo.Provider());

                // JEP-33: Extended Stanza Addressing
                pm.addExtensionProvider("addresses",
                                "http://jabber.org/protocol/address",
                                new MultipleAddressesProvider());

                // FileTransfer
                pm.addIQProvider("si", "http://jabber.org/protocol/si",
                                new StreamInitiationProvider());

                pm.addIQProvider("query", "http://jabber.org/protocol/bytestreams",
                                new BytestreamsProvider());

                // Privacy
                pm.addIQProvider("query", "jabber:iq:privacy", new PrivacyProvider());
                pm.addIQProvider("command", "http://jabber.org/protocol/commands",
                                new AdHocCommandDataProvider());
                pm.addExtensionProvider("malformed-action",
                                "http://jabber.org/protocol/commands",
                                new AdHocCommandDataProvider.MalformedActionError());
                pm.addExtensionProvider("bad-locale",
                                "http://jabber.org/protocol/commands",
                                new AdHocCommandDataProvider.BadLocaleError());
                pm.addExtensionProvider("bad-payload",
                                "http://jabber.org/protocol/commands",
                                new AdHocCommandDataProvider.BadPayloadError());
                pm.addExtensionProvider("bad-sessionid",
                                "http://jabber.org/protocol/commands",
                                new AdHocCommandDataProvider.BadSessionIDError());
                pm.addExtensionProvider("session-expired",
                                "http://jabber.org/protocol/commands",
                                new AdHocCommandDataProvider.SessionExpiredError());
        }

}