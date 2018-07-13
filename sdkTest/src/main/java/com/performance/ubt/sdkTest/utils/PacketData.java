package com.performance.ubt.sdkTest.utils;

/**
 * 封装格式化java数据发往C++ socket
 * editor:chenlin
 * date:2014-09-01
 * 总结高低位只有整型和浮点型要转高低位int、long、float、short、double等(byte不用)
 * java高位在前，C++ 低位在前
 * Java和一些windows编程语言如c、c++、delphi所写的网络程序进行通讯时，需要进行相应的转换 
 * 高、低字节之间的转换 
 * windows的字节序为低字节开头 
 * linux,unix的字节序为高字节开头 
 * java则无论平台变化，都是高字节开头
 */
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

public class PacketData implements Serializable{

	private int nCapacity;
	private int nPosition;
	public ByteBuffer buffer;

	/**
	 * 构造
	 * 
	 * @param Capacity
	 *            容量
	 */
	public PacketData(int Capacity) {
		nCapacity = Capacity;// 3*1024;
		nPosition = 0;
		buffer = ByteBuffer.allocate(nCapacity);
		buffer.clear();
	}

	/**
	 * 重新分配buf容量
	 * 
	 * @param nlen
	 *            新增长度
	 * @return
	 */
	private boolean Allocate(int nlen) {
		if (nPosition + nlen > nCapacity) {
			byte[] temp = buffer.array();
			nCapacity = nCapacity + nlen;
			buffer = ByteBuffer.allocate(nCapacity);
			buffer.put(temp);
			buffer.position(nPosition);

		}
		return true;
	}

	/**
	 * 添加一个int(4个字节)
	 * 
	 * @param n
	 */
	public void putInt(int n) {
		Allocate(4);
		buffer.put(toLH(n));
		nPosition += 4;
	}
	
	/**
	 * 添加一个int(4个字节)
	 * 
	 * @param n
	 */
	public void putInt_(int n) {
		Allocate(4);
		buffer.putInt(n);
//		buffer.put(toHL(n));
		nPosition += 4;
	}
	
	/**
	 * 添加一个long(8个字节)
	 * 
	 * @param n
	 */
	public void putLong(Long n) {
		Allocate(8);
		buffer.put(toLH(n));
		nPosition += 8;
	}

	/**
	 * 添加一个byte(1个字节)
	 * 
	 * @param n
	 */
	public void putByte(byte n) {
		Allocate(1);
		buffer.put(n);
		nPosition += 1;
	}
	
	/**
	 * 添加一个byte(1个字节)
	 * 
	 * @param n
	 */
	public void putBytes(byte[] n) {
		Allocate(n.length);
		buffer.put(n);
		nPosition += n.length;
	}

	/**
	 * 添加一个float(4个字节)
	 * 
	 * @param n
	 */
	public void putFloat(float n) {
		Allocate(4);
		buffer.put(toLH(n));
		nPosition += 4;
	}
	
//	/**
//	 * 添加一个float(4个字节)
//	 * 
//	 * @param n
//	 */
//	public void putString(String str) {
//		byte buf[] = stringToBytes( str );
//		int nLen = buf.length;
//		Allocate( nLen );
//		buffer.put( buf );
//		nPosition += nLen;
//	}
	
	/**
	 * 添加一个String(N个字节)
	 * 
	 * @param str
	 */
	public int putString(String str) {
		if(str == null || "".equals(str) ){
			putShort((short)0);
			return 0;
		}else {
			str.trim();
			byte buf[] = stringToBytes( str ,str.length());
			if(null != buf){
				int nLen = buf.length;
				putShort((short)nLen);
				
				Allocate( nLen );
				buffer.put( buf );
				nPosition += nLen;
				
				return nLen + 2;
			}else {
				putShort((short)0);
				return 0;
			}
		}
	}
	
	/** 
	  * 将byte数组转化成String 
	    */  
	   private static String toStr(byte[] valArr,int maxLen) {  
	        int index = 0;  
	        while(index < valArr.length && index < maxLen) {  
	            if(valArr[index] == 0) {  
	                break;  
	            }  
	            index++;  
	        }  
	       byte[] temp = new byte[index];  
	        System.arraycopy(valArr, 0, temp, 0, index);  
	        return new String(temp);  
	    } 
	
