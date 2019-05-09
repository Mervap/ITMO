<!--suppress HtmlUnknownAnchorTarget -->
<template>
    <article>
        <a class="title" href="#page=PostWithComments" @click="showPostComments()">{{post.title}}</a>
        <div class="information">By {{users[post.userId].login}}</div>
        <div class="body">{{post.text}}</div>
        <div class="footer">
            <div class="left">
                <img src="../../assets/img/voteup.png" title="Vote Up" alt="Vote Up"/>
                <span class="positive-score">+{{score}}</span>
                <img src="../../assets/img/votedown.png" title="Vote Down" alt="Vote Down"/>
            </div>
            <div class="right">
                <img src="../../assets/img/date_16x16.png" title="Publish Time" alt="Publish Time"/>
                2 days ago
                <img src="../../assets/img/comments_16x16.png" title="Comments" alt="Comments"/>
                <a href="#">68</a>
            </div>
        </div>
    </article>
</template>

<script>
    export default {
        props: ['users', 'post'],
        name: "Post",

        computed : {
            score: function () {
                return (this.post.id * 42);
            }
        },

        methods: {
            showPostComments: function () {
                this.$root.$emit("onShowPostComments", this.post);
            }
        }
    }
</script>

<style scoped>

    article {
        list-style-type: none;
    }

    article .title {
        color: var(--caption-color);
        font-weight: bold;
        font-size: 1.3rem;
        text-decoration: none;
    }

    article .information {
        color: var(--border-color);
        font-size: 0.95rem;
        margin-top: 0.3rem;
    }

    article .body {
        margin-top: 0.5rem;
        margin-left: 0.15rem;
        padding-left: 1rem;
        border-left: 4px solid #B9B9B9;
    }

    article .body :last-child {
        -webkit-margin-after: 0;
    }

    article .footer {
        margin-top: 1rem;
        border: 1px solid var(--border-color);
        border-radius: var(--border-radius);
        padding: 0.5rem;
    }

    article .footer .left, article .footer .right {
        display: inline;
    }

    article .footer .right {
        float: right;
        font-size: 0.96rem;
        margin: 0.1rem;
    }

    article .footer .positive-score {
        font-weight: bold;
        color: green;
    }

    article .footer .left *, .right * {
        vertical-align: middle;
    }

    article .footer .right > img {
        position: relative;
        top: -0.1rem;
        margin-left: 0.5rem;
    }
</style>
