package ru.azat.models.api;

import org.telegram.api.TLConfig;
import org.telegram.api.auth.TLAuthorization;
import org.telegram.api.auth.TLExportedAuthorization;
import org.telegram.api.auth.TLSentCode;
import org.telegram.api.chat.channel.TLChannel;
import org.telegram.api.contacts.TLResolvedPeer;
import org.telegram.api.engine.*;
import org.telegram.api.functions.auth.TLRequestAuthExportAuthorization;
import org.telegram.api.functions.auth.TLRequestAuthSendCode;
import org.telegram.api.functions.auth.TLRequestAuthSignIn;
import org.telegram.api.functions.auth.TLRequestAuthSignUp;
import org.telegram.api.functions.channels.TLRequestChannelsJoinChannel;
import org.telegram.api.functions.contacts.TLRequestContactsResolveUsername;
import org.telegram.api.functions.help.TLRequestHelpGetConfig;
import org.telegram.api.functions.messages.*;
import org.telegram.api.functions.updates.TLRequestUpdatesGetState;
import org.telegram.api.functions.users.TLRequestUsersGetUsers;
import org.telegram.api.input.chat.TLInputChannel;
import org.telegram.api.input.peer.TLInputPeerEmpty;
import org.telegram.api.messages.dialogs.TLDialogs;
import org.telegram.api.updates.TLAbsUpdates;
import org.telegram.api.updates.TLUpdatesState;
import org.telegram.api.user.TLAbsUser;
import org.telegram.mtproto.state.ConnectionInfo;
import org.telegram.tl.TLIntVector;
import org.telegram.tl.TLObject;
import org.telegram.tl.TLVector;
import ru.azat.models.RegistrationData;
import ru.azat.models.api.engine.MyApiState;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.TimeoutException;

public class TelegramBot {

    private int apiId = 104888;
    private String apiHash = "7b8172e9631b089f26c49bb20b74ea9b";
    private MyApiState apiState;
    private TelegramApi api;
    private String dcIpAddress = "149.154.167.50";

    public void createApi() {
        apiState = new MyApiState(dcIpAddress);
        api = new TelegramApi(apiState, new AppInfo(apiId, "console", "0.1", "0.1", "ru"), new ApiCallback() {
            @Override
            public void onAuthCancelled(TelegramApi api) {

            }

            @Override
            public void onUpdatesInvalidated(TelegramApi api) {

            }

            @Override
            public void onUpdate(TLAbsUpdates updates) {

            }
        });
    }

    public void createApi(MyApiState myApiState) {
        apiState = myApiState;
        api = new TelegramApi(apiState, new AppInfo(apiId, "console", "0.1", "0.1", "ru"), new ApiCallback() {
            @Override
            public void onAuthCancelled(TelegramApi api) {

            }

            @Override
            public void onUpdatesInvalidated(TelegramApi api) {

            }

            @Override
            public void onUpdate(TLAbsUpdates updates) {

            }
        });
        this.reloadConnectionInfo();
    }

    public void initConfigs() throws TimeoutException, RpcException {
        TLConfig config = api.doRpcCallNonAuth(new TLRequestHelpGetConfig());
        apiState.updateSettings(config);
    }

    public TLSentCode sendCode(String phone) throws TimeoutException, RpcException {
        System.out.println("Sending sms to phone " + phone + "...");
        TLSentCode sentCode;
        TLRequestAuthSendCode requestAuthSendCode = new TLRequestAuthSendCode();
        requestAuthSendCode.setApiHash(apiHash);
        requestAuthSendCode.setApiId(apiId);
        requestAuthSendCode.setPhoneNumber(phone);
        sentCode = api.doRpcCallNonAuth(requestAuthSendCode);
        System.out.println("sent.");
        return sentCode;

    }

