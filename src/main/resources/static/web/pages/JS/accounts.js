const { createApp } = Vue;

createApp({
  data() {
    return {
      data: [],
      show: true,
    };
  },
  created() {
    this.loadData();
  },
  methods: {
    loadData() {
      axios({
        method: "get",
        url: "/api/clients/1",
      }).then(response =>
        this.data = [response.data]
      );
    },
    changeDate(data) {
        for (let client of data) {
        for (let account of client.accounts) {
        let newDate = account.creationDate;
        newDate = newDate + "Z";
        account.newDate = new Date(newDate).toLocaleDateString('en-US');
        }
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


  },
}).mount("#app");