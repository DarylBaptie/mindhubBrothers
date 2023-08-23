const { createApp } = Vue;

createApp({
  data() {
    return {
        firstName: "",
        lastName: "",
        email: "",
        password: "",
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
        .catch(function (error) {
          alert("Email already registered or incomplete data. Please login or complete fields")
        });
    }
  },
}).mount("#app");






