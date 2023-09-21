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
      accountsActive: [],
      installmentAmountShow: 0,
      destinedAccount: "",
      errorMessage: "",
      showAlert: false,
      loanId: 0,
      paymentAmount: 0,
      accountForPayment: 0,
      repaymentLoanName: "",
      paymentType: "",
      installmentAmount: 0,
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
            this.accountsActive = this.accounts.filter(account => account.isActive == true)
            this.clientLoans = response.data.clientloans.filter(loan => loan.active == true)
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
                makePayment() {
                    axios.patch('/api/clients/current/loans/loanPayment',`loanId=${this.loanId}&&accountId=${this.accountForPayment}&paymentAmount=${this.paymentAmount}`)
                  .then((response) => {
                    console.log(response)
                  })
                  .catch(error => {
                  console.log(error)
                  this.errorMessage = error.response.data;
                  this.showAlert = true;
                  });

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
        axios.post('/api/loans',{"name": this.loanName, "amount": this.loanAmount, "payments": this.paymentOption, "accountNumber": this.destinedAccount, "installmentAmount": this.installmentAmount})
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
    loanName = this.loanName;
    loans = this.loans;
    amount = this.loanAmount;
    installments = this.paymentOption;
    percentInterest = 0;
    for(loan of loans) {
    if(loan.name == loanName) {
     percentInterest = loan.interest / 100;
    }
    installmentCalculation = (Number((percentInterest * amount)) + Number(amount)) / installments;
    }
    this.installmentAmountShow = installmentCalculation.toLocaleString("en-US", {
                                                                  style: "currency",
                                                                  currency: "USD",
                                                                  maximumFractionDigits: 0,
    });
    this.installmentAmount = installmentCalculation
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

