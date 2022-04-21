package Logic.Objects;

import java.io.Serializable;


public class News implements Serializable {
//	private static final long serialVersionUID = 1L;
    private int ID;
    private String header;
    private String content;
    private Party regardingParty;

    public News(int ID, String header, String content, Party regardingParty) {
        this.ID = ID;
        this.header = header;
        this.content = content;
        this.regardingParty = regardingParty;
    }

    public int getID() {
        return ID;
    }

    public String getHeader() {
        return header;
    }

    public String getContent() {
        return content;
    }

    public Party getRegardingParty() {
        return regardingParty;
    }
}
