const { createApp } = Vue;

createApp({
  data() {
    return {
      data: [],
      show: true,
      loans: [],
      accounts: [],
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
        this.loans = response.data.clientloans
        this.changeDate(this.accounts)
        this.sortAccounts(this.data)
        this.formatLoanAmount(this.loans)
        this.formatAccountBalance(this.accounts)
      });
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
    logout() {
        axios.post('/api/logout')
        .then(response => {
        window.location = "/index.html";
        })
    }
  },
}).mount("#app");