    public void signIn(String phoneCode, String phoneHash, String phoneNumber) throws TimeoutException, IOException {
        TLAuthorization auth;
        TLRequestAuthSignIn authSignIn = new TLRequestAuthSignIn();
        authSignIn.setPhoneCode(phoneCode);
        authSignIn.setPhoneCodeHash(phoneHash);
        authSignIn.setPhoneNumber(phoneNumber);
        auth = api.doRpcCallNonAuth(authSignIn);
        apiState.doAuth(auth);
        apiState.setAuthenticated(apiState.getPrimaryDc(), true);
        TLUpdatesState state = api.doRpcCall(new TLRequestUpdatesGetState());
    }

    public void signUp(String phoneCode, String phoneHash, String phoneNumber, String firstName, String secondName) throws TimeoutException, IOException {
        TLAuthorization auth;
        TLRequestAuthSignUp authSignUp = new TLRequestAuthSignUp();
        authSignUp.setPhoneCode(phoneCode);
        authSignUp.setPhoneCodeHash(phoneHash);
        authSignUp.setPhoneNumber(phoneNumber);
        authSignUp.setFirstName(firstName);
        authSignUp.setLastName(secondName);

        auth = api.doRpcCallNonAuth(authSignUp);
        apiState.doAuth(auth);
        apiState.setAuthenticated(apiState.getPrimaryDc(), true);
        TLUpdatesState state = api.doRpcCall(new TLRequestUpdatesGetState());

    }

    private void loginBySms() throws TimeoutException, IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        System.out.print("Phone number for bot:");
        String phone = reader.readLine();

        System.out.print("Sending sms to phone " + phone + "...");
        TLSentCode sentCode;
        TLRequestAuthSendCode requestAuthSendCode = new TLRequestAuthSendCode();
        requestAuthSendCode.setApiHash(apiHash);
        requestAuthSendCode.setApiId(apiId);
        requestAuthSendCode.setPhoneNumber(phone);
        sentCode = api.doRpcCallNonAuth(requestAuthSendCode);
        System.out.println("sent.");

        System.out.print("Activation code:");
        String code = reader.readLine();

        TLAuthorization auth;
        if (sentCode.isPhoneRegistered()) {
            TLRequestAuthSignIn authSignIn = new TLRequestAuthSignIn();
            authSignIn.setPhoneCode(code);
            authSignIn.setPhoneCodeHash(sentCode.getPhoneCodeHash());
            authSignIn.setPhoneNumber(phone);
            auth = api.doRpcCallNonAuth(authSignIn);
            apiState.doAuth(auth);
        } else {
            System.out.println("Вы не зарегистрированны. Пожалуйста введите данные для регистрации:");
            System.out.print("FirstName: ");
            String firstName = reader.readLine();
            System.out.print("LastName: ");
            String lastName = reader.readLine();

            TLRequestAuthSignUp authSignUp = new TLRequestAuthSignUp();
            authSignUp.setPhoneCode(code);
            authSignUp.setPhoneCodeHash(sentCode.getPhoneCodeHash());
            authSignUp.setPhoneNumber(phone);
            authSignUp.setFirstName(firstName);
            authSignUp.setLastName(lastName);

            auth = api.doRpcCallNonAuth(authSignUp);
            apiState.doAuth(auth);

        }
        apiState.setAuthenticated(apiState.getPrimaryDc(), true);
        System.out.println("Activation complete.");
        System.out.print("Loading initial state...");
        TLUpdatesState state = api.doRpcCall(new TLRequestUpdatesGetState());
        System.out.println("loaded.");
        saveState("apiState-" + phone + "-" + System.currentTimeMillis() / 60000);
    }

    public void loginFromFile(String fileName) {
        apiState = loadState(fileName);
        reloadConnectionInfo();

        api = new TelegramApi(apiState, new AppInfo(apiId, "console", "0.1", "0.1", "en"), new ApiCallback() {
            @Override
            public void onAuthCancelled(TelegramApi api) {

            }

            @Override
            public void onUpdatesInvalidated(TelegramApi api) {

            }

            @Override
            public void onUpdate(TLAbsUpdates updates) {

            }
        });

        try {
            apiState.setAuthenticated(apiState.getPrimaryDc(), true);
            System.out.println("Activation complete.");
            System.out.print("Loading initial state...");
            TLUpdatesState state = api.doRpcCall(new TLRequestUpdatesGetState());
        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
        }
        reloadConnectionInfo();

        System.out.println("loaded.");
    }

