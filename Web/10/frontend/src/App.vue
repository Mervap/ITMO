<template>
    <!--suppress HtmlUnknownTag -->
    <body id="app">
    <Header :user="user"/>
    <Middle :users="users" :posts="posts"/>
    <Footer/>
    </body>
</template>

<script>
    import axios from 'axios';
    import Header from './components/Header'
    import Middle from './components/Middle'
    import Footer from './components/Footer'

    axios.defaults.baseURL = '/api/1/';

    export default {
        name: 'app',
        data: function () {
            return this.$root.$data;
        },
        components: {
            Header,
            Middle,
            Footer
        }, beforeCreate() {
            this.$root.$on("onLogout", () => {
                localStorage.removeItem("token");
                this.user = null;
                axios.defaults.headers = {};
            });
            this.$root.$on("onEnter", (token) => {
                localStorage.setItem("token", token);
                axios.defaults.headers = {
                    Authorization: "Bearer " + token
                };
                axios.get("users/authenticated").then(response => {
                    this.user = response.data;
                    this.$root.$emit("onEnterSuccess");
                });
            });
            this.$root.$on("onAddPost", (title, text) => {
                if (this.userId) {
                    if (!title || title.length > 5) {
                        this.$root.$emit("onAddPostValidationError", "Title is invalid");
                    } else if (!text || text.length > 10) {
                        this.$root.$emit("onAddPostValidationError", "Text is invalid");
                    } else {
                        const id = Math.max(...Object.keys(this.posts)) + 1;
                        this.$set(this.posts, id, {
                            id,
                            userId: this.userId,
                            title,
                            text
                        })
                    }
                } else {
                    this.$root.$emit("onAddPostValidationError", "No access");
                }
            });
            this.$root.$on("onEditPost", (id, text) => {
                if (this.userId) {
                    if (!id) {
                        this.$root.$emit("onEditPostValidationError", "ID is invalid");
                    } else if (!text || text.length > 10) {
                        this.$root.$emit("onEditPostValidationError", "Text is invalid");
                    } else {
                        let posts = Object.values(this.posts).filter(p => p.id === parseInt(id));
                        if (posts.length) {
                            posts.forEach((item) => {
                                item.text = text;
                            });
                        } else {
                            this.$root.$emit("onEditPostValidationError", "No such post");
                        }
                    }
                } else {
                    this.$root.$emit("onEditPostValidationError", "No access");
                }
            });
        }, beforeMount() {
            if (localStorage.getItem("token") && !this.user) {
                this.$root.$emit("onEnter", localStorage.getItem("token"));
            }
        }
    }
</script>

<style>
</style>
