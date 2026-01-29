package com.orosirian.trade.coupon.utils.code;

import lombok.Data;

@Data
public class ByteHelper {

    private byte[] bytes;   // 原始byte数组

    private int index;      // 记录当前写入到多少位

    public ByteHelper(int capacity){
        bytes = new byte[capacity];
        index = 0;
    }

    // 向数组中追加内容
    public ByteHelper appendNumber(long val){
        byte[] arr = Number2byte(val);
        return appendBytes(arr);
    }
    public ByteHelper appendNumber(int val){
        byte[] arr = Number2byte(val);
        return appendBytes(arr);
    }
    public ByteHelper appendNumber(short val){
        byte[] arr = Number2byte(val);
        return appendBytes(arr);
    }
    public ByteHelper appendNumber(byte val){
        byte[] arr = new byte[]{val};
        return appendBytes(arr);
    }
    public ByteHelper appendBytes(byte[] arr){
        for(byte i = 0 ; i < arr.length ; i ++){
            bytes[index + i] = arr[i];
        }
        index += arr.length;
        return this;
    }

    // 获取数据的总和
    public int getSum(){
        int ret = 0;
        for (byte aByte : bytes) {
            ret += aByte;
        }
        return ret;
    }

    public byte getByteByPos(int pos) {
        return bytes[pos];
    }

    public void setByteByPos(int pos, byte data) {
        bytes[pos] = data;
    }

    // 将数字转换为byte数组
    public static byte[] Number2byte(long val) {
        return new byte[]{
                (byte) ((val >> 56) & 0xFF),
                (byte) ((val >> 48) & 0xFF),
                (byte) ((val >> 40) & 0xFF),
                (byte) ((val >> 32) & 0xFF),
                (byte) ((val >> 24) & 0xFF),
                (byte) ((val >> 16) & 0xFF),
                (byte) ((val >> 8) & 0xFF),
                (byte) (val & 0xFF)
        };
    }
    public static byte[] Number2byte(int val) {
        return new byte[]{
                (byte) ((val >> 24) & 0xFF),
                (byte) ((val >> 16) & 0xFF),
                (byte) ((val >> 8) & 0xFF),
                (byte) (val & 0xFF)
        };
    }
    public static byte[] Number2byte(short val) {
        return new byte[]{
                (byte) ((val >> 8) & 0xFF),
                (byte) (val & 0xFF)
        };
    }

}
