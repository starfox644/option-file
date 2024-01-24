package pesoptionfile;

public abstract class OptionFileElement {

    public OptionFileElement(OptionFile optionFile, int index) {
        this.optionFile = optionFile;
        this.index = index;
        read();
    }

    public void read() {
        readChildren();
    }

    protected void readChildren() {}

    public OptionFile getOptionFile() {return optionFile;}

    public int getIndex() {
        return index;
    }

    public abstract String getName();

    private final int index;

    private final OptionFile optionFile;

}
