package org.xuenan.itook.core.init;

import org.xuenan.itook.core.model.ResponseEntity;

@FunctionalInterface
public interface TokenOutService {
    void setToken(ResponseEntity<?> responseEntity);
}
