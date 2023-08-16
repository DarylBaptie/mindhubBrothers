const { createApp } = Vue

  createApp({
    data() {
      return {
      clients: [],
      cards: [],
      }
    },
        created() {
        this.loadData();
        },
    methods: {
        loadData() {
            axios({
                method: 'get',
                url: '/api/clients/1',
            })
    .then((response) => {
      this.clients.push(response.data);
      this.cards = response.data.cards;
    });
  },
  }
  }).mount('#app')

