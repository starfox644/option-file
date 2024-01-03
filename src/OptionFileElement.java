public abstract class OptionFileElement {

    public OptionFileElement(int index) {
        this.index = index;
        this.name = "";
    }

    public void read(OptionFile optionFile) {
        name = readName(optionFile);
        readChildren(optionFile);
    }

    protected abstract String readName(OptionFile optionFile);

    protected void readChildren(OptionFile optionFile) {}

    private final int index;

    public int getIndex() {
        return index;
    }

    public String getName() {
        return name;
    }

    private String name;

}