//    public TelegramApi getApi() {
//        return api;
//    }

    public TLDialogs getDialogs() {
        TLRequestMessagesGetDialogs dialogsRequest = new TLRequestMessagesGetDialogs();
        dialogsRequest.setLimit(100);
        dialogsRequest.setOffsetDate(-1);
        dialogsRequest.setOffsetId(Integer.MAX_VALUE);
        dialogsRequest.setOffsetPeer(new TLInputPeerEmpty());
        TLObject dialogs = null;
        try {
            dialogs = api.doRpcCall(dialogsRequest);
        } catch (TimeoutException | IOException e) {
            e.printStackTrace();
        }
        return (TLDialogs) dialogs;
    }

    public void addChannels(String... links) {
        for (String str : links) {
            if (str.contains("telegram.me/joinchat")) {
                String hash = str.split("/")[(str.split("/").length) - 1];
                TLRequestMessagesImportChatInvite in = new TLRequestMessagesImportChatInvite();
                in.setHash(hash);
                try {
                    TLAbsUpdates bb = api.doRpcCall(in);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (str.contains("telegram.me/")) {
                String username = str.split("/")[(str.split("/").length) - 1];
                try {
                    TLRequestContactsResolveUsername ru = new TLRequestContactsResolveUsername();
                    ru.setUsername(username);
                    TLResolvedPeer peer = api.doRpcCall(ru);
                    TLRequestChannelsJoinChannel join = new TLRequestChannelsJoinChannel();
                    TLInputChannel ch = new TLInputChannel();
                    ch.setChannelId(peer.getChats().get(0).getId());
                    ch.setAccessHash(((TLChannel) peer.getChats().get(0)).getAccessHash());
                    join.setChannel(ch);
                    api.doRpcCall(join);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void addChanelByName(String chanelName) throws IOException, TimeoutException {
        String username = chanelName;
        TLRequestContactsResolveUsername ru = new TLRequestContactsResolveUsername();
        ru.setUsername(username);
        TLResolvedPeer peer = api.doRpcCall(ru);
        TLRequestChannelsJoinChannel join = new TLRequestChannelsJoinChannel();
        TLInputChannel ch = new TLInputChannel();
        ch.setChannelId(peer.getChats().get(0).getId());
        ch.setAccessHash(((TLChannel) peer.getChats().get(0)).getAccessHash());
        join.setChannel(ch);
        api.doRpcCall(join);
    }

    public TLVector<TLAbsUser> getUser(int userId) {
        TLVector intVector = new TLIntVector();
        intVector.add(userId);
        TLRequestUsersGetUsers usersGetUsers = new TLRequestUsersGetUsers();
        usersGetUsers.setId(intVector);
        TLVector<TLAbsUser> users = null;
        try {
            users = api.doRpcCall(usersGetUsers);
        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
        }
        return users;
    }

    public MyApiState getApiState() {
        return apiState;
    }


    private MyApiState loadState(String fileName) {
        MyApiState deserializableApiState = null;
        try {
            deserializableApiState = utils.deserializeInFile(fileName);
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }
        return deserializableApiState;
    }

    private void reloadConnectionInfo() {
        apiState.setAvailableConnections(1, new ConnectionInfo[]{
                new ConnectionInfo(2, 0, dcIpAddress, 443)
        });
    }

    public void saveState(String fileName) {
        try {
            utils.serializeOnFile(fileName, apiState);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public boolean isClosed(){
        return api == null || api.isClosed();
    }

    public void close() {
        api.close();
    }
}

