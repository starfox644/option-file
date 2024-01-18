import java.io.File;
import java.io.RandomAccessFile;
import java.util.Arrays;

public class OptionFile {
    public OptionFile() {
        optionFileData = new byte[LENGTH];
    }

    public void read(File file) {
        try {
            RandomAccessFile randomaccessfile = new RandomAccessFile(file, "r");
            randomaccessfile.read(optionFileData);
            randomaccessfile.close();
            convert(optionFileData);
            decrypt(optionFileData);
        }
        catch(Exception exception)
        {
            exception.printStackTrace();
        }
    }

    public void decryptedWrite(File file) {
        try {
            RandomAccessFile randomaccessfile = new RandomAccessFile(file, "rw");
            randomaccessfile.write(optionFileData);
            randomaccessfile.close();
        }
        catch(Exception exception)
        {
            exception.printStackTrace();
        }
    }

    public void encryptedWrite(File file) {
        byte[] encryptedData = Arrays.copyOf(optionFileData, optionFileData.length);
        encrypt(encryptedData);
        writeCheckSums(encryptedData);
        convert(encryptedData);

        try {
            RandomAccessFile randomaccessfile = new RandomAccessFile(file, "rw");
            randomaccessfile.write(encryptedData);
            randomaccessfile.close();
        }
        catch(Exception exception)
        {
            exception.printStackTrace();
        }
    }

    private static void convert(byte[] data) {
        int currentKeyIndex = 0;
        for(int currentByte = 0; currentByte < LENGTH; currentByte++)
        {
            data[currentByte] = (byte)(data[currentByte] ^ keyPC[currentKeyIndex]);
            if(currentKeyIndex < 255)
                currentKeyIndex++;
            else
                currentKeyIndex = 0;
        }
    }

    private static void decrypt(byte[] data) {
        for(int i = 1; i < 10; i++)
        {
            int j = 0;
            for(int k = block[i]; k + 4 <= block[i] + blockSize[i]; k += 4)
            {
                int l = OptionFile.bytesToInt(data, k);
                int i1 = (l - key[j]) + 0x7ab3684c ^ 0x7ab3684c;
                data[k] = intToByte(i1 & 0xff);
                data[k + 1] = intToByte(i1 >>> 8 & 0xff);
                data[k + 2] = intToByte(i1 >>> 16 & 0xff);
                data[k + 3] = intToByte(i1 >>> 24 & 0xff);
                if(++j == 446)
                    j = 0;
            }
        }
    }

    private static void encrypt(byte[] data) {
        for(int i = 1; i < 10; i++)
        {
            int j = 0;
            for(int k = block[i]; k + 4 <= block[i] + blockSize[i]; k += 4)
            {
                int l = bytesToInt(data, k);
                int i1 = key[j] + ((l ^ 0x7ab3684c) - 0x7ab3684c);
                data[k] = intToByte(i1 & 0xff);
                data[k + 1] = intToByte(i1 >>> 8 & 0xff);
                data[k + 2] = intToByte(i1 >>> 16 & 0xff);
                data[k + 3] = intToByte(i1 >>> 24 & 0xff);
                if(++j == 446)
                    j = 0;
            }

        }
    }

    private static void writeCheckSums(byte[] data) {
        for(int currentBlockIndex = 0; currentBlockIndex < 10; currentBlockIndex++)
        {
            int j = 0;
            for(int k = block[currentBlockIndex]; k + 4 <= block[currentBlockIndex] + blockSize[currentBlockIndex]; k += 4)
                j += bytesToInt(data, k);

            data[block[currentBlockIndex] - 8] = intToByte(j & 0xff);
            data[block[currentBlockIndex] - 7] = intToByte(j >>> 8 & 0xff);
            data[block[currentBlockIndex] - 6] = intToByte(j >>> 16 & 0xff);
            data[block[currentBlockIndex] - 5] = intToByte(j >>> 24 & 0xff);
        }
    }

    public static byte intToByte(int value) {
        byte result;
        if(value > 127)
            result = (byte)(value - 256);
        else
            result = (byte)value;
        return result;
    }

    public static int bytesToInt(byte value) {
        int result = value;
        if(result < 0)
            result += 256;
        return result;
    }

    private static int bytesToInt(byte[] data, int index) {
        int[] tmp = new int[4];
        tmp[0] = bytesToInt(data[index]);
        tmp[1] = bytesToInt(data[index + 1]);
        tmp[2] = bytesToInt(data[index + 2]);
        tmp[3] = bytesToInt(data[index + 3]);
        return tmp[0] | tmp[1] << 8 | tmp[2] << 16 | tmp[3] << 24;
    }

