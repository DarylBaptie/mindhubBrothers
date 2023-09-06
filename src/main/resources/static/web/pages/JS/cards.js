const { createApp } = Vue

  createApp({
    data() {
      return {
      clients: [],
      cards: [],
      accounts: [],
      showData: true,
      cardType: "DEBIT",
      cardColor: "SILVER",
      debitCards: 0,
      creditCards: 0,
      errorMessage: "",
      showAlert: false,
      }
    },
        created() {
        this.loadData();
        },
    methods: {
        loadData() {
            axios({
                method: 'get',
                url: '/api/clients/current',
            })
    .then((response) => {
      this.clients.push(response.data);
      this.cards = response.data.cards;
      this.accounts = response.data.accounts;
      this.sortAccounts(this.accounts);
      this.formatThruDate(this.cards);
      this.cardGrouping(this.clients);
      this.debitCreditCards(this.cards);
    })
    .catch((error) => {
    console.log(error)
    });
  },
  cardColorStyle(card) {
    switch(card.cardColor) {
    case 'GOLD' :
        return "cardGold";
        break;
    case 'TITANIUM':
        return "cardTitanium";
        break;
    case 'SILVER' :
        return "cardSilver";
        break;
    }
  },
  cardBorderStyle(card) {
    switch(card.cardColor) {
    case 'GOLD' :
        return "cardBorderGold";
        break;
    case 'TITANIUM':
        return "cardBorderTitanium";
        break;
    case 'SILVER' :
        return "cardBorderSilver";
        break;
    }
  },
  formatThruDate(cards) {
  for (let card of cards) {
    let firstCut = card.thruDate.slice(2,4)
    let secondCut = card.thruDate.slice(5,7)
  card.formattedDate = firstCut + "/" + secondCut
  }
  },
  cardGrouping(clients) {
  for(client of clients) {
    client.cards.creditCards = 0;
    client.cards.debitCards = 0;
    }
    for(card of client.cards) {
    if(card.cardType == "DEBIT") {
    client.cards.debitCards++;
    } else {
    client.cards.creditCards++;;
    }
    }
  },
  showDetails() {
    this.showData = !this.showData;
    },
    logout() {
        axios.post('/api/logout')
        .then(response => {
        window.location = "/index.html";
        })
        .catch(error => console.log(error))
    },
    newCard() {
        axios.post('/api/clients/current/cards',`color=${this.cardColor}&type=${this.cardType}`, {headers:{'content-type':'application/x-www-form-urlencoded'}})
        .then(response => {
        console.log(response)
        })
         .catch((error) => {
                         console.log(error);
                         this.errorMessage = error.response.data;
                         this.showAlert = true;
                       })
    },
    debitCreditCards(cards) {
    for (card of cards) {
        if (card.cardType == "DEBIT") {
        this.debitCards++;
        } else if (card.cardType == "CREDIT") {
        this.creditCards++
        }
    }
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
    reloadPage() {
             window.location = "/web/cards.html";
    }
  }
  }).mount('#app')

