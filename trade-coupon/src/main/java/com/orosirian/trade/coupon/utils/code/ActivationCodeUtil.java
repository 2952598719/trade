package com.orosirian.trade.coupon.utils.code;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class ActivationCodeUtil {

    static String stringTable = "ABCDEFGHIJKMNPQRSTUVWXYZ23456789";     // 没有O/0，L/1，防止混淆

    final static String password = "dak3le2";

    final static int convertByteCount = 5;  // 32个字符，需要5bit定位

    public static void main(String[] args) {
        ShowTime();
        System.out.println("=======================");
        List<String> strings = createCodeList(88, 10, 12, password);
        for(String code:strings){
            System.out.println(code);
        }
        System.out.println(VerifyCode("2CZNXWNM2KZA"));
        System.out.println(VerifyCode("HIYGXFWM74MA"));
        System.out.println(VerifyCode("CUT2W36ICK4A"));
        System.out.println(VerifyCode("M3MKCPJFMBBA"));
        System.out.println(VerifyCode("M3MKCPJFMTVA"));
    }

    public static void ShowTime() {
        Instant time = Instant.now();
        System.out.println("current time: " + time.toString());
    }

    public static List<String> createCodeList(int groupId, int codeNum, int codeLength, String password) {
        // code组成：groupId-1b，codeId-4b，随机码-nb，校验码-1b

        List<String> codes = new ArrayList<>();
        int totalByteCount = codeLength * convertByteCount / 8;     // 共需多少byte
        int randByteCount = totalByteCount - 6;     // 有多少byte是随机码
        if (randByteCount <= 0)  {
            return codes;   // 随机码用来防止暴力遍历，如果没有空间放随机码，就干脆不生成
        }

        for (int i = 0; i <= codeNum - 1; i++) {  // 需要codeNum个激活码
            // 1 生成n位随机码
            byte[] randBytes = new byte[randByteCount];
            for(int j = 0; j <= randByteCount - 1; j++) {
                randBytes[j] = (byte)(Math.random() * Byte.MAX_VALUE);
            }

            // 2 存储所有数据
            ByteHelper byteHelper = new ByteHelper(totalByteCount);
            byteHelper.appendNumber((byte) groupId)
                    .appendNumber(i)    // 使用i作为code的id
                    .appendBytes(randBytes);
            byte verify = (byte) (byteHelper.getSum() % Byte.MAX_VALUE);    // 校验码: 所有数据相加的总和与byte.max=127取余
            byteHelper.appendNumber(verify);

            // 3 加密
            // 3.1 对groupId/codeId进行异或
            for(int j = 0 ; j <= 4 ; j ++) {
                byte processedByte = (byte) (byteHelper.getByteByPos(j) ^ byteHelper.getByteByPos(5 + j % randByteCount));
                byteHelper.setByteByPos(j, processedByte);
            }
            // 3.2 对所有字节进行异或
            byte[] passwordBytes = password.getBytes();
            for (int j = 0; j <= byteHelper.getBytes().length - 1; j++) {
                byte processedByte = (byte) (byteHelper.getByteByPos(j) ^ passwordBytes[j % passwordBytes.length]);
            }

            // 4 按6位一组复制给最终数组
            byte[] bytes = new byte[codeLength];
            for (int j = 0; j <= byteHelper.getBytes().length - 1; j++) {
                for (int k = 0; k <= 7; k++) {
                    int sourceIndex = j * 8 + k;    // 第j个字节的第k位
                    int targetIndex_x = sourceIndex / convertByteCount;
                    int targetIndex_y = sourceIndex % convertByteCount;
                    byte placeVal = (byte) Math.pow(2, k);
                    byte val = (byte) ((byteHelper.getByteByPos(j) & placeVal) == placeVal ? 1 : 0);
                    bytes[targetIndex_x] = (byte) (bytes[targetIndex_x] | (val << targetIndex_y));
                }
            }

            // 5 生成字符串
            StringBuilder result = new StringBuilder();
            for (byte b : bytes) {
                result.append(stringTable.charAt(b));
            }
            codes.add(result.toString());
        }
        ShowTime();
        return codes;
    }

    public static boolean VerifyCode(String code) {
        byte[] originalBytes = new byte[code.length()];
        // 1 遍历字符串，从字符表中获取相应的二进制数据
        for(int i = 0;i <= code.length() - 1; i++){
            byte index = (byte) stringTable.indexOf(code.charAt(i));
            originalBytes[i] = index;
        }
        // 2 还原数组
        int totalByteCount = code.length() * convertByteCount / 8;
        int randByteCount = totalByteCount - 6;
        byte[] bytes = new byte[totalByteCount];
        for(int j = 0 ; j <= bytes.length - 1; j++) {
            for(int k = 0 ; k <= 7; k++) {
                int sourceIndex = j * 8 + k;
                int targetIndex_x = sourceIndex / convertByteCount;
                int targetIndex_y = sourceIndex % convertByteCount;
                byte placeVal = (byte)Math.pow(2, targetIndex_y);
                byte val = (byte)((originalBytes[targetIndex_x] & placeVal) == placeVal ? 1:0);
                bytes[j] = (byte) (bytes[j] | (val << k));
            }
        }

        // 3 解密
        // 3.1 使用密码与所有数据进行异或
        byte[] passwordBytes = password.getBytes();
        for(int j = 0 ; j <= bytes.length - 1; j++){
            bytes[j] = (byte) (bytes[j] ^ passwordBytes[j % passwordBytes.length]);
        }
        // 3.2 使用随机码与时间和ID进行异或
        for(int j = 0 ; j <= 4; j++) {
            bytes[j] = (byte) (bytes[j] ^ (bytes[5 + j % randByteCount]));
        }

        // 4 验证
        // 4.1 获取校验码
        int sum = 0;
        for(int i = 0 ; i <= bytes.length - 2; i++){
            sum += bytes[i];
        }
        byte verify = (byte) (sum % Byte.MAX_VALUE);
        // 4.2 校验
        if(verify == bytes[bytes.length - 1]){
            System.out.println(code + " : verify success!");
            return true;
        }else {
            System.out.println(code + " : verify failed!");
            return false;
        }
    }

}
