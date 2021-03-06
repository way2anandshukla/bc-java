package org.bouncycastle.jce.provider.test;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Security;

import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.RC5ParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.bouncycastle.asn1.iana.IANAObjectIdentifiers;
import org.bouncycastle.asn1.nist.NISTObjectIdentifiers;
import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Hex;
import org.bouncycastle.util.test.SimpleTest;

/**
 * HMAC tester
 */
public class HMacTest
    extends SimpleTest
{
    static byte[]   keyBytes = Hex.decode("0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b0b");
    static byte[]   message = Hex.decode("4869205468657265");
    static byte[]   output1 = Hex.decode("b617318655057264e28bc0b6fb378c8ef146be00");
    static byte[]   outputMD5 = Hex.decode("5ccec34ea9656392457fa1ac27f08fbc");
    static byte[]   outputMD2 = Hex.decode("dc1923ef5f161d35bef839ca8c807808");
    static byte[]   outputMD4 = Hex.decode("5570ce964ba8c11756cdc3970278ff5a");
    static byte[]   output224 = Hex.decode("896fb1128abbdf196832107cd49df33f47b4b1169912ba4f53684b22");
    static byte[]   output256 = Hex.decode("b0344c61d8db38535ca8afceaf0bf12b881dc200c9833da726e9376c2e32cff7");
    static byte[]   output384 = Hex.decode("afd03944d84895626b0825f4ab46907f15f9dadbe4101ec682aa034c7cebc59cfaea9ea9076ede7f4af152e8b2fa9cb6");
    static byte[]   output512 = Hex.decode("87aa7cdea5ef619d4ff0b4241a1d6cb02379f4e2ce4ec2787ad0b30545e17cdedaa833b7d6b8a702038b274eaea3f4e4be9d914eeb61f1702e696c203a126854");
    static byte[]   output512_224 = Hex.decode("b244ba01307c0e7a8ccaad13b1067a4cf6b961fe0c6a20bda3d92039");
    static byte[]   output512_256 = Hex.decode("9f9126c3d9c3c330d760425ca8a217e31feae31bfe70196ff81642b868402eab");
    static byte[]   outputRipeMD128 = Hex.decode("fda5717fb7e20cf05d30bb286a44b05d");
    static byte[]   outputRipeMD160 = Hex.decode("24cb4bd67d20fc1a5d2ed7732dcc39377f0a5668");
    static byte[]   outputTiger = Hex.decode("1d7a658c75f8f004916e7b07e2a2e10aec7de2ae124d3647");
    static byte[]   outputOld384 = Hex.decode("0a046aaa0255e432912228f8ccda437c8a8363fb160afb0570ab5b1fd5ddc20eb1888b9ed4e5b6cb5bc034cd9ef70e40");
    static byte[]   outputOld512 = Hex.decode("9656975ee5de55e75f2976ecce9a04501060b9dc22a6eda2eaef638966280182477fe09f080b2bf564649cad42af8607a2bd8d02979df3a980f15e2326a0a22a");

    static byte[]   outputKck224 = Hex.decode("b73d595a2ba9af815e9f2b4e53e78581ebd34a80b3bbaac4e702c4cc");
    static byte[]   outputKck256 = Hex.decode("9663d10c73ee294054dc9faf95647cb99731d12210ff7075fb3d3395abfb9821");
    static byte[]   outputKck288 = Hex.decode("36145df8742160a1811139494d708f9a12757c30dedc622a98aa6ecb69da32a34ea55441");
    static byte[]   outputKck384 = Hex.decode("892dfdf5d51e4679bf320cd16d4c9dc6f749744608e003add7fba894acff87361efa4e5799be06b6461f43b60ae97048");
    static byte[]   outputKck512 = Hex.decode("8852c63be8cfc21541a4ee5e5a9a852fc2f7a9adec2ff3a13718ab4ed81aaea0b87b7eb397323548e261a64e7fc75198f6663a11b22cd957f7c8ec858a1c7755");

    static byte[]   outputSha3_224 = Hex.decode("3b16546bbc7be2706a031dcafd56373d9884367641d8c59af3c860f7");
    static byte[]   outputSha3_256 = Hex.decode("ba85192310dffa96e2a3a40e69774351140bb7185e1202cdcc917589f95e16bb");
    static byte[]   outputSha3_384 = Hex.decode("68d2dcf7fd4ddd0a2240c8a437305f61fb7334cfb5d0226e1bc27dc10a2e723a20d370b47743130e26ac7e3d532886bd");
    static byte[]   outputSha3_512 = Hex.decode("eb3fbd4b2eaab8f5c504bd3a41465aacec15770a7cabac531e482f860b5ec7ba47ccb2c6f2afce8f88d22b6dc61380f23a668fd3888bb80537c0a0b86407689e");

    public HMacTest()
    {
    }

    public void testHMac(
        String  hmacName,
        byte[]  output)
        throws Exception
    {
        SecretKey           key = new SecretKeySpec(keyBytes, hmacName);
        byte[]              out;
        Mac                 mac;

        mac = Mac.getInstance(hmacName, "BC");

        mac.init(key);

        mac.reset();

        mac.update(message, 0, message.length);

        out = mac.doFinal();

        if (!areEqual(out, output))
        {
            fail("Failed - expected " + new String(Hex.encode(output)) + " got " + new String(Hex.encode(out)));
        }

        // no key generator for the old algorithms
        if (hmacName.startsWith("Old"))
        {
            return;
        }

        KeyGenerator kGen = KeyGenerator.getInstance(hmacName, "BC");

        mac.init(kGen.generateKey());

        mac.update(message);

        out = mac.doFinal();
    }

    public void testHMac(
        String  hmacName,
        int     defKeySize,
        byte[]  output)
        throws Exception
    {
        SecretKey           key = new SecretKeySpec(keyBytes, hmacName);
        byte[]              out;
        Mac                 mac;

        mac = Mac.getInstance(hmacName, "BC");

        mac.init(key);

        mac.reset();

        mac.update(message, 0, message.length);

        out = mac.doFinal();

        if (!areEqual(out, output))
        {
            fail("Failed - expected " + new String(Hex.encode(output)) + " got " + new String(Hex.encode(out)));
        }

        KeyGenerator kGen = KeyGenerator.getInstance(hmacName, "BC");

        SecretKey secretKey = kGen.generateKey();

        mac.init(secretKey);

        mac.update(message);

        out = mac.doFinal();

        isTrue("default key wrong length", secretKey.getEncoded().length == defKeySize / 8);
    }


    private void testExceptions()
        throws Exception
    {
        Mac mac = null;

        mac = Mac.getInstance("HmacSHA1", "BC");

        byte [] b = {(byte)1, (byte)2, (byte)3, (byte)4, (byte)5};
        SecretKeySpec sks = new SecretKeySpec(b, "HmacSHA1");
        RC5ParameterSpec algPS = new RC5ParameterSpec(100, 100, 100);

        try
        {
            mac.init(sks, algPS);
        }
        catch (InvalidAlgorithmParameterException e)
        {
            // ignore okay
        }

        try
        {
            mac.init(null, null);
        }
        catch (InvalidKeyException e)
        {
            // ignore okay
        }
        catch (InvalidAlgorithmParameterException e)
        {
            // ignore okay
        }

        try
        {
            mac.init(null);
        }
        catch (InvalidKeyException e)
        {
            // ignore okay
        }
    }

    public void performTest()
        throws Exception
    {
        testHMac("HMac-SHA1", 160, output1);
        testHMac("HMac-MD5", outputMD5);
        testHMac("HMac-MD4", outputMD4);
        testHMac("HMac-MD2", outputMD2);
        testHMac("HMac-SHA224", 224, output224);
        testHMac("HMac-SHA256", 256, output256);
        testHMac("HMac-SHA384", 384, output384);
        testHMac("HMac-SHA512", 512, output512);
        testHMac("HMac-SHA512/224", output512_224);
        testHMac("HMac-SHA512/256", output512_256);
        testHMac("HMac-RIPEMD128", 128, outputRipeMD128);
        testHMac("HMac-RIPEMD160", 160, outputRipeMD160);
        testHMac("HMac-TIGER", 192, outputTiger);
        testHMac("HMac-KECCAK224", 224, outputKck224);
        testHMac("HMac-KECCAK256", 256, outputKck256);
        testHMac("HMac-KECCAK288", 288, outputKck288);
        testHMac("HMac-KECCAK384", 384, outputKck384);
        testHMac("HMac-KECCAK512", 512, outputKck512);
        testHMac("HMac-SHA3-224", 224, outputSha3_224);
        testHMac("HMac-SHA3-256", 256, outputSha3_256);
        testHMac("HMac-SHA3-384", 384, outputSha3_384);
        testHMac("HMac-SHA3-512", 512, outputSha3_512);

        testHMac("HMac/SHA1", output1);
        testHMac("HMac/MD5", outputMD5);
        testHMac("HMac/MD4", outputMD4);
        testHMac("HMac/MD2", outputMD2);
        testHMac("HMac/SHA224", 224, output224);
        testHMac("HMac/SHA256", 256, output256);
        testHMac("HMac/SHA384", 384, output384);
        testHMac("HMac/SHA512", 512, output512);
        testHMac("HMac/RIPEMD128", 128, outputRipeMD128);
        testHMac("HMac/RIPEMD160", 160, outputRipeMD160);
        testHMac("HMac/TIGER", 192, outputTiger);
        testHMac("HMac/KECCAK224", 224, outputKck224);
        testHMac("HMac/KECCAK256", 256, outputKck256);
        testHMac("HMac/KECCAK288", 288, outputKck288);
        testHMac("HMac/KECCAK384", 384, outputKck384);
        testHMac("HMac/KECCAK512", 512, outputKck512);
        testHMac("HMac/SHA3-224", 224, outputSha3_224);
        testHMac("HMac/SHA3-256", 256, outputSha3_256);
        testHMac("HMac/SHA3-384", 384, outputSha3_384);
        testHMac("HMac/SHA3-512", 512, outputSha3_512);

        testHMac(PKCSObjectIdentifiers.id_hmacWithSHA1.getId(), 160, output1);
        testHMac(PKCSObjectIdentifiers.id_hmacWithSHA224.getId(), 224, output224);
        testHMac(PKCSObjectIdentifiers.id_hmacWithSHA256.getId(), 256, output256);
        testHMac(PKCSObjectIdentifiers.id_hmacWithSHA384.getId(), 384, output384);
        testHMac(PKCSObjectIdentifiers.id_hmacWithSHA512.getId(), 512, output512);
        testHMac(IANAObjectIdentifiers.hmacSHA1.getId(), 160, output1);
        testHMac(IANAObjectIdentifiers.hmacMD5.getId(), outputMD5);
        testHMac(IANAObjectIdentifiers.hmacRIPEMD160.getId(), 160, outputRipeMD160);
        testHMac(IANAObjectIdentifiers.hmacTIGER.getId(), 192, outputTiger);

        testHMac(NISTObjectIdentifiers.id_hmacWithSHA3_224.getId(), 224, outputSha3_224);
        testHMac(NISTObjectIdentifiers.id_hmacWithSHA3_256.getId(), 256, outputSha3_256);
        testHMac(NISTObjectIdentifiers.id_hmacWithSHA3_384.getId(), 384, outputSha3_384);
        testHMac(NISTObjectIdentifiers.id_hmacWithSHA3_512.getId(), 512, outputSha3_512);

        // test for compatibility with broken HMac.
        testHMac("OldHMacSHA384", outputOld384);
        testHMac("OldHMacSHA512", outputOld512);

        testExceptions();
    }

    public String getName()
    {
        return "HMac";
    }

    public static void main(
        String[]    args)
    {
        Security.addProvider(new BouncyCastleProvider());

        runTest(new HMacTest());
    }
}
