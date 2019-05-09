<#-- @ftlvariable name="notice" type="ru.itmo.wm4.domain.Notice" -->
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
<div class="add form-box">
    <div class="header">Add notice</div>
    <div class="body">
        <form method="post" action="/addNotice">
            <div class="field">
                <div class="text-name">
                    <label for="content">Content</label>
                </div>
                <div class="text-value">
                    <textarea id="content" name="content" rows="20">${notice.content!}</textarea>
                </div>
                <@error 'notice.content'/>
            </div>
            <div class="button-field">
                <input type="submit" value="Add">
            </div>
        </form>
    </div>
</div>
</@c.page>
