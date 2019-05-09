<template>
    <div class="datalist articles">
        <Post v-for="post in reversePosts" :users="users" :post="post" :key="post.id"/>
    </div>
</template>

<script>
    import Post from './Post';

    export default {
        props: ["users", "posts"],
        name: "Index",

        components: {
            Post: Post
        },

        computed: {
            reversePosts: function () {
                return Object.values(this.posts).slice().reverse()
            }
        }
    }
</script>

<style scoped>
    .articles > article {
        margin-bottom: 2rem;
    }
</style>
