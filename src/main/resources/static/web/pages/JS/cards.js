const { createApp } = Vue

  createApp({
    data() {
      return {
      clients: [],
      cards: [],
      cardsActive: [],
      accounts: [],
      showData: true,
      cardType: "DEBIT",
      cardColor: "SILVER",
      debitCards: 0,
      creditCards: 0,
      errorMessage: "",
      showAlert: false,
      cardId: null,
      dateNow: new Date().toISOString().slice(0, 10),
      eliminateCardNumber: "",
      errorMessageDeleteCard: "",
      showAlertDeleteCard: false,
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
      this.cardsActive = this.cards.filter(card => card.isActive)
      this.accounts = response.data.accounts;
      this.sortAccounts(this.accounts);
      this.formatThruDate(this.cards);
      this.cardGrouping(this.clients);
      this.debitCreditCards(this.cards);
      this.date = new Date().toLocaleDateString('en-CA');
      this.formatJavaDate(this.cards);
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
    formatJavaDate(cards) {
    for (card of cards) {
    card.newFormatDate = Date(card.fromDate).toLocaleString('en-CA')
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
        if (card.cardType == "DEBIT" && card.isActive == true) {
        this.debitCards++;
        } else if (card.cardType == "CREDIT" && card.isActive == true) {
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
        deactivateCard() {
        axios.patch('/api/clients/current/cards/deactivate',`id=${this.cardId}`)
            .then(response => {
            console.log(response)
        })
        .catch((error) => {
            this.showAlertDeleteCard = true;
            this.errorMessageDeleteCard = error.response.data;
            console.log(error);
            })
        },
    reloadPage() {
             window.location = "/web/cards.html";
    }
  }
  }).mount('#app')

