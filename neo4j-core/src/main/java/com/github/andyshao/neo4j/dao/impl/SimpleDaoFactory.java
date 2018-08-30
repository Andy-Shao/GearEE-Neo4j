package com.github.andyshao.neo4j.dao.impl;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

import com.github.andyshao.asm.ClassVisitorOperation;
import com.github.andyshao.lang.ClassAssembly;
import com.github.andyshao.neo4j.dao.DaoClassTypeException;
import com.github.andyshao.neo4j.dao.DaoFactory;
import com.github.andyshao.neo4j.dao.DaoProcessor;
import com.github.andyshao.neo4j.model.Neo4jDaoInfo;
import com.github.andyshao.reflect.ConstructorOperation;
import com.github.andyshao.reflect.SignatureDetector;
import com.github.andyshao.reflect.SignatureDetector.ClassSignature;

import lombok.Setter;

/**
 * 
 * Title:<br>
 * Descript:<br>
 * Copyright: Copryright(c) Aug 17, 2018<br>
 * Encoding:UNIX UTF-8
 * @author Andy.Shao
 *
 */
public class SimpleDaoFactory implements DaoFactory {
    private static final String DAO_PROCESS_PARAM_NAME = "com/github/andyshao/neo4j/dao/DaoProcessorParam";
    private static final String CONSTRUCT_DESC = "(Lcom/github/andyshao/neo4j/dao/DaoProcessor;Lcom/github/andyshao/neo4j/model/Neo4jDaoInfo;)V";
    private static final String DAO_PROCESS_NAME = "daoProcessor";
    private static final String DAO_PROCESS_DESC = "Lcom/github/andyshao/neo4j/dao/DaoProcessor;";
    private static final String NEO4j_DAO_INFO_NAME = "neo4jDaoInfo";
    private static final String NEO4j_DAO_INFO_DESC = "Lcom/github/andyshao/neo4j/model/Neo4jDaoInfo;";
    @Setter
    private DaoProcessor daoProcessor;

