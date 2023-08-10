const { createApp } = Vue

  createApp({
    data() {
      return {
      data: [],
      clientList: [],
          formData: {
            firstName: '',
            lastName:'',
            clientEmail: '',
          },
      }
    },
        created() {
        this.loadData();
        },
    methods: {
        loadData() {
            axios({
                method: 'get',
                url: '/api/clients',
            })
    .then((response) => {
      this.data = response.data;
      this.clientList = response.data._embedded.clients;
    });
  },
        addClient(event) {
            event.preventDefault();
            this.postClient();
            this.cleanForm()
        },
        postClient() {
axios.post('/api/clients', {
    firstName: this.formData.firstName,
    lastName: this.formData.lastName,
    clientEmail: this.formData.clientEmail,
  })
  .then(function (response) {
    console.log(response);
  })
  .catch(function (error) {
    console.log(error);
  });
},
    cleanForm() {
        this.formData.firstName = '';
        this.formData.lastName = '';
        this.formData.clientEmail = '';
        }
    }
  }).mount('#app')

