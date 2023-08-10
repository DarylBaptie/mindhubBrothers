const { createApp } = Vue;

createApp({
  data() {
    return {
      data: [],
      typeTransaction: true,
      show: true,
      client:[],
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
        this.data = [response.data];
        this.accountNumber = idParam;
      });
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
                 url: "/api/clients/1",
                  }).then(response =>
                  this.client = [response.data]
                    );

                  },

        changeDateClient(client) {
                for (let customer of client) {
                for (account of customer.accounts) {
                        let newDate = account.creationDate
                        newDate = newDate + "Z"
                        account.newCreationDate = new Date(newDate).toLocaleDateString('en-US')

                }
                }
        }
    }
}).mount("#app");