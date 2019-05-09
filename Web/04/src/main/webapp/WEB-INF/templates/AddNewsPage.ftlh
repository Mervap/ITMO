<#import "common.ftl" as c>

<@c.page>
<div class="news-box">
    <div class="header">AddNews</div>
    <div class="body">
        <form method="post" action="/addNews">
            <input type="hidden" name="action" value="addNews">
            <div class="field">
                <div class="name">
                    <label for="text">Text of news:</label>
                </div>
                <div class="value">
                    <textarea id="text" name="text" rows="10"></textarea>
                </div>
            </div>
            <div class="button-field">
                <input type="submit" value="Add">
            </div>
        </form>
    </div>
</div>
</@c.page>
