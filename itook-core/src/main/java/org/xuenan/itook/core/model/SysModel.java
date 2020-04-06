package org.xuenan.itook.core.model;

import org.xuenan.itook.core.utils.JSONUtils;
import org.xuenan.itook.core.utils.StringUtils;

import java.io.Serializable;

public abstract class SysModel implements Serializable {
    public static final long serialVersionUID = 1L;

    public SysModel() {
    }

    public final String toJSONString() {
        return JSONUtils.toJSONString(this);
    }

    public final String toJSONHTML() {
        String model = this.toJSONString();
        return StringUtils.isEmpty(new String[]{model}) ? null : model.replace("'", "&apos;").replace("\"", "&quot;");
    }

    public String toString() {
        return this.toJSONString();
    }
}
