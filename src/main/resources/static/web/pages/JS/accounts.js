const { createApp } = Vue;

createApp({
  data() {
    return {
      data: [],
      clients: [],
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
      }).then((response) => {
        this.data = [response.data];
      });
    },
    changeDate(data) {
        for (let client of data) {
        for (let account of client.accounts) {
        let newDate = account.creationDate
        newDate = newDate + "Z"
        account.newDate = new Date(newDate).toLocaleDateString('en-US')
        }
        }
    }
  }
}).mount("#app");