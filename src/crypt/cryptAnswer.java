package crypt;

public class cryptAnswer {

    enum State {
        READY, TIME_OUT, DECODE_ERROR, ENCODE_ERROR;
    }

    private State state;
    private String value;

    public cryptAnswer(State state, String value) {
        this.setState(state);
        this.setValue(value);
    }

    public State getState() {
        return state;
    }

    public cryptAnswer setState(State state) {
        this.state = state;
        return this;
    }

    public String getValue() {
        return value;
    }

    public cryptAnswer setValue(String value) {
        this.value = value;
        return this;
    }
}
