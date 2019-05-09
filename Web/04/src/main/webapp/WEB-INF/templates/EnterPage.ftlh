<#-- @ftlvariable name="loginOrEmail" type="String" -->
<#-- @ftlvariable name="password" type="String" -->
<#-- @ftlvariable name="error" type="long" -->

<#import "common.ftl" as c>

<#-- Task 5 -->
<@c.page>
<div class="form-box">
    <div class="header">Entrance</div>
    <div class="body">
        <form method="post" action="/enter">
            <input type="hidden" name="action" value="enter">
            <div class="field">
                <div class="name">
                    <label for="loginOrEmail">Login/Email</label>
                </div>
                <div class="value">
                    <input id="loginOrEmail" name="loginOrEmail" value="${loginOrEmail!}"/>
                </div>
            </div>
            <div class="field">
                <div class="name">
                    <label for="password">Password</label>
                </div>
                <div class="value">
                    <input id="password" type="password" name="password" value="${password!}"/>
                </div>
            </div>
            <div class="button-field">
                <input type="submit" value="Enter">
            </div>
            <#if error??>
            <div class="error">
                ${error}
            </div>
            </#if>
        </form>
    </div>
</div>
</@c.page>
