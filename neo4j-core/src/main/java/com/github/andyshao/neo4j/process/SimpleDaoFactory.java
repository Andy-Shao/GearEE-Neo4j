package com.github.andyshao.neo4j.process;

import com.github.andyshao.asm.ApiConfs;
import com.github.andyshao.asm.ClassVisitorOperation;
import com.github.andyshao.lang.ClassAssembly;
import com.github.andyshao.neo4j.Neo4jException;
import com.github.andyshao.neo4j.domain.Neo4jDao;
import com.github.andyshao.reflect.ConstructorOperation;
import com.github.andyshao.reflect.SignatureDetector;
import org.objectweb.asm.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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

                // Class<?>[] argTypes = { ... };
                if(parameterCount == 0) mv.visitInsn(Opcodes.ICONST_0);
                else if(parameterCount == 1) mv.visitInsn(Opcodes.ICONST_1);
                else if(parameterCount == 2) mv.visitInsn(Opcodes.ICONST_2);
                else if(parameterCount == 3) mv.visitInsn(Opcodes.ICONST_3);
                else if(parameterCount == 4) mv.visitInsn(Opcodes.ICONST_4);
                else if(parameterCount == 5) mv.visitInsn(Opcodes.ICONST_5);
                else mv.visitIntInsn(Opcodes.BIPUSH , parameterCount);
                mv.visitTypeInsn(Opcodes.ANEWARRAY , "java/lang/Class");
                for(int i=0; i<parameterCount;i++) {
                    mv.visitInsn(Opcodes.DUP);
                    if(i == 0) mv.visitInsn(Opcodes.ICONST_0);
                    else if(i == 1) mv.visitInsn(Opcodes.ICONST_1);
                    else if(i == 2) mv.visitInsn(Opcodes.ICONST_2);
                    else if(i == 3) mv.visitInsn(Opcodes.ICONST_3);
                    else if(i == 4) mv.visitInsn(Opcodes.ICONST_4);
                    else if(i == 5) mv.visitInsn(Opcodes.ICONST_5);
                    else mv.visitIntInsn(Opcodes.BIPUSH , i);
                    if(parameterTypes[i].isPrimitive()) {
                        String owner = "";
                        final Class<?> parameter = parameterTypes[i];
                        if(int.class.isAssignableFrom(parameter)) owner = "java/lang/Integer";
                        else if(char.class.isAssignableFrom(parameter)) owner = "java/lang/Character";
                        else if(byte.class.isAssignableFrom(parameter)) owner = "java/lang/Byte";
                        else if(short.class.isAssignableFrom(parameter)) owner = "java/lang/Short";
                        else if(long.class.isAssignableFrom(parameter)) owner = "java/lang/Long";
                        else if(float.class.isAssignableFrom(parameter)) owner = "java/lang/Float";
                        else if(double.class.isAssignableFrom(parameter)) owner = "java/lang/Double";
                        else if(boolean.class.isAssignableFrom(parameter)) owner = "java/lang/Boolean";
                        mv.visitFieldInsn(Opcodes.GETSTATIC , owner , "TYPE" , "Ljava/lang/Class;");
                    } else mv.visitLdcInsn(Type.getType(parameterTypes[i]));
                    mv.visitInsn(Opcodes.AASTORE);
                }
                mv.visitVarInsn(Opcodes.ASTORE, parameterCount + 1);

                // Optional<Neo4jSql> neo4jSql = this.neo4jDao.findNeo4jSql( ... , argTypes);
                mv.visitVarInsn(Opcodes.ALOAD, 0);
                mv.visitFieldInsn(Opcodes.GETFIELD, classDesc, NEO4j_DAO_NAME, NEO4j_DAO_DESC);
                mv.visitLdcInsn(method.getName());
                mv.visitVarInsn(Opcodes.ALOAD, parameterCount + 1);
                mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "com/github/andyshao/neo4j/domain/Neo4jDao", "findNeo4jSql", "(Ljava/lang/String;[Ljava/lang/Class;)Ljava/util/Optional;", false);
                mv.visitVarInsn(Opcodes.ASTORE, parameterCount + 2);

                // Object[] args = {id, transaction};
                if(parameterCount == 0) mv.visitInsn(Opcodes.ICONST_0);
                else if(parameterCount == 1) mv.visitInsn(Opcodes.ICONST_1);
                else if(parameterCount == 2) mv.visitInsn(Opcodes.ICONST_2);
                else if(parameterCount == 3) mv.visitInsn(Opcodes.ICONST_3);
                else if(parameterCount == 4) mv.visitInsn(Opcodes.ICONST_4);
                else if(parameterCount == 5) mv.visitInsn(Opcodes.ICONST_5);
                else mv.visitIntInsn(Opcodes.BIPUSH , parameterCount);
                mv.visitTypeInsn(Opcodes.ANEWARRAY , "java/lang/Object");
                for(int i=0; i<parameterCount;i++) {
                    mv.visitInsn(Opcodes.DUP);
                    if(i == 0) mv.visitInsn(Opcodes.ICONST_0);
                    else if(i == 1) mv.visitInsn(Opcodes.ICONST_1);
                    else if(i == 2) mv.visitInsn(Opcodes.ICONST_2);
                    else if(i == 3) mv.visitInsn(Opcodes.ICONST_3);
                    else if(i == 4) mv.visitInsn(Opcodes.ICONST_4);
                    else if(i == 5) mv.visitInsn(Opcodes.ICONST_5);
                    else mv.visitIntInsn(Opcodes.BIPUSH , i);
                    mv.visitVarInsn(Opcodes.ALOAD , i+1);
                    mv.visitInsn(Opcodes.AASTORE);
                }
                mv.visitVarInsn(Opcodes.ASTORE, parameterCount + 3);

                // Neo4jSql sqlDefinition = neo4jSql.get();
                mv.visitVarInsn(Opcodes.ALOAD, parameterCount + 2);
                mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/util/Optional", "get", "()Ljava/lang/Object;", false);
                mv.visitTypeInsn(Opcodes.CHECKCAST, "com/github/andyshao/neo4j/domain/Neo4jSql");
                mv.visitVarInsn(Opcodes.ASTORE, parameterCount + 4);

                // return this.daoProcessor.processing(this.neo4jDao, sqlDefinition, transaction, args);
                mv.visitVarInsn(Opcodes.ALOAD, 0);
                mv.visitFieldInsn(Opcodes.GETFIELD, classDesc, DAO_PROCESS_NAME, DAO_PROCESS_DESC);
                mv.visitVarInsn(Opcodes.ALOAD, 0);
                mv.visitFieldInsn(Opcodes.GETFIELD, classDesc, NEO4j_DAO_NAME, NEO4j_DAO_DESC);
                mv.visitVarInsn(Opcodes.ALOAD, parameterCount + 4); //sqlDefinition
                mv.visitVarInsn(Opcodes.ALOAD, parameterCount + 3); //args
                mv.visitMethodInsn(
                        Opcodes.INVOKEINTERFACE,
                        "com/github/andyshao/neo4j/process/DaoProcessor",
                        "processing",
                        "(Lcom/github/andyshao/neo4j/domain/Neo4jDao;Lcom/github/andyshao/neo4j/domain/Neo4jSql;[Ljava/lang/Object;)Ljava/lang/Object;",
                        true);
                if(method.getReturnType().isAssignableFrom(Mono.class))
                    mv.visitTypeInsn(Opcodes.CHECKCAST, "reactor/core/publisher/Mono");
                else if (method.getReturnType().isAssignableFrom(Flux.class))
                    mv.visitTypeInsn(Opcodes.CHECKCAST, "reactor/core/publisher/Flux");
                else throw new UnsupportedOperationException("Method return type is not correct!");
                mv.visitInsn(Opcodes.ARETURN);
                mv.visitMaxs(5, countBasicLocal + 5);
                mv.visitEnd();
            }
        }

        cw.visitEnd();
        Constructor<Object> constructor = ConstructorOperation.getConstructor(ClassAssembly.DEFAULT.assemble(targetName , cw.toByteArray()) , DaoProcessor.class, Neo4jDao.class);
        return ConstructorOperation.newInstance(constructor, daoProcessor, neo4jDao);
    }
}
