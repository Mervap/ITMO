<#-- @ftlvariable name="message" type="java.lang.String" -->

<#import "common.ftl" as c>

<@c.page>
    <#if message??>
        <p>${message}</p>
    </#if>

<div class="datalist articles">
    <ul>
        <li class="no-data">No data</li>
    </ul>
</div>

<template class="articles-li">
    <li>
        <article>
            <div class="title"></div>
            <div class="information">By Vovuh, 2 days ago, translation</div>
            <div class="body"></div>
            <div class="footer">
                <div class="left">
                    <img src="img/voteup.png" title="Vote Up" alt="Vote Up"/>
                    <span class="positive-score">+173</span>
                    <img src="img/votedown.png" title="Vote Down" alt="Vote Down"/>
                </div>
                <div class="right">
                    <img src="img/date_16x16.png" title="Publish Time" alt="Publish Time"/>
                    2 days ago
                    <img src="img/comments_16x16.png" title="Comments" alt="Comments"/>
                    <a href="#">68</a>
                </div>
            </div>
        </article>
    </li>
</template>

<template class="paragraph">
    <p class="text"></p>
</template>

<script>
    $(function () {
        var $noData = $(".articles .no-data");
        $noData.text("Loading...");
        $.post("", {action: "findArticles"}, function (articles) {
            var $ul = $(".articles ul");
            if (articles.length > 0) {
                $ul.empty();
                articles.forEach(function (article) {
                    var $li = cloneTemplate($("template.articles-li"));
                    $li.find(".title").text(article.title);
                    $li.find(".information").text("By " + article.user.login.toLowerCase() + ", " + article.timeAgo);

                    var $body = $li.find("div[class='body']");
                    article.text.split(/\n/).forEach(function (p) {
                        if (p.length > 0) {
                            var $p = cloneTemplate($("template.paragraph"));
                            $p.find(".text").text(p);
                            $body.append($p);
                        }
                    });
                    $ul.append($li);
                });
            } else {
                $noData.text("No data");
            }
        })
    })
</script>
</@c.page>