    @Override
    public Object buildDao(Neo4jDaoInfo info) {
        Class<?> daoClass = info.getDaoClass();
        if(!daoClass.isInterface()) throw new DaoClassTypeException(String.format("%s must be a interface" , 
            daoClass.getName())); 
        final String targetName = String.format("%s.impl.%sImpl" , daoClass.getPackage().getName(), daoClass.getSimpleName());
        final String classDesc = targetName.replace('.' , '/');
        final String daoDesc = daoClass.getName().replace('.' , '/');
        final ClassSignature csig = new SignatureDetector(Opcodes.ASM6).getSignature(daoClass);
        String classSignature = null;
        if(csig.classSignature != null) {
            classSignature = csig.classSignature + String.format("L%s;" , daoDesc);
        }
        final ClassWriter cw = new ClassWriter(0);
//        cw.visit(Opcodes.V1_8 , Opcodes.ACC_PUBLIC + Opcodes.ACC_SUPER , classDesc , classSignature , "java/lang/Object" , 
//            new String[] {daoDesc});
        cw.visit(Opcodes.V1_8 , Opcodes.ACC_PUBLIC + Opcodes.ACC_SUPER , classDesc , classSignature , "java/lang/Object" , 
            new String[] {});
        { // Field definition
            FieldVisitor fv = cw.visitField(Opcodes.ACC_PRIVATE + Opcodes.ACC_FINAL , DAO_PROCESS_NAME , DAO_PROCESS_DESC , null , null);
            fv.visitEnd();
            
            fv = cw.visitField(Opcodes.ACC_PRIVATE + Opcodes.ACC_FINAL , NEO4j_DAO_INFO_NAME , NEO4j_DAO_INFO_DESC , null , null);
            fv.visitEnd();
        }
        MethodVisitor mv = null;
        { // Class Constructor
            mv = cw.visitMethod(Opcodes.ACC_PUBLIC , "<init>" , CONSTRUCT_DESC , null , null);
            mv.visitCode();
            
            mv.visitVarInsn(Opcodes.ALOAD , 0);
            mv.visitMethodInsn(Opcodes.INVOKESPECIAL , "java/lang/Object" , "<init>" , "()V", false);
            
            mv.visitVarInsn(Opcodes.ALOAD , 0);
            mv.visitVarInsn(Opcodes.ALOAD , 1);
            mv.visitFieldInsn(Opcodes.PUTFIELD , classDesc , DAO_PROCESS_NAME , DAO_PROCESS_DESC);
            
            mv.visitVarInsn(Opcodes.ALOAD , 0);
            mv.visitVarInsn(Opcodes.ALOAD , 2);
            mv.visitFieldInsn(Opcodes.PUTFIELD , classDesc , NEO4j_DAO_INFO_NAME , NEO4j_DAO_INFO_DESC);
            
            mv.visitInsn(Opcodes.RETURN);
            mv.visitMaxs(2 , 3);
            mv.visitEnd();
        }
        
        { // interface methods
            for(Method method : daoClass.getMethods()) {
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
                
                //new DaoProcessorParam()
                mv.visitTypeInsn(Opcodes.NEW , DAO_PROCESS_PARAM_NAME);
                mv.visitInsn(Opcodes.DUP);
                mv.visitMethodInsn(Opcodes.INVOKESPECIAL , DAO_PROCESS_PARAM_NAME , "<init>" , "()V", false);
                final int daoProcessorParamIndex = countBasicLocal;
                mv.visitVarInsn(Opcodes.ASTORE , daoProcessorParamIndex);
                mv.visitVarInsn(Opcodes.ALOAD , daoProcessorParamIndex);
                
                //DaoProcessorParam.setArgs(new Object[]{...})
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
                    mv.visitVarInsn(Opcodes.ALOAD , i);
                    mv.visitInsn(Opcodes.AASTORE);
                }
                mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL , DAO_PROCESS_PARAM_NAME , "setArgs" , "([Ljava/lang/Object;)V", false);
                
                //DaoProcessorParam.setArgTypes(new Class<?>[]{...})
                mv.visitVarInsn(Opcodes.ALOAD , parameterCount + 1);
                if(parameterCount == 0)mv.visitInsn(Opcodes.ICONST_0);
                else if(parameterCount == 1) mv.visitInsn(Opcodes.ICONST_1);
                else if(parameterCount == 2) mv.visitInsn(Opcodes.ICONST_2);
                else if(parameterCount == 3) mv.visitInsn(Opcodes.ICONST_3);
                else if(parameterCount == 4) mv.visitInsn(Opcodes.ICONST_4);
                else if(parameterCount == 5) mv.visitInsn(Opcodes.ICONST_5);
                else mv.visitIntInsn(Opcodes.BIPUSH , parameterCount);
                mv.visitTypeInsn(Opcodes.ANEWARRAY , "java/lang/Class");
                for(int i=0; i<parameterCount; i++) {
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
                mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL , DAO_PROCESS_PARAM_NAME , "setArgTypes" , "([Ljava/lang/Class;)V", false);
                
                //DaoProcessorParam.setMethodName(..)
                mv.visitVarInsn(Opcodes.ALOAD , daoProcessorParamIndex);
                mv.visitLdcInsn(method.getName());
                mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL , DAO_PROCESS_PARAM_NAME , "setMethodName" , "(Ljava/lang/String;)V", false);
                
                //return daoProcessor.process(param, neo4jDaoInfo);
                mv.visitVarInsn(Opcodes.ALOAD , 0);
                mv.visitFieldInsn(Opcodes.GETFIELD , classDesc , DAO_PROCESS_NAME , DAO_PROCESS_DESC);
                mv.visitVarInsn(Opcodes.ALOAD , daoProcessorParamIndex);
                mv.visitVarInsn(Opcodes.ALOAD , 0);
                mv.visitFieldInsn(Opcodes.GETFIELD , classDesc , NEO4j_DAO_INFO_NAME , NEO4j_DAO_INFO_DESC);
                mv.visitMethodInsn(Opcodes.INVOKEINTERFACE , "com/github/andyshao/neo4j/dao/DaoProcessor" , "process" , 
                    "(Lcom/github/andyshao/neo4j/dao/DaoProcessorParam;Lcom/github/andyshao/neo4j/model/Neo4jDaoInfo;)Ljava/lang/Object;", true);
                mv.visitTypeInsn(Opcodes.CHECKCAST , "java/util/concurrent/CompletionStage");
                mv.visitInsn(Opcodes.ARETURN);
                mv.visitMaxs(5 , countBasicLocal + 1);
                mv.visitEnd();
            }
        }
        
        cw.visitEnd();
        Constructor<Object> constructor = ConstructorOperation.getConstructor(ClassAssembly.DEFAULT.assemble(targetName , cw.toByteArray()) , DaoProcessor.class, Neo4jDaoInfo.class);
        return ConstructorOperation.newInstance(constructor, daoProcessor, info);
    }

}