	/**
	 * 添加一个Short(2个字节)
	 * 
	 * @param nData
	 */
	public void putShort(Short nData) {
		Allocate( 2 );
		buffer.put( PacketData.toLH(nData) );
		nPosition += 2;
	}
	
	/**
	 * 添加一个Short(2个字节)
	 * 
	 * @param nData
	 */
	public void putShort_(Short nData) {
		Allocate( 2 );
		buffer.put( PacketData.toHH(nData) );
		nPosition += 2;
	}

	public byte[] getBuffer() {
		byte[] temp = buffer.array();
		byte[] buf = new byte[nPosition];
		System.arraycopy(temp, 0, buf, 0, nPosition);
		return buf;
		// return buffer.array();
	}



	/**
	 * 将int转为低字节在前，高字节在后的byte数组
	 * 
	 * @param n 
	 * @return
	 */
	public static byte[] toLH(int n) {
		byte[] b = new byte[4];
		b[0] = (byte) (n & 0xff);
		b[1] = (byte) (n >> 8 & 0xff);
		b[2] = (byte) (n >> 16 & 0xff);
		b[3] = (byte) (n >> 24 & 0xff);
		return b;
	}
	public static byte[] toHL(int n) {
		byte[] b = new byte[4];
		b[3] = (byte) (n & 0xff);
		b[2] = (byte) (n >> 8 & 0xff);
		b[1] = (byte) (n >> 16 & 0xff);
		b[0] = (byte) (n >> 24 & 0xff);
		return b;
	}
	
	/**
	 * 将long转为低字节在前，高字节在后的byte数组
	 * 
	 * @param n
	 * @return
	 */
	public static byte[] toLH(long n){
		byte[] b = new byte[8];
		b[0] = (byte) (n & 0xff);
		b[1] = (byte) (n >> 8 & 0xff);
		b[2] = (byte) (n >> 16 & 0xff);
		b[3] = (byte) (n >> 24 & 0xff);
		b[4] = (byte) (n >> 32 & 0xff);
		b[5] = (byte) (n >> 40 & 0xff);
		b[6] = (byte) (n >> 48 & 0xff);
		b[7] = (byte) (n >> 56 & 0xff);
		return b;
	}

	/**
	 * 将float转为低字节在前，高字节在后的byte数组
	 */
	private static byte[] toLH(float f) {
		return toLH(Float.floatToRawIntBits(f));
	}

	/**
	 * 将short转为低字节在前，高字节在后的byte数组
	 */
	public static byte[] toLH(short n) {
		byte[] b = new byte[2];
		b[0] = (byte) (n & 0xff);
		b[1] = (byte) (n >> 8 & 0xff);
		return b;
	}

	/**
	 * 将short转为高字节在前，低字节在后的byte数组
	 * 
	 * @param n
	 *            short
	 * @return byte[]
	 */
	public static byte[] toHH(short n) {
		byte[] b = new byte[2];
		b[1] = (byte) (n & 0xff);
		b[0] = (byte) (n >> 8 & 0xff);
		return b;
	}

	/**
	 * 将int转为高字节在前，低字节在后的byte数组
	 * 
	 * @param n
	 *            int
	 * @return byte[]
	 */
	public static byte[] toHH(int n) {
		byte[] b = new byte[4];
		b[3] = (byte) (n & 0xff);
		b[2] = (byte) (n >> 8 & 0xff);
		b[1] = (byte) (n >> 16 & 0xff);
		b[0] = (byte) (n >> 24 & 0xff);
		return b;
	}

	/**
	 * 将String转为byte数组
	 */
	public static byte[] stringToBytes(String s, int length) {
		while (s.getBytes().length < length) {
			s += " ";
		}
		return s.getBytes();
	}

	/**
	 * 将字节数组转换为String
	 * 
	 * @param b
	 *            byte[]
	 * @return String
	 */
	public static String bytesToString(byte[] b) {
		StringBuffer result = new StringBuffer("");
		int length = b.length;
		for (int i = 0; i < length; i++) {
			result.append((char) (b[i] & 0xff));
		}
		return result.toString();
	}

