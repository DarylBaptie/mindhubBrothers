const { createApp } = Vue

  createApp({
    data() {
      return {
      data: [],
      accounts: [],
      }
    },
    created() {
        this.loadClientData();
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
            this.sortAccounts(this.accounts);
          })
          .catch(error => console.log(error));
        },
                sortAccounts(accounts) {
                        accounts.sort((a, b) => {
                            if(a.id < b.id){
                                return -1;
                            }
                            if(a.id > b.id){
                                return 1;
                            }
                                return 0;
                        });
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

