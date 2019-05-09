package ru.itmo.wm4.form;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;

public class UserDisableCredentials {

    @Positive
    private long sourceId;

    @Positive
    private long targetId;

    private boolean disable;

    public long getSourceId() {
        return sourceId;
    }

    public void setSourceId(long sourceId) {
        this.sourceId = sourceId;
    }

    public long getTargetId() {
        return targetId;
    }

    public void setTargetId(long targetId) {
        this.targetId = targetId;
    }

    public boolean isDisable() {
        return disable;
    }

    public void setDisable(boolean disable) {
        this.disable = disable;
    }
}
