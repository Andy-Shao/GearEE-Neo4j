package com.github.andyshao.neo4j.asm;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.objectweb.asm.Opcodes;

import com.github.andyshao.neo4j.demo.ApiDao;
import com.github.andyshao.neo4j.demo.ApiKey;
import com.github.andyshao.neo4j.model.Pageable;
import com.github.andyshao.neo4j.output.ApiSearchDaoImpl;
import com.github.andyshao.reflect.MethodOperation;
import com.github.andyshao.reflect.SignatureDetector;
import com.github.andyshao.reflect.SignatureDetector.ClassSignature;

public class ClassSignatureTest{
    @Test
    public void test() {
        ClassSignature signature = new SignatureDetector(Opcodes.ASM6).getSignature(MyMap.class);
        System.out.println(signature.classSignature);
        
        signature = new SignatureDetector(Opcodes.ASM6).getSignature(ApiSearchDaoImpl.class);
        System.out.println(signature.methodSignatures.get(MethodOperation.getDeclaredMethod(ApiSearchDaoImpl.class , "findByPk" , ApiKey.class)));
        System.out.println(signature.methodSignatures.get(MethodOperation.getDeclaredMethod(ApiSearchDaoImpl.class , "findByPage" , Pageable.class)));
        
        signature = new SignatureDetector(Opcodes.ASM6).getSignature(MyClass.class);
        System.out.println(signature.classSignature);
    }
    
    public interface MyInterface extends Serializable, Cloneable{}
    
    public interface MyList extends List<String>{}
    
    public interface MyDao extends ApiDao{}
    
    public interface MyMap extends Map<String , Object>{}
    
    public class MyClass<T> {}
}
