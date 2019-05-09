<template>
    <section>
        <div class="header">
            By {{users[post.userId].login}}
        </div>
        <div class="body">
            {{post.text}}
        </div>
        <div class="footer">
            <a href="#">View all</a>
        </div>
    </section>

</template>

<script>
    export default {
        props: ['users', 'post'],
        name: "SidebarPost"
    }
</script>

<style scoped>

</style>
