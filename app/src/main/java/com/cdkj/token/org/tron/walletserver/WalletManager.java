package com.cdkj.token.org.tron.walletserver;

import android.util.Log;

import com.cdkj.token.org.tron.common.crypto.Hash;
import com.cdkj.token.org.tron.common.utils.Base58;

public class WalletManager {

    public static Wallet store(Wallet wallet, String password)  {
        if (!wallet.isWatchOnly() && (wallet.getECKey() == null || wallet.getECKey().getPrivKey() == null)) {
            throw new NullPointerException("Private Key is null");
        }
        String address = wallet.getAddress();//地址
        Log.e("pppppp", "store: 地址为:"+address );
        return wallet;
    }




    public static String encode58Check(byte[] input) {
        byte[] hash0 = Hash.sha256(input);
        byte[] hash1 = Hash.sha256(hash0);
        byte[] inputCheck = new byte[input.length + 4];
        System.arraycopy(input, 0, inputCheck, 0, input.length);
        System.arraycopy(hash1, 0, inputCheck, input.length, 4);
        return Base58.encode(inputCheck);
    }

    private static byte[] decode58Check(String input) {
        byte[] decodeCheck = Base58.decode(input);
        if (decodeCheck.length <= 4) {
            return null;
        }
        byte[] decodeData = new byte[decodeCheck.length - 4];
        System.arraycopy(decodeCheck, 0, decodeData, 0, decodeData.length);
        byte[] hash0 = Hash.sha256(decodeData);
        byte[] hash1 = Hash.sha256(hash0);
        if (hash1[0] == decodeCheck[decodeData.length] &&
                hash1[1] == decodeCheck[decodeData.length + 1] &&
                hash1[2] == decodeCheck[decodeData.length + 2] &&
                hash1[3] == decodeCheck[decodeData.length + 3]) {
            return decodeData;
        }
        return null;
    }
}
