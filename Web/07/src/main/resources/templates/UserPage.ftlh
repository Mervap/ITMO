<#import "./common.ftlh" as c>

<@c.page>
<div class="datatable">
    <div class="caption">Users</div>
    <table>
        <thead>
        <tr>
            <th>ID</th>
            <th>Login</th>
            <th>Created At</th>
        </tr>
        </thead>
        <tbody>
        <tr>
            <#if error??>
                <td colspan="3">${error}</td>
            <#else>
                <td>${user.id}</td>
                <td>${user.login}</td>
                <td>${user.creationTime}</td>
            </#if>
        </tr>
        </tbody>
    </table>
</div>
</@c.page>
