<#-- @ftlvariable name="notice" type="ru.itmo.wm4.domain.Notice" -->
<#-- @ftlvariable name="commentForm" type="ru.itmo.wm4.domain.Comment" -->
<#-- @ftlvariable name="noticeError" type="java.lang.String" -->
<#import "/spring.ftl" as spring>
<#import "./common.ftlh" as c>

<#macro error field>
    <@spring.bind field/>
    <#list spring.status.errorMessages as error>
        <div class="error">${error}</div>
        <#break/>
    </#list>
</#macro>

<@c.page>
<div class="notice">
    <#if notice??>
        <div class="notice-box">
            <div class="header">${notice.creationTime}</div>
            <div class="body">
                ${notice.text?html}
            </div>
            <div class="author">
                by ${notice.user.login?html}
            </div>
            <div class="tags">
                <#list notice.tags as tag>
                    <span>${tag.name}</span>
                </#list>
            </div>
        </div>
        <#if user??>
            <div class="add-comment-form">
                <form method="post" action="/notice/${notice.id}">
                    <div class="field">
                        <div class="comment-header">
                            <label for="text">New comment</label>
                        </div>
                        <div class="comment-text">
                            <textarea id="text" name="text" rows="4">${commentForm.text!?html}</textarea>
                        </div>
                        <@error 'commentForm.text'/>
                    </div>
                    <div class="button-field">
                        <input type="submit" value="Add">
                    </div>
                </form>
            </div>
        </#if>
        <div class="comments">
            <#list notice.comments as comment>
                <section>
                    <div class="header">
                        ${comment.creationTime}
                    </div>
                    <div class="body">
                        ${comment.text?html}
                    </div>
                    <div class="author">
                        by ${comment.user.login?html}
                    </div>
                </section>
            </#list>
        </div>
    <#else>
        <div class="notice-box">
            <div class="header">${noticeError!}</div>
        <div>
    </#if>
</div>
</@c.page>
