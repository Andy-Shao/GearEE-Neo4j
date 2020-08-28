package com.github.andyshao.neo4j.process;

import com.github.andyshao.asm.ApiConfs;
import com.github.andyshao.asm.ClassVisitorOperation;
import com.github.andyshao.lang.ClassAssembly;
import com.github.andyshao.neo4j.Neo4jException;
import com.github.andyshao.neo4j.domain.Neo4jDao;
import com.github.andyshao.reflect.ConstructorOperation;
import com.github.andyshao.reflect.SignatureDetector;
import org.objectweb.asm.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * Title: <br>
 * Description: <br>
 * Copyright: Copyright(c) 2020/8/28
 * Encoding: UNIX UTF-8
 *
 * @author Andy.Shao
 */
public class SimpleDaoFactory implements DaoFactory {
    private static final String CONSTRUCT_DESC = "(Lcom/github/andyshao/neo4j/process/DaoProcessor;Lcom/github/andyshao/neo4j/domain/Neo4jDao;)V";
    private static final String DAO_PROCESS_NAME = "daoProcessor";
    private static final String DAO_PROCESS_DESC = "Lcom/github/andyshao/neo4j/process/DaoProcessor;";
    private static final String NEO4j_DAO_NAME = "neo4jDao";
    private static final String NEO4j_DAO_DESC = "Lcom/github/andyshao/neo4j/domain/Neo4jDao;";
    private final DaoProcessor daoProcessor;

    public SimpleDaoFactory(DaoProcessor daoProcessor) {
        this.daoProcessor = daoProcessor;
    }

    @Override
    public Object buildDao(Neo4jDao neo4jDao) throws Neo4jException {
        Class<?> daoClass = neo4jDao.getDaoClass();
        if (!daoClass.isInterface()) throw new Neo4jException(
                String.format("%s must be interface!", daoClass.getName()));
        final String targetName = String.format("%s.impl.%sImpl", daoClass.getPackage().getName(), daoClass.getSimpleName());
        final String classDesc = targetName.replace('.', '/');
        final String daoDesc = daoClass.getName().replace('.', '/');
        final SignatureDetector.ClassSignature csig = new SignatureDetector(ApiConfs.DEFAULT_ASM_VERSION).getSignature(daoClass);
        String classSignature = null;
        if (csig.classSignature != null) {
            classSignature = csig.classSignature + String.format("L%s;", daoDesc);
        }
        final ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
        cw.visit(Opcodes.V11, Opcodes.ACC_PUBLIC + Opcodes.ACC_SUPER, classDesc, classSignature, "java/lang/Object",
                new String[]{daoDesc});
        { // Field definition
            FieldVisitor fv = cw.visitField(Opcodes.ACC_PRIVATE + Opcodes.ACC_FINAL, DAO_PROCESS_NAME, DAO_PROCESS_DESC, null, null);
            fv.visitEnd();

            fv = cw.visitField(Opcodes.ACC_PRIVATE + Opcodes.ACC_FINAL, NEO4j_DAO_NAME, NEO4j_DAO_DESC, null, null);
            fv.visitEnd();
        }
        MethodVisitor mv = null;
        { // Class Constructor
            mv = cw.visitMethod(Opcodes.ACC_PUBLIC, "<init>", CONSTRUCT_DESC, null, null);
            mv.visitCode();

            mv.visitVarInsn(Opcodes.ALOAD, 0);
            mv.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false);

            mv.visitVarInsn(Opcodes.ALOAD, 0);
            mv.visitVarInsn(Opcodes.ALOAD, 1);
            mv.visitFieldInsn(Opcodes.PUTFIELD, classDesc, DAO_PROCESS_NAME, DAO_PROCESS_DESC);

            mv.visitVarInsn(Opcodes.ALOAD, 0);
            mv.visitVarInsn(Opcodes.ALOAD, 2);
            mv.visitFieldInsn(Opcodes.PUTFIELD, classDesc, NEO4j_DAO_NAME, NEO4j_DAO_DESC);

            mv.visitInsn(Opcodes.RETURN);
            mv.visitMaxs(2, 3);
            mv.visitEnd();
        }
        { // interface methods
            for(Method method : daoClass.getMethods()) {
                // ignoring STATIC and DEFAULT methods
                if(Modifier.isStatic(method.getModifiers())) continue;
                else if(method.isDefault()) continue;

                Class<?>[] exceptions = method.getExceptionTypes();
                String[] exceptionDescriptions = new String[exceptions.length];
                for(int i=0; i<exceptionDescriptions.length; i++) {
                    exceptionDescriptions[i] = exceptions[i].getName().replace('.' , '/');
                }
                final int countBasicLocal = ClassVisitorOperation.countBasicLocal(method);
                final int parameterCount = method.getParameterCount();
                final Class<?>[] parameterTypes = method.getParameterTypes();
                mv = cw.visitMethod(Opcodes.ACC_PUBLIC , method.getName() , Type.getType(method).getDescriptor() , csig.methodSignatures.get(method) , exceptionDescriptions);
                mv.visitCode();


            }
        }

        cw.visitEnd();
        Constructor<Object> constructor = ConstructorOperation.getConstructor(ClassAssembly.DEFAULT.assemble(targetName , cw.toByteArray()) , DaoProcessor.class, Neo4jDao.class);
        return ConstructorOperation.newInstance(constructor, daoProcessor, neo4jDao);
    }
}
