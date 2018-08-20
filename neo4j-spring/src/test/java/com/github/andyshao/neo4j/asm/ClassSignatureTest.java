package com.github.andyshao.neo4j.asm;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.objectweb.asm.Opcodes;

import com.github.andyshao.neo4j.demo.ApiDao;
import com.github.andyshao.neo4j.output.ApiSearchDaoImpl;
import com.github.andyshao.reflect.FieldOperation;
import com.github.andyshao.reflect.SignatureDetector;
import com.github.andyshao.reflect.SignatureDetector.ClassSignature;

public class ClassSignatureTest{
    @Test
    public void test(String[] args) {
        ClassSignature signature = new SignatureDetector(Opcodes.ASM6).getSignature(MyMap.class);
        System.out.println(signature.classSignature);
        
        signature = new SignatureDetector(Opcodes.ASM6).getSignature(ApiSearchDaoImpl.class);
        System.out.println(signature.fieldSignatures.get(FieldOperation.getDeclaredField(ApiSearchDaoImpl.class , "scan")));
    }
    
    public interface MyInterface extends Serializable, Cloneable{}
    
    public interface MyList extends List<String>{}
    
    public interface MyDao extends ApiDao{}
    
    public interface MyMap extends Map<String , Object>{}
}
