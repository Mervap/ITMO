<template>
    <div class="article">
        <Post :users="users" :post="post"/>
        <div v-if="filteredComments.length > 0" class="comments">
            <section v-for="comment in filteredComments" :key="comment.id">
                <div class="body">
                    {{comment.text}}
                </div>
                <div class="author">
                    by {{users[comment.userId].login}}
                </div>
            </section>
        </div>
        <div v-else class="no-comments">
            No comments
        </div>
    </div>
</template>

<script>

    import Post from './Post';

    export default {
        props: ['users', 'post', 'comments'],
        name: "PostWithComments",

        components: {
            Post: Post
        },

        computed: {
            filteredComments: function () {
                return Object.values(this.comments).filter(a => a.postId === this.post.id);
            }
        }
    }
</script>

<style scoped>
    .comments section {
        margin: 1rem 0;
        border-bottom: 1px solid var(--border-color);
    }

    .comments section .author, .no-comments {
        margin: 0.2rem 0;
        font-size: 0.85rem;
        color: #B9B9B9;
    }
</style>
