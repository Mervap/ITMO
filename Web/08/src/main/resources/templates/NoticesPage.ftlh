<#import "./common.ftlh" as c>

<@c.page>
<div class="datatable">
    <div class="caption">Notices</div>
    <table>
        <thead>
        <tr>
            <th>#</th>
            <th>Text</th>
            <th>Tags</th>
            <th>Created At</th>
        </tr>
        </thead>
        <tbody>
    <#list notices as notice>
    <tr>
        <td>${notice.id}</td>
        <td>${notice.text}</td>
        <td>
            <#list notice.tags as tag>
                <span>${tag.name} </span>
            </#list>
        </td>
        <td>${notice.creationTime}</td>
    </tr>
    </#list>
        </tbody>
    </table>
</div>
</@c.page>
