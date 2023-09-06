const { createApp } = Vue

  createApp({
    data() {
      return {
      data: [],
      clientLoans: [],
      loans: [],
      show: true,
      loanName: "",
      paymentOption: 0,
      loanAmount: 0,
      accounts: [],
      installmentAmount: 0,
      destinedAccount: "",
      errorMessage: "",
      showAlert: false,
      }
    },
    created() {
        this.loadClientData();
        this.loadLoanData();
    },
    methods: {
        loadClientData() {
          axios({
            method: "get",
            url: '/api/clients/current',
          })
          .then((response) => {
            this.data.push(response.data)
            this.accounts = response.data.accounts
            this.clientLoans = response.data.clientloans
            this.formatLoanAmount(this.clientLoans)
          })
          .catch(error => console.log(error));
        },
        loadLoanData() {
        axios({
                    method: "get",
                    url: '/api/loans',
                  })
          .then((response) => {
            this.loans = response.data
            this.formatMaxAmount(this.loans)
          })
          .catch(error => console.log(error));
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
          formatMaxAmount(loans) {
                      for(let loan of loans) {
                          loan.formattedAmount = loan.maxAmount.toLocaleString("en-US", {
                                                                        style: "currency",
                                                                        currency: "USD",
                                                                        maximumFractionDigits: 0,
                      })
                  }
            },
    showBalance() {
     this.show = !this.show;
    },

    newLoan(event) {
        axios.post('/api/loans',{"name": this.loanName, "amount": this.loanAmount, "payments": this.paymentOption, "accountNumber": this.destinedAccount})
        .then(response => {
        console.log(response)
        })
         .catch((error) => {
                         console.log(error);
                         this.errorMessage = error.response.data;
                         this.showAlert = true;
                       })
    },
    calculateInstallments() {
    amount = this.loanAmount;
    installments = this.paymentOption;
    this.installmentAmount = amount * 1.20 / installments;
    },
    reloadPage() {
        window.location = "/web/accounts.html";
    },
        logout() {
            axios.post('/api/logout')
            .then(response => {
            window.location = "/index.html";
            })
            .catch(error => console.log(error))
        },
  }
  }).mount('#app')

