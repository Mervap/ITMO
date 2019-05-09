<template>
    <div class="register form-box">
        <div class="header">Register</div>
        <div class="body">
            <form @submit.prevent="onRegister">
                <div class="field">
                    <div class="name">
                        <label for="login">Login</label>
                    </div>
                    <div class="value">
                        <input id="login" name="login" v-model="login"/>
                    </div>
                </div>

                <div class="field">
                    <div class="name">
                        <label for="name">Name</label>
                    </div>
                    <div class="value">
                        <input id="name" name="name" v-model="name"/>
                    </div>
                </div>

                <div class="field">
                    <div class="name">
                        <label for="password">Password</label>
                    </div>
                    <div class="value">
                        <input id="password" name="password" v-model="password"/>
                    </div>
                </div>

                <div class="error">{{error}}</div>

                <div class="button-field">
                    <input type="submit" value="Register">
                </div>
            </form>
        </div>
    </div>
</template>

<script>

    import axios from 'axios';

    export default {
        data: function () {
            return {
                login: "",
                name: "",
                password: "",
                error: ""
            }
        },
        name: "Register",
        beforeMount() {
            this.login = "";
            this.password = "";
            this.error = "";
        },
        methods: {
            onRegister: function () {
                this.error = "";
                axios.post("users", {
                    login: this.login,
                    password: this.password,
                    name: this.name
                }).then(response => {
                    this.$root.$emit("onRegister", response.data);
                }).catch(error => {
                    if (error.response.status === 400) {
                        this.error = error.response.statusText;
                    } else if (error.response.status === 404) {
                        this.error = "Invalid login/password.";
                    } else {
                        this.error = "Unexpected error.";
                    }
                });
            }
        }
    }
</script>

<style scoped>

</style>
