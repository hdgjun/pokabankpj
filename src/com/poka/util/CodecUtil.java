/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.poka.util;

/*
 * @author Administrator
 */
public final class CodecUtil {

    static CRC16 crc16 = new CRC16();
    private static byte[] tem = new byte[10000];
    private static int pyl = 0;
    private CodecUtil() {
    }

    public static byte[] short2bytes(short s) {
        byte[] bytes = new byte[2];
        for (int i = 1; i >= 0; i--) {
            bytes[i] = (byte) (s % 256);
            s >>= 8;
        }
        return bytes;
    }

    public static short bytes2short(byte[] bytes) {
        short s = (short) (bytes[1] & 0xFF);
        s |= (bytes[0] << 8) & 0xFF00;
        return s;
    }

    /*
     * 获取crc校验的byte形式
     */
    public static byte[] crc16Bytes(byte[] data) {
        return short2bytes(crc16Short(data));
    }

    /*
     * 获取crc校验的short形式
     */
    public static short crc16Short(byte[] data) {
        return crc16.getCrc(data);
    }

    public static void main(String[] args) {
        byte[] test = new byte[] {0, 1, 2, 3, 4};
//        byte[] crc = crc16Bytes(test);
//
//        byte[] testc = new byte[test.length + 2];
//        for (int i = 0; i < test.length; i++) {
//            testc[i] = test[i];
//        }
//        testc[test.length] = crc[0];
//        testc[test.length + 1] = crc[1];
//
//        System.out.println(crc16Short(testc));        
//        ScannGunListenClient client;
//        try {
//            client = new ScannGunListenClient("COM17");
//            int len = 0;      
//            while((len  = client.getInStream().read(tem, pyl,1)) != -1){
//                pyl++;
//                System.out.println(len);
//                System.out.println(StringUtil.byteToHexString(tem, 0, tem.length));
//            }            
//            StringUtil.byteToHexString(tem, 0, len);            
//            //client.close();            
//            int tems = 0;            
//            int temp = 0;            
//            for (int i = 2; i < len - 2; i++) {                
//                temp += tem[i];                
//                tems = tems + (temp % 0x10000);            
//            }            
//            byte[] crc = new byte[2];            
//            crc[0] = (byte) (tems & 0x000000FF);            
//            crc[1] = (byte) (tems & 0x000000FF);   
//            //System.out.println(crc16Short(tem));
//            byte[] mybyte = new byte[256];
//           
//            
//            
//            
//            
//            
//            
//<<<<<<< .mine=======            
//            
//            
//>>>>>>> .theirs        } catch (PortInUseException ex) {
//            Logger.getLogger(CodecUtil.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (IOException ex) {
//            Logger.getLogger(CodecUtil.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (UnsupportedCommOperationException ex) {
//            Logger.getLogger(CodecUtil.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (ClassNotFoundException ex) {
//            Logger.getLogger(CodecUtil.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (InstantiationException ex) {
//            Logger.getLogger(CodecUtil.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (IllegalAccessException ex) {
//            Logger.getLogger(CodecUtil.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (NoSuchPortException ex) {
//            Logger.getLogger(CodecUtil.class.getName()).log(Level.SEVERE, null, ex);
//        }        
//
        }
}
