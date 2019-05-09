<#-- @ftlvariable name="talks" type="ru.itmo.webmail.model.service.TalkService.TalkView[]" -->
<#import "common.ftl" as c>

<@c.page>
<div class="talks-box">
    <div class="header">Messages</div>
    <div class="body">
        <form method="post" action="/talks">
            <input type="hidden" name="action" value="addTalk">
            <div class="field">
                <div class="name">
                    <label for="targetUserLogin">Login of recipient:</label>
                </div>
                <div class="value">
                    <input id="targetUserLogin" name="targetUserLogin" value="${targetUserLogin!}"/>
                </div>
            </div>
            <div class="field">
                <div class="name">
                    <label for="text">Text of message:</label>
                </div>
                <div class="text-value">
                    <textarea id="text" name="text" rows="5">${text!}</textarea>
                </div>
            </div>
            <div class="button-field">
                <input type="submit" value="Send">
            </div>
            <#if error??>
            <div class="error">
                ${error}
            </div>
            </#if>
             <#list talks as talk>
                    <p>
                        <span class="talk-source">${talk.sourceUser.login}</span>
                        <span class="talk-target">${talk.targetUser.login}</span>
                        <span class="talk-text">${talk.text}</span>
                    </p>
             </#list>
        </form>

    </div>
</div>
</@c.page>