	/**
	 * 将字符串转换为byte数组
	 * 
	 * @param s
	 *            String
	 * @return byte[]
	 */
	public static byte[] stringToBytes(String s) {
		try {
			return s.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 将高字节数组转换为int
	 * 
	 * @param b
	 *            byte[]
	 * @return int
	 */
	public static int hBytesToInt(byte[] b) {
		int s = 0;
		for (int i = 0; i < 3; i++) {
			if (b[i] >= 0) {
				s = s + b[i];
			} else {
				s = s + 256 + b[i];
			}
			s = s * 256;
		}
		if (b[3] >= 0) {
			s = s + b[3];
		} else {
			s = s + 256 + b[3];
		}
		return s;
	}

	/**
	 * 将低字节数组转换为int
	 * 
	 * @param b
	 *            byte[]
	 * @return int
	 */
	public static int lBytesToInt(byte[] b) {
		int s = 0;
		for (int i = 0; i < 3; i++) {
			if (b[3 - i] >= 0) {
				s = s + b[3 - i];
			} else {
				s = s + 256 + b[3 - i];
			}
			s = s * 256;
		}
		if (b[0] >= 0) {
			s = s + b[0];
		} else {
			s = s + 256 + b[0];
		}
		return s;
	}

	/**
	 * 高字节数组到short的转换
	 * 
	 * @param b
	 *            byte[]
	 * @return short
	 */
	public static short hBytesToShort(byte[] b) {
		int s = 0;
		if (b[0] >= 0) {
			s = s + b[0];
		} else {
			s = s + 256 + b[0];
		}
		s = s * 256;
		if (b[1] >= 0) {
			s = s + b[1];
		} else {
			s = s + 256 + b[1];
		}
		short result = (short) s;
		return result;
	}

	/**
	 * 低字节数组到short的转换
	 * 
	 * @param b
	 *            byte[]
	 * @return short
	 */
	public static short lBytesToShort(byte[] b) {
		int s = 0;
		if (b[1] >= 0) {
			s = s + b[1];
		} else {
			s = s + 256 + b[1];
		}
		s = s * 256;
		if (b[0] >= 0) {
			s = s + b[0];
		} else {
			s = s + 256 + b[0];
		}
		short result = (short) s;
		return result;
	}

	/**
	 * 高字节数组转换为float
	 * 
	 * @param b
	 *            byte[]
	 * @return float
	 */
	public static float hBytesToFloat(byte[] b) {
		int i = 0;
		Float F = new Float(0.0);
		i = ((((b[0] & 0xff) << 8 | (b[1] & 0xff)) << 8) | (b[2] & 0xff)) << 8
				| (b[3] & 0xff);
		return F.intBitsToFloat(i);
	}

	/**
	 * 低字节数组转换为float
	 * 
	 * @param b
	 *            byte[]
	 * @return float
	 */
	public static float lBytesToFloat(byte[] b) {
		int i = 0;
		Float F = new Float(0.0);
		i = ((((b[3] & 0xff) << 8 | (b[2] & 0xff)) << 8) | (b[1] & 0xff)) << 8
				| (b[0] & 0xff);
		return F.intBitsToFloat(i);
	}

	/**
	 * 将byte数组中的元素倒序排列
	 */
	public static byte[] bytesReverseOrder(byte[] b) {
		int length = b.length;
		byte[] result = new byte[length];
		for (int i = 0; i < length; i++) {
			result[length - i - 1] = b[i];
		}
		return result;
	}

	/**
	 * 打印byte数组
	 */
	public static void printBytes(byte[] bb) {
		int length = bb.length;
	}

	public static void logBytes(byte[] bb) {
		int length = bb.length;
		String out = "";
		for (int i = 0; i < length; i++) {
			out = out + bb + " ";
		}
	}

	/**
	 * 将int类型的值转换为字节序颠倒过来对应的int值
	 * @param i int
	 * @return int
	 */
	public static int reverseInt(int i) {
		int result = PacketData.hBytesToInt(PacketData.toLH(i));
		return result;
	}

	/**
	 * 将short类型的值转换为字节序颠倒过来对应的short值
	 * @param s short
	 * @return short
	 */
	public static short reverseShort(short s) {
		short result = PacketData.hBytesToShort(PacketData.toLH(s));
		return result;
	}

	/**
	 * 将float类型的值转换为字节序颠倒过来对应的float值
	 * @param f float
	 * @return float
	 */
	public static float reverseFloat(float f) {
		float result = PacketData.hBytesToFloat(PacketData.toLH(f));
		return result;
	}
}