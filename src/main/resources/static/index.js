const { createApp } = Vue;

createApp({
  data() {
    return {
        email: "",
        password: "",
    };
  },
  methods: {
          clientLogin(event) {
              email = this.email;
        event.preventDefault();
        axios.post('/api/login',`email=${this.email}&password=${this.password}`,{headers:{'content-type':'application/x-www-form-urlencoded'}})
        .then(function (response) {
          console.log(response);
          if (this.email == "admin@mindhub.com") {
            window.location = "/manager.html";
          } else {
            window.location = "/web/accounts.html";
          }
        })
        .catch(function (error) {
          console.log(error);
          alert("Email or username incorrect, please re-enter your client details")
        });
        this.email = ""
        this.password = ""
    },
  },
}).mount("#app");