    public static final int LENGTH =  0x123000; // 1.2 Mo, file size

    public byte[] optionFileData;

    public static final int[] block =  {
            12, 5144, 9544, 14288, 37116, 0xa0a24, 0xb7770, 0xba79c, 0xde728, 0x11dc58
    };

    public static final int[] blockSize =  {
            4844, 1268, 4730, 22816, 0x975e0, 0x16d3d, 12320, 0x23f80, 0x3f524, 21032
    };

    private static final int[] key =  {
            0x7ab36882, 0x7ab3689e, 0x7ab368bd, 0x7ab3689f, 0x7ab368a4, 0x7ab368c3, 0x7ab36899, 0x7ab368b9, 0x7ab36894, 0x7ab368b5,
            0x7ab368af, 0x7ab3687d, 0x7ab368b4, 0x7ab368bf, 0x7ab368b0, 0x7ab36894, 0x7ab368b5, 0x7ab368c5, 0x7ab368c3, 0x7ab36897,
            0x7ab36890, 0x7ab3687d, 0x7ab3689e, 0x7ab368a5, 0x7ab3688e, 0x7ab368c4, 0x7ab368bd, 0x7ab3687d, 0x7ab368c3, 0x7ab3689c,
            0x7ab36881, 0x7ab368ad, 0x7ab36892, 0x7ab36883, 0x7ab368a2, 0x7ab368c2, 0x7ab3687d, 0x7ab368ad, 0x7ab36884, 0x7ab368c0,
            0x7ab36899, 0x7ab3688f, 0x7ab368b4, 0x7ab368b6, 0x7ab368b7, 0x7ab36885, 0x7ab368a5, 0x7ab3689e, 0x7ab3689d, 0x7ab368c3,
            0x7ab3689c, 0x7ab368a6, 0x7ab368c4, 0x7ab368bf, 0x7ab368a3, 0x7ab368bd, 0x7ab36898, 0x7ab3687f, 0x7ab368a1, 0x7ab36881,
            0x7ab3689a, 0x7ab36880, 0x7ab368a6, 0x7ab368be, 0x7ab368b2, 0x7ab368b6, 0x7ab368b9, 0x7ab36882, 0x7ab3687e, 0x7ab368a6,
            0x7ab368af, 0x7ab368a3, 0x7ab36891, 0x7ab3688e, 0x7ab3687f, 0x7ab368c4, 0x7ab368bd, 0x7ab368b4, 0x7ab36894, 0x7ab368b4,
            0x7ab368bd, 0x7ab368b8, 0x7ab3689f, 0x7ab368a4, 0x7ab368a4, 0x7ab368a3, 0x7ab368a4, 0x7ab368b2, 0x7ab368b0, 0x7ab36896,
            0x7ab36894, 0x7ab368be, 0x7ab368ae, 0x7ab3687d, 0x7ab368c2, 0x7ab368a2, 0x7ab368c4, 0x7ab36894, 0x7ab368c2, 0x7ab368b1,
            0x7ab368a5, 0x7ab368c3, 0x7ab36898, 0x7ab36894, 0x7ab3687f, 0x7ab368b3, 0x7ab3689c, 0x7ab368b5, 0x7ab368a6, 0x7ab368c5,
            0x7ab36884, 0x7ab368bd, 0x7ab36896, 0x7ab36882, 0x7ab368c6, 0x7ab3687e, 0x7ab368bd, 0x7ab368b2, 0x7ab368b3, 0x7ab36890,
            0x7ab36880, 0x7ab368b7, 0x7ab36895, 0x7ab3689b, 0x7ab36894, 0x7ab368a5, 0x7ab368be, 0x7ab368bf, 0x7ab368c2, 0x7ab368be,
            0x7ab368a1, 0x7ab368a4, 0x7ab3689d, 0x7ab36899, 0x7ab368a4, 0x7ab368b4, 0x7ab368b8, 0x7ab3688f, 0x7ab368a3, 0x7ab36881,
            0x7ab36891, 0x7ab368bd, 0x7ab368b9, 0x7ab368b3, 0x7ab368b9, 0x7ab3688e, 0x7ab36895, 0x7ab36896, 0x7ab36891, 0x7ab36892,
            0x7ab3687f, 0x7ab368b4, 0x7ab368bb, 0x7ab3689d, 0x7ab3688e, 0x7ab36882, 0x7ab368c1, 0x7ab368a0, 0x7ab36891, 0x7ab368a3,
            0x7ab368af, 0x7ab368bf, 0x7ab368b0, 0x7ab3687c, 0x7ab368c4, 0x7ab36885, 0x7ab368a4, 0x7ab3689d, 0x7ab368c6, 0x7ab368b4,
            0x7ab36898, 0x7ab3687c, 0x7ab36899, 0x7ab368b8, 0x7ab368c5, 0x7ab3689a, 0x7ab36895, 0x7ab368be, 0x7ab36882, 0x7ab3687d,
            0x7ab3687d, 0x7ab3687f, 0x7ab3687f, 0x7ab368ba, 0x7ab36890, 0x7ab368b3, 0x7ab368af, 0x7ab3688f, 0x7ab36893, 0x7ab36894,
            0x7ab36881, 0x7ab368bc, 0x7ab368b2, 0x7ab368c3, 0x7ab368b3, 0x7ab368b1, 0x7ab368ba, 0x7ab368b1, 0x7ab368c4, 0x7ab368c4,
            0x7ab368be, 0x7ab368c6, 0x7ab368b3, 0x7ab368bf, 0x7ab368ba, 0x7ab368bd, 0x7ab368b5, 0x7ab368ad, 0x7ab3689d, 0x7ab36885,
            0x7ab3688d, 0x7ab368b2, 0x7ab36882, 0x7ab368a6, 0x7ab368b8, 0x7ab368b2, 0x7ab368c1, 0x7ab368b6, 0x7ab36891, 0x7ab3689d,
            0x7ab368a2, 0x7ab3687c, 0x7ab36882, 0x7ab368a5, 0x7ab368c5, 0x7ab3687d, 0x7ab368c5, 0x7ab368c1, 0x7ab368a2, 0x7ab36883,
            0x7ab368be, 0x7ab368b7, 0x7ab368b0, 0x7ab36895, 0x7ab36884, 0x7ab368c2, 0x7ab3687e, 0x7ab3687c, 0x7ab3687f, 0x7ab368bc,
            0x7ab368a3, 0x7ab3689b, 0x7ab3689d, 0x7ab36895, 0x7ab36882, 0x7ab368b5, 0x7ab3688f, 0x7ab368a2, 0x7ab36884, 0x7ab368bc,
            0x7ab368ba, 0x7ab368bd, 0x7ab368c0, 0x7ab3688e, 0x7ab36897, 0x7ab36883, 0x7ab3687f, 0x7ab368b1, 0x7ab3688e, 0x7ab368b4,
            0x7ab36880, 0x7ab368a0, 0x7ab368a0, 0x7ab3689d, 0x7ab3687f, 0x7ab36880, 0x7ab368b0, 0x7ab368b4, 0x7ab368c6, 0x7ab368b9,
            0x7ab368b0, 0x7ab36898, 0x7ab368a5, 0x7ab368a4, 0x7ab3688d, 0x7ab368c2, 0x7ab3687c, 0x7ab36881, 0x7ab36885, 0x7ab368ad,
            0x7ab36890, 0x7ab36881, 0x7ab368a3, 0x7ab36895, 0x7ab36899, 0x7ab368b8, 0x7ab368a0, 0x7ab368b2, 0x7ab368b7, 0x7ab368ad,
            0x7ab36881, 0x7ab3687c, 0x7ab368b3, 0x7ab368ae, 0x7ab36896, 0x7ab368b7, 0x7ab36896, 0x7ab3688e, 0x7ab3689e, 0x7ab368b6,
            0x7ab368b8, 0x7ab368bd, 0x7ab36894, 0x7ab36893, 0x7ab368bc, 0x7ab368bc, 0x7ab3689c, 0x7ab368a6, 0x7ab36885, 0x7ab368b8,
            0x7ab368ad, 0x7ab36896, 0x7ab368b9, 0x7ab368be, 0x7ab368ae, 0x7ab368c0, 0x7ab368a6, 0x7ab36881, 0x7ab368b5, 0x7ab368a6,
            0x7ab368bc, 0x7ab36897, 0x7ab368af, 0x7ab3689f, 0x7ab368b1, 0x7ab368c4, 0x7ab368bf, 0x7ab3687e, 0x7ab368b7, 0x7ab36897,
            0x7ab36893, 0x7ab3688f, 0x7ab368b0, 0x7ab36880, 0x7ab3689f, 0x7ab368b7, 0x7ab36894, 0x7ab368c2, 0x7ab3689f, 0x7ab36898,
            0x7ab368b6, 0x7ab368c3, 0x7ab36885, 0x7ab3687f, 0x7ab368a4, 0x7ab3687e, 0x7ab368be, 0x7ab3687c, 0x7ab368bc, 0x7ab368b5,
            0x7ab3689e, 0x7ab36899, 0x7ab368a6, 0x7ab36881, 0x7ab368bb, 0x7ab36895, 0x7ab3689d, 0x7ab368a6, 0x7ab3689f, 0x7ab368be,
            0x7ab36880, 0x7ab368c4, 0x7ab368b5, 0x7ab368bc, 0x7ab368a1, 0x7ab368ad, 0x7ab368c1, 0x7ab36891, 0x7ab3687d, 0x7ab368a0,
            0x7ab368bd, 0x7ab3689b, 0x7ab368b1, 0x7ab36897, 0x7ab36881, 0x7ab36885, 0x7ab3689e, 0x7ab36882, 0x7ab3689b, 0x7ab36893,
            0x7ab368a3, 0x7ab3688d, 0x7ab368c4, 0x7ab368c4, 0x7ab3689b, 0x7ab368ad, 0x7ab368c4, 0x7ab368b4, 0x7ab368ad, 0x7ab36884,
            0x7ab368a5, 0x7ab3687f, 0x7ab368af, 0x7ab368a1, 0x7ab3687d, 0x7ab3687c, 0x7ab368b5, 0x7ab368b1, 0x7ab368be, 0x7ab368c3,
            0x7ab368b0, 0x7ab36895, 0x7ab36885, 0x7ab36882, 0x7ab368c0, 0x7ab368a5, 0x7ab368b8, 0x7ab368a2, 0x7ab36899, 0x7ab368b3,
            0x7ab368a1, 0x7ab3687d, 0x7ab3689f, 0x7ab36897, 0x7ab368a0, 0x7ab3687f, 0x7ab368c1, 0x7ab3689c, 0x7ab368a2, 0x7ab368c5,
            0x7ab368a6, 0x7ab3687e, 0x7ab368b3, 0x7ab36894, 0x7ab3689a, 0x7ab368bc, 0x7ab36890, 0x7ab3689d, 0x7ab368c3, 0x7ab36892,
            0x7ab3689a, 0x7ab368b8, 0x7ab368a2, 0x7ab368c3, 0x7ab368c5, 0x7ab368a2, 0x7ab368c5, 0x7ab36880, 0x7ab36885, 0x7ab368bb,
            0x7ab3689f, 0x7ab368c4, 0x7ab368a1, 0x7ab368a6, 0x7ab36890, 0x7ab3684c
    };

