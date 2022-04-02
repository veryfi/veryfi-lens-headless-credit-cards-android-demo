package com.veryfi.lens.headless.credit.cards.demo

class CardData {
    var cardNumber: String = ""
    var cardExpDate: String = ""
    var cardName: String = ""
    var cardType: String = ""
    var cardCvc: String = ""

    constructor()
    constructor(
        cardNumber: String,
        cardExpDate: String,
        cardName: String,
        cardType: String,
        cardCvc: String
    ) {
        this.cardName = cardName
        this.cardNumber = cardNumber
        this.cardExpDate = cardExpDate
        this.cardType = cardType
        this.cardCvc = cardCvc
    }
}