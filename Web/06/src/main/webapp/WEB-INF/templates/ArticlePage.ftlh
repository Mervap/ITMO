<#import "common.ftl" as c>

<@c.page>
<div class="addArticle article-box">
    <div class="header">Add article</div>
    <div class="body">
        <form method="post" action="/article">
            <input type="hidden" name="action" value="addArticle">
            <div class="field">
                <div class="name">
                    <label for="title">Title:</label>
                </div>
                <div class="value">
                    <input id="title" name="title" value="${title!}"/>
                </div>
            </div>
            <div class="field">
                <div class="name">
                    <label for="text">Text:</label>
                </div>
                <div class="text-value">
                    <textarea id="text" name="text" rows="20">${text!}</textarea>
                </div>
            </div>
            <div class="button-field">
                <input type="submit" value="Create">
            </div>
            <div class="error"></div>
        </form>
    </div>
</div>
<script>
    $(function () {
        $(".addArticle form").submit(function () {
            var title = $(this).find("input[name='title']").val();
            var text = $(this).find("textarea[name='text']").val();
            var $error = $(this).find(".error").text("");
            $.post("", {action: "addArticle", title: title, text: text}, function (response) {
                if (response["success"]) {
                    setMessage("Article successfully added.");
                    document.location.href = "/";
                } else {
                    $error.text(response["error"]);
                }
            });
            return false;
        });
    })
</script>
</@c.page>
