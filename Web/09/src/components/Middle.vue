<template>
    <div class="middle">
        <Sidebar :posts="posts" :users="users"/>
        <main>
            <Index v-if="page === 'Index'" :users="users" :posts="posts"/>
            <Enter v-if="page === 'Enter'"/>
            <Users v-if="page === 'Users'" :users="users"/>
            <Register v-if="page === 'Register'"/>
            <AddPost v-if="page === 'AddPost'"/>
            <EditPost v-if="page === 'EditPost'"/>
            <PostWithComments v-if="page === 'PostWithComments'" :users="users" :post="post" :comments="comments"/>
        </main>
    </div>
</template>
<script>
    import Index from './middle/Index';
    import Enter from './middle/Enter';
    import Register from './middle/Register';
    import AddPost from './middle/AddPost';
    import Sidebar from './Sidebar';
    import EditPost from "./middle/EditPost";
    import Users from "./middle/Users";
    import PostWithComments from "./middle/PostWithComments"

    export default {
        name: "Middle",
        props: ['users', 'posts', 'comments'],
        data: function () {
            return {
                page: "Index",
                post: null
            }
        },
        components: {
            EditPost,
            Index,
            Enter,
            Register,
            Sidebar,
            AddPost,
            Users,
            PostWithComments
        }, beforeCreate() {
            this.$root.$on("onChangePage", (page) => {
                this.page = page;
            });

            this.$root.$on("onShowPostComments", (post) => {
                this.post = post;
                this.page = "PostWithComments";
            });
        }
    }
</script>

<style scoped>

</style>
