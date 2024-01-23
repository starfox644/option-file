package pesoptionfile;

public class Utils {

	/**
	 * 		Casts the integer to a byte. The byte is formed as an unsigned integer,
	 * 		i.e. -1 represents the highest value, which is 255.
	 * @param i	Integer to cast.
	 * @return A byte containing the value of i.
	 */
	public static byte integerToUnsignedByte(int i) {
		byte b;
		if(i > 127)
			b = (byte)(i - 256);
		else
			b = (byte)i;
		return b;
	}

	/**
	 * 		Casts the byte as an integer. Interprets the bits as an unsigned integer, i.e,
	 * 		-1 represents the highest value, which is 255.
	 * @param b	Byte to cast.
	 * @return An integer containing the value of b.
	 */
	public static int UnsignedbyteToInt(byte b) {
		int i = b;
		if(i < 0)
			i += 256;
		return i;
	}
	
	/**
	 * 		Returns a 2-bytes array containing the two less significant bytes of the given integer.
	 * @param i	Integer to store in byte array.
	 * @return	A 2-bytes array corresponding to the integer.
	 */
	public static byte[] intTo2bytes(int i) {
		byte abyte0[] = new byte[2];
		abyte0[0] = integerToUnsignedByte(i & 0xff);
		abyte0[1] = integerToUnsignedByte(i >>> 8 & 0xff);
		return abyte0;
	}
	
	/**
	 * 		Returns a 4-bytes array containing the bytes of the given integer.
	 * @param i	Integer to store in byte array.
	 * @return	A 4-bytes array corresponding to the integer.
	 */
	public static byte[] intTo4bytes(int i) {
		byte abyte0[] = new byte[4];
		abyte0[0] = integerToUnsignedByte(i & 0xff);
		abyte0[1] = integerToUnsignedByte(i >>> 8 & 0xff);
		abyte0[2] = integerToUnsignedByte(i >>> 16 & 0xff);
		abyte0[3] = integerToUnsignedByte(i >>> 24 & 0xff);
		return abyte0;
	}
}
