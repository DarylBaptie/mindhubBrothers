const { createApp } = Vue

  createApp({
    data() {
      return {
            loanName: "",
            maxAmount:null,
            interestRate: null,
            checkedInstallments: [],
            showAlert: false,
            errorMessage: ""
      }
    },

    methods: {
        postLoan() {
    axios.post('/api/createLoan', {
        "name": this.loanName, "maxAmount": this.maxAmount, "interest": this.interestRate, "payments": this.checkedInstallments
  })
  .then(function (response) {
    console.log(response);
  })
  .catch((error) => {
    console.log(error);
    this.showAlert = true;
    this.errorMessage = error.response.data;
    })
  },
    cleanForm() {
        this.loanName = '';
        this.maxAmount = '';
        this.interestRate = '';
        this.checkedInstallments = '';
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

