package com.github.andyshao.neo4j.dao.impl;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Optional;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

import com.github.andyshao.lang.ClassAssembly;
import com.github.andyshao.neo4j.dao.DaoClassTypeException;
import com.github.andyshao.neo4j.dao.DaoFactory;
import com.github.andyshao.neo4j.dao.DaoProcessor;
import com.github.andyshao.neo4j.model.Neo4jDaoInfo;
import com.github.andyshao.neo4j.model.PageReturn;
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
    private static final String CONSTRUCT_DESC = "(Lcom/github/andyshao/neo4j/dao/DaoProcessor;)V";
    private static final String DAO_PROCESS_NAME = "daoProcessor";
    private static final String DAO_PROCESS_DESC = "Lcom/github/andyshao/neo4j/dao/DaoProcessor;";
    @Setter
    private DaoProcessor daoProcessor;

    @Override
    public Object getDao(Neo4jDaoInfo info) {
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
        cw.visit(Opcodes.V1_8 , Opcodes.ACC_PUBLIC + Opcodes.ACC_SUPER , classDesc , classSignature , "java/lang/Object" , 
            new String[] {daoDesc});
//        cw.visit(Opcodes.V1_8 , Opcodes.ACC_PUBLIC + Opcodes.ACC_SUPER , classDesc , classSignature , "java/lang/Object" , 
//            new String[] {});
        { // Field definition
            FieldVisitor fv = cw.visitField(Opcodes.ACC_PRIVATE + Opcodes.ACC_FINAL , DAO_PROCESS_NAME , DAO_PROCESS_DESC , null , null);
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
            
            mv.visitInsn(Opcodes.RETURN);
            mv.visitMaxs(2 , 2);
            mv.visitEnd();
        }
        
        { // interface methods
            Method[] methods = daoClass.getMethods();
            for(Method method : methods) {
                if(method.isDefault() || Modifier.isStatic(method.getModifiers())) continue;
                Class<?>[] exceptions = method.getExceptionTypes();
                String[] exceptionDescriptions = new String[exceptions.length];
                for(int i = 0; i<exceptionDescriptions.length; i++) {
                    exceptionDescriptions[i] = exceptions[i].getName().replace('.' , '/');
                }
                mv = cw.visitMethod(Opcodes.ACC_PUBLIC , method.getName() , Type.getType(method).getDescriptor() , csig.methodSignatures.get(method) , exceptionDescriptions);
                Class<?> returnType = method.getReturnType();
                mv.visitCode();
                final Class<?>[] parameterTypes = method.getParameterTypes();
                if(void.class.isAssignableFrom(returnType));
                else {
                    mv.visitTypeInsn(Opcodes.NEW, "com/github/andyshao/neo4j/dao/DaoProcessorParam");
                    mv.visitInsn(Opcodes.DUP);
                    mv.visitMethodInsn(Opcodes.INVOKESPECIAL , "com/github/andyshao/neo4j/dao/DaoProcessorParam" , "<init>" , "()V", false);
                    mv.visitVarInsn(Opcodes.ASTORE , 2);
                    
                    mv.visitVarInsn(Opcodes.ALOAD , 2);
                    if(parameterTypes.length == 0) mv.visitInsn(Opcodes.ICONST_0);
                    else if(parameterTypes.length == 1) mv.visitInsn(Opcodes.ICONST_1);
                    else if (parameterTypes.length == 2) mv.visitInsn(Opcodes.ICONST_2);
                    else if (parameterTypes.length == 3) mv.visitInsn(Opcodes.ICONST_3);
                    else if (parameterTypes.length == 4) mv.visitInsn(Opcodes.ICONST_4);
                    else if (parameterTypes.length == 5) mv.visitInsn(Opcodes.ICONST_5);
                    else mv.visitIntInsn(Opcodes.BIPUSH , parameterTypes.length);
                    mv.visitTypeInsn(Opcodes.ANEWARRAY , "java/lang/Object");
                    for(int i=0; i<parameterTypes.length; i++) {
                        mv.visitInsn(Opcodes.DUP);
                        if(i == 0) mv.visitInsn(Opcodes.ICONST_0);
                        else if (i == 1) mv.visitInsn(Opcodes.ICONST_1);
                        else if (i == 2) mv.visitInsn(Opcodes.ICONST_2);
                        else if (i == 3) mv.visitInsn(Opcodes.ICONST_3);
                        else if (i == 4) mv.visitInsn(Opcodes.ICONST_4);
                        else if (i == 5) mv.visitInsn(Opcodes.ICONST_5);
                        else mv.visitIntInsn(Opcodes.BIPUSH , i);
                        
                    }
                    mv.visitInsn(Opcodes.AASTORE);
                }
                mv.visitEnd();
            }
        }
        // TODO Auto-generated method stub
        
        cw.visitEnd();
        Constructor<Object> constructor = ConstructorOperation.getConstructor(ClassAssembly.DEFAULT.assemble(targetName , cw.toByteArray()) , DaoProcessor.class);
        return ConstructorOperation.newInstance(constructor, daoProcessor);
    }

}
