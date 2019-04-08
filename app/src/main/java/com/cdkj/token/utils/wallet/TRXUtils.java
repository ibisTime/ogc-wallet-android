package com.cdkj.token.utils.wallet;

import com.cdkj.token.org.tron.common.crypto.ECKey;
import com.cdkj.token.org.tron.common.crypto.Hash;
import com.cdkj.token.org.tron.common.utils.Base58;
import com.cdkj.token.org.tron.common.utils.Utils;

import org.bitcoinj.core.Sha256Hash;
import org.spongycastle.math.ec.ECPoint;

import java.math.BigInteger;
import java.util.Arrays;

import static java.util.Arrays.copyOfRange;

/**
 * @updateDts 2019/4/1
 */
public class TRXUtils {

    private static ECKey mECKey;

    public static String getPrivateKey(String pass) {
        mECKey = new ECKey(Utils.getRandom());
//        byte[] pwd = getPasswordHash(pass);
//        String pwdAsc = ByteArray.toHexString(pwd);

        byte[] aseKey = getEncKey(pass);
        byte[] privKeyPlain = mECKey.getPrivKeyBytes();
        byte[] privKeyEnced = SymmEncoder.AES128EcbEnc(privKeyPlain, aseKey);
        String privKeyStr = ByteArray.toHexString(privKeyEnced);

        return privKeyStr;
    }

    public static byte[] getPasswordHash(String password) {

        byte[] pwd;
        pwd = Hash.sha256(password.getBytes());
        pwd = Hash.sha256(pwd);
        pwd = Arrays.copyOfRange(pwd, 0, 16);
        return pwd;
    }

    public static byte[] getEncKey(String password) {

        byte[] encKey;
        encKey = Hash.sha256(password.getBytes());
        encKey = Arrays.copyOfRange(encKey, 0, 16);
        return encKey;
    }

    public static String getTRXAddress(String privKey) {
        String address = null;
        try {
            address = private2Address(ByteArray.fromHexString(privKey));
        } catch (CipherException e) {
            e.printStackTrace();
            return address;
        }
        return address;
    }

    private static String private2Address(byte[] privateKey) throws CipherException {

        if(mECKey != null) {
            return encode58Check(mECKey.getAddress());
        }
//        else if (publicKey != null){
//            return decode58Check(ECKey.fromPublicOnly(publicKey).getAddress());
//        } else {
//            return address;
//        }
        getAddress1();


        return null;
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


    private static void getAddress1() {
        //        ECKey eCkey = mECKey;//注意  包路径
//        if (privateKey == null || privateKey.length == 0) {
//            eCkey = new ECKey(Utils.getRandom());  //Gen new Keypair
//            LogUtil.E("ppppppp新创建的的 私钥为空"+privateKey);
//        } else {
//            eCkey = ECKey.fromPrivate(privateKey);
//            LogUtil.E("ppppppp根据私钥创建的"+privateKey);
//        }
//
//        byte[] publicKey0 = eCkey.getPubKey();
//        byte[] publicKey1 = private2Public(eCkey.getPrivKeyBytes());
//        if (!Arrays.equals(publicKey0, publicKey1)) {
//            LogUtil.E("ppppppp两个数组不相同异常");
//            throw new CipherException("publickey error");
//        }
//
//        byte[] address0 = eCkey.getAddress();
//        byte[] address1 = public2Address(publicKey0);
//        if (!Arrays.equals(address0, address1)) {
//            LogUtil.E("ppppppp两个地址不相同,抛出异常");
//            throw new CipherException("address error");
//        }
//        System.out.println("Address: " + ByteArray.toHexString(address0));
//
//        String base58checkAddress0 = encode58Check(address0);
//        String base58checkAddress1 = address2Encode58Check(address0);
//        if (!base58checkAddress0.equals(base58checkAddress1)) {
//            throw new CipherException("base58checkAddress error");
//        }

//        return base58checkAddress1;
    }


    //aaaaaa
    public static String encode58Check(byte[] input) {
        byte[] hash0 = Sha256Hash.hash(input);
        byte[] hash1 = Sha256Hash.hash(hash0);
        byte[] inputCheck = new byte[input.length + 4];
        System.arraycopy(input, 0, inputCheck, 0, input.length);
        System.arraycopy(hash1, 0, inputCheck, input.length, 4);
        return Base58.encode(inputCheck);
    }

    private static byte[] private2Public(byte[] privateKey) {
        BigInteger privKey = new BigInteger(1, privateKey);
        ECPoint point = ECKey.CURVE.getG().multiply(privKey);
        return point.getEncoded(false);
    }

    public static String address2Encode58Check(byte[] input) {
        byte[] hash0 = Sha256Hash.hash(input);

        byte[] hash1 = Sha256Hash.hash(hash0);

        byte[] inputCheck = new byte[input.length + 4];

        System.arraycopy(input, 0, inputCheck, 0, input.length);
        System.arraycopy(hash1, 0, inputCheck, input.length, 4);

        return Base58.encode(inputCheck);
    }

    private static byte[] public2Address(byte[] publicKey) {
        byte[] hash = Hash.sha3(copyOfRange(publicKey, 1, publicKey.length));
        byte[] address = copyOfRange(hash, 11, hash.length);
        //  private static byte addressPreFixByte = 65;  源码
//        address[0] = Wallet.getAddressPreFixByte();
        address[0] = 65;
        return address;
    }


}

class CipherException extends Exception {
    public CipherException(String message) {
        super(message);
    }

    public CipherException(Throwable cause) {
        super(cause);
    }

    public CipherException(String message, Throwable cause) {
        super(message, cause);
    }
}
