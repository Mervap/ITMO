package ru.itmo.wm4.form;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class NoticeCredentials {

    @NotNull
    @Size(max = 5000)
    @Pattern(regexp = "([\\w\\s])*", message = "expected Latin letters" )
    @Pattern(regexp = "([\\w]{2}\\w*|\\s)*", message = "tag can't be shorter then 2")
    private String tags;

    @NotNull
    @NotEmpty
    @Size(min = 1, max = 5000)
    private String text;

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
