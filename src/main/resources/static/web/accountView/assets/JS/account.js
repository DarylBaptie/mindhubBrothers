const { createApp } = Vue;

createApp({
  data() {
    return {
      data: [],
      typeTransaction: true,
      show: true,
      client:[],
      accounts: [],
      transactions:  [],
      accountNumber: 0,
    };
  },
  created() {
    this.loadData();
    this.accountsOfClient();
  },
  methods: {
    loadData() {
      let urlParams = new URLSearchParams(document.location.search);
      let idParam = urlParams.get('id');
      axios({
        method: "get",
        url: `/api/accounts/${idParam}`,
      }).then((response) => {
        this.data.push(response.data);
        this.accountNumber = idParam;
        this.changeDate(this.data);
        this.sortTransactions(this.data);
        this.formatAccountBalance(this.data);
        this.formatAmountAccount(this.data);
        this.transactions = response.data.transactions;
      })
      .catch(error => { console.log(error)
      alert("Unauthorized access")
      window.location = "/web/accounts.html";
      }
      );
;
    },
    changeDate(data) {
        for (let account of data) {
            let newDate = account.creationDate
            newDate = newDate + "Z"
            account.newDate = new Date(newDate).toLocaleDateString('en-US')
            for (let transaction of account.transactions) {
                let newDate = transaction.date
                newDate = newDate + "Z"
                transaction.newDate = new Date(newDate).toLocaleString('en-US')
            }
        }
    },
    sortTransactions(data) {
        for (let account of data) {
        account.transactions.sort((a, b) => {
            if(a.id > b.id){
                return -1;
            }
            if(a.id < b.id){
                return 1;
            }
            return 0;
        });
        }
    },
    showBalance() {
        this.show = !this.show;
    },
    accountsOfClient() {
        axios({
            method: "get",
            url: "/api/clients/current",
        }).then((response) => {
            this.client.push(response.data);
            this.changeDateClient(this.client);
            this.customerAccounts(this.client);
            this.formatAccountBalance(this.accounts);
            });
    },
    changeDateClient(client) {
        for (let customer of client) {
            for (let account of customer.accounts) {
                let newDate = account.creationDate
                newDate = newDate + "Z"
                account.newCreationDate = new Date(newDate).toLocaleDateString('en-US')
            }
        }
    },
    formatAccountBalance(data) {
        for(let account of data) {
            account.formattedBalance = account.balance.toLocaleString("en-US", {
                style: "currency",
                currency: "USD",
                maximumFractionDigits: 0,
            })
        }
    },
    formatAmountAccount(data) {
        for (let account of data) {
          for (let transaction of account.transactions) {
            transaction.formattedAmount = transaction.amount.toLocaleString("en-US", {
                maximumFractionDigits: 0,
            })
          }
        }
    },
    customerAccounts(client) {
     for (let customer of client) {
    for(account of customer.accounts) {
        this.accounts.push(account)
    }
    }
    },
        newAccount() {
        axios.post('/api/clients/current/accounts')
        .then(response => {
        window.location = "/web/accounts.html";
        })
        .catch(error => console.log(error));

        },
        logout() {
            axios.post('/api/logout')
            .then(response => {
            window.location = "/index.html";
            })
            .catch(error => console.log(error));
        }
  }
}).mount("#app");