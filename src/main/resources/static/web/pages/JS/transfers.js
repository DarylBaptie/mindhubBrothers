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
    transferDestination: "",
    showAlert: false,
    };
  },
    created() {
      this.accountsOfClient();
    },
  methods: {
        transfer(event) {
        event.preventDefault();
        axios.post('/api/clients/current/transactions',`amount=${this.amount}&description=${this.description}&accountNumberOrigin=${this.accountNumber}&accountNumberDestination=${this.destinationAccount}&accountDestinationType=${transferDestination}`,{headers:{'content-type':'application/x-www-form-urlencoded'}})
        .then((response) => {
          console.log(response);
        })
        .catch((error) => {
          console.log(error);
          this.errorMessage = error.response.data;
          this.showAlert = true;
        });
    },
        accountsOfClient() {
            axios({
                method: "get",
                url: "/api/clients/current",
            }).then((response) => {
                this.client.push(response.data);
                this.accounts = response.data.accounts;
                this.sortAccounts(this.accounts);
                });
        },
        reloadPage() {
            window.location = "/web/transfers.html";
        },
        logout() {
            axios.post('/api/logout')
            .then(response => {
            window.location = "/index.html";
            })
            .catch(error => console.log(error))
        },
        sortAccounts(accounts) {
                accounts.sort((a, b) => {
                    if(a.balance < b.balance){
                        return -1;
                    }
                    if(a.balance > b.balance){
                        return 1;
                    }
                        return 0;
                });

        },
  },
}).mount("#app");






