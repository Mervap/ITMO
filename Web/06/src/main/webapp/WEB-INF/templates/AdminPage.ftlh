<#-- @ftlvariable name="users" type="ru.itmo.webmail.model.domain.User[]" -->
<#import "common.ftl" as c>

<@c.page>
<div class="datatable articles">
    <p class="success"></p>
    <div class="caption">Admins</div>
    <table>
        <thead>
        <tr>
            <th>#</th>
            <th>Login</th>
            <#if admin??>
                <th>Admin</th>
            </#if>
        </tr>
        </thead>
        <tbody>
            <#list users as user>
            <tr>
                <td class="userId">${user.id}</td>
                <td class="userLogin">${user.login}</td>
                <#if admin??>
                    <td class="userAdmin">
                        <#if user.admin>
                            <a class="adminHref" href="#" title="disable">true</a>
                        <#else>
                            <a class="adminHref" href="#" title="enable">false</a>
                        </#if>
                        <div class="loading"></div>
                    </td>
                </#if>
            </tr>
            </#list>
        </tbody>
    </table>
</div>

<script>
    $(function () {
        $('.adminHref').click(function () {
            var $a = $(this);
            var $success = $(this).closest(".articles").find(".success");
            var $loading = $(this).closest("tr").find(".loading");
            var userId = $(this).closest("tr").find(".userId").text();
            var userLogin = $(this).closest("tr").find(".userLogin").text();

            $loading.text("Loading...");
            if ($(this).text() === 'false') {
                $.post("", {action: "changeAdmin", userId: userId, admin: true}, function (response) {
                    if (response["success"]) {
                        $a.text("true");
                        $a.attr("title", "disable");
                        $success.text("User \"" + userLogin + "\" is admin now");
                        $loading.text("");
                    } else {
                        $loading.text(response["error"])
                    }
                });
            } else {
                $.post("", {action: "changeAdmin", userId: userId, admin: false}, function (response) {
                    if (response["success"]) {
                        $a.text("false");
                        $a.attr("title", "enable");
                        $success.text("Article \"" + userLogin + "\" is not admin now");
                        $loading.text("");
                    } else {
                        $loading.text(response["error"])
                    }
                });
            }
            return false;
        });
    })
</script>
</@c.page>
