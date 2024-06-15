package dev.cephx.cfr.source;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

final class ClassFileUtil {
    private static final byte CONSTANT_Utf8 = 1;
    private static final byte CONSTANT_Integer = 3;
    private static final byte CONSTANT_Float = 4;
    private static final byte CONSTANT_Long = 5;
    private static final byte CONSTANT_Double = 6;
    private static final byte CONSTANT_Class = 7;
    private static final byte CONSTANT_String = 8;
    private static final byte CONSTANT_FieldRef = 9;
    private static final byte CONSTANT_MethodRef = 10;
    private static final byte CONSTANT_InterfaceMethodRef = 11;
    private static final byte CONSTANT_NameAndType = 12;
    private static final byte CONSTANT_MethodHandle = 15;
    private static final byte CONSTANT_MethodType = 16;
    private static final byte CONSTANT_DynamicInfo = 17;
    private static final byte CONSTANT_InvokeDynamic = 18;
    private static final byte CONSTANT_ModuleInfo = 19;
    private static final byte CONSTANT_PackageInfo = 20;
    
    static String getClassName(byte[] data) {
        try (final var dis = new DataInputStream(new ByteArrayInputStream(data))) {
            if (dis.readInt() != 0xCAFEBABE) {
                throw new RuntimeException("Invalid class file header");
            }

            dis.readUnsignedShort(); // minor
            dis.readUnsignedShort(); // major

            final int constPoolSize = dis.readUnsignedShort();
            final var classes = new int[constPoolSize - 1];
            final var strings = new String[constPoolSize - 1];
            for (int i = 0; i < constPoolSize - 1; i++) {
                final int type = dis.readUnsignedByte();
                switch (type) {
                    case CONSTANT_Utf8:
                        strings[i] = dis.readUTF();
                        break;
                    case CONSTANT_Integer:
                        dis.readInt();
                        break;
                    case CONSTANT_Float:
                        dis.readFloat();
                        break;
                    case CONSTANT_Long:
                        dis.readLong();
                        i++;
                        break;
                    case CONSTANT_Double:
                        dis.readDouble();
                        i++;
                        break;
                    case CONSTANT_Class:
                        classes[i] = dis.readUnsignedShort();
                        break;
                    case CONSTANT_String:
                    case CONSTANT_MethodType:
                    case CONSTANT_ModuleInfo:
                    case CONSTANT_PackageInfo:
                        dis.readUnsignedShort();
                        break;
                    case CONSTANT_FieldRef:
                    case CONSTANT_MethodRef:
                    case CONSTANT_InterfaceMethodRef:
                    case CONSTANT_NameAndType:
                    case CONSTANT_DynamicInfo:
                    case CONSTANT_InvokeDynamic:
                        dis.readUnsignedShort();
                        dis.readUnsignedShort();
                        break;
                    case CONSTANT_MethodHandle:
                        dis.readUnsignedByte();
                        dis.readUnsignedShort();
                        break;
                    default:
                        throw new RuntimeException("Invalid constant pool tag " + type + " at position " + i);
                }
            }

            dis.readUnsignedShort(); // access flags

            final int name = dis.readUnsignedShort();
            return strings[classes[name - 1] - 1];
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private ClassFileUtil() {
    }
}
