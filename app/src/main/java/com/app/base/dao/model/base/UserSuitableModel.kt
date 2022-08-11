package com.app.base.dao.model.base


class UserSuitableModel : ResponeBase() {

  /**  {
        "error": false,
        "items": [
        {
            "id": 2232,
            "name": "Độ",
            "phone": "0986301996",
            "email": "email@abc.com",
            "uniqid": "TT2236",
            "address": "Bắc Ninh",
            "avatarURL": "",
            "transaction_limit": 988000
        }
        ],
        "index": 10
    }
  */

  var items: ArrayList<Item>? = null
    var data:Item?=null

    class Item : ProfileUserModel.Profile() {
        var transaction_limit = 0
        var transfer_date =""
        var content =""
    }
}