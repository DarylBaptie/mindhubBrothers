const { createApp } = Vue;

createApp({
  data() {
    return {
      data: [],
      show: true,
      loans: [],
      accounts: [],
      accountsActive: [],
      accountId: null,
      showAlert: false,
      errorMessage: "",
      errorMessageNewAccount: "",
      showAlertNewAccount: false,
      accountType: "CURRENT",
    };
  },
  created() {
    this.loadData()
  },
  methods: {
    loadData() {
      axios({
        method: "get",
        url: '/api/clients/current',
      })
      .then((response) => {
        this.data.push(response.data)
        this.accounts = response.data.accounts
        this.accountsActive = this.accounts.filter(account => account.isActive)
        this.loans = response.data.clientloans.filter(loan => loan.active == true)
        this.changeDate(this.accounts)
        this.sortAccounts(this.data)
        this.formatLoanAmount(this.loans)
        this.formatAccountBalance(this.accounts)
      })
      .catch(error => console.log(error));
    },
    changeDate(accounts) {
        for (let account of accounts) {
            let newDate = account.creationDate;
            newDate = newDate + "Z";
            account.newDate = new Date(newDate).toLocaleDateString('en-US');
        }
    },
    sortAccounts(data) {
        for (let client of data) {
            client.accounts.sort((a, b) => {
                if(a.id < b.id){
                    return -1;
                }
                if(a.id > b.id){
                    return 1;
                }
                    return 0;
            });
        }
    },
    showBalance() {
     this.show = !this.show;
    },
    formatLoanAmount(loans) {
            for(let loan of loans) {
                loan.formattedAmount = loan.amount.toLocaleString("en-US", {
                                                              style: "currency",
                                                              currency: "USD",
                                                              maximumFractionDigits: 0,
            })
        }

  },
      formatAccountBalance(accounts) {
              for(let account of accounts) {
                  account.formattedBalance = account.balance.toLocaleString("en-US", {
                                                                style: "currency",
                                                                currency: "USD",
                                                                maximumFractionDigits: 0,
              })
          }

    },
    newAccount() {
    axios.post('/api/clients/current/accounts',`accountType=${this.accountType}`, {headers:{'content-type':'application/x-www-form-urlencoded'}})
    .then(response => {
    console.log(response)
    })
    .catch(error => {
    console.log(error)
    this.showAlertNewAccount = true;
    this.errorMessageNewAccount = error.response.data;
    }
    );

    },
            deactivateAccount() {
            axios.patch('/api/clients/current/accounts/close',`id=${this.accountId}`)
                .then(response => {
                console.log(response)
            })
            .catch((error) => {
                console.log(error);
                this.showAlert = true;
                this.errorMessage = error.response.data;
                })
            },

    reloadPage() {
        window.location = "/web/accounts.html";
    },
    logout() {
        axios.post('/api/logout')
        .then(response => {
        window.location = "/index.html";
        })
        .catch(error => console.log(error));

    },
  },
}).mount("#app");