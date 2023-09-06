const { createApp } = Vue;

createApp({
  data() {
    return {
        firstName: "",
        lastName: "",
        email: "",
        password: "",
        errorMessage: "",
        showAlert: false,
    };
  },
  methods: {
    clientRegistration(event) {
    email = this.email;
    password = this.password;
        event.preventDefault();
         axios.post('/api/clients',`firstName=${this.firstName}&lastName=${this.lastName}&email=${this.email}&password=${this.password}`,{headers:{'content-type':'application/x-www-form-urlencoded'}})
        .then(function (response) {
                  axios.post('/api/login',`email=${this.email}&password=${this.password}`,{headers:{'content-type':'application/x-www-form-urlencoded'}})
                  .then(function (response) {
                    console.log(response);
                    window.location = "/web/accounts.html";
                     })
        })
        .catch((error) => {
        console.log(error);
        this.errorMessage = error.response.data
        this.showAlert = true
        });
    }
  },
}).mount("#app");