    private static final byte[] keyPC =  {
            115, 96, -31, -58, 31, 60, -83, 66, 11, 88,
            -71, -2, 55, -76, 5, -6, -93, 80, -111, 54,
            79, 44, 93, -78, 59, 72, 105, 110, 103, -92,
            -75, 106, -45, 64, 65, -90, 127, 28, 13, 34,
            107, 56, 25, -34, -105, -108, 101, -38, 3, 48,
            -15, 22, -81, 12, -67, -110, -101, 40, -55, 78,
            -57, -124, 21, 74, 51, 32, -95, -122, -33, -4,
            109, 2, -53, 24, 121, -66, -9, 116, -59, -70,
            99, 16, 81, -10, 15, -20, 29, 114, -5, 8,
            41, 46, 39, 100, 117, 42, -109, 0, 1, 102,
            63, -36, -51, -30, 43, -8, -39, -98, 87, 84,
            37, -102, -61, -16, -79, -42, 111, -52, 125, 82,
            91, -24, -119, 14, -121, 68, -43, 10, -13, -32,
            97, 70, -97, -68, 45, -62, -117, -40, 57, 126,
            -73, 52, -123, 122, 35, -48, 17, -74, -49, -84,
            -35, 50, -69, -56, -23, -18, -25, 36, 53, -22,
            83, -64, -63, 38, -1, -100, -115, -94, -21, -72,
            -103, 94, 23, 20, -27, 90, -125, -80, 113, -106,
            47, -116, 61, 18, 27, -88, 73, -50, 71, 4,
            -107, -54, -77, -96, 33, 6, 95, 124, -19, -126,
            75, -104, -7, 62, 119, -12, 69, 58, -29, -112,
            -47, 118, -113, 108, -99, -14, 123, -120, -87, -82,
            -89, -28, -11, -86, 19, -128, -127, -26, -65, 92,
            77, 98, -85, 120, 89, 30, -41, -44, -91, 26,
            67, 112, 49, 86, -17, 76, -3, -46, -37, 104,
            9, -114, 7, -60, 85, -118
    };
}
