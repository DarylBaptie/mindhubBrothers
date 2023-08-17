const { createApp } = Vue

  createApp({
    data() {
      return {
      clients: [],
      cards: [],
      showData: true,
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
      this.formatThruDate(this.cards);
      this.cardGrouping(this.clients);
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
  }
  }).mount('#app')

