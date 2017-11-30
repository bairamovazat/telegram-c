package ru.azat.models.api.engine;

import org.telegram.api.TLConfig;
import org.telegram.api.TLDcOption;
import org.telegram.api.auth.TLAuthorization;
import org.telegram.api.engine.storage.AbsApiState;
import org.telegram.mtproto.state.AbsMTProtoState;
import org.telegram.mtproto.state.ConnectionInfo;
import org.telegram.mtproto.state.KnownSalt;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by ex3ndr on 13.01.14.
 */
public class MyApiState implements AbsApiState, Serializable {

    private HashMap<Integer, ConnectionInfo[]> connections = new HashMap<Integer, ConnectionInfo[]>();
    private HashMap<Integer, byte[]> keys = new HashMap<Integer, byte[]>();
    private HashMap<Integer, Boolean> isAuth = new HashMap<Integer, Boolean>();
    private HashMap<Integer, Integer> userIds = new HashMap<Integer, Integer>();
    private int userId = 0;

    private int primaryDc = 1;

    public MyApiState(String dcIpAddress) {
        connections.put(1, new ConnectionInfo[]{
                new ConnectionInfo(1, 100, dcIpAddress, 443)
        });
    }

    @Override
    public synchronized int getPrimaryDc() {
        return primaryDc;
    }

    @Override
    public synchronized void setPrimaryDc(int dc) {
        primaryDc = dc;
    }

    @Override
    public synchronized boolean isAuthenticated() {
        return isAuthenticated(getPrimaryDc());
    }


    @Override
    public synchronized boolean isAuthenticated(int dcId) {
        if (isAuth.containsKey(dcId)) {
            return isAuth.get(dcId);
        }
        return false;
    }

    @Override
    public synchronized void setAuthenticated(int dcId, boolean auth) {
        isAuth.put(dcId, auth);
    }

    @Override
    public synchronized void updateSettings(TLConfig config) {
        connections.clear();
        HashMap<Integer, ArrayList<ConnectionInfo>> tConnections = new HashMap<Integer, ArrayList<ConnectionInfo>>();
        int id = 0;
        for (TLDcOption option : config.getDcOptions()) {
            if (!tConnections.containsKey(option.getId())) {
                tConnections.put(option.getId(), new ArrayList<ConnectionInfo>());
            }
            tConnections.get(option.getId()).add(new ConnectionInfo(id++, 0, option.getIpAddress(), option.getPort()));
        }

        for (Integer dc : tConnections.keySet()) {
            connections.put(dc, tConnections.get(dc).toArray(new ConnectionInfo[0]));
        }
    }

    @Override
    public synchronized byte[] getAuthKey(int dcId) {
        return keys.get(dcId);
    }

    @Override
    public synchronized void putAuthKey(int dcId, byte[] key) {
        keys.put(dcId, key);
    }

    @Override
    public synchronized ConnectionInfo[] getAvailableConnections(int dcId) {
        if (!connections.containsKey(dcId)) {
            return new ConnectionInfo[0];
        }

        return connections.get(dcId);
    }

    public synchronized void setAvailableConnections(int dcId,ConnectionInfo[] connectionInfo) {
        connections.put(dcId, connectionInfo);
    }

    @Override
    public synchronized AbsMTProtoState getMtProtoState(final int dcId) {
        return new AbsMTProtoState() {
            private KnownSalt[] knownSalts = new KnownSalt[0];

            @Override
            public byte[] getAuthKey() {
                return MyApiState.this.getAuthKey(dcId);
            }

            @Override
            public ConnectionInfo[] getAvailableConnections() {
                return MyApiState.this.getAvailableConnections(dcId);
            }

            @Override
            public KnownSalt[] readKnownSalts() {
                return knownSalts;
            }

            @Override
            protected void writeKnownSalts(KnownSalt[] salts) {
                knownSalts = salts;
            }
        };
    }

    @Override
    public void doAuth(TLAuthorization authorization) {
        userIds.put(primaryDc, authorization.getUser().getId());
    }

    @Override
    public synchronized void resetAuth() {
        isAuth.clear();
    }

    @Override
    public synchronized void reset() {
        isAuth.clear();
        keys.clear();
        userIds.clear();
    }

    @Override
    public int getUserId() {
        return userIds.get(primaryDc);
    }
}
