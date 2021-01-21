package fr.ensimag.ima.pseudocode;

public class RegisterOffsetRegister extends DAddr{
    public int getOffset() {
        return offset;
    }
    public Register getRegister() {
        return register;
    }

    public Register getRegisterOffset() {
        return registerOffset;
    }

    private final int offset;
    private final Register register;
    private final GPRegister registerOffset;
    public RegisterOffsetRegister(int offset, Register register, GPRegister registerOffset) {
        super();
        this.offset = offset;
        this.register = register;
        this.registerOffset = registerOffset;
    }
    @Override
    public String toString() {
        return offset + "(" + register + "," + registerOffset + ")";
    }
}
