import java.io.UnsupportedEncodingException;

public class Player extends OptionFileElement {
    public Player(int index) {
        super(index);
    }

    @Override
    protected String readName(OptionFile optionFile) {
        String name;
        if(getIndex() == 0)
            name = "<empty>";
            // bounds check
        else if(getIndex() < 0 || getIndex() >= totalPlayers && getIndex() < firstEditIndex || getIndex() > lastEditIndex)
        {
            name = "<ERROR>";
        }
        else
        {
            // players begin offset
            int begin;
            // player offset
            int offset;
            // edited player
            if(getIndex() >= firstEditIndex)
            {
                // edited players begin offset
                begin = beginOffsetEdited;
                // offset relative to the edited players beginning
                offset = (getIndex() - firstEditIndex) * size;
            }
            else
            {
                begin = beginOffset;
                offset = getIndex() * size;
            }
            byte chars[] = new byte[nameTotalBytes];
            int playerOffset = begin + offset;
            // copies name characters (16 characters, 32 bytes in UTF16)
            System.arraycopy(optionFile.optionFileData, playerOffset, chars, 0, nameTotalBytes);
            boolean end = false;
            // name length computation, in searching for the first null character (2 '00' in UTF-16)
            int length = 0;
            for(int i = 0; !end && i < chars.length - 1; i += 2)
            {
                if(chars[i] == 0 && chars[i + 1] == 0)
                {
                    end = true;
                    length = i;
                }
            }
            try
            {
                // UTF-16 name creation
                name = new String(chars, 0, length, "UTF-16LE");
            }
            catch(UnsupportedEncodingException _ex)
            {
                throw new RuntimeException("UTF-16LE not supported, impossible to continue.");
            }
            // edited players, without name by default
            if(name.equals("") && getIndex() >= firstEditIndex)
            {
                name = (new StringBuilder("<Edited ")).append(String.valueOf(getIndex() - firstEditIndex)).append(">").toString();
            }
            else if(name.equals(""))
            {
                if(getIndex() >= firstUnusedIndex)
                {
                    // unused player without name
                    name = (new StringBuilder("<Unused ")).append(String.valueOf(getIndex())).append(">").toString();
                }
                else
                {
                    // normal player without name, this should not happen
                    name = (new StringBuilder("<L ")).append(String.valueOf(getIndex())).append(">").toString();
                }
            }
        }
        return name;
    }

    public static final int minAge = 15;

    public static final int minHeight = 148;

    public static final int size = 0x7C;

    public static final int beginOffset =  0x90FC;

    public static final int shirtNameOffset = 0x911C;

    public static final int beginOffsetEdited =  0x37D0;

    public static final int shirtNameOffsetEdited = 0x37F0;

    public static final int firstUnusedIndex =  4784;

    public static final int totalPlayers =  5000;

    public static final int firstEditIndex =  32768;

    public static final int totalPlayersEdited =  184;

    public static final int lastEditIndex = firstEditIndex + totalPlayersEdited - 1;

    /** aximum number of characters for a name */
    public static final int nameMaxLength = 15;

    /** maximum number of bytes for a name */
    public static final int nameMaxBytes = 30;

    /** total number of characters for a name */
    public static final int nameTotalLength = 16;

    /** total number of bytes for a name */
    public static final int nameTotalBytes = 32;

    public static final int shirtNameTotalLength = 16;

    public static final int shirtNameMaxLength = 15;
}
