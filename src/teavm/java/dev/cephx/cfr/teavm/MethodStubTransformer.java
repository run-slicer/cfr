package dev.cephx.cfr.teavm;

import org.teavm.model.*;
import org.teavm.model.instructions.ExitInstruction;
import org.teavm.model.instructions.IntegerConstantInstruction;
import org.teavm.model.instructions.NullConstantInstruction;

import java.util.logging.Handler;

public class MethodStubTransformer implements ClassHolderTransformer {
    @Override
    public void transformClass(ClassHolder cls, ClassHolderTransformerContext context) {
        switch (cls.getName()) {
            case "org.benf.cfr.reader.state.ClassFileSourceImpl" -> {
                this.stubWithNullConstant(cls.getMethod(new MethodDescriptor("getContentByFromReflectedClass", String.class, byte[].class)));
                this.stubWithBooleanConstant(cls.getMethod(new MethodDescriptor("CheckJrt", boolean.class)), false);
            }
            case "org.benf.cfr.reader.util.output.LoggerFactory" -> {
                this.stubWithNullConstant(cls.getMethod(new MethodDescriptor("getHandler", Handler.class)));
            }
        }
    }

    private void stubWithNullConstant(MethodHolder method) {
        final Program program = this.newProgram(method.parameterCount());

        final Variable nullConst = program.createVariable();
        final BasicBlock block = program.createBasicBlock();

        final var nullConstInsn = new NullConstantInstruction();
        nullConstInsn.setReceiver(nullConst);
        block.add(nullConstInsn);

        final var returnInsn = new ExitInstruction();
        returnInsn.setValueToReturn(nullConst);
        block.add(returnInsn);

        method.setProgram(program);
    }

    private void stubWithBooleanConstant(MethodHolder method, boolean value) {
        final Program program = this.newProgram(method.parameterCount());

        final Variable falseConst = program.createVariable();
        final BasicBlock block = program.createBasicBlock();

        final var falseConstInsn = new IntegerConstantInstruction();
        falseConstInsn.setConstant(value ? 1 : 0);
        falseConstInsn.setReceiver(falseConst);
        block.add(falseConstInsn);

        final var returnInsn = new ExitInstruction();
        returnInsn.setValueToReturn(falseConst);
        block.add(returnInsn);

        method.setProgram(program);
    }

    private Program newProgram(int parameterCount) {
        parameterCount++; // type var

        final Program program = new Program();
        for (int i = 0; i < parameterCount; i++) {
            program.createVariable();
        }

        return program;
    }
}
