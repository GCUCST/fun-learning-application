package cn.cst.services;

import cn.cst.pojos.Entity;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

@Slf4j
public class FunctionService {
    public Entity getEntity() {
        return buildEntity();
    }

    private Entity buildEntity() {
        log.info("buildEntity() is invoked");
        return Entity.builder().id(UUID.randomUUID()).msg("create").object(null).status(200).build();
    }
}
