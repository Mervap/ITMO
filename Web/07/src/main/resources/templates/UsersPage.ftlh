<#-- @ftlvariable name="users" type="ru.itmo.wm4.domain.User[]" -->
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
<div class="datatable">
    <@error 'disableEnableForm.disable'/>
    <div class="caption">Users</div>
    <table>
        <thead>
        <tr>
            <th>ID</th>
            <th>Login</th>
            <th>Created At</th>
            <th>Disable/Enable</th>
        </tr>
        </thead>
        <tbody>
    <#list users as listUser>
    <tr>
        <td>${listUser.id}</td>
        <td>${listUser.login}</td>
        <td>${listUser.creationTime}</td>
        <td>
            <form method="post" action="/disableEnableUser">
                <input type="hidden" name="sourceId" value="${user.id!}">
                <input type="hidden" name="targetId" value="${listUser.id}">
                <#if listUser.disable>
                    <input type="hidden" name="disable" value="false">
                    <div class="button-field">
                        <input type="submit" value="Enable">
                    </div>
                <#else>
                    <input type="hidden" name="disable" value="true">
                    <div class="button-field">
                        <input type="submit" value="Disable">
                    </div>
                </#if>
            </form>
        </td>
    </tr>
    </#list>
        </tbody>
    </table>
</div>
</@c.page>
