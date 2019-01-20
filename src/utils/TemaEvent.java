package utils;

import domain.Tema;

public class TemaEvent implements  Event {
    private Tema oldData;
    private Tema data;
    private ChangeEventType type;

    public TemaEvent(Tema oldData, Tema data, ChangeEventType type) {
        this.oldData = oldData;
        this.data = data;
        this.type = type;
    }

    public Tema getOldData() {
        return oldData;
    }

    public void setOldData(Tema oldData) {
        this.oldData = oldData;
    }

    public Tema getData() {
        return data;
    }

    public void setData(Tema data) {
        this.data = data;
    }

    public ChangeEventType getType() {
        return type;
    }

    public void setType(ChangeEventType type) {
        this.type = type;
    }

}
