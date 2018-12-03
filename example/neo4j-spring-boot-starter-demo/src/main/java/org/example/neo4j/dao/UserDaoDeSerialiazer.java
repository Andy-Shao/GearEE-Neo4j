package org.example.neo4j.dao;

import java.util.Optional;
import java.util.concurrent.CompletionStage;

import org.example.neo4j.domain.AuditRecord;
import org.example.neo4j.domain.User;
import org.example.neo4j.domain.vo.UserStatus;
import org.neo4j.driver.v1.StatementResultCursor;
import org.neo4j.driver.v1.Value;
import org.neo4j.driver.v1.types.Entity;

import com.github.andyshao.lang.NotSupportConvertException;
import com.github.andyshao.neo4j.io.DeSerializer;
import com.github.andyshao.neo4j.model.SqlMethod;

public class UserDaoDeSerialiazer implements DeSerializer{

    @Override
    public CompletionStage<?> deSerialize(StatementResultCursor src , SqlMethod sqlMethod) throws NotSupportConvertException {
        return src.nextAsync().thenApplyAsync(record -> {
            if(record == null) return Optional.empty();
            User user = new User();
            Entity entity = record.get(0).asEntity();
            Value username = entity.get("username");
            user.setUsername(username.isNull() ? null : username.asString());
            Value pwd = entity.get("password");
            user.setPassword(pwd.isNull() ? null : pwd.asString());
            Value status = entity.get("status");
            if(!status.isNull()) user.setStatus(UserStatus.valueOf(status.asString()));
            AuditRecord auditRecord = new AuditRecord();
            user.setAuditRecord(auditRecord);
            Value createUser = entity.get("createUser");
            auditRecord.setCreateUser(createUser.isNull() ? null : createUser.asString());
            Value createTime = entity.get("createTime");
            auditRecord.setCreateTime(createTime.isNull() ? null : createTime.asLocalDateTime());
            Value updateUser = entity.get("updateUser");
            auditRecord.setUpdateUser(updateUser.isNull() ? null : updateUser.asString());
            Value updateTime = entity.get("updateTime");
            auditRecord.setUpdateTime(updateTime.isNull() ? null : updateTime.asLocalDateTime());
            return Optional.of(user);
        });
    }

}
