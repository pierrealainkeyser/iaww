<template>
<v-container>
  <v-card>
    <v-card-title>Please choose an username</v-card-title>

    <v-card-text>
      <v-form v-model="valid" ref="form" @submit="login">
        <v-row>
          <v-col cols="12">
            <v-text-field v-model="user" label="Login" :rules="rules.user" />
          </v-col>
        </v-row>
        <v-row>
          <v-col cols=" 12">
            <v-btn :disabled="!valid" @click="login">Login</v-btn>
          </v-col>
        </v-row>
      </v-form>
    </v-card-text>
  </v-card>
</v-container>
</template>

<script>
export default {
  data() {
    return {
      valid: false,
      user: null,
      rules: {
        user: [
          u => !!u || 'Login is required',
        ]
      }
    };
  },
  methods: {
    login() {
      if (this.$refs.form.validate) {
        this.$store.dispatch('user/login', this.user);
        this.$router.push('/lobby');
      }
    }
  }
}
</script>
