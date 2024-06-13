package dev.cephx.cfr.teavm;

import org.teavm.model.*;
import org.teavm.model.instructions.EmptyInstruction;
import org.teavm.model.instructions.InvokeInstruction;

import java.util.Map;
import java.util.Set;

public class InsnStubTransformer implements ClassHolderTransformer {
    private static final Map<String, Set<String>> INVOKE_STUBS = Map.of(
            "java.lang.System", Set.of("exit"),
            "java.util.logging.Logger", Set.of("setUseParentHandlers", "addHandler")
    );

    @Override
    public void transformClass(ClassHolder cls, ClassHolderTransformerContext context) {
        for (final MethodHolder method : cls.getMethods()) {
            final Program program = method.getProgram();
            if (program == null) continue;

            for (final BasicBlock block : program.getBasicBlocks()) {
                for (final Instruction insn : block) {
                    if (insn instanceof InvokeInstruction invokeInsn) {
                        this.transformInvokeInsn(invokeInsn);
                    }
                }
            }
        }
    }

    private void transformInvokeInsn(InvokeInstruction insn) {
        final MethodReference methodRef = insn.getMethod();

        final Set<String> methods = INVOKE_STUBS.get(methodRef.getClassName());
        if (methods != null && methods.contains(methodRef.getName())) {
            final var emptyInsn = new EmptyInstruction();
            emptyInsn.setLocation(insn.getLocation());

            insn.replace(emptyInsn);
        }
    }
}
