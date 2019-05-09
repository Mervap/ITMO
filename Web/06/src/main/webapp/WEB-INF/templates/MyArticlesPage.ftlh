<#-- @ftlvariable name="userArticles" type="ru.itmo.webmail.model.domain.Article[]" -->
<#import "common.ftl" as c>

<@c.page>
<div class="datatable articles">
    <p class="success"></p>
    <div class="caption">My Articles</div>
    <table>
        <thead>
        <tr>
            <th>#</th>
            <th>Title</th>
            <th>Hide/Show</th>
        </tr>
        </thead>
        <tbody>
            <#list userArticles as userArticle>
            <tr>
                <td class="articleId">${userArticle.id}</td>
                <td class="articleTitle">${userArticle.title}</td>
                <td class="articleHidden">
                    <#if userArticle.hidden>
                        <button class="hideShowButton">Show</button>
                    <#else>
                        <button class="hideShowButton">Hide</button>
                    </#if>
                    <div class="loading"></div>
                </td>
            </tr>
            </#list>
        </tbody>
    </table>
</div>

<script>
    $(function () {
        $('.hideShowButton').click(function () {
            var $button = $(this);
            var $success = $(this).closest(".articles").find(".success");
            var $loading = $(this).closest("tr").find(".loading");
            var articleId = $(this).closest("tr").find(".articleId").text();
            var articleTitle = $(this).closest("tr").find(".articleTitle").text();

            $loading.text("Loading...");
            if ($(this).text() === 'Show') {
                $.post("", {action: "hideShowArticle", articleId: articleId, hidden: false}, function (response) {
                    if (response["success"]) {
                        $button.text("Hide");
                        $success.text("Article \"" + articleTitle + "\" is show");
                        $loading.text("");
                    } else {
                        $loading.text("Something went wrong")
                    }
                });
            } else {
                $.post("", {action: "hideShowArticle", articleId: articleId, hidden: true}, function (response) {
                    if (response["success"]) {
                        $button.text("Show");
                        $success.text("Article \"" + articleTitle + "\" is hide");
                        $loading.text("");
                    } else {
                        $loading.text("Something went wrong")
                    }
                });
            }
        });
    })
</script>
</@c.page>
