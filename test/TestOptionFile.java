import org.junit.Assert;
import org.junit.Test;
import pesoptionfile.OptionFile;

import java.io.*;

public class TestOptionFile {
    @Test
    public void testRead() {
        try {
            byte[] expectedFileData = getFileData("decrypted");
            File file = new File("testdata\\KONAMI-WIN32PES6OPT");
            OptionFile optionFile = new OptionFile();
            optionFile.read(file);

            Assert.assertArrayEquals(optionFile.optionFileData, expectedFileData);
        }
        catch(Exception exception)
        {
            Assert.fail();
        }
    }

    @Test
    public void testDecryptedWrite() {
        try {
            byte[] expectedFileData = getFileData("decrypted");
            File file = new File("testdata\\KONAMI-WIN32PES6OPT");
            OptionFile optionFile = new OptionFile();
            optionFile.read(file);

            File outFile = new File("testdata\\tmpout");
            optionFile.decryptedWrite(outFile);
            byte[] writtenFileData = getFileData("tmpout");
            Assert.assertArrayEquals(writtenFileData, expectedFileData);
        }
        catch(Exception exception)
        {
            Assert.fail();
        }
    }

    @Test
    public void testEncryptedWrite() {
        try {
            byte[] expectedFileData = getFileData("KONAMI-WIN32PES6OPT");
            File file = new File("testdata\\KONAMI-WIN32PES6OPT");
            OptionFile optionFile = new OptionFile();
            optionFile.read(file);

            File outFile = new File("testdata\\tmpout");
            optionFile.encryptedWrite(outFile);
            byte[] writtenFileData = getFileData("tmpout");
            Assert.assertArrayEquals(writtenFileData, expectedFileData);

            byte[] expectedDecryptedFileData = getFileData("decrypted");
            Assert.assertArrayEquals(optionFile.optionFileData, expectedDecryptedFileData);
        }
        catch(Exception exception)
        {
            Assert.fail();
        }
    }

    private byte[] getFileData(String fileName) throws IOException {
        File expectedFile = new File("testdata\\" + fileName);
        byte[] expectedFileData = new byte[(int) expectedFile.length()];
        DataInputStream dis = new DataInputStream(new FileInputStream(expectedFile));
        dis.readFully(expectedFileData);
        dis.close();
        return expectedFileData;
    }
}
