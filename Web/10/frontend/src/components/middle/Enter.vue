<template>
    <div class="enter form-box">
        <div class="header">Enter</div>
        <div class="body">
            <form @submit.prevent="onEnter">
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
                        <label for="password">Password</label>
                    </div>
                    <div class="value">
                        <input id="password" type="password" name="password" v-model="password"/>
                    </div>
                </div>

                <div class="error">{{error}}</div>

                <div class="button-field">
                    <input type="submit" value="Enter">
                </div>
            </form>
        </div>
    </div>
</template>

<script>
    import axios from 'axios';

    export default {
        data: function() {
            return {
                login: "",
                password: "",
                error: ""
            }
        },
        name: "Enter",
        beforeMount() {
            this.login = "";
            this.password = "";
            this.error = "";
        }, methods: {
            onEnter: function () {
                this.error = "";
                axios.get("jwt", {
                    params: {
                        login: this.login,
                        password: this.password
                    }
                }).then(response => {
                    this.$root.$emit("onEnter", response.data);
                }).catch(error => {
                    if (error.response.status === 404) {
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
