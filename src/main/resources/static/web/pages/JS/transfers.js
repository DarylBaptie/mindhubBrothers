const { createApp } = Vue;

createApp({
  data() {
    return {
    amount: "",
    description: "",
    accountNumber: "",
    destinationAccount: "",
    client: [],
    accounts: [],
    errorMessage: "",
    };
  },
    created() {
      this.accountsOfClient();
    },
  methods: {
        transfer(event) {
        event.preventDefault();
        axios.post('/api/clients/current/transactions',`amount=${this.amount}&description=${this.description}&accountNumberOrigin=${this.accountNumber}&accountNumberDestination=${this.destinationAccount}`,{headers:{'content-type':'application/x-www-form-urlencoded'}})
        .then(function (response) {
          console.log(response);
          window.location = "/transfers.html";

        })
        .catch(function (error) {
          console.log(error);
          this.errorMessage = error.response.data

        });
    },
        accountsOfClient() {
            axios({
                method: "get",
                url: "/api/clients/current",
            }).then((response) => {
                this.client.push(response.data);
                this.accounts = response.data.accounts;
                });
        },
  },
}).mount("#app");






